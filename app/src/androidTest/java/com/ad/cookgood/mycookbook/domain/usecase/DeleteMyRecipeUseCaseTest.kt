package com.ad.cookgood.mycookbook.domain.usecase

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ad.cookgood.mycookbook.data.MyRecipeRepositoryImpl
import com.ad.cookgood.recipes.data.local.CookGookDb
import com.ad.cookgood.recipes.data.local.RecipeRepositoryImpl
import com.ad.cookgood.recipes.domain.model.Ingredient
import com.ad.cookgood.recipes.domain.model.Recipe
import com.ad.cookgood.recipes.domain.usecase.AddIngredientUseCase
import com.ad.cookgood.recipes.domain.usecase.AddRecipeUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class DeleteMyRecipeUseCaseTest {

   private lateinit var addRecipeUseCase: AddRecipeUseCase
   private lateinit var addIngredientUseCase: AddIngredientUseCase
   private lateinit var getMyRecipeUseCase: GetMyRecipeUseCase
   private lateinit var deleteMyRecipeUseCase: DeleteMyRecipeUseCase
   private lateinit var getIngredientsOfMyRecipeUseCase: GetIngredientsOfMyRecipeUseCase
   private lateinit var cookGookDb: CookGookDb

   @Before
   fun setUp() =
      run {
         val context = ApplicationProvider.getApplicationContext<Context>()

         cookGookDb = Room.inMemoryDatabaseBuilder(context, CookGookDb::class.java)
            .allowMainThreadQueries()
            .build()

         RecipeRepositoryImpl(
            cookGookDb.recipeDao,
            cookGookDb.ingredientDao,
            cookGookDb.instructionDao
         ).let {
            addRecipeUseCase = AddRecipeUseCase(it)
            addIngredientUseCase = AddIngredientUseCase(it)
         }

         MyRecipeRepositoryImpl(
            cookGookDb.recipeDao,
            cookGookDb.ingredientDao,
            cookGookDb.instructionDao
         ).let {
            deleteMyRecipeUseCase = DeleteMyRecipeUseCase(it)
            getMyRecipeUseCase = GetMyRecipeUseCase(it)
            getIngredientsOfMyRecipeUseCase = GetIngredientsOfMyRecipeUseCase(it)
         }
      }

   @After
   fun tearDown() = cookGookDb.close()

   @Test
   fun invoke() = runTest {

      //arrange
      addRecipeUseCase(Recipe(uri = null))
      addIngredientUseCase(Ingredient(), 1)

      //act
      getMyRecipeUseCase(1).let {
         it.map {
            deleteMyRecipeUseCase(it!!)
         }
      }

      //asset
      getMyRecipeUseCase(1).let {
         it.map {
            assertNull(it)
         }
      }
      getIngredientsOfMyRecipeUseCase(1).let {
         it.map {
            assertEquals(0, it.size)
         }
      }
   }

}