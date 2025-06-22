package com.ad.cookgood.uploadimage.domain

import io.appwrite.models.File
import io.appwrite.models.FileList
import io.appwrite.models.InputFile

interface UploadImageRepository {
   suspend fun upload(file: InputFile, fileId: String): Result<File>
   suspend fun delete(fileId: String): Result<Boolean>
   suspend fun listFile(): Result<FileList>
}