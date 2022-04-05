package com.example.financefree;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.TestObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subscribers.TestSubscriber;

@RunWith(AndroidJUnit4.class)
public class ReactiveTests {
    private static final String TAG = "Test RxJava";
    private String strActual;
    private Observer<List<String>> getAnimalsObserver() {
        return new Observer<List<String>>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
                strActual = "";
            }
            @Override
            public void onNext(List<String> list) {
                Log.d(TAG, "Name: " + list);
                for(String s: list){
                    strActual += s + ",";
                }
            }
            @Override
            public void onError(Throwable e) {Log.e(TAG, "onError: " + e.getMessage());}
            @Override
            public void onComplete() {Log.d(TAG, "All items are emitted!");}
        };
    }

    @Test
    public void test1(){
        //String strExpected = "Ant,Bee,Cat,Dog,Fox,";
        List<String> l = Arrays.asList("Ant", "Bee", "Cat", "Dog", "Fox");
        List<String> lActual;
        Observable<List<String>> animalsObservable = Observable.just(l);
        lActual = animalsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .first(l).blockingGet();
        assert lActual != null;
        assert lActual.get(0).charAt(0) == 'A';
        assert lActual.size() == 5;
    }

    @Test
    public void test2() {
        List<String> letters = Arrays.asList("A", "B", "C", "D", "E");
        List<String> results = new ArrayList<>();
        Observable<String> observable = Observable
                .fromIterable(letters)
                .zipWith(
                        Observable.range(1, Integer.MAX_VALUE),
                        (string, index) -> index + "-" + string);
        observable.subscribe(results::add);

        assert results.size() == 5;
        assert results.contains("1-A");
        assert results.contains("2-B");
        assert results.contains("3-C");
        assert results.contains("4-D");
        assert results.contains("5-E");
    }

    @Test
    public void test3() {
        List<String> letters = Arrays.asList("A", "B", "C", "D", "E");
        TestObserver<String> subscriber = new TestObserver<>();

        Observable<String> observable = Observable
                .fromIterable(letters)
                .zipWith(
                        Observable.range(1, Integer.MAX_VALUE),
                        ((string, index) -> index + "-" + string));

        observable.subscribe(subscriber);

        subscriber.assertComplete();
        subscriber.assertNoErrors();
        subscriber.assertValueCount(5);
        subscriber.assertValueAt(0,"1-A");
        subscriber.assertValueAt(1,"2-B");
        subscriber.assertValueAt(2,"3-C");
        subscriber.assertValueAt(3,"4-D");
        subscriber.assertValueAt(4,"5-E");
    }

}
