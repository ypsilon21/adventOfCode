import java.io.File

fun main(){
    val input: Array<CharArray> = readInput()
    val zeroes: Array<Pair<Int, Int>> = getZeroes(input)
    var res: Pair<Int, Int> = Pair(0, 0)
    for (zero in zeroes){
        val score: Pair<Int, Int> = getScore(input, zero)
        res = Pair(res.first + score.first, res.second + score.second)
    }
    println("First result:  " + res.first.toString())
    println("Second result: " + res.second.toString())
}

fun readInput(): Array<CharArray>{
    val file: File = File("src/input.txt")
    val lines: List<String> = file.readLines()
    var res: MutableList<CharArray> = mutableListOf()
    for (line in lines){
        res.add(line.toCharArray())
    }
    return res.toTypedArray()
}

fun getZeroes(matrix: Array<CharArray>): Array<Pair<Int, Int>>{
    val res: MutableList<Pair<Int, Int>> = mutableListOf()
    for(i in 0..<matrix.size){
        for( j in 0..<matrix[0].size){
            if(matrix[i][j] == '0') res.add(Pair(i, j))
        }
    }
    return res.toTypedArray()
}

fun getScore(matrix: Array<CharArray>, pos: Pair<Int, Int>): Pair<Int, Int> {
    val pos9s: MutableList<Pair<Int, Int>> = findPaths(matrix, pos, '0')
    return Pair(pos9s.distinct().size, pos9s.size)
}

fun findPaths(matrix: Array<CharArray>, pos: Pair<Int, Int>, current: Char): MutableList<Pair<Int, Int>>{
    if(matrix[pos.first][pos.second] == '9') return mutableListOf(pos)
    else{
        val res: MutableList<Pair<Int, Int>> = mutableListOf()
        //up
        if(pos.first > 0){
            if(matrix[pos.first - 1][pos.second] == current + 1){
                for(p in findPaths(matrix, Pair(pos.first - 1, pos.second), current + 1)){
                    res.add(p)
                }
            }
        }
        //down
        if(pos.first < matrix.size - 1){
            if(matrix[pos.first + 1][pos.second] == current + 1) {
                for (p in findPaths(matrix, Pair(pos.first + 1, pos.second), current + 1)) {
                    res.add(p)
                }
            }
        }
        //left
        if(pos.second > 0) {
            if(matrix[pos.first][pos.second - 1] == current + 1) {
                for (p in findPaths(matrix, Pair(pos.first, pos.second - 1), current + 1)) {
                    res.add(p)
                }
            }
        }
        //right
        if(pos.second < matrix[0].size - 1){
            if(matrix[pos.first][pos.second + 1] == current + 1) {
                for (p in findPaths(matrix, Pair(pos.first, pos.second + 1), current + 1)) {
                    res.add(p)
                }
            }
        }
        return res
    }
}
