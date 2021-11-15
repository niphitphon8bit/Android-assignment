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

class RestaurantDetailRecyclerAdapter(fragmentActivity: FragmentActivity, val dataSource: JSONArray) :
    RecyclerView.Adapter<RestaurantDetailRecyclerAdapter.Holder>() {

    private val thisContext: Context = fragmentActivity.baseContext
    private val thisActivity = fragmentActivity

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        private val View = view

        lateinit var layout: ConstraintLayout
        lateinit var foodDetailNameView: TextView
        lateinit var foodDetailPriceView: TextView
        lateinit var image: ImageView

        fun Holder() {
            layout = View.findViewById<View>(R.id.restaurant_detail_recycler_view_layout) as ConstraintLayout
            foodDetailNameView = View.findViewById<View>(R.id.food_detail_name) as TextView
            foodDetailPriceView = View.findViewById<View>(R.id.food_detail_price) as TextView
            image = View.findViewById<View>(R.id.food_detail_img) as ImageView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(R.layout.rest_detail_recy_layout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return dataSource.length()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.Holder()

        holder.foodDetailNameView.setText(
            dataSource.getJSONObject(position).getString("name").toString()
        )
        holder.foodDetailPriceView.setText(
            dataSource.getJSONObject(position).getString("price").toString()
        )

        Glide.with(thisContext)
            .load(dataSource.getJSONObject(position).getString("image_url").toString())
            .into(holder.image)

    }


}
