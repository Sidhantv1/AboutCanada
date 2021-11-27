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

/**
 * MainActivity Launcher Activity
 */
class MainActivity : AppCompatActivity() {

    // variable used for adapter class to set data on the recycler view
    private val mainAdapter by lazy {
        MainAdapter()
    }

    // variable used for adding data to the arraylist of type Row
    private val arrayList by lazy {
        ArrayList<Row>()
    }

    // boolean variable used to check if pulled to reload the list is performed
    private var isPulledToLoad: Boolean = false

    // variable for the viewModel initialization
    private lateinit var mainViewModel: MainViewModel

    // variable for the data binding of the MainActivity's layout
    private lateinit var activityMainBinding: ActivityMainBinding

    /**
     * Method called when the activity is first created
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        activityMainBinding.displayMessage = getString(R.string.no_internet_message)
        initViewModel()
        initRecyclerView()
        loadFactsDataApi(false)
        setObserver()
        setSwipeRefreshListener()
    }

    /**
     * Method called to setup the listener when swipe to refresh is performed
     */
    private fun setSwipeRefreshListener() {
        activityMainBinding.swipeContainer.setOnRefreshListener {
            loadFactsDataApi(true)
        }
        // Configure the refreshing colors
        activityMainBinding.swipeContainer.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )
    }

    /**
     * Method called to call the Facts API to load the data on the recycler view
     */
    private fun loadFactsDataApi(isPulledToRefresh: Boolean) {
        isPulledToLoad = isPulledToRefresh
        when {
            // Checking the internet is available
            mainViewModel.isNetworkAvailable(this) -> {
                activityMainBinding.tvNoInternetMsg.visibility = View.GONE
                activityMainBinding.recyclerView.visibility = View.VISIBLE
                mainViewModel.getFactsData(isPulledToRefresh)
            }
            // Checking is pulled to refresh is performed
            isPulledToRefresh -> {
                activityMainBinding.swipeContainer.isRefreshing = false
                Toasty.info(this, getString(R.string.no_internet_connection_message)).show()
            }
            // When Internet Connection is not available
            else -> {
                activityMainBinding.tvNoInternetMsg.visibility = View.VISIBLE
                activityMainBinding.recyclerView.visibility = View.GONE
                activityMainBinding.progressBar.visibility = View.GONE
            }
        }
    }

    /**
     * Method to Initialize the viewModel object and also adding the ViewModel Factory instance for the repository
     */
    private fun initViewModel() {
        val mainViewModelFactory = MainViewModelFactory(MainRepository())
        mainViewModel = ViewModelProvider(this, mainViewModelFactory)[MainViewModel::class.java]
    }

    /**
     * Method to set the observer for the live data response when the data is fetched
     */
    private fun setObserver() {
        // response live data observer
        mainViewModel.factsDataClassLiveDataResponseModel.observe(this) {
            when (it) {
                // Loader set when the api
                is DataResult.Loading -> {
                    // if pulled to refresh is done then not showing the loader
                    if (isPulledToLoad)
                        activityMainBinding.progressBar.visibility = View.GONE
                    else
                        activityMainBinding.progressBar.visibility = View.VISIBLE
                }
                // api success response is observed
                is DataResult.Success -> {
                    // loader is removed
                    activityMainBinding.progressBar.visibility = View.GONE
                    arrayList.clear()
                    // setting the data on the Toolbar tile
                    title = it.data.title
                    // adding data to the arraylist to be used to set data on the adapter
                    it.data.rows.forEach { row ->
                        if (row.title?.isNotEmpty() == true || row.description?.isNotEmpty() == true || row.imageHref?.isNotEmpty() == true)
                            arrayList.add(row)
                    }
                    // when swipe to refresh is done then disabling its own loader
                    activityMainBinding.swipeContainer.isRefreshing = false
                    //setting the arraylist data on the adapter
                    mainAdapter.setData(arrayList)
                }
                // api failure response is observed
                is DataResult.Failure -> {
                    Toasty.error(this, getString(R.string.error_message)).show()
                }
            }
        }
    }

    /**
     * Method to init recycler view
     */
    private fun initRecyclerView() {
        val recyclerView = activityMainBinding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        mainAdapter.setHasStableIds(true)
        recyclerView.adapter = mainAdapter
    }

}