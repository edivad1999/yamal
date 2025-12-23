package com.yamal.feature.login.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yamal.designSystem.components.YamalButton
import com.yamal.designSystem.components.YamalButtonSize
import com.yamal.designSystem.components.YamalButtonType
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

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            Icons.Filled.Profile,
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = YamalTheme.colors.paletteColors.color6,
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Welcome to YAMAL",
            style = YamalTheme.typography.h3,
            textAlign = TextAlign.Center,
            color = YamalTheme.colors.neutralColors.title,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Yet Another MyAnimeList Client",
            style = YamalTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = YamalTheme.colors.neutralColors.secondaryText,
        )

        Spacer(modifier = Modifier.height(48.dp))

        YamalButton(
            text = "Login with MyAnimeList",
            onClick = {
                presenter.processIntent(LoginIntent.OpenLoginBrowser(state.authorizationUrl))
            },
            type = YamalButtonType.Primary,
            size = YamalButtonSize.Large,
        )
    }
}
