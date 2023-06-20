package com.example.muzik.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.muzik.R
import com.example.muzik.databinding.FragmentLibraryBinding
import com.example.muzik.ui.activities.MainActivity
import com.example.muzik.ui.adapters.MusicItemAdapter
import com.example.muzik.ui.adapters.PlaylistItemAdapter
import com.example.muzik.viewmodels.LibraryViewModel
import com.example.muzik.viewmodels.musicplayer.PlayerViewModel


private const val REQUEST_PERMISSION_CODE = 1
class Library : Fragment() {
    private lateinit var musicListAdapter: MusicItemAdapter;
    private lateinit var playlistAdapter: PlaylistItemAdapter;
//    private val libraryViewmodel: PlayerViewModel by lazy {
//        ViewModelProvider(requireActivity())[PlayerViewModel::class.java]
//    }
    private lateinit var libraryViewmodel: LibraryViewModel
    private val playerViewModel: PlayerViewModel by activityViewModels()
    private lateinit var binding: FragmentLibraryBinding;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //get permission to device storage
        libraryViewmodel = ViewModelProvider(requireActivity())[LibraryViewModel::class.java]
        requestPermission()

        binding =  DataBindingUtil.inflate(inflater,R.layout.fragment_library, container,false);

        //tạm thời set detail playlist = local list song
//        libraryViewmodel.setPlaylistItems(libraryViewmodel.getLocalListSong())

        libraryViewmodel.setPlaySongListener(requireActivity() as MainActivity)

        //set local song playlist
        playerViewModel.setMediaPlaylist(playerViewModel.getListSong().filter { song->song.isLocalSong })

        playlistAdapter = PlaylistItemAdapter(requireActivity(),libraryViewmodel);
        binding.viewmodel = libraryViewmodel;
        binding.context = requireActivity() as MainActivity?
        binding.recPlayList.adapter = playlistAdapter;
        libraryViewmodel.notifyChange.observe(viewLifecycleOwner){
            playlistAdapter.notifyDataSetChanged()
        }
        musicListAdapter = MusicItemAdapter(requireActivity(), playerViewModel, playerViewModel.getListSong().filter { song -> song.isLocalSong });
        binding.recDeviceSongs.adapter = musicListAdapter;
        setObservation();
        return binding.root
    }
    fun requestPermission(){
// Check if the permission is granted
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_PERMISSION_CODE)
        } else {
            // Permission is already granted, proceed to read the files
            activity?.let { libraryViewmodel.getLocalMp3Files(it.applicationContext) };
        }

    }
    // Handle the permission request result
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed to read the files
                activity?.let { libraryViewmodel.getLocalMp3Files(it.applicationContext) }
            } else {
                // Permission denied, handle accordingly (e.g., show an error message)
            }
        }
    }
    private fun setObservation(){
        playerViewModel.isGettingSongs.observe(viewLifecycleOwner){isGettingSongs->
            if(!isGettingSongs){

            }
            else {
            }
        }
    }
}