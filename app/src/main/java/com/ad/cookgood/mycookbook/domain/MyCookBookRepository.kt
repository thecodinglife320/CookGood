package com.ad.cookgood.mycookbook.domain

import com.ad.cookgood.mycookbook.domain.model.MyCookBook

interface MyCookBookRepository {
   suspend fun getMyCookBook(): MyCookBook
}