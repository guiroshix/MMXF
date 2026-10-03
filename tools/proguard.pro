# MMXF minimal Java ME build.
# ProGuard's -microedition option performs CLDC preverification.

-injars ../build/classes
-outjars ../build/preverified.jar

-libraryjars ../lib/cldcapi11.jar
-libraryjars ../lib/midpapi20.jar

-dontoptimize
-dontobfuscate
-dontwarn
-ignorewarnings

-microedition

-keep public class br.guiroshix.mmxf.Main {
    public <init>();
    public void startApp();
    public void pauseApp();
    public void destroyApp(boolean);
}

-keep public class br.guiroshix.mmxf.Main$GameCanvas {
    public <init>();
    public *;
}