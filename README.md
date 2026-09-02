# Remindly

A native Android reminder app built with Kotlin and Jetpack Compose, inspired by the UX of a reference reminder app (see [`docs/VIDEO_ANALYSIS.md`](docs/VIDEO_ANALYSIS.md) for the frame-by-frame visual analysis this rebuild is based on). Original branding, icons, and illustrations — no assets or code copied from the reference app.

## Tech stack

- Kotlin, Jetpack Compose, Material 3
- Navigation Compose (including a nested "editor" graph so New/Edit Reminder, Repeat, Category, and Location Picker share one `ReminderEditorViewModel`)
- Room (reminders + categories), DataStore (settings/preferences)
- AlarmManager + BroadcastReceiver + NotificationManager for scheduled reminders
- Play services Geofencing API for place-based reminders
- Manual dependency injection via `AppContainer` (no DI framework — the app is small enough that one would add ceremony without benefit)
- JUnit + Robolectric for unit tests, Compose UI test for instrumented tests

## Project structure

```
app/src/main/java/com/remindly/app/
  data/local/         Room entities, DAOs, database, type converters
  data/datastore/      DataStore-backed settings
  data/repository/      Repository implementations
  domain/model/         Plain domain models (Reminder, Category, enums)
  domain/repository/    Repository interfaces (ports)
  domain/usecase/      RepeatCalculator, QuickAddParser
  notification/        NotificationHelper, AlarmScheduler
  location/           GeofenceManager
  receiver/           AlarmReceiver, NotificationActionReceiver, BootRestoreReceiver, GeofenceBroadcastReceiver, PhoneCallReceiver
  permissions/        Runtime + special-permission helpers
  ui/theme/           Design system (colors, type, shapes, spacing)
  ui/components/       Reusable Compose components
  ui/screens/         One package per screen
  ui/navigation/       Routes + NavHost graph
```

## Building

```bash
./gradlew assembleDebug
```

APK output: `app/build/outputs/apk/debug/app-debug.apk`

## Testing

```bash
./gradlew testDebugUnitTest          # JVM unit tests (Room via Robolectric, ViewModels, use cases)
./gradlew connectedDebugAndroidTest  # Compose UI tests — needs a connected device/emulator
```

## Known limitations (MVP scope)

- **Billing**: `PremiumRepository` is a dev-mode implementation (a persisted flag flipped by the Premium screen's "Continue" button) — no real Google Play Billing integration.
- **Ads**: `AdsManager` is a no-op abstraction; no ad SDK is wired in.
- **Location picker**: no interactive map (would need a Maps SDK key) — "Use current location" + reverse geocoding + a manual place-name field.
- **Geofencing**: registers on-enter triggers only; on-exit is supported by `GeofenceManager` but not yet exposed as a per-reminder option in the UI.
- **After-call reminders**: only uses call *state* (idle/ringing/off-hook) via `READ_PHONE_STATE` — never reads call logs, audio, or numbers, per Android's privacy restrictions.
- **Languages**: only English is populated today; the string-resource + DataStore architecture supports adding more without code changes to screens.
