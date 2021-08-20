package ru.efremov.playgrondproject.expandablerw

import ru.efremov.playgrondproject.expandablerw.viewholder.GroupViewHolder
import ru.efremov.playgrondproject.expandablerw.model.ExpandableGroup
import androidx.recyclerview.widget.RecyclerView
import ru.efremov.playgrondproject.expandablerw.model.ExpandableList
import android.view.ViewGroup
import ru.efremov.playgrondproject.expandablerw.listener.*
import ru.efremov.playgrondproject.expandablerw.model.ExpandableListPosition
import ru.efremov.playgrondproject.expandablerw.viewholder.ChildViewHolder
import java.lang.IllegalArgumentException

@Suppress("LeakingThis", "KDocUnresolvedReference")
abstract class ExpandableRecyclerViewAdapter<GVH : GroupViewHolder, CVH : ChildViewHolder>(groups: List<ExpandableGroup<*>>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    ExpandCollapseListener,
    OnGroupClickListener,
    OnChildClickListener
{

    protected var expandableList: ExpandableList = ExpandableList(groups)
    private val expandCollapseController: ExpandCollapseController =
        ExpandCollapseController(expandableList, this)
    private var groupClickListener: OnGroupClickListener? = null
    private var childDataListener: OnChildDataListener? = null
    private var expandCollapseListener: GroupExpandCollapseListener? = null

    /**
     * Implementation of Adapter.onCreateViewHolder(ViewGroup, int)
     * that determines if the list item is a group or a child and calls through
     * to the appropriate implementation of either [.onCreateGroupViewHolder]
     * or [.onCreateChildViewHolder]}.
     *
     * @param parent The [ViewGroup] into which the new [android.view.View]
     * will be added after it is bound to an adapter position.
     * @param viewType The view type of the new `android.view.View`.
     * @return Either a new [GroupViewHolder] or a new [ChildViewHolder]
     * that holds a `android.view.View` of the given view type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ExpandableListPosition.GROUP -> {
                val gvh = onCreateGroupViewHolder(parent, viewType)
                gvh.setOnGroupClickListener(this)
                gvh
            }
            ExpandableListPosition.CHILD -> {
                val cvh = onCreateChildViewHolder(parent, viewType)
                cvh.setOnChildClickListener(this)
                cvh
            }
            else -> throw IllegalArgumentException("viewType is not valid")
        }
    }

    /**
     * Implementation of Adapter.onBindViewHolder(RecyclerView.ViewHolder, int)
     * that determines if the list item is a group or a child and calls through
     * to the appropriate implementation of either [.onBindGroupViewHolder]
     * or [.onBindChildViewHolder].
     *
     * @param holder Either the GroupViewHolder or the ChildViewHolder to bind data to
     * @param position The flat position (or index in the list of [ ][ExpandableList.getVisibleItemCount] in the list at which to bind
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val listPos = expandableList.getUnflattenedPosition(position)
        val group = expandableList.getExpandableGroup(listPos)
        when (listPos.type) {
            ExpandableListPosition.GROUP -> {
                onBindGroupViewHolder(holder as GVH?, position, group)
                if (isGroupExpanded(group)) {
                    (holder as GVH?)!!.expand()
                } else {
                    (holder as GVH?)!!.collapse()
                }
            }
            ExpandableListPosition.CHILD -> onBindChildViewHolder(
                holder as CVH?,
                position,
                group,
                listPos.childPos
            )
        }
    }

    /**
     * @return the number of group and child objects currently expanded
     * @see ExpandableList.getVisibleItemCount
     */
    override fun getItemCount(): Int {
        return expandableList.visibleItemCount
    }

    /**
     * Gets the view type of the item at the given position.
     *
     * @param position The flat position in the list to get the view type of
     * @return {@value ExpandableListPosition#CHILD} or {@value ExpandableListPosition#GROUP}
     * @throws RuntimeException if the item at the given position in the list is not found
     */
    override fun getItemViewType(position: Int): Int {
        return expandableList.getUnflattenedPosition(position).type
    }

    /**
     * Called when a group is expanded
     *
     * @param positionStart the flat position of the first child in the [ExpandableGroup]
     * @param itemCount the total number of children in the [ExpandableGroup]
     */
    override fun onGroupExpanded(positionStart: Int, itemCount: Int) {
        //update header
        val headerPosition = positionStart - 1
        notifyItemChanged(headerPosition)

        // only insert if there items to insert
        if (itemCount > 0) {
            notifyItemRangeInserted(positionStart, itemCount)
            if (expandCollapseListener != null) {
                val groupIndex = expandableList.getUnflattenedPosition(positionStart).groupPos
                expandCollapseListener!!.onGroupExpanded(groups[groupIndex])
            }
        }
    }

    /**
     * Called when a group is collapsed
     *
     * @param positionStart the flat position of the first child in the [ExpandableGroup]
     * @param itemCount the total number of children in the [ExpandableGroup]
     */
    override fun onGroupCollapsed(positionStart: Int, itemCount: Int) {
        //update header
        val headerPosition = positionStart - 1
        notifyItemChanged(headerPosition)

        // only remote if there items to remove
        if (itemCount > 0) {
            notifyItemRangeRemoved(positionStart, itemCount)
            if (expandCollapseListener != null) {
                //minus one to return the position of the header, not first child
                val groupIndex = expandableList.getUnflattenedPosition(positionStart - 1).groupPos
                expandCollapseListener!!.onGroupCollapsed(groups[groupIndex])
            }
        }
    }

    /**
     * Triggered by a click on a [GroupViewHolder]
     *
     * @param flatPos the flat position of the [GroupViewHolder] that was clicked
     * @return false if click expanded group, true if click collapsed group
     */
    override fun onGroupClick(flatPos: Int): Boolean {
        groupClickListener?.onGroupClick(flatPos)
        return expandCollapseController.toggleGroup(flatPos)
    }

    /**
     * Triggered by a click on a [ChildViewHolder]
     */
    override fun onChildClick(flatPos: Int) {
        childDataListener?.let { listener ->
            val listPos: ExpandableListPosition = expandableList.getUnflattenedPosition(flatPos)
            val childGroup = expandableList.getExpandableGroup(listPos) as ExpandableGroup
            val childPos = listPos.childPos
            val item = childGroup.items!![childPos]
            listener.obtainChildData(item)
        }
    }

    /**
     * @param flatPos The flat list position of the group
     * @return true if the group is expanded, *after* the toggle, false if the group is now collapsed
     */
    fun toggleGroup(flatPos: Int): Boolean {
        return expandCollapseController.toggleGroup(flatPos)
    }

    /**
     * @param group the [ExpandableGroup] being toggled
     * @return true if the group is expanded, *after* the toggle, false if the group is now collapsed
     */
    fun toggleGroup(group: ExpandableGroup<*>?): Boolean {
        return expandCollapseController.toggleGroup(group!!)
    }

    /**
     * @param flatPos the flattened position of an item in the list
     * @return true if `group` is expanded, false if it is collapsed
     */
    fun isGroupExpanded(flatPos: Int): Boolean {
        return expandCollapseController.isGroupExpanded(flatPos)
    }

    /**
     * @param group the [ExpandableGroup] being checked for its collapsed state
     * @return true if `group` is expanded, false if it is collapsed
     */
    fun isGroupExpanded(group: ExpandableGroup<*>?): Boolean {
        return expandCollapseController.isGroupExpanded(group)
    }

    fun setOnGroupClickListener(listener: OnGroupClickListener?) {
        groupClickListener = listener
    }

    fun setOnChildDataListener(listener: OnChildDataListener) {
        childDataListener = listener
    }

    fun setOnGroupExpandCollapseListener(listener: GroupExpandCollapseListener?) {
        expandCollapseListener = listener
    }

    /**
     * The full list of [ExpandableGroup] backing this RecyclerView
     *
     * @return the list of [ExpandableGroup] that this object was instantiated with
     */
    val groups: List<ExpandableGroup<*>>
        get() = expandableList.groups

    /**
     * Called from [.onCreateViewHolder] when  the list item created is a group
     *
     * @param viewType an int returned by [ExpandableRecyclerViewAdapter.getItemViewType]
     * @param parent the [ViewGroup] in the list for which a [GVH]  is being created
     * @return A [GVH] corresponding to the group list item with the  `ViewGroup` parent
     */
    abstract fun onCreateGroupViewHolder(parent: ViewGroup?, viewType: Int): GVH

    /**
     * Called from [.onCreateViewHolder] when the list item created is a child
     *
     * @param viewType an int returned by [ExpandableRecyclerViewAdapter.getItemViewType]
     * @param parent the [ViewGroup] in the list for which a [CVH]  is being created
     * @return A [CVH] corresponding to child list item with the  `ViewGroup` parent
     */
    abstract fun onCreateChildViewHolder(parent: ViewGroup?, viewType: Int): CVH

    /**
     * Called from onBindViewHolder(RecyclerView.ViewHolder, int) when the list item
     * bound to is a  child.
     *
     *
     * Bind data to the [CVH] here.
     *
     * @param holder The `CVH` to bind data to
     * @param flatPosition the flat position (raw index) in the list at which to bind the child
     * @param group The [ExpandableGroup] that the the child list item belongs to
     * @param childIndex the index of this child within it's [ExpandableGroup]
     */
    abstract fun onBindChildViewHolder(
        holder: CVH?, flatPosition: Int, group: ExpandableGroup<*>?,
        childIndex: Int
    )

    /**
     * Called from onBindViewHolder(RecyclerView.ViewHolder, int) when the list item bound to is a
     * group
     *
     *
     * Bind data to the [GVH] here.
     *
     * @param holder The `GVH` to bind data to
     * @param flatPosition the flat position (raw index) in the list at which to bind the group
     * @param group The [ExpandableGroup] to be used to bind data to this [GVH]
     */
    abstract fun onBindGroupViewHolder(holder: GVH?, flatPosition: Int, group: ExpandableGroup<*>?)

    companion object {
        private const val EXPAND_STATE_MAP = "expandable_recyclerview_adapter_expand_state_map"
    }

}