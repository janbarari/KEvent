package io.github.janbarari.jevent

interface SubscriberInterface {
    fun onEvent(event: Any) {}
    fun onEvent(event: DefaultEvent) {}
}