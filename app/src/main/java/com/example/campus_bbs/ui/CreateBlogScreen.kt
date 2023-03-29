package com.example.campus_bbs.ui

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.campus_bbs.ui.model.CreateBlogViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun CreateBlogScreen(
    modifier: Modifier = Modifier,
    mainAppNavController: NavHostController = rememberNavController(),
    createBlogViewModel: CreateBlogViewModel = viewModel()
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Create New Blog") },
                navigationIcon = {
                    Button(onClick = { mainAppNavController.navigateUp() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Row {
                        IconButton(onClick = { createBlogViewModel.saveState() }) {
                            Icon(imageVector = Icons.Filled.Build, contentDescription = "")
                        }
                        
                        IconButton(onClick = { mainAppNavController.navigateUp() }) {
                            Row {
                                Icon(imageVector = Icons.Filled.Send, contentDescription = "test")
                            }
                        }
                    }
                }
            )
        }
    ) { contentPadding ->
        editBlog(modifier = modifier.padding(contentPadding), createBlogViewModel=createBlogViewModel)
    }
}

@Composable
fun editBlog(
    modifier: Modifier = Modifier,
    createBlogViewModel: CreateBlogViewModel = viewModel()
) {
    var titleText by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(createBlogViewModel.uiState.value.savedTitleText))
    }

    var contentText by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(createBlogViewModel.uiState.value.savedContentText))
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),

    ) {
        TextField(
            label = { Text(text = "Title") },
            value = titleText,
            onValueChange = {
                titleText = it
                createBlogViewModel.updateTitleText(it.text) },
            modifier=Modifier.fillMaxWidth()
        )
        TextField(
            label = { Text(text = "Content") },
            value = contentText,
            onValueChange = {
                contentText = it
                createBlogViewModel.updateContentText(it.text) },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        )
    }

}