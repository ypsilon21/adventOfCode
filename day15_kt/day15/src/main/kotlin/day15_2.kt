import java.io.File
import java.io.FileNotFoundException

class day15_2{
    var matrix:Array<CharArray> = arrayOf()
    var instructions: MutableList<Char> = mutableListOf()
    var position: Pair<Int, Int> = Pair(-1, -1)

    var topMostColoured: Int = 0
    var bottomMostColoured: Int = 0

    fun main(){
        val input: Pair<Array<CharArray>, String> = readFile()
        matrix = transformToWide(input.first)
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

    fun transformToWide(monoSpace: Array<CharArray>): Array<CharArray>{
        var doubleSpace: Array<CharArray> = Array (monoSpace.size) { CharArray(2 * monoSpace[0].size) { ' ' } }
        for(i in 0..monoSpace.size - 1){
            for(j in 0..monoSpace[0].size - 1){
                doubleSpace[i][2 * j] = monoSpace[i][j]
            }
        }
        for(i in 0..monoSpace.size - 1){
            for(j in 0..monoSpace[0].size - 1){
                when(doubleSpace[i][2 * j]){
                    '.' -> doubleSpace[i][2 * j + 1] = '.'
                    '#' -> doubleSpace[i][2 * j + 1] = '#'
                    '@' -> doubleSpace[i][2 * j + 1] = '.'
                    'O' -> {
                        doubleSpace[i][2 * j] = '['
                        doubleSpace[i][2 * j + 1] = ']'
                    }
                }
            }
        }
        return doubleSpace
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
            ']' -> moveIfPossible(direction)
            '[' -> moveIfPossible(direction)
        }
    }

    fun moveIfPossible(direction: Pair<Int, Int>){
        var obstaclePos: Pair<Int, Int> = Pair(position.first + direction.first, position.second + direction.second)
        val firstObstaclePos: Pair<Int, Int> = obstaclePos.copy()
        if(direction.first == 0) {
            if(matrix[obstaclePos.first][obstaclePos.second] == '.'){
                position = Pair(position.first + direction.first, position.second + direction.second)
            }
            else{
                while (matrix[obstaclePos.first][obstaclePos.second] == '[' || matrix[obstaclePos.first][obstaclePos.second] == ']') {
                    obstaclePos = Pair(obstaclePos.first + direction.first, obstaclePos.second + direction.second)
                }
                if (matrix[obstaclePos.first][obstaclePos.second] == '.') {
                    while(!obstaclePos.equals(firstObstaclePos)){
                        matrix[obstaclePos.first][obstaclePos.second] = matrix[obstaclePos.first - direction.first][obstaclePos.second - direction.second]
                        obstaclePos = Pair(obstaclePos.first - direction.first, obstaclePos.second - direction.second)
                    }
                    matrix[obstaclePos.first][obstaclePos.second] = '.'
                    position = firstObstaclePos.copy()
                }
            }
        }
        else{
            topMostColoured = obstaclePos.first
            bottomMostColoured = obstaclePos.first
            when(matrix[obstaclePos.first][obstaclePos.second]){
                '[' -> {
                    colourAdjacent(obstaclePos, direction)
                    moveColoured(obstaclePos, direction)
                }
                ']' -> {
                    colourAdjacent(obstaclePos, direction)
                    moveColoured(obstaclePos, direction)
                }
                '.' -> position = Pair(position.first + direction.first, position.second + direction.second)
                else -> {}
            }
        }
    }

    fun colourAdjacent(pos: Pair<Int, Int>, direction: Pair<Int, Int>){
        when(matrix[pos.first][pos.second]){
            '.', '#', 'x' -> return
            '[' -> {
                matrix[pos.first][pos.second] = 'x'
                colourAdjacent(Pair(pos.first + direction.first, pos.second), direction)
                colourAdjacent(Pair(pos.first, pos.second + 1), direction)
            }
            ']' -> {
                matrix[pos.first][pos.second] = 'x'
                colourAdjacent(Pair(pos.first + direction.first, pos.second), direction)
                colourAdjacent(Pair(pos.first, pos.second - 1), direction)
            }
            else -> {}
        }
        if(pos.first < topMostColoured) topMostColoured = pos.first
        else if(pos.first > bottomMostColoured) bottomMostColoured = pos.first
    }

    fun moveColoured(pos: Pair<Int, Int>, direction: Pair<Int, Int>){
        var level: Int = pos.first
        var movePossible = true
        while(true){
            var containsX: Boolean = false
            for(i in 0..matrix[0].size - 1){
                if(matrix[level][i] == 'x') {
                    containsX = true
                    if(matrix[level + direction.first][i] == '#') movePossible = false
                }
            }
            if(!containsX) break
            else{
                level += direction.first
            }
        }
        if(movePossible) {
            position = pos.copy()
            var left: Boolean = true
            while (level != pos.first - direction.first) {
                for (i in 0..matrix[0].size - 1) {
                    if (matrix[level][i] == 'x') {
                        matrix[level][i] = '.'
                        if (left) matrix[level + direction.first][i] = '['
                        else matrix[level + direction.first][i] = ']'
                        left = !left
                    }
                }
                level -= direction.first
            }
        }
        else{
            var left: Boolean = true
            while (level != pos.first - direction.first) {
                for (i in 0..matrix[0].size - 1) {
                    if (matrix[level][i] == 'x') {
                        if(left) matrix[level][i] = '['
                        else matrix[level][i] = ']'
                        left = !left
                    }
                }
                level -= direction.first
            }
        }
    }

    fun getAllBoxPositions(): Int{
        var res: Int = 0
        for(i in 0..matrix.size - 1){
            for(j in 0..matrix[0].size - 1){
                if(matrix[i][j] == '['){
                    res += 100 * i + j
                }
            }
        }
        return res
    }
}