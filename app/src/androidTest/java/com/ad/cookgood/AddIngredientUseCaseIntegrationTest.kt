package com.ad.cookgood

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ad.cookgood.recipes.data.local.CookGookDb
import com.ad.cookgood.recipes.data.local.RecipeRepositoryImpl
import com.ad.cookgood.recipes.domain.model.Ingredient
import com.ad.cookgood.recipes.domain.model.Recipe
import com.ad.cookgood.recipes.domain.usecase.AddIngredientUseCase
import com.ad.cookgood.recipes.domain.usecase.AddRecipeUseCase
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
   private lateinit var db: CookGookDb

   @Before
   fun setUp() =
      let {
         val context = ApplicationProvider.getApplicationContext<Context>()

         db = Room.inMemoryDatabaseBuilder(context, CookGookDb::class.java)
            .allowMainThreadQueries()
            .build()

         RecipeRepositoryImpl(db.recipeDao, db.ingredientDao, db.instructionDao).let {
            addRecipeUseCase = AddRecipeUseCase(it)
            addIngredientUseCase = AddIngredientUseCase(it)
         }
      }

   @After
   fun tearDown() = db.close()

   @Test
   fun invoke_should_insert_ingredient_for_a_given_recipeId() = runTest {

      // Arrange
      val recipe = Recipe(uri = null)
      val recipeId = addRecipeUseCase.invoke(recipe)

      // Act
      addIngredientUseCase.invoke(Ingredient("Sugar"), recipeId)
      addIngredientUseCase.invoke(Ingredient("rau"), recipeId)

      // Assert
      // Kiểm tra xem ingredient đã được insert vào database và liên kết với recipeId hay chưa
      db.ingredientDao.getAllIngredient().also {
         it.forEach {
            assertEquals(recipeId, it.recipeId.toLong())
         }
      }
   }
}