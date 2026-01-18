package com.github.eylulnc.walkmunich.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Terminal
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.eylulnc.walkmunich.R
import com.github.eylulnc.walkmunich.core.ui.composable.SettingsCard
import com.github.eylulnc.walkmunich.core.ui.composable.SettingsNavigationItem
import com.github.eylulnc.walkmunich.core.ui.composable.SettingsSectionHeader
import com.github.eylulnc.walkmunich.core.ui.composable.WMTopAppBarScreen
import com.github.eylulnc.walkmunich.core.ui.theme.Spacing
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreenUi(
    viewModel: SettingsViewModel = koinViewModel(),
    onAboutClick: () -> Unit
) {
    val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()

    WMTopAppBarScreen(
        title = stringResource(R.string.settings_title),
        content = { modifier ->
            Column(
                modifier = modifier
                    .verticalScroll(rememberScrollState())
                    .padding(Spacing.Medium)
            ) {

                SettingsSectionHeader(stringResource(R.string.settings_preferences))

                SettingsCard {
                    DarkModeSwitch(
                        isDarkMode = isDarkTheme,
                        onDarkThemeToggle = viewModel::setDarkTheme
                    )
                }

                Spacer(Modifier.height(Spacing.Large))

                SettingsSectionHeader(stringResource(R.string.settings_about))

                SettingsCard {
                    SettingsNavigationItem(
                        icon = Icons.Outlined.Info,
                        title = "About App",
                        hasChevron = true,
                        onClick = onAboutClick
                    )

                    HorizontalDivider()

                    SettingsNavigationItem(
                        icon = Icons.Outlined.Terminal,
                        title = "Version",
                        trailingText = "1.0.2 (24)"
                    )
                }
            }
        }
    )
}

@Composable
fun DarkModeSwitch(
    isDarkMode: Boolean,
    onDarkThemeToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.DarkMode,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(Spacing.Large))

        Text(
            stringResource(R.string.dark_mode),
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Medium
        )

        Switch(
            checked = isDarkMode,
            onCheckedChange = onDarkThemeToggle
        )
    }
}
