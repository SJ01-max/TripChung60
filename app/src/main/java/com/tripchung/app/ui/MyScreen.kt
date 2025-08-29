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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

/* ---------- 샘플 데이터(웹 코드 매핑) ---------- */

private data class Stat(val label: String, val value: String, val icon: ImageVector, val tint: Color)
private val userStats = listOf(
    Stat("방문한 곳", "23", Icons.Outlined.Place, Color(0xFF10B981)),          // emerald-600
    Stat("저장한 일정", "8", Icons.Outlined.Bookmark, Color(0xFF2563EB)),       // blue-600
    Stat("여행 일수", "47", Icons.Outlined.CalendarMonth, Color(0xFF7C3AED)),    // purple-600
    Stat("찜한 장소", "31", Icons.Outlined.Favorite, Color(0xFFEF4444)),        // red-600
)

private data class Menu(val icon: ImageVector, val label: String, val action: String)
private val menuItems = listOf(
    Menu(Icons.Outlined.ManageAccounts, "프로필 수정", "profile"),
    Menu(Icons.Outlined.History, "여행 기록", "history"),
    Menu(Icons.Outlined.Notifications, "알림 설정", "notifications"),
    Menu(Icons.Outlined.HeadsetMic, "고객센터", "help"),
    Menu(Icons.Outlined.Info, "앱 정보", "info"),
    Menu(Icons.Outlined.Logout, "로그아웃", "logout")
)

public data class Travel(val id: String, val title: String, val date: String, val places: Int, val image: String)
private val recentTravels = listOf(
    Travel(
        "1", "부여 역사문화 탐방", "2024.09.15", 3,
        "https://readdy.ai/api/search-image?query=Korean%20historical%20site%20with%20autumn%20colors%2C%20traditional%20architecture%2C%20cultural%20heritage%2C%20peaceful%20historic%20atmosphere%2C%20golden%20hour%20lighting&width=280&height=180&seq=71&orientation=landscape"
    ),
    Travel(
        "2", "아산 온천 힐링 여행", "2024.08.22", 2,
        "https://readdy.ai/api/search-image?query=Korean%20traditional%20hot%20spring%20spa%20resort%2C%20relaxing%20thermal%20pools%2C%20autumn%20mountain%20scenery%2C%20peaceful%20wellness%20center%2C%20warm%20therapeutic%20waters&width=280&height=180&seq=72&orientation=landscape"
    )
)

/* ---------- 화면 ---------- */

@Composable
fun MyScreen(
    onMenuClick: (String) -> Unit = {},
    onSeeAllTrips: () -> Unit = {},
    onRecentTripClick: (Travel) -> Unit = {}
) {
    // 웹 코드의 상태값
    var showProfileEdit by remember { mutableStateOf(false) }
    var userName by remember { mutableStateOf("김여행") }
    var userIntro by remember { mutableStateOf("충청도 곳곳을 탐험하는 여행러버 🌿") }

    // 상단 헤더(프로젝트 홈 화면과 톤 맞춤)
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Logo / 알림
        item {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "트립충",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF2A8C54),
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("충청도 AI 여행", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                BadgedBox(badge = { Badge { Text("4") } }) {
                    IconButton(onClick = { /* 알림 */ }) { Icon(Icons.Outlined.NotificationsNone, null) }
                }
            }
        }

        // 프로필 카드
        item {
            ElevatedCard(shape = RoundedCornerShape(24.dp)) {
                Column(Modifier.fillMaxWidth().padding(16.dp)) {
                    // 아바타/이름/소개
                    Column(
                        Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            Modifier.size(96.dp).clip(CircleShape)
                                .background(Color(0xFF34C759)), // emerald 그라데이션 대체
                            contentAlignment = Alignment.Center
                        ) {
                            Text("김", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        Spacer(Modifier.height(10.dp))
                        Text(userName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(4.dp))
                        Text(
                            userIntro,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                    // 통계 4칸
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        userStats.forEach { s ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    Modifier.size(48.dp).clip(RoundedCornerShape(14.dp))
                                        .background(Color(0xFFF7F7F7)),
                                    contentAlignment = Alignment.Center
                                ) { Icon(s.icon, null, tint = s.tint) }
                                Spacer(Modifier.height(4.dp))
                                Text(s.value, fontWeight = FontWeight.Bold)
                                Text(s.label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }

        // 최근 여행 헤더
        item {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("최근 여행", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                TextButton(onClick = onSeeAllTrips) { Text("전체보기") }
            }
        }

        // 최근 여행 리스트
        items(recentTravels) { t ->
            ElevatedCard(
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onRecentTripClick(t) }
            ) {
                Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = t.image, contentDescription = null,
                        modifier = Modifier.size(64.dp).clip(RoundedCornerShape(12.dp))
                    )
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(t.title, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Spacer(Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(t.date, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(Modifier.width(10.dp))
                            Text("방문 ${t.places}곳", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    Icon(Icons.Outlined.KeyboardArrowRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        // 메뉴 카드
        item {
            ElevatedCard(shape = RoundedCornerShape(18.dp)) {
                Column {
                    menuItems.forEachIndexed { idx, m ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    when (m.action) {
                                        "profile" -> showProfileEdit = true
                                        "logout" -> onMenuClick("logout")
                                        else -> onMenuClick(m.action)
                                    }
                                }
                                .padding(horizontal = 16.dp, vertical = 18.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                Modifier.size(40.dp).clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceVariant),
                                contentAlignment = Alignment.Center
                            ) { Icon(m.icon, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) }
                            Spacer(Modifier.width(12.dp))
                            Text(m.label, modifier = Modifier.weight(1f))
                            Icon(Icons.Outlined.KeyboardArrowRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        if (idx != menuItems.lastIndex) Divider(color = MaterialTheme.colorScheme.surfaceVariant)
                    }
                }
            }
        }

        item { Spacer(Modifier.height(24.dp)) }
    }

    /* --------- 프로필 수정 다이얼로그(웹의 모달 대체) --------- */
    if (showProfileEdit) {
        AlertDialog(
            onDismissRequest = { showProfileEdit = false },
            title = { Text("프로필 수정", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    // 아바타
                    Column(
                        Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            Modifier.size(80.dp).clip(CircleShape)
                                .background(Color(0xFF34C759)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("김", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        Spacer(Modifier.height(8.dp))
                        TextButton(onClick = { /* 사진 변경 */ }) { Text("사진 변경", color = Color(0xFF10B981)) }
                    }
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = userName,
                        onValueChange = { userName = it },
                        label = { Text("이름") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = userIntro,
                        onValueChange = { if (it.length <= 500) userIntro = it },
                        label = { Text("소개") },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        Text("${userIntro.length}/500", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            },
            confirmButton = {
                Button(onClick = { showProfileEdit = false /* 저장 처리 */ }) { Text("저장") }
            },
            dismissButton = {
                TextButton(onClick = { showProfileEdit = false }) { Text("취소") }
            }
        )
    }
}
