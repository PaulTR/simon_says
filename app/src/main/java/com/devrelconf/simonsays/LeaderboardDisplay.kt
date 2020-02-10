package com.devrelconf.simonsays

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_start_challenge.*

class LeaderboardDisplay : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_challenge)

        val docRef = db.collection("games").document("gameId")
        docRef.get()
            .addOnSuccessListener {
                games_list.setHasFixedSize(true)

                val layoutManager = LinearLayoutManager(baseContext)
                layoutManager.orientation = LinearLayoutManager.VERTICAL
                games_list.layoutManager = layoutManager
            }


    }

}