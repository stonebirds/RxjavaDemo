package com.stone.rxjavademo;

import android.util.Log;

import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private static final String TAG = ExampleUnitTest.class.getSimpleName();
    private Disposable mDisposable;

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testRxJava() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                System.out.println("\nObservable emit 1" + "\n");
                e.onNext("1");
                System.out.println("Observable emit 2" + "\n");
                e.onNext("2");
                System.out.println("Observable emit 3" + "\n");
                e.onNext("3");
                e.onComplete();
                System.out.println("Observable emit 4" + "\n");
                e.onNext("4");
            }
        }).subscribe(new Observer<String>() {
            private int i = 1;

            @Override
            public void onSubscribe(Disposable d) {
                mDisposable = d;
            }

            @Override
            public void onNext(String s) {
                i++;

                if (i == 3) {
                    mDisposable.dispose();
                }
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError : value : " + e.getMessage() + "\n");
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete" + "\n");
            }
        });
    }

    @Test
    public void testConsumer() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("wwwwwww");
            }
        }) .subscribeOn(Schedulers.newThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println(s);
                    }
                });
    }
}