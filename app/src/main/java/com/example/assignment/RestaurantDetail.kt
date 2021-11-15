package com.example.assignment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class RestaurantDetail : Fragment() {

    private var id: String = ""
    private var name: String = ""
    private var image: String = ""
    private var kind: String = ""
    private var url: String = ""

    fun newInstance(id: String, name: String, image: String, kind: String): RestaurantDetail {

        val fragment = RestaurantDetail()
        val bundle = Bundle()
        bundle.putString("id", id)
        bundle.putString("name", name)
        bundle.putString("image", image)
        bundle.putString("kind", kind)
        fragment.setArguments(bundle)

        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        if (bundle != null) {
            id = bundle.getString("id").toString()
            Log.d("ID",id)
            name = bundle.getString("name").toString()
            image = bundle.getString("image").toString()
            kind = bundle.getString("kind").toString()
            url = "https://order-plz.herokuapp.com/restaurants/${bundle.getString("id").toString()}.json"
            Log.d("URL in bundle?",url)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_restuarant_detail, container, false)

        val imgVi: ImageView = view.findViewById(R.id.rest_detail_img)
//        val id : TextView = view.findViewById(R.id.tv_name)
        val nameTxt: TextView = view.findViewById(R.id.rest_detail_name)
        val kindTxt: TextView = view.findViewById(R.id.rest_detail_kind)

        nameTxt.setText(name)
        kindTxt.setText(kind)
        Glide.with(requireActivity().baseContext)
            .load(image)
            .into(imgVi)


        GlobalScope.launch(Dispatchers.IO) {

            Log.d("URL",url)
            val url = URL(url)
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
                    val str = "{restaurants:$response}"
                    val tmp = JSONObject(str)
                    val rawtemp = tmp.getJSONObject("restaurants").getJSONArray("menus")
                    val recyclerView: RecyclerView = view.findViewById(R.id.RestDetailrecyclerLayout)
                    val layoutManager: RecyclerView.LayoutManager =
                        LinearLayoutManager(requireActivity().baseContext)
                    recyclerView.layoutManager = layoutManager

                    //ตั้งค่า Adapter
                    val adapter =
                        RestaurantDetailRecyclerAdapter(requireActivity(), rawtemp)
                    recyclerView.adapter = adapter

                    return@withContext view
                }

            } else {
                Log.e("HTTPURLCONNECTION_ERROR", responseCode.toString())
            }
        }

        return view
    }
}