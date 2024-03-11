package com.example.pokefight.ui.settings

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pokefight.R
import com.example.pokefight.model.User


class FriendsAdapter : RecyclerView.Adapter<FriendsAdapter.FriendViewHolder>() {

    private var friendList = listOf<User>()
    class FriendViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val friend_name: TextView = view.findViewById(R.id.settings_friend_name)
        val friend_trophy: TextView = view.findViewById(R.id.settings_friend_trophy)
        val friend_card: LinearLayout = view.findViewById(R.id.settings_friend)
    }

    fun setFriendList(newList: List<User>){
        friendList = newList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_friends_adapter, parent, false)
        return FriendViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int {
        return friendList.size
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val friend  = friendList[position]

        holder.friend_name.text = friend.Nickname
        holder.friend_trophy.text = friend.Trophy.toString()
        holder.friend_card.setOnClickListener {
            //TODO start a swap
        }
    }
}