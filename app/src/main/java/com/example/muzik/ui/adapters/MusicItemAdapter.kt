package com.example.muzik.ui.adapters

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.muzik.R
import com.example.muzik.data.models.Song
import com.example.muzik.utils.Formatter
import com.example.muzik.viewmodels.HomeViewModel
import com.example.muzik.viewmodels.LibraryViewModel
import com.example.muzik.viewmodels.musicplayer.PlayerViewModel
import com.squareup.picasso.Picasso

class MusicItemAdapter(
    private val context: Context,
    private val playerViewModel:PlayerViewModel,
    private val list: List<Song> ):
    RecyclerView.Adapter<MusicItemAdapter.ViewHolder>() {
//    private var list: List<Song> =
//        libViewModel?.getLocalListSong() ?: homeViewModel?.getListRemoteSongs() ?: listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(context).inflate(R.layout.item_music_tag,parent,false);
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = list[position];
        holder.itemView.setOnClickListener{
            playerViewModel.getPlaySongListener()?.playSong(position, song)
//            song.mediaItemIndex?.let { it1    -> playerViewModel.getPlaySongListener()?.playSong(it1) };
        }
        holder.bind(song);
    }
    override fun getItemCount(): Int {
        return list.size
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private var imgSong:ImageView;
        private var songName:TextView;
        private var songArtist:TextView;
        private var moreBtn:ImageView
        init {
            imgSong = itemView.findViewById(R.id.music_item_img)
             songName = itemView.findViewById(R.id.music_item_song_name);
             songArtist = itemView.findViewById(R.id.music_item_artist);
             moreBtn = itemView.findViewById(R.id.music_item_more);

        }

        fun bind(song: Song){
            songName.text = song.name;
            songArtist.text = Formatter.convertArrArtistToString(song.artist);
            if(!song.imageUri.isEmpty()){
                Picasso.get().load(song.imageUri).into(imgSong)
            }
            moreBtn.setOnClickListener {
                playerViewModel.getShowBottomSheetMusic()?.showBottomSheet(song)
            }
        }
    }
}