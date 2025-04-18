# Suppress generated warnings
-dontwarn android.media.LoudnessCodecController$OnLoudnessCodecUpdateListener
-dontwarn android.media.LoudnessCodecController

# === JavaMail required classes ===
-keep class javax.mail.** { *; }
-keep class javax.mail.internet.** { *; }
-keep class javax.activation.** { *; }
-keep class com.sun.mail.** { *; }

-dontwarn javax.mail.**
-dontwarn javax.activation.**
-dontwarn com.sun.mail.**

# Keep locale and configuration code
-keep class **.R$string { *; }