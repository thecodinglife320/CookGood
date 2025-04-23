package com.ad.cookgood.recipes.presentation.state

import com.ad.cookgood.recipes.domain.model.Recipe

data class RecipeUiState(
   val title: String = "",
   val brief: String = "",
   val servings: String = "",
   val cookTimeMinutes: String = "",
   val cookTimeHours: String = "",
)

fun RecipeUiState.toDomain() =
   let {

      val servingsInt = servings.toIntOrNull() ?: 0
      val minutesInt = cookTimeMinutes.toIntOrNull() ?: 0
      val hoursInt = cookTimeHours.toIntOrNull() ?: 0

      Recipe(
         title = title,
         brief = brief,
         serving = servingsInt,
         cookTime = hoursInt * 60 + minutesInt
      )
   }