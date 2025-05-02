package com.ad.cookgood.mycookbook.domain.usecase

import com.ad.cookgood.mycookbook.domain.MyCookBookRepository
import javax.inject.Inject

class GetMyCookBookUseCase @Inject constructor(
   private val myCookBookRepository: MyCookBookRepository,
) {
   suspend operator fun invoke() = myCookBookRepository.getMyCookBook()
}

