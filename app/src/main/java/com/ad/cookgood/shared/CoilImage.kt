package com.ad.cookgood.shared

import android.net.Uri
import androidx.compose.runtime.Composable
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
   AsyncImage(
      model = ImageRequest.Builder(context)
         .data(uri)
         .crossfade(true)
         .error(R.drawable.error_image)
         .build(),
      contentDescription = "Ảnh vừa chụp",
      modifier = modifier,
      contentScale = ContentScale.FillBounds
   )
}