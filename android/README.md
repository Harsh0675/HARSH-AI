# HARSH-AI Android — Offline Edition

This is the Android client for **HARSH-AI**, designed around on-device GGUF inference with llama.cpp.

## Product goal

Install the APK, import a GGUF model once, and then chat without Wi-Fi, internet, a cloud API, or the Termux server.

## Architecture

`Android UI → JNI native engine → llama.cpp/ggml → GGUF model → CPU/GPU`

The repository intentionally does **not** contain the ~941 MB `HARSH-AI.gguf` model. Models should be supplied/imported locally because GitHub is not a suitable place for this binary.

## Production engine

The project uses an explicit native integration point in `app/src/main/cpp/`. For the production build, pin a compatible llama.cpp checkout under `android/third_party/llama.cpp` and connect its Android targets in `CMakeLists.txt`.

The official llama.cpp project includes an Android example that can be used as the native build reference. fileciteturn11file0

## Device target

- Android 8.0+ (API 26+)
- ARM64 recommended
- 4 GB+ RAM recommended for small quantized models
- More RAM is strongly recommended for ~1 GB GGUF models
- Storage must accommodate the model plus working memory

## Important

The current commit is the **Android UI + native integration foundation**, not a falsely labeled finished offline inference engine. The next production step is wiring the pinned llama.cpp native targets and model-loading/generation JNI calls, then building and testing an APK on a real ARM64 phone.
