package com.example.muzik.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.muzik.R
import com.example.muzik.data.models.Comment
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class CommentsAdapter(context: Context, list: List<Comment>) :
    RecyclerView.Adapter<CommentsViewHolder>() {
    private var _context: Context
    private var _list: List<Comment>

    init {
        _list = list
        _context = context
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        val view: View =
            LayoutInflater.from(_context).inflate(R.layout.comment_item, parent, false)
        return CommentsViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        val comment: Comment = _list[position]
        holder.tvCreatedAt.text = comment.createdAt;
        holder.tvUsername.text = comment.user.displayName;
        holder.tvContent.text = comment.content;
        holder.tvHeartNumber.text = if(comment.hearts.size == 0) "" else comment.hearts.size.toString();
        // Load ảnh đại diện của user bằng thư viện Picasso
        if(!comment.user.avatarUrl.isNullOrEmpty() ){
            Picasso.get().load(comment.user.avatarUrl).into(holder.avt)
        }
    }
    override fun getItemCount(): Int {
        return _list.size
    }
}
class CommentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var avt: CircleImageView
    var tvUsername: TextView
    var tvCreatedAt: TextView
    var tvContent: TextView
    var tvHeartNumber: TextView
    init {
        tvHeartNumber = itemView.findViewById<TextView>(R.id.tv_comment_heart_number)
        tvUsername = itemView.findViewById<TextView>(R.id.tv_comment_creator)
        tvCreatedAt = itemView.findViewById<TextView>(R.id.tv_comment_createAt)
        avt = itemView.findViewById<CircleImageView>(R.id.img_comment_user_avt)
        tvContent = itemView.findViewById<TextView>(R.id.tv_comment_content)
    }
}
