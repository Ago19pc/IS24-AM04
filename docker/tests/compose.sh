#!/usr/bin/env bash
# Compose test: server + players, started the way the README tells players to.
# Run from anywhere:  docker/tests/compose.sh
# Optional env:  COMPOSE_FILE=path  (default docker/compose.yaml; the release one is
#                                   docker/release/compose.yaml)
# Uses its own project name, so it never touches a real game's volume.
set -uo pipefail

ROOT="$(cd "$(dirname "$0")/../.." && pwd)"
COMPOSE_FILE="${COMPOSE_FILE:-$ROOT/docker/compose.yaml}"
PROJECT=codex-compose-test
NOVNC_TEST_PORT="${NOVNC_TEST_PORT:-16090}"
NOVNC_TEST_RANGE="${NOVNC_TEST_RANGE:-16091-16095}"
dc() { docker compose -f "$COMPOSE_FILE" -p "$PROJECT" "$@"; }

pass=0; fail=0
ok()   { printf '  \033[32m✔ %s\033[0m\n' "$1"; pass=$((pass+1)); }
ko()   { printf '  \033[31m✘ %s\033[0m\n' "$1"; fail=$((fail+1)); }
step() { printf '\n\033[1m▶ %s\033[0m\n' "$1"; }

cleanup() { dc --profile client down -v --remove-orphans >/dev/null 2>&1; }
trap cleanup EXIT

running() { [[ "$(docker inspect -f '{{.State.Running}}' "$1" 2>/dev/null)" == "true" ]]; }
wait_for_log() {  # wait_for_log <container> <fixed-string> <timeout-seconds>
  local c="$1" needle="$2" t="$3" i
  [[ -n $c ]] || return 1
  for ((i = 0; i < t; i++)); do
    docker logs "$c" 2>&1 | grep -F -- "$needle" >/dev/null && return 0
    running "$c" || return 1
    sleep 1
  done
  return 1
}
run_client() { dc run -d "$@" client 2>/dev/null | tail -1; }  # prints the container name

cleanup

# ---------------------------------------------------------------- 1. file
step "1. Compose file"
dc config -q 2>/dev/null && ok "${COMPOSE_FILE#$ROOT/} is valid" || ko "${COMPOSE_FILE#$ROOT/} missing or invalid"

# ---------------------------------------------------------------- 2. server
step "2. Server service"
dc up -d --build server >/dev/null 2>&1 && ok "docker compose up -d server" || ko "server did not start"
srv="$(dc ps -q server 2>/dev/null)"
wait_for_log "$srv" "[Socket] Server avviato sulla porta: 1234" 180 \
  && ok "server listening (RMI 1099, Socket 1234)" || ko "server not listening"
if docker logs "$srv" 2>&1 | grep -F "Non esiste nessuna partita salvata" >/dev/null; then
  ok "GAME_MODE=l by default (no save yet -> new game)"
else
  ko "server did not try to resume a save"; docker logs "$srv" 2>&1 | grep -v Pinging | head -12
fi
[[ "$(docker inspect -f '{{.HostConfig.RestartPolicy.Name}}' "$srv" 2>/dev/null)" == "unless-stopped" ]] \
  && ok "restart: unless-stopped" || ko "no automatic restart"
dc port server 1234 2>/dev/null | grep -q '^0\.0\.0\.0:1234$' \
  && ok "Socket port published to the LAN" || ko "Socket port not published on all interfaces"
# an exposed but unpublished port is reported as ":0"
dc port server 1099 2>/dev/null | grep -q ':[1-9]' \
  && ko "RMI port should not be published" || ok "RMI stays inside the Docker network"
docker inspect -f '{{range .Mounts}}{{.Name}}:{{.Destination}} {{end}}' "$srv" 2>/dev/null \
  | grep -q "${PROJECT}_codex-data:/data" && ok "saves live in the codex-data volume" || ko "no codex-data volume on /data"

# ---------------------------------------------------------------- 3. TUI players
step "3. TUI players reach the server by its service name"
c1="$(run_client -e MODE=tclient -e CONNECTION=rmi)"
wait_for_log "$c1" "Connessione avvenuta con successo" 90 \
  && ok "TUI player joins via RMI" || ko "TUI player could not join via RMI"
c2="$(run_client -e MODE=tclient -e CONNECTION=socket)"
wait_for_log "$c2" "Connessione avvenuta con successo" 90 \
  && ok "TUI player joins via Socket" || ko "TUI player could not join via Socket"

# ---------------------------------------------------------------- 4. GUI player
step "4. GUI player in the browser on a chosen port (NOVNC_PORT)"
c3="$(NOVNC_PORT=$NOVNC_TEST_PORT run_client --service-ports -e MODE=gclient)"
code=000
for _ in $(seq 1 90); do
  code="$(curl -s -o /dev/null -w '%{http_code}' "http://127.0.0.1:$NOVNC_TEST_PORT/vnc.html" || true)"
  [[ $code == 200 ]] && break
  running "$c3" || break
  sleep 1
done
[[ $code == 200 ]] && ok "noVNC on http://127.0.0.1:$NOVNC_TEST_PORT" || ko "noVNC not reachable (HTTP $code)"
docker port "$c3" 6080 2>/dev/null | grep -q "^127\.0\.0\.1:$NOVNC_TEST_PORT$" \
  && ok "noVNC published on localhost only" || ko "noVNC not bound to 127.0.0.1"
docker logs "$c3" 2>&1 | grep -F "http://localhost:$NOVNC_TEST_PORT" >/dev/null \
  && ok "the message shows the right URL" || ko "the message shows the wrong URL"

# ---------------------------------------------------------------- 5. several clients
step "5. Several GUI clients on the same computer (automatic browser ports)"
NOVNC_PORT='' dc --profile client config 2>/dev/null | grep -E 'published: "?6080-6089"?$' >/dev/null \
  && ok "by default the browser port comes from the range 6080-6089" \
  || ko "the browser port is fixed by default"
# --no-deps: a GUI client shows its first screen without a server
g1="$(NOVNC_PORT=$NOVNC_TEST_RANGE run_client --no-deps --service-ports -e MODE=gclient)"
g2="$(NOVNC_PORT=$NOVNC_TEST_RANGE run_client --no-deps --service-ports -e MODE=gclient)"
p1="$(docker port "$g1" 6080 2>/dev/null)"; p2="$(docker port "$g2" 6080 2>/dev/null)"
[[ -n $p1 && -n $p2 && $p1 != "$p2" ]] \
  && ok "two GUI clients started, on $p1 and $p2" || ko "second GUI client did not start (ports: '$p1' '$p2')"
for p in "$p1" "$p2"; do
  code=000
  for _ in $(seq 1 90); do
    code="$(curl -s -o /dev/null -w '%{http_code}' "http://$p/vnc.html" || true)"
    [[ $code == 200 ]] && break
    sleep 1
  done
  [[ $code == 200 ]] && ok "noVNC answers on $p" || ko "noVNC not reachable on '$p' (HTTP $code)"
done
if docker info --format '{{.OperatingSystem}}' 2>/dev/null | grep -F 'Docker Desktop' >/dev/null; then
  # Docker Desktop lets a container reach its own published port: each client finds its URL
  for pair in "$g1 $p1" "$g2 $p2"; do
    set -- $pair
    docker logs "$1" 2>&1 | grep -F "http://localhost:${2##*:}" >/dev/null \
      && ok "client on ${2##*:} shows its exact URL" || ko "client on '${2##*:}' does not show its URL"
  done
else
  docker logs "$g2" 2>&1 | grep -F "docker port" >/dev/null \
    && ok "the message tells how to find this client's port" || ko "the message does not tell how to find the port"
fi

echo
echo "RESULT: $pass passed, $fail failed"
[[ $fail -eq 0 ]]
