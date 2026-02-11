package com.example.studynest.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studynest.R
import com.example.studynest.domain.Message

class MessageAdapter(
    private val myUid: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val messages = mutableListOf<Message>()

    companion object {
        private const val VIEW_ME = 1
        private const val VIEW_OTHER = 2
    }

    fun submitList(newList: List<Message>) {
        messages.clear()
        messages.addAll(newList)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].senderId == myUid) VIEW_ME else VIEW_OTHER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_ME) {
            val view = inflater.inflate(R.layout.item_message_me, parent, false)
            MeViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.item_message_other, parent, false)
            OtherViewHolder(view)
        }
    }

    override fun getItemCount() = messages.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = messages[position]
        if (holder is MeViewHolder) holder.bind(msg)
        if (holder is OtherViewHolder) holder.bind(msg)
    }

    class MeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val txt: TextView = view.findViewById(R.id.txtMessage)
        fun bind(msg: Message) {
            txt.text = msg.text
        }
    }

    class OtherViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val txt: TextView = view.findViewById(R.id.txtMessage)
        fun bind(msg: Message) {
            txt.text = msg.text
        }
    }
}