# Codex Naturalis in Docker

Run the whole game (server, graphical client and text client) on macOS, Windows or Linux,
on Intel/AMD or ARM machines, without installing Java or JavaFX. The graphical client opens
in your browser, or as a regular window through an X server.

## Requirements

- **macOS / Windows:** Docker Desktop.
- **Linux:** Docker Engine with the Compose plugin.
- Optional, for a native game window instead of the browser: an X server
  (XQuartz on macOS, VcXsrv or WSLg on Windows, any X11/XWayland session on Linux).

## Play without building

Every [release](https://github.com/Ago19pc/IS24-AM04/releases/latest) ships a ready-made image
for Intel/AMD and ARM. Download its `compose.yaml` into an empty folder, open a terminal there and
run:

```sh
docker compose up -d server
docker compose run --rm --service-ports client
```

The first command downloads the image (about 150 MB) instead of building it. Everything below
works the same way with the release file: just leave out `-f docker/compose.yaml`.

## Quick start from the repository

Run every command from the repository root.

```sh
docker compose -f docker/compose.yaml up -d server
docker compose -f docker/compose.yaml run --rm --service-ports client
```

The first command builds the image (a few minutes the first time) and starts the server in
the background. The second one opens the launcher menu, where you pick a client:

- **Client GUI → Browser (noVNC):** open the address the terminal shows, e.g.
  <http://localhost:6080>. The game joins the server by itself, over Socket or RMI picked at
  random, and opens on the name screen.
- **Client TUI:** the menu asks how to connect and joins the server for you.

Each player runs the second command in their own terminal. Docker gives every browser client on
the same computer a free port between 6080 and 6089, and each client's terminal shows its own
address (on Linux it shows the `docker port` command that tells it). To pin a client to a port,
set `NOVNC_PORT`:

```sh
NOVNC_PORT=6085 docker compose -f docker/compose.yaml run --rm --service-ports client   # macOS / Linux
```

```powershell
$env:NOVNC_PORT=6085; docker compose -f docker/compose.yaml run --rm --service-ports client; Remove-Item Env:NOVNC_PORT
```

To stop the server: `docker compose -f docker/compose.yaml down` (saved games are kept).

## Playing over the local network

The server publishes the Socket port **1234** on every network interface: players on other
computers join with **SOCKET**, using this computer's IP address and port 1234, either from this
image or with the regular jar. Your firewall may ask you to allow incoming connections.

RMI only works between containers on the same computer: the RMI server calls the clients back,
and Docker's network address translation does not let those calls through.

## Saved games

The server writes `game.json` to the `codex-naturalis_codex-data` volume. With the default
`GAME_MODE=l` it resumes that save when it starts, also after a crash or a reboot
(`restart: unless-stopped`); if there is no save, a new game starts.

```sh
GAME_MODE=n docker compose -f docker/compose.yaml up -d server     # force a new game
docker compose -f docker/compose.yaml cp server:/data/game.json .  # back up the save
docker compose -f docker/compose.yaml down -v                      # stop and DELETE the saves
```

## Game window on your desktop (X11)

Choose **Client GUI → Finestra X11** in the menu, or set `DISPLAY_MODE=x11`. The window keeps the
game's size, 1024×768.

**macOS (XQuartz)**

1. Install [XQuartz](https://www.xquartz.org), open it, go to *Settings → Security*, tick
   *Allow connections from network clients*, then quit and reopen XQuartz.
2. Every time XQuartz starts, run `xhost +localhost` in Terminal. To make it permanent:
   ```sh
   printf '#!/bin/sh\n/opt/X11/bin/xhost +localhost\n' \
     | sudo tee /opt/X11/etc/X11/xinit/xinitrc.d/99-codex-xhost.sh >/dev/null
   sudo chmod +x /opt/X11/etc/X11/xinit/xinitrc.d/99-codex-xhost.sh
   ```
3. Start the client as usual. The container reaches XQuartz at `host.docker.internal:0`.

**Windows**

- **VcXsrv:** start *XLaunch* → *Multiple windows* → *Start no client* → tick
  *Disable access control*. The container finds it at `host.docker.internal:0`.
- **WSLg (Windows 11):** from a WSL terminal,
  `docker compose -f docker/compose.yaml -f docker/compose.x11-linux.yaml run --rm client`.

**Linux**

```sh
xhost +local:
docker compose -f docker/compose.yaml -f docker/compose.x11-linux.yaml run --rm client
```

## Without Compose

```sh
docker build -f docker/Dockerfile -t codex-naturalis:local .
docker network create codex
docker volume create codex-data
docker run -d --name server --network codex -v codex-data:/data -p 1234:1234 \
  -e MODE=host -e GAME_MODE=l codex-naturalis:local
docker run -it --rm --network codex -p 127.0.0.1:6080:6080 codex-naturalis:local
```

`docker run -it --rm codex-naturalis:local bash` opens a shell inside the image.

## Settings

Environment variables read by the container (the menu fills in the ones it asks about):

| Variable | Default | Used by | Meaning |
|---|---|---|---|
| `MODE` | *(menu)* | all | `host`, `gclient` or `tclient`. Empty with a terminal attached shows the menu. |
| `GAME_MODE` | `n` (compose: `l`) | host | `n` new game, `l` resume the saved game. |
| `RMI_PORT` / `SOCKET_PORT` | `1099` / `1234` | host | Server ports inside the container. |
| `CONNECTION` | `socket` (TUI), `random` (GUI) | clients | TUI: `socket` or `rmi`. GUI: `random`, `socket`, `rmi`, or `manual` to fill in the connection screens yourself. |
| `SERVER_HOST` | `server` | clients | Server address. |
| `SERVER_PORT` | `1234` / `1099` | clients | Server port, default depends on the connection. |
| `DISPLAY_MODE` | `novnc` | gclient | `novnc` (browser) or `x11` (window on your X server). |
| `DISPLAY` | `host.docker.internal:0` | gclient, x11 | X server to draw on. |
| `GUI_SIZE` | `1024x768` | gclient, novnc | Size of the virtual screen. |
| `NOVNC_PORT` | `6080-6089` | compose | Host port of the browser client: a port, or a range whose first free port is used. |
| `CODEX_IMAGE` | `codex-naturalis:local` | compose | Image to run, e.g. the published one. |

## What is in the image

- **Base:** `debian:trixie-slim` with a Java 21 runtime trimmed by `jlink` to the modules the
  game uses. About 360 MB on arm64 and 380 MB on amd64.
- **amd64** reuses the delivered `deliveries/jar/codexnaturalis.jar`. **arm64** builds the game
  from source with Maven, because the delivered jar only contains x86-64 JavaFX libraries.
- **Rendering:** JavaFX draws in software (containers have no GPU), so Mesa/OpenGL is left out.
  A placeholder package keeps apt consistent; delete it in the Dockerfile to get Mesa back.
- **Fonts:** the GUI was designed on Windows with Segoe UI and Constantia, which cannot be
  redistributed. Selawik (Microsoft's open-source font with the same metrics as Segoe UI) and
  Caladea take their place.
- **Docker-only JavaFX hooks** (`docker/hooks`, loaded as a preloader; the game's own code is
  unchanged): the game window cannot be resized (every scene is 1024×768, and drag-resizing a
  JavaFX window through XQuartz turns it black), and the two connection screens are filled in and
  confirmed automatically.

## Releases

Pushing a version tag publishes a new release:

```sh
git tag v1.0.1
git push origin v1.0.1
```

The workflow `.github/workflows/release.yml` builds the image on native amd64 and arm64 runners
and runs `docker/tests/smoke.sh` on each. Only if both pass does it publish the image as
`ghcr.io/ago19pc/codex-naturalis:<version>` and `:latest` and create the GitHub Release with the
ready-to-play `compose.yaml` (`docker/release/compose.yaml` with the version filled in) and
`compose.x11-linux.yaml`. No personal token is needed: it uses the repository's own
`GITHUB_TOKEN`, and the package is public like the repository.

With the repository's compose file you can also run a published image instead of building:
`CODEX_IMAGE=ghcr.io/ago19pc/codex-naturalis:latest docker compose -f docker/compose.yaml up -d server`.

## Tests

```sh
docker/tests/smoke.sh                        # the image: errors, menu, server, clients, GUI, X11
PLATFORM=linux/amd64 docker/tests/smoke.sh   # the same on the other architecture
BUILDER=ci docker/tests/smoke.sh             # build with a buildx builder like the CI's:
                                             # docker buildx create --name ci --driver docker-container
docker/tests/compose.sh                      # the compose services, as players use them
COMPOSE_FILE=docker/release/compose.yaml docker/tests/compose.sh
                                             # the release's compose file (pulls the image)
```

## Troubleshooting

- **"nessuna modalità scelta e nessun terminale interattivo":** the menu needs a terminal.
  `docker compose run` gives it one; with `docker run` add `-it`, or set `MODE`.
- **"Server X non raggiungibile":** follow the X11 steps above, or play in the browser.
- **Port already in use:** 1234 belongs to the server. For browser clients it means all of
  6080-6089 are taken: stop some clients, or set another `NOVNC_PORT`.
- **The browser page shows "Disconnected":** the client has exited (for example the game is
  over); start it again.

## Third-party software in the image

noVNC (MPL-2.0), websockify (LGPL-3.0), Selawik (OFL-1.1) and Caladea (Apache-2.0). Their
licence files ship inside the image next to them.
