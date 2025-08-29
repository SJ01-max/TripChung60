package com.tripchung.app.ui

import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

/* ====================== 데이터 모델 ====================== */

private data class Place(val id: String, val name: String, val travelTime: String, val imageUrl: String)
private data class Restaurant(val id: String, val name: String, val type: String, val rating: String, val time: String, val imageUrl: String)
private data class EventInfo(
    val id: String,
    val title: String,
    val period: String,
    val location: String,
    val details: String,
    val programs: List<String>,
    val imageUrl: String
)
private data class Region(val name: String, val cities: List<String>)

/* ====================== 샘플 데이터 (웹 코드 기반) ====================== */

private val todayRecommendations = listOf(
    Place("1","아산 외암마을","🚗 35분","https://readdy.ai/api/search-image?query=Beautiful%20traditional%20Korean%20village%20in%20autumn%20with%20hanok%20houses%2C%20colorful%20fall%20foliage%2C%20maple%20trees%20with%20red%20and%20yellow%20leaves%2C%20traditional%20stone%20walls%2C%20peaceful%20countryside%20atmosphere%2C%20golden%20hour%20lighting%2C%20cultural%20heritage%20site&width=280&height=180&seq=11&orientation=landscape"),
    Place("2","서산 해미읍성","🚗 50분","https://readdy.ai/api/search-image?query=Historic%20Korean%20fortress%20wall%20in%20autumn%2C%20ancient%20stone%20fortification%20with%20fall%20maple%20trees%2C%20traditional%20Korean%20architecture%2C%20golden%20sunset%20lighting%2C%20peaceful%20historical%20site%2C%20autumn%20colors%2C%20cultural%20landmark&width=280&height=180&seq=12&orientation=landscape"),
    Place("3","공주 공산성","🚗 1시간","https://readdy.ai/api/search-image?query=Gongsanseong%20fortress%20at%20night%20with%20beautiful%20illumination%2C%20ancient%20Korean%20castle%20walls%20lit%20up%2C%20evening%20twilight%20sky%2C%20traditional%20architecture%20with%20warm%20lighting%2C%20peaceful%20night%20scene%2C%20historical%20landmark&width=280&height=180&seq=13&orientation=landscape")
)

private val restaurantRecommendations = listOf(
    Restaurant("1","아산 외암마을 전통찻집","전통차·디저트","4.8","🚗 35분","https://readdy.ai/api/search-image?query=Traditional%20Korean%20tea%20house%20in%20historic%20village%2C%20autumn%20atmosphere%2C%20cozy%20interior%20with%20hanok%20architecture%2C%20traditional%20tea%20service%2C%20warm%20lighting%2C%20cultural%20dining%20experience&width=280&height=180&seq=61&orientation=landscape"),
    Restaurant("2","온양온천 한정식","한정식·정갈한 상차림","4.7","🚗 40분","https://readdy.ai/api/search-image?query=Elegant%20Korean%20traditional%20restaurant%20with%20beautiful%20table%20setting%2C%20colorful%20banchan%20side%20dishes%2C%20authentic%20Korean%20cuisine%2C%20warm%20ambient%20lighting%2C%20cultural%20dining%20atmosphere&width=280&height=180&seq=62&orientation=landscape"),
    Restaurant("3","서산 해미읍성 막국수","막국수·향토음식","4.6","🚗 50분","https://readdy.ai/api/search-image?query=Korean%20buckwheat%20noodle%20restaurant%2C%20traditional%20makguksu%20dish%2C%20local%20specialty%20food%2C%20rustic%20dining%20atmosphere%2C%20authentic%20Korean%20comfort%20food%2C%20cozy%20restaurant%20interior&width=280&height=180&seq=63&orientation=landscape"),
    Restaurant("4","대전 성심당","베이커리·튀김소보로","4.9","🚗 1시간","https://readdy.ai/api/search-image?query=Famous%20Korean%20bakery%20with%20signature%20soboro%20bread%2C%20bustling%20local%20bakery%20atmosphere%2C%20traditional%20Korean%20pastries%2C%20warm%20golden%20lighting%2C%20popular%20local%20landmark&width=280&height=180&seq=64&orientation=landscape")
)

private val localEvents = listOf(
    EventInfo(
        id="1",
        title="부여 궁남지 국화축제",
        period="10/3~10/9",
        location="부여군 궁남지",
        details="백제의 옛 정취를 느낄 수 있는 궁남지에서 열리는 국화축제입니다. 다양한 국화 작품과 야간 조명으로 아름다운 가을 정취를 만끽할 수 있어요.",
        programs=listOf("국화 전시","야간 조명","전통 공연","포토존"),
        imageUrl="https://readdy.ai/api/search-image?query=Beautiful%20chrysanthemum%20festival%20at%20Korean%20historical%20pond%2C%20colorful%20autumn%20flowers%2C%20traditional%20Korean%20garden%2C%20festival%20decorations%2C%20peaceful%20cultural%20event%2C%20soft%20natural%20lighting&width=200&height=120&seq=14&orientation=landscape"
    ),
    EventInfo(
        id="2",
        title="홍성 남당항 대하축제",
        period="10/1~10/15",
        location="홍성군 남당항",
        details="홍성 남당항에서 열리는 가을 대하축제로 신선한 해산물과 다양한 먹거리를 즐길 수 있습니다. 항구의 정취와 함께 맛있는 추억을 만들어보세요.",
        programs=listOf("대하 시식","해산물 판매","어선 체험","바다 낚시"),
        imageUrl="https://readdy.ai/api/search-image?query=Korean%20harbor%20festival%20with%20fresh%20seafood%2C%20traditional%20fishing%20village%2C%20festival%20tents%20and%20decorations%2C%20coastal%20autumn%20atmosphere%2C%20local%20cultural%20event%2C%20warm%20community%20gathering&width=200&height=120&seq=15&orientation=landscape"
    )
)

private val regions = listOf(
    Region("충청남도", listOf("천안","공주","보령","아산","서산","논산","계룡","당진","금산","부여","서천","청양","홍성","예산","태안")),
    Region("충청북도", listOf("청주","충주","제천","보은","옥천","영동","증평","진천","괴산","음성","단양")),
    Region("대전광역시", listOf("중구","동구","서구","유성구","대덕구")),
    Region("세종특별자치시", listOf("세종시"))
)

/* ====================== 유틸 ====================== */

private data class DayCell(val date: LocalDate, val isToday: Boolean, val isWeekend: Boolean)
private fun generateCalendarDays(count: Int = 30): List<DayCell> {
    val today = LocalDate.now()
    return (0 until count).map { offset ->
        val d = today.plusDays(offset.toLong())
        DayCell(
            date = d,
            isToday = offset == 0,
            isWeekend = d.dayOfWeek.value == 6 || d.dayOfWeek.value == 7
        )
    }
}

/* ====================== 메인 화면 ====================== */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onPlanClick: () -> Unit
) {
    // 상태들 (웹 코드 매핑)
    var showRestaurants by rememberSaveable { mutableStateOf(false) }
    var showItinerary by rememberSaveable { mutableStateOf(false) }
    var selectedPlaces by rememberSaveable { mutableStateOf(listOf<String>()) }
    var selectedPlaceName by rememberSaveable { mutableStateOf<String?>(null) } // 팝업용
    var showAddConfirm by rememberSaveable { mutableStateOf(false) }
    var optimizedRoute by rememberSaveable { mutableStateOf(false) }

    var selectedRegion by rememberSaveable { mutableStateOf("충청도") }
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    var showRegionPicker by rememberSaveable { mutableStateOf(false) }
    var selectedStartDate by rememberSaveable { mutableStateOf("10월 5일(토)") }
    var selectedEndDate by rememberSaveable { mutableStateOf("10월 6일(일)") }

    var eventDetail by rememberSaveable { mutableStateOf<EventInfo?>(null) }
    var showCoupon by rememberSaveable { mutableStateOf(false) }

    // 공용 콜백
    fun addToItinerary(name: String) {
        if (!selectedPlaces.contains(name)) {
            selectedPlaces = selectedPlaces + name
            selectedPlaceName = name
            showAddConfirm = true
        }
    }

    if (showDatePicker) {
        ModalBottomSheet(onDismissRequest = { showDatePicker = false }) {
            DatePickerSheet(
                onClose = { showDatePicker = false },
                onPick = { d ->
                    val dayKo = d.date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN) // 월/화/...
                    selectedStartDate = "${d.date.monthValue}월 ${d.date.dayOfMonth}일(${dayKo})"
                    showDatePicker = false
                }
            )
        }
    }
    if (showRegionPicker) {
        ModalBottomSheet(onDismissRequest = { showRegionPicker = false }) {
            RegionPickerSheet(
                selectedRegion = selectedRegion,
                onPick = {
                    selectedRegion = it
                    showRegionPicker = false
                },
                onClose = { showRegionPicker = false }
            )
        }
    }
    if (eventDetail != null) {
        ModalBottomSheet(onDismissRequest = { eventDetail = null }) {
            EventDetailSheet(
                info = eventDetail!!,
                onClose = { eventDetail = null },
                onAdd = {
                    addToItinerary(eventDetail!!.title)
                    eventDetail = null
                }
            )
        }
    }
    if (showCoupon) {
        AlertDialog(
            onDismissRequest = { showCoupon = false },
            confirmButton = {
                Button(onClick = {
                    showCoupon = false
                    addToItinerary("아산 온양온천")
                }) { Text("쿠폰 받고 일정에 추가") }
            },
            dismissButton = {
                TextButton(onClick = { showCoupon = false }) { Text("닫기") }
            },
            icon = { Icon(Icons.Outlined.ConfirmationNumber, null) },
            title = { Text("온양온천 할인 쿠폰") },
            text = {
                Column {
                    Text("성인 입욕권 20% 할인 · 가족 패키지 추가 10% · 사우나 무료", style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(8.dp))
                    Text("기간: 2024-11-30, 평일 10:00~18:00, 장소: 아산 온양온천 스파비스", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        )
    }
    if (showAddConfirm && selectedPlaceName != null) {
        AlertDialog(
            onDismissRequest = { showAddConfirm = false },
            confirmButton = {
                Button(onClick = {
                    showAddConfirm = false
                    showItinerary = true
                }) { Text("일정 확인하기") }
            },
            dismissButton = {
                TextButton(onClick = { showAddConfirm = false }) { Text("계속 둘러보기") }
            },
            icon = { Icon(Icons.Outlined.CheckCircle, contentDescription = null) },
            title = { Text("일정에 추가되었습니다!") },
            text = { Text("${selectedPlaceName}이(가) 여행 일정에 추가되었어요") }
        )
    }

    // 본문
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 상단 로고/알림
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("트립충", style = MaterialTheme.typography.titleLarge, color = Color(0xFF2A8C54), fontWeight = FontWeight.ExtraBold)
                    Spacer(Modifier.width(8.dp))
                    Text("충청도 AI 여행", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                BadgedBox(badge = { Badge { Text("4") } }) {
                    IconButton(onClick = { }) { Icon(Icons.Outlined.NotificationsNone, null) }
                }
            }
        }

        // 위치/날짜 카드
        item {
            ElevatedCard(
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFFE8F5E9)),
                            contentAlignment = Alignment.Center
                        ) { Icon(Icons.Outlined.Place, null, tint = Color(0xFF2E7D32)) }
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text("현재 위치", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(selectedRegion, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(horizontalAlignment = Alignment.End) {
                            Text("날짜", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("$selectedStartDate ~ $selectedEndDate", fontWeight = FontWeight.SemiBold)
                        }
                        Spacer(Modifier.width(10.dp))
                        IconButton(onClick = { showDatePicker = true }) { Icon(Icons.Outlined.CalendarMonth, null, tint = Color(0xFFFB8C00)) }
                        IconButton(onClick = { showRegionPicker = true }) { Icon(Icons.Outlined.Map, null, tint = Color(0xFF1565C0)) }
                    }
                }
            }
        }

        // 👉 AI와 함께 일정 만들기 CTA (PlanCtaCard로 교체)
        item { PlanCtaCard(onClick = onPlanClick) }

        // 오늘의 추천 여행지
        item {
            Text("오늘의 추천 여행지", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(todayRecommendations) { p ->
                    ElevatedCard(shape = RoundedCornerShape(18.dp), modifier = Modifier.width(260.dp)) {
                        Box {
                            AsyncImage(
                                model = p.imageUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp)
                            )
                            Column(
                                Modifier
                                    .padding(10.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White.copy(alpha = 0.9f))
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(p.name, fontWeight = FontWeight.Bold)
                                Text(p.travelTime, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        Column(Modifier.padding(12.dp)) {
                            OutlinedButton(onClick = { addToItinerary(p.name) }, modifier = Modifier.fillMaxWidth()) {
                                Text("+ 일정에 추가")
                            }
                        }
                    }
                }
            }
        }

        // 테마별 여행 제안 (맛집 클릭 시 맛집 화면 토글)
        item {
            Text("테마별 여행 제안", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ThemeButton("가족", Icons.Outlined.Diversity3, Color(0xFFF3E8FF)) { /* … */ }
                ThemeButton("자연", Icons.Outlined.Eco, Color(0xFFEAF7EE)) { /* … */ }
                ThemeButton("맛집", Icons.Outlined.Coffee, Color(0xFFFFF2E2)) { showRestaurants = true }
            }
            Spacer(Modifier.height(10.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ThemeButton("축제", Icons.Outlined.Celebration, Color(0xFFF0E9FF)) { /* … */ }
                ThemeButton("역사", Icons.Outlined.Castle, Color(0xFFEFF3FF)) { /* … */ }
                ThemeButton("힐링", Icons.Outlined.LocalCafe, Color(0xFFFFF3D9)) { /* … */ }
            }
        }

        // 맛집 리스트(토글)
        if (showRestaurants) {
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text("충청도 맛집 추천", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text("현지인이 추천하는 진짜 맛집들", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    IconButton(onClick = { showRestaurants = false }) { Icon(Icons.Outlined.Close, null) }
                }
            }
            items(restaurantRecommendations) { r ->
                ElevatedCard(shape = RoundedCornerShape(18.dp)) {
                    Row(Modifier.padding(12.dp)) {
                        AsyncImage(
                            model = r.imageUrl, contentDescription = null,
                            modifier = Modifier.size(88.dp).clip(RoundedCornerShape(12.dp))
                        )
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(r.name, fontWeight = FontWeight.Bold)
                            Text(r.type, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Outlined.Star, null, tint = Color(0xFFFFB300))
                                Spacer(Modifier.width(4.dp))
                                Text(r.rating, fontWeight = FontWeight.SemiBold)
                                Spacer(Modifier.width(8.dp))
                                Text(r.time, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                    Row(Modifier.padding(horizontal = 12.dp, vertical = 10.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { addToItinerary(r.name) }, modifier = Modifier.weight(1f)) { Text("일정에 추가") }
                        OutlinedIconButton(onClick = { /* 찜 */ }) { Icon(Icons.Outlined.FavoriteBorder, null) }
                        OutlinedIconButton(onClick = { /* 전화 */ }) { Icon(Icons.Outlined.Phone, null) }
                    }
                }
            }
            item {
                Button(
                    onClick = onPlanClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(18.dp)
                ) { Text("맛집 투어 일정 만들기") }
            }
        }

        // 내 일정 미리보기
        if (selectedPlaces.isNotEmpty()) {
            item {
                ElevatedCard(shape = RoundedCornerShape(18.dp)) {
                    Column(Modifier.padding(14.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("내 일정 미리보기", fontWeight = FontWeight.Bold)
                            TextButton(onClick = { showItinerary = !showItinerary }) { Text(if (showItinerary) "접기" else "펼치기") }
                        }
                        if (showItinerary) {
                            Text("Day 1: ${selectedPlaces.joinToString(" → ")}")
                            Text("총 이동시간: 2시간 15분", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(Modifier.height(10.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Button(
                                    onClick = {
                                        if (selectedPlaces.size > 1) {
                                            optimizedRoute = true
                                        }
                                    }
                                ) { Text(if (optimizedRoute) "최적화 완료!" else "동선 최적화") }
                                OutlinedButton(onClick = onPlanClick) { Text("상세 편집") }
                            }
                        }
                    }
                }
            }
        }

        // 로컬 이벤트/할인
        item {
            Text("이번 주 충청도 축제", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
        }
        items(localEvents) { e ->
            ElevatedCard(
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.clickable { eventDetail = e }
            ) {
                Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(model = e.imageUrl, contentDescription = null, modifier = Modifier.size(80.dp).clip(RoundedCornerShape(12.dp)))
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(e.title, fontWeight = FontWeight.SemiBold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text(e.period, color = Color(0xFF2A8C54), fontWeight = FontWeight.Medium)
                    }
                    Icon(Icons.Outlined.KeyboardArrowRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        item {
            // 쿠폰 배너
            ElevatedCard(
                onClick = { showCoupon = true },
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFFFFA24C)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier.size(42.dp).clip(RoundedCornerShape(12.dp)).background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) { Icon(Icons.Outlined.ConfirmationNumber, null, tint = Color.White) }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text("아산 온양온천 입욕권 20% 할인", color = Color.White, fontWeight = FontWeight.Bold)
                        Text("11월 30일까지", color = Color.White.copy(alpha = 0.9f), style = MaterialTheme.typography.bodySmall)
                    }
                    Icon(Icons.Outlined.KeyboardArrowRight, null, tint = Color.White)
                }
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

/* ====================== 컴포넌트 조각 ====================== */

@Composable
fun RowScope.ThemeButton(
    label: String,
    icon: ImageVector,
    bg: Color,
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = bg),
        modifier = Modifier.weight(1f)
    ) {
        Column(
            Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, null)
            Spacer(Modifier.height(6.dp))
            Text(label, fontWeight = FontWeight.Medium)
        }
    }
}

/* 👉 분리된 CTA 카드: 네가 원하는 디자인 */
@Composable
private fun PlanCtaCard(onClick: () -> Unit) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(22.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                "AI와 함께 일정 만들기",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                "드래그 앤 드롭으로 완성하는 나만의 여행",
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("일정 만들기 시작")
            }
        }
    }
}

@Composable private fun SpacerItem(h: Dp) { Spacer(Modifier.height(h)) }

/* 날짜 선택 시트 */
@Composable
private fun DatePickerSheet(
    onClose: () -> Unit,
    onPick: (DayCell) -> Unit
) {
    Column(Modifier.fillMaxWidth().padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("날짜 선택", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            IconButton(onClick = onClose) { Icon(Icons.Outlined.Close, null) }
        }
        Spacer(Modifier.height(8.dp))

        // 요일 헤더
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            listOf("일","월","화","수","목","금","토").forEach {
                Text(it, modifier = Modifier.weight(1f), textAlign = androidx.compose.ui.text.style.TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Spacer(Modifier.height(8.dp))

        // 30일 간
        val days = remember { generateCalendarDays(30) }
        val rows = days.chunked(7)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            rows.forEach { row ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    row.forEach { d ->
                        val bg = when {
                            d.isToday -> Color(0xFFFFB300)
                            d.isWeekend -> Color(0xFFFFEBEE)
                            else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                        }
                        Box(
                            Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(bg)
                                .clickable { onPick(d) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("${d.date.dayOfMonth}", fontWeight = if (d.isToday) FontWeight.Bold else FontWeight.Medium,
                                color = if (d.isToday) Color.White else MaterialTheme.colorScheme.onSurface)
                        }
                    }
                    // 빈 칸 보정
                    repeat(7 - row.size) {
                        Spacer(Modifier.weight(1f))
                    }
                }
            }
        }
        Spacer(Modifier.height(12.dp))
    }
}

/* 지역 선택 시트 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RegionPickerSheet(
    selectedRegion: String,
    onPick: (String) -> Unit,
    onClose: () -> Unit
) {
    Column(Modifier.fillMaxWidth().padding(16.dp)) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("지역 선택", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            IconButton(onClick = onClose) { Icon(Icons.Outlined.Close, null) }
        }
        Spacer(Modifier.height(8.dp))

        Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            regions.forEach { r ->
                ElevatedCard(
                    onClick = { onPick(r.name) },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = if (selectedRegion == r.name) Color(0xFFE3F2FD)
                        else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(Modifier.fillMaxWidth().padding(12.dp)) {
                        Text(r.name, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(6.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            r.cities.take(8).forEach { city ->
                                SuggestionChip(text = city)
                            }
                        }
                        if (r.cities.size > 8) {
                            Text(
                                "+${r.cities.size - 8}개",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
private fun SuggestionChip(text: String) {
    Box(
        Modifier
            .padding(end = 6.dp, bottom = 6.dp)
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) { Text(text, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) }
}

/* 이벤트 상세 시트 */
@Composable
private fun EventDetailSheet(
    info: EventInfo,
    onClose: () -> Unit,
    onAdd: () -> Unit
) {
    Column(Modifier.fillMaxWidth().padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(info.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            IconButton(onClick = onClose) { Icon(Icons.Outlined.Close, null) }
        }
        Spacer(Modifier.height(8.dp))
        AsyncImage(model = info.imageUrl, contentDescription = null, modifier = Modifier.fillMaxWidth().height(160.dp).clip(RoundedCornerShape(16.dp)))
        Spacer(Modifier.height(12.dp))
        Text("행사 정보", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.CalendarMonth, null, tint = Color(0xFF2A8C54))
            Spacer(Modifier.width(6.dp))
            Text(info.period)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.LocationOn, null, tint = Color(0xFF2A8C54))
            Spacer(Modifier.width(6.dp))
            Text(info.location)
        }
        Spacer(Modifier.height(10.dp))
        Text("상세 설명", fontWeight = FontWeight.Bold)
        Text(info.details, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(10.dp))
        Text("주요 프로그램", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(6.dp))
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            info.programs.forEach { p ->
                Box(
                    Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFE8F5E9))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) { Text(p, color = Color(0xFF2A8C54)) }
            }
        }
        Spacer(Modifier.height(16.dp))
        Button(onClick = onAdd, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(14.dp)) {
            Text("일정에 추가하기")
        }
        Spacer(Modifier.height(8.dp))
    }
}
