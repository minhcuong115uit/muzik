package com.example.muzik.viewmodels.musicplayer

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muzik.data.models.Comment
import com.example.muzik.data.models.Song
import com.example.muzik.data.models.User
import com.example.muzik.data.repositories.ReactionRepository
import com.example.muzik.viewmodels.authentication.AuthViewModel
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import java.time.LocalDate

class ActionBarViewModel: ViewModel() {
    private val _isShowComments = MutableLiveData(false)
    private val _comments = MutableLiveData<MutableList<Comment>>(mutableListOf())
    private val _isFavorite = MutableLiveData(false)
    private val user = AuthViewModel.getUser()
    private var currentSongId: String?  = null
    var isLoading = MutableLiveData<Boolean>(false)
    var commentContent = ObservableField<String>();
    val isFavourite: LiveData<Boolean>
        get(){
            return _isFavorite
        }
    val comments: LiveData<MutableList<Comment>>
        get() = _comments
    val isShowComments: LiveData<Boolean>
        get(){
            return _isShowComments
        }
    fun handleToggleShowComments(){
        _isShowComments.value = !_isShowComments.value!!
    }

    fun setCurrentSongId (songId: String){
        this.currentSongId = songId
        loadComments(currentSongId!!);
    }
    fun getDefaultFavouriteValue(songId: String, userId: String) {
        viewModelScope.launch {
            _isFavorite.value = ReactionRepository.instance?.getFavouriteValue(songId, userId)
            // Xử lý kết quả trả về
        }
    }
    fun uploadComment(){
        Log.e("COMMENT CURRENT SONG ID",currentSongId.toString());
        if(currentSongId?.isEmpty() == true){
            return
        }
        isLoading.value = true;
        val comment = Comment("",songId = currentSongId!!, createdAt = Timestamp.now(),
            content =  commentContent.get()!!, userName = user!!.displayName, userId = user.userId, userAvatar = user.avatar
        )
        try{
            ReactionRepository.instance!!.uploadComment(comment);
            commentContent.set("")
            _comments.value?.add(comment);
            isLoading.value = false;

        }catch(err: Exception){
            Log.e("COMMENT",err.toString());
            isLoading.value = false;

        }
    }
    fun uploadReplyComment(replyComment: Comment, onSuccess: (Comment) -> Unit){
        replyComment.createdAt = Timestamp.now()
        replyComment.userName =user!!.displayName
        replyComment.userId =user.userId
        replyComment.userAvatar =user.avatar
        ReactionRepository.instance!!.uploadComment(replyComment, onSuccess);
    }
    private fun loadComments(songId:String) {
        isLoading.value = true;
        _comments.value?.clear();
        ReactionRepository.instance!!.getComments (songId,{
                cmts->
            cmts.forEach{comment ->
                _comments.value?.add(comment)
            }
            isLoading.value = false;
        }){
                e->
            Log.e("COMMENT", "Error getting comments", e)
            isLoading.value = false;
        }
    }
    fun getReplyComments(commentId:String, onSuccess: (List<Comment>)->Unit) {
        ReactionRepository.instance!!.getReplyComments (commentId,onSuccess)
    }
    fun handleToggleHeart() {
        currentSongId?.let { ReactionRepository.instance?.sendReactToSong(it,AuthViewModel.getUser()!!.userId){
            _isFavorite.value = !_isFavorite.value!!;
        } }
    }

}