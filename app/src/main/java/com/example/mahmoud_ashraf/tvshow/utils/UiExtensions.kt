package com.example.mahmoud_ashraf.tvshow.utils

import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import androidx.core.view.doOnPreDraw
import com.squareup.picasso.Picasso


fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}


fun ImageView.loadImage(imageUrl: String) =
        doOnPreDraw { Picasso.with(this.context).load(imageUrl).into(this) }

fun AppCompatActivity.showConnectionErrorView(onRetry: () -> Unit) =
        Snackbar.make(findViewById(android.R.id.content),
               "Ups! It seems like you don\'t have internet connection", Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry") { onRetry() }
                .show()