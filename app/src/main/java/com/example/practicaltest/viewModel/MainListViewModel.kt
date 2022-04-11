package com.example.practicaltest.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicaltest.API.RetrofitService
import com.example.practicaltest.responseModel.MainListResponse
import com.example.practicaltest.utils.Prefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.practicaltest.utils.Result
import com.example.practicaltest.utils.safeAPIcall
import kotlinx.coroutines.withContext

class MainListViewModel : ViewModel() {

    var responseModel = MutableLiveData<Result<MainListResponse>>()

    init {
        // Call Main list API when initiate MainListFragment
        Prefs.authAPI = "bd_suvlascentralpos"
        viewModelScope.launch(Dispatchers.IO) {
            responseModel.postValue(Result.Loading())
            val result: Result<MainListResponse> = safeAPIcall {
                RetrofitService.api.callListAPI()
            }
            withContext(Dispatchers.Main) {
                responseModel.value = result
            }
        }
    }

}