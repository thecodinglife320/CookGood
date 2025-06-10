package com.ad.cookgood.mycookbook.presentaion.myrecipedetail

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.core.net.toUri
import com.ad.cookgood.R
import com.ad.cookgood.shared.CoilImage
import com.ad.cookgood.ui.theme.LocalDimens

@Composable
internal fun Header(
   modifier: Modifier = Modifier,
   uri: Uri? = "".toUri(),
   scrollState: ScrollState,
   headerHeightPx: Float = with(receiver = LocalDensity.current) {
      LocalDimens.current.headerHeight.toPx()
   }
) {

   val alpha = (-1f / headerHeightPx) * scrollState.value + 1

   val overlayColor = MaterialTheme.colorScheme.background.copy(alpha = 1 - alpha)

   Box(
      modifier = modifier
         .fillMaxWidth()
         .height(height = LocalDimens.current.headerHeight)
         .graphicsLayer {
            colorFilter = ColorFilter.tint(overlayColor, BlendMode.SrcOver)
         }
   ) {

      CoilImage(
         uri = uri
      )

      /*Bottom gradient*/
      Box(
         modifier = Modifier
            .fillMaxSize()
            .background(
               brush = Brush.verticalGradient(
                  colors = listOf(Color.Transparent, MaterialTheme.colorScheme.background),
                  startY = 3 * headerHeightPx / 4
               )
            )
      )
   }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Toolbar(
   modifier: Modifier = Modifier,
   showToolBar: Boolean,
   navigateUp: () -> Unit,
   navigateToEditScreen: () -> Unit,
   deleteMyRecipe: () -> Unit
) = AnimatedVisibility(
   visible = showToolBar,
   enter = fadeIn(animationSpec = tween(durationMillis = 1000)),
   exit = fadeOut(animationSpec = tween(durationMillis = 1000)),
   modifier = modifier
) {

   var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }

   if (deleteConfirmationRequired) {
      DeleteConfirmationDialog(
         onDeleteConfirm = {
            deleteConfirmationRequired = false
            deleteMyRecipe()
            navigateUp()
         },
         onDeleteCancel = { deleteConfirmationRequired = false },
         modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
      )
   }

   TopAppBar(
      navigationIcon = {
         IconButton(
            onClick = navigateUp,
            modifier = Modifier
               .padding(horizontal = LocalDimens.current.doubleContentPadding)
               .size(size = 36.dp)
         ) {
            Icon(
               imageVector = Icons.AutoMirrored.Filled.ArrowBack,
               contentDescription = null,
            )
         }
      },
      actions = {
         IconButton(onClick = { deleteConfirmationRequired = true }) {
            Icon(
               Icons.Rounded.Delete,
               contentDescription = null
            )
         }
         IconButton(onClick = navigateToEditScreen) {
            Icon(
               Icons.Rounded.Edit,
               contentDescription = null
            )
         }
      },
      title = { },
   )
}

private const val titleFontScaleStart = 1f
private const val titleFontScaleEnd = 0.66f

@Composable
internal fun Title(
   title: String = "",
   scrollState: ScrollState,
   showToolBar: Boolean,
   headerHeightPx: Float,
   toolbarHeightPx: Float,
) {
   var titleHeightPx by remember { mutableFloatStateOf(value = 0f) }
   var titleWidthPx by remember { mutableFloatStateOf(value = 0f) }

   val headerHeight = LocalDimens.current.headerHeight
   val toolbarHeight = LocalDimens.current.toolbarHeight

   val titlePaddingStart = LocalDimens.current.titlePaddingStart
   val titlePaddingEnd = LocalDimens.current.titlePaddingEnd
   val paddingMedium = LocalDimens.current.doubleContentPadding

   Text(
      text = title,
      color = MaterialTheme.colorScheme.onBackground,
      style = MaterialTheme.typography.displayMedium,
      modifier = Modifier
         .statusBarsPadding()
         .graphicsLayer {
            val collapseRange: Float = headerHeightPx - toolbarHeightPx

            val collapseFraction: Float = if (!showToolBar)
               (scrollState.value / collapseRange).coerceIn(0f, 1f)
            else 1f

            val scaleXY = lerp(
               start = titleFontScaleStart.dp,
               stop = titleFontScaleEnd.dp,
               fraction = collapseFraction
            )

            val titleExtraStartPadding = titleWidthPx.toDp() * (1 - scaleXY.value) / 2f

            val titleYFirstInterpolatedPoint = lerp(
               start = headerHeight - titleHeightPx.toDp() - paddingMedium,
               stop = headerHeight / 2f,
               fraction = collapseFraction
            )

            val titleXFirstInterpolatedPoint = lerp(
               start = titlePaddingStart,
               stop = (titlePaddingEnd - titleExtraStartPadding) * 5 / 4,
               fraction = collapseFraction
            )

            val titleYSecondInterpolatedPoint = lerp(
               start = headerHeight / 4,
               stop = toolbarHeight / 2 - titleHeightPx.toDp() / 2,
               fraction = collapseFraction
            )

            val titleXSecondInterpolatedPoint = lerp(
               start = (titlePaddingEnd - titleExtraStartPadding) * 5 / 4,
               stop = titlePaddingEnd - titleExtraStartPadding,
               fraction = collapseFraction
            )

            val titleY = lerp(
               start = titleYFirstInterpolatedPoint,
               stop = titleYSecondInterpolatedPoint,
               fraction = collapseFraction
            )

            val titleX = lerp(
               start = titleXFirstInterpolatedPoint,
               stop = titleXSecondInterpolatedPoint,
               fraction = collapseFraction
            )

            translationY = titleY.toPx()
            translationX = titleX.toPx()
            scaleX = scaleXY.value
            scaleY = scaleXY.value
         }
         .onGloballyPositioned { coordinates ->
            titleHeightPx = coordinates.size.height.toFloat()
            titleWidthPx = coordinates.size.width.toFloat()
         }
   )
}
