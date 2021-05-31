package org.zlobste.spotter.features.analytics.logic

import android.os.Build
import androidx.annotation.RequiresApi
import org.zlobste.spotter.features.analytics.model.Condition
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

object Conditions {
    @RequiresApi(Build.VERSION_CODES.O)
    val conditions = listOf(
        Condition(
            conditionId = "1",
            driverId = "1",
            alcoholPpm = 3.14,
            checkUpTime = getDateFromLocal(LocalDate.parse("2020-01-01"))
        ),
        Condition(
            conditionId = "2",
            driverId = "1",
            alcoholPpm = 6.18,
            checkUpTime = getDateFromLocal(LocalDate.parse("2020-06-01"))
        ),
        Condition(
            conditionId = "3",
            driverId = "2",
            alcoholPpm = 0.65,
            checkUpTime = getDateFromLocal(LocalDate.parse("2020-09-01"))
        ),
        Condition(
            conditionId = "4",
            driverId = "2",
            alcoholPpm = 1.34,
            checkUpTime = getDateFromLocal(LocalDate.parse("2020-10-01"))
        ),
        Condition(
            conditionId = "5",
            driverId = "3",
            alcoholPpm = 7.65,
            checkUpTime = getDateFromLocal(LocalDate.parse("2020-12-01"))
        ),
        Condition(
            conditionId = "6",
            driverId = "3",
            alcoholPpm = 0.65,
            checkUpTime = getDateFromLocal(LocalDate.parse("2020-12-10"))
        ),
        Condition(
            conditionId = "7",
            driverId = "3",
            alcoholPpm = 1.1,
            checkUpTime = getDateFromLocal(LocalDate.parse("2020-12-11"))
        ),
        Condition(
            conditionId = "8",
            driverId = "3",
            alcoholPpm = 0.9,
            checkUpTime = getDateFromLocal(LocalDate.parse("2020-12-09"))
        ),
    )

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDateFromLocal(local: LocalDate): Date {
        return Date.from(
            local.atStartOfDay(ZoneId.systemDefault()).toInstant()
        )
    }
}