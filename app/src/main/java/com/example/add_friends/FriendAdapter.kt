package com.example.add_friends

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FriendAdapter(
    private val friends: List<Friend>,
    private val onAddClicked: ((Friend) -> Unit)? = null,
    private val showAddButton: Boolean = true   // NEW flag
) : RecyclerView.Adapter<FriendAdapter.FriendViewHolder>() {

    inner class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.findViewById(R.id.nameText)
        val numberText: TextView = itemView.findViewById(R.id.numberText)
        val addButton: Button = itemView.findViewById(R.id.addButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_friend, parent, false)
        return FriendViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val friend = friends[position]
        holder.nameText.text = friend.name
        holder.numberText.text = friend.number

        if (showAddButton) {
            holder.addButton.visibility = View.VISIBLE
            holder.addButton.setOnClickListener {
                onAddClicked?.invoke(friend)
            }
        } else {
            // Hide the button in "Added Friends" section
            holder.addButton.visibility = View.GONE
        }
    }

    override fun getItemCount() = friends.size
}