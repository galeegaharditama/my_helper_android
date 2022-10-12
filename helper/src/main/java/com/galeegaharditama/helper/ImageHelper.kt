package com.galeegaharditama.helper

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.destination
import id.zelory.compressor.constraint.quality
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

object ImageHelper {
  fun createFolderWithNomedia(folder: File) {
    val file = File("${folder.path}/.nomedia")
    Timber.d(folder.path + "/.nomedia " + file)
    try {
      file.createNewFile()
    } catch (e: IOException) {
      e.printStackTrace()
    }
  }

  suspend fun compressFile(
    context: Context,
    path: String,
    imageFile: File,
    compressQuality: Int = 80,
  ): File? {
    val folder = File(path)
    var success = true
    if (!folder.exists()) {
      success = folder.mkdirs()
    }
    if (success) {
      try {
        return Compressor.compress(context, imageFile) {
          quality(compressQuality)
          destination(File(folder, imageFile.name))
        }
      } catch (e: Exception) {
        Log.getStackTraceString(e)
      }
    } else {
      Timber.e("Cannot Create Folder")
    }
    return null
  }

  @Throws(IOException::class)
  fun addPictureToGallery(context: Context, file: File?) {
    val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
    file?.let {
      val contentUri = Uri.fromFile(file)
      mediaScanIntent.data = contentUri
    }
    context.sendBroadcast(mediaScanIntent)
  }

  fun convertStringToBitmap(imageString: String): Bitmap {
    val decodedString = Base64.decode(imageString, Base64.NO_WRAP)
    return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
  }

  fun convertFileToBitmap(fileImage: File): Bitmap {
    return imgFileToBitmap(fileImage)
  }

  fun convertFileToBitmap(context: Context, fileImage: File): Bitmap {
    return imgFileToBitmap(context, fileImage)
  }

  fun convertImgToString(fileImage: File, compressQuality: Int = 100): String {
    val bitmap = imgFileToBitmap(fileImage)
    return bitmapToString(bitmap, compressQuality)
  }

  fun convertImgToString(context: Context, fileImage: File, compressQuality: Int = 100): String {
    val bitmap = imgFileToBitmap(context, fileImage)
    return bitmapToString(bitmap, compressQuality)
  }

  fun convertImgToString(bitmap: Bitmap, compressQuality: Int = 100): String {
    return bitmapToString(bitmap, compressQuality)
  }

  private fun bitmapToString(bitmap: Bitmap, compressQuality: Int): String {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, outputStream)
    val byte = outputStream.toByteArray()
    return Base64.encodeToString(byte, Base64.NO_WRAP)
  }

  private fun imgFileToBitmap(fileImage: File): Bitmap {
    val filePath = fileImage.path
    return BitmapFactory.decodeFile(filePath)
  }

  private fun imgFileToBitmap(context: Context, fileImage: File): Bitmap {
    return MediaStore.Images.Media.getBitmap(context.contentResolver, Uri.fromFile(fileImage))
  }
}
