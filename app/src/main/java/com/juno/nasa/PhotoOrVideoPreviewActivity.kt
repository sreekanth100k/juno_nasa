package com.juno.nasa

import android.R
import android.app.ProgressDialog
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*


class PhotoOrVideoPreviewActivity:AppCompatActivity() {

    lateinit var mProgressDialog:ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(com.juno.nasa.R.layout.photo_or_video_preview)

        var url: String?         = intent.getStringExtra("url")
        var photoOrVideo:String? = intent.getStringExtra("photoOrVideo")

        if(photoOrVideo.equals("photo")){

            mProgressDialog = ProgressDialog(this)
            mProgressDialog.setMessage("Loading...")
            mProgressDialog.show()


            Glide.with(this)
                .load(url)
                .listener(object : RequestListener<Drawable> {
                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        mProgressDialog.dismiss()
                        return false
                    }

                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        mProgressDialog.dismiss()
                        return false
                    }
                })
                .into(id_iv)
        }else if(photoOrVideo.equals("video")){

            id_iv.visibility = View.GONE



            val videoView       =   findViewById<View>(com.juno.nasa.R.id.id_video_view) as VideoView
            val mediaController =   MediaController(this)
            mediaController.setAnchorView(videoView)
            val uri             =   Uri.parse(url)
            videoView.setMediaController(mediaController)
            videoView.setVideoURI(uri)
            videoView.requestFocus()
            videoView.start()

        }

    }


    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition( com.juno.nasa.R.anim.no_animation, com.juno.nasa.R.anim.slide_out_from_left_top );

    }
}