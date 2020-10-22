package net.aridai.simpleqiitaclient.common

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

typealias LiveEvent<T> = LiveData<Event<T>>
typealias MutableLiveEvent<T> = MutableLiveData<Event<T>>

class Event<out T : Any>(args: T) {

    private val _args: T = args

    private val _handled = AtomicBoolean(false)
    val handled: Boolean get() = this._handled.get()

    val args: T?
        get() = this._args.takeIf { this._handled.compareAndSet(false, true) }

    fun peekArgs(): T = this._args
}

fun <T : Any> LiveEvent<T>.observeEvent(owner: LifecycleOwner, callback: (T) -> Unit): Observer<Event<T>> =
    Observer<Event<T>> { event -> event.args?.let(callback) }.also { this.observe(owner, it) }

fun <T : Any> LiveEvent<T>.observeEvent(owner: LifecycleOwner, observer: Observer<in T>): Observer<Event<T>> =
    Observer { event: Event<T> -> event.args?.let(observer::onChanged) }.also { this.observe(owner, it) }

fun <T : Any> LiveEvent<T>.observeEventForever(callback: (T) -> Unit): Observer<Event<T>> =
    Observer<Event<T>> { event -> callback(event.peekArgs()) }.also { this.observeForever(it) }

fun <T : Any> LiveEvent<T>.observeEventForever(observer: Observer<in T>): Observer<Event<T>> =
    Observer { event: Event<T> -> observer.onChanged(event.peekArgs()) }.also { this.observeForever(it) }

fun <T : Any> MutableLiveEvent<T>.publish(args: T) {
    this.value = Event(args)
}

fun <T : Any> MutableLiveEvent<T>.publishOnMainThread(args: T) {
    this.postValue(Event(args))
}
