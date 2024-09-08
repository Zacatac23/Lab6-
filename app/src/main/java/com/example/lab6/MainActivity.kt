package com.example.lab6

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab6.ui.theme.Lab6Theme
import com.google.ai.client.generativeai.Chat
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab6Theme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var isSearchBarExpanded by remember { mutableStateOf(false) }
    var isMenuExpanded by remember { mutableStateOf(false) }
    var selectedElement by remember { mutableStateOf<String?>(null) } // Variable para almacenar el elemento seleccionado
    var selectedTabIndex by remember { mutableIntStateOf(1) } // Índice de la pestaña seleccionada

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            IconButton(onClick = {
                                isSearchBarExpanded = !isSearchBarExpanded
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search"
                                )
                            }

                            AnimatedVisibility(
                                visible = isSearchBarExpanded,
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                SearchBar(modifier = Modifier.weight(1f))
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Text(
                                text = "POPULAR RECIPES",
                                color = Color.Red,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            isMenuExpanded = true
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(
                        containerColor = Color.White,
                        titleContentColor = Color.Red,
                    )
                )
            },
            containerColor = Color.White
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = Color.White, // Fondo blanco para toda la sección de pestañas
                    contentColor = Color.Black // Color negro para el contenido por defecto
                ) {
                    Tab(
                        selected = selectedTabIndex == 0,
                        onClick = { selectedTabIndex = 0 },
                        modifier = Modifier
                            .background(Color.White) // Fondo blanco para las pestañas no seleccionadas
                    ) {
                        Text(
                            "APPETIZERS",
                            color = Color.Black, // Color del texto
                            fontWeight = if (selectedTabIndex == 0) FontWeight.Bold else FontWeight.Normal, // Resaltado solo si está seleccionado
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        if (selectedTabIndex == 0) {
                            Spacer(
                                modifier = Modifier
                                    .height(2.dp)
                                    .fillMaxWidth()
                                    .background(Color.Black) // Subrayado en negro
                            )
                        }
                    }

                    Tab(
                        selected = selectedTabIndex == 1, // Esta es la pestaña seleccionada
                        onClick = { selectedTabIndex = 1 },
                        modifier = Modifier
                            .background(Color.White) // Fondo blanco para la pestaña seleccionada
                    ) {
                        Text(
                            "ENTREES",
                            color = Color.Black, // Color del texto
                            fontWeight = if (selectedTabIndex == 1) FontWeight.Bold else FontWeight.Normal, // Texto en negrita para la pestaña seleccionada
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        if (selectedTabIndex == 1) {
                            Spacer(
                                modifier = Modifier
                                    .height(2.dp)
                                    .fillMaxWidth()
                                    .background(Color.Black) // Subrayado en negro
                            )
                        }
                    }

                    Tab(
                        selected = selectedTabIndex == 2,
                        onClick = { selectedTabIndex = 2 },
                        modifier = Modifier
                            .background(Color.White) // Fondo blanco para las pestañas no seleccionadas
                    ) {
                        Text(
                            "DESSERT",
                            color = Color.Black, // Color del texto
                            fontWeight = if (selectedTabIndex == 2) FontWeight.Bold else FontWeight.Normal, // Resaltado solo si está seleccionado
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        if (selectedTabIndex == 2) {
                            Spacer(
                                modifier = Modifier
                                    .height(2.dp)
                                    .fillMaxWidth()
                                    .background(Color.Black) // Subrayado en negro
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                AlignYourBodyRow(onItemSelected = { selectedElement = it })

                Spacer(modifier = Modifier.height(16.dp))

                // Mostrar los detalles de la receta solo si un elemento ha sido seleccionado
                selectedElement?.let {
                    ShowRecipeDetail(it)
                }
            }
        }

        if (isMenuExpanded) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Red)
                    .clickable { isMenuExpanded = false }
            ) {
                DrawerContent()
            }
        }
    }
}

@Composable
fun DrawerContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.Red)
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Text("POPULAR RECIPES", style = MaterialTheme.typography.titleMedium, color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))
        Text("SAVED RECIPES", style = MaterialTheme.typography.titleMedium, color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))
        Text("SHOPPING LIST", style = MaterialTheme.typography.titleMedium, color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))
        Text("SETTINGS", style = MaterialTheme.typography.titleMedium, color = Color.White)

        Spacer(modifier = Modifier.weight(1f))

        Text("HARRY TRUMAN", style = MaterialTheme.typography.bodyLarge, color = Color.White)
    }
}

@Composable
fun ShowRecipeDetail(recipeName: String) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = recipeName,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.timer), // Cambia "ic_clock" al nombre de tu drawable
                contentDescription = "Time",
                modifier = Modifier
                    .size(70.dp)
                    .padding(end = 4.dp)
            )
            Text("5HR", modifier = Modifier.padding(end = 16.dp))

            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = "Favorites",
                modifier = Modifier.padding(end = 4.dp)
            )
            Text("685", modifier = Modifier.padding(end = 16.dp))

            Image(
                painter = painterResource(id = R.drawable.chat), // Cambia "ic_comment" al nombre de tu drawable
                contentDescription = "Comments",
                modifier = Modifier
                    .size(70.dp)
                    .padding(end = 4.dp)
            )
            Text("107", modifier = Modifier.padding(end = 8.dp))
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "The $recipeName is a classic and tender dish. " +
                    "Learn how to make the perfect $recipeName to serve your family and friends. " +
                    "Check out What’s Cooking America’s award-winning $recipeName recipe and " +
                    "photo tutorial to help you make the Perfect $recipeName.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(horizontal = 5.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para ir a InformacionReceta.kt
        Button(
            onClick = {
                val intent = Intent(context, InformationRec::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "Más Información")
        }
    }
}


@Composable
fun RatingStars(rating: Int, maxRating: Int = 5) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(maxRating) { index ->
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Star",
                tint = if (index < rating) Color.Yellow else Color.Gray,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun AlignYourBodyElement(
    drawable: Int,
    text: String,
    rating: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = drawable),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(170.dp) // Tamaño cuadrado más grande
                .clip(RectangleShape) // Forma cuadrada
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.paddingFromBaseline(top = 10.dp, bottom = 8.dp),
            textAlign = TextAlign.Center
        )
        RatingStars(rating = rating)
    }
}

@Composable
fun AlignYourBodyRow(
    modifier: Modifier = Modifier,
    onItemSelected: (String) -> Unit
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        val elements = listOf(
            "Rib Roast" to Pair(R.drawable.rib_bef, 4),
            "Pasta" to Pair(R.drawable.lassa, 3),
            "Soup" to Pair(R.drawable.so, 2),
        )

        items(elements) { (element, drawableAndRating) ->
            val (drawable, rating) = drawableAndRating
            AlignYourBodyElement(
                drawable = drawable,
                text = element,
                rating = rating,
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        onItemSelected(element)
                    })
                }
            )
        }
    }
}

@Composable
fun SearchBar(modifier: Modifier = Modifier) {
    var query by remember { mutableStateOf("") }

    OutlinedTextField(
        value = query,
        onValueChange = { query = it },
        modifier = modifier,
        placeholder = { Text("Search") },
        singleLine = true
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Lab6Theme {
        MainScreen()
    }
}
