# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
# Preserve all annotations.

#-keepattributes *Annotation*
#
## Preserve all .class method names.
#
#-keepclassmembernames class * {
#    java.lang.Class class$(java.lang.String);
#    java.lang.Class class$(java.lang.String, boolean);
#}
#
## Preserve all native method names and the names of their classes.
#
#-keepclasseswithmembernames,includedescriptorclasses class * {
#    native <methods>;
#}
#
## Preserve the special static methods that are required in all enumeration
## classes.
#
#-keepclassmembers class * extends java.lang.Enum {
#    public static **[] values();
#    public static ** valueOf(java.lang.String);
#}
#
## Explicitly preserve all serialization members. The Serializable interface
## is only a marker interface, so it wouldn't save them.
## You can comment this out if your library doesn't use serialization.
## If your code contains serializable classes that have to be backward
## compatible, please refer to the manual.
#
#-keepclassmembers class * implements java.io.Serializable {
#    static final long serialVersionUID;
#    static final java.io.ObjectStreamField[] serialPersistentFields;
#    private void writeObject(java.io.ObjectOutputStream);
#    private void readObject(java.io.ObjectInputStream);
#    java.lang.Object writeReplace();
#    java.lang.Object readResolve();
#}
#
## Preserve all View implementations and their special context constructors.
#
#-keep public class * extends android.view.View {
#    public <init>(android.content.Context);
#    public <init>(android.content.Context, android.util.AttributeSet);
#    public <init>(android.content.Context, android.util.AttributeSet, int);
#    public void set*(...);
#}
#
## Keep setters in Views so that animations can still work.
## See http://proguard.sourceforge.net/manual/examples.html#beans
## From tools/proguard/proguard-android.txt.
#-keepclassmembers public class * extends android.view.View {
#   void set*(***);
#   *** get*();
#}
#
## Preserve all classes that have special context constructors, and the
## constructors themselves.
#
#-keepclasseswithmembers class * {
#    public <init>(android.content.Context, android.util.AttributeSet);
#}
#
## Preserve the special fields of all Parcelable implementations.
#
#-keepclassmembers class * implements android.os.Parcelable {
#    static android.os.Parcelable$Creator CREATOR;
#}
#
## Preserve static fields of inner classes of R classes that might be accessed
## through introspection.
#
#-keepclassmembers class **.R$* {
#  public static <fields>;
#}
#
## GeckoView specific rules.
#
## Keep everthing in org.mozilla.geckoview
#-keep class org.mozilla.geckoview.** { *; }
#
#-keep class org.mozilla.gecko.mozglue.JNIObject {
#    *;
#}
#
#-keep class * extends org.mozilla.gecko.mozglue.JNIObject {
#    *;
#}
#
## Keep the annotation.
#-keep @interface org.mozilla.gecko.annotation.JNITarget
#
## Keep classes tagged with the annotation.
#-keep @org.mozilla.gecko.annotation.JNITarget class *
#
## Keep all members of an annotated class.
#-keepclassmembers @org.mozilla.gecko.annotation.JNITarget class * {
#    *;
#}
#
## Keep annotated members of any class.
#-keepclassmembers class * {
#    @org.mozilla.gecko.annotation.JNITarget *;
#}
#
## Keep classes which contain at least one annotated element. Split over two directives
## because, according to the developer of ProGuard, "the option -keepclasseswithmembers
## doesn't combine well with the '*' wildcard" (And, indeed, using it causes things to
## be deleted that we want to keep.)
#-keepclasseswithmembers class * {
#    @org.mozilla.gecko.annotation.JNITarget <methods>;
#}
#-keepclasseswithmembers class * {
#    @org.mozilla.gecko.annotation.JNITarget <fields>;
#}
#
## Keep WebRTC targets.
#-keep @interface org.mozilla.gecko.annotation.WebRTCJNITarget
#-keep @org.mozilla.gecko.annotation.WebRTCJNITarget class *
#-keepclassmembers class * {
#    @org.mozilla.gecko.annotation.WebRTCJNITarget *;
#}
#-keepclassmembers @org.mozilla.gecko.annotation.WebRTCJNITarget class * {
#    *;
#}
#-keepclasseswithmembers class * {
#    @org.mozilla.gecko.annotation.WebRTCJNITarget <methods>;
#}
#-keepclasseswithmembers class * {
#    @org.mozilla.gecko.annotation.WebRTCJNITarget <fields>;
#}
#
## Keep generator-targeted entry points.
#-keep @interface org.mozilla.gecko.annotation.WrapForJNI
#-keep @org.mozilla.gecko.annotation.WrapForJNI class *
#-keepclassmembers,includedescriptorclasses class * {
#    @org.mozilla.gecko.annotation.WrapForJNI *;
#}
#-keepclasseswithmembers,includedescriptorclasses class * {
#    @org.mozilla.gecko.annotation.WrapForJNI <methods>;
#}
#-keepclasseswithmembers,includedescriptorclasses class * {
#    @org.mozilla.gecko.annotation.WrapForJNI <fields>;
#}
#
## Keep all members of an annotated class.
#-keepclassmembers,includedescriptorclasses @org.mozilla.gecko.annotation.WrapForJNI class * {
#    *;
#}
#
## Keep Reflection targets.
#-keep @interface org.mozilla.gecko.annotation.ReflectionTarget
#-keep @org.mozilla.gecko.annotation.ReflectionTarget class *
#-keepclassmembers class * {
#    @org.mozilla.gecko.annotation.ReflectionTarget *;
#}
#-keepclassmembers @org.mozilla.gecko.annotation.ReflectionTarget class * {
#    *;
#}
#-keepclasseswithmembers class * {
#    @org.mozilla.gecko.annotation.ReflectionTarget <methods>;
#}
#-keepclasseswithmembers class * {
#    @org.mozilla.gecko.annotation.ReflectionTarget <fields>;
#}
#
## Avoid "Warning: org.yaml.snakeyaml.scanner.ScannerImpl: can't find
## referenced method 'java.nio.ByteBuffer flip()' in library class
## java.nio.ByteBuffer".
## Between Java 1.8 and 1.9, the signature of `flip()` changed, which
## trips up proguard.
#
#-dontwarn org.yaml.snakeyaml.scanner.ScannerImpl

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile