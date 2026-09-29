"""PreToolUse hook for Bash/PowerShell: block shell commands that write project source files.

The Edit/Write deny rules in settings.json stop Claude's file tools; this closes the shell loophole
(redirects, sed -i, tee, cp/mv, Set-Content, ...) for .java/.py/.ipynb files.
Heuristic by design: it errs on the side of blocking. Exit code 2 blocks the command.
"""
import json
import re
import sys

SRC = r"[^\s'\"|;&]*\.(?:java|py|ipynb)\b"
WRITE_PATTERNS = [
    rf">>?\s*['\"]?{SRC}",                                  # echo ... > Foo.java
    rf"\btee\b[^|;&]*{SRC}",                                 # ... | tee Foo.java
    rf"\bsed\b[^|;&]*\s-i[^|;&]*{SRC}",                      # sed -i ... Foo.java
    rf"\b(?:cp|mv|copy|move|touch|ni)\b[^|;&]*{SRC}",      # cp x Foo.java
    rf"\b(?:Set-Content|Add-Content|Out-File|New-Item|Copy-Item|Move-Item)\b[^|;&]*{SRC}",
    rf"\bWriteAllText\b[^|;&]*{SRC}",
]


def main():
    data = json.loads(sys.stdin.buffer.read().decode("utf-8"))
    command = (data.get("tool_input") or {}).get("command", "")
    for pattern in WRITE_PATTERNS:
        if re.search(pattern, command, re.IGNORECASE):
            sys.stderr.write(
                "Blocked by course AI policy (see CLAUDE.md): Claude may not create or modify "
                ".java/.py/.ipynb files. Explain the change to the user so they can make it themselves.\n"
            )
            sys.exit(2)


if __name__ == "__main__":
    try:
        main()
    except SystemExit:
        raise
    except Exception:
        pass
    sys.exit(0)
