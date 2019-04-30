package com.example.mahmoud_ashraf.tvshow.models

import android.arch.lifecycle.LiveData
import android.arch.paging.PagedList

data class TvShowsResult(
    val data: LiveData<PagedList<Result>>,
    val networkStatus: LiveData<NetworkStatus>,
    val initialStatus: LiveData<NetworkStatus>
)