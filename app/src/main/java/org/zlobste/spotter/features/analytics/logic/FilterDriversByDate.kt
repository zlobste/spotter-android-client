package org.zlobste.spotter.features.analytics.logic

import org.zlobste.spotter.features.my_drivers.model.Driver
import org.zlobste.spotter.features.my_drivers.model.MyDrivers
import java.util.*

class FilterDriversByDate {
    companion object {
        fun getDrivers(startDate: Date, endDate: Date): List<Driver> {
            val matchedDrivers = mutableListOf<Driver>()

            val matchedConditions = Conditions.conditions.filter {
                it.checkUpTime.after(startDate) && it.checkUpTime.before(endDate)
            }

            matchedConditions.map {
                if (MyDrivers.driversList.containsKey(it.driverId))
                    matchedDrivers.add(MyDrivers.driversList[it.driverId]!!)
            }

            return matchedDrivers
        }

        fun getDriverLevelById(id: String, startDate: Date, endDate: Date): Int {
            var level = 0
            getDrivers(startDate, endDate).map {
                if (it.driverId == id) level ++
            }

            return level
        }
    }
}