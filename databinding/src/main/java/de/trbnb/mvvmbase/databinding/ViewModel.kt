package de.trbnb.mvvmbase.databinding

import androidx.databinding.Observable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import de.trbnb.mvvmbase.databinding.bindableproperty.BeforeSet
import de.trbnb.mvvmbase.databinding.bindableproperty.BindablePropertyBase
import de.trbnb.mvvmbase.databinding.bindableproperty.beforeSet
import de.trbnb.mvvmbase.databinding.utils.destroyAll
import de.trbnb.mvvmbase.events.EventChannelOwner
import de.trbnb.mvvmbase.events.addListener
import kotlin.reflect.KProperty

/**
 * Base interface that defines basic functionality for all view models.
 *
 * View models are bound to either an [MvvmBindingActivity] or an [MvvmBindingFragment] and saved
 * throughout the lifecycle of these by the Architecture Components.
 *
 * It extends the [Observable] interface provided by the Android data binding library. This means
 * that implementations have to handle [androidx.databinding.Observable.OnPropertyChangedCallback]s.
 * This is done the easiest way by extending [androidx.databinding.BaseObservable].
 */
public interface ViewModel : Observable, LifecycleOwner, EventChannelOwner {
    /**
     * Notifies listeners that all properties of this instance have changed.
     */
    public fun notifyChange()

    /**
     * Notifies listeners that a specific property has changed. The getter for the property
     * that changes should be marked with [androidx.databinding.Bindable] to generate a field in
     * `BR` to be used as `fieldId`.
     *
     * @param fieldId The generated BR id for the Bindable field.
     */
    public fun notifyPropertyChanged(fieldId: Int)

    /**
     * Notifies listeners that a specific property has changed. The getter for the property
     * that changes should be marked with [androidx.databinding.Bindable] to generate a field in
     * `BR` to be used as `fieldId`.
     *
     * @see notifyPropertyChanged
     *
     * @param property The property whose BR field ID will be detected via reflection.
     */
    public fun notifyPropertyChanged(property: KProperty<*>)

    /**
     * Registers a property changed callback.
     */
    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback)

    /**
     * Unregisters a property changed callback.
     */
    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback)

    /**
     * Is called when this ViewModel is bound to a View.
     */
    public fun onBind()

    /**
     * Is called this ViewModel is not bound to a View anymore.
     */
    public fun onUnbind()

    /**
     * Is called when this instance is about to be removed from memory.
     * This means that this object is no longer bound to a view and will never be. It is about to
     * be garbage collected.
     * Implementations should provide a method to deregister from callbacks, etc.
     */
    public fun onDestroy()

    /**
     * Is called when this instance is about to be removed from memory.
     * This means that this object is no longer bound to a view and will never be. It is about to
     * be garbage collected.
     * Implementations should provide a method to deregister from callbacks, etc.
     *
     * @see [BaseViewModel.onDestroy]
     */
    public fun destroy()

    /**
     * @see [androidx.lifecycle.ViewModel.getTag]
     */
    public operator fun <T : Any> get(key: String): T?

    /**
     * @see [androidx.lifecycle.ViewModel.setTagIfAbsent]
     */
    public fun <T : Any> initTag(key: String, newValue: T): T

    /**
     * Destroys all ViewModels in that list when the containing ViewModel is destroyed.
     */
    public fun <VM : ViewModel, C : Collection<VM>> C.autoDestroy(): C = onEach { it.autoDestroy() }

    /**
     * Destroys the receiver ViewModel when the containing ViewModel is destroyed.
     */
    public fun <VM : ViewModel> VM.autoDestroy(): VM = also { child ->
        val parentLifecycleObserver = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                child.destroy()
            }
        }.also(this@ViewModel.lifecycle::addObserver)

        // If the child is destroyed for any reason it's listener to the parents lifecycle is removed to avoid leaks.
        child.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                this@ViewModel.lifecycle.removeObserver(parentLifecycleObserver)
            }
        })
    }

    /**
     * Sends all the events of a given list of (receiver type) ViewModels through the event channel of the ViewModel where this function is called in.
     */
    public fun <VM : ViewModel, C : Collection<VM>> C.bindEvents(): C = onEach { it.bindEvents() }

    /**
     * Sends all the events of a given (receiver type) ViewModel through the event channel of the ViewModel where this function is called in.
     */
    public fun <VM : ViewModel> VM.bindEvents(): VM = also { child ->
        child.eventChannel.addListener(this@ViewModel) { event -> this@ViewModel.eventChannel.invoke(event) }
    }

    /**
     * Sets [BindablePropertyBase.afterSet] to a given function and returns that instance.
     */
    public fun <T : Collection<ViewModel>, P : BindablePropertyBase.Provider<*, T>> P.asChildren(beforeSet: BeforeSet<T>? = null): P = apply {
        beforeSet { old, new ->
            old.destroyAll()
            new.autoDestroy().bindEvents()
            beforeSet?.invoke(old, new)
        }
    }
}
