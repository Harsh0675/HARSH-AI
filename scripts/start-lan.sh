#!/data/data/com.termux/files/usr/bin/bash
set -e

cd "$HOME/harsh-ai/llama.cpp"

exec ./build/bin/llama-server \
  -m "$HOME/harsh-ai/models/HARSH-AI.gguf" \
  --host 0.0.0.0 \
  --port 8080 \
  -c 2048
