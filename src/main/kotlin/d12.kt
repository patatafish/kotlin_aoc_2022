import com.use.goodNeighbors
import com.use.readFile

fun main() {
    val rawData = readFile("d12.dat")

    val heightMap = makeMap(rawData)

    runMaze(heightMap)

    println("exiting...")
}

class Map {
    val map = mutableListOf<Node>()
    var maxRow = -1
    var maxCol = -1
    lateinit var start: Node
    lateinit var end: Node
    private val nullNode = Node(-1, -1, ' ')

    fun setEnds() {
        println("Establishing the start, end, and size of this maze...")
        for (node in map) {
            maxRow = if (node.row > maxRow) node.row else maxRow
            maxCol = if (node.col > maxCol) node.col else maxCol
            when (node.char) {
                'S' -> start = node
                'E' -> end = node
                else -> continue
            }
        }
        start.cost = 0
        start.previous = nullNode
        println("Start: (${start.row},${start.col}), End: (${end.row},${end.col})")
    }

    fun show() {
        // be sure we've run Map.setEnds() before we can print
        if (maxRow == -1) setEnds()
        // check if we've solved the whole maze, if so we should mark the cells of the shortest path
        if (end.visited) markSolved()
        // since we have a possible un-ordered list of cells, we need to create an ordered
        // array of items to print. we do this with a pre-sized array that we fill in one
        // cell at a time
        // create the empty array of arrays, to start we fill with newline so when printed the rows linebreak
        val myCharArray = Array(maxRow + 1) {Array(maxCol + 2) {"  \n"} }
        // load each cell from the map using row and col data
        // format the output for easy read by using S/E/char/travel cost
        for (cell in map) {
            if (cell.height == 0) myCharArray[cell.row][cell.col] = "S"
            else if (cell.height == 27) myCharArray[cell.row][cell.col] = "E"
            else if (cell.cost == Int.MAX_VALUE) myCharArray[cell.row][cell.col] = cell.char.toString()
            else myCharArray[cell.row][cell.col] = cell.cost.toString()
        }
        // loop across our newly ordered array and print the items
        println("\nOur Maze:")
        for (row in myCharArray.indices){
            for (col in myCharArray[row].indices) {
                // buffer the string with leading whitespace until it is 4 characters long
                for (i in 3 downTo  myCharArray[row][col].length) print(" ")
                if (!myCharArray[row][col].contains("\n")) getNode(row, col).color()
                print(myCharArray[row][col])
                print("\u001B[0m")
            }
        }
        print("\n")
    }

    private fun markSolved() {
        var current = end
        while (current.previous != nullNode) {
            current.solved = true
            current = current.previous
        }
    }

    // for use in part 2
    //fun setNull() = nullNode
    private fun getNode(row: Int, col: Int): Node {
        for (node in map) {
            if (node.row == row && node.col == col) return node
        }
        println("Could not find specified node...")
        return nullNode
    }

    fun getNode(myCoord: Array<Int>): Node {
        for (node in map) {
            if (node.row == myCoord[0] && node.col == myCoord[1]) return node
        }
        println("Could not find specified node...")
        return nullNode
    }
}

class Node (myRow: Int, myCol: Int, myChar: Char) {
    val row = myRow
    val col = myCol
    val char = myChar
    var cost = Int.MAX_VALUE
    var visited = false
    var solved = false
    lateinit var previous: Node
    val height = when (char) {
        'S' -> 0
        'E' -> 26
        else -> (char.code - 96)
    }

    fun color (){
        if (char == 'S' || char == 'E') print("\u001b[31;1m")
        else if (visited) print("\u001b[35;1m")
        if (solved) print("\u001b[30m\u001b[43m")
    }
}

fun runMaze(maze: Map) {
    println("Running the maze...")
    // create and load the search q
    val q = mutableListOf<Node>()
    q.add(maze.start)
    // print control
    // var i = 1
    // loop across all cells to find the shortest path
    runningMaze@
    do {
        // mark this cell as visited
        q.first().visited = true
        // part 2 adjustment, all 'a' spaces are starting points, set the cost to 0 if the char is a
        //if (q.first().char == 'a') {
            //q.first().previous = maze.setNull()
            //q.first().cost = 0
        //}

        // get a list of neighbors, loop across that list
        val neighborList = goodNeighbors(q.first().row, q.first().col, maze.maxRow, maze.maxCol)
        checkNeighbors@
        for (neighbor in neighborList) {
            // find the Node abject at this coordinate
            val neighborNode = maze.getNode(neighbor)
            // have we visited this node? if so, keep looping across other neighbors
            // if (neighborNode.visited) continue@checkNeighbors
            // check if the height diff is > 1, if so we don't connect these nodes, keep looping across neighbors
            if (neighborNode.height - q.first().height > 1) continue@checkNeighbors
            // now check if the travel cost is less than how we got here through this cell
            if (q.first().cost + 1 >= neighborNode.cost) continue@checkNeighbors
            // we have a valid, unvisited neighbor here first add it to the check queue
            q.add(neighborNode)
            // next we record the cost from here to there (one more than the cost to here)
            neighborNode.cost = q.first().cost + 1
            // finally record current node as the neighbor's previous
            neighborNode.previous = q.first()
        }
        // now that we checked all neighbors, remove this node from our check queue
        q.removeFirst()

        // print control
        // i++
        // if (i % 500 == 0) maze.show()

    } while (q.isNotEmpty())

    println("I've run the maze.")
    println("I think the exit at (${maze.end.row},${maze.end.col}) has a travel cost of ${maze.end.cost}")
    maze.show()
}

fun makeMap(myData: ArrayList<String>): Map {
    println("Creating a new map...")
    val newMap = Map()
    for (row in 0 until myData.size) {
        for (col in 0 until myData[0].length) {
            val node = Node(row, col, myData[row][col])
            newMap.map.add(node)
        }
    }
    newMap.setEnds()
    newMap.show()
    return newMap
}