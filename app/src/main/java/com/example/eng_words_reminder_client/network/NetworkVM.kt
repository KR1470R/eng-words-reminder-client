package com.example.eng_words_reminder_client.network

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eng_words_reminder_client.network.requests.RequestLogin
import android.provider.Settings
import com.example.eng_words_reminder_client.network.requests.RequestGetTenWords
import com.example.eng_words_reminder_client.network.requests.RequestRegister
import com.example.eng_words_reminder_client.network.responses.ResponseGetTenWords
import com.example.eng_words_reminder_client.network.responses.ResponseStatistic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.Base64

class NetworkVM : ViewModel() {

    /**ID part*/

    private val _id = MutableLiveData<String>()
    val id: LiveData<String> = _id
    private val getIdValue: String? get() = _id.value

    /**Maybe not best method for creating unique ID of user, but best right now.
    Google ID much better, but no idea how to get that without Facebook lib and unique keys of that.*/
    @SuppressLint("HardwareIds")
    fun getId(context: Context) = viewModelScope.launch(Dispatchers.IO) {
        val id = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        _id.postValue(id)
    }

    fun setId(id: String) {
        _id.postValue(id)
    }


    /**NetWork part*/

    private val _token = MutableLiveData("")
    val token: LiveData<String> = _token
    private val getTokenValue: String? get() = _token.value

    private fun setTokenValue(token: String) {
        _token.postValue(token)
    }

    fun authUser() {
        if (getTokenValue?.isEmpty() == true) {
            //todo: remove this in the prod
            val input = "dev:8fed5f3efe975dfba30d81502ae2be905baaf04d79ef4b1c32f610e08836f9ce"
            val encodedBytes = Base64.getEncoder().encode(input.toByteArray())
            val encodedString = String(encodedBytes)
            //todo: remove this in the prod

            viewModelScope.launch {
                val call = RequestLogin(
                    getIdValue.toString(),
                    getIdValue.toString()
                )
                try {
                    val response = NetworkService.requestAPI.loginUser(call, "Basic $encodedString")
                    setTokenValue(response.access_token)
                } catch (e: Exception) {
                    Log.d("Network error", "Login:${e.message}")
                }
            }
        }
    }

    fun registerUser() {
        //todo: remove this in the prod
        val input = "dev:8fed5f3efe975dfba30d81502ae2be905baaf04d79ef4b1c32f610e08836f9ce"
        val encodedBytes = Base64.getEncoder().encode(input.toByteArray())
        val encodedString = String(encodedBytes)
        //todo: remove this in the prod

        viewModelScope.launch {
            val call = RequestRegister(
                getIdValue.toString(),
                getIdValue.toString()
            )
            try {
                val response = NetworkService.requestAPI.registerUser(call, "Basic $encodedString")
                setTokenValue(response.access_token)
            } catch (e: Exception) {
                Log.d("Network error", "Registration:${e.message}")
            }
        }
    }

    private val _dataTenWords = MutableLiveData<List<ResponseGetTenWords>>()
    val dataTenWords: LiveData<List<ResponseGetTenWords>> = _dataTenWords
    val getDataTenWords: List<ResponseGetTenWords>? get() = _dataTenWords.value

    fun getTenWords() {
        viewModelScope.launch {
            val call = RequestGetTenWords(
                10
            )
            try {
                val response =
                    NetworkService.requestAPI.getTenWords(call, "Bearer $getTokenValue")
                _dataTenWords.postValue(response)
            } catch (e: Exception) {
                Log.d("Network error", "TenWords:${e.message}")
            }
        }
    }

    private val _statistic = MutableLiveData<ResponseStatistic>()
    val statistic: LiveData<ResponseStatistic> = _statistic

    fun getStatistic() {
        viewModelScope.launch {
            try {
                val response =
                    NetworkService.requestAPI.getStatistic("Bearer $getTokenValue")
                _statistic.postValue(response)
            } catch (e: Exception) {
                Log.d("Network error", "GetStatistic:${e.message}")
            }
        }
    }

    fun resetStatistic() {
        viewModelScope.launch {
            try {
                val response =
                    NetworkService.requestAPI.resetStatistic("Bearer $getTokenValue")
            } catch (e: Exception) {
                Log.d("Network error", "DeleteStatistic:${e.message}")
            }
        }
    }
}