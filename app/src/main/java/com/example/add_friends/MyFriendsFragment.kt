package com.example.add_friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MyFriendsFragment : Fragment() {

    private lateinit var adapter: FriendAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_friends, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.myFriendsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = FriendAdapter(FriendRepository.myFriends, FriendAdapter.Mode.ADDED) { friend ->
            FriendRepository.myFriends.remove(friend)
            adapter.notifyDataSetChanged()
            Toast.makeText(requireContext(), "${friend.name} removed", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = adapter
    }
}