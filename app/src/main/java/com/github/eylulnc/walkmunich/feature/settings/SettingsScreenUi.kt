package com.github.eylulnc.walkmunich.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Terminal
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
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
import com.github.eylulnc.walkmunich.core.ui.util.getAppVersion
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreenUi(
    viewModel: SettingsViewModel = koinViewModel(),
    onAboutClick: () -> Unit
) {
    val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val appVersion = remember { context.getAppVersion() }

    var showClearHistoryDialog by remember { mutableStateOf(false) }
    var showClearFavoritesDialog by remember { mutableStateOf(false) }

    if (showClearHistoryDialog) {
        AlertDialog(
            onDismissRequest = { showClearHistoryDialog = false },
            title = { Text(stringResource(R.string.settings_clear_recently_viewed)) },
            text = { Text(stringResource(R.string.alert_clear_recently_viewed_message)) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.clearRecentlyViewed()
                    showClearHistoryDialog = false
                }) { Text(stringResource(R.string.alert_clear), color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { showClearHistoryDialog = false }) {
                    Text(stringResource(R.string.alert_cancel))
                }
            }
        )
    }

    if (showClearFavoritesDialog) {
        AlertDialog(
            onDismissRequest = { showClearFavoritesDialog = false },
            title = { Text(stringResource(R.string.settings_clear_favorites)) },
            text = { Text(stringResource(R.string.alert_clear_favorites_message)) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.clearFavorites()
                    showClearFavoritesDialog = false
                }) { Text(stringResource(R.string.alert_clear), color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { showClearFavoritesDialog = false }) {
                    Text(stringResource(R.string.alert_cancel))
                }
            }
        )
    }

    WMTopAppBarScreen(
        title = stringResource(R.string.settings_title),
        content = { modifier ->
            Column(
                modifier = modifier
                    .verticalScroll(rememberScrollState())
                    .padding(Spacing.Medium)
            ) {
                // App Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Spacing.Large),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Map,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        Spacer(Modifier.height(Spacing.Small))
                        Text(
                            stringResource(R.string.app_name),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            stringResource(R.string.version_display, appVersion.versionName, appVersion.versionCode),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Appearance
                SettingsSectionHeader(stringResource(R.string.settings_preferences))

                SettingsCard {
                    DarkModeSwitch(
                        isDarkMode = isDarkTheme,
                        onDarkThemeToggle = viewModel::setDarkTheme
                    )
                }

                Spacer(Modifier.height(Spacing.Large))

                // Data
                SettingsSectionHeader(stringResource(R.string.settings_data))

                SettingsCard {
                    SettingsNavigationItem(
                        icon = Icons.Outlined.History,
                        title = stringResource(R.string.settings_clear_recently_viewed),
                        onClick = { showClearHistoryDialog = true }
                    )

                    HorizontalDivider()

                    SettingsNavigationItem(
                        icon = Icons.Outlined.FavoriteBorder,
                        title = stringResource(R.string.settings_clear_favorites),
                        titleColor = MaterialTheme.colorScheme.error,
                        onClick = { showClearFavoritesDialog = true }
                    )
                }

                Spacer(Modifier.height(Spacing.Large))

                // About
                SettingsSectionHeader(stringResource(R.string.settings_about))

                SettingsCard {
                    SettingsNavigationItem(
                        icon = Icons.Outlined.Info,
                        title = stringResource(R.string.settings_about_app),
                        hasChevron = true,
                        onClick = onAboutClick
                    )

                    HorizontalDivider()

                    SettingsNavigationItem(
                        icon = Icons.Outlined.Terminal,
                        title = stringResource(R.string.settings_version),
                        trailingText = stringResource(
                            R.string.version_display,
                            appVersion.versionName,
                            appVersion.versionCode
                        )
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

        Spacer(Modifier.width(Spacing.Large))

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
