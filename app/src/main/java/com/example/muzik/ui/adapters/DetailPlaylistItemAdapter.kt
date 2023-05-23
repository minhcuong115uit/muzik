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
import com.example.muzik.data.models.Song
import com.example.muzik.ui.activities.MainActivity
import com.example.muzik.ui.fragments.MusicPlayer
import com.example.muzik.viewmodels.musicplayer.LibraryViewModel

class DetailPlaylistItemAdapter(private val context: Context, private val viewmodel:LibraryViewModel):
    RecyclerView.Adapter<DetailPlaylistItemAdapter.ViewHolder>() {


    private var list: MutableList<Song>? = viewmodel.getPlaylistItems().value
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_music_tag,parent,false);
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = list?.get(position);
        holder.itemView.setOnClickListener{
            viewmodel.getPlaySongListener()?.playSong(position);
        }
        song?.let{holder.bind(it)
        }
    }
    override fun getItemCount(): Int {
        return list?.size ?: 0
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
//        val imgSong = itemView.findViewById<ImageView>(R.id.)

        private var songName: TextView;
        private var songArtist: TextView;
        private var moreBtn: ImageView
        init {
            songName = itemView.findViewById(R.id.music_item_song_name);
            songArtist = itemView.findViewById(R.id.music_item_artist);
            moreBtn = itemView.findViewById(R.id.music_item_more);
            moreBtn.setOnClickListener {
                Log.e("ClickListener","txt Artist Clicked")
            }
        }

        fun bind(song: Song){
            songName.text = song.name;
            songArtist.text = song.artistName;
        }
    }
}