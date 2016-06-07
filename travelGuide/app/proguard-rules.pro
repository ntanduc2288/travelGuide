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

-keep class com.backendless.** {*;}
-dontwarn com.backendless.**

-keep class weborb.** {*;}
-dontwarn weborb.**

-keep class com.google.common.** {*;}
-dontwarn com.google.common.**


-keep class com.squareup.picasso.** {*;}
-dontwarn com.squareup.picasso.**

-keep class com.yalantis.ucrop.task.** {*;}
-dontwarn com.yalantis.ucrop.task.**

#-keep class org.jivesoftware.smackx.disco.ServiceDiscoveryManager.** {*;}
#-dontwarn org.jivesoftware.smackx.disco.ServiceDiscoveryManager.**

-keep class org.simpleframework.xml.stream.** {*;}
-dontwarn org.simpleframework.xml.stream.**

-keep class rx.internal.util.unsafe.** {*;}
-dontwarn rx.internal.util.unsafe.**

-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keep class org.eclipse.paho.client.mqttv3.** {*;}
-dontwarn org.eclipse.paho.client.mqttv3.**

-keep class org.eclipse.paho.client.mqttv3.** {*;}
-dontwarn org.eclipse.paho.client.mqttv3.**


##---------------Begin: proguard configuration common for all Android apps ----------
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-dump class_files.txt
-printseeds seeds.txt
-printusage unused.txt
-printmapping mapping.txt
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

# Remove Logging statements
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** e(...);
    public static *** i(...);
}

-allowaccessmodification
-keepattributes *Annotation*
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
-repackageclasses ''

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-dontnote com.android.vending.licensing.ILicensingService

# Explicitly preserve all serialization members. The Serializable interface
# is only a marker interface, so it wouldn't save them.
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Preserve all native method names and the names of their classes.
-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# Preserve static fields of inner classes of R classes that might be accessed
# through introspection.
-keepclassmembers class **.R$* {
  public static <fields>;
}

# Preserve the special static methods that are required in all enumeration classes.
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep public class * {
    public protected *;
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
##---------------End: proguard configuration common for all Android apps ----------

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.antew.redditinpictures.library.imgur.** { *; }
-keep class com.antew.redditinpictures.library.reddit.** { *; }

##---------------End: proguard configuration for Gson  ----------

#QuickBlox
#-keep class org.jivesoftware.smack.** { *; }
#-keep class com.quickblox.** { *; }
#-keep class * extends org.jivesoftware.smack { public *; }
#-keep class org.jivesoftware.smack.** { public *; }
#-keep class org.jivesoftware.smackx.** { public *; }
#-keep class com.quickblox.** { public *; }
#-keep class * extends org.jivesoftware.smack { public *; }
#-keep class * implements org.jivesoftware.smack.debugger.SmackDebugger { public *; }

-keep class org.jivesoftware.smack.initializer.VmArgInitializer { public *; }
-keep class org.jivesoftware.smack.ReconnectionManager { public *; }
-keep class com.quickblox.module.c.a.c { public *; }
-keep class com.quickblox.module.chat.QBChatService { public *; }
-keep class com.quickblox.module.chat.QBChatService.loginWithUser { public *; }
-keep class com.quickblox.module.chat.listeners.SessionCallback { public *; }
-keep class * extends org.jivesoftware.smack { public *; }
-keep class org.jivesoftware.smack.** { public *; }
-keep class org.jivesoftware.smackx.** { public *; }
-keep class com.quickblox.** { public *; }
-keep class * extends org.jivesoftware.smack { public *; }
-keep class * implements org.jivesoftware.smack.debugger.SmackDebugger { public *; }



