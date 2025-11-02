package com.github.eylulnc.walkmunich.core.ui.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.eylulnc.walkmunich.core.data.model.Category
import com.github.eylulnc.walkmunich.core.ui.theme.Spacing
import com.github.eylulnc.walkmunich.core.ui.theme.TypographySizes

@Composable
fun CategoryChips(
    selectedCategory: Category?,
    onCategorySelected: (Category?) -> Unit
) {
    val categories = remember { listOf<Category?>(null) + Category.values() }

    LazyRow(
        contentPadding = PaddingValues(horizontal = Spacing.Medium),
        horizontalArrangement = Arrangement.spacedBy(Spacing.Small)
    ) {
        items(categories) { category ->
            val isSelected = selectedCategory == category
            val backgroundColor = if (isSelected)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f)

            val contentColor = if (isSelected)
                MaterialTheme.colorScheme.onPrimary
            else
                MaterialTheme.colorScheme.onSurfaceVariant

            Button(
                onClick = { onCategorySelected(category) },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = backgroundColor,
                    contentColor = contentColor
                ),
                contentPadding = PaddingValues(
                    horizontal = Spacing.Large,
                    vertical = Spacing.ExtraSmall
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = if (isSelected) 2.dp else 0.dp,
                    pressedElevation = 4.dp
                ),
                border = if (!isSelected)
                    BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                else null
            ) {
                val text = when (category) {
                    null -> "All"
                    else -> category.name.lowercase().replaceFirstChar { it.titlecase() }
                }
                Text(
                    text = text,
                    fontSize = TypographySizes.body,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}

