package com.example.lab6

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.lab6.ui.theme.Lab6Theme

// Modelo de datos para la receta
data class Recipe(
    val name: String,
    val description: String,
    val rating: Int,
    val ingredients: List<String>,
    val preparationSteps: List<String>,
    val comments: List<Comment>
)

data class Comment(
    val author: String,
    val date: String,
    val commentText: String
)

class InformationRec : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab6Theme {
                // Datos de prueba para la receta
                val sampleRecipe = Recipe(
                    name = "Prime Rib Roast",
                    description = "The Prime Rib Roast is a classic and tender cut of beef taken from the rib primal cut. Learn how to make the perfect prime rib roast to serve your family and friends.",
                    rating = 4,
                    ingredients = listOf(
                        "1 Prime Rib Roast (8 pounds)",
                        "1/2 cup balsamic vinegar",
                        "1 cup parsley leaves",
                        "8 cloves garlic, minced",
                        "1/4 teaspoon salt",
                        "1 cup water",
                        "3 drops Worcestershire sauce"
                    ),
                    preparationSteps = listOf(
                        "Preheat oven to 350°F. Let roast stand at room temperature for 1 hour.",
                        "In a small saucepan, reduce balsamic vinegar by half.",
                        "Finely mince parsley and garlic, then mix with salt.",
                        "Fill holes in the roast with garlic-parsley mixture.",
                        "Roast until desired temperature is reached."
                    ),
                    comments = listOf(
                        Comment("Tom Klein", "7.01.2017", "This prime rib roast was amazing!"),
                        Comment("Sally Parker", "7.01.2017", "I was amazed at how simple this preparation was.")
                    )
                )

                // Mostrar la pantalla de información de la receta
                RecipeInformationScreen(recipe = sampleRecipe)
            }
        }
    }
}

@Composable
fun RecipeInformationScreen(recipe: Recipe) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Fondo blanco
            .padding(16.dp)
    ) {
        item {
            // Imagen de la receta (sustituir por la imagen real)
            Image(
                painter = painterResource(id = R.drawable.rib_bef), // Coloca aquí el recurso correcto
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Nombre de la receta
            Text(
                text = recipe.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Estrellas de calificación
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(5) { index ->
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Star",
                        tint = if (index < recipe.rating) Color.Yellow else Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Descripción de la receta
            Text(
                text = recipe.description,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Justify
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de compras
            Text(
                text = "SHOPPING LIST",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        // Ingredientes
        items(recipe.ingredients) { ingredient ->
            Text(
                text = "• $ingredient",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))

            // Preparación
            Text(
                text = "PREPARATION",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        // Pasos de preparación
        items(recipe.preparationSteps) { step ->
            Row(
                modifier = Modifier.padding(bottom = 8.dp),
                verticalAlignment = Alignment.Top
            ) {
                val index = recipe.preparationSteps.indexOf(step)
                Text(
                    text = "${index + 1}. ",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = step,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))

            // Sección de comentarios
            Text(
                text = "COMMENTS",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        // Comentarios
        items(recipe.comments) { comment ->
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "User",
                        modifier = Modifier.size(40.dp)
                    )
                    Column(modifier = Modifier.padding(start = 8.dp)) {
                        Text(
                            text = comment.author,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = comment.date,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                Text(
                    text = comment.commentText,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))

            // Campo para escribir un nuevo comentario
            var commentText by remember { mutableStateOf("") }
            OutlinedTextField(
                value = commentText,
                onValueChange = { commentText = it },
                placeholder = { Text("Write your comment...") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
