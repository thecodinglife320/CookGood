package com.ad.cookgood.myrecipes.domain.usecase

import com.ad.cookgood.myrecipes.domain.RecipeRepository
import com.ad.cookgood.myrecipes.domain.model.Ingredient
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

class AddIngredientUseCaseTest {

   private lateinit var recipeRepository: RecipeRepository
   private lateinit var addIngredientUseCase: AddIngredientUseCase

   @Before
   fun setUp() {
      recipeRepository = mock(RecipeRepository::class.java)
      addIngredientUseCase = AddIngredientUseCase(recipeRepository)
   }

   @Test
   fun `Successful ingredient insertion`() = runTest {

      // Arrange
      val ingredient = Ingredient()
      val recipeId = 123L

      // Act
      addIngredientUseCase.invoke(ingredient, recipeId)

      // Assert
      // Kiểm tra xem phương thức insertIngredient đã được gọi đúng một lần với đúng đối tượng ingredient và recipeId
      verify(recipeRepository, times(1)).insertIngredient(ingredient, recipeId)
   }
}