package com.github.eylulnc.walkmunich.core.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.eylulnc.walkmunich.core.ui.theme.Spacing

@Composable
fun WalkSearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit
) {
    val shape = RoundedCornerShape(Spacing.Medium)
    val containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
    val textColor = MaterialTheme.colorScheme.onSurface
    val placeholderColor = textColor.copy(alpha = 0.6f)

    Box(
        modifier = modifier
            .clip(shape)
            .background(containerColor)
            .height(52.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 12.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ✅ Compact search icon with minimal horizontal padding
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = "Search",
                tint = placeholderColor,
                modifier = Modifier.size(22.dp)
            )

            // ✅ Slight space between icon and text
            Spacer(modifier = Modifier.width(8.dp))

            // Text input
            TextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = {
                    Text(
                        text = "Search for tours or landmarks...",
                        color = placeholderColor,
                        fontWeight = FontWeight.Normal
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = textColor
                ),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                textStyle = LocalTextStyle.current.copy(color = textColor),
                singleLine = true
            )

            // Trailing clear icon
            if (query.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Clear",
                    tint = placeholderColor,
                    modifier = Modifier
                        .size(22.dp)
                        .clickable { onClearQuery() }
                        .padding(start = 4.dp)
                )
            }
        }
    }
}

