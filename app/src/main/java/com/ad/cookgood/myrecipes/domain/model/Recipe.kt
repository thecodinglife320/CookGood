package com.ad.cookgood.myrecipes.domain.model

data class Recipe(
   val title: String = "",
   val brief: String = "",
   val serving: Int = 0,
   val cookTime: Int = 0,
)