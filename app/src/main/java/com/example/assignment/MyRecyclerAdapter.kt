package com.example.assignment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.json.JSONArray

class MyRecyclerAdapter(fragmentActivity: FragmentActivity, val dataSource: JSONArray) :
    RecyclerView.Adapter<MyRecyclerAdapter.Holder>() {

    private val thisContext: Context = fragmentActivity.baseContext
    private val thisActivity = fragmentActivity

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        private val View = view

        lateinit var layout: ConstraintLayout
        lateinit var titleTextView: TextView
        lateinit var detailTextView: TextView
        lateinit var image: ImageView

        fun Holder() {
            layout = View.findViewById<View>(R.id.recyview_layout) as ConstraintLayout
            titleTextView = View.findViewById<View>(R.id.rest_detail_name) as TextView
            detailTextView = View.findViewById<View>(R.id.rest_detail_price) as TextView
            image = View.findViewById<View>(R.id.imgV) as ImageView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(R.layout.recy_layout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return dataSource.length()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.Holder()

        holder.titleTextView.setText(
            dataSource.getJSONObject(position).getString("name").toString()
        )
        holder.detailTextView.setText(
            dataSource.getJSONObject(position).getString("kind").toString()
        )

        Glide.with(thisContext)
            .load(dataSource.getJSONObject(position).getString("image_url").toString())
            .into(holder.image)

        holder.layout.setOnClickListener {

            Toast.makeText(thisContext, dataSource.getJSONObject(position).getString("id").toString(), Toast.LENGTH_SHORT)
                .show()

            val id = dataSource.getJSONObject(position).getString("id").toString()
            val name = dataSource.getJSONObject(position).getString("name").toString()
            val image = dataSource.getJSONObject(position).getString("image_url").toString()
            val kind = dataSource.getJSONObject(position).getString("kind").toString()
            val fragmentRestaurantDetail = RestaurantDetail().newInstance(id,name,image,kind)
            val fm = thisActivity.supportFragmentManager
        val transaction : FragmentTransaction =  fm.beginTransaction()
        transaction.replace(R.id.layout, fragmentRestaurantDetail,"fragment_member_detail")
        transaction.addToBackStack("fragment_member_detail")
        transaction.commit()
        }


    }


}
