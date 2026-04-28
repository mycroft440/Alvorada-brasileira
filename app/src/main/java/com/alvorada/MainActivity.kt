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
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.alvorada.ui.theme.AlvoradaTheme
import com.alvorada.ui.screens.WelcomeScreen
import com.alvorada.ui.screens.LoginSuggestionScreen
import com.alvorada.ui.screens.ProfileScreen
import com.alvorada.ui.screens.SpendingDetailScreen
import com.alvorada.ui.screens.LawsScreen
import com.alvorada.ui.screens.WorksScreen
import com.alvorada.ui.screens.SurveysScreen

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
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(12.dp))
                Text(
                    "Categorias de Gastos",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Divider()
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
                    label = { Text("Saúde") },
                    selected = false,
                    onClick = { 
                        navController.navigate("spending_detail/Saúde")
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Build, contentDescription = null) },
                    label = { Text("Educação") },
                    selected = false,
                    onClick = { 
                        navController.navigate("spending_detail/Educação")
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    label = { Text("Segurança") },
                    selected = false,
                    onClick = { 
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Place, contentDescription = null) },
                    label = { Text("Infraestrutura") },
                    selected = false,
                    onClick = { 
                        scope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                
                // Mostrar TopBar apenas nas telas principais
                if (currentRoute in listOf("laws", "works", "surveys")) {
                    TopAppBar(
                        title = { 
                            Text(
                                "Alvorada", 
                                fontWeight = FontWeight.Bold, 
                                color = Color(0xFF1351B4)
                            ) 
                        },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        },
                        actions = {
                            // Círculo de Perfil
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
                    icon = { Icon(Icons.Default.List, contentDescription = "Leis") },
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
                    icon = { Icon(Icons.Default.Info, contentDescription = "Pesquisas") },
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
            composable("spending_detail/{category}") { backStackEntry ->
                val category = backStackEntry.arguments?.getString("category") ?: ""
                SpendingDetailScreen(
                    category = category,
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable("laws") { 
                LawsScreen(onCategoryClick = { category ->
                    navController.navigate("spending_detail/$category")
                }) 
            }
            composable("works") {
                WorksScreen()
            }
            composable("surveys") {
                SurveysScreen()
            }
        }
    }
}
}
