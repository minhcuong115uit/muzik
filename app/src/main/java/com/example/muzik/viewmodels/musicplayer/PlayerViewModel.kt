package com.example.muzik.viewmodels.musicplayer

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.muzik.data.models.Comment
import com.example.muzik.data.models.User
import com.example.muzik.data.repositories.ReactionRepository
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale
import java.util.UUID

//class PlayerViewModel(private val repo :ReactionRepository): ViewModel() {
class PlayerViewModel(): ViewModel() {
    private lateinit var navController: NavController
    var commentContent = ObservableField<String>();
    val user =  User("1212","first","last", "displayName","male","18",official = false, avatarUrl = "")
    val comment = Comment(
        songId = "3Wj9MsZv9nLwsmj75A7w", // ID của bài hát liên quan đến comment
        createdAt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()), // thời gian tạo comment
        modifiedAt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()), // thời gian chỉnh sửa comment
        content = "This is a comment", // nội dung của comment
        user = user,
        hearts = listOf<UUID>()
    )
    private val _comments = MutableLiveData<MutableList<Comment>>(mutableListOf())

    val comments: LiveData<MutableList<Comment>>
        get() = _comments
    private val _isFavorite = MutableLiveData(false)
    val isFavourite: LiveData<Boolean>
        get(){
            return _isFavorite
        }
    private val _isShowComments = MutableLiveData(false)
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading
    val isShowComments: LiveData<Boolean>
        get(){
            return _isShowComments
        }

    fun setNavController(controller: NavController) {
        navController = controller
    }
    fun getNavController(): NavController {
        return navController
    }
    fun handleToggleHeart() {
        _isFavorite.value = !_isFavorite.value!!;
    }
    fun handleToggleShowComments(){
        _isShowComments.value = !_isShowComments.value!!
    }

    fun uploadComment(){
        _isLoading.value = true;

        val comment = Comment("3Wj9MsZv9nLwsmj75A7w", createdAt = LocalDate.now().toString(),
            content =  commentContent.get()!!, user = user,
            modifiedAt = "", hearts = listOf<UUID>()
        );
        try{
            ReactionRepository.instance!!.uploadComment(comment);
            commentContent.set("")
            _comments.value?.add(comment);
            _isLoading.value = false;

        }catch(err: Exception){
            Log.e("COMMENT",err.toString());
            _isLoading.value = false;

        }
    }
    fun loadComments(songId:String) {
        _isLoading.value = true;
        _comments.value?.clear();
        ReactionRepository.instance!!.getComments (songId,{
            cmts->
            cmts.forEach{comment ->
                _comments.value?.add(comment)
            }
            _isLoading.value = false;
        }){
            e->
            Log.e("COMMENT", "Error getting comments", e)
            _isLoading.value = false;
        }
    }
}