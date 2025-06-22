package com.ad.cookgood.uploadimage.domain

import javax.inject.Inject

class ListImageUseCase @Inject constructor(
   private val uploadImageRepository: UploadImageRepository
) {
   suspend operator fun invoke() = uploadImageRepository.listFile()
}