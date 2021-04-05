package com.application.onovapplication.repository


/**
 * Object to keep settings such as URL parts, regions, s3 bucket names.
 */
object BaseUrl {

    private const val PROTOCOL_HTTPS = "http://"
    private const val PRODUCTION_URL = "hourlylancer.com/devlop/onov/api/v1/"
    private const val IMAGE_URL = "http://hourlylancer.com/devlop/onov/"


    val apiBaseUrl: String
        get() = "$PROTOCOL_HTTPS$PRODUCTION_URL"

    val photoUrl: String
        get() = IMAGE_URL

}
