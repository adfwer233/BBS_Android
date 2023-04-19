package com.example.campus_bbs.ui.components

import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraProvider
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.campus_bbs.ui.model.CameraViewModel

@Composable
@Preview
fun SimpleCameraScreen(
    modifier: Modifier = Modifier,
    cameraViewModel: CameraViewModel = viewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    var cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
    var cameraLensFacing by remember {
        mutableStateOf(CameraSelector.LENS_FACING_FRONT)
    }

    val preview = PreviewView(context).apply {
        this.scaleType = scaleType
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        // Preview is incorrectly scaled in Compose on some devices without this
        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
    }

    Log.e("simple camera screen", "Drawing")

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        AndroidView(
            factory = { ctx ->
                val executor = ContextCompat.getMainExecutor(ctx)
                var cameraSelector = CameraSelector.Builder().requireLensFacing(cameraLensFacing).build()

                cameraProviderFuture.addListener({
                    cameraProvider = cameraProviderFuture.get()
                    cameraViewModel.imageManager.bindPreview(lifecycleOwner, preview,
                        cameraProvider as ProcessCameraProvider, cameraSelector)
                }, executor)
                preview
            },
            modifier = Modifier.fillMaxSize(),
        )
        Column {
            Button(onClick = { cameraViewModel.imageManager.takePhoto(context) }) {
                Text(text = "Take photo $cameraLensFacing")
            }
            Button(onClick = {
                if (cameraLensFacing == CameraSelector.LENS_FACING_FRONT) {
                    cameraLensFacing = CameraSelector.LENS_FACING_BACK
                } else {
                    cameraLensFacing = CameraSelector.LENS_FACING_FRONT
                }

                cameraProvider?.let {
                    cameraProvider.unbindAll()
                    var cameraSelector = CameraSelector.Builder().requireLensFacing(cameraLensFacing).build()
                    cameraViewModel.imageManager.bindPreview(lifecycleOwner, preview, cameraProvider, cameraSelector)
                }

                Log.e("fasdf",  cameraLensFacing.toString())
            }) {
                Text(text = "Flip Camera")
            }
        }

    }

}
