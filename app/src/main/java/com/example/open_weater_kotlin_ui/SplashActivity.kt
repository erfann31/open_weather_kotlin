package com.example.open_weater_kotlin_ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import com.example.open_weater_kotlin_ui.view.theme.MyApplicationTheme

/**
 * SplashActivity displays a splash screen while checking for location permissions
 * and GPS availability before navigating to the main activity.
 *
 * @author Erfan Nasri
 */
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplashScreen {
                navigateToMainActivity()
            }
        }
    }

    private fun navigateToMainActivity() {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 3000)
    }
}

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    MyApplicationTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                colorResource(R.color.customCyan),
                                colorResource(R.color.customBlue)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                val imageVector = ImageVector.vectorResource(id = R.drawable.app_icon)
                Icon(
                    imageVector = imageVector,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    tint = Color.White // Set the color to white
                )
            }
        }
    }

    Handler(Looper.getMainLooper()).postDelayed(onTimeout, 3000)
}
