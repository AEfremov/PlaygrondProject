package ru.efremov.playgrondproject.expandablerw.model

/**
 * The backing data object for an [ExpandableGroup]
 */
open class ExpandableGroup<T>(title: String?, items: List<T>?) {

    var title: String? = title
        private set
    var items: List<T>? = items
        private set

    val itemCount: Int
        get() = if (items == null) 0 else items!!.size

    override fun toString(): String {
        return "ExpandableGroup{" +
                "title='" + title + '\'' +
                ", items=" + items +
                '}'
    }

//    protected constructor(`in`: Parcel) {
//        title = `in`.readString()
//        val hasItems = `in`.readByte()
//        val size = `in`.readInt()
//        if (hasItems.toInt() == 0x01) {
//            items = ArrayList(size)
//            val type = `in`.readSerializable() as Class<*>?
//            `in`.readList(items!!, type!!.classLoader)
//        } else {
//            items = null
//        }
//    }
}