package com.example.muzik.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.muzik.R
import com.example.muzik.data.models.Comment
import com.example.muzik.utils.TimeConverter
import com.example.muzik.viewmodels.musicplayer.ActionBarViewModel
import de.hdodenhof.circleimageview.CircleImageView

class ReplyCommentAdapter(private val context: Context,private val list:List<Comment>): RecyclerView.Adapter<ReplyCommentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.reply_comment_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Bind data to the views in the ViewHolder
        val item = list[position]
        holder.tvUsername.text = item.userName
        holder.tvCreatedAt.text = item.createdAt?.let { TimeConverter.convertTimestampToString(it) }
        holder.tvContent.text = item.content
        // Set click listener or any other logic here
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var avt: CircleImageView
        var tvUsername: TextView
        var tvCreatedAt: TextView
        var tvContent: TextView
        init {
            tvUsername = itemView.findViewById<TextView>(R.id.tv_rep_cmt_name)
            tvCreatedAt = itemView.findViewById<TextView>(R.id.tv_rep_cmt_createAt)
            avt = itemView.findViewById<CircleImageView>(R.id.img_rep_cmt_user_avt)
            tvContent = itemView.findViewById<TextView>(R.id.tv_rep_cmt_content)
        }
    }
}