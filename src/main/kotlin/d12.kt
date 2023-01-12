import com.use.readFile

fun main() {
    val rawData = readFile("test.dat")

    val heightMap = makeMap(rawData)

    runMaze(heightMap)

    println("exiting...")
}

class Map () {
    val map = mutableListOf<Node>()
    var start = arrayOf(-1, -1)
    var end = arrayOf(-1, -1)

    fun setEnds() {
        println("Establishing the start and end of this maze...")
        for (node in map) {
            when (node.char) {
                'S' -> start = arrayOf(node.row, node.col)
                'E' -> end = arrayOf(node.row, node.col)
                else -> continue
            }
        }
        println("Start: (${start[0]}, ${start[1]})\nEnd: (${end[0]}, ${end[1]})")
    }
}

class Node (myRow: Int, myCol: Int, myChar: Char) {
    val row = myRow
    val col = myCol
    val char = myChar
    var cost = Int.MAX_VALUE
    var visited = false
    var previous = arrayOf(-1, -1)
    val height = when (char) {
        'S' -> 0
        'E' -> 27
        else -> (char.code - 96)
    }
}

fun runMaze(maze: Map) {
    println("Running the maze...")
    maze.setEnds()
    for (node in maze.map) {

    }
}


fun showMaze(travelCost: Array<Array<Int>>) {
    for (row in travelCost) {
        for (col in row) {
            // int value to string for print of maze, unvisited cells get a .
            var formattedString = if (col == Int.MAX_VALUE) "."
            else col.toString()
            // pad the string for column output
            while (formattedString.length < 4) {
                formattedString = " $formattedString"
            }
            print(formattedString)
        }
        println()
    }
}

fun makeMap(myData: ArrayList<String>): Map {
    val newMap = Map()
    for (row in 0 until myData.size) {
        for (col in 0 until myData[0].length) {
            val node = Node(row, col, myData[row][col])
            newMap.map.add(node)
        }
    }
    return newMap
}