package com.zrt.kotlinapp.jetpack

import java.lang.StringBuilder

/**
 * @author：Zrt
 * @date: 2022/8/9
 * DSL 全称领域特定语言（Domain Specific Language），它是编程语言赋予开发者的一种特殊能力
 * 编写一些看似脱离其原始语法结构的代码，从而构建出一种专有的语法结构
 */


fun main(){
    val dependencies = dependencies {
        implementation("com.squareup.retrofit2:retrofit:2.6.1")
        implementation("com.squareup.retrofit2:converter-gson:2.6.1")
    }
    for (lib in dependencies){
        println(lib)
    }
    println("-------------------------------------")
    val table = table {
        tr {
            td { "Apple" }
            td { "Grape" }
            td { "Orange" }
        }
        tr {
            td { "Pear" }
            td { "Banana" }
            td { "Watermelon" }
        }
    }
    println(table)
}
/**
 * 使用Kotlin的DSL实现Gradle的Groovy类似的语法结构
 * 依赖库
 */
class Dependency{
    val libraries = ArrayList<String>()
    fun implementation(lib: String){
        libraries.add(lib)
    }
}
fun dependencies(block:Dependency.() -> Unit):List<String>{
    val dependency = Dependency()
    dependency.block()
    return dependency.libraries
}

/**
 * <table>
 *     <tr>
 *         <td>Apple</td>
 *         <td>Grape</td>
 *         <td>Orange</td>
 *     </tr>
 *     <tr>
 *         <td>Pear</td>
 *         <td>Banana</td>
 *         <td>Watermelon</td>
 *     </tr>
 * </table>
 * 在kotlin中动态生成表格所对应的Html代码
 * */
class Td{
    var content = ""
    fun html() = "\n\t\t<td>$content</td>"
}
class Tr{
    private val children = ArrayList<Td>()
    fun td(block:Td.()->String){
        val td = Td()
        td.content = td.block()
        children.add(td)
    }
    fun html():String{
        val builder = StringBuilder()
        builder.append("\n\t<tr>")
        for (childTag in children){
            builder.append(childTag.html())
        }
        builder.append("\n\t</tr>")
        return builder.toString()
    }
}
class Table{
    private val children = ArrayList<Tr>()
    fun tr(block:Tr.()->Unit){
        val tr = Tr()
        tr.block()
        children.add(tr)
    }
    fun html():String{
        val builder = StringBuilder()
        builder.append("<table>")
        for (childTag in children){
            builder.append(childTag.html())
        }
        builder.append("</table>")
        return builder.toString()
    }
}
fun table(block: Table.() -> Unit):String{
    val table = Table()
    table.block()
    return table.html()
}