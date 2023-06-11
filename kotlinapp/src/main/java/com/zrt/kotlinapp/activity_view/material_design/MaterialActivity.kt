package com.zrt.kotlinapp.activity_view.material_design

import android.os.Bundle
import com.zrt.kotlinapp.BasicActivity
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_material.*
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * TextInputLayout属性：
 * 1、setCounterEnabled(boolean enabled)
 * 在此布局中是否启用了字符计数器功能。
 * 2、setCounterMaxLength(int maxLength)
设置要在字符计数器上显示的最大长度。
 * 3、setBoxBackgroundColorResource(int boxBackgroundColorId)
设置用于填充框的背景色的资源。
 * 4、setBoxStrokeColor(int boxStrokeColor)
设置轮廓框的笔触颜色。
 * 5、setCounterOverflowTextAppearance(int counterOverflowTextAppearance)
使用指定的 TextAppearance 资源设置溢出字符计数器的文本颜色和大小。
 * 6、setCounterOverflowTextColor(ColorStateList counterOverflowTextColor)
使用 ColorStateList 设置溢出字符计数器的文本颜色。(此文本颜色优先于 counterOverflowTextAppearance 中设置的文本颜色)
 * 7、setCounterTextAppearance(int counterTextAppearance)
使用指定的 TextAppearance 资源设置字符计数器的文本颜色和大小。
 * 8、setCounterTextColor(ColorStateList counterTextColor)
使用 ColorStateList 设置字符计数器的文本颜色。(此文本颜色优先于 counterTextAppearance 中设置的文本颜色)
 * 9、setErrorEnabled(boolean enabled)
在此布局中是否启用了错误功能。
 * 10、setErrorTextAppearance(int errorTextAppearance)
设置来自指定 TextAppearance 资源的错误消息的文本颜色和大小。
 * 11、setErrorTextColor(ColorStateList errorTextColor)
设置错误消息在所有状态下使用的文本颜色。
 * 12、setHelperText(CharSequence helperText)
设置将在下方显示的帮助消息 EditText。
 * 13、setHelperTextColor(ColorStateList helperTextColor)
设置辅助状态在所有状态下使用的文本颜色。
 * 14、setHelperTextEnabled(boolean enabled)
在此布局中是否启用了辅助文本功能。
 * 15、setHelperTextTextAppearance(int helperTextTextAppearance)
设置指定 TextAppearance 资源中的辅助文本的文本颜色和大小。
 * 16、setHint(CharSequence hint)
设置要在浮动标签中显示的提示（如果启用）。
 * 17、setHintAnimationEnabled(boolean enabled)
是否获取焦点的时候，hint 文本上移到左上角开启动画。
 * 18、setHintEnabled(boolean enabled)
设置是否在此布局中启用浮动标签功能。
 * 19、setHintTextAppearance(int resId)
从指定的 TextAppearance 资源设置折叠的提示文本的颜色，大小，样式。
 * 20、setHintTextColor(ColorStateList hintTextColor)
从指定的 ColorStateList 资源设置折叠的提示文本颜色。
 * 21、setPasswordVisibilityToggleEnabled(boolean enabled)
启用或禁用密码可见性切换功能
https://blog.51cto.com/u_15060515/2639943
 * 例：android:theme="@style/TextInputLayoutTheme"
 *    app:errorTextAppearance="@style/TextInputLayoutThemeError"
 */
class MaterialActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutResID(): Int = R.layout.activity_material

    override fun initData() {
        a_m_show_snackbar.setOnClickListener {
            view ->
            view.showSnackbar("标题", actionText = "点击事件"){
                ToastUtils.show("OK")
            }
        }
        a_m_login.setOnClickListener {
            val username = et_username.text.toString()
            val password = et_password.text.toString()
            if (!validateUserName(username)){
                et_username_ll.isErrorEnabled = true
                et_username_ll.error = "请输入正确的邮箱地址"
            }else if (!validatePassword(password)){
                et_password_ll.isErrorEnabled = true
                et_password_ll.error = "密码字数过少"
            }else{
                et_username_ll.isErrorEnabled = false
                et_password_ll.isErrorEnabled = false
                ToastUtils.show("登录成功")
            }
        }
    }

    private fun validatePassword(password: String): Boolean {
        return password.length > 6
    }
    private val EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$"
    private val pattern: Pattern = Pattern.compile(EMAIL_PATTERN)
    private var matcher: Matcher? = null
    private fun validateUserName(username: String): Boolean {
        matcher = pattern.matcher(username)
        return matcher?.matches() ?: false
    }
}