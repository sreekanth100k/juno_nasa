package com.juno.nasa

import android.app.ProgressDialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.id_iv

class PhotoOrVideoPreviewActivity:AppCompatActivity() {

    lateinit var mProgressDialog:ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        setContentView(R.layout.photo_or_video_preview)

        var url: String? = intent.getStringExtra("url")

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
}