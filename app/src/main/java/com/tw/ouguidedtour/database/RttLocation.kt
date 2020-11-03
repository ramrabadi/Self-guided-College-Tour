package com.tw.ouguidedtour.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*


object RttLocations : IntIdTable() {
    // The location that is represented by these four nodes
    val locationName: Column<String> = varchar("location_name", 50)

    // Node 1 should be the closest RTT Node
    val r1: Column<String> = varchar("r1", 50)
    val d1: Column<Int> = integer("d1")

    // Node 2 should be the second closest RTT Node
    val r2: Column<String> = varchar("r2", 50)
    val d2: Column<Int> = integer("d2")

    // Node 3 should be the third closest RTT Node
    val r3: Column<String> = varchar("r3", 50)
    val d3: Column<Int> = integer("d3")

    // Node 4 should be the farthest RTT Node
    val r4: Column<String> = varchar("r4", 50)
    val d4: Column<Int> = integer("d4")
}

class RttLocation(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<RttLocation>(RttLocations)

    var locationName by RttLocations.locationName

    // Node 1 should be the closest RTT Node
    var r1 by RttLocations.r1
    var d1 by RttLocations.d1

    // Node 2 should be the second closest RTT Node
    var r2 by RttLocations.r2
    var d2 by RttLocations.d2

    // Node 3 should be the third closest RTT Node
    var r3 by RttLocations.r3
    var d3 by RttLocations.d3

    // Node 4 should be the farthest RTT Node
    var r4 by RttLocations.r4
    var d4 by RttLocations.d4
}