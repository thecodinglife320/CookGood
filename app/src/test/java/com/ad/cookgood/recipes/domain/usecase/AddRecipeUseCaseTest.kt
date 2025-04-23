package com.ad.cookgood.recipes.domain.usecase

import com.ad.cookgood.recipes.domain.RecipeRepository
import com.ad.cookgood.recipes.domain.model.Ingredient
import com.ad.cookgood.recipes.domain.model.Recipe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
class AddRecipeUseCaseTest {

   private lateinit var addRecipeUseCase: AddRecipeUseCase
   private lateinit var recipeRepository: RecipeRepository

   @Before
   fun setUp() {
      recipeRepository = mock(RecipeRepository::class.java)
      addRecipeUseCase = AddRecipeUseCase(recipeRepository)
   }

   @Test
   fun `return recipe id when repository return recipe id`() = runTest {
      val expectedRecipeId = 1L
      val recipe = Recipe(
         title = "a",
         brief = "b",
         serving = 1,
         cookTime = 1
      )

      //dinh nghia hanh vi cua mock repository khi insert recipe
      `when`(recipeRepository.insertRecipe(recipe)).thenReturn(expectedRecipeId)

      //thuc te
      val actualRecipeId = addRecipeUseCase.invoke(recipe)

      //assert
      assertEquals(expectedRecipeId, actualRecipeId)

      verify(recipeRepository, times(1)).insertRecipe(recipe)

      val ingredient = Ingredient("a")
      verify(recipeRepository, never()).insertIngredient(ingredient, expectedRecipeId)
   }
}