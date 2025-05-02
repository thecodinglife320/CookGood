package com.ad.cookgood.mycookbook.domain.usecase

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ad.cookgood.mycookbook.data.MyRecipeRepositoryImpl
import com.ad.cookgood.recipes.data.local.CookGookDb
import com.ad.cookgood.recipes.data.local.RecipeRepositoryImpl
import com.ad.cookgood.recipes.domain.model.Instruction
import com.ad.cookgood.recipes.domain.model.Recipe
import com.ad.cookgood.recipes.domain.usecase.AddInstructionUseCase
import com.ad.cookgood.recipes.domain.usecase.AddRecipeUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class GetInstructionsOfMyRecipeUseCaseIntegrateTest {

   private lateinit var addRecipeUseCase: AddRecipeUseCase
   private lateinit var addInstructionUseCase: AddInstructionUseCase
   private lateinit var getInstructionsOfMyRecipeUseCase: GetInstructionsOfMyRecipeUseCase
   private lateinit var getMyRecipeUseCase: GetMyRecipeUseCase
   private lateinit var db: CookGookDb

   @Before
   fun setUp() =
      run {
         val context = ApplicationProvider.getApplicationContext<Context>()

         db = Room
            .inMemoryDatabaseBuilder(context, CookGookDb::class.java)
            .allowMainThreadQueries()
            .build()

         RecipeRepositoryImpl(db.recipeDao, db.ingredientDao, db.instructionDao).run {
            addInstructionUseCase = AddInstructionUseCase(this)
            addRecipeUseCase = AddRecipeUseCase(this)
         }

         MyRecipeRepositoryImpl(
            db.recipeDao, db.ingredientDao, db.instructionDao
         ).run {
            getMyRecipeUseCase = GetMyRecipeUseCase(this)
            getInstructionsOfMyRecipeUseCase = GetInstructionsOfMyRecipeUseCase(this)
         }
      }

   @After
   fun tearDown() = db.close()

   @Test
   fun invoke() = runTest {

      //arrange
      val recipe = Recipe()
      val instruction1 = Instruction()
      val instruction2 = Instruction()

      //act
      val recipeId = addRecipeUseCase(recipe)
      val myRecipe = getMyRecipeUseCase(recipeId)
      myRecipe?.let {
         addInstructionUseCase(instruction1, myRecipe.id)
         addInstructionUseCase(instruction2, myRecipe.id)
      }

      //assert
      getInstructionsOfMyRecipeUseCase(myRecipe!!.id).run {
         assert(contains(instruction1))
         assert(contains(instruction2))
      }
   }

}