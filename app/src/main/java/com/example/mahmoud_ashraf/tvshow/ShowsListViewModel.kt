package com.example.mahmoud_ashraf.tvshow

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.arch.paging.PagedList
import com.example.mahmoud_ashraf.tvshow.models.NetworkStatus
import com.example.mahmoud_ashraf.tvshow.models.TvShowsResult
import com.example.mahmoud_ashraf.tvshow.models.Result


/**
 * Created by mahmoud_ashraf on 30,04,2019
 */

class ShowsListViewModel : ViewModel() {

    // observable list of result ...
    private val tvShowListResponse = MutableLiveData<TvShowsResult>()

    // repository to get data from local or private
    private val tvShowListRepository = ShowsListRepository()

    // doing transformation on the parts of observer .....
    val tvShowList: LiveData<PagedList<Result>> = Transformations.switchMap(tvShowListResponse, { it -> it.data })
    val networkStatus: LiveData<NetworkStatus> =  Transformations.switchMap(tvShowListResponse, { it -> it.networkStatus })
    val initialLoading: LiveData<NetworkStatus> = Transformations.switchMap(tvShowListResponse, { it -> it.initialStatus })

    //  باطلب اجيب الدتا من ال repository
    fun onTvShowsRequested() {
        if (tvShowListResponse.value == null || tvShowListResponse.value!!.initialStatus.value == NetworkStatus.ERROR)
            tvShowListResponse.postValue(tvShowListRepository.getPopularShows()) // make the response observable :)
    }


    /* ده شكل ال response الــ هيرجع ... قبل ال transformations
     * data class TvShowsResult(
    val data: LiveData<PagedList<Result>>,
    val networkStatus: LiveData<NetworkStatus>,
    val initialStatus: LiveData<NetworkStatus>
    )
     */

    // باطلب اعادة محاولة تحميل الداتا في حاله فشل التحميل ...
    fun onRetryGettingPagedShows() = tvShowListRepository.retryGettingPagedShows() // make the response observable :)
}