package com.tripchung.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
    onDone: (answers: Map<String, String>) -> Unit = {}
) {
    // 현재 질문 단계 (0~3)
    var step by remember { mutableStateOf(0) }

    // 설문 답변 저장
    val answers = remember { mutableStateMapOf<String, String>() }

    // 질문/선택지 리스트
    val questions = listOf(
        "어떤 여행을 원하시나요?" to listOf("힐링 여행", "액티비티", "문화 탐방", "맛집 투어", "자연 감상", "사진 촬영"),
        "누구와 함께 가시나요?" to listOf("혼자", "연인", "가족", "친구", "단체"),
        "어떤 활동을 선호하시나요?" to listOf("도보 탐방", "체험/액티비티", "휴양/휴식", "쇼핑", "역사/문화"),
        "숙소는 어떤 스타일을 원하시나요?" to listOf("호텔", "펜션", "게스트하우스", "캠핑/카라반", "럭셔리 리조트")
    )

    val (title, options) = questions[step]
    val selected = answers[title]

    Scaffold { inner ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // 상단 진행 표시
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("질문 ${step + 1}/${questions.size}", style = MaterialTheme.typography.labelLarge)
                if (step < questions.lastIndex) {
                    TextButton(onClick = { step++ }) { Text("건너뛰기") }
                }
            }

            LinearProgressIndicator(
                progress = { (step + 1f) / questions.size },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(Modifier.height(24.dp))

            // 아이콘
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
                text = "완벽한 여행을 위해 간단한 질문에 답해주세요",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            )

            Spacer(Modifier.height(20.dp))

            // 질문 카드
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text(
                        title,
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
                        items(options) { option ->
                            val isSel = selected == option
                            OutlinedButton(
                                onClick = { answers[title] = option },
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
                                    option,
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

            // 버튼
            Button(
                onClick = {
                    if (step < questions.lastIndex) {
                        step++
                    } else {
                        onDone(answers) // 완료 시 결과 전달
                    }
                },
                enabled = selected != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(if (step < questions.lastIndex) "다음" else "완료")
            }
        }
    }
}
