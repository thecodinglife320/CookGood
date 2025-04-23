package com.ad.cookgood.recipes.data.local.recipe

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ad.cookgood.recipes.domain.model.Recipe

@Entity(tableName = "recipes")
data class LocalRecipe(
   @PrimaryKey(autoGenerate = true) val id: Long = 0,
   val title: String,
   val brief: String,
   val servings: Int,
   @ColumnInfo(name = "cook_time") val cookTime: Int,
)

fun LocalRecipe.toDomain() =
   Recipe(
      title = title,
      brief = brief,
      serving = servings,
      cookTime = cookTime,
   )