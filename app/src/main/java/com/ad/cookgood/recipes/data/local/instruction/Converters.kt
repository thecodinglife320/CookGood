package com.ad.cookgood.recipes.data.local.instruction

import android.net.Uri
import androidx.room.TypeConverter

class Converters {

   @TypeConverter
   fun fromUri(uri: Uri?) = uri?.toString()

   @TypeConverter
   fun toUri(uriString: String?) = uriString?.let { Uri.parse(it) }
}