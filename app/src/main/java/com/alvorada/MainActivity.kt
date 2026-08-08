package com.alvorada

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import com.alvorada.data.MockAlvoradaRepository
import com.alvorada.ui.AlvoradaApp
import com.alvorada.ui.theme.AlvoradaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlvoradaTheme {
                val repository = remember { MockAlvoradaRepository() }
                AlvoradaApp(repository = repository)
            }
        }
    }
}
