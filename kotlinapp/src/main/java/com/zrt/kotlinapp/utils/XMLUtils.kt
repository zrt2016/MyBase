package com.zrt.kotlinapp.utils

import org.xml.sax.Attributes
import org.xml.sax.InputSource
import org.xml.sax.helpers.DefaultHandler
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader
import java.lang.Exception
import java.util.*
import javax.xml.parsers.SAXParserFactory

/**
 * @author：Zrt
 * @date: 2022/7/24
 */

// checkBlock例子
fun blockDemo(nodeName:String):Boolean
        = when(nodeName){
    "id" -> true
    "name" -> true
    "user" -> true
    else -> false
}
/**
 * xml pull解析方式
 */
fun oarseXMLWithPull(xmlData:String, checkBlock:(String)->Boolean, putBlock:(String, String)->Unit){
    try {
        val factory = XmlPullParserFactory.newInstance()
        val newPullParser = factory.newPullParser()
        newPullParser.setInput(StringReader(xmlData))
        var eventType = newPullParser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            val nodeName = newPullParser.name
            when (eventType) {
                // 开始解析某个节点
                XmlPullParser.START_TAG ->{
                    if (checkBlock(nodeName)){
                        // 获取字节名称，和对应值
                        putBlock(nodeName, newPullParser.nextText())
                    }
                }
                // 完成解析某个节点
                XmlPullParser.END_TAG ->{}
            }
            eventType = newPullParser.next()
        }
    }catch (ex:Exception){

    }
}

/**
 * SAX解析
 */
fun parseXMLWithSAX(xmlData:String, block: (String, String) -> Unit){
    try {
        val factory = SAXParserFactory.newInstance()
        val xmlReader = factory.newSAXParser().xmlReader
        val handler = ContentHandler(block)
        xmlReader.contentHandler = handler
        xmlReader.parse(InputSource(StringReader(xmlData)))
    }catch (ex:Exception){

    }
}
class ContentHandler(val block:(String, String)->Unit): DefaultHandler(){
    //记录节点名称
    var nodeName:String = ""
//    lateinit var mapData:HashMap<String, String>
    override fun startDocument() {
        // 开始XML解析时调用
//        mapData = hashMapOf<String, String>()
    }

    override fun startElement(uri: String?, localName: String, qName: String?, attributes: Attributes?) {
        // 解析某个节点时调用
        nodeName = localName

    }

    override fun characters(ch: CharArray, start: Int, length: Int) {
        // 获取节点内容时调用
//        mapData.put(nodeName, String(ch, start, length))
        block(nodeName, String(ch, start, length))
    }

    override fun endElement(uri: String?, localName: String?, qName: String?) {
        // 完成某个节点解析时调用
    }
    override fun endDocument() {
        // 完成整个XML解析时调用
    }
}