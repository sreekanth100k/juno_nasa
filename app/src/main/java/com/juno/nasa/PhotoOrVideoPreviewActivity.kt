package com.juno.nasa

import android.app.ProgressDialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
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

        supportActionBar?.hide()


        setContentView(com.juno.nasa.R.layout.photo_or_video_preview_activity)

        var url: String?         = intent.getStringExtra("url")
        var photoOrVideo:String? = intent.getStringExtra("photoOrVideo")

        if(photoOrVideo.equals("photo")){

            mProgressDialog = ProgressDialog(this)
            mProgressDialog.setMessage("Loading...")
            mProgressDialog.show()
            mProgressDialog.setCanceledOnTouchOutside(false)
            mProgressDialog.setCancelable(false)


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


            var youtubeWebView: WebView  = findViewById<View>(com.juno.nasa.R.id.id_video_view) as WebView

            youtubeWebView.setWebViewClient(object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView,
                    url: String
                ): Boolean {
                    return false
                }
            })

            val webSettings = youtubeWebView.settings
            webSettings.javaScriptEnabled = true
            webSettings.loadWithOverviewMode = true
            webSettings.useWideViewPort = true

            var urlAsString:String = url as String
            youtubeWebView.loadUrl(urlAsString)

        }
    }


    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition( com.juno.nasa.R.anim.no_animation, com.juno.nasa.R.anim.slide_out_from_left_top );

    }
}