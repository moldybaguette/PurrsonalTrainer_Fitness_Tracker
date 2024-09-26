package za.co.varsitycollege.st10204902.purrsonaltrainer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import za.co.varsitycollege.st10204902.purrsonaltrainer.R

class RmAdapter(private val rmList: List<String>) : RecyclerView.Adapter<RmAdapter.RmViewHolder>() {

    class RmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rmTextView: TextView = itemView.findViewById(R.id.rmTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RmViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rm_item, parent, false)
        return RmViewHolder(view)
    }

    override fun onBindViewHolder(holder: RmViewHolder, position: Int) {
        holder.rmTextView.text = rmList[position]
    }

    override fun getItemCount(): Int = rmList.size
}
