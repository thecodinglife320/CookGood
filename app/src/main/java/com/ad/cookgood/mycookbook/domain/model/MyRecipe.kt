package com.ad.cookgood.mycookbook.domain.model

import com.ad.cookgood.mycookbook.presentaion.state.MyRecipeUiState
import com.ad.cookgood.recipes.domain.model.Recipe

data class MyRecipe(
   var id: Long = 0,
   override val title: String,
   override val brief: String,
   override val serving: Int,
   override val cookTime: Int,
) : Recipe(title, brief, serving, cookTime)

fun MyRecipe.toUiState() =
   MyRecipeUiState(
      id = id,
      title = title
   )


