Ready-to-play Docker image of Codex Naturalis for macOS, Windows and Linux, on Intel/AMD and ARM:
no Java, no JavaFX and nothing to build.

**Image:** `{{IMAGE}}`

### Play

1. Install Docker Desktop (macOS, Windows) or Docker Engine with the Compose plugin (Linux).
2. Download **compose.yaml** from the assets below into an empty folder and open a terminal there.
3. Start the server, then one client for each player:
   ```sh
   docker compose up -d server
   docker compose run --rm --service-ports client
   ```
4. In the menu pick **Client GUI → Browser** and open <http://localhost:6080>, or **Client TUI**
   to play in the terminal. In the game, join *Server IP* `server` with port `1234` (SOCKET)
   or `1099` (RMI).

A second player in the browser: `NOVNC_PORT=6081 docker compose run --rm --service-ports client`.

Game window on your desktop (X11, uses **compose.x11-linux.yaml** on Linux), saved games, playing
over the local network and all settings: [docker/README.md]({{README}}).
