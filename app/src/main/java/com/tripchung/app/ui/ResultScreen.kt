package com.tripchung.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    onAdd: (PlaceItem) -> Unit = {},
    onToggleWish: (PlaceItem) -> Unit = {}
) {
    val items = remember { sampleResults }

    // 헤더
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(12.dp))

        // 상단 그라데이션 헤더
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFE8F7ED), Color(0xFFF5FBF7))
                    )
                )
                .padding(vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF6BC48C).copy(alpha = 0.35f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("✨", fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    "AI 맞춤 여행 추천",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    "당신의 취향에 맞는 완벽한 여행지를 찾았어요!",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // 리스트
        items.forEach { item ->
            ResultCard(
                item = item,
                onAdd = onAdd,
                onToggleWish = onToggleWish
            )
            Spacer(Modifier.height(14.dp))
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun ResultCard(
    item: PlaceItem,
    onAdd: (PlaceItem) -> Unit,
    onToggleWish: (PlaceItem) -> Unit
) {
    ElevatedCard(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFFFAFDF8)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = item.image,
                    contentDescription = null,
                    modifier = Modifier
                        .size(96.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            item.title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(Modifier.width(8.dp))
                        SuggestTag(item.tag)
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(
                        item.desc,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            // 소요시간/거리
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Icon(Icons.Outlined.AccessTime, contentDescription = null)
                Text(item.time, fontWeight = FontWeight.Medium)
                Spacer(Modifier.width(8.dp))
                Icon(Icons.Outlined.Place, contentDescription = null)
                Text(item.distance, fontWeight = FontWeight.Medium)
            }

            Spacer(Modifier.height(12.dp))

            // 액션 버튼들
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = { onAdd(item) },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("일정에 추가")
                }
                FilledTonalIconButton(
                    onClick = { onToggleWish(item) },
                    modifier = Modifier.size(48.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Outlined.FavoriteBorder, contentDescription = null)
                }
            }
        }
    }
}

@Composable
private fun SuggestTag(text: String) {
    Surface(
        color = Color(0xFFE6F5EA),
        contentColor = Color(0xFF3A915F),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

/* ---------------- 샘플 데이터 ---------------- */

data class PlaceItem(
    val title: String,
    val tag: String,
    val desc: String,
    val time: String,
    val distance: String,
    val image: String
)

private val sampleResults = listOf(
    PlaceItem(
        title = "계룡산 국립공원",
        tag = "자연/힐링",
        desc = "가을 단풍이 아름다운 명산으로 힐링과 트레킹을 동시에 즐길 수 있습니다.",
        time = "2-3시간",
        distance = "1.2km",
        image = "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?w=1200"
    ),
    PlaceItem(
        title = "부여 궁남지",
        tag = "역사/문화",
        desc = "백제의 역사가 살아있는 연못으로 사계절 아름다운 풍경을 자랑합니다.",
        time = "1-2시간",
        distance = "800m",
        image = "https://images.unsplash.com/photo-1470770903676-69b98201ea1c?w=1200"
    )
)
