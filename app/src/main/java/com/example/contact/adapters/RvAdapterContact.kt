package com.example.contact.adapters

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.forEach
import androidx.core.view.get
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.example.contact.R
import com.example.contact.databinding.ItemContactBinding
import com.example.contact.models.Contact
import java.util.*
import kotlin.collections.ArrayList

class RvAdapterContact(var context:Context,var list: List<Contact>,var onItemClickListener: OnItemClickListener):
    RecyclerView.Adapter<RvAdapterContact.Vh>() {
      var viewBinderHalper = ViewBinderHelper()

    inner class Vh(var itemContactBinding: ItemContactBinding):RecyclerView.ViewHolder(itemContactBinding.root){
        init {
            viewBinderHalper.setOpenOnlyOne(true)
        }
    fun onBind(contact: Contact,position: Int){
        viewBinderHalper.bind(itemContactBinding.swipeRevealLayout, position.toString())
        itemContactBinding.namePerson.text = contact.name
        itemContactBinding.numberPerson.text = contact.number
        itemContactBinding.root.animation=  AnimationUtils.loadAnimation(context,R.anim.animation_item)
        itemContactBinding.menu.setOnClickListener {
            onItemClickListener.onItemClickMenu(contact,position)
        }
        itemContactBinding.telImage.setOnClickListener {
            onItemClickListener.onItemClickTel(contact,position)
        }
        itemContactBinding.phone.setOnClickListener {
            onItemClickListener.itemClickTel(contact,position)
        }
        itemContactBinding.sms.setOnClickListener {
              onItemClickListener.itemClickSMS(contact,position)
        }
    }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemContactBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position],position)
    }

    override fun getItemCount(): Int {
       return list.size
    }
    interface OnItemClickListener{
        fun onItemClickMenu(contact: Contact,position: Int)
        fun onItemClickTel(contact: Contact,position: Int)

        fun itemClickTel(contact: Contact,position: Int)
        fun itemClickSMS(contact: Contact,position: Int)
    }

    fun filterList(listFilter:ArrayList<Contact>){
        list = listFilter
        notifyDataSetChanged()
    }
}