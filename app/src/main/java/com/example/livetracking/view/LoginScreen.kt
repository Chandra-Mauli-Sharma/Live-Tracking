package com.example.livetracking.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.livetracking.R
import com.example.livetracking.viewmodel.LoginViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(viewModel: LoginViewModel= hiltViewModel()) {

    val username by viewModel.userName.collectAsState()
    val password by viewModel.password.collectAsState()
    Scaffold {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.logistics),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(250.dp)
                    .padding(50.dp)
            )

            Text(text = "Login", style = MaterialTheme.typography.displayLarge)

            Spacer(modifier = Modifier.height(30.dp))
            OutlinedTextField(value = username, onValueChange =viewModel::onUserNameChange, label = {
                Text(text = "Username")
            })
            Spacer(modifier = Modifier.height(15.dp))

            OutlinedTextField(value = password, onValueChange =viewModel::onPasswordChange, label = {
                Text(text = "Password")
            }, visualTransformation = PasswordVisualTransformation())
            Spacer(modifier = Modifier.height(15.dp))

            OutlinedButton(onClick = { viewModel.login()}) {
                Text(text = "Login")
            }
        }
    }
}