package com.example.aboutcanada

import android.os.Bundle
import android.view.View
import android.widget.Toast
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
        initViewModel()
        initRecyclerView()
        loadApi()
        setObserver()
        setSwipeRefreshListener()
    }

    private fun setSwipeRefreshListener() {
        activityMainBinding.swipeContainer.setOnRefreshListener {
            loadApi()
        }
        // Configure the refreshing colors
        activityMainBinding.swipeContainer.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )
    }

    private fun loadApi() {
        if (mainViewModel.isNetworkAvailable(this)) {
            mainViewModel.loadFactsApi()
        } else
            Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT)
                .show()
    }

    private fun initViewModel() {
        val mainViewModelFactory = MainViewModelFactory(MainRepository())
        mainViewModel = ViewModelProvider(this, mainViewModelFactory)[MainViewModel::class.java]
    }

    private fun setObserver() {
        mainViewModel.factsDataObserver().observe(this) { factsData ->
            arrayList.clear()
            title = factsData.title
            factsData.rows.forEach { row ->
                if (row.title?.isNotEmpty() == true || row.description?.isNotEmpty() == true || row.imageHref?.isNotEmpty() == true)
                    arrayList.add(row)
            }
            activityMainBinding.progressBar.visibility = View.GONE
            activityMainBinding.swipeContainer.isRefreshing = false
            adapter.setData(arrayList)
        }
    }

    private fun initRecyclerView() {
        val recyclerView = activityMainBinding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

}