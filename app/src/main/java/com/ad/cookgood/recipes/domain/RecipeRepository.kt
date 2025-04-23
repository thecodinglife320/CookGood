package com.ad.cookgood.recipes.domain

import com.ad.cookgood.recipes.domain.model.Ingredient
import com.ad.cookgood.recipes.domain.model.Instruction
import com.ad.cookgood.recipes.domain.model.Recipe

interface RecipeRepository {
   suspend fun insertRecipe(recipe: Recipe): Long
   suspend fun insertIngredient(ingredient: Ingredient, recipeId: Long)
   suspend fun insertInstruction(instruction: Instruction, recipeId: Long)
}