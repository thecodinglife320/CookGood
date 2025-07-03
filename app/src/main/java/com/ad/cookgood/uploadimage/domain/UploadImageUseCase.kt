package com.ad.cookgood.uploadimage.domain

import io.appwrite.models.InputFile
import javax.inject.Inject

class UploadImageUseCase @Inject constructor(
   private val uploadImageRepository: UploadImageRepository
) {
   suspend operator fun invoke(file: InputFile, fileId: String, bucketId: String) =
      uploadImageRepository.upload(file, fileId, bucketId)
}