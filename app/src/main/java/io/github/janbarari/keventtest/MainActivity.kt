package io.github.janbarari.keventtest

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.github.janbarari.kevent.KEvent
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val sampleSubscriberGUID = "SSGUID"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        KEvent.register<String>(sampleSubscriberGUID) {

        }

        post_event.setOnClickListener {
            KEvent.post(943)
        }

    }

    override fun onStop() {
        super.onStop()
        KEvent.unregister(sampleSubscriberGUID)
    }

}