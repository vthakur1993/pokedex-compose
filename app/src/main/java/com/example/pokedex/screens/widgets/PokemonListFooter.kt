package com.example.pokedex.screens.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pokedex.R
import com.example.pokedex.viewmodel.FooterUIState

@Composable
fun PokemonListFooter(footerUIState: FooterUIState) {
    Column(Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp, alignment = Alignment.CenterVertically)) {
        val modifier = Modifier.size(50.dp)
        when (footerUIState) {
            is FooterUIState.Error -> {
                FooterErrorWidget(modifier, footerUIState.msg)
            }
            is FooterUIState.Loading -> {
                FooterLoadingWidget(modifier)
            }
            else -> {}
        }
    }
}

@Composable
fun FooterErrorWidget(modifier: Modifier, errorMsg: String) {
    Image(
        painter = painterResource(id = R.drawable.ditto), // Replace with your WebP drawable ID
        contentDescription = "Ditto Error image",
        modifier = modifier,
        contentScale = ContentScale.Fit
    )
    Text(
        errorMsg,
        style = MaterialTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.Bold
        ),
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun FooterLoadingWidget(modifier: Modifier) {
    CircularProgressIndicator(
        modifier = modifier,
        color = MaterialTheme.colorScheme.primary,
        strokeWidth = 2.dp,
    )
    Text("Loading more items...")
}

@Preview
@Composable
fun PokemonListFooterPreview() {
    PokemonListFooter(FooterUIState.Error("Error"))
}