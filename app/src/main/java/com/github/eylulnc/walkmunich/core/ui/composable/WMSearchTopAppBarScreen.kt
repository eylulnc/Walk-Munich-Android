package com.github.eylulnc.walkmunich.core.ui.composable

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.github.eylulnc.walkmunich.core.ui.theme.OrangeMain
import com.github.eylulnc.walkmunich.core.ui.theme.Spacing
import com.github.eylulnc.walkmunich.core.ui.theme.TypographySizes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WMSearchTopAppBarScreen(
    title: String,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    onBack: (() -> Unit)? = null,
    containerColor: Color = MaterialTheme.colorScheme.background,
    content: @Composable (Modifier) -> Unit
) {
    var isSearchMode by remember { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(containerColor)
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        Crossfade(targetState = isSearchMode, label = "search-mode") { searchMode ->
            if (searchMode) {
                // 🔍 Search Mode – clean white bar at the top
                Surface(
                    shadowElevation = Spacing.Small,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TopAppBar(
                        title = {
                            TextField(
                                value = searchQuery,
                                onValueChange = onSearchQueryChange,
                                placeholder = {
                                    Text(
                                        text = "Search for tours or landmarks...",
                                        fontSize = TypographySizes.medium,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                },
                                textStyle = TextStyle(
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = TypographySizes.medium,
                                    lineHeight = TypographySizes.medium
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(Spacing.CategoryIconSize) // 56.dp -> design token
                                    .background(Color.White, RoundedCornerShape(Spacing.CornerRadius)),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    cursorColor = MaterialTheme.colorScheme.onSurface
                                ),
                                singleLine = true,
                                trailingIcon = {
                                    IconButton(onClick = {
                                        onClearSearch()
                                        isSearchMode = false
                                    }) {
                                        Icon(
                                            imageVector = Icons.Outlined.Close,
                                            contentDescription = "Close search",
                                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                                        )
                                    }
                                }
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.White,
                            titleContentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        scrollBehavior = null
                    )
                }
            } else {
                // 🏙️ Normal Mode
                TopAppBar(
                    title = {
                        Text(
                            text = title,
                            fontSize = TypographySizes.large,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        onBack?.let {
                            IconButton(onClick = it) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = OrangeMain
                                )
                            }
                        }
                    },
                    actions = {
                        IconButton(onClick = { isSearchMode = true }) {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                contentDescription = "Search",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = containerColor,
                        titleContentColor = MaterialTheme.colorScheme.onBackground
                    ),
                    scrollBehavior = scrollBehavior
                )
            }
        }

        // --- Main Content ---
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            content(Modifier.fillMaxSize())
        }
    }
}
