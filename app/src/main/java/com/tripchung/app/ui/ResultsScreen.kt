// ResultScreen.kt
@file:OptIn(ExperimentalMaterial3Api::class)
package com.tripchung.app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun ResultScreen(
    onBack: () -> Unit = {},
    onAddToPlan: (String) -> Unit = {}
) {
    Scaffold(
        topBar = {
            // ⬇️ SmallTopAppBar → TopAppBar 로 교체
            TopAppBar(
                title = { Text("AI 맞춤 여행 추천", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "뒤로")
                    }
                }
            )
        }
    ) { inner ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "당신의 취향에 맞는 완벽한 여행지를 찾았어요!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            RecommendResultCard(
                title = "계룡산 국립공원",
                tag = "자연/힐링",
                description = "가을 단풍이 아름다운 명산으로 힐링과 트레킹을 동시에 즐길 수 있습니다.",
                time = "⏱ 2-3시간",
                distance = "📍 1.2km",
                imageUrl = "https://images.unsplash.com/photo-1501785888041-af3ef285b470?w=800",
                onAddToPlan = { onAddToPlan("계룡산 국립공원") }
            )

            RecommendResultCard(
                title = "부여 궁남지",
                tag = "역사/문화",
                description = "백제의 역사가 살아있는 연못으로 사계절 아름다운 풍경을 자랑합니다.",
                time = "⏱ 1-2시간",
                distance = "📍 800m",
                imageUrl = "https://images.unsplash.com/photo-1470770841072-f978cf4d019e?w=800",
                onAddToPlan = { onAddToPlan("부여 궁남지") }
            )
        }
    }
}

@Composable
private fun RecommendResultCard(
    title: String,
    tag: String,
    description: String,
    time: String,
    distance: String,
    imageUrl: String,
    onAddToPlan: () -> Unit
) {
    ElevatedCard(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentScale = ContentScale.Crop
            )

            Column(Modifier.padding(16.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(title, fontWeight = FontWeight.Bold)
                    AssistChip(onClick = {}, label = { Text(tag) })
                }

                Spacer(Modifier.height(6.dp))
                Text(description, style = MaterialTheme.typography.bodyMedium)

                Spacer(Modifier.height(6.dp))
                Text("$time   $distance", color = Color.Gray)

                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = onAddToPlan,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("일정에 추가")
                }
            }
        }
    }
}
