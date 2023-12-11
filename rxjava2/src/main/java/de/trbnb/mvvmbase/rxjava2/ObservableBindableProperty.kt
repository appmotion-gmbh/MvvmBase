package de.trbnb.mvvmbase.rxjava2

import de.trbnb.mvvmbase.databinding.ViewModel
import de.trbnb.mvvmbase.databinding.bindableproperty.AfterSet
import de.trbnb.mvvmbase.databinding.bindableproperty.BeforeSet
import de.trbnb.mvvmbase.databinding.bindableproperty.BindablePropertyBase
import de.trbnb.mvvmbase.databinding.bindableproperty.Validate
import de.trbnb.mvvmbase.databinding.utils.resolveFieldId
import io.reactivex.Observable
import io.reactivex.rxkotlin.plusAssign
import kotlin.reflect.KProperty

/**
 * Read-only bindable property delegate that has last emitted value from a [Observable] or `defaultValue` if no value has been emitted.
 */
public class ObservableBindableProperty<T> private constructor(
    viewModel: ViewModel,
    defaultValue: T,
    fieldId: Int,
    observable: Observable<out T>,
    onError: (Throwable) -> Unit,
    onComplete: () -> Unit,
    distinct: Boolean,
    afterSet: AfterSet<T>?,
    beforeSet: BeforeSet<T>?,
    validate: Validate<T>?
) : RxBindablePropertyBase<T>(viewModel, defaultValue, fieldId, distinct, afterSet, beforeSet, validate) {
    init {
        viewModel.compositeDisposable += observable.subscribe({ value = it }, onError, onComplete)
    }

    /**
     * Property delegate provider for [ObservableBindableProperty].
     * Needed so that reflection via [KProperty] is only necessary once, during delegate initialization.
     *
     * @see ObservableBindableProperty
     */
    public class Provider<T> internal constructor(
        private val defaultValue: T,
        private val observable: Observable<out T>,
        private val onError: (Throwable) -> Unit,
        private val onComplete: () -> Unit
    ) : BindablePropertyBase.Provider<ViewModel, T>() {
        override operator fun provideDelegate(thisRef: ViewModel, property: KProperty<*>): ObservableBindableProperty<T> = ObservableBindableProperty(
            viewModel = thisRef,
            fieldId = property.resolveFieldId(),
            defaultValue = defaultValue,
            observable = observable,
            onError = onError,
            onComplete = onComplete,
            distinct = distinct,
            afterSet = afterSet,
            beforeSet = beforeSet,
            validate = validate
        )
    }
}
