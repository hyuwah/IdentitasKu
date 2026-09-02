package com.muhammadwahyudin.identitasku.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ChipElevation
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.data.model.DataWithDataType
import com.muhammadwahyudin.identitasku.ui.home.contract.HomeUiEvent
import com.muhammadwahyudin.identitasku.ui.home.contract.HomeUiState
import com.muhammadwahyudin.identitasku.ui.home.contract.SortFilterViewModel
import com.muhammadwahyudin.identitasku.ui.theme.AppTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeScreen(state: HomeUiState, onEvent: (HomeUiEvent) -> Unit, modifier: Modifier = Modifier) {
    AppTheme {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary,
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { onEvent(HomeUiEvent.AddNewItem) },
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            elevation = FloatingActionButtonDefaults.elevation(),
                            content = {
                                Icon(
                                    Icons.Filled.Add,
                                    contentDescription = "Localized description",
                                    tint = MaterialTheme.colorScheme.tertiary
                                )
                            }
                        )
                    },
                    actions = {
                        AsyncImage(
                            model = R.mipmap.ic_launcher,
                            contentDescription = "Localized description",
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(onClick = { onEvent(HomeUiEvent.OpenFilterPage) }) {
                            Icon(
                                Icons.Filled.FilterList,
                                contentDescription = "Localized description",
                            )
                        }
                        IconButton(onClick = { onEvent(HomeUiEvent.OpenSettingsPage) }) {
                            Icon(
                                Icons.Filled.Settings,
                                contentDescription = "Localized description",
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                    },
                )
            },
            modifier = modifier
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                when (state) {
                    is HomeUiState.EmptyFiltered -> {

                    }

                    HomeUiState.EmptyNoData -> {

                    }

                    is HomeUiState.HasData -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = 16.dp,
                                        end = 16.dp,
                                        top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
                                    )
                            ) {
                                ElevatedAssistChip(
                                    label = {
                                        Text(
                                            text = "Sort by " + state.sort.name,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    },
                                    elevation = AssistChipDefaults.elevatedAssistChipElevation(4.dp),
                                    onClick = {
                                        onEvent(HomeUiEvent.OpenFilterPage)
                                    }
                                )
                            }
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(
                                    bottom = 32.dp + BottomAppBarDefaults.FlexibleBottomAppBarHeight + WindowInsets.navigationBars.asPaddingValues()
                                        .calculateBottomPadding(),
                                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
                                ),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                if (state.sort == SortFilterViewModel.SORT.CATEGORY) {
                                    state.datas.groupBy { it.type() }.toList().forEach { (type, items) ->
                                        stickyHeader(key = type) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .background(color = MaterialTheme.colorScheme.surfaceContainerLow)
                                            ) {
                                                Text(
                                                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                                                    text = LocalContext.current.getString(type.stringRes) + " (${items.size})"
                                                )
                                            }
                                        }
                                        itemsIndexed(items, key = { _, it -> it.id }) { index, item ->
                                            IdentitasItem(
                                                data = item,
                                                onAction = onEvent,
                                                position = index,
                                                modifier = modifier.padding(horizontal = 16.dp)
                                            )
                                        }
                                    }
                                } else {
                                    itemsIndexed(state.datas, key = { _, it -> it.id }) { index, item ->
                                        IdentitasItem(
                                            data = item,
                                            onAction = onEvent,
                                            position = index,
                                            modifier = modifier.padding(horizontal = 16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    val data by remember {
        mutableStateOf(
            (1..20).map {
                DataWithDataType(1, 1, "Value", typeName = "", isUnique = false, isCustom = false)
            }
        )
    }
    HomeScreen(
        state = HomeUiState.HasData(
            datas = data,
            filter = listOf(),
            sort = SortFilterViewModel.DEFAULT_SORT
        ), onEvent = {})
}