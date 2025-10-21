package com.github.eylulnc.walkmunich

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.eylulnc.walkmunich.core.data.repository.UserPreferencesRepository
import com.github.eylulnc.walkmunich.navigation.NavigationRoot
import com.github.eylulnc.walkmunich.core.ui.theme.WalkMunichTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val userPreferencesRepository: UserPreferencesRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkTheme by userPreferencesRepository.isDarkTheme.collectAsStateWithLifecycle(initialValue = false)
            WalkMunichTheme(darkTheme = isDarkTheme) {
                NavigationRoot()
            }
        }
    }
}