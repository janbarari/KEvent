package io.github.janbarari.kevent

class NotSerializableException : Exception() {
    override val message: String?
        get() = "Your event is not serializable or parcelable object, to avoid memory leak and increase the performance you should make your event's serializable or parcelable!"
}

class SizeOutOfRangeException(private val eventLimitSize: Int) : Exception() {
    override val message: String?
        get() = "Only event with smaller than $eventLimitSize byte size can be transfer by KEvent"
}