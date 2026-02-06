package com.example.add_friends

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class FriendsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = 2
    override fun createFragment(position: Int): Fragment {
        return if (position == 0) SuggestedFriendsFragment() else MyFriendsFragment()
    }
}