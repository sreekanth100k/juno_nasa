package com.juno.nasa

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.id_iv
import kotlinx.android.synthetic.main.photo_or_video_preview.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class MainActivity : AppCompatActivity() {

    lateinit var mProgressDialog:ProgressDialog
    var mIsDatePickerDialogObjShowing:Boolean = false

    fun getVideoIdFromYoutubeURL(url:String):String {
        var pattern:String = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";

        var compiledPattern:Pattern = Pattern.compile (pattern)
        var matcher:Matcher = compiledPattern.matcher (url) //url is youtube url for which you want to extract the id.
        if (matcher.find()) {
            return matcher.group()
        }

        return ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()


        id_outer_most_cl.visibility = View.INVISIBLE
        mProgressDialog = ProgressDialog(this)
        mProgressDialog.setMessage("Please Wait....")
        mProgressDialog.show()

        NetworkService.getInstance().jsonApi.fetchApiResponse().enqueue(object:Callback<TestResponse>{
            override fun onResponse(call: Call<TestResponse>, response: Response<TestResponse>) {

                mProgressDialog.dismiss()

                val response: TestResponse? = response.body()
                Log.d("Response",response.toString())

                var explanation:String  = response?.explanation.toString()
                var date:String         = response?.date.toString()
                var mediaType:String    = response?.media_type.toString()
                var title:String        = response?.title.toString()
                var hdUrl:String        = response?.hdurl.toString()
                var url:String          = response?.url.toString()


                id_outer_most_cl.visibility = View.VISIBLE
                /*Set the values*/
                id_title_tv.setText(title)
//                id_description_tv.setText(explanation)
                id_description_tv.loadData("<p style=\"text-align: justify\">"+ explanation +"</p>", "text/html", "UTF-8");

//                id_description_tv.setMovementMethod(ScrollingMovementMethod())
                id_title_tv.setMovementMethod(ScrollingMovementMethod())

                if(mediaType == "image"){
                    val url = URL(hdUrl)

                    id_iv.visibility = View.GONE
//                    id_pb_instead_of_iv.visibility = View.VISIBLE
                    id_play_or_zoom_btn.setBackgroundResource(R.drawable.ic_magnifier_glass)


                    Glide.with(this@MainActivity)
                        .load(url)
                        .listener(object : RequestListener<Drawable> {
                            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                id_iv.visibility = View.VISIBLE
                                id_pb_instead_of_iv.visibility = View.GONE
                                return false
                            }

                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                return false
                            }
                        })
                        .into(id_iv)


                    id_play_or_zoom_btn.setOnClickListener(View.OnClickListener {

                            val intent = Intent(this@MainActivity, PhotoOrVideoPreviewActivity::class.java)
                            intent.putExtra("url",hdUrl)
                            intent.putExtra("photoOrVideo","photo")
                            startActivity(intent);

                            overridePendingTransition( R.anim.slide_in_from_right_bottom, R.anim.no_animation);

                        })

                }else if (mediaType == "video") {

                    id_play_or_zoom_btn.setBackgroundResource(android.R.drawable.ic_media_play)

                    var urlFirstPart:String = "https://img.youtube.com/vi/";
                    var urlVideoId:String   = getVideoIdFromYoutubeURL(url);
                    var urlLastPart:String  = "/maxresdefault.jpg";

                    var urlPhoto:String          =   urlFirstPart+urlVideoId+urlLastPart

                    Glide.with(this@MainActivity)
                        .load(urlPhoto)
                        .listener(object : RequestListener<Drawable> {
                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                id_iv.visibility = View.VISIBLE
                                id_pb_instead_of_iv.visibility = View.GONE
                                return false
                            }

                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                id_iv.visibility = View.VISIBLE
                                id_pb_instead_of_iv.visibility = View.GONE
                                return false
                            }
                        })
                        .into(id_iv)

                    id_play_or_zoom_btn.setOnClickListener(View.OnClickListener {
                        val intent = Intent(
                            this@MainActivity,
                            PhotoOrVideoPreviewActivity::class.java
                        )
                        intent.putExtra("url", url)
                        intent.putExtra("photoOrVideo", "video")
                        startActivity(intent);

                        overridePendingTransition(
                            R.anim.slide_in_from_right_bottom,
                            R.anim.no_animation
                        );
                    })


                }

                if(response == null){
                    id_title_tv.setText("Error")
                    id_iv.visibility                = View.GONE
                    id_pb_instead_of_iv.visibility  = View.GONE
//                    id_description_tv.setText("Error")
                }
            }

            override fun onFailure(call: Call<TestResponse>, t: Throwable) {
            }
        });


        ic_calendar_icon.setOnClickListener(View.OnClickListener{
            if(!mIsDatePickerDialogObjShowing) {
                val c = java.util.Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)
                val datePickerDialogObj = DatePickerDialog(
                    this,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->


                        mIsDatePickerDialogObjShowing = false;
                        view.visibility = View.GONE
                        var monthOfYearInMM: String = ""
                        if (monthOfYear.toString().length == 1) {
                            monthOfYearInMM = "0" + (monthOfYear+1).toString()
                        } else {
                            monthOfYearInMM = (monthOfYear+1).toString()
                        }

                        var dayOfMonthInDD: String = ""
                        if (dayOfMonth.toString().length == 1) {
                            dayOfMonthInDD = "0" + dayOfMonth.toString()
                        } else {
                            dayOfMonthInDD = dayOfMonth.toString()
                        }


                        var date: String =
                            year.toString() + "-" + monthOfYearInMM + "-" + dayOfMonthInDD;


                        mProgressDialog.show()

                        NetworkService.getInstance().jsonApi.fetchApiResponseForDate(date)
                            .enqueue(object : Callback<TestResponse> {
                                override fun onResponse(
                                    call: Call<TestResponse>,
                                    response: Response<TestResponse>
                                ) {

                                    mProgressDialog.dismiss()

                                    val response: TestResponse? = response.body()
                                    Log.d("Response", response.toString())




                                    var explanation: String =   response?.explanation.toString()
                                    var date: String        =   response?.date.toString()
                                    var mediaType: String   =   response?.media_type.toString()
                                    var title: String       =   response?.title.toString()
                                    var hdUrl: String       =   response?.hdurl.toString()
                                    var url:String          =   response?.url.toString()



                                    id_outer_most_cl.visibility = View.VISIBLE
                                    /*Set the values*/
                                    id_title_tv.setText(title)
//                                    id_description_tv.setText(explanation)
//                                    id_description_tv.setMovementMethod(ScrollingMovementMethod())
                                    id_title_tv.setMovementMethod(ScrollingMovementMethod())

                                    if (mediaType == "image") {
                                        id_play_or_zoom_btn.setBackgroundResource(R.drawable.ic_magnifier_glass)

                                        val url = URL(hdUrl)
                                        id_iv.visibility = View.GONE
                                        id_pb_instead_of_iv.visibility = View.VISIBLE

                                        Glide.with(this@MainActivity)
                                            .load(url)
                                            .listener(object : RequestListener<Drawable> {
                                                override fun onResourceReady(
                                                    resource: Drawable?,
                                                    model: Any?,
                                                    target: Target<Drawable>?,
                                                    dataSource: DataSource?,
                                                    isFirstResource: Boolean
                                                ): Boolean {
                                                    id_iv.visibility = View.VISIBLE
                                                    id_pb_instead_of_iv.visibility = View.GONE
                                                    return false
                                                }

                                                override fun onLoadFailed(
                                                    e: GlideException?,
                                                    model: Any?,
                                                    target: Target<Drawable>?,
                                                    isFirstResource: Boolean
                                                ): Boolean {
                                                    id_iv.visibility = View.VISIBLE
                                                    id_pb_instead_of_iv.visibility = View.GONE
                                                    return false
                                                }
                                            })
                                            .into(id_iv)


                                        id_play_or_zoom_btn.setOnClickListener(View.OnClickListener {

                                            val intent = Intent(
                                                this@MainActivity,
                                                PhotoOrVideoPreviewActivity::class.java
                                            )
                                            intent.putExtra("url", hdUrl)
                                            intent.putExtra("photoOrVideo", "photo")
                                            startActivity(intent);

                                            overridePendingTransition(
                                                R.anim.slide_in_from_right_bottom,
                                                R.anim.no_animation
                                            );

                                        })

                                    } else if (mediaType == "video") {

                                        id_play_or_zoom_btn.setBackgroundResource(android.R.drawable.ic_media_play)

                                        var urlFirstPart:String = "https://img.youtube.com/vi/";
                                        var urlVideoId:String   = getVideoIdFromYoutubeURL(url);
                                        var urlLastPart:String  = "/maxresdefault.jpg";

                                        var urlPhoto:String          =   urlFirstPart+urlVideoId+urlLastPart

                                        Glide.with(this@MainActivity)
                                            .load(urlPhoto)
                                            .listener(object : RequestListener<Drawable> {
                                                override fun onResourceReady(
                                                    resource: Drawable?,
                                                    model: Any?,
                                                    target: Target<Drawable>?,
                                                    dataSource: DataSource?,
                                                    isFirstResource: Boolean
                                                ): Boolean {
                                                    id_iv.visibility = View.VISIBLE
                                                    id_pb_instead_of_iv.visibility = View.GONE
                                                    return false
                                                }

                                                override fun onLoadFailed(
                                                    e: GlideException?,
                                                    model: Any?,
                                                    target: Target<Drawable>?,
                                                    isFirstResource: Boolean
                                                ): Boolean {
                                                    id_iv.visibility = View.VISIBLE
                                                    id_pb_instead_of_iv.visibility = View.GONE
                                                    return false
                                                }
                                            })
                                            .into(id_iv)




                                        id_play_or_zoom_btn.setOnClickListener(View.OnClickListener {
                                            val intent = Intent(
                                                this@MainActivity,
                                                PhotoOrVideoPreviewActivity::class.java
                                            )
                                            intent.putExtra("url", url)
                                            intent.putExtra("photoOrVideo", "video")
                                            startActivity(intent);

                                            overridePendingTransition(
                                                R.anim.slide_in_from_right_bottom,
                                                R.anim.no_animation
                                            );

                                        })
                                    }

                                        if(response == null){
                                            id_title_tv.setText("Error")
                                            id_iv.visibility                = View.GONE
                                            id_pb_instead_of_iv.visibility  = View.GONE
//                                            id_description_tv.setText("Error")
                                        }
                                }

                                override fun onFailure(call: Call<TestResponse>, t: Throwable) {
                                }
                            });

                    },
                    year,
                    month,
                    day
                )

                datePickerDialogObj.setCanceledOnTouchOutside(false);

                datePickerDialogObj.setOnDismissListener(DialogInterface.OnDismissListener {
                    mIsDatePickerDialogObjShowing = false;
                })

                datePickerDialogObj.show()
            }
        })
    }
}