package com.example.livestreamusingmvvm.utils

import android.content.Context
import android.os.SystemClock
import android.widget.TextView
import org.webrtc.VideoFrame
import org.webrtc.VideoSink

class FrameStatsRenderer(
    private val context: Context,
    private val textView: TextView
) : VideoSink {

    private var lastTimestamp: Long = 0L

    override fun onFrame(frame: VideoFrame?) {
        if (frame != null) {
            val currentTimestamp = SystemClock.elapsedRealtime()
            val latency = currentTimestamp - (lastTimestamp.takeIf { it > 0 } ?: currentTimestamp)
            lastTimestamp = currentTimestamp

            val frameSizeBytes = frame.buffer?.let {
                // Dummy estimate for size; real encoded size not available here
                it.width * it.height * 3 / 2 // NV21/YUV420 estimate
            } ?: 0

            val statsText = "Latency: ${latency}ms\nApprox Size: ${frameSizeBytes} bytes"

            textView.post {
                textView.text = statsText
            }
        }
    }
}
