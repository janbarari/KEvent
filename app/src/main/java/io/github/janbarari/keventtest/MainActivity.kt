package io.github.janbarari.keventtest

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.github.janbarari.kevent.KEvent
import io.github.janbarari.kevent.ObserverInterface
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val sampleSubscriberGUID = "SSGUID"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        KEvent.subscribe(sampleSubscriberGUID, object : ObserverInterface<String> {
            override fun observe(event: String) {
                Toast.makeText(this@MainActivity, event, Toast.LENGTH_LONG).show()
            }
        })

        post_event.setOnClickListener {
            KEvent.post("Event Posted!")
        }

    }

    override fun onStop() {
        super.onStop()
        KEvent.unsubscribe(sampleSubscriberGUID)
    }

}