package com.github.eylulnc.walkmunich

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.github.eylulnc.walkmunich.navigation.NavigationRoot
import com.github.eylulnc.walkmunich.ui.theme.WalkMunichTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WalkMunichTheme {
                NavigationRoot()
            }
        }
    }
}