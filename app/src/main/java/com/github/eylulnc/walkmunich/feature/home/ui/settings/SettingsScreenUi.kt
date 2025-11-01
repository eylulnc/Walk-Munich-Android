package com.github.eylulnc.walkmunich.feature.home.ui.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.eylulnc.walkmunich.R
import com.github.eylulnc.walkmunich.core.ui.composable.WMTopAppBarScreen
import com.github.eylulnc.walkmunich.core.ui.theme.Spacing
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreenUi(
    viewModel: SettingsViewModel = koinViewModel(),
    onBackClick: () -> Unit
) {
    val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()

    Column {
        WMTopAppBarScreen(
            title = stringResource(id = R.string.settings),
            content = {
                DarkModeSwitch(
                    isDarkMode = isDarkTheme,
                    onDarkThemeToggle = { viewModel.setDarkTheme(it) },
                )
            }
        )
    }
}

@Composable
fun DarkModeSwitch(
    isDarkMode: Boolean,
    onDarkThemeToggle: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Spacing.Medium),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(Spacing.CornerRadius),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.Small),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(R.string.dark_mode))
            Switch(
                checked = isDarkMode,
                onCheckedChange = onDarkThemeToggle
            )
        }
    }
}