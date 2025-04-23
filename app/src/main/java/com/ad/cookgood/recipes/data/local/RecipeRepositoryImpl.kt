package com.ad.cookgood.recipes.data.local

import com.ad.cookgood.recipes.data.local.ingredient.IngredientDao
import com.ad.cookgood.recipes.data.local.instruction.InstructionDao
import com.ad.cookgood.recipes.data.local.recipe.RecipeDao
import com.ad.cookgood.recipes.domain.RecipeRepository
import com.ad.cookgood.recipes.domain.model.Ingredient
import com.ad.cookgood.recipes.domain.model.Instruction
import com.ad.cookgood.recipes.domain.model.Recipe
import com.ad.cookgood.recipes.domain.model.toLocal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
   private val recipeDao: RecipeDao,
   private val ingredientDao: IngredientDao,
   private val instructionDao: InstructionDao,
) : RecipeRepository {

   override suspend fun insertRecipe(recipe: Recipe) =
      withContext(Dispatchers.IO) {
         recipeDao.insertRecipe(recipe.toLocal())
      }

   override suspend fun insertIngredient(ingredient: Ingredient, recipeId: Long) {
      withContext(Dispatchers.IO) {
         ingredientDao.insertIngredient(ingredient.toLocal(recipeId))
      }
   }

   override suspend fun insertInstruction(instruction: Instruction, recipeId: Long) {
      withContext(Dispatchers.IO) {
         instructionDao.insertInstruction(instruction.toLocal(recipeId))
      }
   }
}