package com.example.store.utils.network

import android.annotation.SuppressLint
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@Suppress("DEPRECATION")
class NetworkChecker @Inject constructor(
    private val manger: ConnectivityManager,
    private val request: NetworkRequest
) :
    ConnectivityManager.NetworkCallback() {

    private val isNetworkAvailable = MutableStateFlow(false)
    private var capabilities: NetworkCapabilities? = null

    @SuppressLint("ObsoleteSdkInt")
    fun checkNetwork(): MutableStateFlow<Boolean> {
        //Register
        manger.registerNetworkCallback(request, this)
        //Init network
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Active network
            val activeNetwork = manger.activeNetwork
            if (activeNetwork == null) {
                isNetworkAvailable.value = false
                return isNetworkAvailable
            }
            //Capabilities
            capabilities = manger.getNetworkCapabilities(activeNetwork)
            if (capabilities == null) {
                isNetworkAvailable.value = false
                return isNetworkAvailable
            }

            isNetworkAvailable.value = when {
                capabilities!!.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            manger.run {
                manger.activeNetworkInfo?.run {
                    isNetworkAvailable.value = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }
                }
            }
        }
        return isNetworkAvailable
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        isNetworkAvailable.value = true
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        isNetworkAvailable.value = false
    }
}