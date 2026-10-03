#!/usr/bin/env bash
set -euo pipefail

ROOT="$(CDPATH= cd -- "$(dirname -- "$0")/.." && pwd)"
TOOLCHAIN="$ROOT/tools/toolchain"
LIB="$ROOT/lib"

CLDC_VERSION="2.0.4"
MIDP_VERSION="2.0.4"
PROGUARD_VERSION="7.8.2"

mkdir -p "$TOOLCHAIN" "$LIB"

download() {
    url="$1"
    out="$2"

    if [ -f "$out" ]; then
        echo "Already exists: $out"
        return
    fi

    echo "Downloading $url"
    curl -fL --retry 3 --retry-delay 2 "$url" -o "$out"
}

download \
  "https://repo1.maven.org/maven2/org/microemu/cldcapi11/${CLDC_VERSION}/cldcapi11-${CLDC_VERSION}.jar" \
  "$LIB/cldcapi11.jar"

download \
  "https://repo1.maven.org/maven2/org/microemu/midpapi20/${MIDP_VERSION}/midpapi20-${MIDP_VERSION}.jar" \
  "$LIB/midpapi20.jar"

# ProGuard's distribution contains the launcher and its runtime dependencies.
download \
  "https://github.com/Guardsquare/proguard/releases/download/v${PROGUARD_VERSION}/proguard-${PROGUARD_VERSION}.zip" \
  "$TOOLCHAIN/proguard-${PROGUARD_VERSION}.zip"

if [ ! -x "$TOOLCHAIN/proguard-${PROGUARD_VERSION}/bin/proguard.sh" ]; then
    rm -rf "$TOOLCHAIN/proguard-${PROGUARD_VERSION}"
    unzip -q "$TOOLCHAIN/proguard-${PROGUARD_VERSION}.zip" -d "$TOOLCHAIN"
fi

echo
echo "Toolchain ready."
echo "CLDC API: $LIB/cldcapi11.jar"
echo "MIDP API: $LIB/midpapi20.jar"
echo "ProGuard: $TOOLCHAIN/proguard-${PROGUARD_VERSION}/bin/proguard.sh"
