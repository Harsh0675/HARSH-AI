package com.harsh.ai

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import android.graphics.Color
import android.graphics.Typeface
import java.io.File

class MainActivity : Activity() {
    private lateinit var messages: LinearLayout
    private lateinit var input: EditText
    private external fun nativeGenerate(modelPath: String, prompt: String, maxTokens: Int): String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        System.loadLibrary("harsh_ai")
        buildUi()
    }

    private fun buildUi() {
        val root = LinearLayout(this).apply { orientation=LinearLayout.VERTICAL; setPadding(28,24,28,18); setBackgroundColor(Color.rgb(7,9,13)) }
        val header=LinearLayout(this).apply { orientation=LinearLayout.HORIZONTAL; gravity=Gravity.CENTER_VERTICAL }
        val brand=TextView(this).apply { text="HARSH-AI"; textSize=26f; typeface=Typeface.DEFAULT_BOLD; setTextColor(Color.WHITE) }
        val status=TextView(this).apply { text="  •  OFFLINE"; textSize=13f; setTextColor(Color.rgb(100,220,150)) }
        header.addView(brand,LinearLayout.LayoutParams(0,-2,1f)); header.addView(status); root.addView(header)
        root.addView(TextView(this).apply { text="Private on-device intelligence • No cloud required"; textSize=13f; setTextColor(Color.rgb(155,160,175)); setPadding(0,4,0,18) })
        messages=LinearLayout(this).apply { orientation=LinearLayout.VERTICAL; setPadding(0,8,0,8) }
        root.addView(ScrollView(this).apply { addView(messages) },LinearLayout.LayoutParams(-1,0,1f))
        val row=LinearLayout(this).apply { gravity=Gravity.CENTER_VERTICAL }
        input=EditText(this).apply { hint="Ask HARSH-AI anything…"; hintTextColor=Color.GRAY; setTextColor(Color.WHITE); setSingleLine(false); setBackgroundColor(Color.rgb(20,23,31)); setPadding(18,14,18,14) }
        row.addView(input,LinearLayout.LayoutParams(0,-2,1f)); row.addView(Button(this).apply { text="Send"; setOnClickListener{sendMessage()} })
        root.addView(row); setContentView(root)
        addMessage("HARSH-AI","Offline engine ready. Place HARSH-AI.gguf in the app's private model directory to chat without internet.")
    }
    private fun sendMessage(){ val text=input.text.toString().trim(); if(text.isEmpty())return; addMessage("You",text); input.text.clear(); Thread { val model=File(filesDir,"HARSH-AI.gguf"); val answer=if(model.exists()) nativeGenerate(model.absolutePath,text,256) else "Model not found. Import HARSH-AI.gguf first."; runOnUiThread{addMessage("HARSH-AI",answer)} }.start() }
    private fun addMessage(author:String,text:String){ messages.addView(TextView(this).apply { this.text="$author\n$text"; textSize=15f; setTextColor(Color.WHITE); setPadding(18,14,18,14); setBackgroundColor(Color.rgb(20,23,31)) },LinearLayout.LayoutParams(-1,-2).apply{setMargins(0,0,0,12)}) }
}
