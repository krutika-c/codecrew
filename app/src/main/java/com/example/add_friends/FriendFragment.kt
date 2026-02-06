package com.example.add_friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FriendFragment : Fragment() {

    private val friends = mutableListOf<Friend>()
    private lateinit var adapter: FriendAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_friends, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.friendsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Pass all required parameters: list, mode, and click handler
        adapter = FriendAdapter(friends, FriendAdapter.Mode.ADD) { friend ->
            Toast.makeText(requireContext(), "${friend.name} clicked", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = adapter

        // Load contacts or data here
        loadContacts()
    }

    private fun loadContacts() {
        // Example: query contacts, then add to list
        // friends.add(Friend(name, number, mutualCount))
        adapter.notifyDataSetChanged()
    }
}