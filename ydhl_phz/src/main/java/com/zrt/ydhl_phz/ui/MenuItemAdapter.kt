package com.zrt.ydhl_phz.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zrt.ydhl_phz.R
import com.zrt.ydhl_phz.logic.model.MainMenuMode
import com.zrt.ydhl_phz.logic.model.MenuItem
import com.zrt.ydhl_phz.tools.DensityUtils
import java.lang.reflect.Field
import java.util.*

/**
 * @author：Zrt
 * @date: 2022/11/14
 */
class MenuItemAdapter(val context: Context, val menuMode: MainMenuMode): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        // 0 菜单栏， 1标题
        return if (menuMode.menuList[position].is_group) 0 else 1
    }
    override fun getItemCount(): Int {
        if (menuMode.menuList.size > 9){
            if (menuMode.is_open){
                return menuMode.menuList.size
            }
            return 9
        }
        return menuMode.menuList.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder:RecyclerView.ViewHolder
        if (viewType == 0){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_list_item, parent, false)
            viewHolder = GroupViewHolder(view)
        }else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.main_mokuai_item_new, parent, false)
            viewHolder = ChildViewHolder(view)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val type = holder.itemViewType
        val menuItem: MenuItem = menuMode.menuList.get(position)
        when (type) {
            0 -> {
                val groupHolder = holder as GroupViewHolder
                if (menuMode.menuList.size > 9) {
                    if (menuMode.is_open) {
//                        if (this.current_application.use_mobile_version) {
//                            groupHolder.ic_more.setImageResource(R.drawable.arrow_up)
//                        } else {
                        groupHolder.show_more.text = "收起"
//                        }
                    } else {
//                        if (this.current_application.use_mobile_version) {
//                            groupHolder.ic_more.setImageResource(R.drawable.arrow_down)
//                        } else {
                        groupHolder.show_more.text = "更多"
//                        }
                    }
//                    if (this.current_application.use_mobile_version) {
//                        groupHolder.ic_more.visibility = View.VISIBLE
//                        groupHolder.ic_more.setOnClickListener {
//                            map["is_open"] = !Boolean.valueOf(map["is_open"]).toString()
//                            notifyDataSetChanged()
//                        }
//                    } else {
                    groupHolder.show_more.visibility = View.VISIBLE
                    groupHolder.show_more.setOnClickListener {
                        menuMode.is_open = !menuMode.is_open
                        notifyDataSetChanged()
                    }
//                    }
                } else {
                    groupHolder.show_more.visibility = View.GONE
                    groupHolder.ic_more.visibility = View.GONE
                }
                groupHolder.group_name.text = menuMode.group_name
            }
            1 -> {
                val childHolder = holder as ChildViewHolder
                val icon = menuItem.mokuai_icon
                val iconName = menuItem.mokuai_name
                val is_html = menuItem.is_html5
                val html_url = menuItem.html5_url
//                if ("演示版" == Constants.YIYUAN_NAME) {
//                    // 20210630 演示版本，菜单图标背景颜色分类，如下使用：aly_ys_zyb_ (阿里云-演示-专业版)
//                    // 专业版：移动用血，输血巡视、标本复核，标本递交，标本退回，废弃物管理，出入登记，患者流转
//                    if ("ysydyx" == icon || "new_ydyx" == icon || "sxxs" == icon || "bbfh" == icon || "dlbbdj" == icon || "dlbbth" == icon || "fqwgl" == icon || "hzcrdj" == icon || "hzlz" == icon) {
//                        childHolder.tubiao.setBackgroundResource(Integer.valueOf(getResourceByReflect("aly_ys_zyb_$icon")))
//                    } else if ("phz" == icon || "phz_new" == icon || "hlcs" == icon || "jsymm" == icon || "jrhlgz" == icon || "hlhcd" == icon) {
//                        // 增强版：陪护管理、护理措施、基数药管理、今日工作任务、护理质控核查单
//                        childHolder.tubiao.setBackgroundResource(Integer.valueOf(getResourceByReflect("aly_ys_zjb_$icon")))
//                    } else {
//                        childHolder.tubiao.setBackgroundResource(Integer.valueOf(getResourceByReflect(icon)))
//                    }
//                } else {
                childHolder.tubiao.setBackgroundResource(Integer.valueOf(getResourceByReflect(icon)))
//                }
                if (iconName.length > 4) {
                    childHolder.name.textSize = 11f
                    childHolder.name.setPadding(0, DensityUtils.dip2px(context, 2f), 0, DensityUtils.dip2px(context, 2f))
                }
                childHolder.name.text = iconName
                childHolder.view.setOnClickListener {
                    (context as MainActivity).jumpToMokuai(menuItem)
                }
//                if (!current_application.use_mobile_version) {
                childHolder.ll_content.setBackgroundResource(R.drawable.bg_griditem)
//                }
            }
            else -> {
            }
        }
    }

    /**
     * 获取图片名称获取图片的资源id的方法
     * @param imageName
     * @return
     */
    private fun getResourceByReflect(imageName: String): Int {
        val mipmap: Class<*> = R.mipmap::class.java
        var field: Field? = null
        var r_id: Int
        try {
            field = mipmap.getField(imageName)
            r_id = field.getInt(field.name)
        } catch (e: Exception) {
            r_id = R.mipmap.menu_icon
            //Log.e("ERROR", "PICTURE NOT　FOUND！");
        }
        return r_id
    }

    /**
     * 标题
     */
    inner class ChildViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var ll_content: RelativeLayout = view.findViewById<View>(R.id.ll_content) as RelativeLayout
        var tubiao: ImageView = view.findViewById<View>(R.id.mokuai_tubiao) as ImageView
        var name: TextView = view.findViewById<View>(R.id.mokuai_mingcheng) as TextView
    }

    /**
     * 菜单栏
     */
    inner class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var group_name: TextView = itemView.findViewById<TextView>(R.id.group_name)
        var show_more: TextView = itemView.findViewById<TextView>(R.id.more)
        var ic_more: ImageView = itemView.findViewById<ImageView>(R.id.ic_more)
    }

}