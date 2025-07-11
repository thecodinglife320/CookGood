package com.ad.cookgood.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.util.Log
import com.ad.cookgood.uploadimage.data.FileDetails
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.Normalizer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.regex.Pattern

fun isNetworkAvailable(context: Context): Boolean {
   val connectivityManager =
      context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
   val network = connectivityManager.activeNetwork ?: return false
   val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
   return when {
      activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
      activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
      activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
      else -> false
   }
}

suspend fun getFileDetailsFromUri(context: Context, uri: Uri): FileDetails? {
   var filename: String?
   var mimeType: String?
   var inputStream: InputStream? = null
   var byteArrayOutputStream: ByteArrayOutputStream? = null

   try {
      // 1. Get MIME type
      mimeType = context.contentResolver.getType(uri)
      Log.d("FileDetails", "MIME Type: $mimeType")

      // 2. generate filename depend on timestamp
      val currentDateTime = LocalDateTime.now()
      val formatter = DateTimeFormatter.ofPattern(
         "yyyyMMdd_HHmmssSSS",
         Locale.ENGLISH
      )
      val formattedDateTime = currentDateTime.format(formatter)
      filename = "image_$formattedDateTime"
      Log.d("FileDetails", "Filename: $filename")

      // 3. Get byte array
      inputStream = context.contentResolver.openInputStream(uri)
      if (inputStream == null) {
         Log.e("FileDetails", "Could not open InputStream for URI: $uri")
         return null
      }

      byteArrayOutputStream = ByteArrayOutputStream()
      val buffer = ByteArray(4 * 1024) // 4KB buffer
      var read: Int
      while (inputStream.read(buffer).also { read = it } != -1) {
         byteArrayOutputStream.write(buffer, 0, read)
      }
      byteArrayOutputStream.flush()
      val bytes = byteArrayOutputStream.toByteArray()
      Log.d("FileDetails", "Byte array size: ${bytes.size}")

      return FileDetails(bytes, filename, mimeType)

   } catch (e: IOException) {
      Log.e("FileDetails", "IOException while getting file details: ${e.message}", e)
      return null
   } catch (e: SecurityException) {
      Log.e("FileDetails", "SecurityException while getting file details: ${e.message}", e)
      return null
   } catch (e: Exception) {
      Log.e("FileDetails", "Unexpected error while getting file details: ${e.message}", e)
      return null
   } finally {
      try {
         inputStream?.close()
         byteArrayOutputStream?.close()
      } catch (e: IOException) {
         Log.e("FileDetails", "Error closing streams: ${e.message}", e)
      }
   }
}

fun normalizeStringForSearch(input: String?): String {
   if (input == null) return ""

   // 1. Chuyển sang chữ thường
   var normalized = input.lowercase()

   // 2. Loại bỏ dấu bằng cách chuyển sang dạng NFD (Normalization Form D)
   //    và loại bỏ các ký tự dấu (non-spacing marks)
   normalized = Normalizer.normalize(normalized, Normalizer.Form.NFD)
   val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
   normalized = pattern.matcher(normalized).replaceAll("")

   // 3. Xử lý đặc biệt cho chữ 'đ' (vì nó không bị loại bỏ bởi bước trên)
   normalized = normalized.replace('đ', 'd')

   return normalized
}

fun getAppWriteFileViewUrl(
   bucketId: String,
   fileId: String,
   projectId: String,
   mode: String = "admin"
) =
   "https://fra.cloud.appwrite.io/v1/storage/buckets/$bucketId/files/$fileId/view?project=$projectId&mode=$mode"

