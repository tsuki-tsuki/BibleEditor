package ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterialApi::class)
@Composable
inline fun <reified T> Picker(
    expanded: Boolean,
    noinline onExpandedChange: (Boolean) -> Unit,
    label: String,
    items: List<T>,
    selectedItem: T,
    crossinline onItemSelected: (T) -> Unit,
    crossinline itemValue: (T) -> String,
    modifier: Modifier = Modifier,
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { onExpandedChange(!expanded) },
        modifier = modifier
    ) {
        TextField(
            readOnly = true,
            value = itemValue(selectedItem),
            onValueChange = {},
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { onExpandedChange(false) }) {
            items.forEach {
                DropdownMenuItem(onClick = {
                    onExpandedChange(false)
                    onItemSelected(it)
                }) { Text(itemValue(it)) }
            }
        }
    }
}