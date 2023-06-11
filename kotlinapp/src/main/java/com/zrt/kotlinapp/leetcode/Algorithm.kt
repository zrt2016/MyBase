package com.zrt.kotlinapp.leetcode

import java.util.*
import kotlin.collections.ArrayList


/**
 * 算法
 * @author：Zrt
 * @date: 2022/7/29
 *
 * @see mostCompetitive()：给你一个整数数组 nums 和一个正整数 k ，返回长度为 k 且最具 竞争力 的 nums 子序列。
 * @see getMaxMatrix()：给定一个正整数、负整数和 0 组成的 N × M 矩阵，编写代码找出元素总和最大的子矩阵。
 * @see hasPathSum()：给你二叉树的根节点 root 和一个表示目标和的整数 targetSum 。
 * @see kMirror()：一个 k 镜像数字 指的是一个在十进制和 k 进制下从前往后读和从后往前读都一样的 没有前导 0 的 正 整数。
 * @see queensAttacktheKing()：在一个 8x8 的棋盘上，放置着若干「黑皇后」和一个「白国王」。
 *          给定一个由整数坐标组成的数组 queens ，表示黑皇后的位置；以及一对坐标 king ，
 *          表示白国王的位置，返回所有可以攻击国王的皇后的坐标(任意顺序)。白国王可被同一行、同一列以及对角线的黑皇后攻击。
 * @see
 */

fun main(){
    val array = arrayOf<IntArray>(intArrayOf(9, -8, 1, 3, -2), intArrayOf(-3, 7, 6, -2, 4), intArrayOf(6, -4, -4, 8, -7))
    getMaxMatrix(array)
    println("----------------------------")
    val array2 = arrayOf<IntArray>(intArrayOf(-100, -8, -1, -3, -2), intArrayOf(-3, -7, -6, -2, -4), intArrayOf(-6, -4, -4, -8, -7))
    getMaxMatrix(array2) // [0,2,0,2]
}

/**
 * TODO 给你一个整数数组 nums 和一个正整数 k ，返回长度为 k 且最具 竞争力 的 nums 子序列。
 * 数组的子序列是从数组中删除一些元素（可能不删除元素）得到的序列。
 * 在子序列 a 和子序列 b 第一个不相同的位置上，如果 a 中的数字小于 b 中对应的数字，那么我们称子序列 a 比子序列 b（相同长度下）更具 竞争力 。
 * 例如，[1,3,4] 比 [1,3,5] 更具竞争力，在第一个不相同的位置，也就是最后一个位置上， 4 小于 5 。
 * 输入：nums = [3,5,2,6], k = 2
 * 输出：[2,6]
 * 解释：在所有可能的子序列集合 {[3,5], [3,2], [3,6], [5,2], [5,6], [2,6]} 中，[2,6] 最具竞争力。
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode.cn/problems/find-the-most-competitive-subsequence
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
fun mostCompetitive(nums: IntArray, k: Int): IntArray {
    val stack = Stack<Int>()
    stack.push(-1)
    for (i in 0 until nums.size){
        //当前元素比队尾元素小，将队尾元素出栈
        while (stack.peek() > nums[i] && k-stack.size+1 < nums.size-i){
            stack.pop()
        }
        if (stack.size < k+1){
            stack.push(nums[i])
        }
    }
    var length = k
    val childNum = IntArray(k)
    for (i in stack.size-1 downTo 1){
        childNum[i - 1] = stack.pop()
    }
    return childNum
}

/**
 * TODO 给定一个正整数、负整数和 0 组成的 N × M 矩阵，编写代码找出元素总和最大的子矩阵。
 * 返回一个数组 [r1, c1, r2, c2]，其中 r1, c1 分别代表子矩阵左上角的行号和列号，
 * r2, c2 分别代表右下角的行号和列号。若有多个满足条件的子矩阵，返回任意一个均可。
 * 例：
 * 对于矩阵：
 * 1， 2， 3， 5
 * -1， 2，-4，-3
 * 0，-1， 7， 6
 * 那么对于sum[]的所有可能情况有6种：
 * [1,2,3,5] 仅首行纵向累加
 * [0,4,-1,2] 第一行和第二行
 * [0,3,6,8] 所有的三行
 * [-1,1,3,3] 后两行
 * [-1,2,-4,-3] 第二行
 * [0,-1,7,6] 最后一行
 */
fun getMaxMatrix(matrix: Array<IntArray>): IntArray{
    //保存最大子矩阵的左上角和右下角的行列坐标
    val result = IntArray(4)
    // 记录左上角位置
    var (x, y) = Pair(0, 0)
    val n = matrix.size     // 行
    val m = matrix[0].size  // 列
    val b = IntArray(m) //记录当前i~j行组成大矩阵的每一列的和，将二维转化为一维
    var sum = 0         // 计算总和
    var sumMax = Integer.MIN_VALUE      // 记录最大值，默认给最小值  
    for (i in 0 until n){
        //每次更换子矩形上边，就要清空b，重新计算每列的和
        for (t in 0 until m) b[t] = 0

        for (j in i until n){
            //每一下就相当于求一次最大子序列和
            sum = 0
            for (k in 0 until m){
                b[k] += matrix[j][k]
                if (sum > 0){
                    sum += b[k]
                }else {
                    // 如果都是负数，找最大一个即可
                    sum = b[k]
                    x = i
                    y = k
                }
                if (sum > sumMax){
                    sumMax = sum
                    result[0] = x
                    result[1] = y
                    result[2] = j
                    result[3] = k
                }
            }
            println(Arrays.toString(b))
        }

    }
    return result
}

/**
 * TODO 给你二叉树的根节点 root 和一个表示目标和的整数 targetSum 。
 * 判断该树中是否存在 根节点到叶子节点 的路径，这条路径上所有节点值相加等于目标和 targetSum 。
 * 如果存在，返回 true ；否则，返回 false 。
 */
class TreeNode(var `val`: Int) {
    var left: TreeNode? = null
    var right: TreeNode? = null
}
// 方法一：使用递归
fun hasPathSum(root: TreeNode?, targetSum: Int): Boolean {
    if (root == null) {
        return false;
    }
    if (root.left == null && root.right == null) {
        return targetSum == root.`val`
    }
    return hasPathSum(root.left, targetSum - root.`val`) || hasPathSum(root.right, targetSum - root.`val`)
}

/**
 * https://leetcode.cn/problems/sum-of-k-mirror-numbers/description/
 * @author Zrt
 * 一个 k 镜像数字 指的是一个在十进制和 k 进制下从前往后读和从后往前读都一样的 没有前导 0 的 正 整数。
 * 比方说，9 是一个 2 镜像数字。9 在十进制下为 9 ，二进制下为 1001 ，两者从前往后读和从后往前读都一样。
 * 相反地，4 不是一个 2 镜像数字。4 在二进制下为 100 ，从前往后和从后往前读不相同。
 * 给你进制 k 和一个数字 n ，请你返回 k 镜像数字中 最小 的 n 个数 之和 。
 * 示例：
 * 输入：k = 3, n = 7  输出：499
 * 解释： 7 个最小的 3 镜像数字和它们的三进制表示如下：
 *   十进制       三进制
 *     1          1
 *     2          2
 *     4          11
 *     8          22
 *     121        11111
 *     151        12121
 *     212        21212
 * 它们的和为 1 + 2 + 4 + 8 + 121 + 151 + 212 = 499 。

 * 注：2 <= k <= 9   1 <= n <= 30
 */
fun kMirror(k: Int, n: Int):Long{
    if (k < 2 || k > 9) return -1;
    if (n < 1 || n > 30) return -1;
    // str：镜像后的K进制
    fun symmetry(str: String):Boolean{
        var start = 0
        var end = str.length-1
        while (start < end) {
            if (str[start] != str[end]) return false
            start++
            end--
        }
        return true
    }
    fun nextNum(str: String):String{
        val length = str.length
        val sb = StringBuilder()
        // 截取前部分 9-9、101-10、1001-10
        val beginStr = str.substring(0, (length + 1) / 2)
        val beginNum = beginStr.toLong()
        // 校验前部分值+1后，是否发生进位，例99的下一个镜像数就是101
        if ((beginNum + 1).toString().length != beginStr.length){
            for (i in 0..length){
                if (i == 0 || i == length){
                    sb.append("1")
                }else{
                    sb.append("0")
                }
            }
        }else{
            sb.append(beginNum + 1)
            // 例：str = 7667， beginStr=76， 2<= i < 4
            //  str索引：0123  [length - 1 - i] = 1 、 0
            for (i in (length + 1) / 2 until length){
                // 镜像复制
                sb.append(sb[length - 1 - i])
            }
        }
        return sb.toString()
    }
    var sum = 0L // 总和
    var nextImageNum = "1" // 下一个镜像数字
    var imageNum = n
    while (imageNum > 0){
        val currentNum = nextImageNum.toLong()
        // 1、校验当前镜像数字的k进制是否也是镜像数字
        // 转换成k进制后传入
        if (symmetry(currentNum.toString(k))){
            sum += currentNum
            imageNum--
        }
        // 2、获取下一个镜像数字
        nextImageNum = nextNum(nextImageNum)
    }
    return sum
}

/**
 * 在一个 8x8 的棋盘上，放置着若干「黑皇后」和一个「白国王」。
 * 给定一个由整数坐标组成的数组 queens ，表示黑皇后的位置；以及一对坐标 king ，表示白国王的位置，返回所有可以攻击国王的皇后的坐标(任意顺序)。
 * 输入：queens = [[0,1],[1,0],[4,0],[0,4],[3,3],[2,4]], king = [0,0]
 * 输出：[[0,1],[1,0],[3,3]]
 * 解释：
 * [0,1] 的皇后可以攻击到国王，因为他们在同一行上。
 * [1,0] 的皇后可以攻击到国王，因为他们在同一列上。
 * [3,3] 的皇后可以攻击到国王，因为他们在同一条对角线上。
 * [0,4] 的皇后无法攻击到国王，因为她被位于 [0,1] 的皇后挡住了。
 * [4,0] 的皇后无法攻击到国王，因为她被位于 [1,0] 的皇后挡住了。
 * [2,4] 的皇后无法攻击到国王，因为她和国王不在同一行/列/对角线上。
 * 解：从8个方向分别计算距离最近的黑皇后
 * @param queens
 * @param king
 * @return
 */
fun  queensAttacktheKing(queens: Array<IntArray>, king: IntArray):List<List<Int>> {
    val queensAttackList: MutableList<List<Int>> = ArrayList()
    val flag = Array<BooleanArray>(8){ BooleanArray(8) }
    val attackthe = arrayOf(intArrayOf(-1, 0), intArrayOf(-1, -1),
            intArrayOf(0, -1), intArrayOf(1, -1),
            intArrayOf(1, 0), intArrayOf(1, 1),
            intArrayOf(0, 1), intArrayOf(-1, 1))
    for (i in 0 .. attackthe.size){
        val step = attackthe[i]
        var x = king[0]
        var y = king[1]
        while (x >= 0 && x < 8 && y >= 0 && y < 8) {
            x += step[0]
            y += step[1]
            if (flag[x][y]){
                queensAttackList.add(arrayListOf(x, y))
                break
            }
        }

    }
    return queensAttackList
}

/**
 * 给你一个字符串 word ，该字符串由数字和小写英文字母组成。
 * 请你用空格替换每个不是数字的字符。例如，"a123bc34d8ef34" 将会变成 " 123  34 8  34" 。
 * 注意，剩下的这些整数为（相邻彼此至少有一个空格隔开）："123"、"34"、"8" 和 "34" 。
 * 返回对 word 完成替换后形成的 不同 整数的数目。
 * 只有当两个整数的 不含前导零 的十进制表示不同， 才认为这两个整数也不同。
 * 输入：word = "a123bc34d8ef34"   输出：3
 * 解释：不同的整数有 "123"、"34" 和 "8" 。注意，"34" 只计数一次。
 */
fun numDifferentIntegers(word: String): Int {
    val set = HashSet<String>()
    val n = word.length
    // p1为第一个数字索引，p2为数字尾部索引
    var p1 = 0
    var p2 = 0
    while (true) {
        // 校验当前索引下的字符是否不为数字，isDigit() 是否为数字
        // 获取数字前面字符的索引位置
        while (p1 < n && !Character.isDigit(word[p1])) {
            p1++
        }
        if (p1 == n) {
            break
        }
        p2 = p1
        while (p2 < n && Character.isDigit(word[p2])) {
            p2++
        }
        // 如果数字前部分为0，则进行过滤
        while (p2 - p1 > 1 && word[p1] == '0') {
            p1++
        }
        set.add(word.substring(p1, p2))
        p1 = p2
    }
    return set.size
}

/**
 * 1781. 所有子字符串美丽值之和
 * 一个字符串的 美丽值 定义为：出现频率最高字符与出现频率最低字符的出现次数之差。
 * 比方说，"abaacc" 的美丽值为 3 - 1 = 2 。
 * 给你一个字符串 s ，请你返回它所有子字符串的 美丽值 之和。
 * 输入：s = "aabcb"
 * 输出：5
 * 解释：美丽值不为零的字符串包括 ["aab","aabc","aabcb","abcb","bcb"] ，每一个字符串的美丽值都为 1 。
 * @param s
 * @return
 */
fun beautySum(s: String): Int {
    var sum = 0
    for (x in 0 until s.length){
        var charCount = intArrayOf(26) // 记录26个字母分别出现的次数
        var maxValue = 0
        for (y in x until s.length){
            charCount[s[y] - 'a'] += 1
            maxValue = Math.max(maxValue, charCount[s[y] - 'a'])
            var minValue = s.length
            for (i in 0 until charCount.size){
                if (charCount[i] > 0 && charCount[i] < minValue){
                    minValue = charCount[i]
                }
            }
            sum += (maxValue - minValue)
        }
    }
    return sum
}

/** TODO 阶乘尾数
 * https://leetcode.cn/problems/factorial-zeros-lcci/description/
 * 解题思路：
 * 1、那么 n 过大时，从 1 遍历到 n, 那么会超时,因此我们修改下规律
 *     n! = 1 * 2 * 3 * 4 * (1 * 5) * ... * (2 * 5) * ... * (3 * 5) ...
 *     我们发现，
 *     每隔 5 个数就会出现 一个 5，因此我们只需要通过 n / 5 来计算存在存在多少个 5 个数，那么就对应的存在多少个 5
 *     但是，我们也会发现
 *     每隔 25 个数会出现 一个 25， 而 25 存在 两个 5，我们上面只计算了 25 的一个 5，因此我们需要 n / 25 来计算存在多少个 25，加上它遗漏的 5
 *     同时，我们还会发现
 *     每隔 125 个数会出现一个 125，而 125 存在 三个 5，我们上面只计算了 125 的两个 5，因此我们需要 n / 125 来计算存在多少个 125，加上它遗漏的 5
 *     ...
 *     因此我们 count = n / 5 + n / 25 + n / 125 + ...
 *     最终分母可能过大溢出，上面的式子可以进行转换
 *     count = n / 5 + n / 5 / 5 + n / 5 / 5 / 5 + ...
 *     因此，我们这样进行循环
 *     n /= 5;
 *     count += n;
 *     这样，第一次加上的就是 每隔 5 个数的 5 的个数，第二次加上的就是 每隔 25 个数的 5 的个数 ...
 */
fun trailingZeroes(n: Int): Int {
    var n = n
    var m = 0
    while (n > 0) {
        m += n / 5
        n /= 5
    }
    return m
}













//
//for (x in 0 until s.length) {
//    val count = IntArray(26)
//    var maxValue = 0
//    for (y in x until s.length) {
//        val charAt = s[y]
//        count[charAt - 'a'] += 1 // 利用AsCII来计算每次重复
//        maxValue = Math.max(maxValue, count[charAt - 'a'])
//        var minValue = s.length
//        for (i in count.indices) {
//            if (count[i] > 0 && count[i] < minValue) {
//                minValue = count[i]
//            }
//        }
//        sum += maxValue - minValue
//    }
//}