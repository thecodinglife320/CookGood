package com.ad.cookgood.mycookbook.domain.usecase

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ad.cookgood.mycookbook.data.MyCookBookRepositoryImpl
import com.ad.cookgood.recipes.data.local.CookGookDb
import com.ad.cookgood.recipes.data.local.RecipeRepositoryImpl
import com.ad.cookgood.recipes.domain.model.Recipe
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
class GetMyCookBookUseCaseIntegrateTest {

   private lateinit var addRecipeUseCase: AddRecipeUseCase
   private lateinit var getMyCookBookUseCase: GetMyCookBookUseCase
   private lateinit var cookGookDb: CookGookDb

   @Before
   fun setUp() =
      let {
         val context = ApplicationProvider.getApplicationContext<Context>()

         cookGookDb = Room
            .inMemoryDatabaseBuilder(context, CookGookDb::class.java)
            .allowMainThreadQueries()
            .build()

         MyCookBookRepositoryImpl(cookGookDb.recipeDao).run {
            getMyCookBookUseCase = GetMyCookBookUseCase(this)
         }

         RecipeRepositoryImpl(
            cookGookDb.recipeDao,
            cookGookDb.ingredientDao,
            cookGookDb.instructionDao
         ).let {
            addRecipeUseCase = AddRecipeUseCase(it)
         }

      }

   @After
   fun tearDown() = cookGookDb.close()

   @Test
   fun invoke() = runTest {

      //arrange
      val recipe = Recipe()
      val recipe2 = Recipe()

      //act
      addRecipeUseCase(recipe)
      addRecipeUseCase(recipe2)
      val myRecipes = getMyCookBookUseCase().myRecipes

      //assert
      assertEquals(myRecipes[0].id, 1)
      assertEquals(myRecipes[1].id, 2)
   }

}