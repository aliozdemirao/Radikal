package com.aliozdemir.radikal.ui.detail

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.aliozdemir.radikal.R
import com.aliozdemir.radikal.domain.model.Article
import com.aliozdemir.radikal.ui.detail.DetailContract.UiAction
import com.aliozdemir.radikal.ui.detail.DetailContract.UiEffect
import com.aliozdemir.radikal.ui.detail.DetailContract.UiState
import com.aliozdemir.radikal.util.formatPublishedDate
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DetailScreen(
    article: Article,
    uiState: UiState,
    onAction: (UiAction) -> Unit,
    uiEffect: SharedFlow<UiEffect>,
    onNavigateBack: () -> Unit,
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    LaunchedEffect(article) {
        onAction(UiAction.LoadArticleDetails(article))
    }

    LaunchedEffect(Unit) {
        uiEffect.collectLatest { effect ->
            when (effect) {
                is UiEffect.OpenUrlInBrowser -> {
                    val intent = Intent(Intent.ACTION_VIEW, effect.url.toUri())
                    context.startActivity(intent)
                }

                is UiEffect.ShareArticle -> {
                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, effect.url)
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share via"))
                }

                UiEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DetailTopAppBar(
                onNavigateUp = { onAction(UiAction.OnNavigateUpClicked) },
                onBrowserClick = { onAction(UiAction.OnBrowserClicked) },
                onShareClick = { onAction(UiAction.OnShareClicked) }
            )
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
        ) {
            AsyncImage(
                model = ImageRequest
                    .Builder(LocalContext.current)
                    .data(uiState.article?.urlToImage)
                    .crossfade(true)
                    .build(),
                contentDescription = uiState.article?.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16 / 9f)
                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                    .clip(RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)),
                placeholder = painterResource(id = R.drawable.placeholder_image),
                error = painterResource(id = R.drawable.error_image)
            )

            Text(
                text = uiState.article?.title ?: stringResource(R.string.article_default_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = uiState.article?.source?.name ?: stringResource(R.string.article_default_source),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f, fill = false),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = formatPublishedDate(uiState.article?.publishedAt),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = uiState.article?.description ?: stringResource(R.string.article_default_description),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Text(
                text = uiState.article?.content ?: stringResource(R.string.article_default_content),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTopAppBar(
    onNavigateUp: () -> Unit,
    onBrowserClick: () -> Unit,
    onShareClick: () -> Unit,
) {
    TopAppBar(
        title = { Text(stringResource(R.string.detail_screen_title)) },
        navigationIcon = {
            IconButton(onClick = onNavigateUp) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        },
        actions = {
            IconButton(onClick = onBrowserClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_language),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            IconButton(onClick = onShareClick) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    )
}