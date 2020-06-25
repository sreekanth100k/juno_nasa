package com.juno.nasa

import android.app.DatePickerDialog
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        NetworkService.getInstance().jsonApi.fetchApiResponse().enqueue(object:Callback<TestResponse>{
            override fun onResponse(call: Call<TestResponse>, response: Response<TestResponse>) {

                val response: TestResponse? = response.body()
                Log.d("Response",response.toString())

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