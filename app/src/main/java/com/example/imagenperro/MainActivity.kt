package com.example.imagenperro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.imagenperro.ui.DogImageScreen
import com.example.imagenperro.ui.theme.DogImageAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DogImageAppTheme {
                DogImageScreen()
            }
        }
    }
}
