package com.example.muzik.ui.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.muzik.R
import com.example.muzik.data.models.Playlist
import com.example.muzik.data.models.Song
import com.example.muzik.utils.Formatter
import com.example.muzik.viewmodels.LibraryViewModel
import com.example.muzik.viewmodels.musicplayer.PlayerViewModel
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class DetailPlaylistItemAdapter(private val context: Context,
                                private val playerViewModel: PlayerViewModel,
                                private val libraryViewModel: LibraryViewModel,
                                private val playlist: Playlist,
                                private val list: MutableList<Song>):
    RecyclerView.Adapter<DetailPlaylistItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_music_tag,parent,false);
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val song = list?.get(position);
        holder.itemView.setOnClickListener{

            if (song != null) {
                playerViewModel.getPlaySongListener()?.playSong(position,song)
            };
        }
        song?.let{holder.bind(it)
        }
    }
    override fun getItemCount(): Int {
        return list?.size ?: 0
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private var songName: TextView;
        private var imgSong: CircleImageView;
        private var songArtist: TextView;
        private var moreBtn: ImageView
        private var deleteBtn: ImageView
        init {
            songName = itemView.findViewById(R.id.music_item_song_name);
            songArtist = itemView.findViewById(R.id.music_item_artist);
            moreBtn = itemView.findViewById(R.id.music_item_more);
            imgSong = itemView.findViewById(R.id.music_item_img);
            deleteBtn = itemView.findViewById(R.id.music_item_trash);
        }

        fun bind(song: Song){
            songName.text = song.name;
            moreBtn.visibility = View.GONE
            deleteBtn.visibility = View.VISIBLE
            songArtist.text = Formatter.convertArrArtistToString(song.artist);
            if(song.imageUri.isNotEmpty()){
                Picasso.get().load(song.imageUri).into(imgSong);
            }

            deleteBtn.setOnClickListener {
                libraryViewModel.removeSongFromPlaylist(songId = song.songId, playlistId = playlist.playlistId)
                Toast.makeText(context, "Successfully", Toast.LENGTH_SHORT).show()
            }
        }
    }
}