package io.github.janbarari.jevent

import java.util.*

object JEvent {
    private val subscribers = ArrayList<Subscriber>()

    fun post(event: Any) {
        for (subscriber in subscribers) {
            if (event is DefaultEvent) {
                subscriber.subscriber.onEvent(event)
            } else {
                subscriber.subscriber.onEvent(event)
            }
        }
    }

    fun subscribe(subscriberInterface: SubscriberInterface) {
        subscribers.add(Subscriber(subscriberInterface))
    }

    fun subscribe(key: String, subscriber: SubscriberInterface) {
        for (mSubscriber in subscribers) {
            if (mSubscriber.key != GLOBAL_RECEIVER && mSubscriber.key == key) {
                unsubscribe(key)
                break
            }
        }
        subscribers.add(Subscriber(key, subscriber))
    }

    fun subscribe(clazz: Class<*>, subscriber: SubscriberInterface) {
        subscribe(clazz.name, subscriber)
    }

    fun unsubscribe(key: String) {
        val subscriberIterator = subscribers.iterator()
        while (subscriberIterator.hasNext()) {
            val subscriber = subscriberIterator.next()
            if (subscriber.key != GLOBAL_RECEIVER && subscriber.key == key) {
                subscriberIterator.remove()
            }
        }
    }

    fun unsubscribe(clazz: Class<*>) {
        unsubscribe(clazz.name)
    }
}
