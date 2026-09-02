package com.remindly.app.ui.screens.location

import android.Manifest
import android.location.Geocoder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.LocationServices
import com.remindly.app.R
import com.remindly.app.permissions.PermissionUtils
import com.remindly.app.ui.components.AppTopBar
import com.remindly.app.ui.components.PrimaryButton
import com.remindly.app.ui.screens.newreminder.ReminderEditorViewModel
import com.remindly.app.ui.theme.AppSpacing
import com.remindly.app.ui.theme.AppTheme
import java.util.Locale

@Composable
fun LocationPickerScreen(
    reminderEditorViewModel: ReminderEditorViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var placeName by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf<Double?>(null) }
    var longitude by remember { mutableStateOf<Double?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun resolveCurrentLocation() {
        isLoading = true
        errorMessage = null
        val fusedClient = LocationServices.getFusedLocationProviderClient(context)
        runCatching {
            fusedClient.lastLocation
                .addOnSuccessListener { location ->
                    isLoading = false
                    if (location == null) {
                        errorMessage = "Couldn't determine your current location. Try again outdoors."
                        return@addOnSuccessListener
                    }
                    latitude = location.latitude
                    longitude = location.longitude
                    placeName = runCatching {
                        val geocoder = Geocoder(context, Locale.getDefault())
                        @Suppress("DEPRECATION")
                        geocoder.getFromLocation(location.latitude, location.longitude, 1)
                            ?.firstOrNull()
                            ?.let { it.locality ?: it.subAdminArea ?: it.featureName }
                    }.getOrNull() ?: "Selected location"
                }
                .addOnFailureListener {
                    isLoading = false
                    errorMessage = "Couldn't determine your current location."
                }
        }.onFailure {
            isLoading = false
            errorMessage = "Location lookup failed."
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted ->
        if (granted) resolveCurrentLocation() else errorMessage = context.getString(R.string.location_permission_required)
    }

    Column(modifier = modifier.fillMaxSize()) {
        AppTopBar(title = stringResource(R.string.location_picker_title), onBack = onBack)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(AppSpacing.md),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.md),
        ) {
            if (!PermissionUtils.hasFineLocationPermission(context)) {
                Text(
                    stringResource(R.string.location_permission_required),
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTheme.extendedColors.textSecondary,
                )
                PrimaryButton(
                    text = stringResource(R.string.location_grant_permission),
                    onClick = { locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION) },
                )
            } else {
                PrimaryButton(
                    text = stringResource(R.string.location_use_current),
                    loading = isLoading,
                    onClick = { resolveCurrentLocation() },
                )
            }

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(top = AppSpacing.sm))
            }

            errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            if (latitude != null && longitude != null) {
                OutlinedTextField(
                    value = placeName,
                    onValueChange = { placeName = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.location_search_hint)) },
                    leadingIcon = { Icon(Icons.Filled.LocationOn, contentDescription = null) },
                    colors = OutlinedTextFieldDefaults.colors(),
                )
                Text(
                    "%.5f, %.5f".format(latitude, longitude),
                    style = MaterialTheme.typography.bodySmall,
                    color = AppTheme.extendedColors.textSecondary,
                )

                PrimaryButton(
                    text = stringResource(R.string.save),
                    onClick = {
                        reminderEditorViewModel.setPlace(latitude!!, longitude!!, placeName.ifBlank { "Selected location" })
                        onBack()
                    },
                )
            }
        }
    }
}
