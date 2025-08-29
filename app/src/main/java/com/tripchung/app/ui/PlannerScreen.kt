package com.tripchung.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * 설문 플로우 화면
 *
 * - 기존 코드와의 호환을 위해 onNavChange 는 그대로 둠 (설문 도중 하단 탭 이동 등 사용)
 * - 설문 완료시 onDone 으로 사용자의 답변을 넘겨줌
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlannerScreen(
    onNavChange: (String) -> Unit = {},                     // 바텀탭 이동 등에 사용 (있던 파라미터 유지)
    onDone: (PlannerAnswers) -> Unit = {}                   // 설문 결과 전달
) {
    // 4단계 설문
    val totalSteps = 4
    var step by remember { mutableStateOf(0) }

    // 각 단계 선택 상태
    var tripType by remember { mutableStateOf<String?>(null) }      // Q1
    var withWhom by remember { mutableStateOf<String?>(null) }      // Q2
    var mood by remember { mutableStateOf<String?>(null) }          // Q3
    var transport by remember { mutableStateOf<String?>(null) }     // Q4

    // 단계별 데이터
    val q1Options = listOf("힐링 여행", "액티비티 여행", "문화 탐방", "맛집 투어", "자연 감상", "사진 촬영")
    val q2Options = listOf("혼자", "연인", "가족", "친구", "아이와 함께", "부모님과 함께")
    val q3Options = listOf("조용하고 평화로운", "활기차고 즐거운", "로맨틱한", "모험적인", "전통적인", "모던한")
    val q4Options = listOf("자동차", "대중교통", "도보", "자전거", "상관없음")

    Scaffold { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // 상단: 진행 표시
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("질문 ${step + 1}/$totalSteps", style = MaterialTheme.typography.labelLarge)
                TextButton(onClick = {
                    // 건너뛰기 → 바로 완료 처리(선택값 없을 수 있음)
                    onDone(
                        PlannerAnswers(
                            tripType = tripType,
                            withWhom = withWhom,
                            mood = mood,
                            transport = transport
                        )
                    )
                }) { Text("건너뛰기") }
            }

            LinearProgressIndicator(
                progress = { (step + 1f) / totalSteps },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(Modifier.height(24.dp))

            // 원형 아이콘 + 타이틀/설명
            Box(
                modifier = Modifier
                    .size(108.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.18f))
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                val stepIcon = when (step) {
                    0 -> Icons.Outlined.Explore
                    1 -> Icons.Outlined.Group
                    2 -> Icons.Outlined.Mood
                    else -> Icons.Outlined.DirectionsCar
                }
                Icon(stepIcon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(46.dp))
            }

            Spacer(Modifier.height(12.dp))
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
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(20.dp))

            // 카드 영역: 질문과 선택지
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp)
            ) {
                Column(Modifier.padding(20.dp)) {
                    val (title, options) = when (step) {
                        0 -> "어떤 여행을 원하시나요?" to q1Options
                        1 -> "누구와 함께 여행하시나요?" to q2Options
                        2 -> "어떤 분위기를 선호하시나요?" to q3Options
                        else -> "선호하는 이동 수단은?" to q4Options
                    }

                    Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(14.dp))

                    // 2열 그리드 버튼
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.heightIn(min = 0.dp)
                    ) {
                        items(options) { opt ->
                            val selected = when (step) {
                                0 -> tripType == opt
                                1 -> withWhom == opt
                                2 -> mood == opt
                                else -> transport == opt
                            }

                            OutlinedButton(
                                onClick = {
                                    when (step) {
                                        0 -> tripType = if (tripType == opt) null else opt
                                        1 -> withWhom = if (withWhom == opt) null else opt
                                        2 -> mood = if (mood == opt) null else opt
                                        else -> transport = if (transport == opt) null else opt
                                    }
                                },
                                shape = RoundedCornerShape(18.dp),
                                border = if (selected)
                                    ButtonDefaults.outlinedButtonBorder.copy(
                                        brush = androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.primary)
                                    )
                                else ButtonDefaults.outlinedButtonBorder,
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (selected)
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                                    else MaterialTheme.colorScheme.surface
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(60.dp)
                            ) {
                                Text(
                                    opt,
                                    color = if (selected) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // 하단 컨트롤
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = { if (step > 0) step-- },
                    enabled = step > 0,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp)
                ) { Text("이전") }

                val canNext = when (step) {
                    0 -> tripType != null
                    1 -> withWhom != null
                    2 -> mood != null
                    else -> transport != null
                }

                Button(
                    onClick = {
                        if (step < totalSteps - 1) {
                            step++
                        } else {
                            // 완료
                            onDone(
                                PlannerAnswers(
                                    tripType = tripType,
                                    withWhom = withWhom,
                                    mood = mood,
                                    transport = transport
                                )
                            )
                        }
                    },
                    enabled = canNext,
                    modifier = Modifier.weight(2f),
                    shape = RoundedCornerShape(14.dp)
                ) { Text(if (step == totalSteps - 1) "완료" else "다음") }
            }
        }
    }
}

/** 설문 결과 모델 */
data class PlannerAnswers(
    val tripType: String?,
    val withWhom: String?,
    val mood: String?,
    val transport: String?
)
