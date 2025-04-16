package com.ad.cookgood.myrecipes.data.local.instruction

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ad.cookgood.myrecipes.data.local.recipe.LocalRecipe

@Entity(
   tableName = "instructions",
   foreignKeys = [
      ForeignKey(
         entity = LocalRecipe::class,
         parentColumns = ["id"],
         childColumns = ["recipe_id"],
         onDelete = ForeignKey.Companion.CASCADE,
         onUpdate = ForeignKey.Companion.CASCADE
      )
   ], indices = [Index("recipe_id")]
)
data class LocalInstruction(
   @PrimaryKey(autoGenerate = true) val id: Long = 0,
   val name: String,
   @ColumnInfo("step_number") val strepNumber: Int,
   @ColumnInfo(name = "recipe_id") val recipeId: Long,
)