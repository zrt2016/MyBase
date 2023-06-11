package com.zrt.kotlinapp.activity_view.custom_basic.canvas_basic

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.*
import android.graphics.drawable.shapes.*
import android.os.Build
import android.os.Environment
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.annotation.IdRes
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.utils.LogUtil
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * @author：Zrt
 * @date: 2023/3/30
 * Canvas 基础
 * 一、获取 Canvas 对象
 *   1、方法一：重写 onDraw、dispatchDraw 函数获取
 *      onDraw(Canvas) 函数用于绘制视图自身
 *      dispatchDraw(Canvas) 函数用于绘制子视图
 *      两函数在 View 和 ViewGroup 中的调用顺序：onDraw() ---> dispatchDraw()
 *      在绘制 View 时，需要重写 onDraw 函数。绘制 ViewGroup 时，需要重写 dispatchDraw 函数
 *   2、方法二：使用 Bitmap 创建
 *      val canvas = Canvas()  canvas.setBitmap(bitmap)
 *      val canvas = Canvas(Bitmap)
 *      Bitmap 的获取(此处只写2种)：
 *          Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888) 绘制空白图像
 *          BitmapFactory.decodeResource(getResource(), R.drawable.xxx, null) 从图片中加载
 *      注：用 Bitmap 创建的 Canvas 绘制的图像都会保存在 Bitmap 中，如果绘制在 View 上
 *          ，需要使用 onDraw(Canvas) 函数中的 Canvas 在绘制一次 Bitmap 。
 *   3、方法三：调用 SurfaceHolder.lockCanvas() 函数
 * 二、图层（Layer）与画布（Bitmap）
 * @see com.zrt.kotlinapp.activity_view.custom_basic.view_basic.CanvasView 可参考该类系列函数
 *      1、drawXXX()：每次调用都会生成一个透明图层来专门绘制这个图形，然后叠加到最近的画布上
 *      2、save():保存当前画布，将其放入特定的栈中
 *      3、restore()：获取栈中顶层的画布状态，并按照该状态恢复至当前画布,再次绘制时会在顶层画布上绘制。
 *          只是恢复图层，不会清除已绘制内容。
 *      4、restoreToCount(Int saveCount): 一直退栈，直到把指定的 saveCount 索引退出为止。
 *          save 相关函数保存画布后，都会返回一个ID值，这个ID值表示当前保存画布信息的栈层索引（从0开始）
 *          Int saveCount：图层索引 ID
 *          例：有 id1（红色长度100的矩形）、id2（绿色长度70的矩形）、id3（黄色长度50的矩形）、id4（蓝色长度30的矩形）依次创建
 *              此时 restoreToCount(id3) 后，图层会恢复到创建 id2 矩形的时候。再次绘制会在 id2 图层上绘制，
 *                  修改 id2 画布的背景颜色，会覆盖该图层上所有显示。
 *      5、saveLayer()：
 *          saveLayer(RectF bounds, Paint paint)
 *              参数 RectF bounds：保存区域所对应的矩形对象
 *          saveLayer(float left, float top, float right, float bottom, Paint paint, int saveFlags)
 *              参数 float left, float top, float right, float bottom 保存区域对应矩形区域
 *              int saveFlags: 常用 Canvas.ALL_SAVE_FLAG 表示保存全部内容
 *                  取值：ALL_SAVE_FLAG、MATRIX_SAVE_FLAG、CLIP_SAVE_FLAG、HAS_ALPHA_LAYER_SAVE_FLAG
 *                      、FULL_COLOR_LAYER_SAVE_FLAG、CLIP_TO_LAYER_SAVE_FLAG
 *          调用 saveLayer() 函数后，会生成一块画布（bitmap），大小即为指定的保存区域大小。画布为全透明，
 *          且调用该函数后所有的绘图操作都是在该画布上进行。
 *          在使用Xfermode前，在画布上所绘制的内容均为目标图片，绘制区域外均为透明图层，所以在使用 SRC_IN模式时，
 *          会出现目标图片与源图片相交之外的其他源图片内容没有显示。
 *      6、saveLayerAlpha()：与 saveLayer() 的区别，新增了 alpha 参数，可指定画布的透明度，取值 0 ~ 255，
 *              也可以用16进制 0xAA 表示， 取 0 时完全透明。
 *          saveLayerAlpha(RectF bounds, int alpha)
 *          saveLayerAlpha(RectF bounds, int alpha, int saveFlags)
 *              参数int alpha：指定图层的透明度，取值 0 ~ 255，或16进制取值 0xAA。取 0 完全透明
 *      7、save函数中 int saveFlags 参数（api28 中只剩下 ALL_SAVE_FLAG，其余均被删除）：
 *          修改 compileSdkVersion 和 buildToolsVersion 为 27后使用正常
 *          ALL_SAVE_FLAG（适用于 save()、saveLayer()）：保存所有标识
 *          MATRIX_SAVE_FLAG（适用于 save()、saveLayer()）：仅保存 Canvas 的 matrix 数组，在恢复时也只会恢复位置信息。
 *              如果在其中对画布进行了剪裁，那么恢复时，不会恢复画布原大小。
 *              在 saveLayer() 中需要与 HAS_ALPHA_LAYER_SAVE_FLAG 一起使用，否则新建画布所在区域原图像会被清空。
 *          CLIP_SAVE_FLAG（适用于 save()、saveLayer()）：仅保存 Canvas 的当前大小
 *              在 saveLayer() 中需要与 HAS_ALPHA_LAYER_SAVE_FLAG 一起使用，否则新建画布所在区域原图像会被清空。
 *          HAS_ALPHA_LAYER_SAVE_FLAG（适用于 saveLayer()）：标识新建bmp具有透明度，在与上层画布结合时，
 *              透明位置显示上层图像。与 FULL_COLOR_LAYER_SAVE_FLAG 冲突，同时指定时以 HAS_ALPHA_LAYER_SAVE_FLAG 为主。
 *          FULL_COLOR_LAYER_SAVE_FLAG（适用于 saveLayer()）：标识新建bmp颜色完全独立，在与上层画布结合时，
 *              先清空上层画布再覆盖上去。
 *          CLIP_TO_LAYER_SAVE_FLAG（适用于 saveLayer()）：在保存图层前先把当前画布根据bounds裁剪,恢复时被剪裁的画布不会被恢复。
 *              与 CLIP_SAVE_FLAG 冲突，同时指定以 CLIP_SAVE_FLAG 为主。
 *******（saveLayer() 和 saveLayerAlpha() 的画布大小取值要选择适当的大小，避免出现 OOM。
 *          例： 如果一个像素需要 8bit 存储空间，那么一块 1024像素 * 768像素的机器，
 *          所占用的存储空间= 1024 * 768 * 8 = 6.2MB）
 * 总结：图层（Layer）与画布（Bitmap）和 Canvas 之间的关系
 *     图层（Layer）：每次调用 canvas.drawXXX() 系列函数，都会生成一个透明图层专门来绘制这个图形。
 *     画布（Bitmap）：每块画布都是一个 Bitmap ，所有图形都是花在这个 Bitmap 上。每次调用 canvas.drawXXX() 系列函数，
 *          生成的透明图层，在绘制完成后都会覆盖在画布上。多次调用 drawXXX() ，就会生成多个图层，绘制完成后，
 *          会依次覆盖在画布上显示。
 *          有两种画布：
 *              1、原始画布，用过 onDraw(Canvas) 函数传入的，参数 Canvas 对应的是 View 的原始画布，控件的背景都在该画布上。
 *              2、人造画布，通过 saveLayer()、new Canvas(Bitmap) 等函数人为创建的一块画布。尤其是 saveLayer()函数，
 *                  调用该函数后，会新建一块画布，之后所有的 draw 函数所画的图像都是在此画布中，
 *                  直到调用 restore()、restoreToCount()函数，才会返回至原始画布上进行绘制。
 *     Canvas（绘图工具）：画布的表现形式，所绘制的任何东西都是利用 Canvas 来实现。
 *          生成方式只有一种：new Canvas(Bitmap),无论是原始画布还是人造画布，都是通过 Canvas 绘制到 Bitmap 上。
 * 三、android 画布（canvas）
 *   1、ShapeDrawable：Drawable的派生类之一。
 *      shape 标签所对应的类时 GradientDrawable 类，而不是 ShapeDrawable。
 *      shape 标签动态获取修改：
 *          val drawable = textView.background as GradientDrawable
 *        修改背景颜色：
 *          drawable.setColor(context.resources.getColor(R.color.gray_light))
 *        修改圆角
 *          drawable.cornerRadius = 20f
 *      ShapeDrawable 的构造函数，需要与 Shape 对象进行关联,绘制的图形是最终调用 shape 的 onDraw 绘制完成的。
 *          ShapeDrawable() 需要额外调用 setShape(Shape) 设置 shape 对象
 *          ShapeDrawable(Shape)
 *      Shape 类的派生类：
 *          RectShape： 构造一个矩形 Shape
 *          ArcShape(float startAngle, float sweepAngle)： 构造一个扇形 Shape
 *              startAngle：开始角度。 sweepAngle：扇形扫过的角度
 *          OvalShape： 构造一个椭圆 Shape
 *          RoundRectShape(float[] outerRadii, RectF inset, float[] innerRadii)： 构造一个圆角矩形 Shape
 *              outerRadii：外围矩形各个角的角度大小，长度为 8，每2个数字一组，分别对应左上右下4个叫的角度，
 *                  每一组分别对应x轴和y轴半径。（不需要可传 null）
 *              inset：表示内部矩形和外部矩形的各边的边距，4个值分别对应左上右下4条边的边距。（不需要可传 null）
 *              innerRadii：内部矩形的各个角的角度大小，长度同样为 8，其含义与 outerRadii 一样。（不需要可传 null）
 *          PathShape(Path path, float stdWidth, float stdHeight)： 构造一个可根据路径绘制的 Shape
 *              path:表示所绘制路径
 *              stdWidth：标准宽度，即整个 ShapeDrawable 的宽度被分成多少份，path 中的 moveTo(x,y)\lineTo(x,y)
 *                  这些函数中的数值是根据每一份的位置进行计算的。因此每一份的值会随着 ShapeDrawable 的变大而变大。
 *              stdHeight：标准高度，即整个 ShapeDrawable 的高度被分成多少份。含义与 stdWidth 一致
 *  @see ShapeView 绘制 Shape 的实例
 *  @see ShapePathCustomView 自定义路径  PathShape
 *  @see RegionCustomShape 自定义区域  RegionShape
 *      ShapeDrawable 的常用函数：
 *      setBounds()：指定当前 ShapeDrawable 在当前控件中显示的位置
 *      getPaint()：获取 ShapeDrawable 自带的画笔对象
 *          在 Paint 中的 Shader 是从画布左上角开始绘制，而 ShapeDrawable.getPaint().setShader()
 *          是从 ShapeDrawable区域所在的左上角开始绘制。
 *  @see ShapePaintShaderView 给 ShapeDrawable 的 paint 设置 shader
 *      setAlpha(): 取值范围 0 ~ 255 设置透明值
 *      setColorFilter(ColorFilter) ：ShapeDrawable 自带的函数，也可以通过 paint 设置
 *      setIntrinsicHeight(height)：设置默认高度，当 Drawable 以 setbackGroundDrawable 及
 *           及 setImageDrawable 方式使用时，会使用默认宽高计算当前 Drawable 的大小与位置。
 *           不设置，则默认宽高等于-1
 *      setIntrinsicWidth(width)：设置默认宽度，含义与 setIntrinsicHeight 一致。
 *      setPadding(Rect): 设置边距
 *  @see ShapeTelescopeView 使用 ShapeDrawable 实现放大镜效果
 *   2、自定义 Drawable
 *  @see DrawableCustom 自定义 Drawable 的相关函数
 *  @see DrawableCustomRound 自定义 Drawable 实现圆角转换，将传入的Bitmap转换为圆角
 *      使用 setImageDrawable() 函数设置 ImageView 数据源时，自定义 Drawable 的位置和大小与 ImageView 的 scaleType有关
 *      使用 setBackgrounDrawable() 函数设置 View 背景时，自定义 Drawable 的宽高与控件大小一致，控件的宽高
 *          则选取本身宽高和自定义 Drawable 宽高中的最大值。
 *     自定义 Drawable 与 自定义 View 的区别
 *     自定义 Drawable 缺少手势交互功能，一般用在设置 Drawable 的函数中，或者替代 Bitmap 用于 View 中。
 *   3、Drawable 与 Bitmap 的对比
 *      Bitmap：位图，一般文件格式扩展名为.bmp,编码器有RGB565，RGB8888等。作为一种逐像素的显示对象，执行效率高。
 *          优点：支持像素操作、色相色差调整。绘图方便。
 *          缺点：占用内存大，绘制慢，存储效率低。Drawable 调用 Paint 方便，但调用 Canvas 并不方便。
 *      Drawable：android下通用的图像对象，常用格式GIF、PNG、JPG、BMP等，还提供了一些高级的可视化对象，例如渐变、图形等
 *          优点：占用内存小，绘制速度快。
 *          缺点：不支持像素操作、色相色差调整。
 *      即：Bitmap 是 Drawable，而 Drawable 不一定是 Bitmap。
 *      绘图便利性对比：
 *          Drawable 派生类多，很容易完成渐变、层叠等效果。
 *          Bitmap 空白画布绘图优于 Drawable
 *   4、Bitmap 位图，由一个个像素点组成
 *      1）在绘图中的使用方式有两种：
 *          A、转换为 BitmapDrawable 对象使用
 *              val bmp  = BitmapFactory.decodeResource(resources, R.mipmap.scenery)
 *              val bitmapDrawable = BitmapDrawable(bmp)
 *          B、当作画布来使用
 *             canvas.draw(Rect(),paint)：绘制在默认画布 Bitmap 中
 *             Canvas(Bitmap) ：自定义画布绘制
 *             Canvas(Bitmap.createBitmap(200,200,Bitmap.Config.ARGB_8888)):自定义空白画布
 *      2）Bitmap 格式
 *          一张位图（Bitmap）占用内存 = 图片长度（px） * 图片宽度（px） * 一个像素点占用的字节数
 *          Bitmap.Config.ALPHA_8：表示8位 Alpha 位图，即A=8，表示值存储 Alpha 位，不存储颜色值，一个像素占用1字节
 *          Bitmap.Config.ARGB_4444：表示16位 ARGB 位图，A、R、G、B各占4位，一个像素占用 2 字节
 *          Bitmap.Config.ARGB_8888： 表示32位 ARGB 位图，A、R、G、B各占8位，一个像素占用 4 字节
 *          Bitmap.Config.ARGB_565：表示16位 RGB 位图，R 占5位、G占6位、B占5位，没有透明度，一个像素占2字节。
 *          一般建议使用 ARGB_8888 格式进行存储 Bitmap ，图片更为清晰。如果对透明度没有要求则可使用 ARGB_565 格式。
 *      3）Bitmap 压缩格式
 *          Bitmap.CompressFormat.JPEG：采样 JPEG 压缩算法，是一种有损压缩格式
 *          Bitmap.CompressFormat.PNG：采样 PNG 压缩算法，是一种支持透明度的无损压缩格式
 *          Bitmap.CompressFormat.WEBP：提供了有损和无损压缩的图片格式，派生自视频编码格式VP8
 *              14<=API<=17:是有损压缩格式，且不支持透明度。
 *              API>=18:是无损格式，且支持透明度。
 *              有损压缩时，同等质量下，WEBP格式图像体积比JPEG格式体积小40%，但编码时间却会延长8倍。
 *              即牺牲时间减小产出文件大小
 *          Bitmap.CompressFormat.WEBP_LOSSY：有损耗
 *          Bitmap.CompressFormat.WEBP_LOSSLESS：无损耗
 *      4）BitmapFactory 创建方式一：
 *   @see getBitmapDecodeResource
 *          decodeResource(Resources, int id, Options opts)：
 *              Resources res：资源对象。 int id：图像数据资源ID
 *              BitmapFactory.Options opts：可以设置Bitmap 的采样率，改变宽高缩放比例完成图片像素的减少。
 *   @see getBitmapDecodeFile
 *          decodeFile(String pathName, Options opts)：
 *              String pathName：解码文件的全路径名（例：/data/data/demo.jpg）
 *   @see getBitmapDecodeByteArray
 *          decodeByteArray(byte[] data, int offset, int length, Options opts):
 *              byte[] data：压缩图像数据的字节数组
 *              int offset：图像数据便宜量，用于解码器定位从哪里开始解析
 *              int length：字节数，从偏移量开始，指定去多少字节进行解析
 *   @see getBitmapDecodeFileDescriptor
 *          decodeFileDescriptor(FileDescriptor, Rect outPadding, Options opts):
 *              FileDescriptor：解码位图数据的文件路径
 *              Rect outPadding：用于返回矩形的内边距，如果 Bitmap 解析失败，则返回（-1,-1,-1,-1）,不需要传 null
 *              使用方式：BitmapFactory.decodeFileDescriptor(FileInputStream(path).getFD())
 *              比 decodeFile 解析方式更节省内存，源码分析 decodeFile -> decodeStream(该函数会申请2次空间)
 *   @see getBitmapDecodeStream
 *          decodeStream(InputStream, Rect outPadding, Options opts)：
 *              InputStream：解码位图的原始数据输入流
 *              Rect outPadding：返回矩形的内边距，解析失败返回（-1,-1,-1,-1）,不需要传 null
 *          decodeResourceStream(Resources,TypedValue value,InputStream is,Rect pad,Options opts)：
 *      5）BitmapFactory.Options 参数
 *   @see getBitmapFactoryOption
 *          boolean inJustDecodeBounds：获取图片信息
 *              true：只解析图片信息，不获取图片，不分配内存。能获取图片宽高、MIME类型。
 *                options.outWidth(获取宽度)、options.outHeight（获取高度）、options.outMimeType(MIME类型)
 *          int inSampleSize：对图片内存进行压缩，表示图片采样率。即采样频率，指每个多少个样本采样一次作为结果。
 *              例：值为4，表示在原图中每4个像素取一个像素作为结果返回，奇遇则丢弃。（结果图片宽高均为原来的1/4）
 *              采样率越大，图片越小，同时图片越失真。官方建议取 2 的幂数（1、2、4、8、16等）
 *          int inDensity：设置文件所在资源文件夹的屏幕分辨率
 *          int inTargetDensity：表示真实显示的屏幕分辨率
 *          int inScreenDensity：暂未用的该参数
 *          boolean inScaled：从res资源文件夹取图片时，是否需要对当前文件进行缩放
 *              false：不进行缩放。
 *              true：根据文件夹分辨率和屏幕分辨率动态缩放。
 *              缩放比例 scale的 = inTargetDensity / inDensity
 *          Bitmap.Config inPreferredConfig：设置像素的存储格式
 *          int outWidth：获取图片宽度
 *          int outHeight：获取图片高度
 *          String outMimeType：MIME类型(image/png)
 *          inMutable：true 图片复用，这个属性必须设置；
 *          inBitmap：
 *      6）不同res/drawable-XXX文件夹，会根据屏幕分辨率动态缩放图片大小，
 *        对应参数如下：
 *          ldpi(1dpi = 1densityPx、1英寸 = 160densityDpi)
 *          mdpi(1dpi = 1.5densityPx、1英寸 = 240densityDpi)
 *          hdpi(1dpi = 2densityPx、1英寸 = 320densityDpi)
 *          xhdpi(1dpi = 3densityPx、1英寸 = 480densityDpi)
 *          xxhdpi(1dpi = 3.5densityPx、1英寸 = 560densityDpi)
 *          xxxhdpi(1dpi = 4densityPx、1英寸 = 640densityDpi)
 *         1英寸所对应的px数 = densityDpi * densityPx
 *      7）Bitmap 的创建方式二：Bitmap 的静态方法
 *          createBitmap(int width, int height, Config config)
 *   @see getBitmapCreate1
 *              int width, int height：指定空白尺寸。
 *              Config config：指定单个像素的存储格式，取值：ALPHA_8、ARGB_565、ARGB_4444、ARGB_8888。
 *                  默认值：ARGB_8888
 *          createBitmap(Bitmap source):根据一幅图像创建一份完全一样的 Bitmap 实例
 *          createBitmap(Bitmap source, int x, int y, int width, int height)
 *   @see getBitmapCreate2
 *              Bitmap source：用于裁剪的源图像。
 *              int x, int y：裁剪的开始点坐标
 *              int width, int height：裁剪的宽度和高度
 *          createBitmap(Bitmap source, int x, int y, int width, int height, Matrix m, boolean filter)
 *   @see getBitmapCreate3
 *              Matrix m：给裁剪后的图像添加矩阵
 *              boolean filter：对应paint.setFilterBitmap(filter) 是否给图像添加滤波效果
 *          createBitmap(int[] colors, int width, int height, Config config)
 *   @see getBitmapCreate4
 *              int[] colors:当前图像所对应的每个像素的数组，数组长度必须大于 width * height
 *              int width, int height：需要创建图像的宽度和高度
 *          createBitmap(int[] colors, int offset, int stride, int width, int height, Config config)
 *          createScaledBitmap(Bitmap src, int dstWidth, int dstHeight, boolean filter)
 *   @see getBitmapCreate5
 *          ·   Bitmap src：需要随访的源图像
 *              int dstWidth, int dstHeight：缩放后的目标宽高 (宽高和源图像一致时，返回源图像)
 *              boolean filter：对应paint.setFilterBitmap(filter) 是否给图像添加滤波效果
 *          createBitmap(DisplayMetrics display, int width, int height, Config config)：API 17添加
 *          createBitmap(DisplayMetrics display, int colors[], int width, int height,
 *                  Config config)：API 17添加
 *          createBitmap(DisplayMetrics display, int[] colors, int offset, int stride, int width,
 *                  int height, Config config)：API 17添加
 *      总结：
 *          ① 图像加载可以使用 BitmapFactory 和 Bitmap 的相关方法
 *          ② 通过配置 BitmapFactory.Option 可以实现缩放、获取图片信息、配置缩放比例等功能
 *          ③ 裁剪和缩放图片只能使用 Bitmap 的 Create 系列函数
 *          ④ 加载和创建 Bitmap时，记得 try...catch 捕获 OutOfMemoryError ，防止出现OOM问题
 *      常用函数：
 *          Bitmap copy(Config config, boolean isMutable)：根据源图像创建一个副本，可指定副本像素存储格式
 *              Config config：指定像素格式
 *              boolean isMutable：新创建的图像是否可以更改其中的像素
 *          boolean isMutable(): 判断当前 Bitmap 的像素是否可以更改，true可以，false不可以。
 *              关联函数：false时，setPixel等函数设置其中像素值就会报错
 *              通过 BitmapFactory 创建的 Bitmap 像素，不可更改。
 *          Bitmap extractAlpha()：抽取 Bitmap 中的 Alpha 值，生成一幅只有 Alpha 值的图像。（格式 ALPHA_8）
 *          Bitmap extractAlpha(Paint paint, int[] offsetXY):
 *   @see getBitmapCreate6
 *              Paint paint:具有 MaskFilter 效果的 Paint 对象，一般使用其模糊效果
 *              int[] offsetXY：添加 BlurMaskFilter 效果以后，原点的偏移量，例如使用一个半径为6的BlurMaskFilter，
 *                      那么在源图像被模糊以后，图像的上下左右4条边，会多出6px的模糊效果。因此源图像的绘制
 *                      需要从（-6,-6）点开始绘制
 *          int getAllocationByteCount(): API >= 19 时 使用该函数获取内存大小
 *          int getByteCount()：12 <= API < 19 时，使用该函数获取内存大小
 *          int getRowBytes()：获取每行所分配的内存大小。 所占内存 = getRowBytes() * bitmap.height
 *   @see getBitmapSize 不同版本获取bitmap大小
 *          recycle()：强制回收Bitmap所占的内存（API 11之前需要主动释放，之后最后手动释放不然GC时可能会卡顿）
 *          boolean isRecycle()：判断Bitmap是否被回收
 *          setDensity(int density)：设置图像建议的屏幕尺寸，只影响显示，不影响内存大小（与 BitmapFactory 不同）
 *          int getDensity()：获取 Bitmap 的 Density
 *   @see getBitmapDensity bitmap的密度变大，图片变小 （密度：Density）
 *          setPixel(int c,int y,int color):修改指定位置像素的颜色值
 *          getPixel(int c,int y)：获取指定位置像素的颜色值
 *   @see getBitmapPixel 修改图片指定位置的像素
 *          compress(CompressFormat format, int quality, OutputStream stream)：压缩图像质量
 *   @see getBitmapCompress 图片质量压缩，与 BitmapFactory.Options.inSampleSize 不同。
 *              CompressFormat format：压缩格式，JPEG、PNG、WEBP
 *              int quality：压缩后的图像画质，取值0~100,0表示最低画质，对于PNG无损格式的图片会忽略此项设置
 *              OutputStream stream：指定的输出值，压缩后会以OutputStream形式输出
 *              返回值Boolean：压缩成功返回true
 *            质量压缩，不会对内存产生影响。它是在保持像素的前提下改变图片的位深及透明度等，
 *            来达到压缩图片的目的，不会减少图片的像素。进过它压缩的图片文件大小会变小，
 *            但是解码成bitmap后占得内存是不变的。
 *       关于 Bitmap 中画布设置 抗锯齿会失效的问题（ANTI_ALIAS_FLAG 或 setAntiAlias(true)）
 *          例： bitmapCanvas.drawColor(Color.TRANSPARENT) // 清除上次绘制
 *              bitmapCanvas.drawCicler() //在onDraw中多次绘制，会导致bitmap不停的叠加，导致边缘纯色效果越来越差，需要清空后再绘制
 *              canvas.drawBitmap(Bitmap)
 *  5、SurfaceView：可查看 SurfaceViewUtlis.kt
 *  @see com.zrt.kotlinapp.activity_view.custom_basic.canvas_basic.SurfaceViewUtlis.kt
 *            
 */
fun a(context: Context){
    val canvas = Canvas()
//    canvas.save()
//    canvas.restore()
//    canvas.saveLayer(0f,0f,0f,0f, null)
    canvas.saveLayerAlpha(RectF(0f, 0f, 0f, 0f), 100, Canvas.ALL_SAVE_FLAG)
//    canvas.restoreToCount()
    Math.PI
    val image = ImageView(context)
    val drawable = image.background as GradientDrawable
    drawable.setColor(context.resources.getColor(R.color.gray_light))
    drawable.cornerRadius = 20f

//    val bmp  = BitmapFactory.decodeResource(resources, R.mipmap.scenery)
//    val bitmapDrawable = BitmapDrawable(bmp)
//    Bitmap.CompressFormat.JPEG
//    Bitmap.CompressFormat.PNG
//    Bitmap.CompressFormat.WEBP
//    Bitmap.CompressFormat.WEBP_LOSSY
//    Bitmap.CompressFormat.WEBP_LOSSLESS
//    BitmapFactory.decodeResource(resource, R.mipmap.avator, BitmapFactory.Options.)
//    BitmapFactory.decodeFile(pathName, BitmapFactory.Options.)
//    BitmapFactory.decodeByteArray()
//    BitmapFactory.decodeFileDescriptor()
//    BitmapFactory.decodeStream()
//    BitmapFactory.decodeResourceStream()
//    Bitmap.createBitmap()
//    Bitmap.createScaledBitmap()
    val bitmap = Bitmap.createBitmap(100,100,Bitmap.Config.ARGB_8888)
    bitmap.extractAlpha()
//    bitmap.compress()
}
// TODO 利用 MATRIX_SAVE_FLAG 保存矩阵
class Canvas_MATRIX_SAVE_FLAG_View: View {
    lateinit var mPaint:Paint
    // 一个参数的构造函数是在代码中创建对象的时候会被调用.
    constructor(context: Context) : this(context, null)
    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        mPaint = Paint()
        mPaint.setColor(Color.GRAY)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        // API28 Canvas.MATRIX_SAVE_FLAG 被移除
//        canvas.save(Canvas.MATRIX_SAVE_FLAG)
        canvas.rotate(45f)
        canvas.drawRect(50f, 50f, 250f, 250f, mPaint)
        canvas.restore()
        mPaint.setColor(Color.BLACK)
        canvas.drawRect(50f, 50f, 250f, 250f, mPaint)
    }
}
// TODO
class CanvasRestoreToCountView:View {
    lateinit var mPaint:Paint
    // 一个参数的构造函数是在代码中创建对象的时候会被调用.
    constructor(context: Context) : this(context, null)
    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        mPaint = Paint()
        mPaint.setColor(Color.GRAY)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val id1 = canvas.save()
        canvas.clipRect(0, 0, 600, 600)
        canvas.drawColor(Color.RED)
        val id2 = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), mPaint, Canvas.ALL_SAVE_FLAG)
        canvas.clipRect(100, 100, 500, 500)
        canvas.drawColor(Color.GREEN)
        val id3 = canvas.saveLayerAlpha(0f, 0f, width.toFloat(), height.toFloat(), 0xf0, Canvas.ALL_SAVE_FLAG)
        canvas.clipRect(200, 200, 400, 400)
        canvas.drawColor(Color.YELLOW)
        val id4 = canvas.save()
        canvas.clipRect(250, 250, 350, 350)
        canvas.drawColor(Color.BLUE)

//
        canvas.restoreToCount(id3); // 此时只是恢复至 id2 画布
        canvas.drawColor(Color.GRAY); // 将 id2 画布的背景颜色修改为灰色，id3和id4绘制的内容会被覆盖
//        Log.d(TAG,"count:"+canvas.getSaveCount());
    }
}

// TODO ShapeDrawable 实现
class ShapeView : View {
    lateinit var mShapeDrawable: ShapeDrawable
    lateinit var mShape:Shape
    // 一个参数的构造函数是在代码中创建对象的时候会被调用.
    constructor(context: Context) : this(context, null)
    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.ShapeView)
        when (typeArray.getInt(R.styleable.ShapeView_shape, 0)){
            1 -> {
                mShape = ArcShape(45f, 145f)
            }
            2 -> {
                mShape = OvalShape()
            }
            3 -> {
                mShape = RoundRectShape(floatArrayOf(10f, 10f, 5f, 20f, 0f, 0f, 10f, 10f),
                        RectF(10f, 15f, 10f, 5f),
                        floatArrayOf(20f, 20f, 5f, 20f, 0f, 0f, 9f, 0f))
            }
            4 -> {
                val path = Path()
                path.moveTo(0f, 0f)
                path.lineTo(100f, 0f)
                path.lineTo(100f, 80f)
                path.lineTo(0f, 80f)
                path.close()
                mShape = PathShape(path, 100f, 100f)
            }
            5 -> {
                val r1 = Region(50, 0, 100, 150)
                val r2 = Region(0, 50, 250, 100)
                r1.op(r2, Region.Op.XOR) // 取相交之外的区域
                mShape = RegionCustomShape(r1)
            }
            else -> { mShape = RectShape() }
        }
        typeArray.recycle()
        // 一、创建实例 ShapeDrawable， 并将该实例的形状定义为 mShape。 如果mShape=RectShape 矩形，即画出来的一定是一个矩形
        mShapeDrawable = ShapeDrawable(mShape)
        // 二、指定 ShapeDrawable 在当前控件中显示的位置，即显示在定义的矩形范围中
        mShapeDrawable.setBounds(Rect(50, 50, 200, 100))
        // 三、获取 ShapeDrawable 自带的画笔，将 ShapeDrawable 填充为蓝色
        mShapeDrawable.paint.setColor(Color.BLUE)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 四、使用 mShapeDrawable 的 draw 进行绘制
        mShapeDrawable.draw(canvas)
    }
}
// TODO 自定义 PathShape
class PathCustomShape:Shape {
    lateinit var mPath: Path
    var mStdWidth:Float = 0f
    var mStdHeight:Float = 0f
    // view 宽高和 stdWidth、stdHeight 的比例大小
    var mScaleX:Float = 0f
    var mScaleY:Float = 0f
    constructor(path: Path, stdWidth: Float, stdHeight: Float){
        this.mPath = path
        this.mStdWidth = stdWidth
        this.mStdHeight = stdHeight
    }
    override fun draw(canvas: Canvas, paint: Paint) {
        canvas.save()
        // 按比例进行缩放，还原成view正常的大小值
        canvas.scale(mScaleX, mScaleY)
        canvas.drawPath(mPath, paint)
        canvas.restore()
    }
    override fun onResize(width: Float, height: Float) {
        mScaleX = width / mStdWidth
        mScaleY = height / mStdHeight
    }
}
// TODO 自定义一个构造区域的 Shape
class RegionCustomShape:Shape {
    lateinit var mRegion: Region
    constructor(region: Region) {
        assert(region != null)
        this.mRegion = region
    }
    override fun draw(canvas: Canvas, paint: Paint) {
        val iterator = RegionIterator(mRegion)
        val rect = Rect()
        while (iterator.next(rect)) {
            canvas.drawRect(rect, paint)
        }
    }
}
// TODO 给 ShapeDrawable 的 paint 设置 shader
class ShapePaintShaderView : View {
    lateinit var mShapeDrawable: ShapeDrawable
    // 一个参数的构造函数是在代码中创建对象的时候会被调用.
    constructor(context: Context) : this(context, null)
    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        // 一、创建实例 ShapeDrawable， 并将该实例的形状定义为 mShape。 如果mShape=RectShape 矩形，即画出来的一定是一个矩形
        mShapeDrawable = ShapeDrawable(RectShape())
        // 二、指定 ShapeDrawable 在当前控件中显示的位置，即显示在定义的矩形范围中
        mShapeDrawable.setBounds(Rect(50, 50, 350, 350))
        // 三、获取 ShapeDrawable 自带的画笔，将 ShapeDrawable 填充为蓝色
        mShapeDrawable.paint.setColor(Color.BLUE)
        // 给 mShapeDrawable.paint 设置印章
        val btm = BitmapFactory.decodeResource(resources, R.mipmap.dog)
        val btmShader = BitmapShader(btm, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        mShapeDrawable.paint.setShader(btmShader)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 四、使用 mShapeDrawable 的 draw 进行绘制
        mShapeDrawable.draw(canvas)
    }
}
// TODO 使用 ShapeDrawable 实现放大镜效果
class ShapeTelescopeView: View {
    lateinit var mShapeDrawable: ShapeDrawable
    var bitmap:Bitmap? = null
    // 放大镜半径
    var RADIUS:Int = 100 * 1
    // 放大镜倍数
    var FACTOR:Int = 3
    // 一个参数的构造函数是在代码中创建对象的时候会被调用.
    constructor(context: Context) : this(context, null)
    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        mShapeDrawable = ShapeDrawable(OvalShape())
//        mShapeDrawable = ShapeDrawable(RectShape())
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        Log.i(">>>>", "##x=" + x + ";y=" + y);
        val matrix = Matrix()
        // 当前的 (x,y) 是原图中的位置，而手指点击位置 (x,y) 是 ShapeDrawable 的左上角原点
        // Shader 会从 (x,y) 位置开始绘制，因此当前手指点击的 (x,y) 坐标
        // 对应在 ShapeDrawable 图中的 (x + FACTOR * x, y + FACTOR * y) 坐标
        // 因此需要将 BitmapShader 想左上角平移 (-FACTOR * x, -FACTOR * y) 位置
//         将 BitmapShader 向左上角移动一段距离，使 BitmapShader 中原来的（3x,3y）一直在 ShapeDrawable 区域中
        matrix.setTranslate(-x * FACTOR + RADIUS, -y * FACTOR + RADIUS)
        mShapeDrawable.paint.shader?.setLocalMatrix(matrix)
        mShapeDrawable.setBounds(x.toInt() - RADIUS, y.toInt() - RADIUS, x.toInt() + RADIUS, y.toInt() + RADIUS)
//        mShapeDrawable.setBounds(x.toInt(), y.toInt(), x.toInt() + RADIUS, y.toInt() + RADIUS)
        postInvalidate()
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (bitmap == null) {
            val bmp  = BitmapFactory.decodeResource(resources, R.mipmap.scenery)
            bitmap = Bitmap.createScaledBitmap(bmp, width, height, false)
            bitmap?.let{
                val shader = BitmapShader(Bitmap.createScaledBitmap(it, it.width * FACTOR, it.height * FACTOR, false), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
                mShapeDrawable.paint.setShader(shader)
                mShapeDrawable.setBounds(0, 0, 2 * RADIUS, 2 * RADIUS)
            }
        }
        bitmap?.let {
            canvas.drawBitmap(it, 0f, 0f, null)
            mShapeDrawable.draw(canvas)
        }
    }
}
// TODO 自定义 Drawable 相关函数
class DrawableCustom: Drawable() {
    // 通过 Canvas 绘制图像到 Drawable 中
    override fun draw(canvas: Canvas) {}
    // 将对应参数传给 Paint 完成设置即可
    override fun setAlpha(alpha: Int) {}
    override fun setColorFilter(colorFilter: ColorFilter?) {}
    //
    override fun getOpacity(): Int {
        // 表示未知
        return PixelFormat.UNKNOWN
        // 表示当前 Drawable 的绘制具有 Aplha 通道，即使用 Drawable后，其底部图像仍有可能看见。
        return PixelFormat.TRANSLUCENT // 一般可默认返回该项
        // 表示当前的 Drawable 完全透明，使用 Drawable 将完全显示底部图像
        return PixelFormat.TRANSPARENT
        // 表示当前图像完全不透明，，使用 Drawable 将完全覆盖底部图像
        return PixelFormat.OPAQUE
    }
}
// TODO 自定义 Drawable 实现圆角转换，将传入的Bitmap转换为圆角
class DrawableCustomRound: Drawable {
    lateinit var mPaint: Paint
    lateinit var mBitmap: Bitmap
    lateinit var mBound: RectF
    lateinit var mBitmapShader: BitmapShader
    constructor(bitmap: Bitmap) {
        mPaint = Paint()
        mBitmap = bitmap
        // 开起抗锯齿
        mPaint.isAntiAlias = true
    }
    // 设置 DrawableCustomRound 的边界大小
    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        super.setBounds(left, top, right, bottom)
        // 将 Bitmap 缩放至当前指定矩形的大小
        mBitmapShader = BitmapShader(Bitmap.createScaledBitmap(mBitmap, (right - left), (bottom - top), true), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        mPaint.setShader(mBitmapShader)
        mBound = RectF(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())
    }
    // 设置 DrawableCustomRound 的默认内部宽度,设置 ScaleType 会根据该宽带进行拉伸
    override fun getIntrinsicWidth(): Int {
        return mBitmap.width
    }
    // 设置 DrawableCustomRound 的默认内部高度
    override fun getIntrinsicHeight(): Int {
        return mBitmap.height
    }
    override fun draw(canvas: Canvas) {
        canvas.drawRoundRect(mBound, 20f, 20f, mPaint)
    }
    override fun setAlpha(alpha: Int) {
        mPaint.alpha = alpha
    }
    override fun setColorFilter(colorFilter: ColorFilter?) {
        mPaint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int {
        // 表示当前透明度由传入的 Bitmap 决定，如果 Bitmap 具有透明度，则 Drawable 也可能具有透明度
        return PixelFormat.TRANSLUCENT
    }
}
// TODO 使用 BitmapFactory.decodeResource 获取图片
fun getBitmapDecodeResource(resource: Resources, @IdRes imageID: Int,
        option:BitmapFactory.Options):Bitmap? {
//    val option = BitmapFactory.Options()
//    // true:只获取图片信息
//    option.inJustDecodeBounds = inJustDecodeBounds
    val bitmap = BitmapFactory.decodeResource(resource, imageID, option)
    LogUtil.i(">>>>","##Bitmap=${bitmap?:"null"}")
    LogUtil.i(">>>>","##outWidth=${option.outWidth};outHeight=${option.outHeight};outMimeType=${option.outMimeType}")
    return bitmap
}
// TODO 使用 BitmapFactory.decodeFile 从SD中获取图片
fun getBitmapDecodeFile(paths:String?, option:BitmapFactory.Options):Bitmap? {
    val path = paths?:"${Environment.getExternalStorageDirectory().absolutePath}${File.separator}kotlinapp${File.separator}scenery.png"
//    val option = BitmapFactory.Options()
    val bitmap = BitmapFactory.decodeFile(path, option) //"/data/data/demo.jpg"
    return bitmap
}
val path_scenery = "${Environment.getExternalStorageDirectory().absolutePath}${File.separator}kotlinapp${File.separator}scenery.png"
// TODO 使用 BitmapFactory.decodeByteArray 根据压缩图像数据的字节数组获取图片
fun getBitmapDecodeByteArray(option:BitmapFactory.Options):Bitmap? {
    val urlPath = "http://img.my.csdn.net/uploads/201609/24/1474722458_2880.png"
    // 1、需要在异步线程中调用
    // 2、把网络返回的 InputStream 转换成 byte[]
    // 3、然后使用 decodeByteArray 进行解析
    val data:ByteArray? = getImageData(urlPath)
//    val option = BitmapFactory.Options()
    val bitmap = BitmapFactory.decodeByteArray(data, 0, data?.size ?: 0, option)
    return bitmap
}
// 1、网络请求获取图片
fun getImageData(urlPath: String): ByteArray? {
    val url = URL(urlPath)
    val connection:HttpURLConnection = url.openConnection() as HttpURLConnection;
    connection.requestMethod = "GET"
    connection.readTimeout = 10 * 1000
    var inputStream: InputStream? = null
    if (connection.responseCode == 200) {
        inputStream = connection.inputStream
        val result:ByteArray = readStream(inputStream)
        inputStream?.close()
        return result
    }
    return null
}
// 2、转换 InputStream 为 ByteArray
@Throws(Exception::class)
fun readStream(inputStream: InputStream?): ByteArray {
    val outputStream = ByteArrayOutputStream()
    val buffer:ByteArray = ByteArray(1024)
    var len = -1
    while (inputStream?.read(buffer)?.also { len = it } != -1) {
        outputStream.write(buffer, 0, len)
    }
    outputStream.close()
    inputStream?.close()
    return outputStream.toByteArray()
}
// TODO 使用 BitmapFactory.decodeFileDescriptor 获取图片
fun getBitmapDecodeFileDescriptor(option:BitmapFactory.Options):Bitmap? {
    // decodeFileDescriptor 相比于 decodeFile 更节省内存，可参考源码
    // decodeFileDescriptor: 调用 native 层解析图片
    // decodeFile：最终调用 decodeStream 解析bitmap，decodeStream会开辟2次空间后再调用 native 层。所以容易oom
    // 1、创建包含解码位图数据的文件路径
    val fis = FileInputStream(path_scenery)
    // 2、创建内边距 矩形，解析成功会返回内边距
    val outPadding = Rect()
//    val option = BitmapFactory.Options()
    // 获取图片，不存在则返回 null
    val bitmap = BitmapFactory.decodeFileDescriptor(fis?.fd, outPadding, option)
    return bitmap
}

// TODO 使用 BitmapFactory.decodeStream 获取图片
fun getBitmapDecodeStream(option:BitmapFactory.Options):Bitmap? {
    val urlPath = "http://img.my.csdn.net/uploads/201609/24/1474722458_2880.png"
    // 需要异步请求获取位图原始数据流
    val `in`:InputStream? = getImage(urlPath)
    // 2、创建内边距 矩形，解析成功会返回内边距
    val outPadding = Rect()
//    val option = BitmapFactory.Options()
    `in`?.let {
        // 获取图片，不存在则返回 null
        val bitmap = BitmapFactory.decodeStream(it, outPadding, option)
        return bitmap
    }
    return null
}

fun getImage(urlPath: String): InputStream? {
    val url = URL(urlPath)
    val connection:HttpURLConnection = url.openConnection() as HttpURLConnection;
    connection.requestMethod = "GET"
    connection.readTimeout = 10 * 1000
    var inputStream: InputStream? = null
    if (connection.responseCode == 200) {
        return connection.inputStream
    }
    return null
}

/**
 * BitmapFactory.Options.inJustDecodeBounds： true设置值获取图片信息
 * BitmapFactory.Options.inSampleSize： 获取采样率
 */
fun getBitmapFactoryOption(resource: Resources, @IdRes imageID: Int, path: String, view: ImageView, view2: ImageView) {
    // inJustDecodeBounds = true 只获取图片信息
    val op1 = BitmapFactory.Options()
    op1.inJustDecodeBounds = true
    val btm1 = getBitmapDecodeResource(resource, imageID, op1)
    // bitmap = null 只会获取图像信息
    Log.i(">>>>", "## Options.inJustDecodeBounds=${op1.inJustDecodeBounds}; bitmap=${btm1?:"null"}")
    // Options.inJustDecodeBounds=true; outWidth==640; outHeight=800; outMimeTypes=image/jpeg
    Log.i(">>>>", "## Options.inJustDecodeBounds=${op1.inJustDecodeBounds}; outWidth==${op1.outWidth}" +
            "; outHeight=${op1.outHeight}; outMimeTypes=${op1.outMimeType}")
    // 获取压缩后的图片
    val op2 = BitmapFactory.Options()
    // ## dstWidth=338; dstHeight=591
    op2.inSampleSize = calSampleSize(op1, view.width, view.height)
    val btm2 = getBitmapDecodeResource(resource, imageID, op2)
    // ## inSampleSize=1 压缩后的图片：width=540; height=675; 内存=1458000
    Log.i(">>>>", "## inSampleSize=${op2.inSampleSize} 压缩后的图片：width=${btm2?.width?:""}; height=${btm2?.height?:""}; 内存=${btm2?.byteCount?:0}")
    view.post(Runnable {
        view.setImageBitmap(btm2)
    })
    // Bitmap 从 Drawable/mipmap 或 SD卡中取出：
    val op3 = BitmapFactory.Options()
    val btm3 = getBitmapDecodeResource(resource, imageID, op3)
    // 1、##从 mipmap-xhdpi 中取出会存在图片缩放 width=540; height=675; 内存=1458000
    Log.i(">>>>", "##从 mipmap-xhdpi 中取出：width=${btm3?.width?:""}; height=${btm3?.height?:""}; 内存=${btm3?.byteCount?:0}")
    // 2、##从 SD 卡中取出不会存在图片缩放 width=640; height=800; 内存=2048000
    val op4 = BitmapFactory.Options()
    val btm4 = getBitmapDecodeFile("${Environment.getExternalStorageDirectory().absolutePath}${File.separator}kotlinapp${File.separator}scenery.png", op4)
    Log.i(">>>>", "##从 SD 卡中取出：width=${btm4?.width?:""}; height=${btm4?.height?:""}; 内存=${btm4?.byteCount?:0}")

    // 设置从 Drawable/mipmap 中取文件不进行缩放：width=640; height=800; 内存=2048000
    val op5 = BitmapFactory.Options()
    op5.inScaled = false
    val btm5 = getBitmapDecodeResource(resource, imageID, op5)
    Log.i(">>>>", "##从 Drawable/mipmap 中取文件不进行缩放：width=${btm5?.width?:""}; height=${btm5?.height?:""}; 内存=${btm5?.byteCount?:0}")

    // 手动设置 inDensity 和 inTargetDensity 对图片进行放大和缩小
    // 缩放比例 scale = inTargetDensity / inDensity
    // 1、从 Drawable/mipmap 中取文件放大2倍：width=1280; height=1600; 内存=8192000
    val op6 = BitmapFactory.Options()
    op6.inDensity = 1
    op6.inTargetDensity = 2
    val btm6 = getBitmapDecodeResource(resource, imageID, op6)
    Log.i(">>>>", "##从 Drawable/mipmap 中取文件放大2倍：width=${btm6?.width?:""}; height=${btm6?.height?:""}; 内存=${btm6?.byteCount?:0}")

    // 2、##从 SD 卡中取文件并放大2倍：width=1280; height=1600; 内存=8192000
    val op7 = BitmapFactory.Options()
    op7.inDensity = 1
    op7.inTargetDensity = 2
    val btm7 = getBitmapDecodeFile("${Environment.getExternalStorageDirectory().absolutePath}${File.separator}kotlinapp${File.separator}scenery.png", op7)
    Log.i(">>>>", "##从 SD 卡中取文件放大2倍：width=${btm7?.width?:""}; height=${btm7?.height?:""}; 内存=${btm7?.byteCount?:0}")

    // inPreferredConfig 修改图像格式
    val op8 = BitmapFactory.Options()
    op8.inPreferredConfig = Bitmap.Config.RGB_565
    val btm8 = getBitmapDecodeResource(resource, imageID, op8)
    view2.post(Runnable {
        view2.setImageBitmap(btm8)
    })
    Log.i(">>>>", "")
    Log.i(">>>>", "")
    Log.i(">>>>", "")
}

/**
 * 获取当前图片的采样率, 取宽高比值的最小值，即采样率。
 * @param option 包含 outWidth 和 outHeight 图片信息
 * @param dstWidth ImageView 图片宽度
 * @param dstHeight ImageView 图片高度
 * @return 缩放比例
 */
fun calSampleSize(option: BitmapFactory.Options, dstWidth:Int, dstHeight:Int):Int {
    val rawWidth = option.outWidth
    val rawHeight = option.outHeight
    var inSampleSize = 1
    Log.i(">>>>", "## dstWidth=${dstWidth}; dstHeight=${dstHeight}")
    if (rawWidth > dstWidth || rawHeight > dstHeight) {
        val ratioHeight = rawHeight.toFloat() / dstHeight.toFloat()
        val ratioWidth = rawWidth.toFloat() / dstHeight.toFloat()
        inSampleSize = Math.min(ratioWidth, ratioHeight).toInt()
    }
    return inSampleSize
}
// TODO 获取一个空白图片
fun getBitmapCreate1():Bitmap{
    return Bitmap.createBitmap(500, 300, Bitmap.Config.ARGB_8888)
}
// TODO 裁剪图像
fun getBitmapCreate2(bitmap:Bitmap):Bitmap{
    return Bitmap.createBitmap(bitmap, bitmap.width/3, bitmap.height/3, bitmap.width/3, bitmap.height/3)
}
// TODO 给裁剪图像添加矩阵，进行放大
fun getBitmapCreate3(bitmap:Bitmap):Bitmap {
    val matrix = Matrix()
    matrix.setScale(2f, 2f)
    return Bitmap.createBitmap(bitmap, bitmap.width/3, bitmap.height/3, bitmap.width/3, bitmap.height/3, matrix, true)
}
// TODO 根据像素数组生成图像。
fun getBitmapCreate4():Bitmap {
    val width = 300
    val height = 300
    val colors = initColors(width,height)
    return Bitmap.createBitmap(colors, width, height, Bitmap.Config.ARGB_8888)
}
// 指定图片宽高随机生成一个数组，保存图像中的像素值
fun initColors(width:Int, height:Int):IntArray{
    val colors = IntArray(width * height)
    for (y in 0 until height) {
        for (x in 0 until width) {
            val r = x * 255 / (width - 1)
            val g = y * 255 / (width - 1)
            val b = 255 - Math.min(r, g)
            val a = Math.max(r, g)
            colors[y * width + x] = Color.argb(a, r, g, b)
        }
    }
    return colors
}

// TODO 对指定图像进行缩放
fun getBitmapCreate5(bitmap:Bitmap):Bitmap? {
    try {
        return Bitmap.createScaledBitmap(bitmap, 300,200,true)
    }catch (ex:OutOfMemoryError) {
        return null
    }
}
// TODO 根据 extractAlpha 获取到的淡化图像绘制阴影图
fun getBitmapCreate6(resource: Resources):Bitmap? {
    val srcBitmap = BitmapFactory.decodeResource(resource, R.mipmap.cat_dog)
    // 1、获取 Alpha Bitmap
    val alphaPaint = Paint()
    alphaPaint.setMaskFilter(BlurMaskFilter(10f,BlurMaskFilter.Blur.NORMAL))
    val offsetXY = IntArray(2) // (-9,-9)
    val alphaBitmap = srcBitmap.extractAlpha(alphaPaint, offsetXY)
    // 2、创建空白bitmap
    val bitmap = Bitmap.createBitmap(alphaBitmap.width, alphaBitmap.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val paint = Paint()
    paint.setColor(Color.CYAN) // 青色阴影
//    paint.setColor(Color.LTGRAY) // 灰色阴影
    // 3、绘制 Alpha Bitmap 到空白图片上
    canvas.drawBitmap(alphaBitmap, 0f, 0f, paint)
    // 4、绘制 源图像到空白图片上
//    canvas.drawBitmap(srcBitmap, 0f, 0f, paint)
    // 图片处于阴影正中
    canvas.drawBitmap(srcBitmap, -offsetXY[0].toFloat(), -offsetXY[1].toFloat(), paint)
    srcBitmap.recycle()
    return bitmap
}
// TODO 添加点击阴影效果
class CanvasStrokeImage: androidx.appcompat.widget.AppCompatImageView {
    // 一个参数的构造函数是在代码中创建对象的时候会被调用.
    constructor(context: Context) : this(context, null)
    // 两个参数的构造函数会在布局文件中使用这个View的时候被调用
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    // 三个参数的构造函数会在布局文件中使用 style 的情况下会被调用
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    /**
     * 调用时机在系统将XML解析出对应的控件实例的时候。此时控件已生成，但还未被使用，可以对控件进行一些基本设置
     *
     */
    override fun onFinishInflate() {
        super.onFinishInflate()
        val paint = Paint()
        paint.setColor(Color.CYAN)
        setStateDrawable(this, paint)
    }

    fun setStateDrawable(image: ImageView, paint: Paint) {
        // 1、获取源图像
        val drawable = image.drawable as BitmapDrawable
        val srcbitmap = drawable.bitmap
        // 2、从源图像中获取阴影图
        val dstBitmap = Bitmap.createBitmap(srcbitmap.width, srcbitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(dstBitmap)
        canvas.drawBitmap(srcbitmap.extractAlpha(), 0f, 0f, paint)
        // 3、添加状态 即 selector 标签 对应类 StateListDrawable
        val sld = StateListDrawable()
        sld.addState(intArrayOf(android.R.attr.state_pressed), BitmapDrawable(dstBitmap))
        val pr = Rect(image.paddingLeft, image.paddingTop, image.paddingRight,image.paddingBottom)
        // setBackgroundDrawable 会导致 padding失效，需要重新设置
        image.setBackgroundDrawable(sld)
//        image.setPadding(pr.left, pr.top,pr.right,pr.bottom)
    }
}
// TODO 不同版本获取bitmap大小
fun getBitmapSize(bitmap: Bitmap): Int {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        return bitmap.allocationByteCount
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
        return bitmap.byteCount
    }
    return bitmap.rowBytes * bitmap.height
}
// TODO setDensity / getDensity bitmap密度变大，图片变小
fun getBitmapDensity(resource: Resources, iv1:ImageView, iv2:ImageView){
    val bitmap = BitmapFactory.decodeResource(resource,R.mipmap.cat_dog)
    iv1.setImageBitmap(bitmap)
    // ##bitmap 1 density=270; width=608; height=402
    Log.i(">>>>", "##bitmap 1 density=${bitmap.density}; width=${bitmap.width}; height=${bitmap.height}")
    // density 放大2倍, 即同一分辨率下 占用的 density 更大，因此图片被缩小至对应的分辨率
    val scaleDensity = bitmap.density * 2
    bitmap.density = scaleDensity
    // ##bitmap 2 density=540; width=608; height=402
    Log.i(">>>>", "##bitmap 2 density=${bitmap.density}; width=${bitmap.width}; height=${bitmap.height}")
    iv2.setImageBitmap(bitmap)
}
// TODO 修改图片指定位置的像素(此处将像素的绿色值上调30)
fun getBitmapPixel(resource: Resources, iv1:ImageView, iv2:ImageView) {
    val srcBitmap = BitmapFactory.decodeResource(resource, R.mipmap.dog)
    iv1.setImageBitmap(srcBitmap)
    // 复制原图片,支持修改像素 true
    val dstBitmap = srcBitmap.copy(Bitmap.Config.ARGB_8888, true)
    for (y in 0 until srcBitmap.height) {
        for (x in 0 until srcBitmap.width) {
            val color = srcBitmap.getPixel(x, y)
            val alpha = Color.alpha(color)
            val red = Color.red(color)
            var green = Color.green(color)
            val blue = Color.blue(color)
            // 如果绿色值小于200，则上调30
            if (green < 200) {
                green += 30
            }
            dstBitmap.setPixel(x, y, Color.argb(alpha, red, green, blue))
        }
    }
    iv2.setImageBitmap(dstBitmap)
}
// TODO 图片不同格式压缩
fun getBitmapCompress(resource: Resources, iv1:ImageView, iv2:ImageView, iv3:ImageView, iv4:ImageView){
    val srcBitmap = BitmapFactory.decodeResource(resource, R.mipmap.cat)
    iv1.setImageBitmap(srcBitmap)
    // ##Bitmap compress srcBitmap width=316; height=403; BitmapSize=509392
    Log.i(">>>>", "##Bitmap compress srcBitmap width=${srcBitmap.width}; height=${srcBitmap.height}; BitmapSize=${getBitmapSize(srcBitmap)}")
    // 压缩成 JPEG 格式，会有所损耗
    val byteOS = ByteArrayOutputStream()
    srcBitmap.compress(Bitmap.CompressFormat.JPEG, 1, byteOS)
    val jpegByte = byteOS.toByteArray()
    val jpegBitmap = BitmapFactory.decodeByteArray(jpegByte,0, jpegByte.size)
    iv2.setImageBitmap(jpegBitmap)
    // ##Bitmap compress jpegBitmap width=316; height=403; BitmapSize=509392
    Log.i(">>>>", "##Bitmap compress jpegBitmap width=${jpegBitmap.width}; height=${jpegBitmap.height}; BitmapSize=${getBitmapSize(jpegBitmap)}")
    byteOS.reset()
    // 压缩成 PNG 格式
    srcBitmap.compress(Bitmap.CompressFormat.PNG, 1, byteOS)
    val pngByte = byteOS.toByteArray()
    val pngBitmap = BitmapFactory.decodeByteArray(pngByte,0, pngByte.size)
    iv3.setImageBitmap(pngBitmap)
    // ##Bitmap compress pngBitmap width=316; height=403; BitmapSize=509392
    Log.i(">>>>", "##Bitmap compress pngBitmap width=${pngBitmap.width}; height=${pngBitmap.height}; BitmapSize=${getBitmapSize(pngBitmap)}")
    byteOS.reset()
    srcBitmap.compress(Bitmap.CompressFormat.WEBP, 1, byteOS)
    val webpByte = byteOS.toByteArray()
    val webpBitmap = BitmapFactory.decodeByteArray(webpByte,0, webpByte.size)
    iv4.setImageBitmap(webpBitmap)
    // ##Bitmap compress webpBitmap width=316; height=403; BitmapSize=509392
    Log.i(">>>>", "##Bitmap compress webpBitmap width=${webpBitmap.width}; height=${webpBitmap.height}; BitmapSize=${getBitmapSize(webpBitmap)}")
}
