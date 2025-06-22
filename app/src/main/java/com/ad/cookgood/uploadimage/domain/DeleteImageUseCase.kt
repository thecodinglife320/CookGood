package com.ad.cookgood.uploadimage.domain

import javax.inject.Inject

class DeleteImageUseCase @Inject constructor(
   private val uploadImageRepository: UploadImageRepository
) {
   suspend operator fun invoke(fileId: String) = uploadImageRepository.delete(fileId)
}