package com.ad.cookgood.shared

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ad.cookgood.R

@Composable
fun CoilImage(
   uri: Uri?,
   modifier: Modifier = Modifier
) {
   val context = LocalContext.current
   var isLoading by remember { mutableStateOf(true) }
   Box {
      if (isLoading) {
         CircularProgressIndicator(Modifier.align(Alignment.Center))
      }
      AsyncImage(
         model = ImageRequest.Builder(context)
            .data(uri)
            .crossfade(true)
            .error(R.drawable.error_image)
            .build(),
         contentDescription = "Ảnh vừa chụp",
         modifier = modifier,
         contentScale = ContentScale.FillBounds,
         onSuccess = { isLoading = false }
      )
   }
}