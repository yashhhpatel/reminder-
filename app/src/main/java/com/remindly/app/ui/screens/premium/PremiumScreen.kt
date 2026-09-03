package com.remindly.app.ui.screens.premium

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.remindly.app.R
import com.remindly.app.ui.RemindlyViewModelFactory
import com.remindly.app.ui.components.PrimaryButton
import com.remindly.app.ui.theme.AppSpacing
import com.remindly.app.ui.theme.BrandPurpleLight
import com.remindly.app.ui.theme.PaywallAccentGreen
import com.remindly.app.ui.theme.PaywallBackground

@Composable
fun PremiumScreen(
    factory: RemindlyViewModelFactory,
    onClose: () -> Unit,
    onOpenTerms: () -> Unit,
    onOpenPrivacy: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PremiumViewModel = viewModel(factory = factory),
) {
    val purchaseCompleted by viewModel.purchaseCompleted.collectAsState()

    LaunchedEffect(purchaseCompleted) {
        if (purchaseCompleted) onClose()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PaywallBackground)
            .padding(AppSpacing.lg),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            IconButton(onClick = onClose) {
                Icon(Icons.Filled.Close, contentDescription = stringResource(R.string.close), tint = Color.White)
            }
        }

        Spacer(Modifier.padding(top = AppSpacing.md))

        Text(
            stringResource(R.string.premium_headline),
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold,
        )

        Spacer(Modifier.padding(top = AppSpacing.sm))
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .size(width = 48.dp, height = 3.dp)
                .background(BrandPurpleLight),
        )

        Spacer(Modifier.padding(top = AppSpacing.xl))

        BenefitRow(stringResource(R.string.premium_feature_places))
        BenefitRow(stringResource(R.string.premium_feature_ads))
        BenefitRow(stringResource(R.string.premium_feature_advanced))
        BenefitRow(stringResource(R.string.premium_feature_support))

        Spacer(Modifier.weight(1f))

        PrimaryButton(
            text = stringResource(R.string.premium_continue),
            onClick = { viewModel.continuePurchase() },
            containerColor = PaywallAccentGreen,
        )

        Spacer(Modifier.padding(top = AppSpacing.sm))
        Text(
            stringResource(R.string.premium_renew_note),
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.Filled.Shield,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.size(14.dp),
            )
            Spacer(Modifier.padding(start = 4.dp))
            Text(
                stringResource(R.string.premium_cancel_note),
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.7f),
            )
        }

        Spacer(Modifier.padding(top = AppSpacing.md))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            FooterLink(stringResource(R.string.premium_terms), onOpenTerms)
            FooterLink(stringResource(R.string.premium_privacy), onOpenPrivacy)
            FooterLink(stringResource(R.string.premium_restore)) { viewModel.restore() }
        }
    }
}

@Composable
private fun BenefitRow(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = AppSpacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(Icons.Filled.Check, contentDescription = null, tint = PaywallAccentGreen)
        Spacer(Modifier.padding(start = AppSpacing.sm))
        Text(text, color = Color.White, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun FooterLink(text: String, onClick: () -> Unit) {
    Text(
        text,
        color = Color.White.copy(alpha = 0.6f),
        style = MaterialTheme.typography.labelSmall,
        modifier = Modifier.clickableSimple(onClick),
    )
}

private fun Modifier.clickableSimple(onClick: () -> Unit): Modifier =
    this.clickable(onClick = onClick)
