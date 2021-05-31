package org.zlobste.spotter.features.my_drivers.model

object MyDrivers {
    val driversList = hashMapOf(
        "1" to Driver(
            driverId = "1",
            organizationId = "1",
            firstName = "Nick",
            lastName = "Krainiuk",
            email = "nkrayniuk@gmail.com",
            bDay = "21 Feb 2001",
            isDrunk = true
        ),
        "2" to Driver(
            driverId = "2",
            organizationId = "2",
            firstName = "Valeria",
            lastName = "Radchenko",
            email = "valeria.radchenko@nure.ua",
            bDay = "2 Nov 2000",
            isDrunk = true
        ),
        "3" to Driver(
            driverId = "3",
            organizationId = "6",
            firstName = "Ivan",
            lastName = "Vladimirov",
            email = "vladimirov.ivan@nure.ua",
            bDay = "5 Aug 2000",
            isDrunk = false
        ),
    )
}