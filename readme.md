# A none class implementation event's for android (similar to Eventbus)
[![](https://jitpack.io/v/janbarari/jevent.svg)](https://jitpack.io/#janbarari/jevent)

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
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

implementation 'com.github.janbarari:jevent:v1.0.0'
```

```kotlin
//Subscribe
JEvent.subscribe(object : SubscriberInterface {
    override fun onEvent(event: Any) {

    }
})
//Unsubscribe none global subscribers or ui related subscribers
JEvent.unsubscribe()


//Post from anywhere
JEvent.post("put any object here")
```

### If you like it, please tap the Star(⭐️) button 
