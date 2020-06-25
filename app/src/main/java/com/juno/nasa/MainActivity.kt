package com.juno.nasa

import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.text.method.ScrollingMovementMethod
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        NetworkService.getInstance().jsonApi.fetchApiResponse().enqueue(object:Callback<TestResponse>{
            override fun onResponse(call: Call<TestResponse>, response: Response<TestResponse>) {

                val response: TestResponse? = response.body()
                Log.d("Response",response.toString())

                var explanation:String  = response?.explanation.toString()
                var date:String         = response?.date.toString()
                var mediaType:String    = response?.media_type.toString()
                var title:String        = response?.title.toString()
                var hdUrl:String        = response?.hdurl.toString()

                /*Set the values*/
                id_title_tv.setText(title)
                id_description_tv.setText(explanation)
                id_description_tv.setMovementMethod(ScrollingMovementMethod())
                id_title_tv.setMovementMethod(ScrollingMovementMethod())

                if(mediaType == "image"){
                    val url = URL(hdUrl)

                    val SDK_INT = Build.VERSION.SDK_INT
                    if (SDK_INT > 8) {
                        val policy =
                            StrictMode.ThreadPolicy.Builder()
                                .permitAll().build()
                        StrictMode.setThreadPolicy(policy)

                        val bmp =
                            BitmapFactory.decodeStream(url.openConnection().getInputStream())
                        id_iv.setImageBitmap(bmp)
                    }

                }else if(mediaType == "video"){

                }

            }

            override fun onFailure(call: Call<TestResponse>, t: Throwable) {
            }
        });




//        id_play_or_zoom_btn.setOnClickListener(View.OnClickListener {
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