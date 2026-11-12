# Compliance Metric

How the Training Log Project turns a sprinter's logged training into numbers that show how closely it followed the plan.

> Status: decided by Vitor on 2026-10-27; set averaging confirmed by Vitor and Owen on 2026-11-01.
> Since the Sprint 2 literature review (2026-11-01), this metric belongs to the **stretch goals** (alignment score,
> weekly summaries). The MVP is the rule-based swim-set classifier.
> Drafted with Claude (Claude Code) from the team's decisions. The team chose every rule below;
> record this in the AI-use disclosure.

## 1. Inputs

| Input | Who it belongs to | Contents |
|---|---|---|
| Plan | Shared by the whole sprint group | Sessions with planned sets |
| Log | One per athlete | Sessions with logged sets |
| Athlete profile | One per athlete | Race best time for each distance and stroke |

A session is identified by **date + practice number + type**. The practice number is counted per type
(swim 1, swim 2, lift 1). Within a paired session, sets are matched **by position** (set 1 with set 1, and so on).

## 2. Set scores

### Swim set, no equipment

1. For each rep: **achieved effort = race best time ÷ actual time × 100**.
   The race best is 100%. A rep faster than the race best is above 100% (no cap).
2. Achieved effort of the set = average of its reps.
3. **Set score = achieved effort ÷ goal effort × 100**.
4. Reported as the achieved effort next to the goal, for example `93.8% (goal 95%)`.

### Swim set with equipment (fins, paddles, ...)

Compared with the goal time only; no effort is calculated.

- **Set score = goal time ÷ actual time × 100**, using the average of the rep times.

### Lift set

- **Set score = (sets × reps × load %) logged ÷ (sets × reps × load %) planned × 100**.
- The report also shows the logged load and sets × reps, so the reason for a lower score is visible.

## 3. From sets to weeks

1. **Session score** = average of its set scores.
2. **Missed session** (planned, no matching log) = **0%**.
3. **Two weekly scores**:
   - **Swim score** = average of that week's swim session scores.
   - **Lift score** = average of that week's lift session scores.
4. **Extra sessions** (logged, not in the plan) are **not** part of the averages. They are reported separately as a count.

## 4. Worked example

Race best 50 free: 24.00 s.

| Session | Set scores | Session score |
|---|---|---|
| Mon swim 1 | 98.7%, 96.0% | 97.4% |
| Mon lift 1 | 94.1% | 94.1% |
| Tue swim 1 | missed | 0% |
| Wed swim 1 | 100.8%, 99.0% | 99.9% |
| Wed swim 2 | 97.8% | 97.8% |
| Thu lift 1 | 100% | 100% |
| Sat swim (extra) | not scored | n/a |

Weekly report:

- **Swim: 73.8%**: (97.4 + 0 + 99.9 + 97.8) ÷ 4
- **Lift: 97.1%**: (94.1 + 100) ÷ 2
- **Extra sessions: 1**

## 5. Assumptions

- All planned sets are attempted; skipped sets are not modelled (sprinters almost never skip a set).
- Every rep time is logged, in seconds. Times are compared with a small tolerance, never for exact equality.
- Load % is relative to the same max for the whole plan.
- Practice starts are push-offs, while race bests come from dive starts, so achieved effort in practice tends to be slightly lower.

## 6. Input rules

These are not part of the score, but the reader must enforce them before scoring:

| Rule | Example that is rejected |
|---|---|
| At most 2 practices per day | swim 1, swim 2 and lift 1 on the same day |
| Allowed days: swim + lift, swim + swim, swim only | lift only; two lifts |
| No duplicate session keys | "Mon swim 1" listed twice in one file |

A logged session that follows these rules but is not in the plan counts as **extra**, not as an error.

## 7. Test cases

| Case | Input | Expected |
|---|---|---|
| Perfect swim set | every rep exactly at goal | set score 100% |
| Faster than race best | race best 24.00, rep 23.80, goal 95% | effort 100.8%, set score 106.1% |
| Equipment set | goal 22.0 s, actual 22.5 s | 97.8% |
| Lift, lighter load | 4×3 @ 80% vs. planned 4×3 @ 85% | 94.1% |
| Lift, one set short | 3×3 @ 85% vs. planned 4×3 @ 85% | 75% |
| Missed session | no log for Tue swim 1 | session 0%, counted in the swim average |
| Extra session | Sat swim logged, nothing planned | not averaged, extra count = 1 |
| Week with no lifts planned | swims only | lift score shown as "n/a" (see open questions) |

## 8. Open questions

1. **Ask Owen:** is averaging the sets the right choice, or should longer or more important sets weigh more?
2. **Week with no planned lifts:** the lift average divides by zero. Show "n/a"? (Proposed above, not decided.)
3. **Ask the coach:** are goal times calculated as race best ÷ goal effort? If yes, the no-equipment formula gives the
   same result as goal time ÷ actual time, and one formula could cover every swim set.
4. **Sources:** support each rule with the literature review (set-level comparison, race best = 100%,
   missed = 0%, two separate scores).
