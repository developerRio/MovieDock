package com.originalstocks.moviedock.Activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.StringRequest
import com.originalstocks.moviedock.Adapter.AllMoviesAdapter
import com.originalstocks.moviedock.Model.Movies
import com.originalstocks.moviedock.R
import com.originalstocks.moviedock.Utils.MySingleton
import com.originalstocks.moviedock.Utils.MyUtils
import org.json.JSONArray
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val TAG = "ViewAllMoviesResponse"
    private var staggeredGridLayoutManager: StaggeredGridLayoutManager? = null
    private var gridRecyclerView: RecyclerView? = null
    private val moviesList = ArrayList<Movies>()
    private var directorsList: MutableList<String>? = null
    private var genresList: MutableList<String>? = null
    private var actorsList: MutableList<String>? = null
    private var moviesAdapter: AllMoviesAdapter? = null
    private var animationController: LayoutAnimationController? = null
    private var mSearchView: SearchView? = null
    private var mSpinner: Spinner? = null
    private var mProgressBar: ProgressBar? = null
    private val emptyMovieList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        mProgressBar = findViewById(R.id.progress_circular_bar)
        mProgressBar?.visibility = View.VISIBLE
        mSpinner = findViewById(R.id.sort_spinner)

        if (MyUtils.isNetworkAvailable()) {
            loadMoviesData()
        } else {
            Toast.makeText(this, "Please check your Internet Connection", Toast.LENGTH_LONG).show()
        }

        emptyMovieList.add("NA")
    }// onCreate Closes

    private fun sortByTittle() {
        moviesList.sortWith(Comparator { obj1: Movies, obj2: Movies ->
            obj1.tittle!!.compareTo(obj2.tittle.toString(), true) // To sort alphabetically
        })
    }

    private fun sortByRating() {
        moviesList.sortWith(Comparator { obj1: Movies, obj2: Movies ->
            obj2.rating!!.compareTo(
                obj1.rating.toString(),
                true
            ) // To sort numerically Descending order
        })
    }

    private fun sortByYear() {
        moviesList.sortWith(Comparator { obj1: Movies, obj2: Movies ->
            obj1.year!!.compareTo(obj2.year.toString(), true) // To sort numerically Ascending order
        })
    }

    private fun loadMoviesData() {

        val url = "http://test.terasol.in/moviedata.json"

        val rootStringRequest = StringRequest(Request.Method.GET, url,
            Response.Listener { response ->
                try {
                    mProgressBar?.visibility = View.GONE

                    val rootArray = JSONArray(response)

                    for (i in 0 until rootArray.length()) {
                        val movieListJSON = rootArray.getJSONObject(i)

                        val movies = Movies(i)
                        movies.year = (movieListJSON.getString("year"))
                        movies.tittle = (movieListJSON.getString("title"))

                        val year = movieListJSON.getString("year")
                        val tittle = movieListJSON.getString("title")

                        val infoObject = movieListJSON.getJSONObject("info")
                        if (!infoObject.isNull("release_date")) {
                            movies.releaseDate = (infoObject.getString("release_date"))
                            val releaseDate = infoObject.getString("release_date")

                            // Log.i(TAG, "onResponse: " + releaseDate);
                        } else {
                            movies.releaseDate = ("Not Available")
                        }
                        if (!infoObject.isNull("rating")) {
                            movies.rating = (infoObject.getString("rating"))
                            val rating = infoObject.getString("rating")
                            // Log.i(TAG, "onResponse: " + rating);
                        } else {
                            movies.rating = ("Not Available")
                        }

                        if (!infoObject.isNull("image_url")) {
                            movies.imageUrl = (infoObject.getString("image_url"))
                            val ImageUrl = infoObject.getString("image_url")
                            // Log.i(TAG, "onResponse: " + ImageUrl);

                        } else {
                            movies.imageUrl = ((R.drawable.mountain).toString())
                        }
                        if (!infoObject.isNull("plot")) {
                            movies.plotDescription = (infoObject.getString("plot"))
                            val plot = infoObject.getString("plot")
                            // Log.i(TAG, "onResponse: " + plot);

                        } else {
                            movies.plotDescription = ("Not Available")
                        }
                        if (!infoObject.isNull("rank")) {
                            movies.rank = (infoObject.getString("rank"))
                            val Rank = infoObject.getString("rank")
                            // Log.i(TAG, "onResponse: " + Rank);
                        } else {
                            movies.rank = ("Not Available")
                        }

                        if (!infoObject.isNull("running_time_secs")) {
                            movies.duration = (infoObject.getString("running_time_secs"))
                            movies.duration =
                                (setTimeInMin(infoObject.getString("running_time_secs")))
                            val duration = infoObject.getString("running_time_secs")
                            //  Log.i(TAG, "onResponse: " + duration);

                        } else {
                            movies.duration = ("150+")
                        }

                        if (!infoObject.isNull("directors")) {
                            val directorsArray = infoObject.getJSONArray("directors")
                            for (j in 0 until directorsArray.length()) {
                                val directors = directorsArray.getString(j)
                                Log.i(TAG, "onResponse: Directors: $directors")
                                directorsList = ArrayList()
                                directorsList?.add(directors)
                                //  Log.i(TAG, "onResponse: DirectorsList: " + directorsList);
                                movies.directors = (directorsList)

                            }
                        } else {
                            movies.directors = emptyMovieList
                        }

                        if (!infoObject.isNull("genres")) {
                            val genresArray = infoObject.getJSONArray("genres")
                            for (j in 0 until genresArray.length()) {
                                val genres = genresArray.getString(j)
                                genresList = ArrayList()
                                genresList?.add(genres)
                                // Log.i(TAG, "onResponse: DirectorsList: " + genresList);
                                movies.genres = (genresList)
                            }
                        } else {
                            movies.genres = (emptyMovieList)
                        }

                        if (!infoObject.isNull("actors")) {
                            val actorsArray = infoObject.getJSONArray("actors")
                            for (j in 0 until actorsArray.length()) {
                                val actors = actorsArray.getString(j)
                                //  Log.i(TAG, "onResponse: Actors: " + actors);
                                actorsList = ArrayList()
                                actorsList?.add(actors)
                                //  Log.i(TAG, "onResponse: DirectorsList: " + actorsList);
                                movies.actors = (actorsList)
                            }
                        } else {
                            movies.actors = (emptyMovieList)
                        }

                        moviesList.add(movies)
                    }

                    setupRecyclerContent(moviesList)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { error ->
                if (error != null) {
                    mProgressBar?.visibility = View.INVISIBLE
                    Log.e(TAG, "onErrorResponseMovies: " + error.message)
                } else {
                    Log.e(TAG, "onErrorResponseMovies: Error is null")
                }
            })

        MySingleton.getInstance(this).addToRequestQueue(rootStringRequest)

        val cacheRequest = CacheRequest(0, url,
            Response.Listener { response ->
                try {

                    val rootArray = JSONArray(response)
                    for (i in 0 until rootArray.length()) {
                        val movieListJSON = rootArray.getJSONObject(i)

                        val movies = Movies(i)
                        movies.year = (movieListJSON.getString("year"))
                        movies.tittle = (movieListJSON.getString("title"))

                        val year = movieListJSON.getString("year")
                        val tittle = movieListJSON.getString("title")

                        val infoObject = movieListJSON.getJSONObject("info")
                        if (!infoObject.isNull("release_date")) {
                            movies.releaseDate = (infoObject.getString("release_date"))
                            val releaseDate = infoObject.getString("release_date")

                            // Log.i(TAG, "onResponse: " + releaseDate);
                        } else {
                            movies.releaseDate = ("Not Available")
                        }
                        if (!infoObject.isNull("rating")) {
                            movies.rating = (infoObject.getString("rating"))
                            val rating = infoObject.getString("rating")
                            // Log.i(TAG, "onResponse: " + rating);
                        } else {
                            movies.rating = ("Not Available")
                        }

                        if (!infoObject.isNull("image_url")) {
                            movies.imageUrl = (infoObject.getString("image_url"))
                            val ImageUrl = infoObject.getString("image_url")
                            // Log.i(TAG, "onResponse: " + ImageUrl);

                        } else {
                            movies.imageUrl = ((R.drawable.mountain).toString())
                        }
                        if (!infoObject.isNull("plot")) {
                            movies.plotDescription = (infoObject.getString("plot"))
                            val plot = infoObject.getString("plot")
                            // Log.i(TAG, "onResponse: " + plot);

                        } else {
                            movies.plotDescription = ("Not Available")
                        }
                        if (!infoObject.isNull("rank")) {
                            movies.rank = (infoObject.getString("rank"))
                            val Rank = infoObject.getString("rank")
                            // Log.i(TAG, "onResponse: " + Rank);
                        } else {
                            movies.rank = ("Not Available")
                        }

                        if (!infoObject.isNull("running_time_secs")) {
                            movies.duration = (infoObject.getString("running_time_secs"))
                            movies.duration =
                                (setTimeInMin(infoObject.getString("running_time_secs")))
                            val duration = infoObject.getString("running_time_secs")
                            //  Log.i(TAG, "onResponse: " + duration);

                        } else {
                            movies.duration = ("150+")
                        }

                        if (!infoObject.isNull("directors")) {
                            val directorsArray = infoObject.getJSONArray("directors")
                            for (j in 0 until directorsArray.length()) {
                                val directors = directorsArray.getString(j)
                                Log.i(TAG, "onResponse: Directors: $directors")
                                directorsList = ArrayList()
                                directorsList?.add(directors)
                                //  Log.i(TAG, "onResponse: DirectorsList: " + directorsList);
                                movies.directors = (directorsList)

                            }
                        } else {
                            movies.directors = emptyMovieList
                        }

                        if (!infoObject.isNull("genres")) {
                            val genresArray = infoObject.getJSONArray("genres")
                            for (j in 0 until genresArray.length()) {
                                val genres = genresArray.getString(j)
                                genresList = ArrayList()
                                genresList?.add(genres)
                                // Log.i(TAG, "onResponse: DirectorsList: " + genresList);
                                movies.genres = (genresList)
                            }
                        } else {
                            movies.genres = (emptyMovieList)
                        }

                        if (!infoObject.isNull("actors")) {
                            val actorsArray = infoObject.getJSONArray("actors")
                            for (j in 0 until actorsArray.length()) {
                                val actors = actorsArray.getString(j)
                                //  Log.i(TAG, "onResponse: Actors: " + actors);
                                actorsList = ArrayList()
                                actorsList?.add(actors)
                                //  Log.i(TAG, "onResponse: DirectorsList: " + actorsList);
                                movies.actors = (actorsList)
                            }
                        } else {
                            movies.actors = (emptyMovieList)
                        }

                        moviesList.add(movies)

                    }
                    setupRecyclerContent(moviesList)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                Log.e(TAG, "onErrorResponseMovies :  " + error.message)

                Toast.makeText(this@MainActivity, error.message, Toast.LENGTH_SHORT).show()
            })
        MySingleton.getInstance(this).addToRequestQueue(cacheRequest)
    }

    private fun setTimeInMin(duration: String): String {
        val minutes = TimeUnit.SECONDS.toMinutes(java.lang.Long.parseLong(duration))
        return minutes.toString()
    }

    private fun filter(myMovieList: List<Movies>, query: String): List<Movies> {
        var query = query
        query = query.toLowerCase()

        val filteredMovieList = ArrayList<Movies>()
        for (model in myMovieList) {
            val filterTittle = model.tittle?.toLowerCase()
            val filterRating = model.rating?.toLowerCase()
            val filterYear = model.year?.toLowerCase()
            val filterGenres = model.genres?.get(0)
            val filterDirectors = model.directors?.get(0)

            if (filterTittle!!.startsWith(query)
                || filterRating!!.startsWith(query)
                || filterYear!!.startsWith(query)
                || filterGenres!!.startsWith(query)
                || filterDirectors!!.startsWith(query)
            ) {
                filteredMovieList.add(model)
            }
        }
        return filteredMovieList
    }

    private fun setupRecyclerContent(moviesList: MutableList<Movies>) {

        val adapter = ArrayAdapter.createFromResource(
            this@MainActivity,
            R.array.status, android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mSpinner?.adapter = adapter

        mSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {

                val selectedItem = mSpinner?.selectedItem.toString()
                // Toast.makeText(MainActivity.this, "Selected : " + selectedItem, Toast.LENGTH_SHORT).show();

                if (position == 1) {
                    sortByYear()
                } else if (position == 2) {
                    sortByTittle()
                } else if (position == 0) {
                    sortByRating()
                }
                moviesAdapter?.notifyDataSetChanged()

            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        animationController =
            AnimationUtils.loadLayoutAnimation(this,
                R.anim.layout_slide_fom_bottom_effect
            )
        mSearchView = findViewById(R.id.search_view_movies)

        moviesAdapter = AllMoviesAdapter(this, moviesList)

        gridRecyclerView = findViewById(R.id.all_movies_recycler)
        gridRecyclerView?.setHasFixedSize(true)
        // Staggered Layout
        staggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        staggeredGridLayoutManager?.spanCount = 2
        gridRecyclerView?.layoutManager = staggeredGridLayoutManager
        gridRecyclerView?.adapter = moviesAdapter
        gridRecyclerView?.layoutAnimation = animationController
        gridRecyclerView?.adapter!!.notifyDataSetChanged()
        gridRecyclerView?.scheduleLayoutAnimation()

        mSearchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                val filteredModelList = filter(moviesList, newText)
                moviesAdapter?.setSearchFilters(filteredModelList)
                return true
            }
        })
    }

    private inner class CacheRequest(
        method: Int,
        url: String,
        private val mListener: Response.Listener<NetworkResponse>,
        private val mErrorListener: Response.ErrorListener
    ) : Request<NetworkResponse>(method, url, mErrorListener) {

        override fun parseNetworkResponse(response: NetworkResponse): Response<NetworkResponse> {
            var cacheEntry: Cache.Entry? = HttpHeaderParser.parseCacheHeaders(response)
            if (cacheEntry == null) {
                cacheEntry = Cache.Entry()
            }
            val cacheHitButRefreshed = (3 * 60 * 1000).toLong() // in 3 minutes cache will be hit, but also refreshed on background
            val cacheExpired = (24 * 60 * 60 * 1000).toLong() // in 24 hours this cache entry expires completely
            val now = System.currentTimeMillis()
            val softExpire = now + cacheHitButRefreshed
            val ttl = now + cacheExpired
            cacheEntry.data = response.data
            cacheEntry.softTtl = softExpire
            cacheEntry.ttl = ttl
            var headerValue: String?
            headerValue = response.headers["Date"]
            if (headerValue != null) {
                cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue)
            }
            headerValue = response.headers["Last-Modified"]
            if (headerValue != null) {
                cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue)
            }
            cacheEntry.responseHeaders = response.headers
            return Response.success(response, cacheEntry)
        }

        override fun deliverResponse(response: NetworkResponse) {
            mListener.onResponse(response)
        }

        override fun deliverError(error: VolleyError) {
            mErrorListener.onErrorResponse(error)
        }
    }


}
