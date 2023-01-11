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
                  diagonal<Boolean> = false): Array<Array<Int>> {
    // the new list of neighbor cells
    val neighbor = mutableListOf<Array<Int>>()
    // fill the list with all our neighbors
    neighbor.add(arrayOf(row-1, col))
    neighbor.add(arrayOf(row+1, col))
    neighbor.add(arrayOf(row, col-1))
    neighbor.add(arrayOf(row, col+1))


}