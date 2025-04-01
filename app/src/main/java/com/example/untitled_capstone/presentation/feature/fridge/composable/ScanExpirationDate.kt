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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.untitled_capstone.ui.theme.CustomTheme
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions.DEFAULT_OPTIONS

@Composable
fun ScanExpirationDate() {
    val lifecycleOwner = LocalLifecycleOwner.current
    var detectedText by remember { mutableStateOf("유통기한을 인식하세요") }

    Box(modifier = Modifier.fillMaxSize()) {
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
                                    detectedText = expirationDate ?: "유통기한 정보 없음"
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

        // 감지된 텍스트를 화면에 표시
        Text(
            text = detectedText,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(CustomTheme.colors.surface),
            color = CustomTheme.colors.textPrimary,
        )
    }
}

private fun extractExpirationDate(text: String): String? {
    val regex = Regex("(유통기한|EXP|Expiry Date|소비기한)[:\\s]*(\\d{4}[-./]\\d{1,2}[-./]\\d{1,2})")
    val match = regex.find(text)
    return match?.groupValues?.get(2) // 날짜 부분만 반환
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