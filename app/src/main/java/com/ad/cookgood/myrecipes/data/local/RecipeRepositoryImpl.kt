package com.ad.cookgood.myrecipes.data.local

import com.ad.cookgood.myrecipes.data.local.ingredient.IngredientDao
import com.ad.cookgood.myrecipes.data.local.instruction.InstructionDao
import com.ad.cookgood.myrecipes.data.local.recipe.RecipeDao
import com.ad.cookgood.myrecipes.domain.model.Ingredient
import com.ad.cookgood.myrecipes.domain.model.Instruction
import com.ad.cookgood.myrecipes.domain.model.Recipe
import com.ad.cookgood.myrecipes.domain.usecase.RecipeRepository
import com.ad.cookgood.myrecipes.toLocalIngredient
import com.ad.cookgood.myrecipes.toLocalRecipe
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
         recipeDao.insertRecipe(recipe.toLocalRecipe())
      }

   override suspend fun insertIngredient(ingredient: Ingredient, recipeId: Long) {
      withContext(Dispatchers.IO) {
         ingredientDao.insertIngredient(ingredient.toLocalIngredient(recipeId))
      }
   }

   override suspend fun insertInstruction(instruction: Instruction, recipeId: Long) {
      withContext(Dispatchers.IO) {
         instructionDao.insertInstruction(instruction.toLocal(recipeId))
      }
   }
}