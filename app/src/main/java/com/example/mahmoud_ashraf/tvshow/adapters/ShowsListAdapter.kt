package com.example.mahmoud_ashraf.tvshow.adapters

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import com.example.mahmoud_ashraf.tvshow.R
import com.example.mahmoud_ashraf.tvshow.models.NetworkStatus
import com.example.mahmoud_ashraf.tvshow.models.Result
import com.example.mahmoud_ashraf.tvshow.utils.loadImage

/**
 * Created by mahmoud_ashraf on 30,04,2019
 */

class ShowsListAdapter : PagedListAdapter<Result, RecyclerView.ViewHolder>(SHOW_COMPARATOR) {

    var networkStatus: NetworkStatus? = null
        set(networkStatus) {
            this.networkStatus.let {
                field = networkStatus
                when {
                    it == NetworkStatus.LOADING && networkStatus != NetworkStatus.LOADING ->
                        notifyItemRemoved(super.getItemCount())

                    it != NetworkStatus.LOADING && networkStatus == NetworkStatus.LOADING ->
                        notifyItemInserted(super.getItemCount())
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        LayoutInflater.from(parent.context).inflate(viewType, parent, false).let {
            when (viewType) {
                ITEM_VIEW_TYPE -> ShowItemViewHolder(it)
                NETWORK_STATE_VIEW_TYPE -> NetworkLoadingViewHolder(it)
                else -> throw IllegalStateException()
            }
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ITEM_VIEW_TYPE -> (holder as ShowItemViewHolder).bind(getItem(position))
            NETWORK_STATE_VIEW_TYPE -> Unit
        }
    }

    override fun getItemViewType(position: Int): Int =
        if (isPaginating() && position == itemCount - 1) NETWORK_STATE_VIEW_TYPE else ITEM_VIEW_TYPE

    override fun getItemCount(): Int = super.getItemCount() + if (isPaginating()) 1 else 0

    private fun isPaginating(): Boolean = networkStatus != null && networkStatus!! == NetworkStatus.LOADING

    inner class ShowItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var showNameTextView = view.findViewById<TextView>(R.id.showNameTextView)
        private var showPosterImageView = view.findViewById<ImageView>(R.id.showPosterImageView)
        private var showRatingBar = view.findViewById<RatingBar>(R.id.showRatingBar)

        fun bind(tvShow: Result?) {
            tvShow!!.run {
                showNameTextView.text = name
                Log.e("img ++++++++++++",""+""+backdropPath)
               // backdropPath?.let {
                    showPosterImageView.loadImage("http://image.tmdb.org/t/p/"+"/w342/"+backdropPath)
                //}
                showRatingBar.rating = voteAverage.toFloat() / 2f
                itemView.setOnClickListener {
                    Toast.makeText(it.context,"clicked",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    class NetworkLoadingViewHolder(view: View) : RecyclerView.ViewHolder(view)

    companion object {
        const val ITEM_VIEW_TYPE = R.layout.layout_tvshow_item
        const val NETWORK_STATE_VIEW_TYPE = R.layout.layout_tvshow_loading

        private val SHOW_COMPARATOR = object : DiffUtil.ItemCallback<Result>() {
            override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean =
                oldItem == newItem
        }
    }
}