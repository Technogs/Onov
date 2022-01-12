package com.application.onovapplication.repository


/**
 * Object to keep settings such as URL parts, regions, s3 bucket names.
 */
object BaseUrl {

    private const val PROTOCOL_HTTPS = "http://"

    //  private const val PRODUCTION_URL = "hourlylancer.com/mobile/onov/api/v1/"
    private const val PRODUCTION_URL = "bdztl.com/onov/api/v1/"
    private const val IMAGE_URL = "https://bdztl.com/onov/"
    const val GOOGlE_CLOUD_URL = "https://fcm.googleapis.com/"


    val apiBaseUrl: String
        get() = "$PROTOCOL_HTTPS$PRODUCTION_URL"

    val photoUrl: String
        get() = IMAGE_URL

}
