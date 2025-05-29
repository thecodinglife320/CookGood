package com.ad.cookgood.recipes.presentation.entry

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun CameraPermissionDialog(
   onDismissRequest: () -> Unit,
   onConfirmClick: () -> Unit,
   onDismissClick: () -> Unit,
   message: String
) {
   AlertDialog(
      onDismissRequest = onDismissRequest,
      title = {
         Text(text = "Cấp quyền cho ứng dụng")
      },
      text = {
         Text(message)
      },
      confirmButton = {
         TextButton(onClick = onConfirmClick) {
            Text("Đồng ý")
         }
      },
      dismissButton = {
         TextButton(onClick = onDismissClick) {
            Text("Hủy bỏ")
         }
      }
   )
}