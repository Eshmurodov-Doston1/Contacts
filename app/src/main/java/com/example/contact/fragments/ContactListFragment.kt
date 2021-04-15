package com.example.contact.fragments

import android.Manifest
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.contact.R
import com.example.contact.adapters.RvAdapterContact
import com.example.contact.databinding.FragmentContactListBinding
import com.example.contact.models.Contact
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.github.florent37.runtimepermission.kotlin.coroutines.experimental.askPermission
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ContactListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ContactListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var fragmentContactListBinding: FragmentContactListBinding
    lateinit var root:View
    lateinit var rvAdapterContact: RvAdapterContact
    lateinit var listcontact:ArrayList<Contact>
    lateinit var listContacts:ArrayList<Contact>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentContactListBinding = FragmentContactListBinding.inflate(inflater,container,false)
        root = fragmentContactListBinding.root
        askPermission(Manifest.permission.CALL_PHONE,Manifest.permission.READ_CONTACTS,Manifest.permission.SEND_SMS){
            listcontact = ArrayList()
            var cursor = (activity as AppCompatActivity).contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null)
            while (cursor!!.moveToNext()) {
                var name =  cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                var  phone_number =cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).trim()
                var contact = Contact(name, phone_number)
                listcontact.add(contact)
                 }
            listContacts = ArrayList()
                listcontact.sortBy {
                    listContacts.add(it)
                    true
                }
                rvAdapterContact = RvAdapterContact(root.context, listContacts, object : RvAdapterContact.OnItemClickListener {
                    override fun onItemClickMenu(contact: Contact, position: Int) {

                    }

                    override fun onItemClickTel(contact: Contact, position: Int) {

                    }
                    override fun itemClickTel(contact: Contact, position: Int) {
                        var intent = Intent(Intent.ACTION_CALL)
                        intent.setData(Uri.parse("tel:${contact.number}"))
                        startActivity(intent)
                    }

                    override fun itemClickSMS(contact: Contact, position: Int) {
                       var bundle = Bundle()
                        bundle.putString("name",listContacts[position].name)
                        bundle.putString("phone_number",listContacts[position].number)
                        var navOptions = NavOptions.Builder()
                        navOptions.setEnterAnim(R.anim.enter)
                        navOptions.setExitAnim(R.anim.exit)
                        navOptions.setPopEnterAnim(R.anim.popenter)
                        navOptions.setPopExitAnim(R.anim.popexit)
                        findNavController().navigate(R.id.smsFragment,bundle,navOptions.build())
                    }

                })
                fragmentContactListBinding.rv.adapter = rvAdapterContact
//                 var itemDecortion:RecyclerView.ItemDecoration
//                 itemDecortion =DividerItemDecoration(root.context,DividerItemDecoration.VERTICAL)
//                fragmentContactListBinding.rv.addItemDecoration(itemDecortion)


                cursor.close()
            }.onDeclined {
             if(it.hasDenied()){
                    AlertDialog.Builder(root.context).setMessage("Iltimos dostup bering yo`qsa sizga yordam bera olmayman")
                    .setPositiveButton("Ok") { dialog, which ->
                      it.askAgain()
                    }.setNegativeButton("No") { dialog, which ->
                        dialog.dismiss()
                    }.show()
             }
            if (it.hasForeverDenied()){
                it.goToSettings()
            }

        }
        fragmentContactListBinding.serach1.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                filter(s.toString())
            }
        })
        return root
    }


    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(root.context,Manifest.permission.READ_CONTACTS)==PackageManager.PERMISSION_GRANTED){
            listcontact = ArrayList()
            var cursor = (activity as AppCompatActivity).contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null)
            while (cursor!!.moveToNext()) {
                var     name =  cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                var  phone_number =cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).trim()
                var contact = Contact(name, phone_number)
                listcontact.add(contact)
            }
            listContacts = ArrayList()
            listcontact.sortBy {
                listContacts.add(it)
                true
            }
            rvAdapterContact = RvAdapterContact(root.context, listContacts, object : RvAdapterContact.OnItemClickListener {
                override fun onItemClickMenu(contact: Contact, position: Int) {

                }

                override fun onItemClickTel(contact: Contact, position: Int) {

                }
                override fun itemClickTel(contact: Contact, position: Int) {
                    var intent = Intent(Intent.ACTION_CALL)
                    intent.setData(Uri.parse("tel:${contact.number}"))
                    startActivity(intent)
                }

                override fun itemClickSMS(contact: Contact, position: Int) {
                    var bundle = Bundle()
                    bundle.putString("name",listContacts[position].name)
                    bundle.putString("phone_number",listContacts[position].number)
                    var navOptions = NavOptions.Builder()
                    navOptions.setEnterAnim(R.anim.enter)
                    navOptions.setExitAnim(R.anim.exit)
                    navOptions.setPopEnterAnim(R.anim.popenter)
                    navOptions.setPopExitAnim(R.anim.popexit)
                    findNavController().navigate(R.id.smsFragment,bundle,navOptions.build())
                }

            })
            fragmentContactListBinding.rv.adapter = rvAdapterContact
//            var itemDecortion:RecyclerView.ItemDecoration
//            itemDecortion =DividerItemDecoration(root.context,DividerItemDecoration.VERTICAL)
//            fragmentContactListBinding.rv.addItemDecoration(itemDecortion)

        }else{
            Toast.makeText(root.context, "Denied", Toast.LENGTH_SHORT).show()
        }





    }


      private fun filter(text: String) {
        var list =  ArrayList<Contact>()
        for (i in listContacts){
            if (i.name!!.toLowerCase().contains(text.toLowerCase())){
                list.add(i)
            }
        }
        rvAdapterContact.filterList(list)
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ContactListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ContactListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}