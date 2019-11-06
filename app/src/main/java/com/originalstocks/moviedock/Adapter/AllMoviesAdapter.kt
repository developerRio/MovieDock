package com.originalstocks.moviedock.Adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.originalstocks.moviedock.Fragments.DetailsFragment
import com.originalstocks.moviedock.Model.Movies
import com.originalstocks.moviedock.R
import java.util.*

class AllMoviesAdapter(private val context: Context, private var moviesList: MutableList<Movies>) :
    RecyclerView.Adapter<AllMoviesAdapter.AllMoviesViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): AllMoviesViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.all_movies_item, viewGroup, false)
        return AllMoviesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AllMoviesViewHolder, pos: Int) {

        val movies = moviesList!![pos]

        holder.movieName.text = movies.tittle
        holder.movieDuration.text = movies.duration!! + " minutes"
        Glide.with(context).load(movies.imageUrl)
            .apply(RequestOptions().placeholder(R.drawable.mountain))
            .into(holder.imagePoster)

        holder.mCardView.setOnClickListener { v ->
            val pos = holder.adapterPosition

            val detailsIntent = Bundle()
            for (i in 0..pos) {
                detailsIntent.putString("image_url", moviesList!![pos].imageUrl)
                detailsIntent.putString("name", moviesList!![pos].tittle)
                detailsIntent.putString("rating", moviesList!![pos].rating)
                detailsIntent.putString("duration", moviesList!![pos].duration)
                detailsIntent.putString("description", moviesList!![pos].plotDescription)
                detailsIntent.putString("year", moviesList!![pos].year)
                detailsIntent.putString("release_date", moviesList!![pos].releaseDate)
                detailsIntent.putStringArrayList(
                    "genres",
                    moviesList!![pos].genres as ArrayList<String>?
                )
                detailsIntent.putStringArrayList(
                    "actors",
                    moviesList!![pos].actors as ArrayList<String>?
                )
                detailsIntent.putStringArrayList(
                    "directors",
                    moviesList!![pos].directors as ArrayList<String>?
                )

            }
            // Sending data to fragment
            val detailsFragment = DetailsFragment()
            detailsFragment.arguments = detailsIntent
            val activity = v.context as AppCompatActivity
            activity.supportFragmentManager
                .beginTransaction()
                .add(R.id.frame_frag, detailsFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun getItemCount(): Int {
        return moviesList!!.size
    }

    fun setSearchFilters(list: List<Movies>) {
        moviesList = ArrayList()
        moviesList.addAll(list)
        notifyDataSetChanged()
    }


    inner class AllMoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var imagePoster: ImageView = itemView.findViewById(R.id.all_image_poster)
        internal var movieName: TextView = itemView.findViewById(R.id.all_movie_name_text)
        internal var movieDuration: TextView = itemView.findViewById(R.id.all_duration_text)
        internal var mCardView: CardView = itemView.findViewById(R.id.all_movies_card)

    }
}
