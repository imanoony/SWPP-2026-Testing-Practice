# Spec: `DueDateUtils.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `dueDateLabel(dueDate: LocalDate?, today: LocalDate, isCompleted: Boolean): String`

**Intent**: Return the task's status label.
- Completed tasks show `"CLEAR"`, regardless of `dueDate`.
- Active tasks without a due date show no label (`""`).
- Active tasks with a due date show the signed calendar-day distance from today: `"D-N"` before the due date, `"D-Day"` on it, `"D+N"` after it.

**Format rules**: `N` is a positive integer in plain decimal (no zero-padding, no spaces, no thousands separator). `"D-0"` and `"D+0"` are never returned.

**Pre-conditions**
- `today` is a valid `LocalDate`; `dueDate` is either null or a valid `LocalDate`.

**Post-conditions**
- Success cases
  - A completed task with a future due date returns `"CLEAR"`. `[Unit]`
    - Tip: Given `dueDate = 2026-10-21`, `today = 2026-10-20`, `isCompleted = true` / When `dueDateLabel` is called / Then it returns `"CLEAR"`.
  - A completed task with no due date returns `"CLEAR"`. `[Unit]`
    - Tip: Given `dueDate = null`, `today = 2026-10-20`, `isCompleted = true` / When `dueDateLabel` is called / Then it returns `"CLEAR"`.
  - An active task with no due date returns an empty string. `[Unit]`
    - Tip: Given `dueDate = null`, `today = 2026-10-20`, `isCompleted = false` / When `dueDateLabel` is called / Then it returns `""`.
  - An active task due in the future returns `"D-N"`, where `N` is the full number of calendar days until the due date. `[Unit]`
    - Tip: Given `dueDate = 2026-10-23`, `today = 2026-10-20`, `isCompleted = false` / When `dueDateLabel` is called / Then it returns `"D-3"`.
  - An active task due in the past returns `"D+N"`, where `N` is the full number of calendar days since the due date. `[Unit]`
    - Tip: Given `dueDate = 2026-10-17`, `today = 2026-10-20`, `isCompleted = false` / When `dueDateLabel` is called / Then it returns `"D+3"`.
  - An active task due today returns exactly `"D-Day"` (never `"D-0"` or `"D+0"`). `[Unit]`
    - Tip: Given `dueDate = 2026-10-20`, `today = 2026-10-20`, `isCompleted = false` / When `dueDateLabel` is called / Then it returns `"D-Day"`.
  - A multi-digit distance is rendered in full, without padding. `[Unit]`
    - Tip: Given `dueDate = 2027-10-20`, `today = 2026-10-20`, `isCompleted = false` / When `dueDateLabel` is called / Then it returns `"D-365"`.
  - An active task due across a month boundary uses the full calendar-day distance, not a month/day component. `[Unit]`
    - Tip: Given `dueDate = 2026-03-01`, `today = 2026-01-31`, `isCompleted = false` / When `dueDateLabel` is called / Then it returns `"D-29"`.
- Error cases
  - None; every representable input is valid. `[Unit]`
    - Tip: Given `dueDate = null`, `today = 2026-10-20`, `isCompleted = false` / When `dueDateLabel` is called / Then it returns `""` without throwing.
- Edge cases
  - Completion boundary: a completed task due today returns `"CLEAR"`, not `"D-Day"`. `[Unit]`
    - Tip: Given `dueDate = 2026-10-20`, `today = 2026-10-20`, `isCompleted = true` / When `dueDateLabel` is called / Then it returns `"CLEAR"`.
  - Completion boundary: a completed task that is overdue returns `"CLEAR"`, not `"D+N"`. `[Unit]`
    - Tip: Given `dueDate = 2026-10-17`, `today = 2026-10-20`, `isCompleted = true` / When `dueDateLabel` is called / Then it returns `"CLEAR"`.
  - Zero boundary, past side: the day before today returns `"D+1"`. `[Unit]`
    - Tip: Given `dueDate = 2026-10-19`, `today = 2026-10-20`, `isCompleted = false` / When `dueDateLabel` is called / Then it returns `"D+1"`.
  - Zero boundary, future side: the day after today returns `"D-1"`. `[Unit]`
    - Tip: Given `dueDate = 2026-10-21`, `today = 2026-10-20`, `isCompleted = false` / When `dueDateLabel` is called / Then it returns `"D-1"`.
  - Month boundary, past side: the distance is symmetric to the future side. `[Unit]`
    - Tip: Given `dueDate = 2026-01-31`, `today = 2026-03-01`, `isCompleted = false` / When `dueDateLabel` is called / Then it returns `"D+29"`.
  - Leap-day boundary, future side: March 1, 2024 is two days after February 28, 2024. `[Unit]`
    - Tip: Given `dueDate = 2024-03-01`, `today = 2024-02-28`, `isCompleted = false` / When `dueDateLabel` is called / Then it returns `"D-2"`.
  - Leap-day boundary, past side: February 28, 2024 is two days before March 1, 2024. `[Unit]`
    - Tip: Given `dueDate = 2024-02-28`, `today = 2024-03-01`, `isCompleted = false` / When `dueDateLabel` is called / Then it returns `"D+2"`.
  - Non-leap-year contrast: March 1, 2026 is one day after February 28, 2026 (no Feb 29). `[Unit]`
    - Tip: Given `dueDate = 2026-03-01`, `today = 2026-02-28`, `isCompleted = false` / When `dueDateLabel` is called / Then it returns `"D-1"`.
  - Year boundary, future side: January 1, 2027 is one day after December 31, 2026. `[Unit]`
    - Tip: Given `dueDate = 2027-01-01`, `today = 2026-12-31`, `isCompleted = false` / When `dueDateLabel` is called / Then it returns `"D-1"`.
  - Year boundary, past side: December 31, 2026 is one day before January 1, 2027. `[Unit]`
    - Tip: Given `dueDate = 2026-12-31`, `today = 2027-01-01`, `isCompleted = false` / When `dueDateLabel` is called / Then it returns `"D+1"`.