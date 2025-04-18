package com.ad.cookgood.myrecipes.presentation.state

import androidx.compose.runtime.saveable.listSaver

object StateSaver {

   val ingredientUiStateSaver = listSaver<IngredientUiState, Any>(
      // Chuyển đổi IngredientUiState thành một danh sách các giá trị có thể lưu
      save = { ingredient ->
         listOf(
            ingredient.id,
            ingredient.name
         )
      },

      // Tạo một IngredientUiState từ danh sách các giá trị đã lưu
      restore = { list ->
         IngredientUiState(
            id = list[0] as Int,
            name = list[1] as String
         )
      }
   )
}