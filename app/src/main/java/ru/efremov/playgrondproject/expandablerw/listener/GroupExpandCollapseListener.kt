package ru.efremov.playgrondproject.expandablerw.listener

import ru.efremov.playgrondproject.expandablerw.model.ExpandableGroup

interface GroupExpandCollapseListener {
    /**
     * Called when a group is expanded
     * @param group the [ExpandableGroup] being expanded
     */
    fun <T> onGroupExpanded(group: ExpandableGroup<T>)

    /**
     * Called when a group is collapsed
     * @param group the [ExpandableGroup] being collapsed
     */
    fun <T> onGroupCollapsed(group: ExpandableGroup<T>)
}