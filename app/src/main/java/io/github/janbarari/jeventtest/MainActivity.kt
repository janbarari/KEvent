package io.github.janbarari.jeventtest

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.github.janbarari.jevent.DefaultEvent
import io.github.janbarari.jevent.JEvent
import io.github.janbarari.jevent.SubscriberInterface

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        JEvent.subscribe(this::class.java, object : SubscriberInterface {
            //Optional
            override fun onEvent(event: DefaultEvent) {
                //receive when posting an event that instance of the DefaultEvent
                Toast.makeText(this@MainActivity, event.toString(), Toast.LENGTH_LONG).show()
            }

            //Optional
            override fun onEvent(event: Any) {
                //receive when posting a raw event
                Toast.makeText(this@MainActivity, event.toString(), Toast.LENGTH_LONG).show()
            }
        })

        JEvent.post(
            DefaultEvent(this::class.java)
                .put("isEnabled", true)
                .put("Expired", false)
                .put("Cookie", "ABS")
        )

        Handler().postDelayed({
            JEvent.post("put any object here")
        }, 2000)

    }

    override fun onDestroy() {
        super.onDestroy()
        //Remove the receiver when you using JEvent in ui based classes to avoid crash because of context destroyed
        JEvent.unsubscribe(this::class.java)
    }
}