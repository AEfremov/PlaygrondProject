package ru.efremov.playgrondproject.expandablerw.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.efremov.playgrondproject.expandablerw.listener.OnChildClickListener

/**
 * ViewHolder for [ExpandableGroup.items]
 */
@Suppress("LeakingThis")
open class ChildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private var listener: OnChildClickListener? = null

    override fun onClick(v: View?) {
        listener?.onChildClick(absoluteAdapterPosition)
    }

    fun setOnChildClickListener(listener: OnChildClickListener?) {
        this.listener = listener
    }

    init {
        itemView.setOnClickListener(this)
    }
}