package com.example.borarachar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.DecimalFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var mTTS: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var edtConta: EditText = findViewById(R.id.edtConta)
        var edtPessoas: EditText = findViewById(R.id.edtPessoas)
        var resultText: TextView = findViewById(R.id.result_text)

        val df = DecimalFormat("#.00")
        var resultado: String = "0.00"

        mTTS = TextToSpeech(applicationContext, TextToSpeech.OnInitListener { status ->
            if (status != TextToSpeech.ERROR) {
                mTTS.language = Locale("pt_BR")
            }
        })

        edtConta.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().length < 1) {
                    return;
                }

                var conta:Double = p0.toString().toDouble()

                if (edtPessoas.text.toString().length < 1) {
                    return;
                }

                var pessoas:Int = edtPessoas.text.toString().toInt()

                if (conta > 0 && pessoas > 0) {
                    var resultadoDivisao: Double = conta / pessoas
                    resultado = df.format(resultadoDivisao).toString()
                    resultText.text = resultado
                }
            }
        })

        edtPessoas.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().length < 1) {
                    return;
                }

                var pessoas:Int = p0.toString().toInt()

                if (edtConta.text.toString().length < 1) {
                    return;
                }

                var conta:Double = edtConta.text.toString().toDouble()

                if (conta > 0 && pessoas > 0) {
                    var resultadoDivisao: Double = conta / pessoas
                    resultado = df.format(resultadoDivisao).toString()
                    resultText.text = resultado
                }
            }
        })

        val btnShare:FloatingActionButton = findViewById(R.id.share_action_button)
        val btnVoice:FloatingActionButton = findViewById(R.id.voice_action_button)

        btnShare.setOnClickListener(View.OnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Ficou R$ $resultado para cada um.")
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        })

        btnVoice.setOnClickListener(View.OnClickListener {
            if (mTTS.isSpeaking) {
                mTTS.stop()
            } else {
                Toast.makeText(this, "Ficou R$ $resultado para cada um.", Toast.LENGTH_SHORT).show()
                mTTS.speak("Ficou R$ $resultado para cada um.", TextToSpeech.QUEUE_FLUSH, null)
            }
        })
    }
}