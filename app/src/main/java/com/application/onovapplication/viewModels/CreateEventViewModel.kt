package com.application.onovapplication.viewModels

//https://blog.cpming.top/p/httpurlconnection-multipart-form-data
import androidx.lifecycle.ViewModel
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import java.util.*

class CreateEventViewModel(var requestURL:String , var chrset :String ):ViewModel() {
    private var boundary: String? = null
    private val LINE_FEED = "\r\n"
    private var httpConn: HttpURLConnection? = null
    private var charset: String? = null
    private var outputStream: OutputStream? = null
    private var writer: PrintWriter? = null

    /**
     * This constructor initializes a new HTTP POST request with content type
     * is set to multipart/form-data
     * @param requestURL
     * @param charset
     * @throws IOException
     */
    @Throws(IOException::class)
    fun MultipartUtility(requestURL: String?, charset: String?) {
        this.charset = charset

        // creates a unique boundary based on time stamp
        boundary = "===" + System.currentTimeMillis() + "==="
        val url = URL(requestURL)
        httpConn = url.openConnection() as HttpURLConnection
        httpConn!!.useCaches = false
        httpConn!!.doOutput = true // indicates POST method
        httpConn!!.doInput = true
        httpConn!!.setRequestProperty(
            "Content-Type",
            "multipart/form-data; boundary=$boundary"
        )
        httpConn!!.setRequestProperty("User-Agent", "CodeJava Agent")
        httpConn!!.setRequestProperty("Test", "Bonjour")
        outputStream = httpConn!!.outputStream
        writer = PrintWriter(
            OutputStreamWriter(outputStream, charset),
            true
        )
    }

    /**
     * Adds a form field to the request
     * @param name field name
     * @param value field value
     */
    fun addFormField(name: String, value: String?) {
        writer!!.append("--$boundary").append(LINE_FEED)
        writer!!.append("Content-Disposition: form-data; name=\"$name\"")
            .append(LINE_FEED)
        writer!!.append("Content-Type: text/plain; charset=$charset").append(
            LINE_FEED
        )
        writer!!.append(LINE_FEED)
        writer!!.append(value).append(LINE_FEED)
        writer!!.flush()
    }

    /**
     * Adds a upload file section to the request
     * @param fieldName name attribute in <input type="file" name="..."></input>
     * @param uploadFile a File to be uploaded
     * @throws IOException
     */
    @Throws(IOException::class)
    fun addFilePart(fieldName: String, uploadFile: File) {
        val fileName = uploadFile.name
        writer!!.append("--$boundary").append(LINE_FEED)
        writer!!.append(
            "Content-Disposition: form-data; name=\"" + fieldName
                    + "\"; filename=\"" + fileName + "\""
        )
            .append(LINE_FEED)
        writer!!.append(
            (
                    "Content-Type: "
                            + URLConnection.guessContentTypeFromName(fileName))
        )
            .append(LINE_FEED)
        writer!!.append("Content-Transfer-Encoding: binary").append(LINE_FEED)
        writer!!.append(LINE_FEED)
        writer!!.flush()
        val inputStream = FileInputStream(uploadFile)
        val buffer = ByteArray(4096)
        var bytesRead = -1
        while ((inputStream.read(buffer).also { bytesRead = it }) != -1) {
            outputStream!!.write(buffer, 0, bytesRead)
        }
        outputStream!!.flush()
        inputStream.close()
        writer!!.append(LINE_FEED)
        writer!!.flush()
    }

    /**
     * Adds a header field to the request.
     * @param name - name of the header field
     * @param value - value of the header field
     */
    fun addHeaderField(name: String, value: String) {
        writer!!.append("$name: $value").append(LINE_FEED)
        writer!!.flush()
    }

    /**
     * Completes the request and receives response from the server.
     * @return a list of Strings as response in case the server returned
     * status OK, otherwise an exception is thrown.
     * @throws IOException
     */
    @Throws(IOException::class)
    fun finish(): List<String?>? {
        val response: MutableList<String?> = ArrayList()
        writer!!.append(LINE_FEED).flush()
        writer!!.append("--$boundary--").append(LINE_FEED)
        writer!!.close()

        // checks server's status code first
        val status = httpConn!!.responseCode
        if (status == HttpURLConnection.HTTP_OK) {
            val reader = BufferedReader(
                InputStreamReader(
                    httpConn!!.inputStream
                )
            )
            var line: String? = null
            while ((reader.readLine().also { line = it }) != null) {
                response.add(line)
            }
            reader.close()
            httpConn!!.disconnect()
        } else {
            throw IOException("Server returned non-OK status: $status")
        }
        return response
    }
}

























//    /**
//     * This constructor initializes a new HTTP POST request with content type
//     * is set to multipart/form-data
//     * @param url
//     * *
//     * @throws IOException
//     */
//    @Throws(IOException::class)
//    constructor(url: URL) {
//
////   object {
//        val LINE_FEED = "\r\n"
//        val maxBufferSize = 1024 * 1024
//        val charset = "UTF-8"
////        }
//
//        // creates a unique boundary based on time stamp
//        val boundary: String = "===" + System.currentTimeMillis() + "==="
//        val httpConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
//        val outputStream: OutputStream
//        val writer: PrintWriter
//
//        fun init (){
//
//            httpConnection.setRequestProperty("Accept-Charset", "UTF-8")
//            httpConnection.setRequestProperty("Connection", "Keep-Alive")
//            httpConnection.setRequestProperty("Cache-Control", "no-cache")
//            httpConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary)
//            httpConnection.setChunkedStreamingMode(maxBufferSize)
//            httpConnection.doInput = true
//            httpConnection.doOutput = true    // indicates POST method
//            httpConnection.useCaches = false
//            outputStream = httpConnection.outputStream
//            writer = PrintWriter(OutputStreamWriter(outputStream, charset), true)
//        }
//
//        /**
//         * Adds a form field to the request
//         * @param name  field name
//         * *
//         * @param value field value
//         */
//        fun addFormField(name: String, value: String) {
//            writer.append("--").append(boundary).append(LINE_FEED)
//            writer.append("Content-Disposition: form-data; name=\"").append(name).append("\"")
//                .append(LINE_FEED)
//            writer.append(LINE_FEED)
//            writer.append(value).append(LINE_FEED)
//            writer.flush()
//        }
//
//        /**
//         * Adds a upload file section to the request
//         * @param fieldName  - name attribute in <input type="file" name="..."></input>
//         * *
//         * @param uploadFile - a File to be uploaded
//         * *
//         * @throws IOException
//         */
//        @Throws(IOException::class)
//        fun addFilePart(fieldName: String, uploadFile: File, fileName: String, fileType: String) {
//            writer.append("--").append(boundary).append(LINE_FEED)
//            writer.append("Content-Disposition: file; name=\"").append(fieldName)
//                .append("\"; filename=\"").append(fileName).append("\"").append(LINE_FEED)
//            writer.append("Content-Type: ").append(fileType).append(LINE_FEED)
//            writer.append(LINE_FEED)
//            writer.flush()
//
//            val inputStream = FileInputStream(uploadFile)
//            inputStream.copyTo(outputStream, maxBufferSize)
//
//            outputStream.flush()
//            inputStream.close()
//            writer.append(LINE_FEED)
//            writer.flush()
//        }
//
//        /**
//         * Adds a header field to the request.
//         * @param name  - name of the header field
//         * *
//         * @param value - value of the header field
//         */
//        fun addHeaderField(name: String, value: String) {
//            writer.append(name + ": " + value).append(LINE_FEED)
//            writer.flush()
//        }
//
//        /**
//         * Upload the file and receive a response from the server.
//         * @param onFileUploadedListener
//         * *
//         * @throws IOException
//         */
//        @Throws(IOException::class)
//        fun upload(onFileUploadedListener: OnFileUploadedListener?) {
//            writer.append(LINE_FEED).flush()
//            writer.append("--").append(boundary).append("--")
//                .append(LINE_FEED)
//            writer.close()
//
//            try {
//                // checks server's status code first
//                val status = httpConnection.responseCode
//                if (status == HttpURLConnection.HTTP_OK) {
//                    val reader = BufferedReader(InputStreamReader(httpConnection
//                        .inputStream))
//                    val response = reader.use(BufferedReader::readText)
//                    httpConnection.disconnect()
//                    onFileUploadedListener?.onFileUploadingSuccess(response)
//                } else {
//                    onFileUploadedListener?.onFileUploadingFailed(status)
//                }
//
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//
//        }
//
//
//
//    }
//    interface OnFileUploadedListener {
//        fun onFileUploadingSuccess(response: String)
//
//        fun onFileUploadingFailed(responseCode: Int)
//    }
