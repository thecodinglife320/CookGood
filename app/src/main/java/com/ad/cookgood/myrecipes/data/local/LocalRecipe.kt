package com.ad.cookgood.myrecipes.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class LocalRecipe(
   @PrimaryKey(autoGenerate = true) val id: Int = 0,
   val title: String,
   val brief: String,
   val servings: Int,
   @ColumnInfo(name = "cook_time") val cookTime: Int,
)

@Entity(
   tableName = "ingredients",
   foreignKeys = [
      ForeignKey(
         entity = LocalRecipe::class,
         parentColumns = ["id"],
         childColumns = ["recipe_id"],
         onDelete = CASCADE,
         onUpdate = CASCADE
      )
   ]
)
data class LocalIngredient(
   @PrimaryKey(autoGenerate = true) val id: Int = 0,
   val name: String,
   @ColumnInfo(name = "recipe_id") val recipeId: Int,
)
