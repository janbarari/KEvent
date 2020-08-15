# A none class implementation event's for android (similar to Eventbus)
[![](https://jitpack.io/v/janbarari/jevent.svg)](https://jitpack.io/#janbarari/kevent)

KEvent is an open-source library for kotlin using the publisher/subscriber pattern for loose coupling. KEvent enables central commiunication to decoupled classes with just a few lines of code - simplifying the code, removing dependencies and speeding up app development.

![](image.jpg)

### Benefit's
- Global subscribers that always alive
- Is none class implementation (unlike Eventbus)
- Simplifies the commiunication between components
- Decouples event sender
- Is faster than EventBus (30% in Main Thread)
- Is smaller than EventBus
- Typed Subscribers
- Post to Specific Subscriber
- Data Validation (Only Serializable/Parcelable will be transfered)

### Installation
```gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

implementation 'com.github.janbarari:kevent:v1.1.0'
```

```kotlin
//Subscribe
KEvent.subscribe(object : ObserverInterface<T> {
    override fun observe(event: T) {

    }
})
//Unsubscribe none global subscribers or ui related subscribers
JEvent.unsubscribe()


//Post from anywhere
KEvent.post("put any object here")
```
License
-------
Copyright (C) 2020 Mehdi Janbarari

KEvent binaries and source code can be used according to the [Apache License, Version 2.0](LICENSE).

### I dedicate this library to my princess❤️
### If you like it, please tap the Star(⭐️) button 
