package com.example.livestreamusingmvvm.ui.adapter
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.livestreamusingmvvm.databinding.ItemLiveStreamBinding
import com.example.livestreamusingmvvm.remote.LiveStream
import com.example.livestreamusingmvvm.ui.LiveStreamPlayerActivity


class LiveStreamAdapter(private var mContext :  Context, private val onClick: (LiveStream) -> Unit) :
    ListAdapter<LiveStream, LiveStreamAdapter.LiveStreamViewHolder>(LiveStreamDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LiveStreamViewHolder {
        // Inflate the item layout using View Binding
        val binding = ItemLiveStreamBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LiveStreamViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LiveStreamViewHolder, position: Int) {
        // Bind the data to the ViewHolder
        val stream = getItem(position)
        holder.bind(stream)
    }

    inner class LiveStreamViewHolder(private val binding: ItemLiveStreamBinding) : RecyclerView.ViewHolder(binding.root) {

        private val context = itemView.context
     //   private var player: SimpleExoPlayer? = null

        fun bind(stream: LiveStream) {
            // Set stream title and status using View Binding
            binding.nameTv.text = stream.streamId
            binding.statusTv.text = stream.status

//            // Initialize ExoPlayer for the video preview
//            player = SimpleExoPlayer.Builder(context).build()
//            binding.playerView.player = player
//
//            // Set the video URL for the preview (stream URL or RTMP URL)
//            val mediaItem = MediaItem.fromUri(stream.rtmpURL)
//            Log.d("TAG", "binddsdsdd: "  + "here in line no " + stream.rtmpURL)
//            player?.setMediaItem(mediaItem)
//            player?.prepare()

            // Set the Play button click listener
            binding.playButton.setOnClickListener {
                // Pass the stream to the onClick listener to handle playback
               // onClick(stream)
                val intent = Intent(mContext, LiveStreamPlayerActivity::class.java)
                intent.putExtra("streamId", "your_stream_id") // Pass streamId
                mContext.startActivity(intent)
            }

            // Handle lifecycle events for ExoPlayer (Release when view is destroyed)
            itemView.setOnDetachFromWindowListener {

            }
        }

        private fun View.setOnDetachFromWindowListener(onDetach: () -> Unit) {
            this.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    if (!this@LiveStreamViewHolder.binding.root.isAttachedToWindow) {
                        onDetach.invoke()
                    }
                    return true
                }
            })
        }
    }

    // DiffUtil callback to efficiently update the list
    class LiveStreamDiffCallback : DiffUtil.ItemCallback<LiveStream>() {
        override fun areItemsTheSame(oldItem: LiveStream, newItem: LiveStream): Boolean {
            return oldItem.streamId == newItem.streamId
        }

        override fun areContentsTheSame(oldItem: LiveStream, newItem: LiveStream): Boolean {
            return oldItem == newItem
        }
    }
}
