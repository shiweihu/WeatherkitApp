package com.unisa.weatherkitapp.compose.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.unisa.weatherkitapp.R
import com.unisa.weatherkitapp.viewmodel.IndicesViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun IndexCompose(
    model: IndicesViewModel = hiltViewModel(),
    openBottomSheet: (Int) -> Unit
) {
    val context = LocalContext.current
    val indices = remember { model.getIndicesMap() }
    Column(
        modifier = Modifier.padding(5.dp, 0.dp)
    ) {
        Text(text = "${stringResource(id = R.string.life_index)}:", style = MaterialTheme.typography.titleLarge)
        LazyHorizontalGrid(
            modifier = Modifier
                .height(150.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            rows = GridCells.Fixed(2)
        ) {
            items(indices.toList(), itemContent = { (key, value) ->
                val iconRes = model.getIndicesIcons(value)
                IndexItems(text = stringResource(id = key), icon = iconRes){
                    openBottomSheet(value)
                }
            })
        }
    }
}

@Composable
fun IndexItems(
    text: String,
    icon: Int,
    onClick:()->Unit = {}
) {
    Card(
        modifier = Modifier
            .width(80.dp)
            .height(IntrinsicSize.Max)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(1f)
                .padding(5.dp, 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = icon),
                contentDescription = "icon",
                contentScale = ContentScale.Fit
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }
    }

}

@Preview
@Composable
fun PreviewIndexItems() {
    IndexItems("Air travel", R.drawable.air_travel_icon)
}