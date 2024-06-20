package com.example.financyq.data.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.financyq.data.api.ApiService
import kotlinx.coroutines.Dispatchers
import com.example.financyq.data.di.Result
import com.example.financyq.data.response.DetailResponse

class DetailIncomeRepository(private val apiService: ApiService) {

    fun getDetailIncome(idUser: String): LiveData<Result<DetailResponse>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = apiService.getDetailIncome(idUser)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    emit(Result.Success(responseBody))
                } else {
                    emit(Result.Error("Response body is null"))
                }
            } else {
                emit(Result.Error(response.message() ?: "Unknown error"))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Exception occurred"))
        }
    }

    companion object {
        @Volatile
        private var instance: DetailIncomeRepository? = null

        fun getInstance(apiService: ApiService): DetailIncomeRepository =
            instance ?: synchronized(this) {
                instance ?: DetailIncomeRepository(apiService)
            }.also { instance = it }
    }
}