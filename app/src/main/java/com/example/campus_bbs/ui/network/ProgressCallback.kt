package com.example.campus_bbs.ui.network
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.*

class ReqBodyWithProgress (
    private val multipartBody: RequestBody,
    var onUploadProgress : ((progress:Float) -> Unit)? = null
) : RequestBody() {
    private var mCountingSink: CountingSink? = null

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return multipartBody.contentLength()
    }

    override fun contentType(): MediaType? {
        return multipartBody.contentType()
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        mCountingSink = CountingSink(sink)
        val bufferedSink: BufferedSink = mCountingSink!!.buffer()
        multipartBody.writeTo(bufferedSink)
        bufferedSink.flush()
    }


    inner class CountingSink(delegate: Sink?) : ForwardingSink(delegate!!) {
        private var bytesWritten: Long = 0

        @Throws(IOException::class)
        override fun write(source: Buffer, byteCount: Long) {
            bytesWritten += byteCount

            //This will invoke twice if logging intercept is enable
            onUploadProgress?.invoke((bytesWritten.toFloat() / contentLength().toFloat()))
//            println("progress" +  (100f * bytesWritten / contentLength()).toInt())
            super.write(source, byteCount)
            delegate.flush() // I have added this line to manually flush the sink
        }
    }


}