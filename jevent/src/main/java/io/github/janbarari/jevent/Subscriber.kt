package io.github.janbarari.jevent

const val GLOBAL_RECEIVER = "GLOBAL_RECEIVER"

class Subscriber {
    var key: String
    var subscriber: SubscriberInterface

    constructor(subscriber: SubscriberInterface) {
        key = GLOBAL_RECEIVER
        this.subscriber = subscriber
    }

    constructor(key: String, subscriber: SubscriberInterface) {
        this.key = key
        this.subscriber = subscriber
    }
}