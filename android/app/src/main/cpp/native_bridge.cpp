#include <jni.h>

extern "C" JNIEXPORT jstring JNICALL
Java_com_harsh_ai_NativeEngine_status(JNIEnv* env, jobject) {
    return env->NewStringUTF("HARSH-AI native engine ready for llama.cpp integration");
}
