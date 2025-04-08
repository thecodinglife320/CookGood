package com.ad.cookgood.myrecipes.domain.model

data class Recipe(
   val name: String = "",
   val brief: String = "",
   val serving: Int = 0,
   val cookTime: Int = 0,
)