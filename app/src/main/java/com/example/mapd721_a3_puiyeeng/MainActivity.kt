package com.example.mapd721_a3_puiyeeng

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mapd721_a3_puiyeeng.ui.theme.MAPD721A3PuiYeeNgTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MAPD721A3PuiYeeNgTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RocketAnimationScreen()
                }
            }
        }
    }
}

@Composable
fun RocketAnimationScreen() {
    var isRocketLaunched by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .offset(
                    y = animateFloatAsState(
                        targetValue = if (isRocketLaunched) 100f else 300f,
                        animationSpec = tween(durationMillis = 1000), label = ""
                    ).value.dp
                )
        ) {
            Button(
                onClick = {
                    isRocketLaunched = !isRocketLaunched
                }
            ) {
                Text(if (isRocketLaunched) "Land Rocket" else "Launch Rocket")
            }
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopEnd
        ) {
            RocketImage(isRocketLaunched)
        }
    }
}

@Composable
fun RocketImage(isRocketLaunched: Boolean) {
    val transition = updateTransition(targetState = isRocketLaunched, label = "rocketTransition")
    val translateY by transition.animateFloat(
        transitionSpec = {
            if (targetState) {
                tween(durationMillis = 1000)
            } else {
                spring(stiffness = Spring.StiffnessLow)
            }
        }, label = "rocketLocation"
    ) { launched ->
        if (launched) 0f else 550f
    }
    val scale by transition.animateFloat(
        transitionSpec = {
            tween(durationMillis = 1000)
        }, label = "rocketScale"
    ) { launched ->
        if (launched) 0.5f else 1f
    }


    Image(
        painter = painterResource(R.drawable.rocket),
        contentDescription = "Rocket",
        modifier = Modifier
            .size((200 * scale).dp)
            .offset(y = translateY.dp)
    )
}




@Composable
fun ScaleAnimation() {
    val buttonIsClicked = remember { mutableStateOf(false) }
    val scaleState = remember { mutableStateOf(1f) }
    val clickTime = remember { mutableStateOf(0) }

    scaleState.value = animateFloatAsState(
        targetValue = if (buttonIsClicked.value) {
            if (clickTime.value == 1 ) {
                1.2f
            } else if (clickTime.value == 2) {
                1.4f
            } else if (clickTime.value == 3) {
                1.8f
            }
            else {
                1f
            }
        } else {
            1f
        },
        animationSpec = spring()
    ).value

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Button(
            onClick = {
                buttonIsClicked.value = !buttonIsClicked.value
                if (buttonIsClicked.value){
                    if (clickTime.value < 3){
                        clickTime.value += 1
                    }
                    else{
                        clickTime.value = 1
                    }
                }

                      },
            modifier = Modifier
                .size(200.dp, 80.dp)
                .scale(scaleState.value),
            shape = RectangleShape
        ) {
            Text("Click Me")
        }
    }
}

@Composable
fun InfiniteAnimation() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var isToggled by remember { mutableStateOf(false) }

        val scale by animateFloatAsState(
            targetValue = if (isToggled) 1.8f else 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1000),
                repeatMode = RepeatMode.Reverse
            )
        )

        LaunchedEffect(Unit) {
            while (true) {
                isToggled = !isToggled
                delay(1000)
            }
        }

        Image(
            painter = painterResource(R.drawable.stuart),
            contentDescription = "Toggle Scale Image",
            modifier = Modifier
                .size(200.dp)
                .scale(scale)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun RocketAnimationScreenPreview() {
    MAPD721A3PuiYeeNgTheme {
        RocketAnimationScreen()
    }
}