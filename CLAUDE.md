# Training Log API

Semester CS course project (algorithms/data structures, performance analysis). Team: Vitor Sa, Owen Bartee
(Andy left the group in October 2026). Instructor: Marcus Birkenkrahe.
Java is the primary language; Python is used only for matplotlib plots.

## Current plan (from the Sprint 2 literature review, 2026-11-01)

A Java application whose core is a rule-based classifier for sprint swimmers' training. AI is only an optional
add-on that explains scores and makes visuals. Scope is sprinters only.

- Research question: can a rule-based classifier using only set-level data (distance, intensity, rest interval)
  assign swim sets to energy-system categories as defined in the swimming-training literature?
- Base for the categories: the five training zones of Fernandes et al. (2024) (aerobic capacity 1-3, aerobic power,
  anaerobic power and capacity).
- The classifier is a hand-built decision tree (team decision, 2026-11-02): the team writes every split and threshold
  from the literature, each linked to a source. A learned tree is at most a later comparison.
- MVP (swimming only):
  1. a defined format for logging a set,
  2. rules that turn a logged set into a category,
  3. a way to run the rules on a collection of sets,
  4. a test set to check the output against.
- Measurement: agreement with a reference, i.e. the percentage of test sets where the classifier's category
  matches the reference category. Reference categories come from the literature (the coach does not label sets
  by energy system) and must be decided independently of the rules.
- Defined metric (built on top of the classifier, not part of the MVP): one Training Alignment Score combining
  workout-vs-plan match with lift-vs-swim alignment.
- Stretch goals: logging full workouts and weekly summaries; extending the classifier to lifting; the overall
  alignment score; optional AI explanations and visuals; checking the classifier against the coach's labels.
- Sprint 3 targets: the logging format in Java (classes for a set and a workout) with example sets; the first
  version of the classification rules with their sources, running on example sets; a test set with a reference
  category per set and where it came from; a first agreement percentage.

Sprint 2 sources (APA 7): Fernandes et al. (2024), Hellard et al. (2019), Okičić et al. (2007), Petrov et al. (2026),
Pollock et al. (2019), plus the coach interview (A. Makepeace, personal communication, October 30, 2026).

## Earlier plan (before Sprint 2), kept for reference

The original design checked logged training against a periodization plan: a `TrackComparator` interface
(`SwimTrackComparator`, `LiftTrackComparator`) comparing a `PlannedSession` with a `LoggedSession`, and a benchmark of
three strategies for pairing planned vs. logged sessions by date (nested loop O(n·m), sort + two-pointer O(n log n),
hashmap O(n)) with a doubling-hypothesis experiment. This is no longer the core and is not in the Sprint 2 document;
the comparison side now belongs to the stretch goals (alignment score, weekly summaries). Whether the pairing
benchmark returns as the algorithms/performance part of the project is not decided.

The bioflexible periodization book (Silva & Affonso, 2026, Editora Dialética) was considered but not used as a source.

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
This folder is gitignored: each member's log stays on their own machine and is never committed.
Use it to write the Sprint 3/4 disclosure entries: tool, purpose, what was used vs. changed/corrected.
When a session produces something the team adopts, remind the user to add a disclosure entry.

## Schedule

- Sprint 1 (proposal): graded. Instructor feedback 2026-10-12: define "how well you are training" as a computable
  number first; links are not references; no AI-use statement was given; label Title/Authors sections.
  Grading is stricter from Sprint 2 on.
- Sprint 2 (literature review): due 2026-10-30, finalized 2026-11-01; sprint review in class 2026-11-02
  (each member presents >= 1 source). It answers: what did we achieve, what did we plan but not achieve,
  what will we do in Sprint 3.
- Sprint 3: about one month; targets listed under "Current plan".
- Final presentations: 2026-11-27, 11-30, 12-02 or 12-04.

## Work split (decided 2026-10-29)

- Owen: categorization (the classification rules).
- Vitor: the data model (logging format for sets and workouts) and the comparison side (now stretch goals).
- Stack (2026-11-02): Java 21, Maven, JUnit 5, CSV input files; Vitor uses VS Code. Every file/change is committed
  to the team's GitHub repository.
- Classifier input fields (2026-11-02): distance, intensity, rest. Intensity = effort % calculated from the swimmer's
  race best (race best / actual time x 100).

## Data model decisions (2026-10-27)

Made for the earlier plan. The set-level fields still inform the MVP's logging format; the pairing and scoring
rules apply to the stretch goals (alignment score, weekly summaries). Full metric spec: `docs/compliance-metric.md`.

- Two levels: Session (date, practice number, type SWIM/LIFT) contains Sets; comparison happens at the set level
  (main sets only; warm-up/cool-down ignored).
- Sets within a paired session are matched by position; skipped sets are not modelled (swimmers almost never skip).
- Pairing key: date + practice number + type (practice 1, 2, ... = the planned practices of that day). The logged
  session states which planned practice it is for, so a missed practice made up later the same day still pairs.
  No make-up that day -> missed. A session moved to another day counts as missed + extra (rare).
  Practice number is counted per type (swim 1, swim 2, lift 1). A logged practice that is not in the plan counts as
  extra; input that breaks the daily rules below is rejected.
- At most 2 practices per day, and only these combinations: swim + lift, swim + swim, or swim only.
- One plan shared by the whole sprint group (same schedule); logs are per athlete.
- Swim sets log every rep's time in seconds. Achieved effort is calculated from times: race best time / actual time
  x 100 (race best = 100%; faster gives > 100%, no cap). Needs each athlete's race best per distance and stroke.
  This effort % is a candidate for the classifier's "intensity" input, since it is relative to each swimmer.
  Sets with equipment (fins, paddles...) are compared with goal times only, no effort %.
- Compliance metric (stretch goal; averaging the sets confirmed by Vitor and Owen 2026-11-01):
  - Swim set, no equipment: score = achieved effort / goal effort (reported as "93.8% (goal 95%)").
  - Swim set with equipment: score = goal time / actual time x 100.
  - Lift set: score = (sets x reps x load %) logged / (sets x reps x load %) planned.
  - Session score = average of its set scores. Missed session = 0%.
  - Two weekly scores: swim and lift, each the average of that week's session scores.
  - Extra sessions are not averaged; they are reported separately as a count.
  Compare times with a tolerance, never with ==.
- Lift load is stored as % of 1RM, planned and logged.

## Coach input

Coach Andrew Makepeace answered our questions (Vitor's file "Answers from Coach Andrew.txt", 2026-10-30). Used as
background on how periodization works: he plans weekly, adapts daily, does not write energy systems in workouts,
values effort over times in sprint sets, and plans lifting independently of swimming. His Q1 answer (season
structure: two peaks, ~16-week block, 3-4 week sub-blocks) is a garbled transcription; confirm before citing.
He agreed his plans can be used (anonymized); no example practice yet.

## Open decisions (as of 2026-11-01)

1. Maven project set up in `training-log/` (Java 21, JUnit 5.11.4; builds with `mvn test`). Packages agreed:
   `model` (Vitor), `classifier` (Owen), `io` (CSV reading).
   Owen suggested perceived effort (RPE) and closeness to a goal time; the team kept effort % from race best as the
   only intensity measure (2026-11-02).
2. Intensity = effort % from race best (decided). Storage decided 2026-11-02 (option A): the CSV stores raw data
   (athlete, stroke, distance, rest, rep times) and effort % is calculated in code from a separate race-best file
   (athlete, distance, stroke, best time). A set's effort = average of rep efforts (decided 2026-11-04).
   Still open: how reference categories are chosen independently of the rules.
   Swim set table (being designed with Vitor, 2026-11-04): athlete = short ID (e.g. A01, anonymous); stroke = one of
   4 strokes (free, back, breast, fly; no IM); no equipment in the MVP (equipment = stretch goal / later change);
   pool = short course yards (SCY) only; rep distance = any positive value (sets like 10x15 @ 60 exist); rep times in
   seconds with hundredths; both the send-off interval and rest are stored.
   Rest used by the classifier = AVERAGE rest of the set, where each rep's rest = send-off interval - rep time.
   Why (to explain to Owen): rest differs rep by rep because slower reps leave less rest; the average is one number
   per set for the tree, stays close to the rest the coach intended with the interval, and is not distorted by a
   single slow or fast rep the way the shortest or longest rest would be.
   If a rep time is longer than the interval, that rep's rest counts as 0 (the set is not rejected).
   Each set stores its position in the workout (set 1, 2, ...). The athlete ID is stored on the workout (with the
   date), not on each set.
   Set effort % (classifier intensity) = AVERAGE of the rep efforts, each rep effort = best time / rep time x 100
   (not the effort of the average time). Why: consistent with average rest, every rep counts equally (the coach says
   each effort matters), simple to explain and test. Median is a possible later comparison.
   Shared design doc for Owen: `docs/Data Model - Swim Set.docx` (2026-11-04).
   Race/test best table (2026-11-04): a swimmer can have both a race best and a test best for the same stroke and
   distance; effort % uses the race best when one exists, otherwise the test best. Each best records its start
   (dive or push) for transparency only, no adjustment (push vs dive is a stated limitation). Only the latest best is
   kept, with the date it was swum (replaced when the swimmer improves). A set with no matching best is skipped (not
   classified) and reported as "missing reference time".
   Key = athlete + stroke + distance + kind (RACE/TEST); start = DIVE/PUSH; date = when swum (not in the future).
   Duplicate rows for the same key: use the faster time. Limitation accepted for the MVP: workouts before a best's
   date are measured against the newer (faster) best; keeping dated history is a stretch goal (or fix manually).
   Short reps (e.g. 15 or 25 yd) have no race distance, so the race-best table also holds "test bests" (fastest
   practice time for that distance and stroke). Why (to explain to Owen, this is an addition to the project): effort %
   needs a reference time for the same distance and stroke; without test bests, sprint sets of 15s and 25s could not be
   classified, and scaling a 50 race best would be inaccurate (the first 25 of a 50 includes the start).
3. Whether the pairing benchmark / doubling experiment comes back as the algorithms-and-performance part of the
   project, and where.
4. Which matplotlib graphs to produce.
   Idea to brainstorm later (Vitor, 2026-11-04): a Google Form sent to the team's sprint group to collect each
   swimmer's sets and best times individually, exported to CSV as the project's data sets.
5. Every deliverable needs an explicit AI-use statement, including "none used". No AI was used on the Sprint 1
   proposal itself; Claude was used from 2026-09-28 for repo tooling and planning.
