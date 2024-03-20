package com.example.rxjava

import android.content.ContentValues.TAG
import android.util.Log
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

private val disposeBag = CompositeDisposable()

fun disposeResult() {
    val dispose: Disposable = Observable.just("1", "2", "3", "4")
        .delay(3, TimeUnit.SECONDS)
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
            Log.e(TAG, "value is -> $it")
        }, {

        })

    disposeBag.add(dispose)
}
fun onDestroy(){
    disposeBag.clear()

}