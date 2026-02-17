package com.carlafdez.autolog.presentation.screens.addScreen.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.carlafdez.autolog.presentation.screens.addScreen.AccentBlue

@Composable
fun SectionTitle(text: String) {
    Text(text = text, fontWeight = FontWeight.Bold, color = AccentBlue, fontSize = 13.sp)
}

@Preview
@Composable
fun SectionTitlePreview() {
    SectionTitle("Especificaciones")
}