package com.example.add_friends

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FriendAdapter(
    private val friends: MutableList<Friend>,
    private val mode: Mode,
    private val onActionClick: (Friend) -> Unit
) : RecyclerView.Adapter<FriendAdapter.FriendViewHolder>() {

    enum class Mode { ADD, ADDED }

    inner class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileInitial: TextView = itemView.findViewById(R.id.profileInitial)
        val nameText: TextView = itemView.findViewById(R.id.friendName)
        val actionButton: Button = itemView.findViewById(R.id.actionButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_friend, parent, false)
        return FriendViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val friend = friends[position]
        holder.nameText.text = friend.name
        holder.profileInitial.text = friend.name.first().toString()

        holder.actionButton.text = if (mode == Mode.ADD) "Add" else "Remove"
        holder.actionButton.setOnClickListener { onActionClick(friend) }
    }

    override fun getItemCount(): Int = friends.size
}