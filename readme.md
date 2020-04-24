# A none class implementation event's for android (similar to Eventbus)
JEvent is an open-source library for android, java and kotlin using the publisher/subscriber pattern for loose coupling. JEvent enables central commiunication to decoupled classes with just a few lines of code - simplifying the code, removing dependencies and speeding up app development.

![](image.jpg)

### Benefit's
- Global subscribers that always alive
- Is none class implementation (unlike Eventbus)
- Simplifies the commiunication between components
- Decouples event sender
- Is fast
- Is tiny

### Installation
```gradle

```

```kotlin
//Subscribe
JEvent.subscribe(object : SubscriberInterface {
    override fun onEvent(event: Any) {

    }
})

//Post
JEvent.post("put any object here")

//Unsubscribe for none global subscribers or UI related subscribers
JEvent.unsubscribe()
```

### If you like it, please tap the Star(⭐️) button 
