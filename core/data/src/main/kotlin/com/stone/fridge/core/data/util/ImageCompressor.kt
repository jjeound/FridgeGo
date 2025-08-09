package com.stone.fridge.core.data.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream

object ImageCompressor {
    fun compressImage(
        context: Context,
        originalFile: File,
        quality: Int = 70
    ): File {
        val bitmap = BitmapFactory.decodeFile(originalFile.absolutePath)
        val compressedFile = File.createTempFile("compressed_", ".jpg", context.cacheDir)
        FileOutputStream(compressedFile).use { output ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, output)
        }
        return compressedFile
    }
}