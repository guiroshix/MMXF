# MMXF minimal Java ME build.
#
# ProGuard's -microedition option performs CLDC preverification.

-injars build/classes
-outjars build/preverified.jar

-libraryjars lib/cldcapi11.jar
-libraryjars lib/midpapi20.jar

-dontshrink
-dontoptimize
-dontobfuscate
-dontwarn
-ignorewarnings

-microedition

-keep public class * extends javax.microedition.midlet.MIDlet {
    public <init>();
    public void startApp();
    public void pauseApp();
    public void destroyApp(boolean);
}
