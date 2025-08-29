package com.tripchung.app.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Android
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.PhoneIphone
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * 세련된 로그인/회원가입 화면
 */
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit = {},
    onBrowse: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }

    var isLogin by remember { mutableStateOf(true) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var keepSignedIn by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(false) }

    fun isValidEmail(s: String) = s.contains("@") && s.contains(".") && s.length >= 6

    suspend fun performAuth() {
        if (!isValidEmail(email)) {
            snackbar.showMessage("이메일 형식을 확인해 주세요.")
            return
        }
        if (password.length < 6) {
            snackbar.showMessage("비밀번호는 6자 이상이어야 해요.")
            return
        }
        if (!isLogin && name.isBlank()) {
            snackbar.showMessage("이름을 입력해 주세요.")
            return
        }

        isLoading = true
        // TODO 실제 네트워크 요청으로 교체
        kotlinx.coroutines.delay(900)
        isLoading = false
        onLoginSuccess()
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbar) }) { inner ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFE8F5E9), Color.White, Color(0xFFFFF8E1))
                    )
                )
                .padding(inner)
                .padding(18.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                tonalElevation = 2.dp,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    Modifier.padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 로고
                    Box(
                        modifier = Modifier
                            .size(84.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFF34C759), Color(0xFF2E8B57))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "트",
                            color = Color.White,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                    Text("트립충", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
                    Text("충청도 AI 여행 가이드", color = MaterialTheme.colorScheme.onSurfaceVariant)

                    Spacer(Modifier.height(20.dp))

                    // 탭
                    Surface(color = Color(0xFFF2F4F7), shape = RoundedCornerShape(16.dp)) {
                        Row(
                            Modifier.fillMaxWidth().padding(6.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            ToggleChip("로그인", isLogin, { isLogin = true }, Modifier.weight(1f))
                            ToggleChip("회원가입", !isLogin, { isLogin = false }, Modifier.weight(1f))
                        }
                    }

                    Spacer(Modifier.height(18.dp))

                    // 폼
                    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        AnimatedVisibility(!isLogin, enter = fadeIn(), exit = fadeOut()) {
                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                label = { Text("이름") },
                                singleLine = true,
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("이메일") },
                            singleLine = true,
                            leadingIcon = { Icon(Icons.Outlined.Email, null) },
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("비밀번호") },
                            singleLine = true,
                            visualTransformation = if (showPassword) VisualTransformation.None
                            else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { showPassword = !showPassword }) {
                                    Icon(
                                        if (showPassword) Icons.Outlined.VisibilityOff
                                        else Icons.Outlined.Visibility,
                                        null
                                    )
                                }
                            },
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        AnimatedVisibility(isLogin) {
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(checked = keepSignedIn, onCheckedChange = { keepSignedIn = it })
                                    Text("로그인 상태 유지", color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                Text(
                                    "비밀번호 찾기",
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.clickable { /* TODO */ }
                                )
                            }
                        }

                        Button(
                            onClick = { scope.launch { performAuth() } },
                            enabled = !isLoading,
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    strokeWidth = 2.dp,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(Modifier.width(10.dp))
                            }
                            Text(if (isLogin) "로그인" else "회원가입", fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(Modifier.height(18.dp))
                    // divider
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.weight(1f).height(1.dp).background(Color(0xFFE5E7EB)))
                        Text("또는", color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(horizontal = 10.dp))
                        Box(Modifier.weight(1f).height(1.dp).background(Color(0xFFE5E7EB)))
                    }

                    // 소셜 로그인 (대체 아이콘 사용)
                    Spacer(Modifier.height(12.dp))
                    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = { scope.launch { snackbar.showMessage("Google 로그인 준비중") } },
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth().height(48.dp)
                        ) {
                            Icon(Icons.Outlined.Android, null) // 대체 아이콘
                            Spacer(Modifier.width(8.dp))
                            Text("Google로 계속하기")
                        }

                        OutlinedButton(
                            onClick = { scope.launch { snackbar.showMessage("카카오 로그인 준비중") } },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color(0xFFFFE812),
                                contentColor = Color(0xFF3B1E1E)
                            ),
                            modifier = Modifier.fillMaxWidth().height(48.dp)
                        ) {
                            Box(Modifier.size(18.dp).clip(CircleShape).background(Color(0xFF3B1E1E)))
                            Spacer(Modifier.width(8.dp))
                            Text("카카오로 계속하기")
                        }

                        OutlinedButton(
                            onClick = { scope.launch { snackbar.showMessage("Apple 로그인 준비중") } },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color.Black,
                                contentColor = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth().height(48.dp)
                        ) {
                            Icon(Icons.Outlined.PhoneIphone, null) // 대체 아이콘
                            Spacer(Modifier.width(8.dp))
                            Text("Apple로 계속하기")
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // 하단 링크
                    Text(
                        text = if (isLogin) "아직 계정이 없으신가요?  회원가입" else "이미 계정이 있으신가요?  로그인",
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isLogin = !isLogin }
                            .padding(vertical = 6.dp)
                    )

                    Text(
                        text = "둘러보기로 시작하기",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable { onBrowse() }
                            .padding(6.dp)
                    )

                    AnimatedVisibility(!isLogin) {
                        Text(
                            "회원가입 시 이용약관 및 개인정보처리방침에 동의한 것으로 간주됩니다.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.labelSmall,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 8.dp, start = 6.dp, end = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ToggleChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = if (selected) Color.White else Color.Transparent,
        tonalElevation = if (selected) 1.dp else 0.dp,
        modifier = modifier
    ) {
        Box(
            Modifier
                .height(44.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                color = if (selected) MaterialTheme.colorScheme.onSurface
                else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/** Snackbar helper */
private suspend fun SnackbarHostState.showMessage(message: String) {
    this.showSnackbar(message = message, withDismissAction = true)
}
