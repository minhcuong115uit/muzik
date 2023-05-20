package com.example.muzik.ui.adapters

import android.content.Context
import android.media.Image
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
import com.example.muzik.viewmodels.musicplayer.PlayerViewModel

class MusicItemAdapter(private val context: Context, private val playerViewModel: PlayerViewModel):
    RecyclerView.Adapter<MusicItemAdapter.ViewHolder>() {


    private var list: List<Song> = playerViewModel.getLocalListSong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(context).inflate(R.layout.item_music_tag,parent,false);
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = list[position];
        holder.itemView.setOnClickListener{
            Log.e("ClickListener","Item Clicked")
            val musicPlayerFragment = MusicPlayer();
            val fragmentManager = (context as MainActivity).supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.setCustomAnimations(R.anim.slide_up,R.anim.slide_down,R.anim.slide_up, R.anim.slide_down)
                .addToBackStack("Player")
            .replace(R.id.music_player_fragment, musicPlayerFragment).commit()
            playerViewModel.playSong(position);
        }
        holder.bind(song);
    }
    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
//        val imgSong = itemView.findViewById<ImageView>(R.id.)

        private var songName:TextView;
        private var songArtist:TextView;
        private var moreBtn:ImageView
        init {
             songName = itemView.findViewById(R.id.music_item_song_name);
             songArtist = itemView.findViewById(R.id.music_item_artist);
            moreBtn = itemView.findViewById<ImageView>(R.id.music_item_more);
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