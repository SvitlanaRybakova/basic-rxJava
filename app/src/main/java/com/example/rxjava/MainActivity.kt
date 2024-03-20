package com.example.rxjava

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.reactivex.rxjava3.core.*;
import io.reactivex.rxjava3.schedulers.Schedulers

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Button(
                onClick = { Log.e(TAG, "async works!") },
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Click me")
            }
            val dispose = dataSourceObservable()
                .subscribeOn(Schedulers.newThread())
                .subscribe({
                    Log.e(TAG, "next int $it")
                }, {

                }, {

                })


            val dispose2 = dataSourceFlowable()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainTread())
                .subscribe({
                    Log.e(TAG, "next int $it")
                }, {
                    Log.e(TAG, "it ${it.localizedMessage}")
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
    return Flowable.create ({ subscriber ->
        for (i in 0..100) {
            Thread.sleep(1000)
            subscriber.onNext(i)
        }
        subscriber.onComplete()
    }, BackpressureStrategy.DROP)

}
