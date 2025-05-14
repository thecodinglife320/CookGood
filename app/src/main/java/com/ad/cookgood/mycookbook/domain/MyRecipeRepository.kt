package com.ad.cookgood.mycookbook.domain

import com.ad.cookgood.mycookbook.domain.model.IngredientEdit
import com.ad.cookgood.mycookbook.domain.model.InstructionEdit
import com.ad.cookgood.mycookbook.domain.model.MyRecipe
import com.ad.cookgood.recipes.domain.model.Ingredient
import com.ad.cookgood.recipes.domain.model.Instruction
import kotlinx.coroutines.flow.Flow

interface MyRecipeRepository {
   fun getMyRecipeById(recipeId: Long): Flow<MyRecipe?>
   fun getInstructionsByRecipeId(recipeId: Long): Flow<List<Instruction>>
   fun getIngredientsByRecipeId(recipeId: Long): Flow<List<Ingredient>>
   fun getMyRecipes(): Flow<List<MyRecipe>>
   suspend fun deleteMyRecipe(myRecipe: MyRecipe): Int
   suspend fun updateMyRecipe(myRecipe: MyRecipe)
   suspend fun getIngredientEdits(recipeId: Long): Flow<List<IngredientEdit>>
   suspend fun getInstructionEdits(recipeId: Long): Flow<List<InstructionEdit>>
   suspend fun deleteIngredient(ingredientEdit: IngredientEdit, recipeId: Long)
   suspend fun deleteInstruction(instructionEdit: InstructionEdit, recipeId: Long)
}