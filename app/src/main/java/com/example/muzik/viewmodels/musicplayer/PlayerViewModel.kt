package com.example.muzik.viewmodels.musicplayer

import android.util.Log
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
    var commentContent = MutableLiveData<String>();
    val currentUser = FirebaseAuth.getInstance().currentUser
    val user =  User("1212","first","last", "displayName","male","18",official = false, avatarUrl = "")
    val comment = Comment(
        songId = "123456", // ID của bài hát liên quan đến comment
        createdAt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()), // thời gian tạo comment
        modifiedAt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()), // thời gian chỉnh sửa comment
        content = "This is a comment", // nội dung của comment
        user = user,
        hearts = listOf<UUID>()
    )
    private val _comments = mutableListOf<Comment>(comment);
    val comments: List<Comment>
        get() = _comments
    private val _isFavorite = MutableLiveData(false)
    val isFavourite: LiveData<Boolean>
        get(){
            return _isFavorite
        }
    private val _isShowComments = MutableLiveData(false)
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
        val comment = Comment("", createdAt = LocalDate.now().toString(),
            content = commentContent.value!!, user = user,
            modifiedAt = "", hearts = listOf<UUID>()
        );
        try{
            ReactionRepository.instance!!.uploadComment(comment);
            commentContent.value = "";
        }catch(err: Exception){
            Log.e("COMMENT",err.toString());
        }

    }
}