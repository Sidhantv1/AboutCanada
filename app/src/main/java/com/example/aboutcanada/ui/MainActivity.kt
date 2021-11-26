package com.example.aboutcanada.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aboutcanada.R
import com.example.aboutcanada.databinding.ActivityMainBinding
import com.example.aboutcanada.dataclass.Row
import com.example.aboutcanada.networking.DataResult
import com.example.aboutcanada.repository.MainRepository
import com.example.aboutcanada.viewmodel.MainViewModel
import com.example.aboutcanada.viewmodel.MainViewModelFactory
import es.dmoral.toasty.Toasty

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
        loadApi(false)
        setObserver()
        setSwipeRefreshListener()
    }

    private fun setSwipeRefreshListener() {
        activityMainBinding.swipeContainer.setOnRefreshListener {
            loadApi(true)
        }
        // Configure the refreshing colors
        activityMainBinding.swipeContainer.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )
    }

    private fun loadApi(isPulledToRefresh: Boolean) {
        if (mainViewModel.isNetworkAvailable(this)) {
            mainViewModel.getFactsData(isPulledToRefresh)
        } else
            Toasty.info(this, getString(R.string.no_internet_connection)).show()
    }

    private fun initViewModel() {
        val mainViewModelFactory = MainViewModelFactory(MainRepository())
        mainViewModel = ViewModelProvider(this, mainViewModelFactory)[MainViewModel::class.java]
    }

    private fun setObserver() {
        mainViewModel.factsDataClassLiveDataResponseModel.observe(this) {
            when (it) {
                is DataResult.Loading -> {
                    activityMainBinding.progressBar.visibility = View.VISIBLE
                }

                is DataResult.Success -> {
                    activityMainBinding.progressBar.visibility = View.GONE
                    arrayList.clear()
                    title = it.data.title
                    it.data.rows.forEach { row ->
                        if (row.title?.isNotEmpty() == true || row.description?.isNotEmpty() == true || row.imageHref?.isNotEmpty() == true)
                            arrayList.add(row)
                    }
                    activityMainBinding.swipeContainer.isRefreshing = false
                    adapter.setData(arrayList)
                }

                is DataResult.Failure -> {
                    Toasty.error(this, getString(R.string.error_message)).show()
                }
            }
        }
    }

    private fun initRecyclerView() {
        val recyclerView = activityMainBinding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        adapter.setHasStableIds(true)
        recyclerView.adapter = adapter
    }

}