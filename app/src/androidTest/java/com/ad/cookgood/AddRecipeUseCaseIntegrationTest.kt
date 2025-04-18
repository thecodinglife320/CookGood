package com.ad.cookgood

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ad.cookgood.myrecipes.data.local.CookGookDb
import com.ad.cookgood.myrecipes.data.local.RecipeRepositoryImpl
import com.ad.cookgood.myrecipes.domain.model.Recipe
import com.ad.cookgood.myrecipes.domain.usecase.AddRecipeUseCase
import com.ad.cookgood.myrecipes.toLocal
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class AddRecipeUseCaseIntegrationTest {

   private lateinit var addRecipeUseCase: AddRecipeUseCase
   private lateinit var db: CookGookDb

   @Before
   fun setUp() =
      let {
         val context = ApplicationProvider.getApplicationContext<Context>()

         db = Room.inMemoryDatabaseBuilder(context, CookGookDb::class.java)
            .allowMainThreadQueries()
            .build()

         RecipeRepositoryImpl(db.recipeDao, db.ingredientDao, db.instructionDao).run {
            addRecipeUseCase = AddRecipeUseCase(this)
         }
      }

   @After
   fun tearDown() {
      db.close()
   }

   @Test
   fun insert_recipe_and_return_id() = runTest {
      val recipe = Recipe(
         title = "a",
         brief = "a",
         serving = 1,
         cookTime = 1
      )

      val recipeId = addRecipeUseCase.invoke(recipe)

      assert(recipeId > 0)

      val retrievedRecipe = db.recipeDao.getRecipeById(recipeId)
      val localRecipe = recipe.toLocal()

      assertEquals(localRecipe.title, retrievedRecipe?.title)
      assertEquals(localRecipe.brief, retrievedRecipe?.brief)
   }

}