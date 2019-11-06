package com.originalstocks.moviedock.Fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.originalstocks.moviedock.R

/**
 * A simple [Fragment] subclass.
 */
class DetailsFragment : Fragment() {

    private var movieName: TextView? = null
    private var movieRating: TextView? = null
    private var movieDuration: TextView? = null
    private var movieDescription: TextView? = null
    private var movieReleaseDate: TextView? = null
    private var movieYear: TextView? = null
    private var movieGenres: TextView? = null
    private var moviesActors: TextView? = null
    private var movieDirectors: TextView? = null
    private var posterImageView: ImageView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_details, container, false)

        posterImageView = view.findViewById(R.id.movie_poster)
        movieName = view.findViewById(R.id.detail_movie_name)
        movieRating = view.findViewById(R.id.detail_movie_rating)
        movieDuration = view.findViewById(R.id.detail_movie_duration)
        movieDescription = view.findViewById(R.id.detail_movie_description)
        movieReleaseDate = view.findViewById(R.id.detail_movie_release_date)
        movieYear = view.findViewById(R.id.detail_movie_year)
        movieGenres = view.findViewById(R.id.detail_movie_genre)
        moviesActors = view.findViewById(R.id.detail_movie_actors)
        movieDirectors = view.findViewById(R.id.detail_movie_directors)

        val nestedScrollView: View = view.findViewById(R.id.nested_movie_scroll)
        val mSheetBehaviour: BottomSheetBehavior<*> = BottomSheetBehavior.from(nestedScrollView)
        mSheetBehaviour.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        mSheetBehaviour.peekHeight = 280    //peek height


        // getting data from Bundle
        val mData = this.arguments
        if (mData != null) {

            val name = mData.get("name")!!.toString()
            val imageUrl = mData.get("image_url")!!.toString()
            val rating = mData.get("rating")!!.toString()
            val duration = mData.get("duration")!!.toString()
            val description = mData.get("description")!!.toString()
            val year = mData.get("year")!!.toString()
            val releaseDate = mData.get("release_date")!!.toString()

            val genreList = mData.getStringArrayList("genres")
            val actorsList = mData.getStringArrayList("actors")
            val directorsList = mData.getStringArrayList("directors")

             Log.i("Detailed_Values", "onCreate: " + "\n" + name + "\n" + imageUrl + "\n" + rating + "\n" + duration
                        + "\n" + description + "\n" + year + "\n" + releaseDate + "\n" + genreList + "\n" + actorsList + "\n" + directorsList)


            movieName?.text = name
            movieRating?.text = rating
            movieDuration?.text = "$duration mins"
            movieDescription?.text = description
            movieYear?.text = "($year)"
            movieReleaseDate?.text = "Released on : $releaseDate"

            if (genreList != null) {
                movieGenres?.text = ""
                for (i in genreList.indices) {
                    movieGenres?.append(genreList[i])
                }
            } else {
                movieGenres?.append("NA")
            }

            if (actorsList != null) {
                if (!actorsList.isEmpty()) {
                    moviesActors?.text = ""
                    for (i in actorsList.indices) {
                        moviesActors?.append("Actor(s): " + actorsList[i])
                    }
                } else {
                    moviesActors?.append("NA")
                }
            }

            if (directorsList != null) {
                movieDirectors?.text = ""
                for (i in directorsList.indices) {
                    movieDirectors?.append("Director(s): " + directorsList[i])
                }
            } else {
                movieDirectors?.append("NA")
            }

            Glide.with(this).load(imageUrl)
                .apply(RequestOptions().centerCrop().placeholder(R.drawable.mountain))
                .into(posterImageView!!)

        }


        mSheetBehaviour.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

                var state = ""
                when (newState) {
                    BottomSheetBehavior.STATE_DRAGGING -> {
                        mSheetBehaviour.state = BottomSheetBehavior.STATE_DRAGGING
                        state = "DRAGGING"
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                        state = "SETTLING"
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        mSheetBehaviour.state = BottomSheetBehavior.STATE_EXPANDED

                        state = "EXPANDED"
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {

                        state = "COLLAPSED"
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> mSheetBehaviour.setState(
                        BottomSheetBehavior.STATE_HALF_EXPANDED
                    )

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        state = "HIDDEN"
                    }
                }

            }

            override fun onSlide(view: View, v: Float) {

            }
        })

        return view
    }


}
