package com.ad.cookgood

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ad.cookgood.myrecipes.data.local.CookGookDb
import com.ad.cookgood.myrecipes.data.local.IngredientDao
import com.ad.cookgood.myrecipes.data.local.RecipeRepositoryImpl
import com.ad.cookgood.myrecipes.domain.model.Ingredient
import com.ad.cookgood.myrecipes.domain.model.Recipe
import com.ad.cookgood.myrecipes.domain.usecase.AddIngredientUseCase
import com.ad.cookgood.myrecipes.domain.usecase.AddRecipeUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class AddIngredientUseCaseIntegrationTest {

   private lateinit var addRecipeUseCase: AddRecipeUseCase
   private lateinit var addIngredientUseCase: AddIngredientUseCase
   private lateinit var ingredientDao1: IngredientDao
   private lateinit var db: CookGookDb

   @Before
   fun setUp() {
      Room.inMemoryDatabaseBuilder(
         context = ApplicationProvider.getApplicationContext<Context>(),
         CookGookDb::class.java
      )
         .allowMainThreadQueries()
         .build().apply {
            db = this
            ingredientDao1 = ingredientDao
            RecipeRepositoryImpl(recipeDao, ingredientDao).apply {
               addRecipeUseCase = AddRecipeUseCase(this)
               addIngredientUseCase = AddIngredientUseCase(this)
            }
         }
   }

   @After
   fun tearDown() = db.close()

   @Test
   fun invoke_should_insert_ingredient_after_insert_recipe() = runTest {

      // Arrange
      val recipe = Recipe()
      val recipeId = addRecipeUseCase.invoke(recipe)

      // Act
      addIngredientUseCase.invoke(Ingredient("Sugar"), recipeId)
      addIngredientUseCase.invoke(Ingredient("rau"), recipeId)

      // Assert
      // Kiểm tra xem ingredient đã được insert vào database và liên kết với recipeId hay chưa
      ingredientDao1.getAllIngredient()?.also {
         it.forEach {
            assertEquals(recipeId, it.recipeId.toLong())
         }
      }
   }
}