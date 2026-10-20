# Spec: `DueDateUtils.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `dueDateLabel(dueDate: LocalDate?, today: LocalDate, isCompleted: Boolean): String`

**Intent**: Return the task's status label: completed tasks show `"CLEAR"`; active tasks without a due date show no label; active tasks with a due date show the signed calendar-day distance from today.

**Pre-conditions**
- `today` is a valid `LocalDate`; `dueDate` is either null or a valid `LocalDate`.

**Post-conditions**
- Success cases
  - A completed task returns `"CLEAR"` regardless of whether `dueDate` is null or non-null. `[Unit]`
    - Tip: Given `dueDate = 2026-10-21`, `today = 2026-10-20`, and `isCompleted = true` / When `dueDateLabel` is called / Then it returns `"CLEAR"`.
  - An active task with no due date returns an empty string. `[Unit]`
    - Tip: Given `dueDate = null`, `today = 2026-10-20`, and `isCompleted = false` / When `dueDateLabel` is called / Then it returns `""`.
  - An active task due in the future returns `"D-N"`, where `N` is the full number of calendar days until the due date. `[Unit]`
    - Tip: Given `dueDate = 2026-10-23`, `today = 2026-10-20`, and `isCompleted = false` / When `dueDateLabel` is called / Then it returns `"D-3"`.
  - An active task due in the past returns `"D+N"`, where `N` is the full number of calendar days since the due date. `[Unit]`
    - Tip: Given `dueDate = 2026-10-17`, `today = 2026-10-20`, and `isCompleted = false` / When `dueDateLabel` is called / Then it returns `"D+3"`.
  - An active task due across a month boundary uses the full calendar-day distance. `[Unit]`
    - Tip: Given `dueDate = 2026-03-01`, `today = 2026-01-31`, and `isCompleted = false` / When `dueDateLabel` is called / Then it returns `"D-29"`.
- Error cases
  - None; every representable input is valid. `[Unit]`
    - Tip: Given `dueDate = null`, `today = 2026-10-20`, and `isCompleted = false` / When `dueDateLabel` is called / Then it returns `""` without throwing.
- Edge cases
  - The day before today returns `"D+1"`, the exact day returns `"D-Day"`, and the day after today returns `"D-1"`. `[Unit]`
    - Tip: Given `today = 2026-10-20` and `isCompleted = false` / When `dueDateLabel` is called with `dueDate = 2026-10-19`, `2026-10-20`, and `2026-10-21` / Then the results are `"D+1"`, `"D-Day"`, and `"D-1"` respectively.
  - A leap-day boundary counts each calendar day: March 1, 2024 is two days after February 28, 2024. `[Unit]`
    - Tip: Given `dueDate = 2024-03-01`, `today = 2024-02-28`, and `isCompleted = false` / When `dueDateLabel` is called / Then it returns `"D-2"`.
  - A year boundary counts each calendar day: January 1, 2027 is one day after December 31, 2026. `[Unit]`
    - Tip: Given `dueDate = 2027-01-01`, `today = 2026-12-31`, and `isCompleted = false` / When `dueDateLabel` is called / Then it returns `"D-1"`.

