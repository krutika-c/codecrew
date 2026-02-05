package com.example.add_friends

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MyFriendsFragment : Fragment(R.layout.fragment_myfriends) {

    private lateinit var addFriendsRecyclerView: RecyclerView
    private lateinit var addedFriendsRecyclerView: RecyclerView

    private val allContacts = mutableListOf<Friend>()
    private val addedFriends = mutableListOf<Friend>()

    private lateinit var addFriendsAdapter: MyFriendsAdapter
    private lateinit var addedFriendsAdapter: MyFriendsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addFriendsRecyclerView = view.findViewById(R.id.addFriendsRecyclerView)
        addedFriendsRecyclerView = view.findViewById(R.id.addedFriendsRecyclerView)

        addFriendsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        addedFriendsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_CONTACTS),
                1
            )
        } else {
            loadContacts()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadContacts()
        }
    }

    private fun loadContacts() {
        allContacts.clear()
        val cursor = requireContext().contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null, null, null, null
        ) ?: return

        cursor.use {
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            while (it.moveToNext()) {
                val name = it.getString(nameIndex) ?: ""
                val number = it.getString(numberIndex) ?: ""
                allContacts.add(Friend(name, number))
            }
        }

        // Adapter for "Add Friends"
        addFriendsAdapter = MyFriendsAdapter(allContacts, MyFriendsAdapter.Mode.ADD) { friend ->
            allContacts.remove(friend)
            addedFriends.add(friend)
            addFriendsAdapter.notifyDataSetChanged()
            addedFriendsAdapter.notifyDataSetChanged()
        }
        addFriendsRecyclerView.adapter = addFriendsAdapter

        // Adapter for "Added Friends"
        addedFriendsAdapter = MyFriendsAdapter(addedFriends, MyFriendsAdapter.Mode.ADDED)
        addedFriendsRecyclerView.adapter = addedFriendsAdapter
    }
}