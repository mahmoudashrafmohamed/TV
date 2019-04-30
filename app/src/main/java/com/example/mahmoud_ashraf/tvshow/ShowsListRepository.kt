package com.example.mahmoud_ashraf.tvshow

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.example.mahmoud_ashraf.tvshow.paging.TvShowsPagingDataSource
import com.example.mahmoud_ashraf.tvshow.paging.TvShowsPagingDataSourceFactory
import com.example.mahmoud_ashraf.tvshow.reset.Api
import com.example.mahmoud_ashraf.tvshow.reset.NetworkConfigurator
import com.example.mahmoud_ashraf.tvshow.models.TvShowsResult

/**
 * Created by mahmoud_ashraf on 30,04,2019
 */

class ShowsListRepository  {


     var api: Api

    var sourceLiveData: LiveData<TvShowsPagingDataSource>? = null

    init {
        api = NetworkConfigurator.provideApi()
    }

    fun getPopularShows(): TvShowsResult = getShows(TvShowsPagingDataSourceFactory(api))


    private fun getShows(dataSourceFactory: TvShowsPagingDataSourceFactory): TvShowsResult {
        sourceLiveData = dataSourceFactory.sourceLiveData


        val networkStatus = Transformations.switchMap(dataSourceFactory.sourceLiveData, { it.networkStatus })
        val initialLoading = Transformations.switchMap(dataSourceFactory.sourceLiveData, { it.initialLoading })

        val pagedListConfig = PagedList
            .Config
            .Builder()
            .setEnablePlaceholders(false)
            .setPageSize(TV_SHOWS_PAGE_SIZE)
            .build()

        // LiveData<PagedList<Result>>
        val data = LivePagedListBuilder(dataSourceFactory, pagedListConfig).build()

        return TvShowsResult(data, networkStatus, initialLoading)
    }

    fun retryGettingPagedShows() = sourceLiveData?.value?.retry()

    companion object {
        private const val TV_SHOWS_PAGE_SIZE = 20
    }
}