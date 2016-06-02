# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/user/Documents/Moving_data/Software/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#Fix warning Error:warning: Ignoring InnerClasses attribute for an anonymous inner class
#-keepattributes Signature, Exceptions, *Annotation*, EnclosingMethod
#-keepattributes InnerClasses

#-keep class com.backendless.** {*;}
#-dontwarn com.backendless.**
#
#-keep class weborb.** {*;}
#-dontwarn weborb.**
#
#-keep class com.google.common.** {*;}
#-dontwarn com.google.common.**
#
#-keep class org.jivesoftware.smackx.disco.ServiceDiscoveryManager.** {*;}
#-dontwarn org.jivesoftware.smackx.disco.ServiceDiscoveryManager.**
#
#-keep class com.squareup.picasso.** {*;}
#-dontwarn com.squareup.picasso.**
#
#-keep class com.yalantis.ucrop.task.** {*;}
#-dontwarn com.yalantis.ucrop.task.**
#
#
#
#-keep class org.simpleframework.xml.stream.** {*;}
#-dontwarn org.simpleframework.xml.stream.**
#
#-keep class rx.internal.util.unsafe.** {*;}
#-dontwarn rx.internal.util.unsafe.**
#
#-keep class butterknife.** { *; }
#-dontwarn butterknife.internal.**
#-keep class **$$ViewBinder { *; }
#
#-keep class org.eclipse.paho.client.mqttv3.** {*;}
#-dontwarn org.eclipse.paho.client.mqttv3.**
#
#-keep class org.eclipse.paho.client.mqttv3.** {*;}
#-dontwarn org.eclipse.paho.client.mqttv3.**

