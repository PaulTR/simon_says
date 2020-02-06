package com.devrelconf.simonsays

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot

class GamesListAdapter(private val myDataset: MutableList<DocumentSnapshot>, private val activity: AppCompatActivity) :
    RecyclerView.Adapter<GamesListAdapter.MyViewHolder>() {

    class MyViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)


    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): GamesListAdapter.MyViewHolder {
        val textView = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false) as TextView
        return MyViewHolder(textView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textView.text = myDataset[position].get("label").toString()

        holder.textView.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, PlayGameActivity::class.java)
            myDataset[position].id
            intent.putExtra("label", holder.textView.text.toString())
            activity.startActivity(intent)
            activity.finish()
        })
    }

    override fun getItemCount() = myDataset.size
}