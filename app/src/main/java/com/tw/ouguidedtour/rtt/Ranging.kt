package com.tw.ouguidedtour.rtt

import com.tw.ouguidedtour.database.RttNode

/**
 *  Function: getThreeClosestNodes
 *
 *  Purpose: This function takes a List of RttNode's and finds the three closest nodes which is the users location.
 *
 *  Parameters:     nodes: List<RttNode>, a list of RttNode objects
 *
 *  Pre-condition:  Non empty List
 *
 *  Post-condition: Returns userLocation which is a list of three nodes which is meant to represent the user's current
 *                  location and will be used to check if the user has made it to a location.
 *
 */
fun getThreeClosestNodes(nodes: List<RttNode>): MutableList<RttNode>  {
    val userLocation: MutableList<RttNode> = mutableListOf()

    // Create three blank nodes
    val node1 = RttNode()
    val node2 = RttNode()
    val node3 = RttNode()

    userLocation.add(node1)
    userLocation.add(node2)
    userLocation.add(node3)

    try {
        if (nodes.size <= 2) {
            throw IllegalArgumentException("List must have more than 2 nodes")
        }
    } catch (e: IllegalArgumentException) {
        return userLocation
    }

    userLocation[0].setDistance(Float.MAX_VALUE)
    userLocation[1].setDistance(Float.MAX_VALUE)
    userLocation[2].setDistance(Float.MAX_VALUE)
    for (node in nodes) {
        if (userLocation[0].getDistance() > node.getDistance() ) {

            userLocation[2].create(userLocation[1].getRssi(), userLocation[1].getDistance())

            userLocation[1].create(userLocation[0].getRssi(), userLocation[0].getDistance())

            userLocation[0].create(node.getRssi(), node.getDistance())

        } else if (userLocation[1].getDistance() > node.getDistance() ) {
            userLocation[2].create(userLocation[1].getRssi(), userLocation[1].getDistance())

            userLocation[1].create(node.getRssi(), node.getDistance())

        } else if (userLocation[2].getDistance() > node.getDistance()) {

            userLocation[2].create(node.getRssi(), node.getDistance())
        }
    }

    return userLocation
}
