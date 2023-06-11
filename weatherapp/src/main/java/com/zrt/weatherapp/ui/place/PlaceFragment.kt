package com.zrt.weatherapp.ui.place

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zrt.weatherapp.MainActivity
import com.zrt.weatherapp.R
import com.zrt.weatherapp.tools.ToastUtils
import com.zrt.weatherapp.ui.weather.WeatherActivity
import kotlinx.android.synthetic.main.fragment_place.*

/**
 * @author：Zrt
 * @date: 2022/8/16
 */
class PlaceFragment: Fragment() {
    val viewModel by lazy { ViewModelProvider(this).get(PlaceViewModel::class.java) }
    lateinit var adapter: PlaceAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_place, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (activity is MainActivity && viewModel.isPlaceSaved()){
            val savedPlace = viewModel.getSavedPlace()
            val intent = Intent(context, WeatherActivity::class.java).apply {
                putExtra("location_lng", savedPlace.location.lng)
                putExtra("location_lat", savedPlace.location.lat)
                putExtra("place_name", savedPlace.name)
            }
            startActivity(intent)
            activity?.finish()
            return
        }
        val linearLayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = linearLayoutManager
        adapter = PlaceAdapter(this, viewModel.placeList)
        recyclerView.adapter = adapter
        searchPlaceEdit.addTextChangedListener {
            editable ->
            val content = editable.toString()
            if (content.isNotEmpty()){
                viewModel.searchPlaces(content)
            }else{
                recyclerView.visibility = View.GONE
                bgImageView.visibility = View.VISIBLE
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }
        }
        viewModel.placeLiveData.observe(viewLifecycleOwner, Observer {
            result ->
            // 实例表示[success][Result.isSuccess]，则返回封装的值
            // 如果实例是[failure][Result.isFailure]则返回null。
            val place = result.getOrNull()
            if (place != null){
                recyclerView.visibility = View.VISIBLE
                bgImageView.visibility = View.GONE
                viewModel.placeList.clear()
                viewModel.placeList.addAll(place)
                adapter.notifyDataSetChanged()
            }else{
                ToastUtils.show("未能查询到任何地点")
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }
}