package com.example.imagenperro.ui

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.imagenperro.R
import com.example.imagenperro.data.DogRepository
import com.example.imagenperro.ui.theme.DogImageAppTheme


@Composable
fun DogImageScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }
    var currentImageUrl by remember { mutableStateOf<String?>(null) }
    var buttonScale by remember { mutableFloatStateOf(1f) }

    LaunchedEffect(Unit) {
        loadNewImage(context) { url ->
            currentImageUrl = url
            isLoading = false
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(vertical = 48.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(0.8f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "¡Bienvenido a mi app de perros!",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 16.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Aca puedes ver una imagen de un perro al azar",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 16.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .aspectRatio(0.7f)
                    .padding(16.dp),
                contentAlignment = Alignment.Center

            ) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = isLoading,
                    enter = fadeIn() + scaleIn(initialScale = 0.8f, animationSpec = spring()),
                    exit = fadeOut(),
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.Center)
                    )
                }

                androidx.compose.animation.AnimatedVisibility(
                    visible = !isLoading,
                    enter = fadeIn() + scaleIn(initialScale = 0.8f, animationSpec = spring()),
                    exit = fadeOut()
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(currentImageUrl)
                            .crossfade(true)
                            .error(R.drawable.ic_error_24)
                            .build(),
                        contentDescription = "Imagen de un perro",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                    )
                }
            }

            Button(
                onClick = {
                    buttonScale = 0.9f
                    isLoading = true
                    loadNewImage(context) { url ->
                        currentImageUrl = url
                        isLoading = false
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .scale(buttonScale),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Cambiar Imagen")
            }

            LaunchedEffect(buttonScale) {
                if (buttonScale < 1f) {
                    kotlinx.coroutines.delay(200)
                    buttonScale = 1f
                }
            }
        }
    }
}

private fun loadNewImage(
    context: android.content.Context,
    onResult: (String?) -> Unit
) {
    DogRepository.fetchNewDogImage { fetchedDog ->
        if (fetchedDog != null) {
            onResult(fetchedDog.imageLink)
        } else {
            Toast.makeText(context, "Error al cargar la imagen", Toast.LENGTH_SHORT).show()
            onResult(null)
        }
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_NO)
@Composable
fun DogImageScreenPreviewLight() {
    DogImageAppTheme {
        DogImageScreen()
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DogImageScreenPreviewDark() {
    DogImageAppTheme {
        DogImageScreen()
    }
}

