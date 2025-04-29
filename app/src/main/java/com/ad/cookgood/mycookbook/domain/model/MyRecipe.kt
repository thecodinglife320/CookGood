package com.ad.cookgood.mycookbook.domain.model

import com.ad.cookgood.mycookbook.presentaion.state.MyRecipeUiState

data class MyRecipe(
   val id: Long = 0,
   val title: String,
   val brief: String,
   val serving: Int,
   val cookTime: Int,
)

fun MyRecipe.toUiState() =
   MyRecipeUiState(
      id = id,
      title = title
   )


