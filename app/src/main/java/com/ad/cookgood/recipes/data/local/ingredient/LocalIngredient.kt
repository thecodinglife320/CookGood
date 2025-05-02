package com.ad.cookgood.recipes.data.local.ingredient

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ad.cookgood.recipes.data.local.recipe.LocalRecipe
import com.ad.cookgood.recipes.domain.model.Ingredient

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
   @PrimaryKey(autoGenerate = true) val id: Long = 0,
   val name: String,
   @ColumnInfo(name = "recipe_id") val recipeId: Long,
)

fun LocalIngredient.toDomain() =
   Ingredient(
      name = name
   )