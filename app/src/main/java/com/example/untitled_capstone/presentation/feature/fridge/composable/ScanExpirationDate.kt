package com.example.untitled_capstone.presentation.feature.fridge.composable

import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.example.untitled_capstone.R
import com.example.untitled_capstone.core.util.Dimens
import com.example.untitled_capstone.ui.theme.CustomTheme
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions.DEFAULT_OPTIONS

@kotlin.OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanExpirationDate(navController: NavHostController) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var detectedText by remember { mutableStateOf<String?>(null) }
    Scaffold(
        containerColor = CustomTheme.colors.onSurface,
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(horizontal = Dimens.topBarPadding),
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.chevron_left),
                            tint = CustomTheme.colors.iconSelected,
                            contentDescription = "back",
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CustomTheme.colors.onSurface
                )
            )
        },
    ){ innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        val preview = Preview.Builder().build().also {
                            it.surfaceProvider = previewView.surfaceProvider
                        }

                        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                        val imageAnalysis = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                            .also {
                                it.setAnalyzer(ContextCompat.getMainExecutor(ctx)) { imageProxy ->
                                    processImageProxy(imageProxy) { extractedText ->
                                        val expirationDate = extractExpirationDate(extractedText)
                                        if (detectedText == null && expirationDate != null) {  // ✅ 첫 값만 저장
                                            detectedText = expirationDate
                                            navController.previousBackStackEntry?.savedStateHandle?.set("date", detectedText)
                                            navController.popBackStack()
                                        }
                                    }
                                }
                            }

                        cameraProvider.unbindAll() // 기존 바인딩 해제 (중복 방지)
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner, cameraSelector, preview, imageAnalysis
                        )

                    }, ContextCompat.getMainExecutor(ctx))

                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )
//            if(showDialog){
//                AlertDialog(
//                    onDismissRequest = { showDialog = false },
//                    title = {
//                        Text(
//                            text = "유통기한 정보",
//                            style = CustomTheme.typography.title1,
//                            color = CustomTheme.colors.textPrimary,
//                        )
//                    },
//                    text = {
//                        Text(
//                            text = detectedText ?: "",
//                            style = CustomTheme.typography.body1,
//                            color = CustomTheme.colors.textPrimary,
//                        )
//                    },
//                    dismissButton = {
//
//                    },
//                    confirmButton = {
//                        IconButton(
//                            onClick = { showDialog = false }
//                        ) {
//                            Icon(
//                                imageVector = ImageVector.vectorResource(R.drawable.close),
//                                tint = CustomTheme.colors.iconSelected,
//                                contentDescription = "close",
//                            )
//                        }
//                    },
//                    modifier = Modifier.background(CustomTheme.colors.surface)
//                )
//            }
        }
    }
}

private fun extractExpirationDate(text: String): String? {
    val regex = Regex("[:\\s]*(\\d{4}[-./]\\d{1,2}[-./]\\d{1,2})")
    val match = regex.find(text)
    return match?.groupValues?.get(1)
}

@OptIn(ExperimentalGetImage::class)
fun processImageProxy(imageProxy: ImageProxy, onTextExtracted: (String) -> Unit) {
    val mediaImage = imageProxy.image
    if (mediaImage != null) {
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        val recognizer = TextRecognition.getClient(DEFAULT_OPTIONS)

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                val extractedText = visionText.text
                onTextExtracted(extractedText) // 콜백으로 전달
            }
            .addOnFailureListener {
                Log.e("ML Kit", "Text recognition failed", it)
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }
}