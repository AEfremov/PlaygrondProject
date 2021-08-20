package ru.efremov.playgrondproject.expandablerw.listener

interface OnChildClickListener {
    fun onChildClick(flatPos: Int)
}

interface OnChildDataListener {
    fun <T> obtainChildData(data: T)
}