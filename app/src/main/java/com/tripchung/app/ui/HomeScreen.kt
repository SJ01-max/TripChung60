package com.tripchung.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tripchung.app.model.sampleSpots

@Composable
fun HomeScreen(onPlanClick: () -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { TopLogoRow() }
        item { RegionDateCard("충청도", "10월 5일(토) ~ 10월 6일(일)") }
        item { PlanCtaCard(onPlanClick) }
        item { SectionTitle("오늘의 추천 여행지") }
        item { RecommendRow() }
    }
}

@Composable
fun TopLogoRow() {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text("트립충", style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary)
        Icon(Icons.Default.Notifications, contentDescription = null)
    }
}

@Composable
fun RegionDateCard(region: String, date: String) {
    ElevatedCard {
        Row(Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text("현재 위치")
                Text(region, fontWeight = FontWeight.SemiBold)
            }
            Column {
                Text("날짜")
                Text(date, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun PlanCtaCard(onClick: () -> Unit) {
    ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.primary)) {
        Column(Modifier.fillMaxWidth().padding(20.dp)) {
            Text("AI와 함께 일정 만들기", color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleLarge)
            Text("드래그 앤 드롭으로 완성하는 나만의 여행", color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f))
            Spacer(Modifier.height(12.dp))
            Button(onClick, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onPrimary)) {
                Text("일정 만들기 시작", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(title, style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold)
}

@Composable
fun RecommendRow() {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(sampleSpots.size) { index ->
            val s = sampleSpots[index]
            ElevatedCard(Modifier.width(240.dp)) {
                AsyncImage(model = s.imageUrl, contentDescription = null,
                    modifier = Modifier.height(140.dp).fillMaxWidth())
                Column(Modifier.padding(12.dp)) {
                    Text(s.name, fontWeight = FontWeight.SemiBold)
                    Text("🚗 ${s.minutes}분", style = MaterialTheme.typography.bodySmall)
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(onClick = { /* 일정에 추가 */ }) { Text("+ 일정에 추가") }
                }
            }
        }
    }
}
