import com.use.readFile

fun main () {
    val rawData = readFile("test.dat")

    val heightMap = makeMap(rawData)

    runMaze(heightMap)

    println("exiting...")
}


fun runMaze(map: Array<Array<Int>>) {
    println("Running the maze...")
    // create a copy of the map with travel cost values for fastest route
    val travelCost = Array(map.size) { Array(map[0].size) { Int.MAX_VALUE } }
    // stack for running through maze
    val route = mutableListOf<IntArray>()

    // find the start of the maze, add that to the route stack
    search_loop@
    for (row in map.indices) {
        for (cell in map[row].indices) {
            if (map[row][cell] == 0) {
                route.add(intArrayOf(row, cell))
                break@search_loop // found the start, break and stop looking
            }
        }
    }
    println("Found the start of the maze at (${route[0][0]}, ${route[0][1]})")

    // set starting travel cost as 0
    travelCost[route[0][0]][route[0][1]] = 0
    // main loop to run maze
    running@
    while (true) {
        // establish our current cell from the route list
        var currentLocation = route.removeAt(0)
        // establish valid moves

        // evaluate costs
        // record if cheaper
        // exit if we've found it
        break@running
    }
}



fun makeMap(myData: ArrayList<String>): Array<Array<Int>> {
    var map: Array<Array<Int>> = arrayOf()
    for (row in myData) {
        var newRow = arrayOf<Int>()
        for (cell in row.toCharArray()) {
            newRow += when (cell) {
                'S' -> 0
                'E' -> 27
                else -> (cell.code - 96)
            }
        }
        map += newRow
    }
    return map
}