package com.zrt.kotlinapp.activity_view.custom_view_basic.matrix_basic

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.zrt.kotlinapp.R

/**
 * @author：Zrt
 * @date: 2023/5/8
 *  Matrix(矩阵)与坐标转换
 *  一、矩阵的加法和减法运算：只有两个矩阵的行数和列数分别相等才有意义
 *     例：2个矩阵 A 和 B
 *         {{1, 2, 3},     {{11, 22, 33},
 *      A = {4, 5, 6},  B = {44, 55, 66},
 *          {7, 8, 9}}      {77, 88, 99}}
 *                                     {{12, 24, 36},
 *      C = A + B = A[x][y] + B[x][y] = {48, 60, 72},
 *                                      {84, 96, 108}}
 *  二、矩阵与数的乘法：即数 γ 乘以矩阵 A，就是将数 γ 乘以矩阵 A 中的每一个元素，记为 γA
 *                       {{10, 20, 30},       {{5, 10, 15},
 *      X = (B - A) / 2 = {40, 50, 60},  / 2 = {20, 25, 30},  = (B[x][y] - A[x][y]) / 2
 *                        {70, 80, 90}}        {35, 40, 45}}
 *  三、矩阵与矩阵的乘法
 *      设：A = m * p 的矩阵(m行p列), B = p * n 的矩阵(p行n列) 即：C = AB = p * p (p行p列)
 *      规则：
 *        1) C的行数=A的行数，C的列数=B的列数。
 *        2) C的第i行第j列元素由A的第i行元素与B的第j列元素对应相乘，再取乘积之和。
 *      例：C[j][i] = A[n][i] * B[j][n]
 *              {{1, 2, 3},   {{1, 4},   {{C00, C10},   {{A00*B00 + A10*B01 + A20*B02, A00*B10 + A10*B11 + A20*B12},
 *      C = AB = {4, 5, 6}} *  {2, 5}, =  {C01, C11}} =  {A01*B00 + A11*B01 + A21*B02, A01*B10 + A11*B11 + A21*B12}}
 *                             {3, 6}}
 *          {{1*1 + 2*2 + 3*3, 1*4 + 2*5 + 3*6},   {{14, 32},
 *        =  {4*1 + 5*2 + 6*3, 4*4 + 5*5 + 6*6}} =  {32, 77}}
 *
 *      Cpp = A[0][p]*B[p][0] + A[1][p]*B[p][1] + .... + A[i][p]*B[p][j]
 *               {{1, 4},    {{1, 2, 3},                {{1*1+4*4, 1*2+4*5, 1*3+4*6},  {{17, 22, 27},
 *      D = BA =  {2, 5},  *  {4, 5, 6}}  = 3行 * 3列 =  {2*1+5*4, 2*2+5*5, 2*3+5*6}, = {22, 29, 36},
 *                {3, 6}}                               {3*1+6*4, 3*2+6*5, 3*3+6*6}}   {27, 36, 45}}
 *  四、运算性质
 *      1、结合律：(AB)C = A(BC)
 *      2、分配率：A(B±C) = AB±AC (左分配律)；(B±C)A = BA±CA (右分配律)；
 *      3、(γA)B = γ(AB) = A(γB)
 *
 *  五、色彩变化矩阵
 *      四阶色彩矩阵{{R, 0, 0, 0}, // 红
 *                  {0, G, 0, 0}, // 绿
 *                  {0, 0, B, 0}, // 蓝
 *                  {0, 0, 0, A}} // 透明
 *     修改一个色彩为半透明：{{1, 0, 0, 0},    {{R},   {{R},
 *                          {0, 1, 0, 0},     {G},   {G},
 *                          {0, 0, 1, 0},  *  {B}, = {B},
 *                          {0, 0, 0, 0.5}}   {A}}   {0.5A}}
 *     如果只修改R色，则需要再多加一阶来表示平移变化。所有一个既包含线性变换又包含平移变换的组合变换称为仿射变换
 *     五阶矩阵，红色分量值*2，绿色增加100：
 *     {{2, 0, 0, 0, 0},     {{25},      {{50},
 *      {0, 1, 0, 0, 100},    {100},      {200},
 *      {0, 0, 1, 0, 0},   *  {100},   =  {100},
 *      {0, 0, 0, 1, 0}}      {255},      {255}}
 *                            {1}}
 *     绿色增加100，分量值用100
 *  @see matrixColor 色彩变化矩阵使用 ColorMatrix 表示。
 *      ColorMatrix是一个4*5的矩阵
 *
 *  六、色彩旋转运算
 *      例：R（平面向右的X轴）、G（平面向上的Y轴），B（平面上的Z轴）
 *      以Z轴为中心，逆时针旋转 α度：
 *          最终R = 原R*cosα - 原G*sinα  （G有一部分转向了R的负轴方向上）
 *          最终G = 原R*sinα + 原G*cosα   （R值有一部分移动到了G值上 R*sinα）
 *      1、旋转色彩、围绕蓝色轴旋转α度,对应色彩变化矩阵,R轴旋转后，R矩阵上的R值 = 原R*sinα，G值 = 原R*sinα
 *       可参考ColorMatrixView 中的矩阵 5
 *          (cosα,sinα,0f,0f,0f
 *          ,-sinα,cosα,0f,0f,0f
 *          ,0f,0f,1f,0f,0f
 *          ,0f,0f,0f,1f,0f
 *          ,0f,0f,0f,0f,1f)
 *      2、绕红色轴旋转 α 度，对应色彩变化矩阵
 *          (1,0,0f,0f,0f
 *          ,0f, cosα,sinα,0f,0f
 *          ,0f,-sinα,cosα,0f,0f
 *          ,0f,0f,0f,1f,0f
 *          ,0f,0f,0f,0f,1f)
 *      3、绕绿色轴旋转 α 度，对应色彩变化矩阵
 *          (cosα,0f,0f,-sinα,0f
 *          ,0f,1f,0f,0f,0f
 *          ,sinα,0f,cosα,0f,0f
 *          ,0f,0f,0f,1f,0f
 *          ,0f,0f,0f,0f,1f)
 *  七、色彩的投射运算：在运算中将红色运算单独标记，通过利用B、G、A的颜色值的分量来增加红色值。
 *      1、黑白照片：可参考ColorMatrixView 中的矩阵 6
 *      2、色彩反色：可参考ColorMatrixView 中的矩阵 7
 *      3、照片做旧：可参考ColorMatrixView 中的矩阵8
 *  八、ColorMatrix 函数
 *      1、构造函数：
 *          ColorMatrix() ： 需要与其他函数共用
 *          ColorMatrix(float[] src) ： 传入一个长度20的数组
 *          ColorMatrix(ColorMatrix src) ：复制另一个ColorMatrix实例
 *      2、第一个构造函数ColorMatrix()需要与其他函数共用. 主要是设置和重置函数
 *          set(ColorMatrix src)
 *          set(float[] src)
 *          reset():重置函数,即将矩阵重置初始化 a[0] = a[6] = a[12] = a[18] = 1;
 *      3、饱和度设置
 *   @see handleColorMatrixSaturationBmp 获取饱和度修改后的图片
 *          setSaturation(float):整体增加色彩饱和度，同时增强R、G、B的色彩饱和度
 *              参数float sat：表示把当前色彩饱和度放大的倍数。
 *                  等于0，表示无色彩，即灰度图像。等于1，色彩不变。大于1，色彩过度饱和
 *      4、色彩缩放
 *          setScale(float rScale, float gScale, float bScale, float aScale)
 *              参数分别对应R、G、B、A颜色值的缩放倍数
 *      5、色彩旋转
 *          setRotate(int axis, float degrees)
 *              参数1：axis=0 围绕红色轴旋转。axis=1，围绕绿色轴旋转。axis=2，围绕蓝色轴旋转。
 *              参数2：旋转度数
 *      6、ColorMatrix 相乘
 *          setConcat(ColorMatrix matA, ColorMatrix matB)：matA * matB = 当前ColorMatrix的值
 *          preConcat(ColorMatrix prematrix)：当前矩阵 乘以 prematrix
 *          postConcat(ColorMatrix postmatrix): prematrix 乘以 当前矩阵
 *
 */

fun matrixColor() {
    arrayOf(arrayOf(1f,0f,0f,0f,0f), arrayOf(1f,0f,0f,0f,0f))
//    Array<FloatArray>(5){floatArrayOf(1f,0f,0f,0f,0f)}.
    val colorMatrix = ColorMatrix(floatArrayOf(1f,0f,0f,0f,0f
                                              ,0f,1f,0f,0f,0f
                                              ,0f,0f,1f,0f,0f
                                              ,0f,0f,0f,0.5f,0f))
    Paint().setColorFilter(ColorMatrixColorFilter(colorMatrix))
    colorMatrix.reset()
//    colorMatrix.setSaturation()
//    colorMatrix.setScale()
//    colorMatrix.setRotate()
//    colorMatrix.setConcat()
//    colorMatrix.preConcat()
//    colorMatrix.postConcat()
}
// TODO 通过不同 ColorMatrix 修改图片
class ColorMatrixView: View {
    lateinit var paint:Paint
    var bitmap:Bitmap? = null
    // 一个参数的构造函数是在代码中创建对象的时候会被调用.
    constructor(context: Context):this(context, null)
    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?):this(context, attrs, 0)
    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):super(context, attrs, defStyleAttr) {
        paint = Paint()
        paint.isAntiAlias = true
        bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.dog)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        bitmap?.let {
            paint.setColorFilter(null)
            canvas?.drawBitmap(it, null, Rect(0, 0, 300, 300*it.height/it.width), paint)
            canvas?.translate(310f, 0f)
            // 1、五阶矩阵：只保留蓝色通道
            var colorMatrix = ColorMatrix(floatArrayOf(0f,0f,0f,0f,0f  // R
                                                      ,0f,0f,0f,0f,0f  // G
                                                      ,0f,0f,1f,0f,0f  // B
                                                      ,0f,0f,0f,1f,0f))// A
            // 2、增量特定颜色饱和度，指定绿色值添加50
            colorMatrix = ColorMatrix(floatArrayOf(
                    1f,0f,0f,0f,0f
                    ,0f,1f,0f,0f,50f
                    ,0f,0f,1f,0f,0f
                    ,0f,0f,0f,1f,0f
            ))
//            // 3、色彩反转
            colorMatrix = ColorMatrix(floatArrayOf(
                    -1f,0f,0f,0f,255f
                    ,0f,-1f,0f,0f,255f
                    ,0f,0f,-1f,0f,255f
                    ,0f,0f,0f,1f,0f
            ))
//            // 4、亮度调节，色彩的缩放运算，针对某个颜色值进行放大和缩小
            colorMatrix = ColorMatrix(floatArrayOf(
                    1.2f,0f,0f,0f,255f
                    ,0f,1.2f,0f,0f,255f
                    ,0f,0f,1.2f,0f,255f
                    ,0f,0f,0f,1.2f,0f
            ))
//            // 5、旋转色彩、围绕蓝色轴旋转α度
            colorMatrix = ColorMatrix(floatArrayOf(
                    Math.cos(30.0).toFloat(),Math.sin(30.0).toFloat(),0f,0f,255f
                    ,-Math.sin(30.0).toFloat(),Math.cos(30.0).toFloat(),0f,0f,255f
                    ,0f,0f,1.2f,0f,255f
                    ,0f,0f,0f,1.2f,0f
            ))
            // 6、黑白图片：去色原理，只要把RGB三通道的色彩信息设置长一样，即R=G=B,图像就变成了灰色。
            // 并且为了保证图像亮度不变，同一个通道中的 R+G+B = 1，即：0.213 + 0.715 + 0.072 = 1
            colorMatrix = ColorMatrix(floatArrayOf(
                     0.213f, 0.715f, 0.072f, 0f, 0f
                    ,0.213f, 0.715f, 0.072f, 0f, 00f
                    ,0.213f, 0.715f, 0.072f, 0f, 0f
                    ,0f,0f,0f,1f,0f
            ))
            // 7、色彩反色：利用色彩变换矩阵将两个颜色反转，例红色和绿色反色
            colorMatrix = ColorMatrix(floatArrayOf(
                    0f,1f,0f,0f,0f
                    ,1f,0f,0f,0f,0f
                    ,0f,0f,1f,0f,0f
                    ,0f,0f,0f,1f,0f
            ))
            // 8、照片变旧
            colorMatrix = ColorMatrix(floatArrayOf(
                    1/2f,1/2f,1/2f,0f,0f
                    ,1/3f,1/3f,1/3f,0f,0f
                    ,1/4f,1/4f,1/4f,0f,0f
                    ,0f,0f,0f,1f,0f
            ))
            colorMatrix.setSaturation(0f)
            paint.setColorFilter(ColorMatrixColorFilter(colorMatrix))
            canvas?.drawBitmap(it, null, Rect(0, 0, 300, 300*it.height/it.width), paint)
        }
    }
}

/**
 * 获取饱和度修改后的图片
 */
fun handleColorMatrixSaturationBmp(mOriginBitmap:Bitmap, tempBitmap:Bitmap, progress: Int): Bitmap {
    val canvas = Canvas(tempBitmap)
    val paint = Paint()
    val colorMatrix = ColorMatrix()
    colorMatrix.setSaturation(progress.toFloat() / 100f)
    paint.setColorFilter(ColorMatrixColorFilter(colorMatrix))
    canvas.drawBitmap(mOriginBitmap, 0f, 0f, paint)
    return tempBitmap
}

/**
 * 色彩缩放
 */
fun handleColorMatrixScaleBmp(mOriginBitmap:Bitmap, tempBitmap:Bitmap, progress: Int): Bitmap {
    val canvas = Canvas(tempBitmap)
    val paint = Paint()
    val colorMatrix = ColorMatrix()
    val pro = progress.toFloat() / 100f
    colorMatrix.setScale(pro, pro, pro, pro)
    paint.setColorFilter(ColorMatrixColorFilter(colorMatrix))
    canvas.drawBitmap(mOriginBitmap, 0f, 0f, paint)
    return tempBitmap
}

/**
 * 色彩旋转
 */
fun handleColorMatrixRotateBmp(mOriginBitmap:Bitmap, tempBitmap:Bitmap, progress: Int): Bitmap {
    val canvas = Canvas(tempBitmap)
    val paint = Paint()
    val colorMatrix = ColorMatrix()
    // 当前位置progress-180，即中间位置的数字。中间位置的色彩旋转度数为0，整个旋转度数范围-180~180。
    // 360是正余函数的最小正周期
    val pro = progress.toFloat() - 180f
    colorMatrix.setRotate(2, pro)
    paint.setColorFilter(ColorMatrixColorFilter(colorMatrix))
    canvas.drawBitmap(mOriginBitmap, 0f, 0f, paint)
    return tempBitmap
}