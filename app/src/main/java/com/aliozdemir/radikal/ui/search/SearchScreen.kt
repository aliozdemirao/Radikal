package com.aliozdemir.radikal.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.aliozdemir.radikal.R
import com.aliozdemir.radikal.domain.model.Article
import com.aliozdemir.radikal.ui.search.SearchContract.FilterOptions
import com.aliozdemir.radikal.ui.search.SearchContract.UiAction
import com.aliozdemir.radikal.ui.search.SearchContract.UiEffect
import com.aliozdemir.radikal.ui.search.SearchContract.UiState
import com.aliozdemir.radikal.util.formatPublishedDate
import kotlinx.coroutines.flow.SharedFlow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun SearchScreen(
    uiState: UiState,
    onAction: (UiAction) -> Unit,
    uiEffect: SharedFlow<UiEffect>,
    onArticleClick: (Article) -> Unit,
) {
    LaunchedEffect(Unit) {
        uiEffect.collect { effect ->
            when (effect) {
                is UiEffect.NavigateToDetail -> onArticleClick(effect.article)
            }
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SearchTopBar(
                searchQuery = uiState.searchQuery,
                onQueryChanged = { onAction(UiAction.OnSearchQueryChanged(it)) },
                onSearchClick = { onAction(UiAction.OnSearchClicked) },
                onFilterClick = { onAction(UiAction.OnFilterIconClicked) })

            SearchContent(
                uiState = uiState,
                onArticleClick = { onAction(UiAction.OnArticleClicked(it)) },
            )

            if (uiState.showFilterDialog) {
                FilterDialog(
                    currentFilterOptions = uiState.filterOptions,
                    languageMap = uiState.languageMap,
                    sortByMap = uiState.sortByMap,
                    searchInMap = uiState.searchInMap,
                    onDismiss = { onAction(UiAction.OnFilterDialogDismissed) },
                    onApply = { onAction(UiAction.OnFilterApplied(it)) },
                    onClearFilters = { onAction(UiAction.OnClearFiltersClicked) })
            }
        }
    }
}

@Composable
fun SearchTopBar(
    searchQuery: String,
    onQueryChanged: (String) -> Unit,
    onSearchClick: () -> Unit,
    onFilterClick: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = searchQuery,
        onValueChange = onQueryChanged,
        label = { Text("Search") },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearchClick()
                keyboardController?.hide()
            }),
        trailingIcon = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {
                    onSearchClick()
                    keyboardController?.hide()
                }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                }
                IconButton(onClick = onFilterClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_filter), contentDescription = null
                    )
                }
            }
        })
}

@Composable
fun SearchContent(
    uiState: UiState,
    onArticleClick: (Article) -> Unit,
) {
    when {
        uiState.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        uiState.error != null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = uiState.error,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        uiState.searchResults.isEmpty() && uiState.searchQuery.isNotEmpty() -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "No news found.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        uiState.searchResults.isEmpty() && uiState.searchQuery.isEmpty() -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Welcome Search",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        else -> {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.searchResults) { article ->
                    SearchArticleCard(
                        article = article,
                        onArticleClick = onArticleClick
                    )
                }
            }
        }
    }
}

@Composable
fun SearchArticleCard(
    article: Article,
    onArticleClick: (Article) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onArticleClick(article) },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            AsyncImage(
                model = ImageRequest
                    .Builder(LocalContext.current)
                    .data(article.urlToImage)
                    .crossfade(true)
                    .build(),
                contentDescription = article.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16 / 9f),
                placeholder = painterResource(id = R.drawable.placeholder_image),
                error = painterResource(id = R.drawable.error_image)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Text(
                    text = article.title ?: stringResource(R.string.article_default_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = article.source?.name ?: stringResource(R.string.article_default_source),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formatPublishedDate(article.publishedAt),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDialog(
    currentFilterOptions: FilterOptions,
    languageMap: Map<String, String>,
    sortByMap: Map<String, String>,
    searchInMap: Map<String, String>,
    onDismiss: () -> Unit,
    onApply: (FilterOptions) -> Unit,
    onClearFilters: () -> Unit,
) {
    var filterOptions by remember { mutableStateOf(currentFilterOptions) }
    var showFromDatePicker by remember { mutableStateOf(false) }
    var showToDatePicker by remember { mutableStateOf(false) }

    val fromDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = filterOptions.fromDate?.let { dateString ->
            runCatching {
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateString)?.time
            }.getOrNull()
        })
    val toDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = filterOptions.toDate?.let { dateString ->
            runCatching {
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateString)?.time
            }.getOrNull()
        })

    AlertDialog(onDismissRequest = onDismiss, title = { Text("Search Filters") }, text = {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            DropdownSelector(
                label = "Language",
                options = languageMap.keys.toList(),
                optionToText = { key -> languageMap[key] ?: key },
                selectedOption = filterOptions.language,
                onOptionSelected = { filterOptions = filterOptions.copy(language = it) })
            Spacer(modifier = Modifier.height(12.dp))

            DropdownSelector(
                label = "Sort By",
                options = sortByMap.keys.toList(),
                optionToText = { key -> sortByMap[key] ?: key },
                selectedOption = filterOptions.sortBy,
                onOptionSelected = { filterOptions = filterOptions.copy(sortBy = it) })
            Spacer(modifier = Modifier.height(12.dp))

            MultiSelectDropdown(
                label = "Search In",
                options = searchInMap.keys.toList(),
                optionToText = { key -> searchInMap[key] ?: key },
                selectedOptions = filterOptions.searchIn ?: emptyList(),
                onOptionsSelected = {
                    filterOptions = filterOptions.copy(searchIn = it.takeIf { it.isNotEmpty() })
                })
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = filterOptions.sources ?: "",
                onValueChange = {
                    filterOptions = filterOptions.copy(sources = it.takeIf { it.isNotBlank() })
                },
                label = { Text("Sources (comma-separated)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = filterOptions.domains ?: "",
                onValueChange = {
                    filterOptions = filterOptions.copy(domains = it.takeIf { it.isNotBlank() })
                },
                label = { Text("Domains (comma-separated)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = filterOptions.excludeDomains ?: "",
                onValueChange = {
                    filterOptions =
                        filterOptions.copy(excludeDomains = it.takeIf { it.isNotBlank() })
                },
                label = { Text("Exclude Domains (comma-separated)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = filterOptions.fromDate ?: "",
                onValueChange = { /* read only */ },
                label = { Text("From Date") },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showFromDatePicker = true },
                trailingIcon = {
                    IconButton(onClick = { showFromDatePicker = true }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Select from date")
                    }
                })
            if (showFromDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showFromDatePicker = false },
                    confirmButton = {
                        Button(onClick = {
                            fromDatePickerState.selectedDateMillis?.let { millis ->
                                val calendar =
                                    Calendar.getInstance().apply { timeInMillis = millis }
                                val formattedDate =
                                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                        calendar.time
                                    )
                                filterOptions = filterOptions.copy(fromDate = formattedDate)
                            }
                            showFromDatePicker = false
                        }) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showFromDatePicker = false }) {
                            Text("Cancel")
                        }
                    }) {
                    DatePicker(state = fromDatePickerState)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = filterOptions.toDate ?: "",
                onValueChange = { /* read only */ },
                label = { Text("To Date") },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showToDatePicker = true },
                trailingIcon = {
                    IconButton(onClick = { showToDatePicker = true }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Select to date")
                    }
                })
            if (showToDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showToDatePicker = false },
                    confirmButton = {
                        Button(onClick = {
                            toDatePickerState.selectedDateMillis?.let { millis ->
                                val calendar =
                                    Calendar.getInstance().apply { timeInMillis = millis }
                                val formattedDate =
                                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                        calendar.time
                                    )
                                filterOptions = filterOptions.copy(toDate = formattedDate)
                            }
                            showToDatePicker = false
                        }) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showToDatePicker = false }) {
                            Text("Cancel")
                        }
                    }) {
                    DatePicker(state = toDatePickerState)
                }
            }
        }
    }, confirmButton = {
        Button(onClick = { onApply(filterOptions) }) {
            Text("Apply Filters")
        }
    }, dismissButton = {
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = onClearFilters) {
                Text("Clear All")
            }
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSelector(
    label: String,
    options: List<String>,
    optionToText: (String) -> String,
    selectedOption: String?,
    onOptionSelected: (String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedOption?.let { optionToText(it) } ?: "Select",
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryEditable, enabled = true)
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = { Text("None") }, onClick = {
                onOptionSelected(null)
                expanded = false
            })
            options.forEach { option ->
                DropdownMenuItem(text = { Text(optionToText(option)) }, onClick = {
                    onOptionSelected(option)
                    expanded = false
                })
            }
        }
    }
}


@Composable
fun MultiSelectDropdown(
    label: String,
    options: List<String>,
    optionToText: (String) -> String,
    selectedOptions: List<String>,
    onOptionsSelected: (List<String>) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedOptions.joinToString { optionToText(it) },
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = if (expanded) "Close" else "Open"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded })

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .fillMaxWidth()
        ) {
            options.forEach { option ->
                val isSelected = option in selectedOptions
                DropdownMenuItem(text = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isSelected, onCheckedChange = { isChecked ->
                                val newSelection = if (isChecked) {
                                    selectedOptions + option
                                } else {
                                    selectedOptions - option
                                }
                                onOptionsSelected(newSelection)
                            }, modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(text = optionToText(option))
                    }
                }, onClick = { })
            }
        }
    }
}