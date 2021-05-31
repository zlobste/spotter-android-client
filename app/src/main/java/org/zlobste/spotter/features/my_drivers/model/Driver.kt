package org.zlobste.spotter.features.my_drivers.model

data class Driver(
    val driverId: String,
    val organizationId: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val bDay: String,
    val isDrunk: Boolean
)