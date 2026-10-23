package xyz.nbani.ecsetnaplo.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldDecorator
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.TransformingLazyColumnItemScope
import androidx.wear.compose.material3.LocalTextStyle
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.lazy.TransformationSpec
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight

@Composable
fun TextBox(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    spec: TransformationSpec,
    itemScope: TransformingLazyColumnItemScope,
    decorator: TextFieldDecorator? = null,
    enabled: Boolean = true
) {
    val transformation = with(itemScope) {
        SurfaceTransformation(spec)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                with(transformation) {
                    applyContainerTransformation()
                }
            }
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        BasicTextField(
            state = state,
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            decorator = decorator,
            lineLimits = TextFieldLineLimits.SingleLine,
            enabled = enabled
        )
    }
}

@Preview(device = "id:wearos_small_round", showBackground = true)
@Composable
fun TextBoxPreview() {
    val spec = rememberTransformationSpec()
    val state = rememberTextFieldState()

    state.setTextAndPlaceCursorAtEnd("R1")

    TransformingLazyColumn {
        item {
            TextBox(
                state = state,
                spec = spec,
                itemScope = this,
                modifier = Modifier.transformedHeight(this, spec)
            )
        }

        item {
            TextBox(
                state = state,
                spec = spec,
                itemScope = this,
                modifier = Modifier.transformedHeight(this, spec)
            )
        }
    }
}