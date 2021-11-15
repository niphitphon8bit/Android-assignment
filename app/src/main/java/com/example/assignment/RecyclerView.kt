package com.example.assignment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class RecyclerView : Fragment() {
    private var prettyJson: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recycler_view, container, false)
        // Inflate the layout for this fragment

        GlobalScope.launch(Dispatchers.IO) {
            val url = URL("https://order-plz.herokuapp.com/restaurants.json")
            val httpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.setRequestProperty(
                "Accept",
                "application/json"
            ) // The format of response we want to get from the server
            httpURLConnection.requestMethod = "GET"
            httpURLConnection.doInput = true
            httpURLConnection.doOutput = false
            // Check if the connection is successful
            val responseCode = httpURLConnection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = httpURLConnection.inputStream.bufferedReader()
                    .use { it.readText() }  // defaults to UTF-8
                withContext(Dispatchers.Main) {
//                     Convert raw JSON to pretty JSON using GSON library
                    val tmp = JSONObject(response)

                    val recyclerView: RecyclerView = view.findViewById(R.id.recyclerLayout)

                    val layoutManager: RecyclerView.LayoutManager =
                        LinearLayoutManager(requireActivity().baseContext)
                    recyclerView.layoutManager = layoutManager

                    //ตั้งค่า Adapter
                    val adapter =
                        MyRecyclerAdapter(requireActivity(), tmp.getJSONArray("restaurants"))
                    recyclerView.adapter = adapter

                    return@withContext view
                }

            } else {
                Log.e("HTTPURLCONNECTION_ERROR", responseCode.toString())
            }
        }
        return view
    }

    private fun loadJsonFromAsset(filename: String, context: Context): String? {
        val json: String?

        try {
            val inputStream = context.assets.open(filename)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, Charsets.UTF_8)
        } catch (ex: java.io.IOException) {
            ex.printStackTrace()
            return null
        }

        return json
    }

}