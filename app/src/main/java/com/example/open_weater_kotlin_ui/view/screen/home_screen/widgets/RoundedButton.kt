import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.open_weater_kotlin_ui.R

/**
 * Composable function for rendering a rounded button.
 *
 * This function creates a rounded button with customizable text, icon, and click behavior.
 *
 * @param modifier Modifier for customizing the button layout.
 * @param text Lambda function for defining the text content of the button.
 * @param item Lambda function for defining the icon or additional content of the button.
 * @param onClick Callback function for handling button click events.
 *
 * @author Erfan Nasri
 */
@Composable
fun RoundedButton(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(70.dp)
        .padding(horizontal = 8.dp),
    text: @Composable () -> Unit,
    item: @Composable () -> Unit,
    onClick: () -> Unit
) {
    TextButton(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.customBox)),
        elevation = ButtonDefaults.buttonElevation(5.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            text()
            item()
        }
    }
}
