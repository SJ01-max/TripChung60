// KakaoMapBox.kt
package com.tripchung.app.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.unit.dp


import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.kakao.vectormap.*

import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.label.LabelTextBuilder

@Composable
fun KakaoMapBox(
    lat: Double,
    lng: Double,
    name: String,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(300.dp),
    zoomLevel: Double = 13.0,
    pinIconResId: Int? = null
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val mapView = remember { MapView(context) }

    var status by remember { mutableStateOf("지도 초기화 중…") }

    DisposableEffect(mapView, lifecycleOwner) {
        val life = object : MapLifeCycleCallback() {
            override fun onMapDestroy() { Log.d("5UEpEI2D+nIRqGWI4PsA4nLERxE=","onMapDestroy") }
            override fun onMapError(error: Exception?) {
                status = "지도 에러: ${error?.message ?: "Unknown"}"
                Log.e("5UEpEI2D+nIRqGWI4PsA4nLERxE=","onMapError", error)
            }
        }
        val ready = object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                status = "지도 로딩 완료"
                Log.d("5UEpEI2D+nIRqGWI4PsA4nLERxE=","onMapReady")

                val center = LatLng.from(lat, lng)
                kakaoMap.moveCamera(CameraUpdateFactory.newCenterPosition(center))
                kakaoMap.moveCamera(CameraUpdateFactory.zoomTo(zoomLevel.toInt()))

                val text = LabelTextBuilder().setTexts(name)
                val options = LabelOptions.from(center).setTexts(text)
                pinIconResId?.let { options.setStyles(LabelStyles.from(LabelStyle.from(it))) }
                kakaoMap.labelManager?.layer?.addLabel(options)
            }
        }

        mapView.start(life, ready)

        val obs = LifecycleEventObserver { _, e ->
            when (e) {
                Lifecycle.Event.ON_RESUME -> mapView.resume()
                Lifecycle.Event.ON_PAUSE  -> mapView.pause()
                Lifecycle.Event.ON_DESTROY-> mapView.finish()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(obs)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(obs)
            mapView.finish()
        }
    }

    Box {
        AndroidView(factory = { mapView }, modifier = modifier)
        if (status != "지도 로딩 완료") {
            Text(status, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
