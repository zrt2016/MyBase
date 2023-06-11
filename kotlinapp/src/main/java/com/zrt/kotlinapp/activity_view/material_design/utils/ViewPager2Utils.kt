package com.zrt.kotlinapp.activity_view.material_design.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zrt.kotlinapp.R
import kotlinx.android.synthetic.main.list_item_card.*

/**
 * @author：Zrt
 * @date: 2022/9/6
 * 添加ViewPager2的适配器
 */
class VPFragment2Adapter(val activity:FragmentActivity, val itemsCount:Int)
        : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = itemsCount

    override fun createFragment(position: Int): Fragment {
        return VPFragment2.newInstance(position)
    }

}
class VPFragment2: Fragment() {
    var fgPosition: Int = -1
    companion object{
        fun newInstance(position: Int):Fragment{
            return VPFragment2().apply {
                arguments = Bundle().apply {
                    putInt("position", position)
                }
            }
        }
    }

//    private var mRecyclerView: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            fgPosition = it.getInt("position")
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        mRecyclerView = inflater.inflate(R.layout.vp_fragment_recycle, container, false) as RecyclerView?
        return inflater.inflate(R.layout.list_item_card, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        item_card_context.text = "Position:$fgPosition"
    }
}