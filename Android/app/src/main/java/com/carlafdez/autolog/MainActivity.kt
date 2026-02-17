package com.carlafdez.autolog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.carlafdez.autolog.navigation.NavigationRoot
import com.carlafdez.autolog.ui.theme.PruebaKotlinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PruebaKotlinTheme{
                NavigationRoot()
            }
        }
    }
}