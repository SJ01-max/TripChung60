package com.tripchung.app.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

/** 설문 결과 모델 */
data class PlannerAnswers(
    val tripType: String?,   // ex) 힐링 여행
    val withWhom: String?,   // ex) 가족
    val mood: String?,       // ex) 조용하고 평화로운
    val transport: String?   // ex) 자동차
)

/** 추천 카드용 모델 */
data class RecommendPlace(
    val id: String,
    val title: String,
    val tag: String,
    val desc: String,
    val time: String,
    val distance: String,
    val imageUrl: String,
    val rating: Double,
    val badges: List<String>
)

class PlannerViewModel : ViewModel() {

    var answers: PlannerAnswers? by mutableStateOf(null)
        private set

    var results: List<RecommendPlace> by mutableStateOf(emptyList())
        private set

    /** ✅ 설문 응답 저장 + 추천 리스트 생성 (이전 setAnswers 충돌 해결) */
    fun submitAnswers(a: PlannerAnswers) {
        answers = a
        results = buildRecommendations(a)
    }

    /** 간단 샘플 추천 로직 (원하는 API/DB 로직으로 교체 가능) */
    private fun buildRecommendations(a: PlannerAnswers): List<RecommendPlace> {
        val theme = a.tripType ?: "힐링 여행"
        val mood  = a.mood ?: "조용하고 평화로운"

        val base = listOf(
            RecommendPlace(
                id = "kyr-park",
                title = "계룡산 국립공원",
                tag = "자연/힐링",
                desc = "가을 단풍이 아름다운 명산으로 힐링과 트레킹을 동시에 즐길 수 있어요.",
                time = "⏱ 2-3시간",
                distance = "📍 1.2km",
                imageUrl = "https://images.unsplash.com/photo-1501785888041-af3ef285b470?w=1200",
                rating = 4.7,
                badges = listOf("산책", "전망", "단풍")
            ),
            RecommendPlace(
                id = "buyeo-gungnamji",
                title = "부여 궁남지",
                tag = "역사/문화",
                desc = "백제의 역사가 살아있는 연못으로 사계절 아름다운 풍경을 자랑합니다.",
                time = "⏱ 1-2시간",
                distance = "📍 800m",
                imageUrl = "https://images.unsplash.com/photo-1470770841072-f978cf4d019e?w=1200",
                rating = 4.6,
                badges = listOf("야경", "연못", "산책")
            ),
            RecommendPlace(
                id = "asan-oncheon",
                title = "온양온천",
                tag = "힐링/온천",
                desc = "따뜻한 온천수와 함께 피로를 풀 수 있는 힐링 명소예요.",
                time = "⏱ 1-2시간",
                distance = "📍 5.1km",
                imageUrl = "https://images.unsplash.com/photo-1542826438-1c3b01c1f2a4?w=1200",
                rating = 4.5,
                badges = listOf("스파", "가족", "휴식")
            )
        )

        // 아주 간단한 필터 + 가중치 샘플
        val filtered = when (theme) {
            "힐링 여행" -> base.filter { it.tag.contains("힐링") || it.tag.contains("자연") }
            "문화 탐방" -> base.filter { it.tag.contains("역사") || it.tag.contains("문화") }
            "맛집 투어" -> base // 샘플 데이터엔 맛집이 없어 전체 유지
            else -> base
        }

        return filtered.sortedByDescending {
            val moodBoost = if (mood.contains("조용") && it.tag.contains("힐링")) 0.2 else 0.0
            it.rating + moodBoost
        }
    }
}
