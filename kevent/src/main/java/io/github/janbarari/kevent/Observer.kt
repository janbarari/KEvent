package io.github.janbarari.kevent

class Observer(var observerType: Class<*>, var guid: String, var unit: (Any) -> Unit) {

    override fun toString(): String {
        return "Subscriber($guid, ${observerType::class.java.name})"
    }
}