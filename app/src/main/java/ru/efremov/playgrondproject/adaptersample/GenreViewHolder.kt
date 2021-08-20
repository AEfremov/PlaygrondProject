package ru.efremov.playgrondproject.adaptersample

import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import ru.efremov.playgrondproject.expandablerw.viewholder.GroupViewHolder
import android.widget.TextView
import ru.efremov.playgrondproject.expandablerw.model.ExpandableGroup
import ru.efremov.playgrondproject.R

class GenreViewHolder(itemView: View) : GroupViewHolder(itemView) {

    private val genreName: TextView = itemView.findViewById<View>(R.id.list_item_genre_name) as TextView
    private val arrow: ImageView = itemView.findViewById<View>(R.id.list_item_genre_arrow) as ImageView
    private val icon: ImageView = itemView.findViewById<View>(R.id.list_item_genre_icon) as ImageView

    fun setGenreTitle(genre: ExpandableGroup<*>) {
        if (genre is Genre) {
            genreName.text = genre.title
            icon.setBackgroundResource(genre.iconResId)
        }
    }

    override fun expand() {
        animateExpand()
    }

    override fun collapse() {
        animateCollapse()
    }

    private fun animateExpand() {
        val rotate = RotateAnimation(
            360f,
            180f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        rotate.duration = 300
        rotate.fillAfter = true
        arrow.animation = rotate
    }

    private fun animateCollapse() {
        val rotate = RotateAnimation(
            180f,
            360f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        rotate.duration = 300
        rotate.fillAfter = true
        arrow.animation = rotate
    }

}