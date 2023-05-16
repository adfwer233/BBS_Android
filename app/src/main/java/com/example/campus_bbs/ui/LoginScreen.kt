package com.example.campus_bbs.ui

import android.util.Log
import android.widget.Space
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.campus_bbs.BlogsList
import com.example.campus_bbs.ui.model.LoginViewModel
import com.example.campus_bbs.ui.network.LoginApi
import com.example.campus_bbs.ui.network.LoginDTO
import com.example.campus_bbs.ui.network.RegisterDTO
import kotlinx.coroutines.launch
import java.security.MessageDigest

fun registerHash(username: String, password: String): String {
    var hash = MessageDigest.getInstance("MD5").digest(password.toByteArray())
    var hex = StringBuilder(hash.size * 2)
    for (b in hash) {
        var str = Integer.toHexString(b.toInt())
        if (b < 0x10) {
            str = "0$str"
        }
        hex.append(str.substring(str.length - 2))
    }
    val res = hex.toString()

    hash = MessageDigest.getInstance("MD5").digest(res.toByteArray())
    hex = StringBuilder(hash.size * 2)
    for (b in hash) {
        var str = Integer.toHexString(b.toInt())
        if (b < 0x10) {
            str = "0$str"
        }
        hex.append(str.substring(str.length - 2))
    }

    return hex.toString()
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LoginScreen() {

    val items = listOf("Sign In", "Sign Up")

    var pagerState = rememberPagerState(0)

    val coroutineScope = rememberCoroutineScope()

    Column {

        Text(
            "LOGO",
            textAlign = TextAlign.Center,
            fontSize = 200.sp,
            fontWeight = FontWeight(30),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {

            Box {
                TabRow(
                    selectedTabIndex = pagerState.currentPage,
                ) {
                    items.forEachIndexed { index, title ->
                        Tab(
                            selected = pagerState.currentPage == index,
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index, 0F)
                                }
                            },
                            modifier = Modifier
                        ) {
                            Text(text = title, modifier = Modifier.padding(10.dp))
                        }
                    }
                }
            }
        }

        HorizontalPager(
            pageCount = items.size,
            state = pagerState,
            modifier = Modifier.fillMaxHeight()
        ) { page ->
            when (page) {
                0 -> SignIn()
                1 -> SignUp()
            }
        }
    }
}

@Composable
fun SignUp(

) {
    Column(modifier = Modifier.fillMaxHeight()) {
        val loginViewModel: LoginViewModel = viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)

        val uiState = loginViewModel.uiState.collectAsState()
        val scope = rememberCoroutineScope()
        val context = LocalContext.current

        Box(
            modifier = Modifier.padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxHeight()
            ) {

                OutlinedTextField(
                    value = uiState.value.username,
                    onValueChange = { loginViewModel.updateUserName(it) },
                    label = { Text(text = "Username") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        autoCorrect = true
                    )
                )

//                OutlinedTextField(
//                    value = uiState.value.email,
//                    onValueChange = { loginViewModel.updateEmail(it) },
//                    label = { Text(text = "Email") },
//                    modifier = Modifier.fillMaxWidth(),
//                    keyboardOptions = KeyboardOptions(
//                        keyboardType = KeyboardType.Email,
//                        autoCorrect = true
//                    )
//                )

                OutlinedTextField(
                    value = uiState.value.password,
                    onValueChange = { loginViewModel.updatePassword(it) },
                    label = { Text(text = "Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        autoCorrect = true
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = uiState.value.repeatedPassword,
                    onValueChange = { loginViewModel.updateRepeatedPassword(it) },
                    label = { Text(text = "Repeat Password") },
                    isError = !loginViewModel.repeatCorrect(),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        autoCorrect = true
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(5.dp))

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(onClick = {
                        scope.launch {
                            try {
                                val response = LoginApi.retrofitService.register(
                                    RegisterDTO(
                                        username = uiState.value.username.text,
                                        password = registerHash(
                                            uiState.value.username.text,
                                            uiState.value.password.text
                                        )
                                    )
                                )

                                Toast.makeText(
                                    context, "Sign Up Success",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } catch (e: Exception) {
                                Log.e("Sign Up", e.toString())
                                Toast.makeText(
                                    context, "网络错误",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }, modifier = Modifier.weight(1f)) {
                        Text(text = "Sign Up")
                    }
                }
            }
        }
    }
}

@Composable
fun SignIn(
    modifier: Modifier = Modifier
) {
    val loginViewModel: LoginViewModel = viewModel(LocalContext.current as ComponentActivity, factory = AppViewModelProvider.Factory)

    val uiState = loginViewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Box(
        modifier = Modifier.padding(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxHeight()
        ) {

            OutlinedTextField(
                value = uiState.value.username,
                onValueChange = { loginViewModel.updateUserName(it) },
                label = { Text(text = "Username") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    autoCorrect = true
                )
            )

            OutlinedTextField(
                value = uiState.value.password,
                onValueChange = { loginViewModel.updatePassword(it) },
                label = { Text(text = "Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    autoCorrect = true
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(5.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(onClick = {
                    scope.launch {
                        try {
                            val response = LoginApi.retrofitService.login(
                                LoginDTO(
                                    username = uiState.value.username.text,
                                    password = registerHash(uiState.value.username.text, uiState.value.password.text)
                                )
                            )
                            loginViewModel.setToken(response.token)

                            Toast.makeText(
                                context, "Login Success" + response.token,
                                Toast.LENGTH_SHORT
                            ).show()
                        } catch (e: Exception) {
                            Log.e("Sign In", e.toString())
                            Toast.makeText(
                                context, "网络错误",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                }, modifier = Modifier.weight(1f)) {
                    Text(text = "Sign in")
                }
            }
        }
    }
}