package com.example.rxjava

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.*;
import io.reactivex.rxjava3.schedulers.Schedulers

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var textState by remember { mutableStateOf("") }
            Column() {
                Button(
                    onClick = { Log.e(TAG, "async works!") },
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = "Click me")
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = textState,
                        style = TextStyle(fontSize = 20.sp)
                    )
                }
            }

            val disposeObservable = dataSourceObservable()
                .subscribeOn(Schedulers.newThread())
                .subscribe({
                    Log.e(TAG, "next int $it")
                }, {

                }, {

                })


            val disposeFlowable = dataSourceFlowable()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.e(TAG, "next int $it")
                    textState = it.toString()
                }, {
                    Log.e(TAG, "Error, it ${it.localizedMessage}")
                })

            val disposeSingle = dataSourceSingle()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.e(TAG, "next int single $it")
                    textState = it.toString()
                }, {
                    Log.e(TAG, "Error, it ${it.localizedMessage}")
                })

            val disposeCompletable = dataSourceSingle()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.e(TAG, "next int single $it")
                    textState = it.toString()
                }, {
                    Log.e(TAG, "Error, it ${it.localizedMessage}")
                })
        }
    }
}

fun dataSourceObservable(): Observable<Int> {
    return Observable.create { subscriber ->
        for (i in 0..100) {
            Thread.sleep(1000)
            subscriber.onNext(i)
        }
    }
}

fun dataSourceFlowable(): Flowable<Int> {
    return Flowable.create({ subscriber ->
        for (i in 0..900000) {
            subscriber.onNext(i)
        }
        subscriber.onComplete()
    }, BackpressureStrategy.LATEST)
}

fun dataSourceSingle(): Single<List<Int>> {
    return Single.create { subscriber ->
        val list = (0..10).toList()
        subscriber.onSuccess(list)
    }
}

fun dataSourceCompletable(): Completable{
    return Completable.create { subscriber ->
        val list = (0..10).toList()
        subscriber.onComplete()
    }
}

fun dataSourceMaybe(): Maybe<List<Int>>{
    return Maybe.create { subscriber ->
        val list = (0..10).toList()
        subscriber.onSuccess(list)
        subscriber.onComplete()
    }
}