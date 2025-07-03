package com.ad.cookgood.share_recipe.domain.usecase

import com.ad.cookgood.share_recipe.domain.ShareRecipeRepository
import javax.inject.Inject

/**
 * Use case for retrieving the details of a shared recipe.
 *
 * This class encapsulates the business logic for fetching a shared recipe's information,
 * such as its ingredients, instructions, and other relevant details. It interacts with
 * the [com.ad.cookgood.share_recipe.domain.ShareRecipeRepository] to access the data.
 *
 * @property repository The repository responsible for handling shared recipe data.
 *///GetSharedRecipeDetail usecase
class GetSharedRecipeDetailUseCase @Inject constructor(
   private val repository: ShareRecipeRepository
) {
   suspend operator fun invoke(sharedRecipeId: String) = repository.getSharedRecipe(sharedRecipeId)
}