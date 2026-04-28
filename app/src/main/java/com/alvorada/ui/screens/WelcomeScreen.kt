package com.alvorada.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * WelcomeScreen - Tela de Onboarding do Alvorada Brasileira.
 * Desenvolvida com foco em estética rica e padrões Material 3.
 */
@Composable
fun WelcomeScreen() {
    var isMuted by remember { mutableStateOf(false) }
    var startAnimation by remember { mutableStateOf(false) }
    
    // Animação de Fade-In inicial
    val alphaAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "FadeIn"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // 1. Background com Imagem e Overlay de Gradiente
        // Nota: Substitua pelo recurso real R.drawable.onca_pintada
        Box(modifier = Modifier.fillMaxSize()) {
            // Simulando a imagem de fundo (Onça-Pintada)
            // No app real, usar Image(painter = painterResource(id = R.drawable.onca_background), ...)
            Box(modifier = Modifier.fillMaxSize().background(Color.DarkGray)) 
            
            // Overlay de Gradiente (Esmeralda para Marinho)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xAA004D40), // Verde Esmeralda Translúcido
                                Color(0xEE0D47A1)  // Azul Marinho Profundo
                            )
                        )
                    )
            )
        }

        // 2. Botão de Áudio (Glassmorphism Effect)
        IconButton(
            onClick = { isMuted = !isMuted },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(24.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.2f))
                .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape)
        ) {
            Icon(
                imageVector = if (isMuted) Icons.Default.VolumeMute else Icons.Default.VolumeUp,
                contentDescription = "Alternar Áudio",
                tint = Color.White
            )
        }

        // Conteúdo Principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
                .alpha(alphaAnim),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 3. Logo (Ícone de Governo)
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White)
                    .border(3.dp, Color(0xFFFFD600), RoundedCornerShape(24.dp)), // Borda Amarela
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AccountBalance,
                    contentDescription = "Logo Governo",
                    modifier = Modifier.size(60.dp),
                    tint = Color(0xFF1351B4) // Azul Gov.br
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 4. Título: Alvorada Brasileira
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.White)) {
                        append("Alvorada ")
                    }
                    withStyle(style = SpanStyle(color = Color(0xFFFFD600))) {
                        append("Brasileira")
                    }
                },
                fontSize = 42.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
                lineHeight = 48.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Slogan
            Text(
                text = "Ajude a melhorar o país com a Alvorada Brasileira",
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.Light
            )

            Spacer(modifier = Modifier.height(48.dp))

            // 5. Botões de Ação
            // Botão Principal: Entrar com gov.br
            Button(
                onClick = { /* Ação Login gov.br */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1351B4) // Azul Oficial gov.br
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Entrar com ",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "gov.br",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botão Secundário: Continuar sem login
            OutlinedButton(
                onClick = { /* Continuar sem login */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
            ) {
                Text(
                    text = "Continuar sem login",
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // 6. Rodapé Ancorado
        Text(
            text = "Ao entrar, você concorda com nossos Termos de Uso e Política de Privacidade de Dados Abertos.",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp, start = 40.dp, end = 40.dp),
            fontSize = 10.sp,
            color = Color.White.copy(alpha = 0.5f),
            textAlign = TextAlign.Center,
            lineHeight = 14.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    MaterialTheme {
        WelcomeScreen()
    }
}
