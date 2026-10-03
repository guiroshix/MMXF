# MMXF

**Mega MEn X Framework** - minimal Java ME / J2ME starter project.

This repository is intentionally tiny. It demonstrates the complete pipeline:

```text
Java source
    |
    v
javac 8
    |
    v
CLDC 1.1 / MIDP 2.0 bytecode
    |
    v
ProGuard -microedition
    |
    v
CLDC-preverified JAR
    |
    v
JAD
```

## Target

- CLDC 1.1
- MIDP 2.0
- Java ME / J2ME
- No external game engine
- No Gradle
- No Maven project required

The sample MIDlet creates a `Canvas`, runs a basic game loop and displays its FPS.

## GitHub Actions

Every push, pull request, or manual workflow run builds:

```text
MMXF.jar
MMXF.jad
```

The files are uploaded as a GitHub Actions artifact.

## Toolchain

The build uses:

- JDK 8
- `org.microemu:cldcapi11:2.0.4`
- `org.microemu:midpapi20:2.0.4`
- ProGuard 7.8.2 with its `-microedition` preverification mode

The API JARs are compile-time stubs. They are not bundled into the game JAR.

## Local build

On Linux:

```bash
sudo apt install openjdk-8-jdk curl unzip
```

Then:

```bash
./tools/setup.sh
```

Compile:

```bash
rm -rf build
mkdir -p build/classes

find src -name '*.java' > build/sources.txt

javac   -source 1.3   -target 1.1   -encoding UTF-8   -bootclasspath "lib/cldcapi11.jar:lib/midpapi20.jar"   -classpath "lib/cldcapi11.jar:lib/midpapi20.jar"   -d build/classes   @build/sources.txt
```

Preverify:

```bash
tools/toolchain/proguard-7.8.2/bin/proguard.sh @tools/proguard.pro
```

Package:

```bash
mkdir -p build/jar/META-INF
cp resources/META-INF/MANIFEST.MF build/jar/META-INF/MANIFEST.MF

cd build/jar
jar xf ../preverified.jar

jar cfm ../MMXF.jar META-INF/MANIFEST.MF .
cd ../..
```

Generate the JAD:

```bash
SIZE=$(stat -c '%s' build/MMXF.jar)
sed "s/@JAR_SIZE@/${SIZE}/" MMXF.jad > build/MMXF.jad
```

The resulting files are:

```text
build/MMXF.jar
build/MMXF.jad
```

## Source

The main entry point is:

```text
src/br/guiroshix/mmxf/Main.java
```

This is deliberately just the foundation. Game systems can be added later without changing the CI architecture.
