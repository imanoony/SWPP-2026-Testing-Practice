package com.example.swpp.tododapp.statistics

import org.junit.Assert.*
import org.junit.Test
import com.example.swpp.tododapp.data.Task

class StatisticsUtilsTest {
    @Test
    fun getActiveAndCompletedStats() {

    }

    @Test
    fun getActiveAndCompletedStats_two_return50f50f() {
        val tasks = listOf(
            Task("t1", "desc", true, "Test1")
        )
    }

    @Test
    fun getActiveAndCompletedStats_zero_return0f0f(){
        val tasks = emptyList<Task>()
        val result = getActiveAndCompletedStats(tasks)
        assertEquals(0f, result.activeTasksPercent)
        assertEquals(0f, result.completedTasksPercent)
    }

    @Test
    fun getActiveAndCompletedStats_two_return0f100f(){
        val tasks = listOf(
            Task("t1","desc", true, "Test1"),
            Task("t2","desc", true, "Test2")
        )
        val result = getActiveAndCompletedStats(tasks)
        assertEquals(0f, result.activeTasksPercent)
        assertEquals(100f, result.completedTasksPercent)
    }

}