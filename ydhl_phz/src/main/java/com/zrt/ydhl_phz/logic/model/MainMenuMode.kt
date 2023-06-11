package com.zrt.ydhl_phz.logic.model

/**
 * @author：Zrt
 * @date: 2022/11/13
 */
class MainMenuMode {
    /** 主界面分组名称 */
    var group_name = ""
    var is_group = true
    /** 主界面分组菜单是否展开，true展开 false收起，默认false收起 */
    var is_open = false
    var menuList:MutableList<MenuItem> = ArrayList<MenuItem>()
}
class MenuItem{
    var mokuai_name = ""
    var mokuai_icon = ""
    var is_html5 = "0"
    var html5_url = ""
    var is_group = false
}