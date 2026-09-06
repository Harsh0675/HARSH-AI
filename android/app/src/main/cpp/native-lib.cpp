#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_harsh_ai_MainActivity_nativeGenerate(JNIEnv* env, jobject, jstring modelPath, jstring prompt, jint maxTokens) {
    // Production binding point: llama.cpp model/context/sampler integration.
    // The Gradle/CMake project fetches llama.cpp during the build; no network is
    // required at runtime. This placeholder keeps the UI buildable while the
    // model API is pinned and wired in the next engine commit.
    (void)modelPath; (void)maxTokens;
    const char* p = env->GetStringUTFChars(prompt, nullptr);
    std::string out = "HARSH-AI native runtime initialized. Prompt received: " + std::string(p);
    env->ReleaseStringUTFChars(prompt, p);
    return env->NewStringUTF(out.c_str());
}
