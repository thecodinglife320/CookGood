package com.ad.cookgood.share_recipe.data

import com.ad.cookgood.recipes.domain.model.Instruction
import com.google.firebase.firestore.DocumentId

data class FirebaseInstruction(
   @DocumentId val id: String? = null,
   val instruction: Instruction = Instruction()
)