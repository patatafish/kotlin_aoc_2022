import com.use.readFile
import java.util.LinkedList

fun main() {
    val turns = 20  // how many iterations
    val rawData = readFile("d11.dat", "bl")
    val troop = Troop()
    for (monkeyData in rawData) {
        val monkey = Monkey()
        monkey.pickLice(monkeyData.split("\r\n"))
        troop.add(monkey)
    }

    for (i in 1..turns) {
        println("Round $i:")
        troop.monkeyAround()
        troop.showFamily()
    }

    println("exiting...")
}

open class Troop {
    // the linked list of other monkeys in the tree
    var members: LinkedList<Monkey> = LinkedList()

    fun add(monkey: Monkey) {
        // add the monkey to the tree
        members.add(monkey)
        // link the monkey family to this list
        monkey.members = this.members
        println("Added monkey ${monkey.name} to the tree...")
    }

    fun showFamily() {
        val tops: IntArray = intArrayOf(1, 0)
        for (monkey in members) {
            if (monkey.getTouches() > tops[0]) {
                tops[1] = tops[0]
                tops[0] = monkey.getTouches()
            }
            else if (monkey.getTouches() > tops[1]) {
                tops[1] = monkey.getTouches()
            }
            monkey.exposeSelf()
        }
        println("Current level of monkey business is ${tops[0] * tops[1]}.")
    }

    fun monkeyAround(verbose:Boolean = false) {
        for (monkey in members) monkey.examineItems(verbose)
    }
}

class Monkey : Troop() {
    // internal values
    var name = -1
    private val holding = LinkedList<Int>()
    private var operation = ""
    private var test = -1
    private var truePass = -1
    private var falsePass = -1
    private var touchCount = 0

    init {
        println("Creating a new monkey...")
    }

    fun pickLice(monkeyData: List<String>) {
        name = monkeyData[0].replace("Monkey ", "").replace(":", "").toInt()
        println("Filling data for monkey $name...")
        for (item
                in monkeyData[1].replace("Starting items: ", "").split(',')) {
            holding.add(item.trim().toInt())
        }
        operation = monkeyData[2].replace("Operation: new = old ", "").trim()
        test = monkeyData[3].replace("Test: divisible by ", "").trim().toInt()
        truePass = monkeyData[4].replace("If true: throw to monkey ", "").trim().toInt()
        falsePass = monkeyData[5].replace("If false: throw to monkey ", "").trim().toInt()
    }

    fun examineItems(verbose: Boolean = false) {
        if (verbose) println("\n===============================================\nMonkey $name is examining items...")
        while (holding.isNotEmpty()) {
            val item = holding.remove()
            if (verbose) println("Looking at item $item, worry change is $operation.")
            touchCount++
            // establish new worry level by checking multiply or add
            var newWorry: Int = if (operation.contains('*')) {
                // multiplication is the only value that might reference old
                if (operation.contains("old")) item * item
                else item * operation.replace("*", "").trim().toInt()
            } else {
                item + operation.replace("+", "").trim().toInt()
            }
            if (verbose) println("Item's new worry level is $newWorry.")
            // part 1 worry decrease
            newWorry /= 3
            if (verbose) println("It didn't break, worry level down to $newWorry.")
            // part 1 MOD check
            if (newWorry % test == 0) {
                if (verbose) println("Passing item $newWorry to monkey $truePass\n")
                members[truePass].holding.add(newWorry)
            }
            else {
                if (verbose) println("Passing item $newWorry to monkey $falsePass\n")
                members[falsePass].holding.add(newWorry)
            }
        }
    }

    fun getTouches() = touchCount

    fun exposeSelf() {
        print("Monkey $name has $touchCount touches, and is holding: ")
        for (item in holding) print("$item ")
        println()
    }
}