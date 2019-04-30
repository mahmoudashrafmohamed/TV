package com.example.mahmoud_ashraf.tvshow

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import androidx.core.view.forEach
import com.example.mahmoud_ashraf.tvshow.adapters.ShowsListAdapter
import com.example.mahmoud_ashraf.tvshow.adapters.ShowsListAdapter.Companion.NETWORK_STATE_VIEW_TYPE
import com.example.mahmoud_ashraf.tvshow.models.NetworkStatus
import com.example.mahmoud_ashraf.tvshow.utils.*
import kotlinx.android.synthetic.main.activity_show_list.*

class ShowListActivity : AppCompatActivity() {



    private lateinit var viewModel: ShowsListViewModel
    val adapter: ShowsListAdapter = ShowsListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_list)
        viewModel = ViewModelProviders.of(this).get(ShowsListViewModel::class.java)

        setupShowListView()
        handleNetworkState()
        handleTvShowListAdapter()
        viewModel.onTvShowsRequested()
    }

    private fun setupShowListView() {
        val columns = 2
        val layoutManager = GridLayoutManager(this, columns)
        layoutManager.spanSizeLookup = object: GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when(adapter.getItemViewType(position)) {
                    NETWORK_STATE_VIEW_TYPE -> columns
                    else -> 1
                }
            }
        }
        showListRecyclerView.layoutManager = layoutManager


    }

    private fun handleNetworkState() {


        viewModel.initialLoading.observe(this, Observer {

            // hide all views with help of ktx :)
            showListRootLayout.forEach { it.hide() }
            when (it) {
                NetworkStatus.LOADING -> loadingProgressBar.show()
                NetworkStatus.DONE -> showListRecyclerView.show()
                NetworkStatus.ERROR -> showConnectionErrorView({ viewModel.onTvShowsRequested() })
            }
        })



        viewModel.networkStatus.observe(this, Observer {
            adapter.networkStatus = it
            when (it) { // preview snack bar with retry option ....
                NetworkStatus.ERROR -> showConnectionErrorView({ viewModel.onRetryGettingPagedShows() })
                else -> Unit
            }
        })
    }

    private fun handleTvShowListAdapter() {
        if (showListRecyclerView.adapter == null)
            showListRecyclerView.adapter = adapter

        viewModel.tvShowList.observe(this, Observer {
            adapter.submitList(it) })
    }
}
