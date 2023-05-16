package com.example.muzik.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.muzik.R
import com.example.muzik.data.models.Song

class MusicItemAdapter(private val context: Context, private val list: List<Song>): RecyclerView.Adapter<MusicItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(context).inflate(R.layout.item_music_tag,parent,false);
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = list[position];
        holder.bind(song);
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
//        val imgSong = itemView.findViewById<ImageView>(R.id.)
        private var songName:TextView;
        private var songArtist:TextView;
        init {
             songName = itemView.findViewById(R.id.music_item_song_name);
             songArtist = itemView.findViewById(R.id.music_item_artist);
        }
        fun bind(song: Song){
            songName.text = song.name;
            songArtist.text = song.artistName;
        }
    }
}