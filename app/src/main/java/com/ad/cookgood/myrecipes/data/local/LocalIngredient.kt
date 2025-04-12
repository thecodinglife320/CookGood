package com.ad.cookgood.myrecipes.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
   tableName = "ingredients",
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
data class LocalIngredient(
   @PrimaryKey(autoGenerate = true) val id: Int = 0,
   val name: String,
   @ColumnInfo(name = "recipe_id") val recipeId: Int,
)