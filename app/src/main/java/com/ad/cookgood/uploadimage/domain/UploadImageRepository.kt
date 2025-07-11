package com.ad.cookgood.uploadimage.domain

import io.appwrite.models.File
import io.appwrite.models.InputFile

interface UploadImageRepository {
   suspend fun upload(file: InputFile, fileId: String, bucketId: String): Result<File>
}