package com.example.muzik.ui.custom_components

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.muzik.R
import com.example.muzik.data.models.Comment
import com.example.muzik.databinding.CommentItemBinding
import com.example.muzik.ui.adapters.ReplyCommentAdapter
import com.example.muzik.viewmodels.musicplayer.ActionBarViewModel
import de.hdodenhof.circleimageview.CircleImageView

class CommentItemComponent : LinearLayout {
    private lateinit var viewModel: ActionBarViewModel
    private lateinit var binding: CommentItemBinding;
    lateinit var adapter: ReplyCommentAdapter;
    var replyCommentList: MutableList<Comment> = mutableListOf()
    constructor(context: Context) : super(context) {
        initialize()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initialize()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialize()
    }

    private fun initialize() {
        binding = CommentItemBinding.inflate(LayoutInflater.from(context), this, true)
        adapter = ReplyCommentAdapter(context,replyCommentList);
        binding.recReplyComment.adapter = adapter
    }

    fun setViewModel(viewModel: ActionBarViewModel) {
        this.viewModel = viewModel
    }

    fun setReplyComments(list:MutableList<Comment>){
        replyCommentList.clear();
        replyCommentList.addAll(list);
        adapter.notifyDataSetChanged();
    }
    fun addReplyComments(comment: Comment){
        replyCommentList.add(comment);
        adapter.notifyDataSetChanged()
    }
    override fun onFinishInflate() {
        super.onFinishInflate()
    }
}
