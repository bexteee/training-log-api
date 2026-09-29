# Training Log API

Semester CS course project (algorithms/data structures, performance analysis). Team: Vitor, Owen, Andy.
Java is the primary language; Python is used only for matplotlib plots.

An API that checks whether a swimmer's logged training (swim practices + lifts) follows a prescribed
periodization plan, and reports compliance/deviation. Fully rule-based: there is no AI agent in scope.

- `TrackComparator` interface, implemented by `SwimTrackComparator` (sets, distance, stroke, intensity zone)
  and `LiftTrackComparator` (exercise, sets×reps, load %). Each compares a `PlannedSession` with a `LoggedSession`.
- Core deliverable: benchmark three strategies for pairing planned vs. logged sessions by date
  (nested loop O(n·m), sort + two-pointer O(n log n), hashmap by date O(n)) with a doubling-hypothesis experiment.
- Output: compliance report + matplotlib graphs computed directly from data.

## Rules for Claude (course AI policy — non-negotiable)

Claude does NOT write code for this project. This covers Java and Python source, tests, the benchmark harness,
the synthetic data generator and plotting scripts.

Claude MAY:
- Review code and flag bugs/logic errors, pointing to `file:line` and explaining *why* — the author writes the fix.
- Explain concepts, algorithms, complexity, and unfamiliar syntax/library behavior (generic examples only,
  never a drop-in solution for this project).
- Help design test cases as plain-language tables (input → expected output), not test code.
- Review benchmark methodology and results, and discuss design decisions (e.g. the compliance metric).

Claude must NOT:
- Edit or create `.java`, `.py` or `.ipynb` files (also enforced in `.claude/settings.json`), including via shell commands.
- Paste project-specific implementations in chat, even "just as a sketch".
- Run `/code-review --fix` or apply review findings itself.

Every team member must be able to explain and reproduce any part of the codebase without AI (Sprint 4 spot-checks).
The files under `.claude/` are team tooling, not project code, and their creation is recorded in the AI-use log.

## AI-use log

Every prompt is recorded automatically in `docs/ai-log/prompts-<name>.md` (UserPromptSubmit hook).
Use it to write the Sprint 3/4 disclosure entries: tool, purpose, what was used vs. changed/corrected.
When a session produces something the team adopts, remind the user to add a disclosure entry.

## Open decisions (as of 2026-09-28)

1. Compliance metric formula — highest priority; blocks comparators and the literature review.
2. Coaches not yet contacted for real periodization programs.
3. Which matplotlib graphs to produce.
4. Proposal: replace links with proper citations; add the AI-use statement.
