package com.ad.cookgood.mycookbook.domain.usecase

import com.ad.cookgood.mycookbook.domain.MyRecipeRepository
import com.ad.cookgood.mycookbook.domain.model.IngredientEdit
import com.ad.cookgood.mycookbook.domain.model.InstructionEdit
import javax.inject.Inject

class DeleteIngredientUseCase @Inject constructor(
   private val repository: MyRecipeRepository
) {
   suspend operator fun invoke(ingredientEdit: IngredientEdit, recipeId: Long) =
      repository.deleteIngredient(ingredientEdit, recipeId)
}

class DeleteInstructionUseCase @Inject constructor(
   private val repository: MyRecipeRepository
) {
   suspend operator fun invoke(instructionEdit: InstructionEdit, recipeId: Long) =
      repository.deleteInstruction(instructionEdit, recipeId)
}