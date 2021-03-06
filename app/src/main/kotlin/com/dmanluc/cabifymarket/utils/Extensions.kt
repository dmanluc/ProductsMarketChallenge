@file:JvmName("ExtensionsKt")

package com.dmanluc.cabifymarket.utils

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Handler
import android.view.View
import android.widget.ImageView
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import br.com.simplepass.loadingbutton.animatedDrawables.ProgressType
import br.com.simplepass.loadingbutton.customViews.ProgressButton
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.signature.ObjectKey
import com.dmanluc.cabifymarket.R
import com.dmanluc.cabifymarket.presentation.core.GlideApp
import com.google.android.material.snackbar.Snackbar
import org.apache.commons.io.IOUtils
import org.jetbrains.annotations.TestOnly

/**
 * App extension utils
 *
 * @author   Daniel Manrique Lucas <dmanluc91@gmail.com>
 * @version  1
 * @since    29/10/2018.
 */
fun Fragment.showSnackbar(snackbarText: String, timeLength: Int) {
    activity?.let {
        Snackbar.make(it.findViewById(android.R.id.content), snackbarText, timeLength).show()
    }
}

fun Fragment.setupSnackbarWithStringResId(
    lifecycleOwner: LifecycleOwner,
    snackbarEvent: LiveData<Event<Int>>,
    timeLength: Int
) {
    snackbarEvent.observe(lifecycleOwner, Observer { event ->
        event.getContentIfNotHandled()?.let { res ->
            context?.let { showSnackbar(it.getString(res), timeLength) }
        }
    })
}

fun Fragment.setupSnackbarWithStringLiteral(
    lifecycleOwner: LifecycleOwner,
    snackbarEvent: LiveData<Event<String>>,
    timeLength: Int
) {
    snackbarEvent.observe(lifecycleOwner, Observer { event ->
        event.getContentIfNotHandled()?.let { literal ->
            context?.let { showSnackbar(literal, timeLength) }
        }
    })
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun ImageView.loadImage(
    path: String,
    errorResource: Int,
    onExceptionDelegate: () -> Unit = {},
    onResourceReadyDelegate: () -> Unit = {},
    daysWhileValidCache: Int = 1
) {

    GlideApp.with(this.context).load(Uri.parse(path)).listener(object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: com.bumptech.glide.request.target.Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            onExceptionDelegate()
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: com.bumptech.glide.request.target.Target<Drawable>,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            onResourceReadyDelegate()
            return false
        }
    }).diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        .signature(ObjectKey(with(System.currentTimeMillis()) {
            if (daysWhileValidCache > 0) (this / (daysWhileValidCache * 24 * 60 * 60 * 1000)).toString()
            else this.toString()
        })).error(errorResource).transition(DrawableTransitionOptions().crossFade()).into(this)
}

fun AssetManager.readJsonAssetFileName(fileName: String): String {
    if (fileName.isBlank()) {
        throw IllegalArgumentException("JsonFileName can\'t be null or empty")
    }

    return IOUtils.toString(this.open(String.format("json/%s.json", fileName)))
}

@MainThread
fun <T> MutableLiveData<T>.notifyObserver() {
    this.value = this.value
}

@WorkerThread
fun <T> MutableLiveData<T>.postNotifyObserver() {
    postValue(this.value)
}

@MainThread
inline fun <T> LiveData<Event<T>>.observeEvent(
    owner: LifecycleOwner,
    crossinline onEventUnhandledContent: (T) -> Unit
) {
    observe(owner, Observer { it?.getContentIfNotHandled()?.let(onEventUnhandledContent) })
}

@MainThread
fun <M, S> MediatorLiveData<M>.observeAndMapValue(source: LiveData<S>, func: (data: S) -> M) {
    this.removeSource(source)
    this.addSource(source) {
        this.value = func.invoke(it)
    }
}

fun Boolean?.orFalse(): Boolean = this ?: false

fun <T1 : Any, T2 : Any, R : Any> safeLet(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R? =
    if (p1 != null && p2 != null) block(p1, p2) else null

@TestOnly
fun Fragment.executePendingDataBindingTransactions() {
    with(this) {
        view?.let {
            activity?.runOnUiThread {
                androidx.databinding.DataBindingUtil.getBinding<ViewDataBinding>(it)
                    ?.executePendingBindings()
            }
        }
    }
}

fun Int.dpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

fun ProgressButton.morphDoneAndRevert(
    context: Context,
    doneTime: Long = 2000,
    finishTime: Long = 2650,
    onAnimationFinished: () -> Unit = {}
) {
    progressType = ProgressType.INDETERMINATE

    val button = this
    val fillColor = ContextCompat.getColor(context, R.color.colorPrimary)
    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ic_done_white_48dp)

    button.initialCorner = 6.dpToPx().toFloat()

    startAnimation()

    Handler().run {
        postDelayed({ doneLoadingAnimation(fillColor, bitmap) }, doneTime)
        postDelayed({ onAnimationFinished() }, finishTime)
    }
}