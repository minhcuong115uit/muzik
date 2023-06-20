package com.example.muzik.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.TransitionInflater
import android.transition.TransitionManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.muzik.R
import com.example.muzik.data.models.Comment
import com.example.muzik.databinding.FragmentCommentsBinding
import com.example.muzik.ui.adapters.CommentsAdapter
import com.example.muzik.viewmodels.musicplayer.ActionBarViewModel
import com.example.muzik.viewmodels.musicplayer.PlayerViewModel


class Comments : Fragment() {
    lateinit var binding: FragmentCommentsBinding
    lateinit var adapter: CommentsAdapter
    private lateinit var songId:String
    private val viewModel: ActionBarViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            songId = it.getString(BottomActionsBar.SONG_ID).toString()

        }
    }
    companion object {
        const val SONG_ID = "SongId"
        @JvmStatic
        fun newInstance(songId: String): Comments {
            val fragment = Comments()
            val args = Bundle().apply {
                putString(SONG_ID, songId)
            }
            fragment.arguments = args
            return fragment
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        adapter = CommentsAdapter(requireActivity(),viewModel)
        binding = FragmentCommentsBinding.inflate(inflater, container,false);
        binding.viewmodel = viewModel;
        binding.recCommentList.adapter = adapter;
        viewModel.comments.observe(viewLifecycleOwner) { cmts: List<Comment> ->
            if (cmts.isNotEmpty()) {
                binding.tvEmpty.visibility = View.GONE
            }
            adapter.notifyDataSetChanged()
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            val commentCount = viewModel.comments.value?.size;
            if(commentCount != 0 ){
                binding.tvEmpty.visibility = View.GONE
                binding.recCommentList.scrollToPosition(commentCount!!.minus(1) );
            }
            else {
                binding.tvEmpty.visibility =  View.VISIBLE
            }
        }
        // textInput animation when user click to it
        binding.edtInput.setOnFocusChangeListener { _, hasFocus ->
            val transition = TransitionInflater.from(requireContext())
                .inflateTransition(R.transition.transition_comment_bar)
            TransitionManager.beginDelayedTransition(binding.root as ViewGroup, transition)
            if (hasFocus) {
                binding.userCommentAvt.visibility = View.GONE
                val params = binding.edtInput.layoutParams as LinearLayout.LayoutParams
                params.width = LinearLayout.LayoutParams.MATCH_PARENT - 40;
                binding.edtInput.layoutParams = params
            } else {
                binding.userCommentAvt.visibility = View.VISIBLE
                val params = binding.edtInput.layoutParams as LinearLayout.LayoutParams
                params.width = resources.getDimensionPixelSize(R.dimen.comment_input_width)
                binding.edtInput.layoutParams = params
            }
        }
        binding.closeComments.setOnClickListener(View.OnClickListener {
             parentFragmentManager.popBackStack()

        })
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //set isShowcomment = false
        viewModel.handleToggleShowComments()
    }
}