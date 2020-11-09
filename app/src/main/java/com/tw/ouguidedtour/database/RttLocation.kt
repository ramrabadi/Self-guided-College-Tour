package com.tw.ouguidedtour.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*


object RttLocations : IntIdTable() {
    // The location that is represented by these four nodes
    val locationName: Column<String> = varchar("location_name", 50)

    // Corner 1
    val rx1: Column<String> = varchar("rx1", 50)
    val dx1: Column<Int> = integer("dx1")
    val ry1: Column<String> = varchar("ry1", 50)
    val dy1: Column<Int> = integer("dy1")
    val rz1: Column<String> = varchar("rz1", 50)
    val dz1: Column<Int> = integer("dz1")

    // Corner 2
    val rx2: Column<String> = varchar("rx2", 50)
    val dx2: Column<Int> = integer("dx2")
    val ry2: Column<String> = varchar("ry2", 50)
    val dy2: Column<Int> = integer("dy2")
    val rz2: Column<String> = varchar("rz2", 50)
    val dz2: Column<Int> = integer("dz2")

    // Corner 3
    val rx3: Column<String> = varchar("rx3", 50)
    val dx3: Column<Int> = integer("dx3")
    val ry3: Column<String> = varchar("ry3", 50)
    val dy3: Column<Int> = integer("dy3")
    val rz3: Column<String> = varchar("rz3", 50)
    val dz3: Column<Int> = integer("dz3")

    // Corner 4
    val rx4: Column<String> = varchar("rx4", 50)
    val dx4: Column<Int> = integer("dx4")
    val ry4: Column<String> = varchar("ry4", 50)
    val dy4: Column<Int> = integer("dy4")
    val rz4: Column<String> = varchar("rz4", 50)
    val dz4: Column<Int> = integer("dz4")
}

class RttLocation(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<RttLocation>(RttLocations)

    var locationName by RttLocations.locationName

    // Corner 1
    var rx1 by RttLocations.rx1
    var dx1 by RttLocations.dx1
    var ry1 by RttLocations.ry1
    var dy1 by RttLocations.dy1
    var rz1 by RttLocations.rz1
    var dz1 by RttLocations.dz1

    // Corner 2
    var rx2 by RttLocations.rx2
    var dx2 by RttLocations.dx2
    var ry2 by RttLocations.ry2
    var dy2 by RttLocations.dy2
    var rz2 by RttLocations.rz2
    var dz2 by RttLocations.dz2

    // Corner 3
    var rx3 by RttLocations.rx3
    var dx3 by RttLocations.dx3
    var ry3 by RttLocations.ry3
    var dy3 by RttLocations.dy3
    var rz3 by RttLocations.rz3
    var dz3 by RttLocations.dz3

    // Corner 4
    var rx4 by RttLocations.rx4
    var dx4 by RttLocations.dx4
    var ry4 by RttLocations.ry4
    var dy4 by RttLocations.dy4
    var rz4 by RttLocations.rz4
    var dz4 by RttLocations.dz4

}