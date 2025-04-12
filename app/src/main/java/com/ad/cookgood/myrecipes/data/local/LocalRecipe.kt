package com.ad.cookgood.myrecipes.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class LocalRecipe(
   @PrimaryKey(autoGenerate = true) val id: Int = 0,
   val title: String,
   val brief: String,
   val servings: Int,
   @ColumnInfo(name = "cook_time") val cookTime: Int,
)

