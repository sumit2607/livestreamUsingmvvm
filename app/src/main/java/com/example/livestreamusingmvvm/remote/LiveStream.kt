package com.example.livestreamusingmvvm.remote

data class LiveStream(
    val streamId: String,                // Unique stream identifier
    val status: String,                  // Stream status (e.g., "broadcasting")
    val type: String,                    // Type of stream (e.g., "liveStream")
    val publish: Boolean,                // Whether the stream is published
    val date: Long,                      // Date (timestamp in milliseconds)
    val plannedStartDate: Long,          // Planned start date (timestamp)
    val plannedEndDate: Long,            // Planned end date (timestamp)
    val duration: Long,                  // Duration of the stream
    val publicStream: Boolean,           // Whether the stream is public
    val is360: Boolean,                  // Whether it's a 360° stream
    val originAdress: String,            // IP address of the stream origin
    val rtmpURL: String,                 // RTMP URL for the stream
    val zombi: Boolean,                  // Whether the stream is considered a "zombie"
    val hlsViewerCount: Int,             // HLS viewer count
    val dashViewerCount: Int,            // DASH viewer count
    val webRTCViewerCount: Int,          // WebRTC viewer count
    val rtmpViewerCount: Int,            // RTMP viewer count
    val startTime: Long,                 // Stream start time (timestamp)
    val bitrate: Int,                    // Bitrate of the stream
    val width: Int,                      // Width of the video stream
    val height: Int,                     // Height of the video stream
    val dropPacketCountInIngestion: Int, // Number of dropped packets in ingestion
    val dropFrameCountInEncoding: Int,  // Number of dropped frames in encoding
    val packetLostRatio: Float,          // Packet loss ratio
    val packetsLost: Int,                // Total number of lost packets
    val jitterMs: Int,                   // Jitter in milliseconds
    val rttMs: Int,                      // Round trip time in milliseconds
    val userAgent: String,               // User agent (client information)
    val remoteIp: String?,               // Remote IP address (nullable)
    val latitude: Double?,               // Latitude (nullable)
    val longitude: Double?,              // Longitude (nullable)
    val altitude: Double?,               // Altitude (nullable)
    val mainTrackStreamId: String?,      // Main track stream ID (nullable)
    val subTrackStreamIds: List<String>, // List of sub-track stream IDs
    val absoluteStartTimeMs: Long,       // Absolute start time in milliseconds
    val webRTCViewerLimit: Int,          // WebRTC viewer limit
    val hlsViewerLimit: Int,             // HLS viewer limit
    val dashViewerLimit: Int,            // DASH viewer limit
    val subFolder: String?,              // Sub-folder (nullable)
    val currentPlayIndex: Int,           // Current play index
    val playlistLoopEnabled: Boolean,    // Whether playlist loop is enabled
    val updateTime: Long,                // Last update time (timestamp)
    val role: String,                    // Stream role (e.g., "default")
    val hlsParameters: String?,          // HLS parameters (nullable)
    val autoStartStopEnabled: Boolean,   // Whether auto start/stop is enabled
    val encoderSettingsList: List<String>?, // List of encoder settings (nullable)
    val virtual: Boolean,                // Whether the stream is virtual
    val anyoneWatching: Boolean          // Whether anyone is watching the stream
)
