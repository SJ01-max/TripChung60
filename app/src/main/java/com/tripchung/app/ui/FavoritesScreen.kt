package com.tripchung.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

/* -------------------- 데이터 모델 -------------------- */

data class FavPlace(
    val id: String,
    val name: String,
    val category: String,
    val rating: Double,
    val savedDate: String,
    val tags: List<String>,
    val imageUrl: String
)

data class FavPlan(
    val id: String,
    val title: String,
    val days: Int,
    val savedDate: String,
    val coverImageUrl: String,
    val placesPreview: List<String>
)

/* -------------------- 샘플 데이터 -------------------- */

private val sampleFavPlaces = listOf(
    FavPlace(
        id = "p1",
        name = "아산 외암마을",
        category = "문화유산",
        rating = 4.6,
        savedDate = "2024.10.03",
        tags = listOf("#전통마을", "#한옥", "#가을단풍"),
        imageUrl = "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?w=1200"
    ),
    FavPlace(
        id = "p2",
        name = "대전 성심당",
        category = "베이커리 · 카페",
        rating = 4.8,
        savedDate = "2024.10.01",
        tags = listOf("#베이커리", "#튀김소보로", "#맛집"),
        imageUrl = "https://images.unsplash.com/photo-1541976076758-347942db1974?w=1200"
    ),
    FavPlace(
        id = "p3",
        name = "온양온천",
        category = "온천 · 스파",
        rating = 4.5,
        savedDate = "2024.09.28",
        tags = listOf("#온천", "#힐링", "#휴식"),
        imageUrl = "https://images.unsplash.com/photo-1501785888041-af3ef285b470?w=1200"
    )
)

private val sampleFavPlans = listOf(
    FavPlan(
        id = "pl1",
        title = "충청도 힐링 여행 2박3일",
        days = 3,
        savedDate = "2024.10.05",
        coverImageUrl = "https://images.unsplash.com/photo-1500534314209-a25ddb2bd429?w=1200",
        placesPreview = listOf("아산 외암마을", "온양온천", "+1곳")
    ),
    FavPlan(
        id = "pl2",
        title = "대전 맛집 투어 당일코스",
        days = 1,
        savedDate = "2024.10.02",
        coverImageUrl = "https://images.unsplash.com/photo-1544025162-d76694265947?w=1200",
        placesPreview = listOf("성심당", "중앙시장", "+1곳")
    )
)

/* -------------------- 화면 -------------------- */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    onAddPlaceToPlan: (FavPlace) -> Unit = {},
    onOpenPlan: (FavPlan) -> Unit = {},
    onDuplicatePlan: (FavPlan) -> Unit = {},
    onSharePlace: (FavPlace) -> Unit = {},
    onSharePlan: (FavPlan) -> Unit = {},
    onEditTap: () -> Unit = {}
) {
    var showPlaces by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("찜한 목록", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text(
                            "마음에 든 장소와 일정을 모아보세요",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    TextButton(onClick = onEditTap) { Text("편집") }
                }
            )
        }
    ) { inner ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(inner)
        ) {
            // 세그먼트 스타일 탭
            SegmentBar(
                leftLabel = "장소 (${sampleFavPlaces.size})",
                rightLabel = "일정 (${sampleFavPlans.size})",
                leftSelected = showPlaces,
                onLeft = { showPlaces = true },
                onRight = { showPlaces = false }
            )

            if (showPlaces) {
                PlaceList(
                    places = sampleFavPlaces,
                    onAdd = onAddPlaceToPlan,
                    onShare = onSharePlace
                )
            } else {
                PlanList(
                    plans = sampleFavPlans,
                    onOpen = onOpenPlan,
                    onDuplicate = onDuplicatePlan,
                    onShare = onSharePlan
                )
            }
        }
    }
}

/* -------------------- 리스트 UI -------------------- */

@Composable
private fun PlaceList(
    places: List<FavPlace>,
    onAdd: (FavPlace) -> Unit,
    onShare: (FavPlace) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(places, key = { it.id }) { p ->
            ElevatedCard(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                // 커버 + 오버레이
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                ) {
                    AsyncImage(
                        model = p.imageUrl,
                        contentDescription = p.name,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    0f to Color.Transparent,
                                    1f to Color.Black.copy(alpha = 0.35f)
                                )
                            )
                    )
                    Row(
                        Modifier
                            .align(Alignment.BottomStart)
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(50)
                        ) {
                            Row(
                                Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Place,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    p.category,
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }
                        Spacer(Modifier.width(8.dp))
                        RatingChip(p.rating)
                    }
                    IconButton(
                        onClick = { /* 찜 토글 */ },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .size(36.dp)
                            .background(Color.Black.copy(alpha = 0.25f), CircleShape)
                    ) {
                        Icon(
                            Icons.Outlined.FavoriteBorder,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }

                Column(Modifier.padding(horizontal = 14.dp, vertical = 12.dp)) {
                    Text(p.name, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Spacer(Modifier.height(4.dp))
                    Text("저장: ${p.savedDate}", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                    Spacer(Modifier.height(8.dp))
                    FlowTags(p.tags)
                    Spacer(Modifier.height(12.dp))
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { onAdd(p) },
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) { Text("일정에 추가") }

                        OutlinedIconButton(
                            onClick = { onShare(p) },
                            modifier = Modifier.size(44.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) { Icon(Icons.Outlined.Share, null) }
                    }
                }
            }
        }
    }
}

@Composable
private fun PlanList(
    plans: List<FavPlan>,
    onOpen: (FavPlan) -> Unit,
    onDuplicate: (FavPlan) -> Unit,
    onShare: (FavPlan) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(plans, key = { it.id }) { plan ->
            ElevatedCard(shape = RoundedCornerShape(20.dp)) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                ) {
                    AsyncImage(model = plan.coverImageUrl, contentDescription = plan.title, modifier = Modifier.fillMaxSize())
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    0f to Color.Transparent,
                                    1f to Color.Black.copy(alpha = 0.35f)
                                )
                            )
                    )
                    Column(
                        Modifier
                            .align(Alignment.BottomStart)
                            .padding(12.dp)
                    ) {
                        Text(
                            plan.title,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            SmallGlassChip(text = "${plan.days}일 코스")
                            SmallGlassChip(text = "저장 ${plan.savedDate}")
                        }
                    }
                    IconButton(
                        onClick = { /* more */ },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .size(36.dp)
                            .background(Color.Black.copy(alpha = 0.25f), CircleShape)
                    ) {
                        Icon(Icons.Outlined.MoreHoriz, null, tint = Color.White)
                    }
                }

                Column(Modifier.padding(horizontal = 14.dp, vertical = 12.dp)) {
                    FlowTags(plan.placesPreview)
                    Spacer(Modifier.height(12.dp))
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { onOpen(plan) },
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) { Text("일정보기") }

                        OutlinedButton(
                            onClick = { onDuplicate(plan) },
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) { Text("복사하기") }

                        OutlinedIconButton(
                            onClick = { onShare(plan) },
                            modifier = Modifier.size(44.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) { Icon(Icons.Outlined.Share, null) }
                    }
                }
            }
        }
    }
}

/* -------------------- 보조 UI -------------------- */

@Composable
private fun SegmentBar(
    leftLabel: String,
    rightLabel: String,
    leftSelected: Boolean,
    onLeft: () -> Unit,
    onRight: () -> Unit
) {
    val bg = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    val shape = RoundedCornerShape(14.dp)

    Row(
        Modifier
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .clip(shape)
            .background(bg)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilterChip(
            selected = leftSelected,
            onClick = onLeft,
            label = { Text(leftLabel) },
            leadingIcon = {
                Icon(
                    if (leftSelected) Icons.Outlined.Place else Icons.Outlined.Place,
                    contentDescription = null
                )
            },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(10.dp)
        )
        Spacer(Modifier.width(8.dp))
        FilterChip(
            selected = !leftSelected,
            onClick = onRight,
            label = { Text(rightLabel) },
            leadingIcon = { Icon(Icons.Outlined.Event, null) },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(10.dp)
        )
    }
}

@Composable
private fun RatingChip(rating: Double) {
    Surface(
        color = Color.Black.copy(alpha = 0.35f),
        contentColor = Color.White,
        shape = RoundedCornerShape(50)
    ) {
        Row(
            Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Outlined.Star, null, tint = Color(0xFFFFD54F))
            Spacer(Modifier.width(4.dp))
            Text("%.1f".format(rating), style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
private fun SmallGlassChip(text: String) {
    Box(
        Modifier
            .clip(RoundedCornerShape(50))
            .background(Color.White.copy(alpha = 0.25f))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(text, color = Color.White, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun FlowTags(tags: List<String>) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        tags.forEach {
            Box(
                Modifier
                    .clip(RoundedCornerShape(50))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(it, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
