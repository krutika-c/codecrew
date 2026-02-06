package com.example.add_friends

object SharedFriendsViewModel {
    val myFriends = mutableListOf<Friend>()

    fun addFriend(friend: Friend) {
        if (!myFriends.contains(friend)) {
            myFriends.add(friend)
        }
    }

    fun removeFriend(friend: Friend) {
        myFriends.remove(friend)
    }
}