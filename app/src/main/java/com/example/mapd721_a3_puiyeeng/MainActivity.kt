package com.example.mapd721_a3_puiyeeng

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.mapd721_a3_puiyeeng.ui.theme.MAPD721A3PuiYeeNgTheme
import kotlinx.coroutines.delay
import androidx.navigation.compose.composable

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
                    AnimationApp()
                }
            }
        }
    }
}

@Composable
fun AnimationApp() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController)
        }
        composable("rocket") {
            RocketAnimationScreen(navController)
        }
        composable("scale") {
            ScaleAnimationScreen(navController)
        }
        composable("infinite") {
            InfiniteAnimationScreen(navController)
        }
        composable("enterExit") {
            ExitAnimationScreen(navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { navController.navigate("rocket") },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = "Transition Animation")
                }
                Button(
                    onClick = { navController.navigate("scale") },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = "Scale Animation")
                }
                Button(
                    onClick = { navController.navigate("infinite") },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = "Infinite Animation")
                }
                Button(
                    onClick = { navController.navigate("enterExit") },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = "Enter Exit Animation")
                }
            }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RocketAnimationScreen(navController: NavController) {
    var isRocketLaunched by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Animation Samples") },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { padding ->
            Box(
                modifier = Modifier.fillMaxSize().padding(padding)
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
    )
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
        if (launched) 0f else 500f
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



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaleAnimationScreen(navController: NavController) {
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Animation Samples") },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { padding ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize().padding(padding)
            ) {
                Button(
                    onClick = {
                        buttonIsClicked.value = !buttonIsClicked.value
                        if (buttonIsClicked.value) {
                            if (clickTime.value < 3) {
                                clickTime.value += 1
                            } else {
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
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfiniteAnimationScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Animation Samples") },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(padding),
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
                    contentDescription = "Stuart",
                    modifier = Modifier
                        .size(200.dp)
                        .scale(scale)
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExitAnimationScreen(navController: NavController) {
    var visible by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Animation Samples") },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { padding ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AnimatedVisibility(
                        visible = visible,
                        enter = slideInHorizontally(
                            initialOffsetX = { -it },
                            animationSpec = tween(durationMillis = 1000) // Adjust the duration here
                        ),
                        exit = slideOutVertically()
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(top = 32.dp)
                                .size(256.dp)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.cat),
                                contentDescription = "Cat",
                                modifier = Modifier.fillMaxSize()
                            )
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val radius = size.minDimension / 2
                                drawCircle(
                                    color = Color.Gray,
                                    radius = radius,
                                    style = Stroke(width = 4.dp.toPx())
                                )
                            }
                        }
                    }
                    Button(onClick = { visible = !visible }) {
                        Text(text = if (visible) "Press for Exit Animation" else "Press for Enter Animation")
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ExitAnimationScreenPreview() {
    MAPD721A3PuiYeeNgTheme {
        AnimationApp()
    }
}