package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    var txtString: TextView? = null

    companion object {
        val JSON = "application/json; charset=utf-8".toMediaType()

        private var POST_URL_AB = "https://ab.staging.okcredit.in/v1/GetProfile"
        private var AB_BODY = """{
                "device_id": "86c20472-5ff6-4d23-b1f6-546bb041b7d3",
                "merchant_id": ""
        }"""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.postOkhttp).setOnClickListener(this)
        findViewById<View>(R.id.postRetrofit).setOnClickListener(this)
        findViewById<View>(R.id.postAsyncTask).setOnClickListener(this)
        txtString = findViewById(R.id.txtString)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.postOkhttp -> try {
                postRequestOkHttp(POST_URL_AB, AB_BODY)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            R.id.postRetrofit -> {
                postRequestRetrofit()
            }
            R.id.postAsyncTask -> {
                postAsyncTask()
            }
        }
    }

    private fun postAsyncTask() {
        val postRequest = AsynTaskPost.POSTRequest()

        postRequest.execute(POST_URL_AB, AB_BODY)
    }

    @Throws(IOException::class)
    fun postRequestOkHttp(postUrl: String, postBody: String) {
        val client = OkHttpClient()
        val body = RequestBody.create(JSON, postBody)
        val request: Request = Request.Builder()
            .url(postUrl)
            .post(body)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                call.cancel()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    try {
                        txtString!!.text = response.body!!.string()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        })
    }


    fun postRequestRetrofit(){
        val apiInterface = ApiInterface.create().getAB(GetProfileRequest(merchant_id = "", device_id = UUID.randomUUID().toString()))

        //apiInterface.enqueue( Callback<List<Movie>>())
        apiInterface.enqueue(object:retrofit2.Callback<GetProfileResponse> {
            override fun onResponse(
                call: retrofit2.Call<GetProfileResponse>,
                response: retrofit2.Response<GetProfileResponse>
            ) {
                try {
                    txtString!!.text = response.body().toString()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: retrofit2.Call<GetProfileResponse>, t: Throwable) {
            }

        })
    }
}