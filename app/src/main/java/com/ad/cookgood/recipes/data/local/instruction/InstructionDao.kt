package com.ad.cookgood.recipes.data.local.instruction

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface InstructionDao {
   @Insert
   suspend fun insertInstruction(localInstruction: LocalInstruction): Long

   @Query("select * from instructions where id = :instructionId")
   suspend fun getInstructionById(instructionId: Long): LocalInstruction?

   @Query("select * from instructions")
   suspend fun getAllInstruction(): List<LocalInstruction>

   @Query("select * from instructions where recipe_id=:recipeId")
   fun getInstructionsByRecipeId(recipeId: Long): List<LocalInstruction>
}