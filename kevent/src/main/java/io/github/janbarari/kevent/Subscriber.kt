package io.github.janbarari.kevent

class Subscriber {
    var observerType: Class<*>
    var guid: String
    var observer: ObserverInterface<Any>?

    constructor(
        observerType: Class<*>,
        guid: String,
        observer: ObserverInterface<Any>
    ) {
        this.guid = guid
        this.observer = observer
        this.observerType = observerType
    }

    override fun toString(): String {
        return "Subscriber($guid)"
    }
}