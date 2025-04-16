package com.ad.cookgood

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.ad.cookgood.myrecipes.data.local.CookGookDb
import com.ad.cookgood.myrecipes.data.local.RecipeRepositoryImpl
import com.ad.cookgood.myrecipes.domain.model.Instruction
import com.ad.cookgood.myrecipes.domain.model.Recipe
import com.ad.cookgood.myrecipes.domain.usecase.AddInstructionUseCase
import com.ad.cookgood.myrecipes.domain.usecase.AddRecipeUseCase
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class AddInstructionUseCaseIntegrationTest {
   private lateinit var addRecipeUseCase: AddRecipeUseCase
   private lateinit var addInstructionUseCase: AddInstructionUseCase
   private lateinit var recipeRepository: RecipeRepositoryImpl
   private lateinit var db: CookGookDb

   @Before
   fun setUp() =
      let {
         val context = ApplicationProvider.getApplicationContext<Context>()

         db = Room.inMemoryDatabaseBuilder(context, CookGookDb::class.java)
            .allowMainThreadQueries()
            .build()

         recipeRepository = RecipeRepositoryImpl(db.recipeDao, db.ingredientDao, db.instructionDao)
         addRecipeUseCase = AddRecipeUseCase(recipeRepository)
         addInstructionUseCase = AddInstructionUseCase(recipeRepository)
      }

   @After
   fun tearDown() {
      db.close()
   }

   @Test
   fun insert_instruction_after_insert_recipe() = runTest {

      // Arrange
      val recipe = Recipe()
      val recipeId = addRecipeUseCase.invoke(recipe)

      // Act
      addInstructionUseCase.invoke(Instruction(1, "b1"), recipeId)
      addInstructionUseCase.invoke(Instruction(2, "b2"), recipeId)

      // Assert
      // Kiểm tra xem instruction đã được insert vào database và liên kết với recipeId hay chưa
      db.instructionDao.getAllInstruction().also {
         it.forEach {
            Assert.assertEquals(recipeId, it.recipeId)
         }
      }
   }
}