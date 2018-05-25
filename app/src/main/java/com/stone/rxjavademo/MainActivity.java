package com.stone.rxjavademo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Disposable mDisposable;
    private TextView tv;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.tv);
        btn = findViewById(R.id.btn);


        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                Log.e(TAG, "subscribe线程所在：" + Thread.currentThread().getName() + "\n");
                Log.e(TAG, "\nObservable emit 1" + "\n");
                e.onNext("1");
                Log.e(TAG, "Observable emit 2" + "\n");
                e.onNext("2");
                Log.e(TAG, "Observable emit 3" + "\n");
                e.onNext("3");
                e.onComplete();
                Log.e(TAG, "Observable emit 4" + "\n");
                e.onNext("4");
            }
        }).map(new Function<String, Integer>() {
            @Override
            public Integer apply(String string) throws Exception {
                return Integer.parseInt(string);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    private int i = 1;

                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(Integer s) {
                        Log.e(TAG, "onNext线程所在：" + Thread.currentThread().getName() + "----------- " + s + "\n");
                        i++;
                        if (i == 3) {
                            mDisposable.dispose();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError : value : " + e.getMessage() + "\n");
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete" + "\n");
                    }
                });

        requestNet();

        rxBindingTest();
    }

    private void rxBindingTest() {
        RxView.clicks(btn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        clickBtn();
                    }
                });

        RxView.longClicks(btn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        longClickBtn();
                    }
                });

    }

    private void longClickBtn() {
        Toast.makeText(this, "亲，轻轻长按！", Toast.LENGTH_SHORT).show();
    }

    private void clickBtn() {
    }

    @SuppressLint("CheckResult")
    private void requestNet() {
        Disposable subscribe = Observable.create(new ObservableOnSubscribe<Response>() {
            @Override
            public void subscribe(ObservableEmitter<Response> e) throws Exception {
                Request.Builder builder = new Request.Builder()
                        .url("http://api.avatardata.cn/MobilePlace/LookUp?key=ec47b85086be4dc8b5d941f5abd37a4e&mobileNumber=13021671512")
                        .get();
                Request request = builder.build();
                Call call = new OkHttpClient().newCall(request);
                Response response = call.execute();
                e.onNext(response);
            }
        }).map(new Function<Response, MobileAddress>() {
            @Override
            public MobileAddress apply(Response response) throws Exception {
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    if (body != null) {
                        Log.e(TAG, "map:转换前:" + response.body());
                        return new Gson().fromJson(body.string(), MobileAddress.class);
                    }
                }
                return null;
            }
        }).observeOn(Schedulers.io())
                .doOnNext(new Consumer<MobileAddress>() {
                    @Override
                    public void accept(MobileAddress s) throws Exception {
                        Log.e(TAG, "doOnNext线程所在：" + Thread.currentThread().getName() + "----------- " + s + "\n");
                        Log.e(TAG, "doOnNext: 保存成功：" + s.getReason() + "\n");
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MobileAddress>() {
                    @Override
                    public void accept(MobileAddress data) throws Exception {
                        Log.e(TAG, "成功:" + data.getReason() + "\n");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "失败：" + throwable.getMessage() + "\n");
                    }
                });

        Flowable.interval(1, TimeUnit.SECONDS)
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.e(TAG, "accept: doOnNext : " + aLong);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.e(TAG, "accept: 设置文本 ：" + aLong);
                        tv.append("accept: 设置文本 ：" + aLong + "\n");
                    }
                });


    }
}
