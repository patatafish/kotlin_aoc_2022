package com.use

import java.io.File
import java.io.BufferedReader
import java.lang.Exception

fun readFile(fileName: String, readType: String = "l"): ArrayList<String> {

    // check to see if our data file exists
    val myFile = File("dat\\$fileName")
    if (!myFile.exists()) throw Exception("Sorry file $myFile doesn't exist! (broken readFile)")

    // read file using BufferedReader
    val bufferedReader: BufferedReader = myFile.bufferedReader()
    val inputString = bufferedReader.use { it.readText() }

    // parse file to array using the preferred separators
    return when (readType) {
        // split line by line
        "l" -> inputString.split("\r\n") as ArrayList<String>
        // split by empty line
        "bl" -> inputString.split("\r\n\r\n") as ArrayList<String>
        else -> throw Exception("Invalid split type (broken readFile)")
    }
}

fun goodNeighbors(row: Int,
                  col: Int,
                  maxRow: Int,
                  maxCol: Int,
                  diagonal: Boolean = false): MutableList<Array<Int>> {
    // the new list of neighbor cells
    val neighbor = mutableListOf<Array<Int>>()
    val goodNeighbor = mutableListOf<Array<Int>>()
    // fill the list with all our neighbors
    neighbor.add(arrayOf(row-1, col))
    neighbor.add(arrayOf(row+1, col))
    neighbor.add(arrayOf(row, col-1))
    neighbor.add(arrayOf(row, col+1))
    // if we need all 8 neighbors, add the diagonal as well
    if (diagonal) {
        neighbor.add(arrayOf(row-1, col-1))
        neighbor.add(arrayOf(row+1, col-1))
        neighbor.add(arrayOf(row+1, col-1))
        neighbor.add(arrayOf(row-1, col+1))
    }

    // check for out-of-bounds, only record in-bounds
    for (i in neighbor.indices) {
        if (neighbor[i][0] < 0 || neighbor[i][1] < 0) continue
        if (neighbor[i][0] > maxRow-1 || neighbor[i][1] > maxCol-1) continue
        goodNeighbor.add(neighbor[i])
    }
    return goodNeighbor
}