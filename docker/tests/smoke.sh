#!/usr/bin/env bash
# Smoke test for the Codex Naturalis Docker image.
# Run from anywhere:  docker/tests/smoke.sh
# Optional env:  PLATFORM=linux/amd64  (test the emulated/prebuilt-jar path)
#                SKIP_BUILD=1          (reuse an already built image)
#                IMAGE=name:tag        (image to build/test, default codex-naturalis:smoke)
#                PFX=name              (container/network prefix, to run two suites in parallel)
#                NOVNC_HOST_PORT=port  (host port for the noVNC check, default 16080)
#                BUILDER=name          (build with this buildx builder, e.g. a docker-container
#                                       one, which runs the same recent BuildKit as the CI)
set -uo pipefail

ROOT="$(cd "$(dirname "$0")/../.." && pwd)"
IMAGE="${IMAGE:-codex-naturalis:smoke}"
PLATFORM="${PLATFORM:-}"
PFX="${PFX:-codex-smoke}"
NET="$PFX-net"
VOL="$PFX-data"
SERVER="$PFX-server"
MENU="$PFX-menu"
TUI_SOCKET="$PFX-tui-socket"
TUI_RMI="$PFX-tui-rmi"
GUI="$PFX-gui"
XSRV="$PFX-xserver"
GUI_X11="$PFX-gui-x11"
NOVNC_HOST_PORT="${NOVNC_HOST_PORT:-16080}"
SOCKET_PORT=1234
RMI_PORT=1099

PLATFORM_ARGS=()
[[ -n "$PLATFORM" ]] && PLATFORM_ARGS=(--platform "$PLATFORM")
drun() { docker run ${PLATFORM_ARGS[@]+"${PLATFORM_ARGS[@]}"} "$@"; }

pass=0; fail=0
ok()   { printf '  \033[32m✔ %s\033[0m\n' "$1"; pass=$((pass+1)); }
ko()   { printf '  \033[31m✘ %s\033[0m\n' "$1"; fail=$((fail+1)); }
step() { printf '\n\033[1m▶ %s\033[0m\n' "$1"; }

cleanup() {
  docker rm -f "$SERVER" "$MENU" "$TUI_SOCKET" "$TUI_RMI" "$GUI" "$XSRV" "$GUI_X11" >/dev/null 2>&1
  docker network rm "$NET" >/dev/null 2>&1
  docker volume rm "$VOL" >/dev/null 2>&1
}
trap cleanup EXIT

running() { [[ "$(docker inspect -f '{{.State.Running}}' "$1" 2>/dev/null)" == "true" ]]; }

# has_process <container> <regex>: retried, `docker top` can come back empty under heavy load
has_process() {
  local i
  for i in 1 2 3 4 5; do
    docker top "$1" -o pid,args 2>/dev/null | grep -E -- "$2" >/dev/null && return 0
    sleep 1
  done
  return 1
}

# wait_for_log <container> <fixed-string> <timeout-seconds>
# Fails fast when the container has exited without printing the line.
wait_for_log() {
  local c="$1" needle="$2" t="$3" i
  for ((i = 0; i < t; i++)); do
    docker logs "$c" 2>&1 | grep -F -- "$needle" >/dev/null && return 0
    running "$c" || return 1
    sleep 1
  done
  return 1
}

# count_log <container> <fixed-string>: how many log lines contain it
count_log() { docker logs "$1" 2>&1 | grep -cF -- "$2"; }

# wait_for_more <container> <fixed-string> <count-before> <timeout-seconds>
wait_for_more() {
  local i
  for ((i = 0; i < $4; i++)); do
    (( $(count_log "$1" "$2") > $3 )) && return 0
    sleep 1
  done
  return 1
}

start_server() {
  drun -d --name "$SERVER" --network "$NET" -v "$VOL:/data" \
    -e MODE=host -e GAME_MODE=n -e SOCKET_PORT=$SOCKET_PORT -e RMI_PORT=$RMI_PORT \
    "$IMAGE" >/dev/null
}

cleanup
docker network create "$NET" >/dev/null
docker volume create "$VOL" >/dev/null

# ---------------------------------------------------------------- 1. build
step "1. Build image"
if [[ -z "${SKIP_BUILD:-}" ]]; then
  build=(docker build)
  [[ -n "${BUILDER:-}" ]] && build=(docker buildx build --builder "$BUILDER" --load)
  if "${build[@]}" ${PLATFORM_ARGS[@]+"${PLATFORM_ARGS[@]}"} -f "$ROOT/docker/Dockerfile" -t "$IMAGE" "$ROOT"; then
    ok "image built"
  else
    ko "image build failed"; echo; echo "RESULT: $pass passed, $fail failed"; exit 1
  fi
else
  ok "build skipped (SKIP_BUILD)"
fi

# ---------------------------------------------------------------- 2. entrypoint errors
step "2. Entrypoint: clear errors instead of hanging"
out="$(drun --rm "$IMAGE" 2>&1)"; rc=$?
[[ $rc -eq 64 ]] && ok "no MODE and no terminal -> exit 64" || ko "no MODE and no terminal -> exit $rc (expected 64)"
grep -qF 'MODE=host' <<<"$out" && ok "usage text explains MODE" || ko "usage text missing"
out="$(drun --rm -e MODE=bogus "$IMAGE" 2>&1)"; rc=$?
[[ $rc -eq 64 ]] && grep -qi 'MODE non valido' <<<"$out" \
  && ok "invalid MODE rejected" || ko "invalid MODE not rejected (exit $rc)"
docker inspect -f '{{json .Config.Entrypoint}}' "$IMAGE" | grep -q tini \
  && ok "tini is PID 1" || ko "tini is not the entrypoint"
drun --rm --entrypoint sh "$IMAGE" -c \
  'test -r /usr/local/lib/codex/autorespond.py && test -r /usr/local/lib/codex/start-gui.sh' \
  && ok "the app user can read the helper scripts" || ko "the app user cannot read /usr/local/lib/codex"

# ---------------------------------------------------------------- 3. menu
step "3. Menu shows up when a terminal is attached"
drun -d -t --name "$MENU" "$IMAGE" >/dev/null
wait_for_log "$MENU" "IS24-AM04" 30 && ok "banner shown" || ko "banner missing"
wait_for_log "$MENU" "Cosa vuoi avviare" 10 && ok "main menu shown" || ko "main menu missing"
docker rm -f "$MENU" >/dev/null 2>&1

# ---------------------------------------------------------------- 4. headless host
step "4. Headless host (MODE=host, no terminal)"
start_server
wait_for_log "$SERVER" "[RMI] Server avviato sulla porta: $RMI_PORT" 60 \
  && ok "RMI listening on $RMI_PORT" || ko "RMI not started on $RMI_PORT"
wait_for_log "$SERVER" "[Socket] Server avviato sulla porta: $SOCKET_PORT" 30 \
  && ok "Socket listening on $SOCKET_PORT" || ko "Socket not started on $SOCKET_PORT"
[[ "$(docker inspect -f '{{.State.Running}}' "$SERVER" 2>/dev/null)" == "true" ]] \
  && ok "server still running" || ko "server exited"

# ---------------------------------------------------------------- 5. persistence
step "5. Persistence on /data volume"
[[ "$(docker inspect -f '{{.Config.WorkingDir}}' "$IMAGE")" == "/data" ]] \
  && ok "working dir is /data (game.json lands on the volume)" || ko "working dir is not /data"
docker exec "$SERVER" sh -c 'echo probe > /data/.smoke-probe' 2>/dev/null \
  && ok "/data writable by app user" || ko "/data not writable"
docker rm -f "$SERVER" >/dev/null
drun --rm -v "$VOL:/data" --entrypoint cat "$IMAGE" /data/.smoke-probe 2>/dev/null \
  | grep -q probe && ok "data survives container removal" || ko "data lost after container removal"
[[ "$(drun --rm --entrypoint id "$IMAGE" -u)" != "0" ]] \
  && ok "runs as non-root" || ko "runs as root"

# restart the server for the client tests
start_server
wait_for_log "$SERVER" "[Socket] Server avviato sulla porta: $SOCKET_PORT" 60 || true

# ---------------------------------------------------------------- 6. TUI over Socket
step "6. TUI client joins via Socket (guided connection)"
drun -d --name "$TUI_SOCKET" --network "$NET" -e MODE=tclient \
  -e CONNECTION=socket -e SERVER_HOST="$SERVER" -e SERVER_PORT=$SOCKET_PORT "$IMAGE" >/dev/null
wait_for_log "$SERVER" "[Socket] Nuova connessione accettata" 60 \
  && ok "server accepted the Socket client" || ko "no Socket connection on the server"
wait_for_log "$TUI_SOCKET" "Connessione avvenuta con successo" 20 \
  && ok "client confirms the Socket connection" || ko "client did not confirm (Socket)"

# ---------------------------------------------------------------- 7. TUI over RMI
step "7. TUI client joins via RMI (server callbacks across containers)"
drun -d --name "$TUI_RMI" --network "$NET" -e MODE=tclient \
  -e CONNECTION=rmi -e SERVER_HOST="$SERVER" -e SERVER_PORT=$RMI_PORT "$IMAGE" >/dev/null
wait_for_log "$SERVER" "[RMI] Client connected" 60 \
  && ok "server registered the RMI client" || ko "no RMI client on the server"
wait_for_log "$TUI_RMI" "Connessione avvenuta con successo" 20 \
  && ok "client confirms the RMI connection" || ko "client did not confirm (RMI)"

# ---------------------------------------------------------------- 8. GUI via noVNC
step "8. GUI client via noVNC (joins the server by itself via Socket)"
socket_before=$(count_log "$SERVER" "[Socket] Nuova connessione accettata")
drun -d --name "$GUI" --network "$NET" \
  -e MODE=gclient -e DISPLAY_MODE=novnc -e CONNECTION=socket -e SERVER_HOST="$SERVER" \
  -p "127.0.0.1:$NOVNC_HOST_PORT:6080" "$IMAGE" >/dev/null
code=000
for _ in $(seq 1 60); do
  code="$(curl -s -o /dev/null -w '%{http_code}' "http://127.0.0.1:$NOVNC_HOST_PORT/vnc.html" || true)"
  [[ "$code" == "200" ]] && break
  running "$GUI" || break
  sleep 1
done
[[ "$code" == "200" ]] && ok "noVNC answers 200" || ko "noVNC not reachable (HTTP $code)"
running "$GUI" && sleep 8
has_process "$GUI" '[j]ava' \
  && ok "JavaFX process alive" || ko "JavaFX process not running"
if docker logs "$GUI" 2>&1 | grep -iE 'Exception|Error initializing|Unable to open DISPLAY|Errore' >/dev/null; then
  ko "errors in GUI logs"; docker logs "$GUI" 2>&1 | tail -20
else
  ok "no errors in GUI logs"
fi
curl -s "http://127.0.0.1:$NOVNC_HOST_PORT/" | grep -q 'autoconnect=true' \
  && ok "/ opens noVNC with autoconnect" || ko "/ does not autoconnect"
has_process "$GUI" '[X]vfb.*1024x768' \
  && ok "virtual screen is 1024x768" || ko "virtual screen is not 1024x768"
docker exec "$GUI" fc-match sans-serif 2>/dev/null | grep -q Selawik \
  && ok "default font is Selawik (Segoe UI metrics)" || ko "default font is not Selawik"
docker exec "$GUI" fc-list : fullname 2>/dev/null | grep Constantia >/dev/null \
  && ok "Caladea answers to the name Constantia" || ko "no font named Constantia"
wait_for_log "$GUI" "[codex] finestra a dimensione fissa: Welcome to Codex Naturalis!" 10 \
  && ok "game window cannot be resized" || ko "game window still resizable"
wait_for_more "$SERVER" "[Socket] Nuova connessione accettata" "$socket_before" 40 \
  && ok "GUI joined the server by itself via Socket" || ko "GUI did not join the server via Socket"
wait_for_log "$GUI" "[codex] scelta del nome" 30 \
  && ok "GUI landed on the name screen" || ko "GUI did not reach the name screen"

# ---------------------------------------------------------------- 9. GUI via X11
step "9. GUI client via X11 (another container plays the host's X server; joins via RMI)"
drun -d --name "$XSRV" --network "$NET" --entrypoint Xvfb "$IMAGE" \
  :0 -screen 0 1024x768x24 -listen tcp -ac >/dev/null
sleep 2
rmi_before=$(count_log "$SERVER" "[RMI] Client connected")
drun -d --name "$GUI_X11" --network "$NET" \
  -e MODE=gclient -e DISPLAY_MODE=x11 -e DISPLAY="$XSRV:0" -e CONNECTION=rmi -e SERVER_HOST="$SERVER" \
  "$IMAGE" >/dev/null
sleep 12
running "$GUI_X11" && has_process "$GUI_X11" '[j]ava' \
  && ok "JavaFX window opened on the X server" || ko "JavaFX did not start on X11"
wait_for_log "$GUI_X11" "[codex] finestra a dimensione fissa: Welcome to Codex Naturalis!" 10 \
  && ok "X11 window cannot be resized (no black window on XQuartz drag-resize)" \
  || ko "X11 window still resizable"
if docker logs "$GUI_X11" 2>&1 | grep -iE 'Exception|Unable to open DISPLAY|Errore' >/dev/null; then
  ko "errors in X11 GUI logs"; docker logs "$GUI_X11" 2>&1 | tail -10
else
  ok "no errors in X11 GUI logs"
fi
wait_for_more "$SERVER" "[RMI] Client connected" "$rmi_before" 40 \
  && ok "X11 GUI joined the server by itself via RMI" || ko "X11 GUI did not join the server via RMI"
wait_for_log "$GUI_X11" "[codex] scelta del nome" 30 \
  && ok "X11 GUI landed on the name screen" || ko "X11 GUI did not reach the name screen"
out="$(drun --rm -e MODE=gclient -e DISPLAY_MODE=x11 -e DISPLAY=nowhere.invalid:0 "$IMAGE" 2>&1)"; rc=$?
[[ $rc -eq 64 ]] && grep -q 'Server X non raggiungibile' <<<"$out" \
  && ok "unreachable X server -> clear error" || ko "unreachable X server not reported (exit $rc)"
grep -qE 'Collegamento automatico \(scelto a caso\): (SOCKET|RMI)' <<<"$out" \
  && ok "without CONNECTION the GUI picks Socket or RMI at random" || ko "no random connection by default"

# ---------------------------------------------------------------- 10. graceful stop
step "10. Graceful stop (signals reach the JVM)"
if [[ "$(docker inspect -f '{{.State.Running}}' "$SERVER" 2>/dev/null)" == "true" ]]; then
  t0=$SECONDS; docker stop -t 15 "$SERVER" >/dev/null; dt=$((SECONDS - t0))
  [[ $dt -lt 5 ]] && ok "server stopped in ${dt}s" || ko "server took ${dt}s to stop (signals not forwarded)"
else
  ko "server not running, cannot test stop"
fi

echo
echo "RESULT: $pass passed, $fail failed"
[[ $fail -eq 0 ]]
