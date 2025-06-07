package com.ad.cookgood.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.core.net.toUri
import com.ad.cookgood.uploadimage.data.FileDetails
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

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
   var filename: String? = null
   var mimeType: String? = null
   var inputStream: InputStream? = null
   var byteArrayOutputStream: ByteArrayOutputStream? = null

   try {
      // 1. Get MIME type
      mimeType = context.contentResolver.getType(uri)
      Log.d("FileDetails", "MIME Type: $mimeType")

      // 2. Get filename (optional, might not always be available or accurate)
      context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
         if (cursor.moveToFirst()) {
            val displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (displayNameIndex != -1) {
               filename = cursor.getString(displayNameIndex)
               Log.d("FileDetails", "Filename: $filename")
            }
         }
      }
      // Fallback if display name is not found (you can generate a name if needed)
      if (filename == null) {
         filename = "upload_${System.currentTimeMillis()}" + (mimeType?.let {
            "." + it.substringAfterLast('/')
         } ?: "")
         Log.d("FileDetails", "Generated Filename: $filename")
      }

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

fun getAppWriteFileViewUrl(
   bucketId: String,
   fileId: String,
   projectId: String,
   mode: String = "admin"
) =
   "https://fra.cloud.appwrite.io/v1/storage/buckets/$bucketId/files/$fileId/view?project=$projectId&mode=$mode".toUri()

fun extractFileIdWithUriClass(uri: Uri): String {
   val pathSegments = uri.pathSegments // List of path segments
   // The structure is like: ..., "storage", "buckets", BUCKET_ID, "files", FILE_ID, "view"
   // So, if "files" is found, the next segment should be the FILE_ID.
   val filesIndex = pathSegments.indexOf("files")
   return if (filesIndex != -1 && filesIndex + 1 < pathSegments.size) {
      pathSegments[filesIndex + 1]
   } else ""
}

