package com.example.aboutcanada

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aboutcanada.databinding.ActivityMainBinding
import com.example.aboutcanada.dataclass.Row

class MainActivity : AppCompatActivity() {

    private val adapter by lazy {
        MainAdapter()
    }

    private val arrayList by lazy {
        ArrayList<Row>()
    }

    private lateinit var mainViewModel: MainViewModel

    private lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        initRecyclerView()
        mainViewModel.fetchFactsDataFromJson()
        setObserver()
    }

    private fun setObserver() {
        mainViewModel.countryDataObserver().observe(this) { countryData ->
            title = countryData.title
            countryData.rows.forEach { row ->
                if (row.title?.isNotEmpty() == true || row.description?.isNotEmpty() == true || row.imageHref?.isNotEmpty() == true)
                    arrayList.add(row)
            }
            adapter.setData(arrayList)
        }
    }

    private fun initRecyclerView() {
        val rv = activityMainBinding.rv
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
    }

}