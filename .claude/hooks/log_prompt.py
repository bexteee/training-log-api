"""UserPromptSubmit hook: append each prompt to docs/ai-log/prompts-<git user>.md.

Team tooling for the course AI-use disclosure log (not project code).
It never blocks a prompt: any failure is swallowed and the hook exits 0.
"""
import datetime
import json
import os
import re
import subprocess
import sys

MAX_CHARS = 1000


def git_user(cwd):
    try:
        name = subprocess.run(
            ["git", "config", "user.name"], cwd=cwd, capture_output=True, text=True, timeout=5
        ).stdout.strip()
    except Exception:
        name = ""
    return name or os.environ.get("USERNAME") or os.environ.get("USER") or "unknown"


def main():
    data = json.loads(sys.stdin.buffer.read().decode("utf-8"))
    root = os.environ.get("CLAUDE_PROJECT_DIR") or data.get("cwd") or os.getcwd()
    user = git_user(root)
    slug = re.sub(r"[^a-z0-9]+", "-", user.lower()).strip("-") or "unknown"

    prompt = data.get("prompt", "").strip()
    if len(prompt) > MAX_CHARS:
        prompt = prompt[:MAX_CHARS] + " […truncated]"
    quoted = "\n".join("> " + line for line in prompt.splitlines()) or "> (empty)"

    log_dir = os.path.join(root, "docs", "ai-log")
    os.makedirs(log_dir, exist_ok=True)
    path = os.path.join(log_dir, f"prompts-{slug}.md")
    is_new = not os.path.exists(path)

    stamp = datetime.datetime.now().strftime("%Y-%m-%d %H:%M")
    session = str(data.get("session_id", ""))[:8]
    with open(path, "a", encoding="utf-8") as f:
        if is_new:
            f.write(f"# Claude prompt log — {user}\n\nRaw material for the AI-use disclosure log.\n")
        f.write(f"\n### {stamp} · session {session}\n\n{quoted}\n")


if __name__ == "__main__":
    try:
        main()
    except Exception:
        pass
    sys.exit(0)
