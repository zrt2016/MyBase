package com.zrt.kotlinapp.fragment.new_content

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zrt.kotlinapp.R
import com.zrt.kotlinapp.fragment.BasicFragment
import com.zrt.kotlinapp.fragment.NewsContentActivity
import kotlinx.android.synthetic.main.activity_news_title.*
import kotlinx.android.synthetic.main.fragment_news_content.*
import kotlinx.android.synthetic.main.fragment_news_title.*

/**
 * @author：Zrt
 * @date: 2022/6/19
 */
class NewsTitleFragment:BasicFragment() {
    //是否开起双页
    var isTwoPane = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_news_title, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isTwoPane = activity?.findViewById<View>(R.id.newsContentLayout) != null
        Log.i(">>>>", "##isTwoPane=$isTwoPane")
        // 添加数据
        val layoutManager = LinearLayoutManager(activity)
        newsTitleRecycler.layoutManager = layoutManager
        val newsAdapter = NewsAdapter(getNews())
        newsTitleRecycler.adapter = newsAdapter
    }

    private fun getNews(): List<News> {
        val newList = ArrayList<News>()
        for (i in 1..50){
            newList.add(News("Title $i", "Content $i"))
        }
        return newList
    }


    inner class NewsAdapter(val newsList: List<News>): RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
        inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
            val newsTitle = view.findViewById<TextView>(R.id.newsTitle)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
            val holder = ViewHolder(view)
            holder.itemView.setOnClickListener {
                val news = newsList[holder.adapterPosition]
                if (isTwoPane){ // 双页模式
                    val newsContentFragment = newsContentFrag as NewsContentFragment
                    newsContentFragment.refresh(news.title,news.content)
                }else { // 单页模式
                    NewsContentActivity.actionStart(parent.context, news.title, news.content)
                }
            }
            return holder
        }

        override fun getItemCount(): Int = newsList.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val news = newsList[position]
            holder.newsTitle.text = news.title
        }
    }
}