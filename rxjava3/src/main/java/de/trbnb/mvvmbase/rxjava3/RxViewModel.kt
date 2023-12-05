package de.trbnb.mvvmbase.rxjava3

import de.trbnb.mvvmbase.databinding.ViewModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable

/**
 * Interface that declares extension functions for RxKotlin classes.
 * To be used with [ViewModel].
 */
public interface RxViewModel : ViewModel {
    /**
     * Converts an [Observable] of non-nullable type `T` into a bindable property of nullable type `T`.
     */
    public fun <T : Any> Observable<out T>.toBindable(
        onError: (Throwable) -> Unit = onErrorStub,
        onComplete: () -> Unit = onCompleteStub
    ): ObservableBindableProperty.Provider<T?> = ObservableBindableProperty.Provider(null, this as Observable<Any>, onError, onComplete)

    /**
     * Converts an [Observable] of non-nullable type `T` into a bindable property.
     */
    public fun <T : Any> Observable<out T>.toBindable(
        defaultValue: T,
        onError: (Throwable) -> Unit = onErrorStub,
        onComplete: () -> Unit = onCompleteStub
    ): ObservableBindableProperty.Provider<T> = ObservableBindableProperty.Provider(defaultValue, this as Observable<Any>, onError, onComplete)

    /**
     * Converts a [Flowable] of non-nullable type `T` into a bindable property of nullable type `T`.
     */
    public fun <T : Any> Flowable<out T>.toBindable(
        onError: (Throwable) -> Unit = onErrorStub,
        onComplete: () -> Unit = onCompleteStub
    ): FlowableBindableProperty.Provider<T?> = FlowableBindableProperty.Provider(null, this as Flowable<Any>, onError, onComplete)

    /**
     * Converts a [Flowable] of non-nullable type `T` into a bindable property.
     */
    public fun <T : Any> Flowable<out T>.toBindable(
        defaultValue: T,
        onError: (Throwable) -> Unit = onErrorStub,
        onComplete: () -> Unit = onCompleteStub
    ): FlowableBindableProperty.Provider<T> = FlowableBindableProperty.Provider(defaultValue, this as Flowable<Any>, onError, onComplete)

    /**
     * Converts a [Single] of non-nullable type `T` into a bindable property of nullable type `T`.
     */
    public fun <T : Any> Single<out T>.toBindable(
        onError: (Throwable) -> Unit = onErrorStub
    ): SingleBindableProperty.Provider<T?> = SingleBindableProperty.Provider(null, this as Single<Any>, onError)

    /**
     * Converts a [Single] of non-nullable type `T` into a bindable property.
     */
    public fun <T : Any> Single<out T>.toBindable(
        defaultValue: T,
        onError: (Throwable) -> Unit = onErrorStub
    ): SingleBindableProperty.Provider<T> = SingleBindableProperty.Provider(defaultValue, this as Single<Any>, onError)

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
