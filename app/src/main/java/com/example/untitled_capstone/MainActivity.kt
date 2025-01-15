package com.example.untitled_capstone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.untitled_capstone.ui.theme.CustomTheme
import com.example.untitled_capstone.ui.theme.Untitled_CapstoneTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Untitled_CapstoneTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(CustomTheme.colors.surface).fillMaxSize()
    ) {
        Row {
            Text(
                text = "Hello, $name!",
                style = CustomTheme.typography.body1,
                color = CustomTheme.colors.textPrimary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Untitled_CapstoneTheme {
        Greeting("Android")
    }
}