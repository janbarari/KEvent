package io.github.janbarari.keventtest

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.github.janbarari.kevent.KEvent
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val stringObserverGUID = "SOGUID"
    private val intObserverGUID = "IOGUID"
    private val customObserverGUID = "COGUID"
    private val stringSenderObserverGUID = "SSOGUID"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        KEvent.register<String>(stringObserverGUID) {
            Toast.makeText(this, "observerGUID: $stringObserverGUID\nvalue: $it", Toast.LENGTH_LONG).show()
        }

        KEvent.registerWithSender<String, MainActivity>(stringSenderObserverGUID) {
            Toast.makeText(this, "observerGUID: $stringSenderObserverGUID\nvalue: $it", Toast.LENGTH_LONG).show()
        }

        KEvent.register<Float>(intObserverGUID) {
            Toast.makeText(this, "observerGUID: $intObserverGUID\nvalue: $it", Toast.LENGTH_LONG).show()
        }

        KEvent.register<String>(customObserverGUID) {
            Toast.makeText(this, "observerGUID: $customObserverGUID\nvalue: $it", Toast.LENGTH_LONG).show()
        }

        post_string.setOnClickListener {
            KEvent.post("Hello World!")
        }

        post_int.setOnClickListener {
            KEvent.post(99f)
        }

        post_custom.setOnClickListener {
            KEvent.post("Hello World!", customObserverGUID)
        }

        post_with_sender.setOnClickListener {
            KEvent.postWithSender<MainActivity>("Hello World!", stringSenderObserverGUID)
        }

    }

    override fun onStop() {
        super.onStop()
        KEvent.unregister(stringObserverGUID)
        KEvent.unregister(intObserverGUID)
        KEvent.unregister(customObserverGUID)
        KEvent.unregister(stringSenderObserverGUID)
    }

}