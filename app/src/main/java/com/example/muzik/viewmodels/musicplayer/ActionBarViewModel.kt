package com.example.muzik.viewmodels.musicplayer

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.muzik.data.models.Comment
import com.example.muzik.data.models.User
import com.example.muzik.data.repositories.ReactionRepository
import java.time.LocalDate

class ActionBarViewModel: ViewModel() {
    private val _isShowComments = MutableLiveData(false)
    private val _comments = MutableLiveData<MutableList<Comment>>(mutableListOf())
    private val _isFavorite = MutableLiveData(false)
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
    private val user =  User("1212","first","last", "displayName","male","18",official = false, avatar = "")
    fun uploadComment(){
        isLoading.value = true;
        val comment = Comment("",songId = "3Wj9MsZv9nLwsmj75A7w", createdAt = LocalDate.now().toString(),
            content =  commentContent.get()!!, userName = user.displayName, userId = user.userId, userAvatar = user.avatar,
            modifiedAt = ""
        );
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
        replyComment.createdAt = LocalDate.now().toString();
        replyComment.userName =user.displayName
        replyComment.userId =user.userId
        replyComment.userAvatar =user.avatar
        ReactionRepository.instance!!.uploadComment(replyComment, onSuccess);
    }
    fun loadComments(songId:String) {
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
        _isFavorite.value = !_isFavorite.value!!;
    }
}