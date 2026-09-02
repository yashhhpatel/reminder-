# Reference App — Video/Screenshot Visual & Structural Analysis

Source: 100 sequential JPEG frames (`f_0001.jpg`…`f_0100.jpg`), extracted every 2.5s from a
~4 minute screen-recording of a reference reminder/to-do Android app. Frames are small
(~380px wide) so exact hex colors and fine print cannot be guaranteed — approximate
colors/names are used and called out where uncertain. This document is a VISUAL/STRUCTURAL
reference only; no branding/text should be copied verbatim.

Device used for recording: appears to be an Android phone with 3-button-ish gesture nav
(shows a pill/home/back row at bottom in system screens), status bar with clock top-left,
signal/battery top-right. Screen recording indicator confirms this is a real device capture
(not an emulator), battery drains from 9% to 7% over the session, elapsed recording timer
visible at one point ("02:12").

---

## 1. Ordered Flow (chronological, frame-referenced)

1. **f_0001** — Android home screen (not part of app; just launch context).
2. **f_0002–0003** — Onboarding / Welcome screen (light mode). Tap "START NOW".
3. **f_0003** — System dialog: "Allow #Reminder to send you notifications?" (POST_NOTIFICATIONS runtime permission).
4. **f_0004** — Custom dialog: "Notification Required" explaining why permission is needed (shown when a required permission is skipped/denied or as pre-prompt), with OK button.
5. **f_0005** — Notification permission system dialog appears again (re-prompt after explanation).
6. **f_0006** — System dialog: "Allow #Reminder to make and manage phone calls?" (READ_PHONE_STATE/CALL permission, tied to "After Call Reminders" feature).
7. **f_0007** — Custom pre-permission screen: "One last step! ... Grant permission to view and set reminders immediately after ending a call." with a mock illustration of the system "Display over other apps" toggle screen and a "GO TO SETTINGS" pill button.
8. **f_0008–0009** — Loading spinner while navigating to system settings ("Display over other apps" page, "Loading...").
9. **f_0010–0029** — System Settings: "Display over other apps" permission manager. Shows a searchable/scrollable list of ALL installed apps with Allowed/Denied status per app (system UI, not the app's own UI — chrome differs from app: plain list, black/gray link-colored status text). User scrolls through the full list (Messages, Phone, #Reminder, Android Auto, Axis Mobile, Calculator, Camera, Clock, CoCoFlix, CricHeroes, DMSS, Facebook, Feedback, FilmTV, Games, Global Search, Google, Google Play services, Google Play Services for AR, HeyTap Cloud, System Messages, Telegram, Theme Store, Threads, Truecaller, Weather Services, Wireless Earphones, YouTube, Zen Mode, DMSS, etc.), finds "#Reminder" (denied), taps it → detail toggle screen ("#Reminder / Display over other apps" with a single toggle, off by default) → toggles it ON.
10. **f_0016** — Returns to the app's custom "One last step!" screen, now showing the toggle switched ON with a finger/cursor icon animation cue.
11. **f_0023–0024** — **Language selection screen** (light mode): full list of 13 languages (English, Spanish, Hindi, French, Chinese, Arabic, Russian, Portuguese, Italian, Bengali, German, Japanese, Korean) as a radio-button list; English pre-selected; purple circular checkmark button top-right to confirm.
12. **f_0025, 0027** — **Privacy/consent screen**: "Your comfort comes first!" — light mode, purple heading, black body text explaining data collection (location, device identifiers, ads, analytics), links ("Do Not Sell or Share...", "Privacy Policy", "Notice at Collection"), two full-width pill buttons: "ACCEPT" (filled purple) and "DECLINE" (filled purple, appears same style/weight as Accept — not de-emphasized).
13. **f_0026** — System dialog triggered by Accept: "Allow #Reminder to access this device's location?" — standard Android location dialog with Precise/Approximate map icons and buttons: "While using the app" / "Only this time" / "Don't allow".
14. **f_0028** — **"My reminder" home screen — Empty State** (light mode). Top bar: "My reminder" title left, a red circular "AD" icon and a gear/settings icon top-right. Center illustration (person on couch with laptop, plant, wall clock) with text "Welcome to Reminders" / "Tap the Add reminder to add your first reminder" and a large pill CTA "Add reminder".
15. **f_0029–0038** — **Settings screen** (opened via gear icon), first in light mode then app theme changed to Dark Mode live in front of the user. Sections: green "Full access / Unlock full functionality" promo banner with crown icon + "Go Pro" pill button; "General" (Language, App Theme); "Presets" (Add time — toggle ON; **Add place — has a small crown/badge icon, ships in a locked/gold-outlined card, toggle stays OFF and is likely disabled/non-interactive**; Reminder after a call — toggle ON).
16. **f_0033, 0035–0036** — **App Theme dialog** (modal, center card over dimmed background): radio options "Auto Mode" / "Light Mode" / "Dark Mode" + "Save" pill button. User selects Light Mode then later re-opens and selects/confirms Dark Mode.
17. **f_0037–0038** — Settings screen fully in **Dark Mode**, scrolled further: "Communicate" section (Feedback, Privacy Policy), continuing to Presets bottom.
18. **f_0039–0040** — **Paywall / "Go Pro" screen** (dark mode, near-black/dark-teal background) triggered from the Go Pro button: heading "Get Unlimited access to premium features.", underline accent bar, 3 checklist bullets ("Unlimited Add Places – Break Barriers!", "Remove every single ad – Zero Interruptions!", "365 Days, 24/7 Assistance – Here for You!"), bottom sticky bright-green pill CTA "Continue", subtext "Subscription renews automatically unless cancelled", a shield icon line "Cancel anytime. Secure with Play Store", and small footer links "Terms & C..." | "Privacy Pol..." | "Restore". Close (X) icon top-right.
19. **f_0041–0042** — Back to Settings (dark), scrolled: "Add place" premium card, "Reminder after a call" toggle (briefly appears OFF in one frame, likely an accidental tap, then ON again), "Communicate" (Feedback, Privacy Policy, Privacy Settings), "Others" section beginning.
20. **f_0043–0049** — **Off-app diversion**: user taps "Feedback" which opens an external Gmail compose draft (To: a support address, Subject "Feedback for Reminder app", body "Version :- 32.4.324"), briefly checks Gmail inbox, opens Android recent-apps/task-switcher (showing the Settings screen mid-app plus system notification tray elements and a "Close all" button), and opens a browser to a Privacy Policy URL which fails to load (Chrome dinosaur offline error page) — none of this is the app's own UI, but confirms: app version string "32.4.324" / "32.4", and package concept name "#Reminder" is a placeholder/hashtag prefix used for internal app naming (likely a build/debug label, not real branding).
21. **f_0050** — Back to Android recents/task switcher.
22. **f_0051–0052** — **Privacy Settings screen** (dark mode): single row "Location Permission" with description text and a toggle, ON (purple).
23. **f_0053, 0056** — Settings screen scrolled to bottom: "Communicate" (Feedback, Privacy Policy, Privacy Settings), "Others" (Share, Rate Us), and a centered small-caption footer "App version 32.4".
24. **f_0054** — Android system notification shade appears momentarily (battery saver prompt, screen-recording control, WhatsApp notification) — device chrome, not app UI.
25. **f_0055, 0096** — **Android system Share sheet** (bottom sheet) triggered from "Share" in Settings and later from a reminder's Share action: copy-link chip, "Share via Nearby Share" row, then app grid (WhatsApp, Telegram, My Files/Save to My Files, Truecaller, Instagram, Notes, Chrome, Files by Google), page dots indicating more apps.
26. **f_0057** — Brief Google Play Store loading screen (from "Rate Us").
27. **f_0058** — Settings screen (dark) again, scrolled to top (Go Pro banner, Language, App Theme = Dark Mode).
28. **f_0059** — "My reminder" empty state, now rendered in **Dark Mode**.
29. **f_0060–0069** — **New Reminder creation screen** (dark mode): top bar "New reminder" with back arrow and a checkmark (save) icon; "Memo" text input row with two trailing icons (checklist icon, image/attachment icon); "Add time" row with toggle (OFF by default); "Add place" row with crown/sun badge icon, gold/amber outlined card, toggle OFF (locked — premium); a category chip row at the bottom showing a colored dot + "My reminder" (default category). User types "Tant" into the Memo field (partial/likely truncated text — actual final word not fully visible, keyboard autocomplete suggests "Tant"/"Tang"/"Tantra"/"Thik" etc., so treat as illustrative dummy text, not literal spec content).
30. **f_0070** — Toggling "Add time" ON reveals an inline expanded panel: date/time row ("Today 01:30 AM" with a calendar icon and a red minus/remove-circle icon), then a list of quick-pick presets ("1 hour from now", "Today 7:00 AM", "Today 3:00 PM", "Today 10:00 PM"), then two more rows: "Don't repeat" (repeat icon) and "Ring once" (speaker icon) — both act as entry points to sub-screens.
31. **f_0071** — **Date-picker bottom sheet/dialog** (Material date picker): "- Select Date -" label, large date display "Sep 2, 2026" with edit-pencil icon, month header "September 2026" with prev/next chevrons, calendar grid (S M T W T F S), selected date filled purple circle, today outlined in purple ring, "Cancel" / "OK" text buttons bottom-right.
32. **f_0072–0074** — After date selection the date/time row updates to "Tomorrow · 01:30 AM".
33. **f_0075** — **Sound options bottom sheet/popover** (appears anchored below the time row, dark card, rounded): two options — "Ring once — Notification and short sound" (selected, filled purple radio) and "Keep ringing — Notification and long sound" (empty radio).
34. **f_0076–0077** — **Repeat options screen** (full screen, dark, back arrow + "Repeat" title): radio list — "Don't repeat" (selected), "Every minute", "Every hour", "Every day", "Every week", "Every month", "Every year".
35. **f_0079–0080** — **Select Category screen** (full screen, dark, back arrow + "Select category" title): list rows each with a colored ring/dot avatar + name + a count badge ("0"): "My reminders" (purple, checked/selected), "Work" (blue outline), "Personal" (red/pink outline), "My Health" (green/lime outline), "Finance" (indigo outline); final row "+ Add category" (purple plus icon).
36. **f_0081** — **Add Category screen** (full screen, dark): "Enter here" text input (name), "Colour" label, a swatch grid of 9 color circles — row 1: gold/yellow, red, orange, lime-green, teal; row 2: blue, magenta/purple, indigo (selected, shown with an outer ring) — plus presumably a 9th/overflow. Checkmark (save) top-right.
37. **f_0083–0084** — Back in New Reminder screen, category row now shows "Work" (blue dot) instead of default.
38. **f_0085** — **"My reminder" list screen with content** (dark mode): a collapsible section header "Soon" with a down-chevron, one reminder row as a checkbox-style card: empty square checkbox (outlined blue) + title "Tant" + subtitle "Tomorrow · 01:30 AM". Below the list, a persistent **bottom quick-add bar**: rounded text field with placeholder example text ("Drink Water at 7 PM" — rotating example hint) + a filled purple circular "+" send button to its right.
39. **f_0086–0088** — Tapping the quick-add field expands a **suggestion chip row** directly above the keyboard: pill-shaped bell-icon chips — "Later today", "This evening", "No time", "Today HH:MM AM" (a live current-time chip) — plus the text field ("Add reminder") and the purple circular checkmark/send button. This is the fast natural-language/quick-time-pick add flow.
40. **f_0089–0090** — **Search screen** (dark, back arrow + rounded "Search" input at top): live results list below showing matching reminders (same checkbox-card style, e.g. "Tant / Tomorrow · 01:30 AM").
41. **f_0091–0092** — **Reminder Detail screen** (dark, back arrow only, no title bar text — content pushed up): stacked info cards — Memo title card ("Tant"), a "Time" card grouping Tomorrow·01:30 AM / Don't repeat / Ring once (with icons), a category card ("Work", blue dot). **Bottom action bar** with 4 icon+label buttons: "Complete" (double-check icon), "Edit" (pencil), "Share" (share-nodes icon), "Delete" (trash icon).
42. **f_0093** — **"My reminder" list — Completed section**: header "Completed" with chevron; the reminder now shown with a filled/checked checkbox and strikethrough title text "~~Tant~~" plus "Completed Today 01:17 AM" subtitle. A native ad banner unit appears pinned at the very bottom of the screen (white background even in dark mode — a 3rd-party ad SDK banner, e.g. "Drivers allege E20 fuel blend..." with a red "LEARN MORE" button) — this is an AdMob/ad-network banner, not custom app UI.
43. **f_0094** — Reminder detail screen reflecting completed state: title shows strikethrough "Tant" with "Completed: Today 01:17 AM" instead of the plain memo card, still showing Time card and category card, bottom action bar now shows only "Edit" / "Share" / "Delete" (no "Complete" action once already completed).
44. **f_0095** — Share sheet opened again from detail view.
45. **f_0096–0097** — **Edit Reminder screen**: same layout as New Reminder (Memo, Add time, Add place, category row). Toggling "Add time" OFF surfaces an inline warning row: a bell-with-slash icon + gray text "You won't get notification." directly under the category card.
46. **f_0098** — Reminder detail card view again.
47. **f_0099–0100** — Returns to **empty state** "Welcome to Reminders" (dark mode) — implying the single test reminder was deleted/completed-cleared by the end of the walkthrough.

---

## 2. Screen-by-Screen Specification

### 2.1 Onboarding / Welcome Screen
- **Purpose**: First-run intro explaining core permissions-driven features before requesting them.
- **Layout (top→bottom)**: Status bar → large two-line headline ("Welcome to a new kind of" regular weight + "Reminder" bold, larger) → two feature rows, each: circular/line icon (bell; phone-with-arrow) + bold title + gray description text, stacked vertically with generous spacing → large empty flex space → full-width pill button "START NOW" (with an animated tap/finger-cursor icon overlay near it, suggesting an onboarding tutorial hint animation) → small centered legal text "By Continuing agree to our Privacy Policy and Terms & Conditions" (links underlined, colored).
- **Colors**: Background near-white/very light gray-blue (~#F1F2F6). Headline text near-black. Body/description text medium gray. CTA button vivid purple/indigo (~#6A3DE8 to #5B3AD1, looks like a vertical gradient darker at bottom giving it a slight 3D pill look). Button text white bold.
- **Card/shape impression**: No cards; the CTA button is a full stadium/pill shape (very rounded, fully round ends).
- **Icons**: Outline bell icon, outline phone-with-diagonal-slash-like "after call" icon (looks like a phone handset with a small arrow/redirect glyph).
- **Dialogs**: Sequentially, tapping Start Now triggers OS permission dialogs (see §3).
- **Transition**: Not directly observable, but permission dialogs appear as standard Android modal dialogs (centered white rounded-rect card over a dimmed/scrimmed version of the same screen — content behind is visible but darkened), consistent with system dialog behavior, not custom transitions.
- **Mode**: Light only observed here.

### 2.2 Pre-Permission Explainer — "One last step!" (Display over other apps)
- **Purpose**: Custom "soft-ask" screen shown before requesting the special "Display over other apps" system permission (needed for after-call reminder overlay).
- **Layout**: Headline "One last step!" (bold, larger) + gray explanatory paragraph → a **mock phone frame illustration** (rounded rectangle outlined like a phone bezel) containing a faux settings row: bell/app icon + "#Reminder" name, with a hand/cursor icon pointing at it (and later, a toggle shown ON with the same hand cue) → bottom pill button "GO TO SETTINGS" (same purple as onboarding, tap-hint hand icon overlay).
- **Colors**: Same light background, purple CTA, black bezel/frame illustration line art.
- **Mode**: Light.
- **Note**: This mock illustration is purely decorative UI teaching the user what to expect in system settings — it is not real navigable UI, just an image.

### 2.3 System Settings — "Display over other apps" (OS chrome, not app UI)
- **Purpose**: Standard Android permission-manager screen (do not rebuild this — it's OS UI) reached by deep-linking out of the app.
- **Notable structure for reference**: Top app bar "Display over other apps" with back arrow + overflow (⋮) menu; a two-segment toggle/filter pill "All" / "Filtered set (e.g. Allowed)"; below, a plain scrollable list where each row = small app icon + app name (bold) + status text ("Allowed"/"Denied", link-blue colored) — sorted alphabetically. Tapping an app row opens an app-specific detail page: icon, app name, version number caption, a single "Display over other apps" toggle row (white/light card even in light-mode OS theme) and the same explanatory paragraph.
- Confirms: the toggle-off state color for this OS toggle is plain gray (not the app's purple), and toggle-on is a darker/blue-purple — this is OS-level Material toggle styling, distinct from the app's own accent color.

### 2.4 Language Selection Screen
- **Purpose**: Language preference selector, reached from onboarding flow and also from Settings.
- **Layout**: Centered title "Language" with a purple circular checkmark confirm button top-right (floating, circular, filled purple with white check) → a single rounded white card containing a vertical list of language rows, each a full-width tap target with left-aligned label and right-aligned radio circle, separated by thin hairline dividers.
- **Content (all visible)**: English (selected — filled purple radio with white checkmark-style dot), Spanish, Hindi, French, Chinese, Arabic, Russian, Portuguese, Italian, Bengali, German, Japanese, Korean.
- **Colors**: Light background, white list card, purple selected-radio, gray unselected radio outline, black text.
- **Corner radius**: Card has large rounded corners (looks ~16–20dp); overall card appears to be one continuous rounded rectangle housing all rows (not individually-rounded row chips).
- **Mode**: Light (only seen in light mode in this recording).

### 2.5 Privacy/Consent Screen — "Your comfort comes first!"
- **Purpose**: GDPR/CCPA-style ad-consent and data-collection disclosure (likely a Google UMP-style consent form or custom equivalent), shown once during onboarding.
- **Layout**: Purple bold headline "Your comfort comes first!" → black bold subheading "Ensuring your privacy" → paragraph of body copy → bold subheading "We enhance your app experience by collecting:" → bullet list (Precise location data and Device Identifiers; Show location details for relevant results; Deliver tailored ads and improve performance; Perform Advertising and Location-based analytics; Improve connectivity and app features on wireless networks) → fine-print paragraph with underlined links (Do Not Sell or Share My Personal Information / Limit Use and Disclosure of Sensitive Personal Information / Privacy Policy / Notice at Collection) and consent statement → two full-width stacked pill buttons "ACCEPT" and "DECLINE", both same filled-purple styling (no visual de-emphasis on Decline).
- **Colors**: White/light background, purple heading, black body text, purple buttons with white bold uppercase labels.
- **Mode**: Light.
- **Follow-up**: Accept triggers the OS location-permission dialog (Precise/Approximate map graphic, While using the app / Only this time / Don't allow).

### 2.6 "My reminder" — Empty State (Home/List Screen)
- **Purpose**: Main/home screen when no reminders exist yet.
- **Layout (top→bottom)**: Top app bar — screen title "My reminder" (bold, left-aligned, largest text on screen) with two icons right-aligned: a red circular "AD" badge icon (likely an AdMob debug/test-ads indicator rather than real product UI — flag this to engineers, do not necessarily rebuild it) and a gear/settings icon → large center-flex area with a flat-illustration graphic (person sitting on a sofa with a laptop, a potted plant to the left, a wall clock upper-right, small "zzz"/motion lines) → bold headline "Welcome to Reminders" → gray subtext "Tap the **Add reminder** to add your first reminder" (the phrase "Add reminder" appears colored purple, like an inline link) → large pill CTA button "Add reminder".
- **Colors — Light mode**: background near-white/very pale lavender-gray; illustration in flat outline style using purple/indigo, white, and dark navy/charcoal line strokes; CTA purple pill, white bold text.
- **Colors — Dark mode**: background true near-black (~#0A0A0D); same illustration recolored (still purple laptop, white outline figure) sits fine against black; CTA remains the same purple pill; title text becomes white, subtext light gray.
- **Mode**: Both light and dark observed for this exact screen (see f_0028 light vs f_0059/0099 dark) — good reference for dark/light empty-state parity.

### 2.7 "My reminder" — List (Populated) Screen
- **Purpose**: Main list once reminders exist, with section grouping and quick-add.
- **Layout (top→bottom)**: Top bar "My reminder" + AD icon + search (magnifying glass) icon + settings gear icon (search icon appears once list has content; it wasn't present on the pure-empty state, which only had AD+gear) → collapsible section headers, e.g. **"Soon"** and **"Completed"**, each right-aligned with a small down-chevron (tap likely to collapse/expand) → reminder rows as rounded rectangular cards: a leading checkbox (outlined square, blue accent when unchecked-but-highlighted, filled/checked with checkmark + strikethrough text when completed) + title (bold) + subtitle line (due description, e.g. "Tomorrow · 01:30 AM", or "Completed Today 01:17 AM") → (when present) a bottom-pinned third-party **ad banner** (white bg, headline text + red "LEARN MORE" pill button) sitting above the very bottom → persistent **bottom quick-add bar**: a large rounded pill text field with rotating example placeholder text (e.g. "Drink Water at 7 PM") and a separate circular filled-purple "+" button to its right.
- **Quick-add expanded state**: tapping the field brings up the keyboard and a horizontally-arranged row of rounded pill "suggestion chips" directly above the keyboard, each with a small bell icon + label: "Later today", "This evening", "No time", and a live current-time chip ("Today 01:15 AM" style, updating as time passes) — tapping one presumably fast-fills the time for a natural-language quick add. The field itself becomes "Add reminder" and the trailing button becomes a purple circular checkmark (confirm/save), replacing the "+".
- **Colors (dark mode variant observed most)**: background near-black; cards a lighter dark charcoal gray (~#1B1B20) with visible separation from the pure-black page background; text white/light-gray; accent purple for checkboxes/buttons; ad banner sits in stark white/light card contrasting the dark theme (this is a 3rd-party SDK banner, not styled to match app theme — worth noting for engineers that ad units break dark-mode consistency in the reference app).
- **Corner radius**: Cards clearly "very rounded" — larger radius than typical Material default, closer to ~16dp, giving a soft pill-adjacent rectangle look consistent across all card surfaces app-wide (Settings rows, reminder cards, category rows, dialogs).

### 2.8 New Reminder / Edit Reminder Screen
- **Purpose**: Create or edit a single reminder; identical layout for both (Edit screen title says "Edit reminder" instead of "New reminder" and pre-fills fields).
- **Layout (top→bottom)**:
  1. Top bar: back arrow (left), title ("New reminder" / "Edit reminder"), and a checkmark (save/confirm) icon top-right.
  2. **Memo** input card: single-line/expandable text field placeholder "Memo", with two trailing small icon buttons inside the same card — a checklist/checkbox icon (possibly "convert to sub-tasks/list") and an image icon (attach photo).
  3. **Add time** row/card: label "Add time" + toggle switch (right-aligned). When OFF, card collapses to just the label+toggle. When ON, the card expands in-place (no navigation) to reveal: a sub-row with calendar icon + date/time text (e.g. "Tomorrow · 01:30 AM") + a red circular minus/remove icon to clear it; a divider; a vertical list of quick preset rows with plain text only, no icons ("1 hour from now", "Today 7:00 AM", "Today 3:00 PM", "Today 10:00 PM"); then two more rows each with a leading icon — repeat-arrows icon + "Don't repeat" (opens Repeat screen), and speaker icon + "Ring once" (opens Sound bottom sheet).
  4. **Add place** row/card: label "Add place" with a small circular orange/gold badge icon next to it (looks like a stylized sun/crown — the app's premium-lock indicator) + toggle (right-aligned). This entire card has a distinct **gold/amber 1–2px border outline** distinguishing it from the plain dark cards around it — a clear "this is a locked/premium feature" visual treatment. The toggle appears permanently OFF/disabled through the whole recording; user never successfully enables it.
  5. **Category** row: a plain card showing a small colored filled circle (category color) + category name (defaults to "My reminder"; changes to "Work" after user reassigns it). Tapping opens the Select Category screen.
  6. (Edit mode only, when Add time is OFF) an inline warning line beneath the category card: small bell-with-a-slash icon (muted gray) + text "You won't get notification." — a plain-text warning row, no card/border, just directly on the background.
- **Colors (dark mode, the mode this screen is always shown in during the walkthrough)**: page background near-black; each row is its own dark-gray rounded card with small gaps between cards (not one continuous list); "Memo" placeholder text light gray; toggle-on = purple fill w/ white knob; toggle-off = dark slate-gray fill w/ gray knob; premium card border = warm gold/amber (~#B08D3E-ish, muted gold, not bright yellow); premium badge icon = a small circular orange/gold sun-like glyph with a highlight, roughly 16–20dp.
- **Never observed in light mode** — worth having engineers double check whether this screen renders in light mode elsewhere; the recording only shows it in dark mode.

### 2.9 Date Picker (Bottom Sheet/Dialog)
- **Purpose**: Pick the exact date for "Add time".
- **Layout**: Modal card, top small label "- Select Date -", very large numeral display of the chosen date ("Sep 2, 2026") with a small pencil/edit icon beside it (tap to switch to manual keyboard entry, standard Material behavior), a month/year row with dropdown chevron and prev/next arrow icons, a 7-column calendar grid (S M T W T F S headers, then date cells) — selected date shown as a filled purple circle, "today" shown as a purple-outlined (unfilled) circle when different from selection — footer with "Cancel" and "OK" text buttons, purple text, right-aligned.
- **Colors**: Dark card matching Dark Mode theme (dark gray/charcoal), purple accent for selection, white/light-gray day numerals.
- **This is very close to stock Android/Material DatePicker styling** — likely intended to literally be the platform's Material date picker rather than a fully custom one.

### 2.10 Sound Options (Popover/Bottom Sheet)
- **Purpose**: Choose alert sound behavior.
- **Options** (exactly 2, radio-select, single-choice):
  1. "Ring once" — subtext "Notification and short sound" (default/selected).
  2. "Keep ringing" — subtext "Notification and long sound".
- **Layout**: Small rounded card anchored just below the "Ring once" trigger row (looks like an inline dropdown/popover rather than a full bottom sheet or full-screen dialog), dark themed, purple filled radio for selection.

### 2.11 Repeat Options Screen
- **Purpose**: Choose a recurrence rule.
- **Layout**: Full screen, back arrow + "Repeat" title top bar, then a single tall rounded card containing all options as rows with hairline dividers between them, right-aligned radio buttons.
- **Options in order**: Don't repeat (default/selected), Every minute, Every hour, Every day, Every week, Every month, Every year.
- Note for engineers: "Every minute" and "Every hour" are unusually granular repeat options for a consumer reminder app — worth confirming against the textual spec since these are easy to mis-scope or omit.

### 2.12 Select Category Screen
- **Purpose**: Pick which category/list a reminder belongs to.
- **Layout**: Full screen, back arrow + "Select category" title, single rounded card housing all rows with dividers. Each row: colored ring/outline circle avatar (empty center, colored stroke) on the left (filled + white checkmark when selected) + category name (bold) + right-aligned count badge (numeral, "0" for all in this recording, presumably the reminder count in that category) → final row (no count) with a purple "+" icon + "Add category" label, visually the same row style, always last.
- **Default categories observed**: "My reminders" (purple/indigo, pre-selected/checked), "Work" (blue), "Personal" (red/pink), "My Health" (green/lime), "Finance" (indigo/violet — a different shade than "My reminders" purple, close but distinguishable).
- **Mode**: Dark.

### 2.13 Add Category Screen
- **Purpose**: Create a new custom category.
- **Layout**: Full screen, back arrow + confirm checkmark top-right (no visible title text row beyond default header, or title may be blank/not clearly legible at this resolution), one rounded card containing: a text input "Enter here" (underlined style, not boxed) → label "Colour" → a grid of solid color circle swatches, 2 rows: row 1 (5 swatches) gold/yellow, red, orange, lime-green, teal; row 2 (visible 3, likely a 4th cut off) blue, magenta/purple, indigo (this last one shown selected, with a thicker outer ring/halo). So the full palette is 8–9 fixed swatch options, no custom color picker/hex input observed.
- **Mode**: Dark.

### 2.14 Search Screen
- **Purpose**: Search existing reminders.
- **Layout**: Back arrow + a full-width rounded pill search input at the top (placeholder "Search", auto-focused with keyboard up) → results list below using the exact same reminder-row card style as the main list (checkbox + title + subtitle).
- **Mode**: Dark.

### 2.15 Reminder Detail Screen
- **Purpose**: View a single reminder's full info and take actions.
- **Layout**: Back arrow only (no title text in the top bar — title likely scrolled/embedded in content below) → stacked cards: (1) Memo/title card — plain text when active, strikethrough text + "Completed: [date/time]" caption when the reminder has been marked complete; (2) "Time" card — a mini-header "Time" then rows with icons: calendar icon + "Tomorrow · 01:30 AM", repeat icon + "Don't repeat", speaker icon + "Ring once"; (3) category card — colored dot + category name (e.g. "Work"). Below the cards, a **bottom action bar** (not a card — sits directly on background, icons+labels evenly spaced across the width): "Complete" (double-checkmark icon), "Edit" (pencil icon), "Share" (share/nodes icon), "Delete" (trash icon). Once a reminder is completed, the "Complete" action disappears from this bar, leaving only Edit / Share / Delete.
- **Mode**: Dark.

### 2.16 Settings Screen
- **Purpose**: App-wide preferences and monetization entry point.
- **Layout (top→bottom)**: Back arrow + "Settings" title → full-width **"Full access" promo banner** (its own rounded card, distinct bright-green gradient, containing a crown emoji/icon, bold white heading "Full access", white subtext "Unlock full functionality.", and a white pill button "Go Pro" right-aligned) → section label "General" (small caps/gray) → "Language" row (globe icon, current value "English" shown in purple beneath the label) → "App Theme" row (half-filled circle/brightness icon, current value e.g. "Auto Mode"/"Dark Mode" in purple beneath label) → section label "Presets" → "Add time" row (clock icon, description text, toggle ON) → **"Add place" row (location-pin icon, small gold/crown badge next to the label, description text, toggle — this entire row/card has the gold outline premium treatment, toggle stays OFF)** → "Reminder after a call" row (phone icon, description text, toggle ON) → section label "Communicate" → "Feedback" row (speech-bubble icon) → "Privacy Policy" row (shield-with-@ icon) → "Privacy Settings" row (shield-with-! icon) → section label "Others" → "Share" row (share-nodes icon) → "Rate Us" row (star icon) → footer caption, centered, small gray text "App version 32.4".
- **Colors — Light mode**: page background pale lavender-white; each settings row is its own light-gray/white rounded card (subtle, barely different from bg) with black title text and purple value/subtitle text; toggle-on purple, toggle-off light gray.
- **Colors — Dark mode**: page background near-black; each row is a dark-charcoal rounded card; title text white, value/subtitle text purple (same purple family, stays vivid/legible on dark); toggle-on purple w/ white knob, toggle-off dark-slate w/ gray knob; the green promo banner and gold-outlined "Add place" card are the only two saturated-color accents on an otherwise monochrome+purple palette — a deliberate way of drawing the eye to monetization surfaces.
- **This is the one screen confirmed present in BOTH light and dark mode** in the recording (theme switched live via the App Theme dialog while sitting on this screen) — best reference for verifying full light/dark token parity.

### 2.17 App Theme Dialog
- **Purpose**: Theme picker.
- **Layout**: Modal centered card over dimmed backdrop, small bold title "App Theme", 3 radio rows (Auto Mode, Light Mode, Dark Mode) stacked with generous spacing (not hairline-divided like other lists — more breathing room, looks like a simple dialog rather than a settings list), full-width pill "Save" button at the bottom of the card.
- Card itself stays light/white-ish in both app themes in the frames observed (i.e., this dialog's own chrome did not visibly flip to a dark card even when Dark Mode was being selected — worth double-checking live in-app since this could just be transition timing in the captured frames, not a confirmed design decision).

### 2.18 Paywall / "Go Pro" Screen
- **Purpose**: Subscription upsell, reached from the Settings "Go Pro" button (and implied also reachable by tapping the locked "Add place" feature, though that exact tap wasn't directly captured — only its downstream screen was).
- **Layout**: Close "X" icon top-right → bold two-line headline "Get Unlimited access to premium features." → short purple underline/accent bar beneath the headline (decorative divider) → 3 benefit rows, each a checkmark icon + bold-lead phrase + short benefit text ("Unlimited Add Places – Break Barriers!", "Remove every single ad – Zero Interruptions!", "365 Days, 24/7 Assistance – Here for You!") → large empty flexible space (likely where a pricing/plan-selector card would normally sit — not shown/visible in these frames, so the plan-selection UI is NOT captured in this recording; flag this gap to engineers) → sticky bottom area: full-width bright neon-green pill button "Continue" → centered small gray subtext "Subscription renews automatically unless cancelled" → a small shield-check icon + "Cancel anytime. Secure with Play Store" line → footer row of 3 small text links "Terms & C..." | "Privacy Pol..." | "Restore".
- **Colors**: Background a very dark teal/near-black gradient (distinctly cooler/greener-black than the pure neutral near-black used elsewhere in dark mode — this screen has its own bespoke palette, not just "dark mode theme applied"). Checkmarks white. CTA button a saturated bright/neon green (~#22FF7A-ish), contrasting sharply with the app's usual purple accent — deliberate "money screen" color break from the rest of the app.
- **Mode**: This screen only has one look (doesn't adapt to light/dark toggle) — it's a fixed dark paywall regardless of the app theme setting.

---

## 3. Permission / System Dialogs Observed (in order)

| # | Frame(s) | Dialog | Type | Options shown |
|---|----------|--------|------|----------------|
| 1 | f_0003 | "Allow #Reminder to send you notifications?" | OS runtime permission (POST_NOTIFICATIONS) | Allow / Don't allow |
| 2 | f_0004 | "Notification Required — Required to send reminder alerts so you never miss important tasks." | Custom in-app dialog (soft pre/re-prompt) | OK |
| 3 | f_0005 | "Allow #Reminder to send you notifications?" (re-shown) | OS runtime permission | Allow / Don't allow |
| 4 | f_0006 | "Allow #Reminder to make and manage phone calls?" | OS runtime permission (phone/call state) | Allow / Don't allow |
| 5 | f_0007 | "One last step! ... Grant permission to view and set reminders immediately after ending a call." | Custom in-app explainer screen (not a dialog, full screen) leading into... | GO TO SETTINGS |
| 6 | f_0010–0022 | System "Display over other apps" manager | OS Settings screen (deep link, `ACTION_MANAGE_OVERLAY_PERMISSION`-style) | Per-app Allowed/Denied list + per-app toggle |
| 7 | f_0026 | "Allow #Reminder to access this device's location?" | OS runtime permission (location, with Precise/Approximate) | Precise / Approximate + While using the app / Only this time / Don't allow |

Also note: a custom **privacy/consent screen** (§2.5, "Your comfort comes first!") functions like a dialog gate in the flow (Accept/Decline) but renders as a full page, not a system dialog — likely a Google UMP (User Messaging Platform) consent form or an equivalent custom-built GDPR/CCPA gate tied to the ad SDK, given the explicit mention of "third party partners," "tailored ads," and "Advertising and Location-based analytics."

No dialogs for Storage, Camera, Contacts, Microphone, Calendar, or Exact Alarm permission were observed in this recording — if the textual spec mentions any of those, flag for reconciliation since this walkthrough only exercises Notifications, Phone/Call, Display-over-other-apps (overlay), and Location.

---

## 4. Switches / Toggles Inventory

| Toggle | Location | Default state | Color when ON | Color when OFF |
|---|---|---|---|---|
| Notification permission (OS) | System dialog | N/A (button, not toggle) | — | — |
| Display over other apps (OS, per-app) | System Settings | OFF | Dark blue/black filled (OS style) | Light gray |
| Display over other apps (app's own mirrored toggle on "One last step" screen) | Onboarding | OFF → user turns ON | Purple, white knob | Gray |
| Location Permission | Privacy Settings screen | ON | Purple, white knob | (not seen off) |
| Add time | Settings → Presets | ON | Purple, white knob | Dark slate, gray knob |
| **Add place** | Settings → Presets, and New/Edit Reminder screen | **OFF (locked — premium; never toggled ON in the entire recording)** | N/A — not achieved | Dark slate, gray knob, inside a gold/amber-bordered card |
| Reminder after a call | Settings → Presets | ON (briefly flickers OFF once, likely an accidental/incidental tap, then back ON) | Purple, white knob | Dark slate, gray knob |
| Add time (New/Edit reminder screen) | New/Edit Reminder | OFF by default when creating a new reminder | Purple, white knob | Dark slate, gray knob |

Overall toggle visual language: ON = solid purple track + white circular knob (positioned right); OFF = dark gray/slate track + mid-gray knob (positioned left). This is consistent across both OS-adjacent screens (styled slightly differently, more muted/system-gray) and the app's own screens (more saturated purple).

---

## 5. Bottom Sheets / Dialogs — Full Option Lists

**App Theme dialog** — Auto Mode, Light Mode, Dark Mode (single-select radio + Save button).

**Sound options popover** — Ring once (Notification and short sound), Keep ringing (Notification and long sound). Only 2 options; single-select radio.

**Repeat options screen** (full screen, not technically a sheet, but functions as one) — Don't repeat, Every minute, Every hour, Every day, Every week, Every month, Every year. Single-select radio, 7 options total.

**Date picker** — Standard Material-style calendar dialog: month navigation, day grid, Cancel/OK. (Not an enumerable "options list" — a full calendar widget.)

**Select Category screen** — My reminders, Work, Personal, My Health, Finance, + Add category action row. (5 fixed/seed categories + ability to add unlimited custom ones.)

**Add Category color swatches** — 9 fixed colors offered (gold/yellow, red, orange, lime-green, teal, blue, magenta/purple, indigo, [possible 9th cut off-screen]); no custom hex/RGB picker seen.

**Android system Share sheet** — standard OS share sheet (Nearby Share row + app icon grid: WhatsApp, Telegram, My Files/Save to My Files, Truecaller, Instagram, Notes, Chrome, Files by Google, plus more via paging dots). Not custom app UI — just confirms Share triggers the standard `Intent.ACTION_SEND` chooser.

**Quick-add time-suggestion chips** (on main list, above keyboard) — Later today, This evening, No time, [live current time] (e.g. "Today 01:15 AM"). Presented as a horizontally scrollable chip row, not a modal sheet.

---

## 6. Premium / Lock Indicators

- **"Add place" (location-triggered reminders) is the one and only feature gated behind premium** in this entire recording. It is visually marked in exactly the same way everywhere it appears (Settings screen row, New Reminder screen row, Edit Reminder screen row):
  - A small circular badge icon next to the feature's label — looks like a stylized **orange/gold sun burst or crown-like glyph** (~16–20dp), sometimes rendered with what reads as a subtle "!" or sparkle inside it at this resolution — best described as "a warm gold/amber circular premium badge," not a crisp literal crown in every frame (it may literally be a crown emoji that just renders small/blurry).
  - The entire row/card containing the feature gets a **thin gold/amber border outline** (~1–2dp), clearly differentiating it from the plain dark/light cards around it — this is the primary "this is locked" signal, more so than the badge icon itself.
  - The toggle for this feature is always shown OFF and never observed to successfully switch ON, even after multiple tap attempts across different screens — consistent with it being non-functional/blocked pending purchase.
- **"Go Pro" banner** on the Settings screen (green gradient card, crown emoji, "Full access / Unlock full functionality", "Go Pro" button) is the main upsell entry point, distinct from the per-feature gold-outline treatment — this is a general/app-wide premium CTA, not tied to one feature.
- **Paywall screen benefits** explicitly list exactly 3 premium perks: Unlimited Add Places, Remove every single ad, 365-day/24-7 "assistance" (likely priority support or an always-on guarantee messaging point) — confirms "Add place" and "ad removal" are the two concrete gated capabilities; the third is a service/support claim, not a UI feature to build.
- **Ads**: a native ad banner unit appears at the bottom of at least the Completed list view — visually confirms ads are shown to free users and that removing ads is a paid perk (matches paywall copy). The recording also shows a red circular "AD" icon in the main list's top bar at all times — this is very likely a debug/test-ads SDK indicator (common with AdMob test builds) rather than intentional production UI; recommend engineers confirm with the textual spec / ignore it as a debug artifact rather than rebuild it literally.
- No other lock icons, gold borders, or "premium"/"pro" text badges were observed anywhere else (Repeat options, Sound options, Category creation, Search, etc. are all fully free/unlocked in this recording).

---

## 7. Empty States

- **"My reminder" home/list screen, no reminders**: illustration (person on a couch with a laptop, a potted plant, a wall clout clock on the wall, small motion/emphasis lines) + bold headline "Welcome to Reminders" + gray instructional subtext with an inline purple "Add reminder" phrase + a large pill CTA button "Add reminder". Confirmed rendered correctly in **both light mode** (f_0028) and **dark mode** (f_0059, f_0099–0100) with the same illustration (recolored line art) and layout — good parity reference.
- No other empty states (e.g., empty Search results, empty Category list, empty Completed section) were captured in this recording — if the textual spec defines those, they cannot be cross-checked against this video and should be trusted from the textual spec alone.

---

## 8. Color Palette Summary

### Light Mode
- **Background**: very pale lavender-white / off-white, roughly `#F0F1F5`–`#F5F6FA` (Welcome, Language, Privacy consent, empty-state, and early Settings screens).
- **Card surfaces**: white or near-white, subtly distinguishable from the page background (e.g. Language list card).
- **Primary text**: near-black / very dark charcoal.
- **Secondary/description text**: medium gray.
- **Primary accent (buttons, selected states, links)**: vivid purple/indigo, roughly `#5B3AD1`–`#6A3DE8` (looks like it may carry a subtle vertical gradient on large pill buttons, slightly darker at the bottom edge for a soft 3D/pressed look).
- **Value/subtitle text in Settings rows**: same purple accent, used for things like "English", "Auto Mode".

### Dark Mode
- **Background**: true near-black / very dark charcoal, roughly `#0A0A0D`–`#0D0D12`.
- **Card surfaces**: dark charcoal-gray, a shade or two lighter than the page background for separation, roughly `#1B1B20`–`#1F1F26`.
- **Primary text**: white / near-white.
- **Secondary/description text**: mid-gray.
- **Primary accent**: same purple family as light mode, `#5B3AD1`–`#6A3DE8`, stays consistent across themes (not re-tinted for dark mode) — used for toggles-on, radios, buttons, category default color, value text.
- **Category swatch colors** (fixed palette, same in dark mode UI): gold/yellow, red, orange, lime-green, teal, blue, magenta/purple, indigo/violet.

### Accent / Special-Purpose Colors (mode-independent)
- **Premium/lock gold**: warm muted gold/amber border + badge icon, roughly `#B08D3E`–`#D9A94A` (border looks more muted/desaturated than the badge icon itself).
- **Go Pro banner green**: bright/saturated green gradient card, roughly `#12C97A`–`#0F9D5B`, with a crown emoji and white text — visually distinct from any other green in the app.
- **Paywall CTA green**: separate, more neon/electric green than the Go Pro banner, roughly `#22FF7A`–`#2BE87A` — the paywall screen overall uses a dark teal-black background rather than the app's usual pure neutral black, giting it a slightly different "money screen" identity from the rest of dark mode.
- **Destructive/remove**: red circular minus icon for clearing a set time; standard red also appears in OS/ad contexts (ad "LEARN MORE" button, permission list "Denied" is actually rendered in the same blue-purple link color, not red — worth noting Denied/Allowed in the OS list are BOTH the same blue-ish link color, differentiated only by text, not color).

---

## 9. Icon Inventory (purpose-labeled)

- Bell (outline) — reminders/notifications, category icon for "Add time" bell chips, warning "no notification" bell-with-slash.
- Phone handset with small arrow/redirect glyph — "After call reminders" feature.
- Back arrow (←) — universal back navigation, top-left, every sub-screen.
- Checkmark (circular, filled purple) — confirm/save action, top-right on Language, New/Edit Reminder, Add Category screens.
- Gear/cog — Settings entry point.
- Magnifying glass — Search entry point (only appears once list has content).
- Red circular "AD" badge (with an occasional diagonal slash overlay) — likely ad-SDK debug indicator, not to be treated as confirmed production UI.
- Globe — Language row.
- Half-filled circle (brightness-style) — App Theme row.
- Clock (outline) — "Add time" feature/row.
- Location pin (outline) — "Add place" feature/row (the locked one).
- Speech bubble — Feedback row.
- Shield with "@" — Privacy Policy row.
- Shield with "!" — Privacy Settings row.
- Share nodes (3 dots connected by lines) — Share row / Share action.
- Star (outline) — Rate Us row.
- Checklist/checkbox icon and image/picture icon — trailing icons inside the Memo input field (sub-task conversion and photo attachment, inferred from icon shape).
- Calendar icon — date row inside expanded Add Time panel, and Date Picker trigger.
- Circular minus/remove (red) — clear the currently set date/time.
- Repeat/loop arrows — repeat row trigger and repeat-summary display.
- Speaker/volume — sound row trigger and sound-summary display.
- Colored solid dot — category color indicator (list rows, reminder cards, detail screen).
- Colored ring/outline circle (empty center) — category avatar in the Select Category list (fills solid + gets a white checkmark when selected).
- Double-checkmark — "Complete" action on reminder detail.
- Pencil — "Edit" action.
- Trash/bin — "Delete" action.
- Plus (+) — floating add button on the main list (quick add), and "Add category" row action.
- Crown/gold sunburst badge (small, ~16-20dp) — premium/lock indicator specifically on "Add place".
- Shield-check — "secure with Play Store" line on the paywall.
- Close "X" — dismiss paywall screen.

---

## 10. Transitions (best-effort, inferred from adjacent-frame deltas)

Frame sampling every 2.5s is too coarse to see actual animation curves, but structural cues suggest:
- **Full-screen navigations** (New Reminder → Repeat, → Select Category, → Add Category; Settings → Language; Settings → Privacy Settings) all use a **top app bar with a back arrow**, consistent with a standard Android push/slide-in-from-right (or fragment replace) navigation pattern — no evidence of full-screen fades or bottom-to-top reveals for these.
- **Dialogs** (permission dialogs, App Theme dialog) render as **centered modal cards with a dimmed/scrimmed backdrop** showing the previous screen still faintly visible behind — consistent with standard Android `AlertDialog`/`Dialog` fade+scale-in behavior, not full-screen transitions.
- **Popovers** (Sound options) appear to render as a **small anchored card directly below/near their trigger row** rather than as a centered dialog or bottom sheet — closer to a dropdown/menu pattern.
- **Paywall screen** appears to slide up or replace the Settings screen entirely (full screen, with its own Close "X" rather than a back arrow) — behaves like a modal full-screen route rather than a stack push.
- The "Add time" and "Add place" expand/collapse **in place within the New/Edit Reminder screen** (accordion-style inline expansion) rather than navigating to a new screen — confirmed by the surrounding Memo/category cards staying fixed in position while only the Add Time card grows/shrinks.

---

## 11. Gaps, Uncertainties, and Flags for Engineers

1. **Frames are low-resolution (~380px wide)** — all colors above are best-effort visual approximations, not measured/sampled hex values. Please verify exact brand colors against any design source (Figma, style guide) if one exists; do not treat the hex guesses in §8 as authoritative.
2. **"Add place" premium toggle was never seen turned ON** — its enabled/active visual state (once purchased) is not present anywhere in this recording. The gold-outline/locked state is the only state captured.
3. **Paywall pricing/plan-selection UI was not captured** — the screen jumps from the 3-benefit checklist straight to the sticky CTA footer in the frames sampled; there is likely a plan-selector (e.g. monthly/yearly toggle or cards) in the blank space between that this recording does not show. Textual spec should be the source of truth for that section.
4. **App Theme dialog's own card color** did not clearly flip to a dark surface in the frames captured even while Dark Mode was being selected — may just be a capture-timing artifact (dialog appears before the theme fully reapplies), not a confirmed light-styled-dialog-in-dark-mode design decision. Worth a quick live check rather than trusting this literally.
5. **The red "AD" icon with an occasional slash overlay** in the main screen's top bar is most likely a Google Mobile Ads SDK debug/test-mode indicator (common when `isTestDevice`/test ad units are configured) rather than an intentional piece of product UI. Recommend NOT rebuilding this literally — flag to confirm against the textual spec.
6. **New/Edit Reminder, Repeat, Category, Search, and Reminder Detail screens were only ever observed in Dark Mode** in this recording — light-mode versions of these specific screens were not captured, so light-mode styling for them must come from the textual spec, not this video.
7. **No exact-alarm, storage/photos, contacts, calendar, or microphone permission prompts appear** in this recording. If the textual spec calls for reminders with photo attachments (an image icon does appear in the Memo field, implying a photo-attach capability exists) a storage/photos permission flow presumably exists elsewhere in the app but was not exercised here.
8. **Memo text entered during the demo ("Tant"/partial word) is placeholder/demo content**, not meaningful copy — do not treat it as a real string requirement.
9. **Category counts all read "0"** in the Select Category screen because no reminders had been assigned to those categories yet at that point in the walkthrough — this is just an artifact of demo state, not evidence the counter is broken.
10. **A "Reminder after a call" toggle briefly appears OFF in one single frame (f_0042)** flanked by ON in the frames immediately before and after — almost certainly an incidental/accidental tap during recording, not an intentional state change; treat its default as ON.

---

## 12. Total Distinct Screens/States Identified

Counting each unique screen or dialog/sheet state once (not counting OS-native chrome like the Play Store, Gmail, Chrome, or the notification shade, which are incidental to the recording, not part of the app):

1. Onboarding/Welcome
2. Notification permission dialog (OS)
3. "Notification Required" custom dialog
4. Phone/Call permission dialog (OS)
5. "One last step!" overlay-permission explainer
6. System "Display over other apps" manager (OS, referenced only)
7. Language selection
8. Privacy/consent screen ("Your comfort comes first!")
9. Location permission dialog (OS)
10. "My reminder" empty state (light)
11. "My reminder" empty state (dark)
12. Settings screen (light)
13. Settings screen (dark)
14. App Theme dialog
15. Paywall / Go Pro screen
16. Privacy Settings screen
17. Android system Share sheet (OS, referenced only)
18. New Reminder screen
19. Date picker dialog
20. Sound options popover
21. Repeat options screen
22. Select Category screen
23. Add Category screen
24. "My reminder" populated list (Soon section)
25. Quick-add expanded (time-suggestion chips) state
26. Search screen
27. Reminder Detail screen (active)
28. Reminder Detail screen (completed)
29. "My reminder" populated list (Completed section)
30. Edit Reminder screen
31. Edit Reminder — "no notification" warning state

**Total: 31 distinct app screens/states/dialogs** identified across the 100 frames (23 of these are the app's own custom UI excluding pure OS chrome that was only passed through, e.g. the overlay-permission manager and the share sheet, which are standard Android surfaces and not something to rebuild pixel-for-pixel).
