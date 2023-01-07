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
    when (readType) {
        // split line by line
        "l" -> return inputString.split("\r\n") as ArrayList<String>
        else -> throw Exception("Invalid split type (broken readFile)")
    }
}