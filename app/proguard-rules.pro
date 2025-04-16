# Please add these rules to your existing keep rules in order to suppress warnings.
# This is generated automatically by the Android Gradle plugin.
-dontwarn android.media.LoudnessCodecController$OnLoudnessCodecUpdateListener
-dontwarn android.media.LoudnessCodecController

# Only what is necessary
-keep class javax.mail.Transport { *; }
-keep class javax.mail.internet.MimeMessage { *; }
-keep class javax.mail.internet.InternetAddress { *; }
-keep class javax.mail.Session { *; }
-keep class javax.mail.PasswordAuthentication { *; }
-keep class javax.mail.Message { *; }
-keep class javax.mail.Authenticator { *; }

-keep class javax.activation.DataHandler { *; }
-keep class javax.activation.DataSource { *; }
-dontwarn javax.activation.**