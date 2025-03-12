import scala.collection.mutable.ListBuffer
import scala.annotation.nowarn

object day7{
  def main(args: Array[String]): Unit = {
    val source = scala.io.Source.fromFile("input.txt")
    val input: Array[String] = try source.mkString.split("\n") finally source.close()

    val inputArray = new Array[Array[String]](input.length)
    for(i <- 0 until input.length){
      inputArray(i) = input(i).dropRight(1).split(' ')
      inputArray(i)(0) = inputArray(i)(0).dropRight(1)
    }

    println("First Result:  " + getValidEquations(inputArray, false).sum)
    println("Second Result: " + getValidEquations(inputArray, true).sum)
  }

  def getValidEquations(input: Array[Array[String]], part2: Boolean): ListBuffer[Long] = {
    var res = new ListBuffer[Long]
    for(i <- 0 until input.length){
      if(checkIfEq(input(i), part2)){
        res += input(i)(0).toLong
      }
    }
    res
  }


  def checkIfEq(line: Array[String], part2: Boolean): Boolean = {
    var res = false
    var result = line(0).toLong
    var nums = new ListBuffer[Long]
    for(i <- 1 until line.length){
      nums += line(i).toLong
    }

    var multiplicationEdges: ListBuffer[ListBuffer[Int]] = new ListBuffer[ListBuffer[Int]]
    if(part2 == false) multiplicationEdges = getBoolLists(line.length)
    else multiplicationEdges = getTrinaryLists(line.length)

    for(edge <- multiplicationEdges){
      if(multEquals(nums, edge, result)){
        res = true
      }
    }

    res
  }

  def getBoolLists(length: Int): ListBuffer[ListBuffer[Int]] = {
    var res: ListBuffer[ListBuffer[Int]] = new ListBuffer[ListBuffer[Int]]
    for(i: Int <- 0 until (Math.pow(2, (length - 2)).toInt)){
      @nowarn()
      var temp = ListBuffer(i.toBinaryString.map(c => if (c == '1') 1 else 0):_*)
      while(temp.length < length - 2){
        temp.insert(0, 0)
      }
      res += temp
    }
    res
  }

  def getTrinaryLists(length: Int): ListBuffer[ListBuffer[Int]] = {
    var res: ListBuffer[ListBuffer[Int]] = new ListBuffer[ListBuffer[Int]]
    for(i: Int <- 0 until (Math.pow(3, (length - 2)).toInt)){
      var temp = toTrinaryString(i, length-2)
      while(temp.length < length - 2){
        temp.insert(0, 0)
      }
      res += temp
    }
    res
  }

  def toTrinaryString(i: Int, len: Int): ListBuffer[Int] = {
    var x = i
    val res = new ListBuffer[Int]

    for (_ <- 0 until len) {
      res.prepend(x % 3)
      x /= 3
    }
    res
  }

  def multEquals(nums: ListBuffer[Long], mults: ListBuffer[Int], result: Long): Boolean = {
    var nums2 = nums.clone
    var res: Long = nums2(0)
    nums2.remove(0)
    for(i <- 0 until mults.length){
      if(mults(i) == 0){
        res += nums2(i)
      }
      else if(mults(i) == 1){
        res *= nums2(i)
      }
      else res = (res.toString + nums2(i).toString).toLong
    }

    res == result
  }
}