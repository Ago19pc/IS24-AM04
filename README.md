# IS AM-04
## Authors: Contemi Agostino, Consorte Federico, Dodi Filippo, Corsi Jacopo

|Feature|Status|
|-------|------|
|UML (high level)|✅|
|Model (complete rules)|✅|
|Socket|✅|
|RMI|✅|
|Controller|✅|
|TUI|⚙️|
|GUI|⚙️|
|Persistance|⏱|
|Connection resilience|⏱|
|Chat|✅|

|To Do |Comments | Priority |Status|
|-------|-------|----------|------|
|Save games|Need to change json library/customize serialization| 1        |⚙️|
|Host handling|Choose to host or join when running main| 3        |⏱|
|Reconnections|-------| WIP      |⚙️|
|Switch to state pattern|currently clientcontroller and servercontroller use a gamestate variable| 5        |⏱|
|Add missing javadoc|-------| 4        |⚙️|
|Add missing tests|tests are currently broken| 6        |⚙️|
|Add RMI port choice|currently they are hardcoded and the user choice is fake| 2        |⏱|
|Improve CLI|It works but it's not enjoyable| 7        |⏱|
|Finish GUI|-------| WIP      |⚙️|

### Reconnections to do list:

| To Do                                                                    | Comments                                                                                                              | Status |
|--------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------|--------|
| Get and set player online status                                         | GeneralServerConnectionHandler has a list of all disconnected (but not yet kicked) player ids                         | Done   |
| Handle disconnection kick timeout                                        | -------                                                                                                               | Done   |
| Differentiate between a player's disconnection and the player's deletion | --------                                                                                                              | Done   |
| CLI reconnection command                                                 | -------                                                                                                               | Done   |
| Messages sent on reconnection                                            | client does not receive gameinfo message when he reconnects while choosing either secret achievement or starting face | Done   |
| Don't send messages to disconnected players                              | -------                                                                                                               | Done   |
| Test | ------- | Doing  |
