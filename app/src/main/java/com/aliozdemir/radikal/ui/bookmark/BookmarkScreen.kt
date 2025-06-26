package com.aliozdemir.radikal.ui.bookmark

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.aliozdemir.radikal.R
import com.aliozdemir.radikal.domain.model.Article
import com.aliozdemir.radikal.ui.bookmark.BookmarkContract.UiAction
import com.aliozdemir.radikal.ui.bookmark.BookmarkContract.UiEffect
import com.aliozdemir.radikal.ui.bookmark.BookmarkContract.UiState
import com.aliozdemir.radikal.util.formatPublishedDate
import kotlinx.coroutines.flow.SharedFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkScreen(
    uiState: UiState,
    onAction: (UiAction) -> Unit,
    uiEffect: SharedFlow<UiEffect>,
    onArticleClick: (Article) -> Unit,
) {
    var showMenu by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        uiEffect.collect { effect ->
            when (effect) {
                is UiEffect.NavigateToDetail -> onArticleClick(effect.article)
            }
        }
    }

    Scaffold(
        topBar = {
            BookmarkTopAppBar(
                showMenu = showMenu,
                onMenuToggle = { showMenu = !showMenu },
                onDeleteAllClicked = {
                    showMenu = false
                    onAction(UiAction.OnDeleteAllArticlesClicked)
                }
            )
        }
    ) { paddingValues ->
        BookmarkContent(
            modifier = Modifier.padding(paddingValues),
            uiState = uiState,
            onAction = onAction
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkTopAppBar(
    showMenu: Boolean,
    onMenuToggle: () -> Unit,
    onDeleteAllClicked: () -> Unit,
) {
    TopAppBar(
        title = { Text(stringResource(R.string.bookmark_screen_title)) },
        actions = {
            IconButton(onClick = onMenuToggle) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null
                )
            }
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = onMenuToggle
            ) {
                DropdownMenuItem(
                    text = { Text("Delete All") },
                    onClick = onDeleteAllClicked
                )
            }
        }
    )
}

@Composable
fun BookmarkContent(
    modifier: Modifier = Modifier,
    uiState: UiState,
    onAction: (UiAction) -> Unit,
) {
    when {
        uiState.bookmarkedArticles.isEmpty() -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No Bookmarked Articles",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        else -> {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = uiState.bookmarkedArticles,
                    key = { it.url!! },
                ) { article ->
                    BookmarkArticleCard(
                        article = article,
                        onArticleClick = { onAction(UiAction.OnArticleClicked(it)) },
                        onDeleteClick = { onAction(UiAction.OnDeleteArticleClicked(it)) }
                    )
                }
            }
        }
    }
}

@Composable
fun BookmarkArticleCard(
    article: Article,
    onArticleClick: (Article) -> Unit,
    onDeleteClick: (String) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onArticleClick(article) }
            .clip(RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
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
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = article.title ?: stringResource(R.string.article_default_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = article.source?.name ?: stringResource(R.string.article_default_source),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = formatPublishedDate(article.publishedAt),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = { onDeleteClick(article.url!!) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}