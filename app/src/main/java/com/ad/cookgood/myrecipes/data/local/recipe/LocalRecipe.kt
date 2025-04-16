package com.ad.cookgood.myrecipes.data.local.recipe

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class LocalRecipe(
   @PrimaryKey(autoGenerate = true) val id: Long = 0,
   val title: String,
   val brief: String,
   val servings: Int,
   @ColumnInfo(name = "cook_time") val cookTime: Int,
)