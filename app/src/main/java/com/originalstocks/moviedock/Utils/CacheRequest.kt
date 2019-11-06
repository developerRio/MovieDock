package com.originalstocks.moviedock.Utils

import com.android.volley.Cache
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.HttpHeaderParser

class CacheRequest(
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
        val cacheHitButRefreshed =
            (3 * 60 * 1000).toLong() // in 3 minutes cache will be hit, but also refreshed on background
        val cacheExpired =
            (24 * 60 * 60 * 1000).toLong() // in 24 hours this cache entry expires completely
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

    override fun parseNetworkError(volleyError: VolleyError): VolleyError {
        return super.parseNetworkError(volleyError)
    }

    override fun deliverError(error: VolleyError) {
        mErrorListener.onErrorResponse(error)
    }
}