import com.use.goodNeighbors
import com.use.readFile

fun main () {
    val rawData = readFile("d12.dat")

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
        val currentLocation = route.removeAt(0)
        val currentSteps = travelCost[currentLocation[0]][currentLocation[1]]

        // exit if we're at end of maze
        if (map[currentLocation[0]][currentLocation[1]] == 27) {
            println("I found the exit after $currentSteps steps.")
            break@running
        }

        // establish valid moves
        val checkList = goodNeighbors(currentLocation[0], currentLocation[1], map.size, map[0].size)
        // loop across moves to add them to the route stack for later check
        for (move in checkList) {
            // first break the check if there is a "wall" of > 1 difference in height, continue to next item
            if (map[move[0]][move[1]] - map[currentLocation[0]][currentLocation[1]] > 1) continue
            // is it cheaper to move this route than the currently cheapest way?
            if (currentSteps + 1 < travelCost[move[0]][move[1]]) {
                // if so then record this new cheaper route
                travelCost[move[0]][move[1]] = currentSteps + 1
                // add this valid moves to the check stack
                route.add(move.toIntArray())
            }
        }
    }

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