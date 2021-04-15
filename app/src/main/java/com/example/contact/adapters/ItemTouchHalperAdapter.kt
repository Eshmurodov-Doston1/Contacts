package com.example.contact.adapters

interface ItemTouchHalperAdapter {
    fun onItemMove(fromPosition:Int,toPosition:Int)
    fun onItemDismiss(position:Int)
}