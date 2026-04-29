#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"
cd "$REPO_ROOT/website"

if ! command -v npm >/dev/null 2>&1; then
  echo "[LabLogix] npm was not found in PATH."
  exit 1
fi

if [ ! -d node_modules ]; then
  echo "[LabLogix] Installing website dependencies..."
  npm install
fi

echo "[LabLogix] Starting website server..."
npm start
