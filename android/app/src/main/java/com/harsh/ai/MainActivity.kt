package com.harsh.ai

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import android.graphics.Color
import android.graphics.Typeface
import android.view.ViewGroup

class MainActivity : Activity() {
    private lateinit var status: TextView
    private lateinit var messages: LinearLayout
    private lateinit var input: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildUi()
    }

    private fun buildUi() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(28, 24, 28, 18)
            setBackgroundColor(Color.rgb(7, 9, 13))
        }

        val header = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        val brand = TextView(this).apply {
            text = "HARSH-AI"
            textSize = 26f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.WHITE)
        }
        status = TextView(this).apply {
            text = "  •  OFFLINE"
            textSize = 13f
            setTextColor(Color.rgb(100, 220, 150))
        }
        header.addView(brand, LinearLayout.LayoutParams(0, -2, 1f))
        header.addView(status)
        root.addView(header)

        val subtitle = TextView(this).apply {
            text = "Private on-device intelligence • No cloud required"
            textSize = 13f
            setTextColor(Color.rgb(155, 160, 175))
            setPadding(0, 4, 0, 18)
        }
        root.addView(subtitle)

        messages = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(0, 8, 0, 8)
        }
        val scroll = ScrollView(this).apply { addView(messages) }
        root.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))

        val row = LinearLayout(this).apply { gravity = Gravity.CENTER_VERTICAL }
        input = EditText(this).apply {
            hint = "Ask HARSH-AI anything…"
            hintTextColor = Color.rgb(110, 115, 130)
            setTextColor(Color.WHITE)
            setSingleLine(false)
            setBackgroundColor(Color.rgb(20, 23, 31))
            setPadding(18, 14, 18, 14)
        }
        val send = Button(this).apply {
            text = "Send"
            setOnClickListener { sendMessage() }
        }
        row.addView(input, LinearLayout.LayoutParams(0, -2, 1f))
        row.addView(send, LinearLayout.LayoutParams(-2, -2))
        root.addView(row)

        setContentView(root)
        addMessage("HARSH-AI", "Your private AI is ready. Import a GGUF model in the Model screen to begin fully offline inference.")
    }

    private fun sendMessage() {
        val text = input.text.toString().trim()
        if (text.isEmpty()) return
        addMessage("You", text)
        input.text.clear()
        addMessage("HARSH-AI", "Native llama.cpp inference is the next engine layer. This UI is prepared for the on-device engine.")
    }

    private fun addMessage(author: String, text: String) {
        val tv = TextView(this).apply {
            this.text = "$author\n$text"
            textSize = 15f
            setTextColor(Color.WHITE)
            setPadding(18, 14, 18, 14)
            setBackgroundColor(Color.rgb(20, 23, 31))
        }
        val lp = LinearLayout.LayoutParams(-1, -2)
        lp.setMargins(0, 0, 0, 12)
        messages.addView(tv, lp)
    }
}
