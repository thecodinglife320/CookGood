package com.ad.cookgood.util

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

val statusBarInsetDp: Dp
   @Composable
   get() = with(LocalDensity.current) {
      WindowInsets.statusBars.getTop(density = this).toDp()
   }