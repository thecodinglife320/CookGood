package com.ad.cookgood.mycookbook.data

import com.ad.cookgood.mycookbook.domain.MyRecipeRepository
import com.ad.cookgood.mycookbook.domain.model.MyRecipe
import com.ad.cookgood.mycookbook.domain.model.toLocal
import com.ad.cookgood.recipes.data.local.ingredient.IngredientDao
import com.ad.cookgood.recipes.data.local.ingredient.toDomain
import com.ad.cookgood.recipes.data.local.instruction.InstructionDao
import com.ad.cookgood.recipes.data.local.instruction.toDomain
import com.ad.cookgood.recipes.data.local.recipe.RecipeDao
import com.ad.cookgood.recipes.data.local.recipe.toDomain
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MyRecipeRepositoryImpl @Inject constructor(
   private val recipeDao: RecipeDao,
   private val ingredientDao: IngredientDao,
   private val instructionDao: InstructionDao
) : MyRecipeRepository {
   override fun getMyRecipeById(recipeId: Long) =
      recipeDao.getRecipeById(recipeId).map {
         it?.let {
            MyRecipe(it.id, it.toDomain())
         }
      }

   override fun getInstructionsByRecipeId(recipeId: Long) =
      instructionDao.getInstructionsByRecipeId(recipeId).map {
         it.map {
            it.toDomain()
         }

      }

   override fun getIngredientsByRecipeId(recipeId: Long) =
      ingredientDao.getIngredientsByRecipeId(recipeId).map {
         it.map {
            it.toDomain()
         }
      }

   override fun getMyRecipes() =
      recipeDao.getAllRecipe().map {
         it.map {
            MyRecipe(
               id = it.id,
               recipe = it.toDomain()
            )
         }
      }

   override suspend fun deleteMyRecipe(myRecipe: MyRecipe) =
      recipeDao.delete(myRecipe.toLocal())

   override suspend fun updateMyRecipe(myRecipe: MyRecipe) {
      recipeDao.update(myRecipe.toLocal())
   }

}