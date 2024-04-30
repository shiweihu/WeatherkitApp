package com.unisa.weatherkitapp.compose.view

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.icu.util.TimeZone
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.ads.AdSize
import com.unisa.weatherkitapp.R
import com.unisa.weatherkitapp.public.variable.LocalUnitType
import com.unisa.weatherkitapp.public.variable.LocalsnackbarHostState
import com.unisa.weatherkitapp.public.variable.SEARCH_ADID
import com.unisa.weatherkitapp.viewmodel.LocationSearchViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale


@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LocationSearchCompose(
    model: LocationSearchViewModel = hiltViewModel(),
    navigateFuc: (route: String) -> Unit
) {
    var query by rememberSaveable { mutableStateOf("") }
    var searchFocusRequester by remember { mutableStateOf(false) }
    var showAutoList by remember{ mutableStateOf(true) }
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = LocalsnackbarHostState.current
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(1f),
        topBar = {
            Box(
                modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .padding(5.dp, 5.dp)
                            .fillMaxWidth(1f),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { navigateFuc(Route.MAIN.name) }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = stringResource(
                                    id = R.string.back
                                )
                            )
                        }
                        Spacer(modifier = Modifier.width(20.dp))
                        val keyboardController = LocalSoftwareKeyboardController.current
                        OutlinedTextField(
                            value = query,
                            onValueChange = {
                                query = it
                                showAutoList = true
                            },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant, // 设置焦点时的边框颜色
                                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                    alpha = 0.4f
                                ), // 设置非焦点时的边框颜色
                            ),
                            shape = RoundedCornerShape(45),
                            leadingIcon = {
                                if (searchFocusRequester) {
                                    IconButton(onClick = {
                                        focusManager.clearFocus()
                                        showAutoList = false
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "clear"
                                        )
                                    }
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Search Icon"
                                    )
                                }
                            },
                            modifier = Modifier.onFocusChanged {
                                searchFocusRequester = it.isFocused
                                if (!it.isFocused) {
                                    keyboardController?.hide()
                                    query = ""
                                    showAutoList = false
                                }
                            },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    keyboardController?.hide()
                                    onSearchClick(query, model, onError = {
                                        scope.launch {
                                            snackbarHostState.showSnackbar(context.getString(R.string.network_error))
                                        }
                                    })
                                    //hide the auto complete list
                                    showAutoList = false
                                }
                            )
                        )
                    }
                }
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(1f)
                .padding(it)
        ) {
            Column {
                if (searchFocusRequester && query.isEmpty()) {
                    TopCityCompose(navigateFuc = navigateFuc)
                    Spacer(modifier = Modifier.height(20.dp))
                    AdvertiseViewCompose(adsize = AdSize.LARGE_BANNER, adid = SEARCH_ADID)
                } else if (!searchFocusRequester && query.isEmpty()) {
                    SearchedLocations(navigateFuc = navigateFuc)
                } else if (query.isNotEmpty()) {
                    SearchList(navigateFuc = navigateFuc, showAutoList = showAutoList, query = query)
                }
            }
        }
    }
}

@Composable
fun AutoCompleteBox(
    query: String,
    model: LocationSearchViewModel = hiltViewModel(),
    navigateFuc: (route: String) -> Unit
) {
    val autoCompleteList by remember(query){model.autoComplete(query)}
    if (autoCompleteList.isNotEmpty()) {
        ElevatedCard(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            )
        ) {
            Spacer(modifier = Modifier.height(5.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(5.dp, 0.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                items(autoCompleteList) {item->
                    OutlinedCard(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.inverseSurface),
                    )  {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(5.dp, 5.dp)
                                .clickable {
                                    model.requestLocationDetails(item.Key, onError = {

                                    }) {
                                        navigateFuc(Route.MAIN.name)
                                    }
                                },
                            horizontalArrangement = Arrangement.Start
                        ){
                            Text(text="${if(item.LocalizedName.isNotEmpty()) item.LocalizedName else item.EnglishName},${item.AdministrativeArea.LocalizedName},${item.Country.LocalizedName}", style = MaterialTheme.typography.titleSmall)
                        }
                    }
                }
            }
        }
    }

}

private fun onSearchClick(
    query: String,
    model: LocationSearchViewModel,
    onError:(Exception)->Unit,
    setSearchText: (text: String) -> Unit = {}
) {
    if (query.isNotEmpty()) {
        model.requestLocations(query,onError = onError)
        setSearchText(query)
    }
}

@Composable
fun SearchedLocations(
    model: LocationSearchViewModel = hiltViewModel(),
    navigateFuc: (route: String) -> Unit
) {
    val list by model.usedLocations.collectAsStateWithLifecycle()
    val state = rememberLazyListState()
    val unitType = LocalUnitType.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = LocalsnackbarHostState.current
    val context = LocalContext.current

    LazyColumn(
        state = state,
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(5.dp, 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsIndexed(list, key = { _, item ->
            item.locationId
        }) { index, item ->

            var removing by remember { mutableStateOf(false) }
            val alphaValue by animateFloatAsState(
                targetValue = if (removing) 0f else 1f,
                animationSpec = tween(durationMillis = 300, easing = LinearEasing),
                label = "",
                finishedListener = {
                    if (removing) model.removeLocation(item.locationId)
                }
            )

            val zoomTimeZone: TimeZone = TimeZone.getTimeZone(item.locationInfo.TimeZone.Name)
            val sdf: SimpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).apply {
                timeZone = zoomTimeZone
            }
            var currentTime by remember { mutableStateOf(Calendar.getInstance(zoomTimeZone)) }
            LaunchedEffect(key1 = item.locationId) {
                while (true) {
                    delay(1000) // 1秒更新一次
                    currentTime = Calendar.getInstance(zoomTimeZone)
                }
            }
            val formattedTime by remember(currentTime) {
                mutableStateOf(sdf.format(currentTime.time))
            }
            Box(
                modifier = Modifier
                    .padding(0.dp, 5.dp)
                    .height(IntrinsicSize.Min)
                    .graphicsLayer(alpha = alphaValue),
                contentAlignment = Alignment.TopEnd
            ) {
                Column {
                    ElevatedCard(
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 6.dp
                        ),
                        modifier = Modifier
                            .clickable {
                                model.selectLocation(item.locationInfo){
                                    navigateFuc(Route.MAIN.name)
                                }
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(5.dp, 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Column {
                                Text(
                                    text = item.locationInfo.LocalizedName.ifEmpty { item.locationInfo.EnglishName },
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Text(
                                    text = formattedTime,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                            val weatherResponseItemState by remember {
                                model.requestCurrentWeatherWithoutDetail(
                                    item.locationInfo
                                ){
                                    scope.launch {
                                        snackbarHostState.showSnackbar(context.getString(R.string.network_error))
                                    }
                                }
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (weatherResponseItemState != null) {
                                    val temperature =
                                        if (unitType) "${weatherResponseItemState!!.Temperature.Metric.Value}${weatherResponseItemState!!.Temperature.Metric.Unit}"
                                        else "${weatherResponseItemState!!.Temperature.Imperial.Value}${weatherResponseItemState!!.Temperature.Imperial.Unit}"
                                    Column(
                                        horizontalAlignment = Alignment.End
                                    ) {
                                        Text(
                                            text = temperature,
                                            style = MaterialTheme.typography.titleLarge
                                        )
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Image(
                                                modifier = Modifier.size(25.dp),
                                                painter = painterResource(
                                                    id = model.getImage(
                                                        weatherResponseItemState!!.WeatherIcon
                                                    )
                                                ),
                                                contentDescription = weatherResponseItemState!!.WeatherText
                                            )
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Text(
                                                text = weatherResponseItemState!!.WeatherText,
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                        }
                                    }
                                } else {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = MaterialTheme.colorScheme.secondary,
                                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                                    )
                                }
                                if (item.selected != 1) {
                                    IconButton(onClick = { removing = true }, modifier = Modifier) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "remove"
                                        )
                                    }
                                }
                            }
                        }
                    }
                    if ((index + 1) % 5 == 0) {
                        Spacer(modifier = Modifier.height(10.dp))
                        AdvertiseViewCompose(adsize = AdSize.LARGE_BANNER, adid = SEARCH_ADID)
                    }
                }
            }
        }
    }

}


@Composable
fun SearchList(
    query: String,
    showAutoList:Boolean = true,
    model: LocationSearchViewModel = hiltViewModel(),
    navigateFuc: (route: String) -> Unit
) {

    //empty ,so shows the auto complete list
    if(showAutoList && query.isNotEmpty()){
        AutoCompleteBox(query = query,navigateFuc = navigateFuc)
    }else{
        val list by model.locationList
        LazyColumn(
            state = rememberLazyListState(),
            modifier = Modifier.fillMaxWidth(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(list) { item ->
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .padding(3.dp, 5.dp)
                        .clickable {
                            model.selectLocation(item) {
                                navigateFuc(Route.MAIN.name)
                            }
                        }
                ) {
                    Text(
                        modifier = Modifier.padding(5.dp, 5.dp),
                        text = "${item.LocalizedName},${item.AdministrativeArea.LocalizedName},${item.Country.LocalizedName}",
                    )
                }
            }
        }
    }

}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TopCityCompose(
    model: LocationSearchViewModel = hiltViewModel(),
    navigateFuc: (route: String) -> Unit,
) {
    val list by model.requestTopCities().collectAsStateWithLifecycle(initialValue = listOf())

    Column(
        modifier = Modifier.padding(5.dp, 0.dp)
    ) {
        Text(text = "${stringResource(id = R.string.top_cities)}:", style = MaterialTheme.typography.titleLarge)
        FlowRow(
            modifier = Modifier.fillMaxWidth(1f),
        ) {
            list.forEach { item ->
                SuggestionChip(modifier = Modifier.padding(3.dp, 3.dp), onClick =
                {
                    model.selectLocation(item.locationInfo){
                        navigateFuc(Route.MAIN.name)
                    }
                }, label = { Text(text = item.locationInfo.LocalizedName) })
            }
        }
    }
    //val state = rememberLazyListState()

}

@Preview
@Composable
fun previewLocationSearchCompose() {
    LocationSearchCompose() {

    }
}