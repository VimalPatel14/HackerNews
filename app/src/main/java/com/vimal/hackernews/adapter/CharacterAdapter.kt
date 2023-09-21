package com.vimal.hackernews.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vimal.hackernews.R
import com.vimal.hackernews.network.Item

class CharacterAdapter(
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var characterList = mutableListOf<Item>()

    fun setCharacter(movies: Item) {
        if (!characterList.contains(movies)) {
            characterList.add(movies)
            notifyDataSetChanged()
        }
    }

    fun showLoadingCharacter() {
        notifyItemInserted(characterList.size) // Insert the loading item at the end
    }

    fun removePosition(position: Int) {
        characterList.removeAt(position)
        notifyItemChanged(position)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_character, parent, false)
        return ViewHolder(inflater)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val character = characterList[position]
                holder.name.text = character.title
            }
        }
    }

    override fun getItemCount(): Int {
        return characterList.size
    }
}