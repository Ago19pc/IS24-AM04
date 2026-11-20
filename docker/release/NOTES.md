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
4. In the menu pick **Client GUI → Browser** and open the address the terminal shows (e.g.
   <http://localhost:6080>): the game joins the server by itself and opens on the name screen.
   Or pick **Client TUI** to play in the terminal.

More players on the same computer: run the client command again; each client shows its own
address (ports 6080-6089).

Game window on your desktop (X11, uses **compose.x11-linux.yaml** on Linux), saved games, playing
over the local network and all settings: [docker/README.md]({{README}}).
