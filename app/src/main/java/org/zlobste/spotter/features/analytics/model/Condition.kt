package org.zlobste.spotter.features.analytics.model

import java.util.*

data class Condition (
    val conditionId: String,
    val driverId: String,
    val alcoholPpm: Double,
    val checkUpTime: Date
)