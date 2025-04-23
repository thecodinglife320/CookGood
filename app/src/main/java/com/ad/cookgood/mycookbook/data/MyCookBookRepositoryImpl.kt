package com.ad.cookgood.mycookbook.data

import com.ad.cookgood.mycookbook.domain.MyCookBookRepository
import com.ad.cookgood.mycookbook.domain.model.MyCookBook
import com.ad.cookgood.mycookbook.domain.model.MyRecipe
import com.ad.cookgood.recipes.data.local.recipe.RecipeDao
import com.ad.cookgood.recipes.data.local.recipe.toDomain
import javax.inject.Inject

class MyCookBookRepositoryImpl @Inject constructor(
   private val recipeDao: RecipeDao,
) : MyCookBookRepository {
   override suspend fun getMyCookBook() =
      run {
         recipeDao.getAllRecipe().map {
            it.toDomain()
               .run {
                  MyRecipe(
                     id = it.id,
                     title = title,
                     brief = brief,
                     serving = serving,
                     cookTime = cookTime
                  )
               }
         }.let {
            MyCookBook(myRecipes = it)
         }
      }
}
