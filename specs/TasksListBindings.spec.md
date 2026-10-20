# Spec: `TasksListBindings.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `setDueDateLabel(textView: TextView, task: Task?): Unit`

**Intent**: Bind a task's due-date status label to its list `TextView`.
- When the task has a non-empty label, show it and make the view visible.
- When there is no label (null task, or active task without a due date), clear the text and hide the view, so a recycled row never displays a previous task's label.
- The label text is the same value `dueDateLabel(task.dueDate, today, task.isCompleted)` would return.

**State rules**
- "Cleared" means `text == ""`.
- "Hidden" means `visibility == View.GONE`. `INVISIBLE` is never used.
- After every call, the view is in exactly one of two states: (`text` non-empty, `VISIBLE`) or (`text == ""`, `GONE`).

**Pre-conditions**
- `textView` is a valid `TextView`.
- `task` is either null or a valid task.
- The current local date is read at bind time, not at task creation. Tests must be able to pin it (fixed clock or equivalent).

**Post-conditions**
- Success cases
  - A completed task with no due date displays `"CLEAR"` and the view is visible. `[Integration]`
    - Tip: Given a `TextView` with text `"old"` and visibility `GONE`, a completed task with `dueDate = null`, and the current local date `2026-10-20` / When `setDueDateLabel` is called / Then the text is `"CLEAR"` and visibility is `VISIBLE`.
  - A completed task with a future due date displays `"CLEAR"`, not a countdown. `[Integration]`
    - Tip: Given a `TextView` with text `""` and visibility `GONE`, a completed task with `dueDate = 2026-10-23`, and the current local date `2026-10-20` / When `setDueDateLabel` is called / Then the text is `"CLEAR"` and visibility is `VISIBLE`.
  - An active task due today displays `"D-Day"` and the view is visible. `[Integration]`
    - Tip: Given a `TextView` with text `""` and visibility `GONE`, an active task with `dueDate = 2026-10-20`, and the current local date `2026-10-20` / When `setDueDateLabel` is called / Then the text is `"D-Day"` and visibility is `VISIBLE`.
  - An active task due in the future displays `"D-N"` and the view is visible. `[Integration]`
    - Tip: Given a `TextView` with text `""` and visibility `GONE`, an active task with `dueDate = 2026-10-23`, and the current local date `2026-10-20` / When `setDueDateLabel` is called / Then the text is `"D-3"` and visibility is `VISIBLE`.
  - An active overdue task displays `"D+N"` and the view is visible. `[Integration]`
    - Tip: Given a `TextView` with text `""` and visibility `GONE`, an active task with `dueDate = 2026-10-17`, and the current local date `2026-10-20` / When `setDueDateLabel` is called / Then the text is `"D+3"` and visibility is `VISIBLE`.
- Error cases
  - None; a null task is a valid input and clears the label. `[Integration]`
    - Tip: Given a `TextView` with text `"D-3"` and visibility `VISIBLE`, and `task = null` / When `setDueDateLabel` is called / Then the text is `""` and visibility is `GONE` without throwing.
- Edge cases
  - No-label boundary: an active task with no due date clears and hides a view that previously showed a label. `[Integration]`
    - Tip: Given a `TextView` with text `"D-3"` and visibility `VISIBLE`, an active task with `dueDate = null`, and the current local date `2026-10-20` / When `setDueDateLabel` is called / Then the text is `""` and visibility is `GONE`.
  - No-label boundary: binding a no-label task to an already cleared and hidden view leaves it cleared and hidden. `[Integration]`
    - Tip: Given a `TextView` with text `""` and visibility `GONE`, and `task = null` / When `setDueDateLabel` is called / Then the text is `""` and visibility is `GONE`.
  - Recycled row: binding a labeled task to a view that shows a different label replaces the text and stays visible. `[Integration]`
    - Tip: Given a `TextView` with text `"D-3"` and visibility `VISIBLE`, an active task with `dueDate = 2026-10-20`, and the current local date `2026-10-20` / When `setDueDateLabel` is called / Then the text is `"D-Day"` and visibility is `VISIBLE`.
  - Zero boundary, past side: a task due the day before today displays `"D+1"`. `[Integration]`
    - Tip: Given a `TextView` with text `""` and visibility `GONE`, an active task with `dueDate = 2026-10-19`, and the current local date `2026-10-20` / When `setDueDateLabel` is called / Then the text is `"D+1"` and visibility is `VISIBLE`.
  - Zero boundary, future side: a task due the day after today displays `"D-1"`. `[Integration]`
    - Tip: Given a `TextView` with text `""` and visibility `GONE`, an active task with `dueDate = 2026-10-21`, and the current local date `2026-10-20` / When `setDueDateLabel` is called / Then the text is `"D-1"` and visibility is `VISIBLE`.
  - Completion boundary: a completed task due today displays `"CLEAR"`, not `"D-Day"`. `[Integration]`
    - Tip: Given a `TextView` with text `""` and visibility `GONE`, a completed task with `dueDate = 2026-10-20`, and the current local date `2026-10-20` / When `setDueDateLabel` is called / Then the text is `"CLEAR"` and visibility is `VISIBLE`.
  - Completion boundary: a completed overdue task displays `"CLEAR"`, not `"D+N"`. `[Integration]`
    - Tip: Given a `TextView` with text `""` and visibility `GONE`, a completed task with `dueDate = 2026-10-17`, and the current local date `2026-10-20` / When `setDueDateLabel` is called / Then the text is `"CLEAR"` and visibility is `VISIBLE`.
  - Date rollover: the current date is read at each bind, so rebinding the same task after midnight updates the label. `[Integration]`
    - Tip: Given an active task with `dueDate = 2026-10-21` bound on the current local date `2026-10-20` (text `"D-1"`) / When the current local date becomes `2026-10-21` and `setDueDateLabel` is called again on the same view / Then the text is `"D-Day"` and visibility is `VISIBLE`.