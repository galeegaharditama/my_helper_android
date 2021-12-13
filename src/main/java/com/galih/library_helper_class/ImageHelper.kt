package com.galih.library_helper_class

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.Base64
import android.util.Log
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.destination
import id.zelory.compressor.constraint.quality
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

object ImageHelper {
    fun createFolderWithNomedia(folder: File) {
        val file = File("${folder.path}/.nomedia")
        Log.d(javaClass.simpleName, "${folder.path}/.nomedia $file")
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
            Log.e(javaClass.simpleName, "Cannot Create Folder")
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

    fun drawMultilineTextToBitmap(
        context: Context,
        bitmap: Bitmap,
        text: String,
        colorText: Int
    ): Bitmap {
        val scale = context.resources.displayMetrics.density

        var bitmapConfig = bitmap.config
        if (bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888
        }

        val mBitmap = bitmap.copy(bitmapConfig, true)

        val canvas = Canvas(mBitmap)
        // new antialiased Paint
        val paint = TextPaint(Paint(Paint.ANTI_ALIAS_FLAG))
        // text color - #3D3D3D
        paint.color = colorText
        // text size in pixels
        paint.textSize = 12 * scale
        // text shadow
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE)

        // set text width to canvas width minus 16dp padding
        val textWidth = (canvas.width - 16 * scale)
        val textLayout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val sb = StaticLayout.Builder.obtain(text, 0, text.length, paint, textWidth.toInt())
                .setAlignment(Layout.Alignment.ALIGN_CENTER)
                .setLineSpacing(0.0f, 1.0f)
                .setIncludePad(false)
            sb.build()
        } else {
            StaticLayout(
                text,
                paint,
                textWidth.toInt(),
                Layout.Alignment.ALIGN_CENTER,
                1.0f,
                0.0f,
                false
            )
        }
        val textHeight = textLayout.height
        val x = (mBitmap.width - textWidth) / 2
        val y = (mBitmap.height - textHeight) * 8 / 9
        canvas.save()
        canvas.translate(x, y.toFloat())
        textLayout.draw(canvas)
        canvas.restore()
        return mBitmap
    }
}
