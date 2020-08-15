package io.github.janbarari.kevent

import android.os.Looper
import android.os.Parcelable
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream
import java.io.Serializable

object KEvent {
    @Volatile var subscribers = ArrayList<Subscriber>()

    @Synchronized fun getAllSubscribers(): ArrayList<Subscriber> {
        return subscribers
    }

    private const val DEFAULT_POST_EVENT_LIMITATION_SIZE_IN_BYTES = 0
    private val TAG: String = this::class.java.simpleName
    private var postEventLimitationSizeInBytes: Int = DEFAULT_POST_EVENT_LIMITATION_SIZE_IN_BYTES
    private var pendingDroppingSubscribers: ArrayList<Subscriber> = arrayListOf()

    fun setPostEventSizeLimitation(sizeInBytes: Int) {
        postEventLimitationSizeInBytes = sizeInBytes
    }

    fun <T : Any> post(event: T) {
        post(event, null)
    }

    fun <T : Any> post(receiverSubscriberGUID: String, event: T) {
        post(event, receiverSubscriberGUID)
    }

    private fun <T : Any> post(event: T, receiverSubscriberGUID: String?) {
        GlobalScope.launch(Dispatchers.IO) {
            validateEventType(event) {
                val subscriberIterator = getAllSubscribers().iterator()
                while (subscriberIterator.hasNext()) {
                    val subscriber = subscriberIterator.next()
                    if (receiverSubscriberGUID != null) {
                        if (subscriber.guid == receiverSubscriberGUID) {
                            postWithThread(subscriber, event)
                        }
                    } else {
                        postWithThread(subscriber, event)
                    }
                }
                dropSubscribersIfNeeded()
            }
        }
    }

    /**
     * Add new subscriber
     * @param subscriberGUID every subscriber should have an GUID to unsubscribe if needed
     * @param observer Typed Observer of the subscriber, only subscribers will invoke that any instance of entered Type is posted
     */
    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> subscribe(subscriberGUID: String, observer: ObserverInterface<T>) {
        getAllSubscribers().forEach { subscriber ->
            if (subscriber.guid == subscriberGUID) {
                unsubscribe(subscriberGUID)
                return@forEach
            }
        }
        getAllSubscribers().add(
            Subscriber(
                T::class.java,
                subscriberGUID,
                observer as ObserverInterface<Any>
            )
        )
    }

    /**
     * @param subscriberGUID Unsubscribe the subscriber
     */
    fun unsubscribe(subscriberGUID: String) {
        val subscriberIterator = getAllSubscribers().iterator()
        while (subscriberIterator.hasNext()) {
            val subscriber = subscriberIterator.next()
            if (subscriber.guid == subscriberGUID) {
                subscriberIterator.remove()
            }
        }
    }

    /**
     * Unsubscribe all subscribers that KEvent notifies
     */
    fun unsubscribeAll() {
        getAllSubscribers().clear()
    }

    private fun dropSubscribersIfNeeded() {
        pendingDroppingSubscribers.forEach { droppedSubscriber ->
            val subscriberIterator = getAllSubscribers().iterator()
            while (subscriberIterator.hasNext()) {
                val subscriber = subscriberIterator.next()
                if (subscriber.guid == droppedSubscriber.guid) {
                    subscriberIterator.remove()
                    Log.d(TAG, "[$subscriber -> Dropped]")
                }
            }
        }
        pendingDroppingSubscribers.clear()
    }

    private fun <T : Any> validateEventType(event: T, validated: () -> Unit) {
        if (event is Serializable || event is Parcelable) {
            if (postEventLimitationSizeInBytes > DEFAULT_POST_EVENT_LIMITATION_SIZE_IN_BYTES) {
                if (sizeOf(event) < postEventLimitationSizeInBytes) {
                    validated()
                } else {
                    throwException(SizeOutOfRangeException(postEventLimitationSizeInBytes))
                }
            } else {
                validated()
            }
        } else {
            throwException(NotSerializableException())
        }
    }

    private fun <T : Any> sizeOf(event: T): Int {
        return try {
            val byteOutputStream = ByteArrayOutputStream()
            val objectOutputStream = ObjectOutputStream(byteOutputStream)
            objectOutputStream.writeObject(event)
            objectOutputStream.flush()
            objectOutputStream.close()
            byteOutputStream.toByteArray().size
        } catch (e: Exception) {
            e.printStackTrace()
            DEFAULT_POST_EVENT_LIMITATION_SIZE_IN_BYTES
        }
    }

    /**
     * Exceptions will be throwing at debugging mode. And for prevent app crash,
     * all exceptions will be just print in stack trace at release mode
     */
    private fun throwException(exception: Exception) {
        if (BuildConfig.DEBUG) {
            throw exception
        } else {
            exception.printStackTrace()
        }
    }

    private fun <T : Any> postWithThread(subscriber: Subscriber, event: T) {
        if (subscriber.observerType == event::class.java) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                postToSubscriber(subscriber, event)
            } else {
                GlobalScope.launch(Dispatchers.Main) {
                    postToSubscriber(subscriber, event)
                }
            }
        }
    }

    private fun <T : Any> postToSubscriber(subscriber: Subscriber, event: T) {
        try {
            subscriber.observer?.observe(event)
        } catch (e: Exception) {
            pendingDroppingSubscribers.add(subscriber)
        }
    }

}