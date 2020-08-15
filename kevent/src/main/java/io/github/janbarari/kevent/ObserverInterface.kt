package io.github.janbarari.kevent

interface ObserverInterface<T> {
    fun observe(event: T)
}