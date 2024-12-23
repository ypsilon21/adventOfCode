import java.io.File
import java.io.FileNotFoundException

fun main(){
    val task1: day15_1 = day15_1()
    task1.main()
    task1.simulate()
    println("First result:  " + task1.getAllBoxPositions())

    val task2: day15_2 = day15_2()
    task2.main()
    task2.simulate()
    println("Second result: " + task2.getAllBoxPositions())
}

class day15_1{
    var matrix:Array<CharArray> = arrayOf()
    var instructions: MutableList<Char> = mutableListOf()
    var position: Pair<Int, Int> = Pair(-1, -1)

    fun main(){
        val input: Pair<Array<CharArray>, String> = readFile()
        matrix = input.first
        instructions = input.second.toMutableList()
        position = getInitialPosition()
    }

    fun readFile(): Pair<Array<CharArray>, String> {
        val fileName = "src/main/resources/input.txt"
        val matrixList = mutableListOf<CharArray>()
        var instructions = ""
        var foundEmptyLine = false
        try {
            File(fileName).useLines { lines ->
                lines.forEach {
                    if (it.equals("")){
                        foundEmptyLine = true
                    }
                    else if (!foundEmptyLine) matrixList.add(it.toCharArray())
                    else instructions += it
                }
            }
        }
        catch (e: FileNotFoundException){
            print("Couldn't find: " + fileName)
        }

        return Pair(matrixList.toTypedArray(), instructions)
    }

    fun getInitialPosition(): Pair<Int, Int>{
        for(i in 0..matrix.size - 1){
            for(j in 0..matrix[0].size - 1){
                if(matrix[i][j] == '@'){
                    matrix[i][j] = '.'
                    return Pair(i, j)
                }
            }
        }
        error("Position not found")
    }

    fun simulate(){
        while(instructions.isNotEmpty()){
            simulateStep(instructions.removeFirst())
        }
    }

    fun simulateStep(instruction: Char){
        val direction: Pair<Int, Int>
        when(instruction){
            '<' -> direction = Pair(0, -1)
            '>' -> direction = Pair(0, 1)
            '^' -> direction = Pair(-1, 0)
            'v' -> direction = Pair(1, 0)
            else -> direction = Pair(0, 0)
        }

        when(matrix[position.first + direction.first][position.second + direction.second]){
            '.' -> position = Pair(position.first + direction.first, position.second + direction.second)
            'O' -> moveIfPossible(direction)
        }
    }

    fun moveIfPossible(direction: Pair<Int, Int>){
        var obstaclePos: Pair<Int, Int> = Pair(position.first + direction.first, position.second + direction.second)
        val firstObstaclePos: Pair<Int, Int> = obstaclePos.copy()
        while(matrix[obstaclePos.first][obstaclePos.second] == 'O'){
            obstaclePos = Pair(obstaclePos.first + direction.first, obstaclePos.second + direction.second)
        }
        if(matrix[obstaclePos.first][obstaclePos.second] == '.'){
            matrix[obstaclePos.first][obstaclePos.second] = 'O'
            matrix[firstObstaclePos.first][firstObstaclePos.second] = '.'
            position = firstObstaclePos.copy()
        }
    }

    fun getAllBoxPositions(): Int{
        var res: Int = 0
        for(i in 0..matrix.size - 1){
            for(j in 0..matrix[0].size - 1){
                if(matrix[i][j] == 'O'){
                    res += 100 * i + j
                }
            }
        }
        return res
    }
}