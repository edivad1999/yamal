package com.yamal.feature.login.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yamal.designSystem.components.button.ButtonColor
import com.yamal.designSystem.components.button.ButtonFill
import com.yamal.designSystem.components.button.ButtonShape
import com.yamal.designSystem.components.button.ButtonSize
import com.yamal.designSystem.components.button.YamalButton
import com.yamal.designSystem.components.text.Text
import com.yamal.designSystem.icons.Icon
import com.yamal.designSystem.icons.Icons
import com.yamal.designSystem.theme.YamalTheme
import com.yamal.feature.login.ui.presenter.LoginEffect
import com.yamal.feature.login.ui.presenter.LoginIntent
import com.yamal.feature.login.ui.presenter.LoginPresenter
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.koinInject

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    authCodeFlow: StateFlow<String?>,
    launchBrowser: (String) -> Unit,
    presenter: LoginPresenter = koinInject(),
) {
    val state by presenter.state.collectAsState()

    LaunchedEffect(state.isLoggedIn) {
        if (state.isLoggedIn) {
            onLoginSuccess()
        }
    }

    // Collect auth code from external source
    LaunchedEffect(Unit) {
        authCodeFlow.collectLatest { code ->
            if (code != null) {
                presenter.processIntent(LoginIntent.AuthorizationComplete(code))
            }
        }
    }

    LaunchedEffect(Unit) {
        presenter.getEffects().collectLatest { effect ->
            when (effect) {
                is LoginEffect.OpenBrowser -> {
                    launchBrowser(effect.url)
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        // Header with close button (top-left)
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp),
        ) {
            YamalButton(
                onClick = { /* Optional: handle close/back action */ },
                color = ButtonColor.Default,
                fill = ButtonFill.None,
                shape = ButtonShape.Rounded,
                modifier = Modifier.size(40.dp),
            ) {
                Icon(
                    Icons.Outlined.Close,
                    contentDescription = "Close",
                    tint = YamalTheme.colors.text,
                    modifier = Modifier.size(24.dp),
                )
            }
        }

        // Main content (centered)
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            // Hero illustration with glow effect
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(bottom = 40.dp),
            ) {
                // Glow effect
                Box(
                    modifier =
                        Modifier
                            .size(280.dp)
                            .clip(CircleShape)
                            .background(YamalTheme.colors.primary.copy(alpha = 0.2f))
                            .blur(40.dp),
                )

                // Main circular container
                Box(
                    modifier =
                        Modifier
                            .size(256.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors =
                                        listOf(
                                            YamalTheme.colors.primary.copy(alpha = 0.3f),
                                            YamalTheme.colors.primary.copy(alpha = 0.1f),
                                            Color.Transparent,
                                        ),
                                ),
                            ),
                    contentAlignment = Alignment.Center,
                ) {
                    // Link icon with backdrop
                    Box(
                        modifier =
                            Modifier
                                .size(72.dp)
                                .clip(
                                    androidx.compose.foundation.shape
                                        .RoundedCornerShape(16.dp),
                                ).background(YamalTheme.colors.background.copy(alpha = 0.9f)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            Icons.Outlined.Link,
                            contentDescription = null,
                            tint = YamalTheme.colors.primary,
                            modifier = Modifier.size(40.dp),
                        )
                    }
                }
            }

            // Title
            Text(
                text = "Connect to MyAnimeList",
                style = YamalTheme.typography.displaySmall,
                textAlign = TextAlign.Center,
                color = YamalTheme.colors.text,
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Description
            Text(
                text = "You will be redirected to your browser to authenticate. We do not store your password.",
                style = YamalTheme.typography.body,
                textAlign = TextAlign.Center,
                color = YamalTheme.colors.textSecondary,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }

        // Footer with action buttons (bottom)
        Column(
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors =
                                listOf(
                                    Color.Transparent,
                                    YamalTheme.colors.background,
                                    YamalTheme.colors.background,
                                ),
                        ),
                    ).windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // Primary action button
            YamalButton(
                onClick = {
                    presenter.processIntent(LoginIntent.OpenLoginBrowser(state.authorizationUrl))
                },
                color = ButtonColor.Primary,
                size = ButtonSize.Large,
                shape = ButtonShape.Rounded,
                block = true,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Continue to Login")
                Spacer(Modifier.width(8.dp))
                Icon(
                    Icons.Outlined.Export,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
            }

            // Secondary action button
            YamalButton(
                text = "Cancel and browse as guest",
                onClick = { /* Optional: handle guest browsing */ },
                color = ButtonColor.Default,
                fill = ButtonFill.None,
                size = ButtonSize.Middle,
                block = true,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
