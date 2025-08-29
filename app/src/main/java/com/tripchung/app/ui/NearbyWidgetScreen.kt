package com.tripchung.app.ui

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/* ---------- 더미 데이터 ---------- */
data class NearbyPlace(
    val id: String,
    val name: String,
    val type: String,
    val distance: String,
    val walkTime: String,
    val rating: String,
    val isOpen: Boolean,
    val image: String,
    val lat: Double,
    val lng: Double,
)

private val nearbyPlaces = listOf(
    NearbyPlace("1","아산 외암마을","문화유산","2.5km","35분","4.6",true,
        "https://readdy.ai/api/search-image?query=Beautiful%20traditional%20Korean%20village%20in%20autumn%20with%20hanok%20houses%2C%20colorful%20fall%20foliage%2C%20maple%20trees%20with%20red%20and%20yellow%20leaves%2C%20traditional%20stone%20walls%2C%20peaceful%20countryside%20atmosphere%2C%20golden%20hour%20lighting%2C%20cultural%20heritage%20site&width=280&height=180&seq=51&orientation=landscape",
        36.7833,127.0167),
    NearbyPlace("2","온양온천","온천・스파","3.2km","42분","4.5",true,
        "https://readdy.ai/api/search-image?query=Korean%20traditional%20hot%20spring%20spa%20resort%2C%20relaxing%20thermal%20pools%2C%20autumn%20mountain%20scenery%2C%20peaceful%20wellness%20center%2C%20warm%20therapeutic%20waters%2C%20traditional%20Korean%20spa%20atmosphere&width=280&height=180&seq=52&orientation=landscape",
        36.7901,127.0021),
    NearbyPlace("3","아산 맛집골목","맛집거리","1.8km","25분","4.4",true,
        "https://readdy.ai/api/search-image?query=Korean%20traditional%20food%20street%20with%20various%20restaurants%2C%20warm%20evening%20lights%2C%20bustling%20food%20alley%20atmosphere%2C%20local%20dining%20culture%2C%20cozy%20restaurant%20facades&width=280&height=180&seq=53&orientation=landscape",
        36.7950,127.0120),
    NearbyPlace("4","현충사","역사문화","4.1km","55분","4.7",false,
        "https://readdy.ai/api/search-image?query=Korean%20historical%20shrine%20with%20traditional%20architecture%2C%20autumn%20trees%2C%20peaceful%20cultural%20site%2C%20memorial%20building%20with%20traditional%20Korean%20roof%2C%20serene%20atmosphere&width=280&height=180&seq=54&orientation=landscape",
        36.7811,127.0089)
)

private val categories = listOf("전체","맛집","카페","온천","문화","쇼핑","주차")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NearbyWidgetScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    var userLocation by remember { mutableStateOf("아산시 온천동") }
    var selectedCategory by remember { mutableStateOf("전체") }
    val favorites = remember { mutableStateListOf<String>() }
    var added: String? by remember { mutableStateOf(null) }

    // 👇 인앱 지도 타깃 (BottomSheet용)
    var mapTarget by remember { mutableStateOf<NearbyPlace?>(null) }

    val filtered = remember(selectedCategory) {
        when (selectedCategory) {
            "전체" -> nearbyPlaces
            "맛집" -> nearbyPlaces.filter { it.type.contains("맛집") }
            "카페" -> emptyList()
            "온천" -> nearbyPlaces.filter { it.type.contains("온천") }
            "문화" -> nearbyPlaces.filter { it.type.contains("문화") }
            else -> emptyList()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("트립충", color = Color(0xFF10B981), fontWeight = FontWeight.ExtraBold)
                        Text("충청도 AI 여행", style = MaterialTheme.typography.labelSmall)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "뒤로")
                    }
                },
                actions = {
                    BadgedBox(badge = { Badge { Text("4") } }) {
                        Icon(Icons.Outlined.NotificationsNone, contentDescription = null)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* 새로고침 */ },
                containerColor = Color(0xFF10B981),
                contentColor = Color.White
            ) { Icon(Icons.Outlined.Refresh, contentDescription = "새로고침") }
        }
    ) { inner ->
        Column(
            modifier
                .fillMaxSize()
                .padding(inner)
                .background(Brush.linearGradient(listOf(Color(0xFFEFFCF6), Color.White, Color(0xFFFFF4E5))))
        ) {
            // 현재 위치 카드
            Card(
                modifier = Modifier.padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(48.dp).clip(CircleShape).background(Color(0xFFD1FAE5)), contentAlignment = Alignment.Center) {
                            Text("📍")
                        }
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text("현재 위치", color = Color.Gray, style = MaterialTheme.typography.labelSmall)
                            Text(userLocation, fontWeight = FontWeight.Bold)
                        }
                    }
                    // 👇 외부 앱/웹 열지 않고, 리스트 첫 항목 지도를 인앱으로 열기
                    Button(
                        onClick = { mapTarget = filtered.firstOrNull() ?: nearbyPlaces.firstOrNull() },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981), contentColor = Color.White)
                    ) { Text("지도보기") }
                }
            }

            // 카테고리 칩
            Row(
                Modifier.padding(horizontal = 16.dp).horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { name ->
                    val selected = selectedCategory == name
                    val label = when (name) {
                        "맛집" -> "🍜 맛집"
                        "카페" -> "☕ 카페"
                        "온천" -> "♨️ 온천"
                        "문화" -> "🏛️ 문화"
                        "쇼핑" -> "🛍️ 쇼핑"
                        "주차" -> "🅿️ 주차"
                        else -> "전체"
                    }
                    Box(
                        Modifier
                            .clip(RoundedCornerShape(999.dp))
                            .background(if (selected) Color(0xFF10B981) else Color(0xFFF3F4F6))
                            .clickable { selectedCategory = name }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(label, color = if (selected) Color.White else Color(0xFF4B5563), style = MaterialTheme.typography.labelLarge)
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            // 목록
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filtered, key = { it.id }) { place ->
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(1.dp)
                    ) {
                        Row(Modifier.padding(12.dp)) {
                            AsyncImage(
                                model = place.image,
                                contentDescription = place.name,
                                modifier = Modifier.size(96.dp).clip(RoundedCornerShape(14.dp))
                            )
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(place.name, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                    Spacer(Modifier.width(6.dp))
                                    val chipBg = if (place.isOpen) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                                    val chipFg = if (place.isOpen) Color(0xFF2E7D32) else Color(0xFFC62828)
                                    Box(
                                        Modifier.clip(RoundedCornerShape(10.dp)).background(chipBg).padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) { Text(if (place.isOpen) "영업중" else "영업종료", color = chipFg, style = MaterialTheme.typography.labelSmall) }
                                }
                                Text(place.type, color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                                Spacer(Modifier.height(6.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    LabelChip("⭐ ${place.rating}")
                                    LabelChip("📍 ${place.distance}")
                                    LabelChip("🚶 ${place.walkTime}")
                                }
                            }
                            IconButton(onClick = {
                                if (favorites.contains(place.id)) favorites.remove(place.id) else favorites.add(place.id)
                            }) {
                                Icon(
                                    imageVector = if (favorites.contains(place.id)) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                                    contentDescription = null,
                                    tint = if (favorites.contains(place.id)) Color.Red else Color.Gray
                                )
                            }
                        }

                        Row(Modifier.padding(horizontal = 12.dp, vertical = 12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            // 👇 길찾기 → 인앱 지도 미리보기(외부로 안 나감)
                            Button(
                                onClick = { mapTarget = place },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981), contentColor = Color.White)
                            ) { Text("길찾기") }

                            OutlinedButton(
                                onClick = { added = place.name },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) { Text("일정 추가") }

                            OutlinedIconButton(onClick = { /* 전화 */ }, shape = RoundedCornerShape(12.dp)) {
                                Icon(Icons.Outlined.Phone, contentDescription = null)
                            }
                        }
                    }
                }
            }

            // 일정 추가 완료 다이얼로그ß
            if (added != null) {
                AlertDialog(
                    onDismissRequest = { added = null },
                    confirmButton = { Button(onClick = { added = null }) { Text("일정 확인하기") } },
                    title = { Text("일정에 추가되었습니다!") },
                    text  = { Text("$added 이(가) 일정에 추가되었어요") }
                )
            }

            // 👇 인앱 지도 모달 시트 (외부/웹 NO)
            if (mapTarget != null) {
                ModalBottomSheet(onDismissRequest = { mapTarget = null }) {
                    Text(
                        mapTarget!!.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )
                    KakaoMapBox(
                        lat = mapTarget!!.lat,
                        lng = mapTarget!!.lng,
                        name = mapTarget!!.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .padding(horizontal = 16.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        zoomLevel = 13.0
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(
                        Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // 선택: 필요하면 외부 카카오맵 앱으로 "진짜 길찾기" 실행
                        OutlinedButton(
                            onClick = {
                                openKakaoMapRouteFoot(
                                    context = context,
                                    lat = mapTarget!!.lat,
                                    lng = mapTarget!!.lng,
                                    name = mapTarget!!.name
                                )
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) { Text("카카오맵 앱에서 길찾기") }

                        Button(
                            onClick = { mapTarget = null },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) { Text("닫기") }
                    }
                    Spacer(Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
private fun LabelChip(text: String) {
    Text(text, color = Color.Gray, style = MaterialTheme.typography.labelMedium)
}

/* -------------------------------------------------------
   (선택) 카카오맵 앱 열기 헬퍼 — 인앱 미리보기와 별개
   ------------------------------------------------------- */
private const val KAKAO_PKG = "net.daum.android.map"

private fun isKakaoMapInstalled(context: Context): Boolean {
    val pm = context.packageManager
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pm.getPackageInfo(KAKAO_PKG, PackageManager.PackageInfoFlags.of(0))
        } else {
            @Suppress("DEPRECATION")
            pm.getPackageInfo(KAKAO_PKG, 0)
        }
        true
    } catch (_: Exception) {
        false
    }
}

private fun openKakaoMapRouteFoot(
    context: Context,
    lat: Double,
    lng: Double,
    name: String = ""
) {
    val encName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString())
    val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("kakaomap://route?ep=$lat,$lng&by=FOOT&epname=$encName"))
        .apply { setPackage(KAKAO_PKG) }
    val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://map.kakao.com/link/to/$encName,$lat,$lng"))

    if (isKakaoMapInstalled(context)) {
        runCatching { context.startActivity(appIntent) }.onFailure { context.startActivity(webIntent) }
    } else {
        context.startActivity(webIntent)
    }
}
