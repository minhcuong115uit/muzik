package com.example.muzik.ui.adapters

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.awesomedialog.AwesomeDialog
import com.example.awesomedialog.body
import com.example.awesomedialog.icon
import com.example.awesomedialog.onNegative
import com.example.awesomedialog.onPositive
import com.example.awesomedialog.title
import com.example.muzik.R
import com.example.muzik.data.models.Playlist
import com.example.muzik.data.models.Song
import com.example.muzik.ui.activities.MainActivity
import com.example.muzik.ui.fragments.DetailPlaylist
import com.example.muzik.viewmodels.LibraryViewModel
import com.example.muzik.viewmodels.musicplayer.PlayerViewModel

class PlaylistItemAdapter(
    private val context: Context,
    private val viewModel: LibraryViewModel,
    private val song: Song? = null,
    private val playerViewModel: PlayerViewModel? = null,
    private val isItemSheetDialog: Boolean = false
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val VIEW_TYPE_1 = 0
        private const val VIEW_TYPE_2 = 1
    }
    private var list: MutableList<Playlist>? = viewModel.getPlaylist().value
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // Tạo ra ViewHolder tương ứng với kiểu view
         return if (!isItemSheetDialog) {
             val view = LayoutInflater.from(context).inflate(R.layout.playlist_item, parent, false)
             ViewHolder(view)
         }
         else {
                val view = LayoutInflater.from(context).inflate(R.layout.playlist_item_dialog, parent, false)
                ViewHolderSheetDialog(view)
         }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val playlist = list?.get(position);
        holder.itemView.setOnClickListener{
            val detailPlaylistFragment = playlist?.let { it1 -> DetailPlaylist.newInstance(it1) }
            val fragmentManager = (context as MainActivity).supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            if (detailPlaylistFragment != null) {
                fragmentTransaction.setCustomAnimations(R.anim.slide_up,R.anim.slide_down,R.anim.slide_up, R.anim.slide_down)
                    .addToBackStack("DetailFragment")
                    .add(R.id.main_bottom_fragment, detailPlaylistFragment)
                    .commit()
            }
        }
        when (holder) {
            is ViewHolder -> holder.bind(playlist)
            is ViewHolderSheetDialog -> {
                holder.bind(playlist)
                if (playlist != null) {
                    holder.setClickListener(playlist,holder.itemView)
                }
            }
        }
    }
    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    inner class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){

        private var playListName: TextView;
        private var songCount: TextView;
        private var playListNameEdt: EditText;
        private var iconPrivate: ImageView;
        private var playlistImg: ImageView;
        private var deleteBtn: ImageView
        private var renameBtn: ImageView
        private var saveNewNameBtn: ImageView
        init {
            playListName = itemView.findViewById(R.id.playlist_name);
            iconPrivate = itemView.findViewById(R.id.is_private_playlist_icon);
            playlistImg = itemView.findViewById(R.id.playlist_image);
            deleteBtn = itemView.findViewById(R.id.playlist_item_more);
            renameBtn = itemView.findViewById(R.id.playlist_item_ic_pencil);
            playListNameEdt =  itemView.findViewById(R.id.playlist_name_edt);
            saveNewNameBtn =  itemView.findViewById(R.id.playlist_item_ic_check);
            songCount =  itemView.findViewById(R.id.playlist_song_count);
            playListNameEdt.setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus) {
                    playListNameEdt.visibility = View.GONE
                    playListName.visibility = View.VISIBLE
                }
            }

        }

        fun bind(playlist: Playlist?){
            playListName.text = playlist?.name;
            songCount.text = playlist?.songIds!!.size.toString()
            iconPrivate.visibility = if(playlist?.isPrivate == true) View.VISIBLE else View.GONE
//            playlistImg.setImageURI(playlist.playListImageUri)
            deleteBtn.setOnClickListener {
                AwesomeDialog.build(context as Activity)
                    .title("Bạn muốn xoá playlist này?")
                    .body("Không thể hoàn tác được thao tác này")
                    .onPositive("Xoá") {
                        if (playlist != null) {
                            viewModel.deletePlaylist( playlist.playlistId)
                        }
                        Log.d("TAG", "positive ")
                    }
                    .onNegative("Huỷ") {
                        Log.d("TAG", "negative ")
                    }
            }
            renameBtn.setOnClickListener {

                playListName.visibility = View.GONE
                playListNameEdt.visibility = View.VISIBLE
                playListNameEdt.setText(playListName.text)
                playListNameEdt.focusable = View.FOCUSABLE
                playListNameEdt.requestFocus()

                renameBtn.visibility = View.GONE
                saveNewNameBtn.visibility = View.VISIBLE
            }
            saveNewNameBtn.setOnClickListener {
                val newName = playListNameEdt.text.toString()
                viewModel.updatePlaylist(playlist!!.playlistId,"name", newName);
                playListName.visibility = View.VISIBLE
                playListNameEdt.visibility = View.GONE
                playListName.text = playListNameEdt.text
                playListNameEdt.clearFocus();

                renameBtn.visibility = View.VISIBLE
                saveNewNameBtn.visibility = View.GONE
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        // Trả về kiểu view tương ứng với mỗi vị trí
        return if (position % 2 == 0) {
            VIEW_TYPE_1
        } else {
            VIEW_TYPE_2
        }
    }

    inner class ViewHolderSheetDialog(itemView: View) :RecyclerView.ViewHolder(itemView){
        private var playListName: TextView;
        private var songCount: TextView;
        private var iconPrivate: ImageView;
        private var playlistImg: ImageView;
        init {
            playListName = itemView.findViewById(R.id.playlist_name);
            iconPrivate = itemView.findViewById(R.id.is_private_playlist_icon);
            playlistImg = itemView.findViewById(R.id.playlist_image);
            songCount =  itemView.findViewById(R.id.playlist_song_count);
        }

        fun bind(playlist: Playlist?){
            playListName.text = playlist?.name;
            songCount.text = playlist?.songIds!!.size.toString()
            iconPrivate.visibility = if(playlist?.isPrivate == true) View.VISIBLE else View.GONE
//            playlistImg.setImageURI(playlist.playListImageUri)
        }
        fun setClickListener(playlist: Playlist,view :View){
            view.setOnClickListener {
                Toast.makeText(context, "Successfully", Toast.LENGTH_SHORT).show()
                song?.let { it1 -> viewModel.addSongToPlaylist(playlist.playlistId, it1.songId) }
                playerViewModel?.getShowBottomSheetMusic()?.closePlaylistSheet()
            }
        }
    }
}