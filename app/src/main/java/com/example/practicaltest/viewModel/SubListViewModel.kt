package com.example.practicaltest.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicaltest.API.RetrofitService
import com.example.practicaltest.responseModel.SublistResponse
import com.example.practicaltest.utils.Result
import com.example.practicaltest.utils.safeAPIcall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SubListViewModel : ViewModel() {
    var responseModel = MutableLiveData<Result<SublistResponse>>()

    // Call SubList API when initiate SubListFragment
    fun callSublistAPI() {
        viewModelScope.launch(Dispatchers.IO) {
            responseModel.postValue(Result.Loading())
            val result: Result<SublistResponse> = safeAPIcall {
                RetrofitService.api.callSubListAPI()
            }
            responseModel.postValue(result)
        }
    }
}