package com.example.muzik.ui.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.muzik.R
import com.example.muzik.data.models.Playlist
import com.example.muzik.data.models.Song
import com.example.muzik.ui.activities.MainActivity
import com.example.muzik.ui.fragments.DetailPlaylist
import com.example.muzik.ui.fragments.MusicPlayer
import com.example.muzik.viewmodels.musicplayer.LibraryViewModel
import com.example.muzik.viewmodels.musicplayer.PlayerViewModel

class PlaylistItemAdapter(private val context: Context, private val viewModel:LibraryViewModel): RecyclerView.Adapter<PlaylistItemAdapter.ViewHolder>() {

    private var list: MutableList<Playlist>? = viewModel.getPlaylist().value

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistItemAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.playlist_item,parent,false);
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistItemAdapter.ViewHolder, position: Int) {
        val playlist = list?.get(position);
        holder.itemView.setOnClickListener{
            val detailPlaylistFragment = DetailPlaylist()
            val fragmentManager = (context as MainActivity).supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.setCustomAnimations(R.anim.slide_up,R.anim.slide_down,R.anim.slide_up, R.anim.slide_down)
                .addToBackStack("DetailFragment")
                .add(R.id.main_bottom_fragment, detailPlaylistFragment)
                .commit()
        }
        holder.bind(playlist);
    }
    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    inner class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){

        private var playListName: TextView;
        private var playlistOwner: TextView;
        private var iconPrivate: ImageView;
        private var playlistImg: ImageView;
        private var moreBtn: ImageView
        init {
            playListName = itemView.findViewById(R.id.playlist_name);
            playlistOwner = itemView.findViewById(R.id.playlist_owner);
            iconPrivate = itemView.findViewById(R.id.is_private_playlist_icon);
            playlistImg = itemView.findViewById(R.id.playlist_image);
            moreBtn = itemView.findViewById(R.id.playlist_item_more);
            moreBtn.setOnClickListener {
                Log.e("ClickListener","more Playlist Clicked")
            }
        }

        fun bind(playlist: Playlist?){
            playListName.text = playlist?.name;
            playlistOwner.text = playlist?.ownerName;
            iconPrivate.visibility = if(playlist?.isPrivate == true) View.VISIBLE else View.GONE
//            playlistImg.setImageURI(playlist.playListImageUri)
        }
    }
}