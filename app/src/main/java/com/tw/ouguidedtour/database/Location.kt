package com.tw.ouguidedtour.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*


object Locations : IntIdTable() {
    val locationName: Column<String> = varchar("location_name", 50)
    val nextLocation: Column<EntityID<Int>?> = reference("next_location", Locations).nullable()
    val videoUrl: Column<String> = varchar("video_url", 100)
    val description: Column<Long> = long("description")

}

class Location(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<Location>(Locations)

    // The name of the location
    var locationName by Locations.locationName

    // The next Location in the tour
    var nextLocation by Locations.nextLocation

    // The URL of the video for this location
    var videoUrl by Locations.videoUrl

    // A description of the location which can be used to be shown on screen or as subtitles
    var description by Locations.description

}

