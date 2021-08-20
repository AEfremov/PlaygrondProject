package ru.efremov.playgrondproject.adaptersample

import android.view.View
import android.widget.TextView
import ru.efremov.playgrondproject.R
import ru.efremov.playgrondproject.expandablerw.viewholder.ChildViewHolder

class ArtistViewHolder(itemView: View) : ChildViewHolder(itemView) {

    private val childTextView: TextView = itemView.findViewById<View>(R.id.list_item_artist_name) as TextView

    fun setArtistName(name: String?) {
        childTextView.text = name
    }
}