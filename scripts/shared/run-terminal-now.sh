#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"
cd "$REPO_ROOT"

echo "[LabLogix] Building terminal (current setup: src + Test)..."
rm -rf out
mkdir -p out

find src -name "*.java" > out/sources-now.txt
echo "Test/Authenticate.java" >> out/sources-now.txt
echo "Test/UserAccount.java" >> out/sources-now.txt

javac -d out @out/sources-now.txt

echo "[LabLogix] Starting terminal..."
java -cp "out:lib/mysql-connector-j-9.6.0.jar" src.Main
