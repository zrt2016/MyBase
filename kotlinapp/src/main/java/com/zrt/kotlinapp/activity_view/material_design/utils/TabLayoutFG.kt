package com.zrt.kotlinapp.activity_view.material_design.utils

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.utils.ToastUtils


/**
 * @author：Zrt
 * @date: 2022/9/5
 */
class VPFragment: Fragment() {

    private var mRecyclerView: RecyclerView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRecyclerView = inflater.inflate(R.layout.vp_fragment_recycle, container, false) as RecyclerView?
        return mRecyclerView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mRecyclerView?.let {
            it.layoutManager = LinearLayoutManager(it?.context)
            it.adapter = RecyclerViewAdapter(requireContext())
        }
    }
}
class RecyclerViewAdapter(val context:Context, val countItems: Int = 10) :RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return countItems
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemView = holder.itemView
        itemView.setOnClickListener {
            view ->
            ToastUtils.show("奔跑在孤傲的路上")
        }
    }
}

class FragmentAdapter(val fm:FragmentManager, val fragments:List<Fragment>,
                       val titles:List<String>): FragmentStatePagerAdapter(fm){
    override fun getItem(position: Int): Fragment {
        return fragments?.get(position)
    }

    override fun getCount(): Int {
        return if (fragments == null) 0 else fragments.size
    }
    override fun getPageTitle(position: Int): CharSequence? {
        return return titles[position]
    }
}
