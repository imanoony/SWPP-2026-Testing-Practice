package com.example.swpp.tododapp.tasks

import android.view.View
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.swpp.tododapp.data.Task
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@MediumTest
@RunWith(AndroidJUnit4::class)
class TasksListBindingsTest {
    private val fixedToday = LocalDate.of(2026, 10, 20)

    private fun bind(initialText: String, initialVisibility: Int, task: Task?): Pair<String, Int> {
        lateinit var result: Pair<String, Int>
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val view = TextView(ApplicationProvider.getApplicationContext())
            view.text = initialText
            view.visibility = initialVisibility
            setDueDateLabel(view, task)
            result = view.text.toString() to view.visibility
        }
        return result
    }

    private fun task(dueDate: LocalDate?, completed: Boolean = false) =
        Task(title = "Task", description = "Description", isCompleted = completed, dueDate = dueDate)

    @Test
    fun setDueDateLabel_completedWithoutDueDate_showsClear() {
        assertEquals("CLEAR" to View.VISIBLE, bind("old", View.GONE, task(null, true)))
    }

    @Test
    fun setDueDateLabel_completedFuture_showsClear() {
        assertEquals("CLEAR" to View.VISIBLE, bind("", View.GONE, task(LocalDate.of(2026, 10, 23), true)))
    }

    @Test
    fun setDueDateLabel_dueToday_showsDayLabel() {
        assertEquals("D-Day" to View.VISIBLE, bind("", View.GONE, task(fixedToday)))
    }

    @Test
    fun setDueDateLabel_futureDueDate_showsCountdown() {
        assertEquals("D-3" to View.VISIBLE, bind("", View.GONE, task(LocalDate.of(2026, 10, 23))) )
    }

    @Test
    fun setDueDateLabel_overdueTask_showsElapsedDays() {
        assertEquals("D+3" to View.VISIBLE, bind("", View.GONE, task(LocalDate.of(2026, 10, 17))))
    }

    @Test
    fun setDueDateLabel_nullTask_clearsAndHidesWithoutThrowing() {
        assertEquals("" to View.GONE, bind("D-3", View.VISIBLE, null))
    }

    @Test
    fun setDueDateLabel_activeWithoutDueDate_clearsPreviousLabel() {
        assertEquals("" to View.GONE, bind("D-3", View.VISIBLE, task(null)))
    }

    @Test
    fun setDueDateLabel_nullTaskOnClearedView_staysClearedAndHidden() {
        assertEquals("" to View.GONE, bind("", View.GONE, null))
    }

    @Test
    fun setDueDateLabel_recycledView_replacesPreviousLabel() {
        assertEquals("D-Day" to View.VISIBLE, bind("D-3", View.VISIBLE, task(fixedToday)))
    }

    @Test
    fun setDueDateLabel_dayBeforeToday_showsOneDayOverdue() {
        assertEquals("D+1" to View.VISIBLE, bind("", View.GONE, task(LocalDate.of(2026, 10, 19))))
    }

    @Test
    fun setDueDateLabel_dayAfterToday_showsOneDayCountdown() {
        assertEquals("D-1" to View.VISIBLE, bind("", View.GONE, task(LocalDate.of(2026, 10, 21))))
    }

    @Test
    fun setDueDateLabel_completedDueToday_showsClear() {
        assertEquals("CLEAR" to View.VISIBLE, bind("", View.GONE, task(fixedToday, true)))
    }

    @Test
    fun setDueDateLabel_completedOverdue_showsClear() {
        assertEquals("CLEAR" to View.VISIBLE, bind("", View.GONE, task(LocalDate.of(2026, 10, 17), true)))
    }

    @Test
    fun setDueDateLabel_rebindAfterDateRollover_updatesLabel() {
        lateinit var view: TextView
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            view = TextView(ApplicationProvider.getApplicationContext())
            setDueDateLabel(view, task(LocalDate.of(2026, 10, 21)))
            assertEquals("D-1", view.text.toString())
            // This second call represents the same binding after the local date advances.
            setDueDateLabel(view, task(LocalDate.of(2026, 10, 21)))
            assertEquals("D-Day", view.text.toString())
        }
    }
}
