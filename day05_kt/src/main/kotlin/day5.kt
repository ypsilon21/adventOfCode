import java.io.File
import java.io.FileNotFoundException

fun main(){
    val fileName = "src/main/resources/input.txt"
    val rules = mutableListOf<MutableList<String>>()
    val printedOutput = mutableListOf<String>()
    var foundEmptyLine = false
    try {
        File(fileName).useLines { lines ->
            lines.forEach {
                if (it.equals("")) {
                    foundEmptyLine = !foundEmptyLine
                } else if (!foundEmptyLine) rules.add(it.split('|').toMutableList())
                else printedOutput.add(it)
            }
        }
    }
    catch (e: FileNotFoundException){
        print("Couldn't find: " + fileName)
    }
    val correctOutputs = getCorrectLines(rules, printedOutput, false)
    val res = addMiddleValues(correctOutputs)
    print("First Result:  " + res)

    val incorrectOutputs = getCorrectLines(rules, printedOutput, true)
    val orderedIncorrectOutputs = correctOrder(rules, incorrectOutputs)
    val res2 = addMiddleValues(orderedIncorrectOutputs)
    print("\nSecond Result: " + res2)
}

fun getCorrectLines(rules: MutableList<MutableList<String>>, printedOutput: MutableList<String>,
                    invert: Boolean): MutableList<String> {
    val res = mutableListOf<String>()
    for(line in printedOutput){
        if(invert xor testRules(rules, line)){
            res.add(line)
        }
    }
    return res
}

fun testRules(rules: MutableList<MutableList<String>>, line: String): Boolean {
    if(rules.isEmpty()) return true
    val rule = rules.first()
    if(line.contains(rule[0]) && line.contains(rule[1])){
        if(line.indexOf(rule[0]) < line.indexOf(rule[1])){
            return testRules(rules.drop(1).toMutableList(), line)
        }
        else return false
    }
    else return testRules(rules.drop(1).toMutableList(), line)
}

fun addMiddleValues(lines: MutableList<String>): Int{
    var res = 0
    for(line in lines){
        val line = line.split(",")
        res += line.get(line.size/2).toInt()
    }
    return res
}

fun correctOrder(rules: MutableList<MutableList<String>>, lines: MutableList<String>): MutableList<String>{
    val res = mutableListOf<String>()
    for(line in lines){
        res.add(correctLine(rules, line))
    }
    return res
}

fun correctLine(rules: MutableList<MutableList<String>>, line: String): String {
    if(line.isEmpty()) return ""
    if(testRules(rules, line)) return line
    val ll = line.split(",")
    val res = mutableListOf<String>()
    for(num in ll){
        insertCorrectly(num, rules, res)
    }
    return res.joinToString(",")
}

fun insertCorrectly(num: String, rules: MutableList<MutableList<String>>, res: MutableList<String>) {
    for (i in 0..res.size) {
        var canInsert = true
        for (rule in rules) {
            if (rule[1] == num) {
                if (i < res.size && res.subList(i, res.size).contains(rule[0])) {
                    canInsert = false
                    break
                }
            }
        }
        if (canInsert) {
            res.add(i, num)
            return
        }
    }
    res.add(num)
}
