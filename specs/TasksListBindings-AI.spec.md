# Spec: `TasksListBindings.kt`

> Post-conditions are the test oracle: write the expected behavior from intent, not from the code.
> Tag each post-condition [Unit] if its test runs on the JVM; otherwise tag it [Integration].
> End each valid case with `Tested by <test>` if an existing test checks it, or `Tip:` describing the test to write.

## `setDueDateLabel(textView: TextView, task: Task?): Unit`

**Intent**: Bind a task's due-date status label to its list `TextView`. Show non-empty labels and clear and hide the view when there is no label, so a recycled row never displays a previous task's label.

**Pre-conditions**
- `textView` is a valid `TextView`.
- `task` is either null or a valid task; the current local date is read when the binding is applied.

**Post-conditions**
- Success cases
  - A completed task displays `"CLEAR"` and the `TextView` is visible, even if the task has no due date. `[Integration]`
    - Tip: Given a `TextView` with text `"old"` and visibility `GONE`, a completed task with `dueDate = null`, and the current local date `2026-10-20` / When `setDueDateLabel` is called / Then the text is `"CLEAR"` and visibility is `VISIBLE`.
  - An active task due today displays `"D-Day"` and the `TextView` is visible. `[Integration]`
    - Tip: Given a `TextView` with text `""` and visibility `GONE`, an active task with `dueDate = 2026-10-20`, and the current local date `2026-10-20` / When `setDueDateLabel` is called / Then the text is `"D-Day"` and visibility is `VISIBLE`.
  - An active task due in the future displays the correct countdown and the `TextView` is visible. `[Integration]`
    - Tip: Given a `TextView` with text `""` and visibility `GONE`, an active task with `dueDate = 2026-10-23`, and the current local date `2026-10-20` / When `setDueDateLabel` is called / Then the text is `"D-3"` and visibility is `VISIBLE`.
  - An active overdue task displays the correct elapsed-day label and the `TextView` is visible. `[Integration]`
    - Tip: Given a `TextView` with text `""` and visibility `GONE`, an active task with `dueDate = 2026-10-17`, and the current local date `2026-10-20` / When `setDueDateLabel` is called / Then the text is `"D+3"` and visibility is `VISIBLE`.
- Error cases
  - None; a null task is a valid input and should clear the label. `[Integration]`
    - Tip: Given a `TextView` with text `"D-3"` and visibility `VISIBLE`, and `task = null` / When `setDueDateLabel` is called / Then the text is `""` and visibility is `GONE` without throwing.
- Edge cases
  - For an active task with no due date, the view is cleared and hidden, including when it previously displayed a label. `[Integration]`
    - Tip: Given a `TextView` with text `"D-3"` and visibility `VISIBLE`, an active task with `dueDate = null`, and the current local date `2026-10-20` / When `setDueDateLabel` is called / Then the text is `""` and visibility is `GONE`.
  - For an active task due the day before, on, or after the current date, the displayed labels are respectively `"D+1"`, `"D-Day"`, and `"D-1"`; visibility is `VISIBLE` in all three cases. `[Integration]`
    - Tip: Given three active tasks with `dueDate = 2026-10-19`, `2026-10-20`, and `2026-10-21` and the current local date `2026-10-20` / When each task is bound to a `TextView` / Then their text values are `"D+1"`, `"D-Day"`, and `"D-1"` and each view is `VISIBLE`.
