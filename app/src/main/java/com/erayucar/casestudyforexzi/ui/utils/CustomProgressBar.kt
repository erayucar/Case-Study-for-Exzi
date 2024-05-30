import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erayucar.casestudyforexzi.ui.theme.Gray80

@Composable
fun CustomProgressBar() {
    Canvas(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val points = listOf(0f, 0.25f, 0.5f, 0.75f, 1f)

        // Draw lines
        drawLine(
            color = Color(0xFF24283F),
            start = Offset(x = canvasWidth * points[0], y = canvasHeight / 2),
            end = Offset(x = canvasWidth * points[4], y = canvasHeight / 2),
            strokeWidth = 5f
        )

        // Draw circles
        points.forEach { point ->
            drawCircle(
                color = if (point == 0f) Color.White else Color(0xFF39405A),
                radius = 15f,
                center = Offset(x = canvasWidth * point, y = canvasHeight / 2),
                style = Stroke(width = 5f)
            )
        }
    }

    // Draw text
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        listOf("0%", "25%", "50%", "75%", "100%").forEach { text ->
            Text(
                text = text,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Gray80
            )
        }
    }
}
