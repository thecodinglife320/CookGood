package com.ad.cookgood.mycookbook.domain

import com.ad.cookgood.mycookbook.domain.model.MyRecipe
import com.ad.cookgood.recipes.domain.model.Ingredient
import com.ad.cookgood.recipes.domain.model.Instruction
import kotlinx.coroutines.flow.Flow

interface MyRecipeRepository {
   suspend fun getMyRecipeById(recipeId: Long): MyRecipe?
   suspend fun getInstructionsByRecipeId(recipeId: Long): List<Instruction>
   suspend fun getIngredientsByRecipeId(recipeId: Long): List<Ingredient>
   fun getMyRecipes(): Flow<List<MyRecipe>>
}