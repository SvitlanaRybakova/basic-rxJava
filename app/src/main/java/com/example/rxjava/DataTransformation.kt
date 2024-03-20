package com.example.rxjava

import android.content.ContentValues
import android.util.Log
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.Random
import java.util.concurrent.TimeUnit


fun dataTransformation(){
    val dispose: Disposable = Observable.just("Alex", "Bob", "Carl", "Din")
        //.map{ it + "Some extra text"}
        .flatMap {
            val delay = Random().nextInt(10)
            Observable.just(it) .delay(delay.toLong(), TimeUnit.SECONDS)
        }
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
            Log.e(ContentValues.TAG, "value is -> $it")
        }, {

        })
}