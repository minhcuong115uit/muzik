package com.example.muzik.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.muzik.R
import com.example.muzik.data.models.Playlist
import com.example.muzik.data.models.Song
import com.example.muzik.databinding.FragmentDetailPlaylistBinding
import com.example.muzik.ui.adapters.DetailPlaylistItemAdapter
import com.example.muzik.viewmodels.LibraryViewModel
import com.example.muzik.viewmodels.authentication.AuthViewModel
import com.example.muzik.viewmodels.musicplayer.PlayerViewModel


class DetailPlaylist : Fragment() {
    private lateinit var binding: FragmentDetailPlaylistBinding
    private lateinit var adapter: DetailPlaylistItemAdapter
    private val libraryViewModel: LibraryViewModel by activityViewModels()
    private val playerViewModel: PlayerViewModel by activityViewModels()
    private var playlist:Playlist? =null
    private lateinit var playlistSongs:MutableList<Song>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_detail_playlist, container, false);

        playlistSongs =  playerViewModel.getSongByIds(
            playlist?.songIds as List<String>
        ) as MutableList<Song>

        adapter = DetailPlaylistItemAdapter(requireContext(),playerViewModel, libraryViewModel,
            playlist!!,playlistSongs);
        playerViewModel.setMediaPlaylist(playlistSongs)

        binding.recDetailPlaylist.adapter = adapter

        libraryViewModel.notifyChange.observe(viewLifecycleOwner){
            val list = playerViewModel.getSongByIds(
                playlist?.songIds as List<String>
            ) as MutableList<Song>
            playlistSongs.clear()
            playlistSongs.addAll(list)
            adapter.notifyDataSetChanged()
        }

        if(playlist!=null){
            binding.tvNameDetailPlaylist.text = playlist!!.name
//            binding.tvOwnerDetailPlaylist.text = AuthViewModel.getUser()?.displayName
            binding.tvOwnerDetailPlaylist.setOnClickListener {
                if(playlistSongs.size >0){
                    playerViewModel.playSong(0, playlistSongs[0])
                    playerViewModel.getActionPlayerListener()
                }
            }
        }

        return binding.root
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setOnTouchListener { view, _ -> return@setOnTouchListener true }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            playlist = it.getParcelable<Playlist>(PLAYLIST);
        }
    }
    companion object {
        const val PLAYLIST = "Playlist"
        @JvmStatic
        fun newInstance( playlist: Playlist): DetailPlaylist {
            val fragment = DetailPlaylist()
            val args = Bundle().apply {
                putParcelable(PLAYLIST, playlist)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onPause() {
        super.onPause()
        playerViewModel.setMediaPlaylist(playerViewModel.getListLocalSong())
        Log.d("Player ", "set local songs to media playlist")
    }
}