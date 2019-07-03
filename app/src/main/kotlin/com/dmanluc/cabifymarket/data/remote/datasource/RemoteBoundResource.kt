package com.dmanluc.cabifymarket.data.remote.datasource

import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dmanluc.cabifymarket.data.remote.utils.Resource
import kotlinx.coroutines.*
import kotlin.coroutines.coroutineContext

abstract class RemoteBoundResource<ResultType, RequestType> {

    private val result = MutableLiveData<Resource<ResultType>>()
    private val supervisorJob = SupervisorJob()

    suspend fun build(): RemoteBoundResource<ResultType, RequestType> {
        withContext(Dispatchers.Main) {
            result.value = Resource.loading()
        }
        CoroutineScope(coroutineContext).launch(supervisorJob) {
            try {
                fetchFromNetwork()
            } catch (ex: Exception) {
                Log.e("BaseDataSource", "An error happened: $ex")
                setValue(Resource.error(ex))
            }
        }
        return this
    }

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    private suspend fun fetchFromNetwork() {
        Log.d(RemoteBoundResource::class.java.name, "Fetch data from network")
        val apiResponse = performNetworkCallAsync().await()
        Log.d(RemoteBoundResource::class.java.name, "Data fetched from network")
        setValue(Resource.success(processResponse(apiResponse)))
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        Log.d(RemoteBoundResource::class.java.name, "Resource: $newValue")
        if (result.value != newValue) result.postValue(newValue)
    }

    @WorkerThread
    protected abstract fun processResponse(response: RequestType): ResultType

    @MainThread
    protected abstract fun performNetworkCallAsync(): Deferred<RequestType>

}