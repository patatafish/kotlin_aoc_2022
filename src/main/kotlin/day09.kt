import com.use.readFile
import java.lang.Exception


/**
 * returns an array object of len items, all initialized to IntArray(0, 0)
 * @return Array
 */
fun makeString(len: Int) = Array(len) { IntArray(2) }

fun whip(myData: MutableList<String>, ropeLen: Int = 2) {
    // create the new rope
    val rope = makeString(ropeLen)
    // created a visited list
    val visitedSites: ArrayList<IntArray> = arrayListOf()
    visitedSites.add(intArrayOf(0, 0))
    // loop across the instructions
    while (myData.isNotEmpty()) {
        // parse direction and size of movement
        val (direction: String, size: String) = myData.removeFirst().split(' ')
        //move head of rope i times
        println("$direction $size")
        for (i in 0 until size.toInt()) {
            when (direction) {
                // moving x coordinate
                "R" -> {rope[0][0]++; andNaeNae(rope)}
                "L" -> {rope[0][0]--; andNaeNae(rope)}
                // moving y coordinate
                "U" -> {rope[0][1]++; andNaeNae(rope)}
                "D" -> {rope[0][1]--; andNaeNae(rope)}
                else -> throw Exception("Invalid movement direction!")
            }
            if (!visitedSites.any { it.contentEquals(rope.last()) }){
                val test = rope.last().copyOf()
                visitedSites.add(test)
            }
        }
        showRope(rope)
    }
    println("The tail has seen ${visitedSites.size} spaces")
}

fun andNaeNae(myRope: Array<IntArray>) {
    // loop across each link the rope, using the array index
    for (i in 1 until myRope.size) {
        // print("[${myRope[i][0]}, ${myRope[i][1]}] [${myRope[i-1][0]}, ${myRope[i-1][1]}]")
        if (kotlin.math.abs(myRope[i - 1][0] - myRope[i][0]) > 1) {
            // print("xdif")
            if (kotlin.math.abs(myRope[i-1][1] - myRope[i][1]) > 0){
                // print("&ydif")
                myRope[i][1] += if (myRope[i-1][1] < myRope[i][1]) -1 else 1
                myRope[i][0] += if (myRope[i-1][0] < myRope[i][0]) -1 else 1
            }
            else myRope[i][0] += if (myRope[i-1][0] < myRope[i][0]) -1 else 1
        }
        else if (kotlin.math.abs(myRope[i - 1][1] - myRope[i][1]) > 1) {
            // print("ydif")
            if (kotlin.math.abs(myRope[i-1][0] - myRope[i][0]) > 0){
                // print("&xdif")
                myRope[i][0] += if (myRope[i-1][0] < myRope[i][0]) -1 else 1
                myRope[i][1] += if (myRope[i-1][1] < myRope[i][1]) -1 else 1
            }
            else myRope[i][1] += if (myRope[i-1][1] < myRope[i][1]) -1 else 1
        }
        // println("->[${myRope[i][0]}, ${myRope[i][1]}] [${myRope[i-1][0]}, ${myRope[i-1][1]}]")
    }
}

fun showRope(myRope: Array<IntArray>) {
    // get the scope of our grid to print
    var (xMax: Int, xMin: Int, yMax: Int, yMin: Int) = listOf(0, 0, 0, 0)
    for (link in myRope) {
        if (link[0] > xMax) xMax = link[0]
        if (link[0] < xMin) xMin = link[0]
        if (link[1] > yMax) yMax = link[1]
        if (link[1] < yMin) yMin = link[1]
    }
    // create a margin around our grid
    xMax += 2
    xMin -= 2
    yMax += 2
    yMin -= 2

    // print the grid
    for (y in yMax downTo yMin) {
        for (x in xMin..xMax) {
            val myCoord = intArrayOf(x, y)
            if (myRope.any { it.contentEquals(myCoord) }) {
                val matchingIndex = myRope.indexOfFirst { it.contentEquals(myCoord) }
                print("$matchingIndex  ")
            } else {
                when {
                    x == 0 && y == 0 -> print("O  ")
                    x == 0 -> print("|  ")
                    y == 0 -> print("_  ")
                    else -> print("`  ")
                }
            }
        }
        println()
    }
    println()
}
fun main() {
    val rawData = readFile("d09.dat")
    println(rawData)
    // part 1
    // whip(rawData.toMutableList())
    // println(rawData)
    // part 2
    whip(rawData.toMutableList(), 10)
}
