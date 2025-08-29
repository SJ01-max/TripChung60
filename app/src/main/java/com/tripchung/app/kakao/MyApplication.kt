package com.tripchung.app.kakao

import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import android.util.Base64
import android.util.Log
import com.kakao.vectormap.KakaoMapSdk
import java.security.MessageDigest

private const val TAG_HASH = "KAKAO_KEY_HASH"
private const val TAG_PKG  = "PKG"

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // ⚠️ 반드시 '네이티브 앱 키' 사용 (JS 키 X)
        KakaoMapSdk.init(this, "85a2cf20033e7b1ea26ad4c739437917")

        // 패키지명 + 키 해시 로그 출력 (콘솔 등록용)
        logPkgAndKeyHashes()
    }

    private fun logPkgAndKeyHashes() {
        try {
            // 실행 중 실제 패키지명 확인 (콘솔 '패키지명'과 100% 일치해야 함)
            Log.d(TAG_PKG, packageName)

            val pm = packageManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val info = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    pm.getPackageInfo(
                        packageName,
                        PackageManager.PackageInfoFlags.of(PackageManager.GET_SIGNING_CERTIFICATES.toLong())
                    )
                } else {
                    @Suppress("DEPRECATION")
                    pm.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
                }
                val sigs = info.signingInfo.apkContentsSigners
                for (sig in sigs) {
                    val md = MessageDigest.getInstance("SHA")
                    md.update(sig.toByteArray())
                    val keyHash = Base64.encodeToString(md.digest(), Base64.NO_WRAP)
                    Log.d(TAG_HASH, keyHash)   // ← 이 값을 콘솔 '키 해시'에 추가
                }
            } else {
                @Suppress("DEPRECATION")
                val info = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
                @Suppress("DEPRECATION")
                for (sig in info.signatures) {
                    val md = MessageDigest.getInstance("SHA")
                    md.update(sig.toByteArray())
                    val keyHash = Base64.encodeToString(md.digest(), Base64.NO_WRAP)
                    Log.d(TAG_HASH, keyHash)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG_HASH, "Failed to get key hash", e)
        }
    }
}
