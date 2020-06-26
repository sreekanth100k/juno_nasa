package com.juno.nasa

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL


class MainActivity : AppCompatActivity() {

    lateinit var mProgressDialog:ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        id_outer_most_cl.visibility = View.INVISIBLE
        mProgressDialog = ProgressDialog(this)
        mProgressDialog.setMessage("Please Wait....")
        mProgressDialog.show()

        NetworkService.getInstance().jsonApi.fetchApiResponse().enqueue(object:Callback<TestResponse>{
            override fun onResponse(call: Call<TestResponse>, response: Response<TestResponse>) {



                val response: TestResponse? = response.body()
                Log.d("Response",response.toString())

                var explanation:String  = response?.explanation.toString()
                var date:String         = response?.date.toString()
                var mediaType:String    = response?.media_type.toString()
                var title:String        = response?.title.toString()
                var hdUrl:String        = response?.hdurl.toString()


                id_outer_most_cl.visibility = View.VISIBLE
                /*Set the values*/
                id_title_tv.setText(title)
                id_description_tv.setText(explanation)
                id_description_tv.setMovementMethod(ScrollingMovementMethod())
                id_title_tv.setMovementMethod(ScrollingMovementMethod())

                if(mediaType == "image"){
                    val url = URL(hdUrl)

                    Glide.with(this@MainActivity)
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

                        id_play_or_zoom_btn.setOnClickListener(View.OnClickListener {

                            val intent = Intent(this@MainActivity, PhotoOrVideoPreviewActivity::class.java)
                            intent.putExtra("url",hdUrl)
                            startActivity(intent);

//                            overridePendingTransition( R.anim.slide_in_right, R.anim.slide_out_left );

                        })

                }else if(mediaType == "video"){

                }
            }

            override fun onFailure(call: Call<TestResponse>, t: Throwable) {
            }
        });





//        id_play_or_zoom_btn.setOnClickListener(View.OnClickListener {
//
//
//
//        })
//
//        ic_calendar_icon.setOnClickListener(View.OnClickListener{
//            val c       =   java.util.Calendar.getInstance()
//            val year    =   c.get(Calendar.YEAR)
//            val month   =   c.get(Calendar.MONTH)
//            val day     =   c.get(Calendar.DAY_OF_MONTH)
//            val dpd     =   DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
//
//
//            }, year, month, day)
//
//            dpd.show()
//        })
    }
}