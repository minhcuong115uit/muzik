package com.example.muzik.ui.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.muzik.R
import com.example.muzik.data.models.Comment
import com.example.muzik.ui.custom_components.CommentItemComponent
import com.example.muzik.utils.TimeConverter
import com.example.muzik.viewmodels.musicplayer.ActionBarViewModel
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class CommentsAdapter(private val context: Context,private  val viewModel:ActionBarViewModel) : RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>() {
    private var comments: List<Comment> = viewModel.comments.value!!;
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        val view = CommentItemComponent(parent.context)
        view.setViewModel(viewModel);
        return CommentsViewHolder(view)
    }
    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        val comment = comments[position]
        holder.bind(comment)
    }
    override fun getItemCount(): Int {
        return comments.size
    }

    inner class CommentsViewHolder(itemView: CommentItemComponent) : RecyclerView.ViewHolder(itemView) {
        private val avt: CircleImageView = itemView.findViewById(R.id.img_comment_user_avt)
        private val tvUsername: TextView = itemView.findViewById(R.id.tv_comment_creator)
        private val tvCreatedAt: TextView = itemView.findViewById(R.id.tv_comment_createAt)
        private val tvContent: TextView = itemView.findViewById(R.id.tv_comment_content)
        private val tvHeartNumber: TextView = itemView.findViewById(R.id.tv_comment_heart_number)
        private val tvReply: TextView = itemView.findViewById(R.id.tv_reply)
        private val inputContainer:LinearLayout = itemView.findViewById(R.id.input_bar_container)
        private val sendBtn: ImageView = itemView.findViewById(R.id.send_btn);
        private val edtText: EditText = itemView.findViewById(R.id.edt_input);
        private val childRec: RecyclerView = itemView.findViewById(R.id.rec_reply_comment);
        fun bind(comment: Comment) {
            tvCreatedAt.text = comment.createdAt?.let { TimeConverter.convertTimestampToString(it) }
            tvUsername.text = comment.userName
            tvContent.text = comment.content
            childRec.layoutManager = LinearLayoutManager(itemView.context);
            //get reply comments
            viewModel.getReplyComments(comment.commentId ){
                (itemView as CommentItemComponent).setReplyComments(it as MutableList<Comment>)
            }
            if (!comment.userAvatar.isNullOrEmpty()) {
                Picasso.get().load(comment.userAvatar).into(avt)
            }
            tvReply.setOnClickListener {
                inputContainer.visibility = if(inputContainer.visibility == View.VISIBLE) View.GONE else View.VISIBLE
            }
            sendBtn.setOnClickListener {
                val replyComment = Comment(commentId ="", songId = comment.songId, content = edtText.text.toString(), replyToCommentId = comment.commentId)
                viewModel.uploadReplyComment(replyComment){
                    (itemView as CommentItemComponent).addReplyComments(it);
                }
                edtText.setText("");
            }
        }
    }
}
