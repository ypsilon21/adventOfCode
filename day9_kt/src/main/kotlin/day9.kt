import java.io.FileReader

fun main(){
    val input: IntArray = getInput("src/main/resources/input.txt")
    val initialSystem = toFileSystem(input)

    val condensedSystem = condense(initialSystem)
    val res1 = getSum(condensedSystem)

    val condensedSystem2 = condenseFirstFit(initialSystem)
    val res2 = getSum(condensedSystem2)

    println("First result:  " + res1)
    println("Second result: " + res2)
}

fun getInput(path: String): IntArray {
    val fileReader = FileReader(path)
    val line = fileReader.readLines()[0]
    fileReader.close()

    val res: MutableList<Int> = mutableListOf()
    for(char in line.toCharArray()){
        res.add(char.digitToInt())
    }
    return res.toIntArray()
}

fun toFileSystem(array: IntArray): IntArray {
    val res: MutableList<Int> = mutableListOf()
    for(i in 0 until array.size ){
        for(j in 0 until array[i]){
            if(i % 2 == 0) res.add((i/2))
            else res.add(-1)
        }
    }
    return res.toIntArray()
}

fun condense(system: IntArray): IntArray {
    var left = 0
    var right = system.size - 1
    val res: MutableList<Int> = mutableListOf()

    outer@ while(left <= right){
        if(system[left] == -1){
            while(system[right] == -1){
                right--
                if(left >= right) break@outer
            }
            res.add(system[right])
            right--
        }
        else res.add(system[left])
        left++
    }
    return res.toIntArray()
}

fun condenseFirstFit(systemInit: IntArray): IntArray {
    val system: IntArray = systemInit.copyOf()
    var left = 0
    var right = system.size -1
    
    var blockSize: Int
    var freeSize: Int
    var blockStart: Int
    var id: Int
    var fitFound: Boolean

    while(right > 0){
        while(system[right] == -1) right--
        blockSize = 1
        id = system[right]
        while(right > 0 && system[right] == system[right - 1]){
            right--
            blockSize++
        }

        left = 0
        freeSize = 0
        blockStart = 0
        fitFound = false

        while(left < right){
            if(system[left] != -1) freeSize = 0
            else{
                freeSize++
                if(left == 0) blockStart = 0
                else if(system[left-1] != -1) blockStart = left
                if(blockSize == freeSize){
                    fitFound = true
                    break
                }
            }
            left++
        }

        if(fitFound){
            for(i in 0 until blockSize) system[blockStart + i] = id
            while(right < system.size - 1 && system[right] == system[right + 1]){
                system[right] = -1
                right++
            }
            system[right] = -1
        }

        right--
    }
    return system
}

fun getSum(array: IntArray): Long {
    var res: Long = 0
    for(i in 0 until array.size){
        if(array[i] == -1) continue
        res += i * array[i]
    }
    return res
}