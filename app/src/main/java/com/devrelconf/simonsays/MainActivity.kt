package com.devrelconf.simonsays

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_create_challenge.setOnClickListener( View.OnClickListener {
            startActivity(Intent(baseContext, CreateChallengeActivity::class.java))
        })

        btn_start_challenge.setOnClickListener( View.OnClickListener {
            startActivity(Intent(baseContext, StartChallengeActivity::class.java))
        })
    }
}
