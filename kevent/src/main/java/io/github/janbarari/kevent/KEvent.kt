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
    @Volatile private var observers = ArrayList<Observer>()

    @Synchronized fun getObservers(): ArrayList<Observer> {
        return observers
    }

    private const val DEFAULT_POST_EVENT_LIMITATION_SIZE_IN_BYTES = 0
    private val TAG: String = this::class.java.simpleName
    private var postEventLimitationSizeInBytes: Int = DEFAULT_POST_EVENT_LIMITATION_SIZE_IN_BYTES
    private var pendingDroppingObservers: ArrayList<Observer> = arrayListOf()

    fun setPostEventSizeLimitation(sizeInBytes: Int) {
        postEventLimitationSizeInBytes = sizeInBytes
    }

    fun <T : Any> post(event: T) {
        post(event, null)
    }

    fun <T : Any> post(observerGUID: String, event: T) {
        post(event, observerGUID)
    }

    private fun <T : Any> post(event: T, observerGUID: String?) {
        GlobalScope.launch(Dispatchers.IO) {
            validateEventType(event) {
                val observerIterator = getObservers().iterator()
                while (observerIterator.hasNext()) {
                    val observer = observerIterator.next()
                    if (observerGUID != null) {
                        if (observer.guid == observerGUID) {
                            postWithThread(observer, event)
                        }
                    } else {
                        postWithThread(observer, event)
                    }
                }
                dropObserversIfNeeded()
            }
        }
    }

    /**
     * Add new observer
     * @param observerGUID every observer should have an GUID to unregister if needed
     * @param observer Typed Observer of the observer, only observers will invoke that any instance of entered Type is posted
     */
    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> register(observerGUID: String, noinline unit: (T) -> Unit) {
        getObservers().forEach { observer ->
            if (observer.guid == observerGUID) {
                unregister(observerGUID)
                return@forEach
            }
        }
        getObservers().add(
            Observer(
                T::class.java,
                observerGUID,
                unit as (Any) -> Unit
            )
        )
    }

    /**
     * @param observerGUID Unregister the observer
     */
    fun unregister(observerGUID: String) {
        val observerIterator = getObservers().iterator()
        while (observerIterator.hasNext()) {
            val observer = observerIterator.next()
            if (observer.guid == observerGUID) {
                observerIterator.remove()
            }
        }
    }

    /**
     * Unregister all observers that KEvent notifies
     */
    fun unregisterAll() {
        getObservers().clear()
    }

    private fun dropObserversIfNeeded() {
        pendingDroppingObservers.forEach { droppedObserver ->
            val observerIterator = getObservers().iterator()
            while (observerIterator.hasNext()) {
                val observer = observerIterator.next()
                if (observer.guid == droppedObserver.guid) {
                    observerIterator.remove()
                    Log.d(TAG, "[$observer -> Dropped]")
                }
            }
        }
        pendingDroppingObservers.clear()
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

    private fun <T : Any> postWithThread(observer: Observer, event: T) {
        if (observer.observerType == event::class.java) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                postToObserver(observer, event)
            } else {
                GlobalScope.launch(Dispatchers.Main) {
                    postToObserver(observer, event)
                }
            }
        }
    }

    private fun <T : Any> postToObserver(observer: Observer, event: T) {
        try {
            observer.unit.invoke(event)
        } catch (e: Exception) {
            pendingDroppingObservers.add(observer)
        }
    }

}