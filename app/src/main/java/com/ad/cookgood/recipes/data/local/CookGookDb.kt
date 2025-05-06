package com.ad.cookgood.recipes.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ad.cookgood.recipes.data.local.ingredient.IngredientDao
import com.ad.cookgood.recipes.data.local.ingredient.LocalIngredient
import com.ad.cookgood.recipes.data.local.instruction.InstructionDao
import com.ad.cookgood.recipes.data.local.instruction.LocalInstruction
import com.ad.cookgood.recipes.data.local.recipe.LocalRecipe
import com.ad.cookgood.recipes.data.local.recipe.RecipeDao

@Database(
   entities = [LocalRecipe::class, LocalIngredient::class, LocalInstruction::class],
   version = 6,
   exportSchema = true,
)
abstract class CookGookDb : RoomDatabase() {

   abstract val recipeDao: RecipeDao
   abstract val ingredientDao: IngredientDao
   abstract val instructionDao: InstructionDao
}