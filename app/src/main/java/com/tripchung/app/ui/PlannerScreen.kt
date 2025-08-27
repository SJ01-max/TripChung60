package com.tripchung.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlannerScreen(
    onNavChange: (String) -> Unit = {} // 필요하면 바텀탭 이동 콜백
) {
    val categories = listOf(
        "힐링 여행", "액티비티 여행",
        "문화 탐방", "맛집 투어",
        "자연 감상", "사진 촬영"
    )
    var selected by remember { mutableStateOf<String?>(null) }

    Scaffold(
        bottomBar = {
            NavigationBar(tonalElevation = 0.dp) {
                NavigationBarItem(
                    selected = true,
                    onClick = { onNavChange("home") },
                    icon = { Icon(Icons.Outlined.Home, null) },
                    label = { Text("홈") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { onNavChange("search") },
                    icon = { Icon(Icons.Outlined.Search, null) },
                    label = { Text("검색") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { onNavChange("nearby") },
                    icon = { Icon(Icons.Outlined.LocationOn, null) },
                    label = { Text("주변") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { onNavChange("fav") },
                    icon = { Icon(Icons.Outlined.FavoriteBorder, null) },
                    label = { Text("찜") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { onNavChange("me") },
                    icon = { Icon(Icons.Outlined.Person, null) },
                    label = { Text("마이") }
                )
            }
        }
    ) { inner ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // 상단 타이틀 라인
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("질문 1/4", style = MaterialTheme.typography.labelLarge)
                TextButton(onClick = { /* TODO: 건너뛰기 */ }) {
                    Text("건너뛰기")
                }
            }

            // 진행바
            LinearProgressIndicator(
                progress = { 0.25f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(Modifier.height(24.dp))

            // 동그란 아이콘
            Box(
                modifier = Modifier
                    .size(108.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.25f))
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.Explore,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(Modifier.height(16.dp))
            Text(
                text = "AI 여행 플래너",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "몇 가지 질문으로 완벽한 여행을 만들어드릴게요",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            )

            Spacer(Modifier.height(20.dp))

            // 카드 안의 질문 + 카테고리 그리드
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text(
                        "어떤 여행을 원하시나요?",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(12.dp))

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.heightIn(min = 0.dp)
                    ) {
                        items(categories) { c ->
                            val isSel = selected == c
                            OutlinedButton(
                                onClick = { selected = if (isSel) null else c },
                                shape = RoundedCornerShape(18.dp),
                                border = if (isSel) ButtonDefaults.outlinedButtonBorder.copy(
                                    brush = SolidColor(MaterialTheme.colorScheme.primary)
                                ) else ButtonDefaults.outlinedButtonBorder,
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (isSel)
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                                    else
                                        MaterialTheme.colorScheme.surface
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(64.dp)
                            ) {
                                Text(
                                    c,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = if (isSel)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // 다음 버튼
            Button(
                onClick = { /* TODO: 다음 질문 화면으로 이동 */ },
                enabled = selected != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("다음")
            }
        }
    }
}
