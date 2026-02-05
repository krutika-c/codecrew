package com.example.add_friends

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyFriendsAdapter(
    private val friends: MutableList<Friend>,
    private val mode: Mode,
    private val onActionClicked: ((Friend) -> Unit)? = null
) : RecyclerView.Adapter<MyFriendsAdapter.FriendViewHolder>() {

    enum class Mode {
        ADD, ADDED
    }

    inner class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avatarText: TextView = itemView.findViewById(R.id.avatarText)
        val nameText: TextView = itemView.findViewById(R.id.nameText)
        val numberText: TextView = itemView.findViewById(R.id.numberText)
        val actionButton: Button = itemView.findViewById(R.id.addButton)
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

        // Generate initials (first letter of name)
        val initials = if (friend.name.isNotEmpty()) friend.name[0].toString().uppercase() else "?"
        holder.avatarText.text = initials

        when (mode) {
            Mode.ADD -> {
                holder.actionButton.visibility = View.VISIBLE
                holder.actionButton.text = "Add"
                holder.actionButton.setOnClickListener {
                    onActionClicked?.invoke(friend)
                }
            }
            Mode.ADDED -> {
                holder.actionButton.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int = friends.size
}