package com.ad.cookgood.myrecipes

import com.ad.cookgood.myrecipes.data.local.LocalRecipe
import com.ad.cookgood.myrecipes.domain.model.Recipe
import com.ad.cookgood.myrecipes.presentation.state.RecipeUiState

fun Recipe.toLocalRecipe() =
   LocalRecipe(
      title = this.title,
      brief = this.brief,
      servings = this.serving,
      cookTime = this.cookTime
   )

fun LocalRecipe.toDomainRecipe() =
   Recipe(
      title = this.title,
      brief = this.brief,
      serving = this.servings,
      cookTime = this.cookTime
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

