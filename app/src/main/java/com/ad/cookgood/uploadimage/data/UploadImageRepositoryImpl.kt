package com.ad.cookgood.uploadimage.data

import com.ad.cookgood.BuildConfig
import com.ad.cookgood.uploadimage.domain.UploadImageRepository
import io.appwrite.Query
import io.appwrite.models.File
import io.appwrite.models.FileList
import io.appwrite.models.InputFile
import io.appwrite.services.Storage
import javax.inject.Inject

class UploadImageRepositoryImpl @Inject constructor(
   private val storage: Storage,
) : UploadImageRepository {

   override suspend fun upload(file: InputFile, fileId: String): Result<File> {
      return try {
         val file = storage.createFile(
            bucketId = BuildConfig.APPWRITE_BUCKET_ID,
            fileId = fileId,
            file = file,
         )
         Result.success(file)
      } catch (e: Exception) {
         Result.failure(e)
      }
   }

   override suspend fun delete(fileId: String) = try {
      storage.deleteFile(
         bucketId = BuildConfig.APPWRITE_BUCKET_ID,
         fileId = fileId,
      )
      Result.success(true)
   } catch (e: Exception) {
      Result.failure(e)
   }

   override suspend fun listFile(): Result<FileList> {
      return try {
         val result = storage.listFiles(
            bucketId = BuildConfig.APPWRITE_BUCKET_ID,
            queries = listOf(
               Query.equal("mimeType", "image/jpeg")
            )
         )
         Result.success(result)
      } catch (e: Exception) {
         Result.failure(e)
      }
   }
}