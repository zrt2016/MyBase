package com.zrt.kotlinapp.designmode

import java.lang.StringBuilder
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

/**
 * 设计模式
 * @author：Zrt
 * @date: 2022/10/18
 * 1、结构性设计模式：从程序的结构上解决模块之间的耦合问题
 *  包括以下模式（*标记，有例子）：适配器模式、 *代理模式、 *装饰模式、 *外观模式、桥接模式、组合模式和 *享元模式
 * 2、行为型设计模式：处理类和对象如何交互及如何分配职责。共有11种模式：*策略、*模板方法、*观察者、迭代器、
 *  责任链、命令、备忘录、状态、访问者、中介者和解释器模式。
 */

/** TODO
 * 工厂方法模式
 * 1、抽象产品：Computer
 * 2、具体产品类：LenovoComputer、HpComputer
 * 3、抽象工厂：ComputerFactory
 * 4、具体工厂类：GDCComputerFactory
 */
// 抽象产品
abstract class Computer{
    abstract fun start()
}
// 抽象工厂
abstract class ComputerFactory{
    abstract fun <T:Computer> createComputer(clazz: Class<T>):T
}
// 具体产品类
class LenovoComputer:Computer(){
    override fun start() {
        println("联想电脑")
    }
}
class HpComputer:Computer(){
    override fun start() {
        println("惠普电脑")
    }
}
// 具体工厂类
class GDCComputerFactory: ComputerFactory(){
    override fun <T : Computer> createComputer(clazz: Class<T>): T {
        val name = clazz.name
        // 通过反射创建不同厂家的计算机
        var newInstance = Class.forName(name).newInstance() as Computer
        return newInstance as T
    }
}

/**  TODO
 * 建造者模式
 * 1、Director：导演类，负责安排已有模块的顺序，然后通知Builder开始建造
 * 2、Builder：抽象builder类，规范产品的组建，一般由子类实现
 * 3、ConcreteBuilder：具体建造者，实现抽象Builder类定义的所有方法，并返回建好的对象
 * 4、Product：产品类
 */
// 产品类
class BComputer{
    var mCpu:String = ""
    var mMainboard:String = ""
    var mRam:String = ""
}
// 创建Builder规范产品的组建
abstract class BBuilder{
    abstract fun BuildCpu(cpu:String)
    abstract fun BuildMainboard(mainboard:String)
    abstract fun BuildRam(ram:String)
    abstract fun create():BComputer
}
// 实现抽象的builder类
class MoonComputerBuilder: BBuilder(){
    val computer = BComputer()
    override fun BuildCpu(cpu: String) {
        computer.mCpu = cpu
    }
    override fun BuildMainboard(mainboard: String) {
        computer.mMainboard = mainboard
    }
    override fun BuildRam(ram: String) {
        computer.mRam = ram
    }
    override fun create(): BComputer {
        return computer
    }
}
// 导演类完成组装
class BDirector(val builder: BBuilder){
    fun createBComputer(cpu:String, mainboard: String, ram: String):BComputer{
        builder.BuildCpu(cpu)
        builder.BuildMainboard(mainboard)
        builder.BuildRam(ram)
        return builder.create()
    }
}

/** TODO
 * 代理模式：静态代理和动态代理
 * Subject：抽象主题类，声明代理类和真实主题类的共同接口方法
 * RealSubject：真实主题类，代理类所代表的真实主题，客户端通过代理类间接调用真实主题类的方法
 * Proxy：代理类，持有真实主题类的引用，在其接口中实现真实主题类的接口方法
 * Client：客户端
 *
 * 动态代理优点：真实主题类可以随时发生变化，因为其实现了公共接口，所以代理类无需任何修改即可使用。
*/
// 静态代理
// 抽象类
interface PSubject{
    fun buy()
}
// 主题类
class PRealSubject:PSubject{
    override fun buy() {
        println("购买")
    }
}
// 代理类
class PProxy(val pSubject: PSubject):PSubject{
    override fun buy() {
        pSubject.buy()
    }
}
// 动态代理：通过反射来动态地生成代理类
// 修改代理类实现InvocationHandler动态代理接口
class DynamicPProxy(val any:Any): InvocationHandler{
    override fun invoke(proxy: Any?, method: Method, args: Array<out Any>?): Any? {
        val invoke = method.invoke(any, *args.orEmpty()) // 需要展开args数组
        if (method.name.equals("buy")){
            println("zrt 买买买")
        }
        return invoke
    }
}

/** TODO
 * 装饰者模式：动态给一个对象添加一些额外的职责，就增加功能来说，装饰模式比生成子类更加灵活
 * Component：抽象组件，可以使接口或是抽象类，被装饰的最原始的对象
 * ConcreteComponent：组件具体实现类，COmponent的具体实现类，被装饰的具体对象
 * Decorator：抽象装饰者，从外类来拓展Component类的功能，但对于Component类来说，无需知道Decorator的存在。
 *         Decorator中必有一个属性持有Component的抽象组件
 * ConcreteDecorator：装饰者的具体实现类
 *
 *  优点：
 *      1、避免使用继承方式扩展对象而带来的灵活性差，子类无限扩展的问题。
 *      2、具体组件类金额装饰类可以独立变化
 *      3、动态扩展对象，可以选择不同的装饰器，实现不同的行为
 * 缺点：
 *      1、装饰层数不能过多，否则影响效率
 *      2、Component内部如果发生改变，则会影响到子类
 *      3、排错会更加困难，寻找错误需要逐级排查
 */
// 抽象组件
abstract class DComponent{
    abstract fun attackMagic()
}
// 组件具体实现类
class Yangguo : DComponent(){
    override fun attackMagic() {
        println("杨过，全真剑法")
    }
}
// 抽象装饰者
abstract class DDecorator(val component:DComponent): DComponent(){
    override fun attackMagic() {
        component.attackMagic()
    }
}
// 装饰者具体实现类
class HongQiGong(val mComponent:DComponent): DDecorator(mComponent){
    override fun attackMagic() {
        super.attackMagic()
        println("洪七公教授大狗棒法")
    }
}

/** TODO
 * 外观模式：通过一个外观类使得整个系统的结构只有一个同一的高层接口，降低用户使用成本。
 * 定义：要求一个子系统的外部与内部的通信必须通过一个同一的对象进行，此模式提供一个高层接口，使得子系统更易于使用。
 * Facade：外观类，知道哪些子系统类负责处理请求，将客户端的请求代理给适当的子系统对象
 * Subsystem：子系统类，可以有一个或者多个子系统。实现子系统的功能，处理外观类指派的任务，注意子系统不含有外观类的引用
 * 外观模式的简单实现例子：例：一个人的武功可分为招式、内功和经脉，即为三个系统
 *
 * 优点：1、减少系统的相互依赖，所有依赖都是对外观类的依赖。2、对用户隐藏了子系统的具体实现。减少用户对子系统的耦合。
 *      3、加强了安全性，子系统的方法如果不在外观中开通，就无法访问子系统中的方法
 * 缺点：不符合开放封闭原则，如果业务出现变更，则可能要直接修改外观类
 */
// 招式：子系统类
class Zhaoshi{
    fun taijiquan(){ println("太极拳")}
    fun dugujiujian(){ println("独孤九剑")}
}
// 内功：子系统类
class Neigong{
    fun jiuyangshengong(){ println("九阳神功")}
    fun longxiangboregong(){ println("龙象般若功")}
}
// 经脉：子系统类
class Jingmai{
    fun jingmai(){ println("经脉") }
}
// 外观类:持有子系统的引用
class Wuxue{
    private lateinit var zhaoshi: Zhaoshi
    private lateinit var neigong: Neigong
    private lateinit var jingmai: Jingmai
    constructor(){
        zhaoshi = Zhaoshi()
        neigong = Neigong()
        jingmai = Jingmai()
    }
    fun shizhan(){ // 实战
        jingmai.jingmai() // 开起经脉
        neigong.jiuyangshengong() // 运行九阳神功
        neigong.longxiangboregong() // 运行龙象般若功
        zhaoshi.taijiquan() // 施展太极拳
    }
}

/** TODO
 * 享元模式：池技术的重要实现方式，可以减少应用程序创建的对象，降低程序内存的占用，提高程序的性能。
 * 定义：使用共享对象有效地支持大量细粒度的对象
 * 细粒度对象：会是得对象数量多且性质相近。这些对象分为两个部分：内部状态和外部状态。
 *    1、内部状态：对象可共享出来的信息，存储在享元对象内部并且不会随环境的改变而改变。
 *    2、外部状态：对象依赖的一个标记，会随环境改变而改变并且不可共享
 * Flyweight：抽象享元角色，同时定义出内部状态和外部状态的接口或者实现
 * ConcreteFlyweight：具体享元对象
 * FlyweightFactory：享元工厂，负责管理对象池和创建享元对象
 * 例子：网上商城卖商品，如果每个用户下单都生成商品对象，显然会耗费很多资源，订单多时会产生oom。
 * 存在大量相似的对象或需要缓冲池的场景时，可使用享元模式
 */
// 抽象享元角色
interface Flyweight{
    fun showGoodsPrice(name:String)
}
// 具体享元对象
class ConcreteFlyweight(private var name:String):Flyweight{
//    private var name:String = "" // 内部状态 名称
//    private var version:String // 外部状态 版本
    override fun showGoodsPrice(name: String) {
        when(name){
            "32G" -> println("价格为5199")
            "128G" -> println("价格为5999")
        }
    }
}
// 享元工厂
object FlyweightFactory{
    val pool = linkedMapOf<String, ConcreteFlyweight>()
    fun getConcreteFlyweight(name: String):ConcreteFlyweight{
        if (pool.containsKey(name)){
            println("使用缓存，key为：$name")
            return pool.getValue(name)
        }else{
            val concreteFlyweight = ConcreteFlyweight(name)
            pool[name] = concreteFlyweight
            return concreteFlyweight
        }
    }
}

/** TODO
 * 策略模式：定义一系列的算法，把每一个算法封装起来，并且使它们可相互替换，策略模式使得算法可独立于使用它的客户而独立变化
 * Context：上下文角色，用来操作策略的上下文环境，起到承上启下的作用，屏蔽高层模块对策略和算法的直接访问
 * Stragety：抽象策略角色，策略、算法的抽象，通常为接口
 * ConcreteStragety：具体的策略实现
 * 例子：一个武者面对不同敌人，使用不同的策略
 * 优点：避免多重条件语句的使用，易于拓展，增加策略时，实现接口即可
 * 缺点：策略类过多，复用性小。上层模块需要知道具体策略
 */
// 策略接口
interface CStragety{
    fun fighting()
}
// 具体策略实现:
class WerkCStragety:CStragety{
    override fun fighting() {
        println("比较弱的对手，使用拳脚")
    }
}
class StrongCStragety:CStragety{
    override fun fighting() {
        println("比较强的对手，使用武器、内功")
    }
}
// 上下文角色
class CContext(val cStragety: CStragety){
    fun fighting(){
        cStragety.fighting()
    }
}

/** TODO
 * 模板方法模式：定义一个操作中的算法框架，讲一个步骤延迟到子类中，使得子类不改变一个算法的结构即可重定义算法的某些特定步骤
 * AbstractClass：抽象类：定义了一套算法框架
 * ConcreteClass：具体实现类
 * 优点：子类实现算法的细节，有助于算法的扩展。把算法不变的细节搬到超类，去除了子类的重复代码。
 * 缺点：不同实现都需要定义一个类
 */
// 创建抽象类，定义算法框架
abstract class AbstractClass{
    final fun fighting(){ // 不可被子类继承
        neigong() // 运行内功
        jingmai() // 调整经脉
        if (hasWeapons()){ // 如果有武器，使用武器
            weapons()
        }
        moves() // 使用招式
        hook() // 钩子方法
    }

    abstract fun neigong()
    fun jingmai(){
        println("开起经脉")
    }
    open fun hasWeapons(): Boolean{ // 默认有武器
        return true
    }
    abstract fun weapons()
    abstract fun moves()
    open fun hook(){ } // 交由子类自定义
}
// 具体实现类
class Zhangsan:AbstractClass(){
    override fun neigong() {
        println("九阴真经")
    }
    override fun weapons() {}

    override fun moves() {
        println("天山折梅手")
    }

    override fun hasWeapons(): Boolean {
        return false
    }
}

/** TODO
 * 观察者模式：对象间一种一对多的依赖关系，每当一个对象状态改变时，则所有依赖于他的对象都会收到通知，并被自动更新
 * Subject:抽象主题，抽象主题角色吧所有观察对象保存在一个集合中，每个主题都可以与任意数量的观察者，提供一个接口，进行增删观察者对象
 * ConcreteSubject：具体主题，该角色将有关状态存入具体观察者对象，具体主题的内部发生改变时，给所有注册观察者对象发送通知
 * Observe：抽象观察者，定义一个更新接口，使得主题发生变化是通知更新
 * ConcreteObserve：具体观察者，实现抽象观察者定义的更新接口，方便在收到主题变更时，更新自身状态
 * 优点：观察者和被观察者之间是抽象耦合的，易拓展。方便建立触发机制
 * 缺点：需要考虑开发效率和运行效率问题，如果其中一个流程比较复杂导致卡顿，会影响整体的运行效率，这种情况下一般要使用异步运行
 */
// 抽象观察者
interface OObserve{
    fun update(messgae:String)
}
// 具体观察者
class OConcreteObserve(val name:String):OObserve{
    override fun update(messgae: String) {
        println("##name=$name-msg$messgae")
    }
}
// 抽象主题(抽象被观察者)
interface OSubject{
    fun attch(observe:OObserve) // 增加订阅者
    fun detach(observe:OObserve) // 删除订阅者
    fun notify(msg:String) // 更新订阅者
}
// 具体被观察者类
class OConcreteSubject:OSubject{
    val userList = mutableListOf<OObserve>()
    override fun attch(observe: OObserve) {
        userList.add(observe)
    }
    override fun detach(observe: OObserve) {
        userList.remove(observe)
    }
    override fun notify(msg: String) {
        for ((index, value) in userList.withIndex()){
            userList.get(index).update(msg)
        }
    }
}

fun main(){
    val gdcComputerFactory = GDCComputerFactory()
    val createComputer = gdcComputerFactory.createComputer(LenovoComputer::class.java)
    createComputer.start()
    // TODO 建造者模式：客户端完成调用
    val mBBuilder = MoonComputerBuilder()
    BDirector(mBBuilder).createBComputer("i9-7700", "华擎玩家至尊", "十铨DDR4")
    // TODO 代理模式(静态代理)，客户端实现
    PProxy(PRealSubject()).buy()
    // 动态代理
    val pRealSubject = PRealSubject()
    // 创建动态代理
    val dynamicPProxy = DynamicPProxy(pRealSubject)
    val classLoader = pRealSubject::class.java.classLoader
    // 动态代理创建类
    val newProxyInstance = Proxy.newProxyInstance(classLoader, arrayOf(PSubject::class.java), dynamicPProxy)
    // 实现
    if (newProxyInstance is PSubject) {
        newProxyInstance.buy()
    }
    //  TODO 装饰模式
    val yangguo = Yangguo()
    val hongQiGong = HongQiGong(yangguo)
    hongQiGong.attackMagic()

    // TODO 外观模式
    val wuxue = Wuxue()
    wuxue.shizhan()
    // TODO 享元模式
    val xiaomi11 = FlyweightFactory.getConcreteFlyweight("Xiaomi11")
    xiaomi11.showGoodsPrice("32")

    // TODO 策略模式
    val context = CContext(WerkCStragety())
    context.fighting()

    // TODO 模板方法模式
    Zhangsan().fighting()

    // TODO 观察者模式
    val oOSubject = OConcreteSubject()
    // 创建用户以及订阅公众号
    oOSubject.attch(OConcreteObserve("张三"))
    oOSubject.attch(OConcreteObserve("李四"))
    oOSubject.attch(OConcreteObserve("王五"))
    // 公众号更新，发送通知
    oOSubject.notify("专栏更新")

}