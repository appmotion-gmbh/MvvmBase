package de.trbnb.mvvmbase.rxjava2

import de.trbnb.mvvmbase.databinding.ViewModel
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable

/**
 * Interface that declares extension functions for RxKotlin classes.
 * To be used with [ViewModel].
 */
public interface RxViewModel : ViewModel {
    /**
     * Converts an [Observable] of non-nullable type `T` into a bindable property of nullable type `T`.
     */
    public fun <T> Observable<out T>.toBindable(
        onError: (Throwable) -> Unit = onErrorStub,
        onComplete: () -> Unit = onCompleteStub
    ): ObservableBindableProperty.Provider<T?> = ObservableBindableProperty.Provider(null, this, onError, onComplete)

    /**
     * Converts an [Observable] of non-nullable type `T` into a bindable property.
     */
    public fun <T> Observable<out T>.toBindable(
        defaultValue: T,
        onError: (Throwable) -> Unit = onErrorStub,
        onComplete: () -> Unit = onCompleteStub
    ): ObservableBindableProperty.Provider<T> = ObservableBindableProperty.Provider(defaultValue, this, onError, onComplete)

    /**
     * Converts a [Flowable] of non-nullable type `T` into a bindable property of nullable type `T`.
     */
    public fun <T> Flowable<out T>.toBindable(
        onError: (Throwable) -> Unit = onErrorStub,
        onComplete: () -> Unit = onCompleteStub
    ): FlowableBindableProperty.Provider<T?> = FlowableBindableProperty.Provider(null, this, onError, onComplete)

    /**
     * Converts a [Flowable] of non-nullable type `T` into a bindable property.
     */
    public fun <T> Flowable<out T>.toBindable(
        defaultValue: T,
        onError: (Throwable) -> Unit = onErrorStub,
        onComplete: () -> Unit = onCompleteStub
    ): FlowableBindableProperty.Provider<T> = FlowableBindableProperty.Provider(defaultValue, this, onError, onComplete)

    /**
     * Converts a [Single] of non-nullable type `T` into a bindable property of nullable type `T`.
     */
    public fun <T> Single<out T>.toBindable(
        fieldId: Int? = null,
        onError: (Throwable) -> Unit = onErrorStub
    ): SingleBindableProperty.Provider<T?> = SingleBindableProperty.Provider(null, this, onError)

    /**
     * Converts a [Single] of non-nullable type `T` into a bindable property.
     */
    public fun <T> Single<out T>.toBindable(
        defaultValue: T,
        onError: (Throwable) -> Unit = onErrorStub
    ): SingleBindableProperty.Provider<T> = SingleBindableProperty.Provider(defaultValue, this, onError)

    /**
     * Converts a [Maybe] of non-nullable type `T` into a bindable property of nullable type `T`.
     */
    public fun <T> Maybe<out T>.toBindable(
        onError: (Throwable) -> Unit = onErrorStub,
        onComplete: () -> Unit = onCompleteStub
    ): MaybeBindableProperty.Provider<T?> = MaybeBindableProperty.Provider(null, this, onError, onComplete)

    /**
     * Converts a [Maybe] of non-nullable type `T` into a bindable property.
     */
    public fun <T> Maybe<T>.toBindable(
        defaultValue: T,
        onError: (Throwable) -> Unit = onErrorStub,
        onComplete: () -> Unit = onCompleteStub
    ): MaybeBindableProperty.Provider<T> = MaybeBindableProperty.Provider(defaultValue, this, onError, onComplete)

    /**
     * Converts a [Completable] without type into a bindable property of the primitive boolean type.
     */
    public fun Completable.toBindable(
        onError: (Throwable) -> Unit = onErrorStub
    ): CompletableBindableProperty.Provider = CompletableBindableProperty.Provider(this, onError)

    /**
     * Automatically disposes a [Disposable] in the ViewModels [onDestroy].
     *
     * @see ViewModel.compositeDisposable
     */
    public fun Disposable.autoDispose() {
        compositeDisposable.add(this)
    }
}

private val onErrorStub: (Throwable) -> Unit
    get() = {}

private val onCompleteStub: () -> Unit
    get() = {}
