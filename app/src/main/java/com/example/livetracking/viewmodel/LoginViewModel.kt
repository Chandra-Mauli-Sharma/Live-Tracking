package com.example.livetracking.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livetracking.repository.LoginRepository
import com.example.livetracking.util.readString
import com.example.livetracking.util.writeString
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository,@ApplicationContext val appContext:Context): ViewModel() {
    private val _userName = MutableStateFlow("");
    val userName = _userName.asStateFlow()

    private val _password = MutableStateFlow("");
    val password = _password.asStateFlow()


    private val _driverId=MutableStateFlow("")
    val driverId=_driverId.asStateFlow()

    private val _isLoggedIn=MutableStateFlow(false)
    val isLoggedIn=_isLoggedIn.asStateFlow()
    fun onUserNameChange(userName:String) {
        _userName.update { userName }
    }

    fun onPasswordChange(password:String) {
        _password.update { password }
    }

    fun login(){
        viewModelScope.launch {
            val response=loginRepository.login(userName.value,password.value)

            if(response.isSuccessful){
                appContext.writeString("driver_id",response.body()?.data?.id.toString())
                _driverId.update {
                    response.body()?.data?.id.toString()
                }
            }
        }
    }
}