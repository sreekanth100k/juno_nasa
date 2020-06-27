package com.juno.nasa

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import android.widget.Toast
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

        setContentView(R.layout.photo_or_video_preview)

        var url: String?         = intent.getStringExtra("url")
        var photoOrVideo:String? = intent.getStringExtra("photoOrVideo")



        if(photoOrVideo.equals("photo")){
            Toast.makeText(this,"Photo",Toast.LENGTH_SHORT).show()
        }else if(photoOrVideo.equals("video")){
            Toast.makeText(this,"Video",Toast.LENGTH_SHORT).show()

            id_iv.visibility = View.GONE

//            val videoView = findViewById<View>(R.id.id_video_view) as VideoView
//            val controller = MediaController(this)
//            videoView.setVideoURI(Uri.parse("https://www.youtube.com/watch?v=4-BiDcw4CjY"))
//            videoView.setMediaController(controller)
//            videoView.requestFocus();
//            videoView.start()

            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=4-BiDcw4CjY"))
            startActivity(intent)
        }


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
    }


    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition( R.anim.no_animation, R.anim.slide_out_from_left_top );

    }
}