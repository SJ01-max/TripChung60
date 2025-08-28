@file:OptIn(ExperimentalMaterial3Api::class)

package com.tripchung.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

@Composable
fun ResultScreen(
    onBack: () -> Unit = {},
    onAddToPlan: (String) -> Unit = {},
    plannerViewModel: PlannerViewModel = viewModel()
) {
    val answers = plannerViewModel.answers
    val results = plannerViewModel.results

    val subtitle = remember(answers) {
        if (answers == null) "설문 응답이 없습니다. 다시 시도해 주세요."
        else listOfNotNull(answers.tripType, answers.withWhom, answers.mood, answers.transport)
            .joinToString(" · ")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI 맞춤 여행 추천", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Outlined.ArrowBack, null) }
                }
            )
        }
    ) { inner ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                ElevatedCard(shape = RoundedCornerShape(18.dp)) {
                    Column(Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                Modifier.size(44.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) { Icon(Icons.Outlined.AutoAwesome, null, tint = MaterialTheme.colorScheme.primary) }
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text("당신을 위한 추천을 찾았어요!", fontWeight = FontWeight.SemiBold)
                                Text(subtitle, style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        if (answers == null) {
                            Spacer(Modifier.height(8.dp))
                            TextButton(onClick = onBack) { Text("설문 다시 하기") }
                        }
                    }
                }
            }

            items(results, key = { it.id }) { r ->
                RecommendResultCard(
                    item = r,
                    onAddToPlan = { onAddToPlan(r.title) },
                    onShare = { /* 공유 */ },
                    onLike = { /* 찜 */ }
                )
            }

            if (results.isEmpty()) {
                item {
                    OutlinedCard(shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Outlined.SearchOff, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(Modifier.height(8.dp))
                            Text("조건에 맞는 결과가 없어요", fontWeight = FontWeight.SemiBold)
                            Text("조건을 바꿔 다시 시도해보세요.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(Modifier.height(8.dp))
                            TextButton(onClick = onBack) { Text("설문 다시 하기") }
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(4.dp)) }
        }
    }
}

@Composable
private fun RecommendResultCard(
    item: RecommendPlace,
    onAddToPlan: () -> Unit,
    onShare: () -> Unit,
    onLike: () -> Unit
) {
    ElevatedCard(shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) {
        Box(Modifier.height(160.dp).fillMaxWidth()) {
            AsyncImage(model = item.imageUrl, contentDescription = item.title, modifier = Modifier.fillMaxSize())
            Row(
                Modifier.fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            0f to Color.Black.copy(alpha = 0.35f),
                            1f to Color.Transparent
                        )
                    )
                    .padding(10.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Surface(color = Color.White.copy(alpha = 0.25f), shape = CircleShape) {
                    Row(Modifier.padding(horizontal = 10.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Star, null, tint = Color(0xFFFFD54F))
                        Spacer(Modifier.width(4.dp))
                        Text(String.format("%.1f", item.rating), color = Color.White, style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }

        Column(Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(item.title, fontWeight = FontWeight.Bold)
                AssistChip(onClick = {}, label = { Text(item.tag) }, leadingIcon = { Icon(Icons.Outlined.Label, null) })
            }

            Spacer(Modifier.height(6.dp))
            Text(item.desc, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(6.dp))
            Text("${item.time}   ${item.distance}", color = MaterialTheme.colorScheme.onSurfaceVariant)

            if (item.badges.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    item.badges.forEach { b ->
                        Surface(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                            contentColor = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(b, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onAddToPlan,
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(12.dp)
                ) { Text("일정에 추가") }

                OutlinedIconButton(onClick = onShare, modifier = Modifier.size(48.dp), shape = RoundedCornerShape(12.dp)) {
                    Icon(Icons.Outlined.Share, null)
                }
                OutlinedIconButton(onClick = onLike, modifier = Modifier.size(48.dp), shape = RoundedCornerShape(12.dp)) {
                    Icon(Icons.Outlined.FavoriteBorder, null)
                }
            }
        }
    }
}
