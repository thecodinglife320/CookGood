package com.ad.cookgood.uploadimage.data

import com.ad.cookgood.uploadimage.domain.UploadImageRepository
import io.appwrite.models.File
import io.appwrite.models.InputFile
import io.appwrite.services.Storage
import javax.inject.Inject

class UploadImageRepositoryImpl @Inject constructor(
   private val storage: Storage,
) : UploadImageRepository {

   override suspend fun upload(file: InputFile, fileId: String, bucketId: String): Result<File> {
      return try {
         val file = storage.createFile(
            bucketId = bucketId,
            fileId = fileId,
            file = file,
         )
         Result.success(file)
      } catch (e: Exception) {
         Result.failure(e)
      }
   }
}