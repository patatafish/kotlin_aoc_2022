import com.use.readFile
import java.util.LinkedList

fun main() {
    val turns = 10_000  // how many iterations
    val rawData = readFile("d11.dat", "bl")
    val troop = Troop()
    for (monkeyData in rawData) {
        val monkey = Monkey()
        monkey.pickLice(monkeyData.split("\r\n"))
        troop.add(monkey)
    }

    val interested: IntArray = intArrayOf(1, 20, 1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10_000)
    for (i in 1..turns) {
        if (interested.contains(i)) println("Round $i:")
        troop.monkeyAround()
        if (interested.contains(i)) troop.showFamily()
    }

    println("exiting...")
}

open class Troop {
    // the linked list of other monkeys in the tree
    var members: LinkedList<Monkey> = LinkedList()
    // modulus const for part 2 reduction

    fun add(monkey: Monkey) {
        // add the monkey to the tree
        members.add(monkey)
        // link the monkey family to this list
        monkey.members = this.members
        setConst()
        println("Added monkey ${monkey.name} to the tree...")
    }

    private fun setConst() {
        var newConst = 1
        for (monkey in members) newConst *= monkey.getTest()
        for (monkey in members) monkey.setModConst(newConst)
    }

    fun showFamily() {
        val tops: LongArray = longArrayOf(0, 0)
        for (monkey in members) {
            if (monkey.getTouches() > tops[0]) {
                tops[1] = tops[0]
                tops[0] = monkey.getTouches().toLong()
            }
            else if (monkey.getTouches() > tops[1]) {
                tops[1] = monkey.getTouches().toLong()
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
    private val holding = LinkedList<Long>()
    private var operation = ""
    private var test = -1
    private var truePass = -1
    private var falsePass = -1
    private var touchCount = 0
    private var modConst = 1

    init {
        println("Creating a new monkey...")
    }

    fun pickLice(monkeyData: List<String>) {
        name = monkeyData[0].replace("Monkey ", "").replace(":", "").toInt()
        println("Filling data for monkey $name...")
        for (item
                in monkeyData[1].replace("Starting items: ", "").split(',')) {
            holding.add(item.trim().toLong())
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
            val newWorry: Long = if (operation.contains('*')) {
                // multiplication is the only value that might reference old
                if (operation.contains("old")) item * item
                else item * operation.replace("*", "").trim().toLong()
            } else {
                item + operation.replace("+", "").trim().toLong()
            }
            if (verbose) println("Item's new worry level is $newWorry.")
            //part 1 worry decrease
            //newWorry /= 3
            //if (verbose) println("It didn't break, worry level down to $newWorry.")

            if (newWorry % test == 0L) {
                if (verbose) println("Passing item $newWorry to monkey $truePass\n")
                members[truePass].holding.add(newWorry%modConst)
            }
            else {
                if (verbose) println("Passing item $newWorry to monkey $falsePass\n")
                members[falsePass].holding.add(newWorry%modConst)
            }
        }
    }

    fun getTouches() = touchCount
    fun getTest() = test

    fun setModConst(set: Int) {
        modConst = set
    }

    fun exposeSelf() {
        print("Monkey $name has $touchCount touches, and is holding: ")
        for (item in holding) print("$item ")
        println()
    }
}