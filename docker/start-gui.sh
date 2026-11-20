# shellcheck shell=bash
# Sourced by entrypoint.sh when MODE=gclient: gets a display ready for JavaFX and exports DISPLAY.
#   DISPLAY_MODE=novnc  virtual screen (Xvfb) shown in the browser through noVNC
#   DISPLAY_MODE=x11    window on the host's X server (XQuartz, VcXsrv/WSLg, Linux)
# Uses die/summary from entrypoint.sh.

GUI_SIZE="${GUI_SIZE:-1024x768}"   # every scene of the game is 1024x768
NOVNC_PORT="${NOVNC_PORT:-6080}"   # host port published by compose/run, only used in messages

wait_until() {  # wait_until <seconds> <command...>
  local tries=$(( $1 * 10 ))
  shift
  while (( tries-- > 0 )); do "$@" && return 0; sleep 0.1; done
  return 1
}

port_open() { timeout 2 bash -c "exec 3<>/dev/tcp/$1/$2" 2>/dev/null; }

start_novnc() {
  export DISPLAY=:99
  Xvfb "$DISPLAY" -screen 0 "${GUI_SIZE}x24" -nolisten tcp >/tmp/xvfb.log 2>&1 &
  wait_until 10 test -S /tmp/.X11-unix/X99 || die "Xvfb non è partito: $(tail -3 /tmp/xvfb.log)"
  x11vnc -display "$DISPLAY" -localhost -rfbport 5900 -forever -shared -nopw -xkb -quiet \
    -desktop "Codex Naturalis" >/tmp/x11vnc.log 2>&1 &
  wait_until 10 port_open 127.0.0.1 5900 || die "x11vnc non è partito: $(tail -3 /tmp/x11vnc.log)"
  PYTHONPATH=/opt/websockify PYTHONWARNINGS=ignore \
    python3 -m websockify --web /opt/novnc 6080 localhost:5900 >/tmp/websockify.log 2>&1 &
  wait_until 10 port_open 127.0.0.1 6080 || die "noVNC non è partito: $(tail -3 /tmp/websockify.log)"
  summary "Client grafico pronto" "Apri  http://localhost:$NOVNC_PORT  nel browser"
}

x11_reachable() {
  local host=${DISPLAY%:*} num=${DISPLAY##*:}
  num=${num%%.*}
  [[ $num =~ ^[0-9]+$ ]] || return 1
  if [[ -z $host || $host == unix ]]; then
    test -S "/tmp/.X11-unix/X$num"
  else
    port_open "$host" $(( 6000 + num ))
  fi
}

x11_help() {
  cat >&2 <<EOF
Errore: Server X non raggiungibile su DISPLAY=$DISPLAY

  macOS    avvia XQuartz; in Impostazioni > Sicurezza attiva "Consenti connessioni dai client
           di rete", riavvia XQuartz, poi nel Terminale:  xhost +localhost
  Windows  avvia VcXsrv (XLaunch) con "Disable access control", oppure usa WSLg
  Linux    xhost +local:   e avvia il container con  -e DISPLAY -v /tmp/.X11-unix:/tmp/.X11-unix

In alternativa gioca nel browser:  DISPLAY_MODE=novnc
EOF
  exit 64
}

use_host_x11() {
  # macOS exports DISPLAY as a launchd socket path, useless inside the container
  [[ ${DISPLAY:-} == */org.xquartz:* ]] && DISPLAY="host.docker.internal:${DISPLAY##*:}"
  export DISPLAY="${DISPLAY:-host.docker.internal:0}"
  x11_reachable || x11_help
  summary "Client grafico" "La finestra si apre sul tuo desktop (DISPLAY=$DISPLAY)"
}

prepare_display() {
  case "${DISPLAY_MODE,,}" in
    novnc) start_novnc ;;
    x11)   use_host_x11 ;;
    *) die "DISPLAY_MODE non valido: '$DISPLAY_MODE' (usa novnc oppure x11)" ;;
  esac
}
