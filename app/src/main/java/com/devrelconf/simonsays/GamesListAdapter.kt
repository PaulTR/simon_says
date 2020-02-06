package com.devrelconf.simonsays

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class GamesListAdapter(private val myDataset: MutableList<DocumentSnapshot>, private val activity: AppCompatActivity) :
    RecyclerView.Adapter<GamesListAdapter.MyViewHolder>() {

    val db = FirebaseFirestore.getInstance()


    class MyViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): GamesListAdapter.MyViewHolder {
        // create a new view
        val textView = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false) as TextView

        textView.setOnClickListener(View.OnClickListener {
            Toast.makeText(parent.context, textView.text, Toast.LENGTH_SHORT).show()

            val document = hashMapOf(
                "label" to textView.text
            )

            db.collection("games").document(textView.text.toString())
                .set(document)
                .addOnSuccessListener {
                    startActivity(activity, Intent(activity, PlayGameActivity::class.java), null)
                    activity.finish()
                }

        })
        return MyViewHolder(textView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.text = myDataset[position].get("label").toString()
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}