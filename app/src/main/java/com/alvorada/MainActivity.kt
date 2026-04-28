package com.alvorada

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Poll
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.alvorada.ui.theme.AlvoradaTheme
import com.alvorada.ui.screens.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlvoradaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    var userName by remember { mutableStateOf("Cidadão") }
    
    Scaffold(
        topBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            
            // Mostrar TopBar apenas nas telas principais (leis, obras, pesquisas)
            if (currentRoute in listOf("laws", "works", "surveys")) {
                TopAppBar(
                    title = { 
                        Text(
                            "Alvorada", 
                            fontWeight = FontWeight.Bold, 
                            color = Color(0xFF1351B4)
                        ) 
                    },
                    actions = {
                        // Círculo de Perfil no Topo
                        Box(
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .clickable { navController.navigate("profile") },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Perfil",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        },
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                NavigationBarItem(
                    selected = currentRoute == "laws",
                    onClick = { navController.navigate("laws") },
                    icon = { Icon(Icons.Default.Description, contentDescription = "Leis") },
                    label = { Text("Leis") }
                )
                NavigationBarItem(
                    selected = currentRoute == "works",
                    onClick = { navController.navigate("works") },
                    icon = { Icon(Icons.Default.Build, contentDescription = "Obras") },
                    label = { Text("Obras") }
                )
                NavigationBarItem(
                    selected = currentRoute == "surveys",
                    onClick = { navController.navigate("surveys") },
                    icon = { Icon(Icons.Default.Poll, contentDescription = "Pesquisas") },
                    label = { Text("Pesquisas") }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "welcome",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("welcome") { 
                WelcomeScreen(onNavigateToSuggestion = { 
                    navController.navigate("suggestion") 
                }) 
            }
            composable("suggestion") {
                LoginSuggestionScreen(
                    onLoginClick = { /* Futura integração gov.br */ navController.navigate("laws") },
                    onSkipClick = { navController.navigate("laws") }
                )
            }
            composable("profile") {
                ProfileScreen(
                    currentName = userName,
                    onNameChange = { userName = it },
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable("laws") { LawsScreen() }
            composable("works") { WorksScreen() }
            composable("surveys") { SurveysScreen() }
        }
    }
}
