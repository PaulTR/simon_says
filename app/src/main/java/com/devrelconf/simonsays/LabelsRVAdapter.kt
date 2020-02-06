package com.devrelconf.simonsays

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel

class LabelsRVAdapter(private val myDataset: MutableList<FirebaseVisionImageLabel>, private val activity: AppCompatActivity) :
    RecyclerView.Adapter<LabelsRVAdapter.MyViewHolder>() {

    val db = FirebaseFirestore.getInstance()


    class MyViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): LabelsRVAdapter.MyViewHolder {
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
                    Toast.makeText(parent.context, "Success!", Toast.LENGTH_SHORT).show()
                    activity.finish()
                }

        })
        return MyViewHolder(textView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.text = myDataset[position].text
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}