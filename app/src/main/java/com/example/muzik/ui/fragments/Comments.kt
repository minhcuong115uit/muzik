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
import androidx.lifecycle.MutableLiveData
import com.example.muzik.R
import com.example.muzik.data.models.Comment
import com.example.muzik.databinding.FragmentCommentsBinding
import com.example.muzik.ui.adapters.CommentsAdapter
import com.example.muzik.viewmodels.musicplayer.PlayerViewModel


class Comments : Fragment() {

    lateinit var binding: FragmentCommentsBinding
    lateinit var adapter: CommentsAdapter
    private val viewModel: PlayerViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        adapter = CommentsAdapter(requireActivity().applicationContext,viewModel.comments.value!!)
        binding = FragmentCommentsBinding.inflate(inflater, container,false);
        viewModel.loadComments("3Wj9MsZv9nLwsmj75A7w");
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
            binding.tvEmpty.visibility = if (viewModel.comments.value?.size == 0) View.VISIBLE else View.GONE

        }
        binding.edtInput.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrEmpty()) {
                binding.sendBtn.visibility = View.INVISIBLE;
//                binding.edtInput.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,null)
            } else {
                binding.sendBtn.visibility = View.VISIBLE;
//                binding.edtInput.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.right_arrow, 0)
            }
        }
        binding.edtInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val transition = TransitionInflater.from(requireContext())
                    .inflateTransition(R.transition.transition_comment_bar)
                TransitionManager.beginDelayedTransition(binding.root as ViewGroup, transition)
                binding.userCommentAvt.visibility = View.GONE
                val params = binding.edtInput.layoutParams as LinearLayout.LayoutParams
                params.width = LinearLayout.LayoutParams.MATCH_PARENT
                binding.edtInput.layoutParams = params
            } else {
                binding.userCommentAvt.visibility = View.VISIBLE
                val params = binding.edtInput.layoutParams as LinearLayout.LayoutParams
                params.width = resources.getDimensionPixelSize(R.dimen.comment_input_width)
                binding.edtInput.layoutParams = params
            }
        }

        binding.closeComments.setOnClickListener(View.OnClickListener {
            viewModel.getNavController().navigateUp()
        })
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.handleToggleShowComments()
    }
}