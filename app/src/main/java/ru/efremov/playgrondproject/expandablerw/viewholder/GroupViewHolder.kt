package ru.efremov.playgrondproject.expandablerw.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.efremov.playgrondproject.expandablerw.listener.OnGroupClickListener

/**
 * ViewHolder for the in a [ExpandableGroup]
 *
 * The current implementation does now allow for sub [View] of the parent view to trigger
 * a collapse / expand. *Only* click events on the parent [View] will trigger a collapse or
 * expand
 */
abstract class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private var listener: OnGroupClickListener? = null

    override fun onClick(v: View) {
        listener?.onGroupClick(absoluteAdapterPosition)
    }

    fun setOnGroupClickListener(listener: OnGroupClickListener?) {
        this.listener = listener
    }

    open fun expand() {}
    open fun collapse() {}

    init {
        itemView.setOnClickListener(this)
    }
}