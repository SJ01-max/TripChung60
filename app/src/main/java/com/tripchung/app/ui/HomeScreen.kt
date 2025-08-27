package com.tripchung.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tripchung.app.model.sampleSpots

@Composable
fun HomeScreen(
    onPlanClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { TopLogoRow() }
        item { RegionDateCard(region = "충청도", date = "10월 5일(토) ~ 10월 6일(일)") }
        item { PlanCtaCard(onClick = onPlanClick) }
        item { SectionTitle("오늘의 추천 여행지") }
        item { RecommendRow() }
        // 필요하면 추가 섹션을 계속 item { ... } 형태로 붙이면 됩니다.
    }
}

@Composable
private fun TopLogoRow() {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            "트립충",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.ExtraBold
        )
        Icon(Icons.Filled.Notifications, contentDescription = null)
    }
}

@Composable
private fun RegionDateCard(region: String, date: String) {
    ElevatedCard {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("현재 위치", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(region, fontWeight = FontWeight.SemiBold)
            }
            Column {
                Text("날짜", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(date, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun PlanCtaCard(onClick: () -> Unit) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Column(Modifier.fillMaxWidth().padding(20.dp)) {
            Text(
                "AI와 함께 일정 만들기",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                "드래그 앤 드롭으로 완성하는 나만의 여행",
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f),
            )
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("일정 만들기 시작")
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun RecommendRow() {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(sampleSpots) { s ->
            ElevatedCard(Modifier.width(240.dp)) {
                AsyncImage(
                    model = s.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .height(140.dp)
                        .fillMaxWidth()
                )
                Column(Modifier.padding(12.dp)) {
                    Text(s.name, fontWeight = FontWeight.SemiBold)
                    Text("🚗 ${s.minutes}분", style = MaterialTheme.typography.bodySmall)
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(onClick = { /* TODO: 일정에 추가 */ }) {
                        Text("+ 일정에 추가")
                    }
                }
            }
        }
    }
}
