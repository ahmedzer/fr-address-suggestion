package com.za.addresssuggestion.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.za.addresssuggestion.domain.model.AddressSuggestion
import kotlin.collections.isNotEmpty

/**
 * A reusable Compose dropdown component for displaying address suggestions.
 *
 * This component is stateless and does not perform any network calls.
 * State and data loading must be handled by the host application.
 *
 * @param value Current text field value
 * @param onValueChange Called when the user types
 * @param suggestions List of address suggestions to display
 * @param onSelect Called when a suggestion is selected
 * @param inputField Composable used to render the input field
 * @param modifier Optional modifier for layout customization
 */
@Composable
fun AddressDropdownField(
    value: String,
    onValueChange: (String) -> Unit,
    suggestions: List<AddressSuggestion>,
    onSelect: (AddressSuggestion) -> Unit,
    inputField: @Composable (value: String, onValueChange: (String) -> Unit) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(IntSize.Zero) }
    val focusRequester = remember { FocusRequester() }

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size
                }
        ) {
            inputField(value) {
                onValueChange(it)
                expanded = true
            }


            LaunchedEffect(expanded) {
                if (expanded) focusRequester.requestFocus()
            }
        }

        if (expanded && suggestions.isNotEmpty()) {
            Popup(
                alignment = Alignment.TopStart,
                offset = IntOffset(0, textFieldSize.height),
                properties = PopupProperties(
                    focusable = false,
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                )
            ) {
                Column(
                    modifier = Modifier
                        .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                ) {
                    suggestions.forEach { suggestion ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelect(suggestion)
                                    expanded = false
                                }
                                .padding(horizontal = 12.dp, vertical = 12.dp)
                        ) {
                            Text(
                                text = suggestion.label,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}