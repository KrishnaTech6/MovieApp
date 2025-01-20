package com.example.movieapp.ui


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.movieapp.models.Title

@Composable
fun MovieItem(show: Title, onClick: () -> Unit) {
    Row(Modifier.clickable { onClick() }.padding(8.dp)) {
        Column {
            Text(show.title, style = MaterialTheme.typography.headlineSmall)
        }
    }
}
