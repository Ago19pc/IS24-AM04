#!/usr/bin/env bash
# Codex Naturalis — container entrypoint.
#
#   MODE set    -> start right away:  host | gclient | tclient
#   MODE empty  -> interactive menu (needs `docker run -it`)
#   first arg   -> a mode (e.g. `docker run -it codex-naturalis host`)
#                  or any command  (e.g. `docker run -it codex-naturalis bash`)
#
# The game asks its questions on stdin; autorespond.py types each answer only
# after the matching prompt shows up (every component has its own Scanner).
set -euo pipefail

APP_CLASSPATH='/opt/app/lib/*'
AUTORESPOND=/usr/local/lib/codex/autorespond.py
GUI_HELPER=/usr/local/lib/codex/start-gui.sh
SAVE_FILE=/data/game.json
WELCOME="to exit the game"   # last line of the game's own start menu

MODE="${MODE:-}"
GAME_MODE="${GAME_MODE:-n}"
RMI_PORT="${RMI_PORT:-1099}"
SOCKET_PORT="${SOCKET_PORT:-1234}"
CONNECTION="${CONNECTION:-}"   # tclient: socket (default) or rmi; gclient: random (default), socket, rmi, manual
SERVER_HOST="${SERVER_HOST:-server}"
SERVER_PORT="${SERVER_PORT:-}"
DISPLAY_MODE="${DISPLAY_MODE:-novnc}"

# The four kingdoms of the cover art (xterm-256 palette)
C_PLANT=71 C_ANIMAL=38 C_INSECT=134 C_FUNGI=167 C_MUTED=245

BIG_CODEX=(
' ██████╗ ██████╗ ██████╗ ███████╗██╗  ██╗'
'██╔════╝██╔═══██╗██╔══██╗██╔════╝╚██╗██╔╝'
'██║     ██║   ██║██║  ██║█████╗   ╚███╔╝ '
'██║     ██║   ██║██║  ██║██╔══╝   ██╔██╗ '
'╚██████╗╚██████╔╝██████╔╝███████╗██╔╝ ██╗'
' ╚═════╝ ╚═════╝ ╚═════╝ ╚══════╝╚═╝  ╚═╝'
)
BIG_NATURALIS=(
'███╗   ██╗ █████╗ ████████╗██╗   ██╗██████╗  █████╗ ██╗     ██╗███████╗'
'████╗  ██║██╔══██╗╚══██╔══╝██║   ██║██╔══██╗██╔══██╗██║     ██║██╔════╝'
'██╔██╗ ██║███████║   ██║   ██║   ██║██████╔╝███████║██║     ██║███████╗'
'██║╚██╗██║██╔══██║   ██║   ██║   ██║██╔══██╗██╔══██║██║     ██║╚════██║'
'██║ ╚████║██║  ██║   ██║   ╚██████╔╝██║  ██║██║  ██║███████╗██║███████║'
'╚═╝  ╚═══╝╚═╝  ╚═╝   ╚═╝    ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝╚═╝╚══════╝'
)
SMALL_CODEX=(
'  ___ ___  ___  _____  __'
' / __/ _ \|   \| __\ \/ /'
'| (_| (_) | |) | _| >  < '
' \___\___/|___/|___/_/\_\'
)
SMALL_NATURALIS=(
' _  _   _ _____ _   _ ___    _   _    ___ ___ '
'| \| | /_\_   _| | | | _ \  /_\ | |  |_ _/ __|'
'| .` |/ _ \| | | |_| |   / / _ \| |__ | |\__ \'
'|_|\_/_/ \_\_|  \___/|_|_\/_/ \_\____|___|___/'
)

# ----------------------------------------------------------------- helpers
die() { printf 'Errore: %s\n' "$*" >&2; exit 64; }

usage() {
  cat >&2 <<'EOF'
Codex Naturalis — nessuna modalità scelta e nessun terminale interattivo.

Per il menu avvia il container con un terminale:
  docker run -it codex-naturalis

Oppure scegli la modalità con le variabili d'ambiente:
  MODE=host     GAME_MODE=n|l  RMI_PORT=1099  SOCKET_PORT=1234
  MODE=tclient  CONNECTION=socket|rmi  SERVER_HOST=server  SERVER_PORT=1234|1099
  MODE=gclient  DISPLAY_MODE=novnc|x11  CONNECTION=random|socket|rmi|manual
EOF
  exit 64
}

is_port() { [[ $1 =~ ^[0-9]{1,5}$ ]] && (( 10#$1 >= 1 && 10#$1 <= 65535 )); }

# ----------------------------------------------------------------- menu (gum)
paint_block() {  # paint_block <left-pad> <color-top-half> <color-bottom-half> <line>...
  local pad=$1 top=$2 bottom=$3; shift 3
  local half=$(( ($# + 1) / 2 )) i=0 line
  for line in "$@"; do
    printf '%*s\e[38;5;%sm%s\e[0m\n' "$pad" '' "$(( i < half ? top : bottom ))" "$line"
    i=$(( i + 1 ))
  done
}

banner() {
  local cols=0 width cw line sub="IS24-AM04 · Docker edition"
  read -r _ cols < <(stty size 2>/dev/null) || true
  [[ $cols =~ ^[0-9]+$ ]] && (( cols > 0 )) || cols=80
  local -a codex naturalis
  if (( cols >= 72 )); then
    codex=("${BIG_CODEX[@]}"); naturalis=("${BIG_NATURALIS[@]}")
  elif (( cols >= 48 )); then
    codex=("${SMALL_CODEX[@]}"); naturalis=("${SMALL_NATURALIS[@]}")
  else
    printf '\n  \e[1;38;5;%sm◆ CODEX NATURALIS ◆\e[0m\n  \e[38;5;%sm%s\e[0m\n\n' "$C_PLANT" "$C_MUTED" "$sub"
    return
  fi
  width=0; for line in "${naturalis[@]}"; do (( ${#line} > width )) && width=${#line}; done
  cw=0;    for line in "${codex[@]}";     do (( ${#line} > cw ))    && cw=${#line};    done
  local pad=$(( (cols - width) / 2 ))
  printf '\n'
  paint_block $(( pad + (width - cw) / 2 )) "$C_PLANT" "$C_ANIMAL" "${codex[@]}"
  paint_block "$pad" "$C_INSECT" "$C_FUNGI" "${naturalis[@]}"
  printf '\n%*s\e[38;5;%sm%s\e[0m\n\n' $(( (cols - ${#sub}) / 2 )) '' "$C_MUTED" "$sub"
}

choose() {  # choose <header> <option>...  -> prints the chosen option
  gum choose --header "$1" --cursor "▸ " \
    --header.foreground "$C_MUTED" --cursor.foreground "$C_PLANT" \
    --selected.foreground "$C_PLANT" "${@:2}"
}

ask() {  # ask <header> <default>
  gum input --header "$1" --value "$2" --prompt "▸ " \
    --header.foreground "$C_MUTED" --prompt.foreground "$C_PLANT"
}

ask_port() {  # ask_port <header> <default>
  local v
  while true; do
    v=$(ask "$1" "$2") || return 1
    v=${v:-$2}
    is_port "$v" && { printf '%s' "$v"; return 0; }
    gum style --foreground "$C_FUNGI" "  «$v» non è una porta valida (1-65535)" >&2
  done
}

summary() { gum style --border rounded --border-foreground "$C_ANIMAL" --padding "0 2" --margin "1 2" "$@"; }

bye() { printf '\n  \e[38;5;%smAlla prossima partita!\e[0m\n\n' "$C_PLANT"; exit 0; }

menu_host() {
  local saved="nessun salvataggio trovato" choice
  [[ -f $SAVE_FILE ]] && saved="salvataggio del $(date -r "$SAVE_FILE" '+%d/%m/%Y %H:%M')"
  choice=$(choose "Che partita vuoi ospitare?" "Nuova partita" "Carica partita   $saved") || bye
  if [[ $choice == Carica* ]]; then GAME_MODE=l; else GAME_MODE=n; fi
  RMI_PORT=$(ask_port "Porta RMI" "$RMI_PORT") || bye
  SOCKET_PORT=$(ask_port "Porta Socket" "$SOCKET_PORT") || bye
  summary "Server in avvio" "RMI $RMI_PORT · Socket $SOCKET_PORT · salvataggi in /data"
  MODE=host
}

menu_gui() {
  local choice port=${NOVNC_PORT:-6080}
  choice=$(choose "Dove vuoi vedere la grafica?" \
    "Browser (noVNC)   apri http://localhost:${port%%-*}" \
    "Finestra X11      XQuartz · VcXsrv/WSLg · Linux") || bye
  if [[ $choice == Browser* ]]; then DISPLAY_MODE=novnc; else DISPLAY_MODE=x11; fi
  MODE=gclient
}

menu_tui() {
  local choice default_port
  choice=$(choose "Come vuoi collegarti al server?" "Socket" "RMI") || bye
  CONNECTION=${choice,,}
  SERVER_HOST=$(ask "Indirizzo del server" "$SERVER_HOST") || bye
  SERVER_HOST=${SERVER_HOST:-server}
  if [[ $CONNECTION == rmi ]]; then default_port=1099; else default_port=1234; fi
  SERVER_PORT=$(ask_port "Porta del server" "${SERVER_PORT:-$default_port}") || bye
  summary "Client testuale" "$choice → $SERVER_HOST:$SERVER_PORT"
  MODE=tclient
}

menu() {
  [[ ${TERM:-xterm} == xterm ]] && export TERM=xterm-256color
  # A terminal that reports 0x0 (e.g. `docker run -t` with nothing attached) would make gum
  # draw nothing: give it a sane size.
  [[ $(stty size 2>/dev/null) == "0 0" ]] && stty rows 24 cols 80 2>/dev/null
  printf '\e[H\e[2J'
  banner
  local choice
  choice=$(choose "Cosa vuoi avviare?" \
    "Server       ospita una partita" \
    "Client GUI   gioca con la grafica" \
    "Client TUI   gioca nel terminale" \
    "Esci") || bye
  case $choice in
    Server*)       menu_host ;;
    "Client GUI"*) menu_gui ;;
    "Client TUI"*) menu_tui ;;
    *)             bye ;;
  esac
}

# ----------------------------------------------------------------- start
run_game() {  # run_game <answer-to-start-menu> [<prompt> <answer>]...
  local args=(--answer "$WELCOME" "$1"); shift
  while (( $# )); do args+=(--answer "$1" "$2"); shift 2; done
  # prism.order=sw: software rendering (no GPU in a container)
  # javafx.preloader: Docker-only hooks (docker/hooks): fixed 1024x768 window, automatic join
  exec python3 "$AUTORESPOND" "${args[@]}" -- \
    java -Dprism.order=sw -Djavafx.preloader=codex.docker.DockerPreloader \
      -cp "$APP_CLASSPATH" theMain.main
}

start_host() {
  case "${GAME_MODE,,}" in
    n|new|nuova)  GAME_MODE=n ;;
    l|load|carica) GAME_MODE=l ;;
    *) die "GAME_MODE non valido: '$GAME_MODE' (usa n oppure l)" ;;
  esac
  is_port "$RMI_PORT"    || die "RMI_PORT non valida: '$RMI_PORT'"
  is_port "$SOCKET_PORT" || die "SOCKET_PORT non valida: '$SOCKET_PORT'"
  run_game host \
    "nuova partita"                 "$GAME_MODE" \
    "[RMI] Inserire la porta"       "$RMI_PORT" \
    "[Socket] Ciao, su quale porta" "$SOCKET_PORT"
}

start_tui() {
  local connection=${CONNECTION:-socket}
  case "${connection,,}" in
    socket) CONNECTION=SOCKET; SERVER_PORT=${SERVER_PORT:-1234} ;;
    rmi)    CONNECTION=RMI;    SERVER_PORT=${SERVER_PORT:-1099} ;;
    *) die "CONNECTION non valida: '$CONNECTION' (usa socket oppure rmi)" ;;
  esac
  [[ $SERVER_HOST =~ ^[A-Za-z0-9._:-]+$ ]] || die "SERVER_HOST non valido: '$SERVER_HOST'"
  is_port "$SERVER_PORT" || die "SERVER_PORT non valida: '$SERVER_PORT'"
  run_game tclient \
    "RMI o SOCKET?"                  "$CONNECTION" \
    "A che server vuoi connetterti?" "$SERVER_HOST $SERVER_PORT"
}

# The GUI joins the server by itself (AutoConnect in docker/hooks): Socket or RMI at random,
# unless CONNECTION says which; CONNECTION=manual leaves the connection screens to the player.
choose_gui_connection() {
  local picked="" default_port=1234
  case "${CONNECTION,,}" in
    ""|random)
      if (( RANDOM % 2 )); then CODEX_CONNECTION=RMI; else CODEX_CONNECTION=SOCKET; fi
      picked=" (scelto a caso)" ;;
    socket) CODEX_CONNECTION=SOCKET ;;
    rmi)    CODEX_CONNECTION=RMI ;;
    manual) CODEX_CONNECTION="" ;;
    *) die "CONNECTION non valida: '$CONNECTION' (usa random, socket, rmi oppure manual)" ;;
  esac
  if [[ -z $CODEX_CONNECTION ]]; then
    gum style --foreground "$C_MUTED" "  Collegamento: lo scegli nella finestra del gioco"
    return
  fi
  [[ $CODEX_CONNECTION == RMI ]] && default_port=1099
  CODEX_SERVER_HOST=$SERVER_HOST
  CODEX_SERVER_PORT=${SERVER_PORT:-$default_port}
  [[ $CODEX_SERVER_HOST =~ ^[A-Za-z0-9._:-]+$ ]] || die "SERVER_HOST non valido: '$CODEX_SERVER_HOST'"
  is_port "$CODEX_SERVER_PORT" || die "SERVER_PORT non valida: '$CODEX_SERVER_PORT'"
  export CODEX_CONNECTION CODEX_SERVER_HOST CODEX_SERVER_PORT
  gum style --foreground "$C_MUTED" \
    "  Collegamento automatico${picked}: $CODEX_CONNECTION → $CODEX_SERVER_HOST:$CODEX_SERVER_PORT"
}

start_gui() {
  [[ -r $GUI_HELPER ]] || die "client grafico non disponibile in questa immagine"
  choose_gui_connection
  # shellcheck source=start-gui.sh
  source "$GUI_HELPER"
  prepare_display        # noVNC or X11, exports DISPLAY
  run_game gclient
}

main() {
  if (( $# > 0 )); then
    case "${1,,}" in
      host|server|gclient|gui|tclient|tui) MODE=$1 ;;
      *) exec "$@" ;;
    esac
  fi
  if [[ -z $MODE ]]; then
    [[ -t 0 && -t 1 ]] || usage
    menu
  fi
  case "${MODE,,}" in
    host|server)  start_host ;;
    tclient|tui)  start_tui ;;
    gclient|gui)  start_gui ;;
    *) die "MODE non valido: '$MODE' (usa host, gclient o tclient)" ;;
  esac
}

main "$@"
