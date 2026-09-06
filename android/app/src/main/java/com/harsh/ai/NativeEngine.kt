package com.harsh.ai

object NativeEngine {
    init { System.loadLibrary("harshai") }
    external fun status(): String
}
