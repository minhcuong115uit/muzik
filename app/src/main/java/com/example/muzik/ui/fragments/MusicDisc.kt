package com.example.muzik.ui.fragments


import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.example.muzik.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView;
/**
 * A simple [Fragment] subclass.
 * Use the [MusicDisc.newInstance] factory method to
 * create an instance of this fragment.
 */
class MusicDisc : Fragment() {
    private lateinit var circleImageView: CircleImageView
    private lateinit var objectAnimator: ObjectAnimator

    @Nullable
    override fun onCreateView(
        @NonNull inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_music_disc, container, false)
        circleImageView = view.findViewById(R.id.imageviewcircle)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        objectAnimator = ObjectAnimator.ofFloat(circleImageView, "rotation", 0f, 360f)
        objectAnimator.duration = 20000
        objectAnimator.repeatCount = ValueAnimator.INFINITE
        objectAnimator.repeatMode = ValueAnimator.RESTART
        objectAnimator.interpolator = LinearInterpolator()
        objectAnimator.start()
    }
    private fun Play(image: String?) {
        Picasso.get().load(image).into(circleImageView)
    }
    override fun onDestroyView() {
        super.onDestroyView()

        objectAnimator.cancel();
    }
    fun stopAnimation(){
        if(::objectAnimator.isInitialized){
            objectAnimator.pause();
        }
    }
    fun resumeAnimation(){
        if(::objectAnimator.isInitialized){
            objectAnimator.resume();
        }
    }
}