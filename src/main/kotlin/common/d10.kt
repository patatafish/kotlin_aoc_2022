package common

import com.use.readFile
import java.util.LinkedList
import java.util.Queue
import kotlin.math.abs

fun main() {

    val rawData = readFile("d10.dat")

    val signalQueue: Queue<String> = LinkedList()

    // part 1 variables
    // val interested = arrayOf(20, 60, 100, 140, 180, 220)
    // var signalStrength = 0

    // part 2 variables
    var currentPixel = 0


    // register has two elements, the value at [0] and the cycle number at [1]
    val register = arrayOf(1, 1)

    // load the instruction set into the queue
    for (line in rawData) {
        // add the instruction to the queue
        signalQueue.add("noop")
        // if the instruction was addx, keep the noop (the first cycle where addx does nothing)
        // and add the string value of the integer to add for the next instruction
        if (line.contains("addx")) signalQueue.add(line.replace("addx ", ""))
    }
    // process the queue
    for (line in signalQueue) {
        // increase the register cycle count
        register[1]++

        // output the CRT pixel (part 2)
        if (abs(currentPixel-register[0]) <= 1) print("X ")
        else print("  ")
        // increase the pixel
        currentPixel++
        if (currentPixel == 40) {
            println()
            currentPixel = 0
        }


        // do nothing if noop, otherwise process.
        if (line != "noop") {
            register[0] += line.toInt()
        }
/*
        // check to see if we are interested in this cycle (part 1)
        if (interested.contains(register[1])) {
            signalStrength += (register[0] * register[1])
            println("Cycle ${register[1]} holds value ${register[0]}")
            println("Signal strength is currently $signalStrength")

        }
*/


    }
}

