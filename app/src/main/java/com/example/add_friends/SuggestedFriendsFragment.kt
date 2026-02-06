package com.example.add_friends

import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SuggestedFriendsFragment : Fragment() {

    private lateinit var adapter: FriendAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_suggested_friends, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.suggestedRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = FriendAdapter(FriendRepository.suggestedFriends, FriendAdapter.Mode.ADD) { friend ->
            FriendRepository.suggestedFriends.remove(friend)
            FriendRepository.myFriends.add(friend)
            adapter.notifyDataSetChanged()
            Toast.makeText(requireContext(), "${friend.name} added", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = adapter

        loadContacts()
    }

    private fun loadContacts() {
        FriendRepository.suggestedFriends.clear()

        val cursor = requireContext().contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
            ),
            null,
            null,
            null
        )

        cursor?.use {
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (it.moveToNext()) {
                val name = it.getString(nameIndex)
                val number = it.getString(numberIndex)
                FriendRepository.suggestedFriends.add(Friend(name, number))
            }
        }

        adapter.notifyDataSetChanged()
    }
}