package com.ad.cookgood.mycookbook.data

import com.ad.cookgood.mycookbook.domain.MyRecipeRepository
import com.ad.cookgood.mycookbook.domain.model.MyRecipe
import com.ad.cookgood.recipes.data.local.ingredient.IngredientDao
import com.ad.cookgood.recipes.data.local.ingredient.toDomain
import com.ad.cookgood.recipes.data.local.instruction.InstructionDao
import com.ad.cookgood.recipes.data.local.instruction.toDomain
import com.ad.cookgood.recipes.data.local.recipe.RecipeDao
import com.ad.cookgood.recipes.data.local.recipe.toDomain
import javax.inject.Inject

class MyRecipeRepositoryImpl @Inject constructor(
   private val recipeDao: RecipeDao,
   private val ingredientDao: IngredientDao,
   private val instructionDao: InstructionDao
) : MyRecipeRepository {
   override suspend fun getMyRecipeById(recipeId: Long) =
      run {
         recipeDao.getRecipeById(recipeId)?.let {
            MyRecipe(it.id, it.toDomain())
         }
      }

   override suspend fun getInstructionsByRecipeId(recipeId: Long) =
      run {
         instructionDao.getInstructionsByRecipeId(recipeId).map {
            it.toDomain()
         }
      }

   override suspend fun getIngredientsByRecipeId(recipeId: Long) =
      run {
         ingredientDao.getIngredientsByRecipeId(recipeId).map {
            it.toDomain()
         }
      }
}