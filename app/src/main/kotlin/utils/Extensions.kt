package utils

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.signature.ObjectKey
import com.dmanluc.cabifymarket.presentation.core.GlideApp
import com.google.android.material.snackbar.Snackbar
import org.apache.commons.io.IOUtils

/**
 * Extension utils
 *
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    29/10/2018.
 */
fun Context.hasActiveNetworkConnectivity(): Boolean {
    return (getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)?.let {
        it.activeNetworkInfo != null && it.activeNetworkInfo.isConnected
    } ?: false
}

fun Fragment.showSnackbar(snackbarText: String, timeLength: Int) {
    activity?.let { Snackbar.make(it.findViewById(android.R.id.content), snackbarText, timeLength).show() }
}

fun Fragment.setupSnackbar(lifecycleOwner: LifecycleOwner, snackbarEvent: LiveData<Event<Int>>,
                           timeLength: Int) {
    snackbarEvent.observe(lifecycleOwner, Observer { event ->
        event.getContentIfNotHandled()?.let { res ->
            context?.let { showSnackbar(it.getString(res), timeLength) }
        }
    })
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun ImageView.loadImage(path: String, errorResource: Int,
                        onExceptionDelegate: () -> Unit = {},
                        onResourceReadyDelegate: () -> Unit = {},
                        daysWhileValidCache: Int = 1) {

    GlideApp.with(this.context)
        .load(Uri.parse(path))
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?,
                                      isFirstResource: Boolean): Boolean {
                onExceptionDelegate()
                return false
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>,
                                         dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                onResourceReadyDelegate()
                return false
            }
        })
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        .signature(ObjectKey(with(System.currentTimeMillis()) {
            if (daysWhileValidCache > 0) (this / (daysWhileValidCache * 24 * 60 * 60 * 1000)).toString()
            else this.toString()
        }))
        .error(errorResource)
        .transition(DrawableTransitionOptions().crossFade())
        .into(this)
}

fun Int.dpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

fun AssetManager.readJsonAssetFileName(fileName: String): String {
    if (fileName.isBlank()) {
        throw IllegalArgumentException("JsonFileName can\'t be null or empty")
    }

    return IOUtils.toString(this.open(String.format("json/%s.json", fileName)))
}

fun <T> MutableLiveData<T>.notifyObserver() {
    this.value = this.value
}

private fun <R> bindObserver(observer: MediatorLiveData<R?>?,
                             source: LiveData<R?>) {
    observer?.apply {
        addSource(source) {
            postValue(it)
        }
    }
}