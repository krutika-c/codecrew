package com.example.add_friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class FriendsContainerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_friends_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)
        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout)

        val adapter = FriendsPagerAdapter(this)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = if (position == 0) "Suggested" else "My Friends"
        }.attach()
    }
}