#!/usr/bin/env python3
"""Run a command in a pseudo-terminal and answer its prompts, one at a time.

    autorespond.py [--answer PATTERN TEXT]... -- COMMAND [ARG]...

Each TEXT is typed only after its PATTERN has appeared in the output, in the
given order. Codex Naturalis needs this because every component builds its own
Scanner(System.in): answers written ahead of time would be swallowed by the
read buffer of the first Scanner. After the last answer the terminal belongs
to the user (stdin is forwarded), so the same wrapper serves a headless server
and an interactive TUI.
"""
import fcntl
import os
import pty
import select
import signal
import sys
import termios
import time
import tty

STDIN, STDOUT = 0, 1
WARN_AFTER = 60  # seconds without the expected prompt before printing a hint


def parse(argv):
    answers, i = [], 0
    while i < len(argv) and argv[i] != "--":
        if argv[i] in ("-a", "--answer") and i + 2 < len(argv):
            answers.append((argv[i + 1].encode(), argv[i + 2].encode()))
            i += 3
        else:
            sys.exit(f"autorespond: unexpected argument {argv[i]!r}\n{__doc__}")
    command = argv[i + 1:]
    if not command:
        sys.exit(f"autorespond: missing command after --\n{__doc__}")
    return answers, command


def write_all(fd, data):
    while data:
        try:
            data = data[os.write(fd, data):]
        except BlockingIOError:
            select.select([], [fd], [])


def copy_winsize(src, dst):
    try:
        fcntl.ioctl(dst, termios.TIOCSWINSZ, fcntl.ioctl(src, termios.TIOCGWINSZ, b"\0" * 8))
    except OSError:
        pass


def pump(master, answers):
    """Copy child output to stdout and stdin to the child, typing answers on cue."""
    window = b""
    deadline = time.monotonic() + WARN_AFTER
    read_stdin = True
    while True:
        ready, _, _ = select.select([master, STDIN] if read_stdin else [master], [], [], 1.0)
        if answers and time.monotonic() > deadline:
            print(f"\r\n[autorespond] still waiting for: {answers[0][0].decode()!r}\r",
                  file=sys.stderr)
            deadline = float("inf")
        if master in ready:
            try:
                data = os.read(master, 65536)
            except OSError:  # EIO: the child closed its terminal
                return
            if not data:
                return
            write_all(STDOUT, data)
            if answers:
                window = (window + data)[-8192:]
                pattern, text = answers[0]
                if pattern in window:
                    write_all(master, text + b"\r")
                    answers.pop(0)
                    window, deadline = b"", time.monotonic() + WARN_AFTER
        if read_stdin and STDIN in ready:
            data = os.read(STDIN, 65536)
            if data:
                write_all(master, data)
            else:  # EOF (detached container): keep serving the child
                read_stdin = False


def main():
    answers, command = parse(sys.argv[1:])
    interactive = os.isatty(STDIN)
    pid, master = pty.fork()
    if pid == 0:  # child: session leader of the new terminal
        if not interactive:  # plain "\n" line endings in `docker logs`
            attrs = termios.tcgetattr(STDOUT)
            attrs[1] &= ~termios.ONLCR
            termios.tcsetattr(STDOUT, termios.TCSANOW, attrs)
        try:
            os.execvp(command[0], command)
        except OSError as err:
            print(f"autorespond: cannot run {command[0]}: {err}", file=sys.stderr)
            os._exit(127)

    def forward(signum, _frame):
        try:
            os.kill(pid, signum)
        except ProcessLookupError:
            pass

    for sig in (signal.SIGTERM, signal.SIGINT, signal.SIGHUP, signal.SIGQUIT):
        signal.signal(sig, forward)

    saved = None
    if interactive:
        copy_winsize(STDIN, master)
        signal.signal(signal.SIGWINCH, lambda *_: copy_winsize(STDIN, master))
        saved = termios.tcgetattr(STDIN)
        tty.setraw(STDIN)
    try:
        pump(master, answers)
    finally:
        if saved is not None:
            termios.tcsetattr(STDIN, termios.TCSAFLUSH, saved)
    _, status = os.waitpid(pid, 0)
    code = os.waitstatus_to_exitcode(status)
    return 128 - code if code < 0 else code


if __name__ == "__main__":
    sys.exit(main())
