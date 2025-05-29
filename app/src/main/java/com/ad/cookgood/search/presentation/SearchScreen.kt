package com.ad.cookgood.search.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
) {
   Scaffold(
      topBar = {
         SearchScreenAppBar()
      }
   ) {
      SearchScreenContent(Modifier.padding(it))
   }
}

@Composable
fun SearchScreenContent(
   modifier: Modifier = Modifier
) {
   Column(modifier) { }
}