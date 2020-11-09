package com.tw.ouguidedtour.rtt

import com.tw.ouguidedtour.database.RttNode
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import timber.log.Timber

class RangingKtTest {

    @Before
    fun setUp() {
    }

    @Test
    fun getThreeClosestNodes_2_nodes_returns_blank_list() {
        val node: MutableList<RttNode> = mutableListOf()
        val node1 = RttNode()
        val node2 = RttNode()

        node.add(node1)
        node.add(node2)

        val result = getThreeClosestNodes(node)

        assertEquals("0",result[0].getRssi())
        assertEquals(0, result[0].getDistance())
        assertEquals("0",result[1].getRssi())
        assertEquals(0, result[1].getDistance() )
        assertEquals("0", result[2].getRssi())
        assertEquals(0, result[2].getDistance())
    }

    @Test
    fun getThreeClosestNodes_3_nodes_returns_rssi_1_2_3 () {
        val node: MutableList<RttNode> = mutableListOf()
        val node1 = RttNode()
        val node2 = RttNode()
        val node3 = RttNode()

        node1.create("1",1)
        node2.create("2",2)
        node3.create("3",3)


        node.add(node1)
        node.add(node2)
        node.add(node3)
        val result = getThreeClosestNodes(node)

        assertEquals(3, result.size)
        assertEquals("1", result[0].getRssi() )
        assertEquals("2",result[1].getRssi() )
        assertEquals("3", result[2].getRssi() )
    }

    @Test
    fun getThreeClosestNodes_sends_10_nodes_should_only_receive_3_back() {
        val node: MutableList<RttNode> = mutableListOf()
        val node1 = RttNode()
        val node2 = RttNode()
        val node3 = RttNode()
        val node4 = RttNode()
        val node5 = RttNode()
        val node6 = RttNode()
        val node7 = RttNode()
        val node8 = RttNode()
        val node9 = RttNode()
        val node10 = RttNode()

        node1.create("a",10)
        node2.create("b",2)
        node3.create("c",3)
        node4.create("d",4)
        node5.create("e",5)
        node6.create("f",6)
        node7.create("g",7)
        node8.create("h",8)
        node9.create("i",9)
        node10.create("j", 1)


        node.add(node1)
        node.add(node2)
        node.add(node3)
        node.add(node4)
        node.add(node5)
        node.add(node6)
        node.add(node7)
        node.add(node8)
        node.add(node9)
        node.add(node10)
        val result = getThreeClosestNodes(node)

        assertEquals(3, result.size)
        assertEquals("j", result[0].getRssi() )
        assertEquals("b", result[1].getRssi() )
        assertEquals("c", result[2].getRssi() )
    }
}