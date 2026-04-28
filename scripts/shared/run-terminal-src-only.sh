#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"
cd "$REPO_ROOT"

echo "[LabLogix] Building terminal (future setup: src-only)..."
rm -rf out
mkdir -p out

find src -name "*.java" > out/sources.txt

javac -d out @out/sources.txt

echo "[LabLogix] Starting terminal..."
java -cp "out:lib/mysql-connector-j-9.6.0.jar" src.Main
