package com.ablecloud.rxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * author : dukai
 * date  : 2018/8/16
 * describe:
 */
public class FilterOperationActivity extends AppCompatActivity {
    private static final String TAG = "FilterOperation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.filter, R.id.ofType, R.id.skip, R.id.distinct, R.id.distinctUntilChanged, R.id.take
            , R.id.debounce, R.id.firstElement_lastElement, R.id.elementAt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.filter:
                filter();
                break;
            case R.id.ofType:
                ofType();
                break;
            case R.id.skip:
                skip();
                break;
            case R.id.distinct:
                distinct();
                break;
            case R.id.distinctUntilChanged:
                distinctUntilChanged();
                break;
            case R.id.take:
                take();
                break;
            case R.id.debounce:
                debounce();
                break;
            case R.id.firstElement_lastElement:
                firstElement_lastElement();
                break;
            case R.id.elementAt:
                elementAt();
                break;
        }
    }

    /**
     * 通过一定逻辑来过滤被观察者发送的事件，如果返回 true 则会发送事件，否则不会发送
     */
    private void filter() {
        Observable.just(1, 2, 3)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        if (integer <= 2) {
                            return true;
                        }
                        return false;
                    }
                }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.e(TAG, "accept: " + integer);
            }
        });
    }

    /**
     * 可以过滤不符合该类型事件
     */
    private void ofType() {
        Observable.just(1, 2, 3, "4", "5")
                .ofType(String.class)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.e(TAG, "accept: " + s);
                    }
                });
    }

    /**
     * 跳过正序某些事件，count 代表跳过事件的数量
     */
    private void skip() {
        Observable.just(1, 2, 3, "4", "5")
                .skipLast(3)
                .subscribe(new Consumer<Serializable>() {
                    @Override
                    public void accept(Serializable serializable) throws Exception {
                        Log.e(TAG, "accept: " + serializable);
                    }
                });
    }

    /**
     * 过滤事件序列中的重复事件
     */
    private void distinct() {
        Observable.just(1, 2, 3, 1, 2)
                .distinct()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, "accept: " + integer);
                    }
                });
    }

    /**
     * 过滤掉连续重复的事件
     */
    private void distinctUntilChanged() {
        Observable.just(1, 2, 3, 3, 3, 2, 1)
                .distinctUntilChanged()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, "accept: " + integer);
                    }
                });
    }

    /**
     * 控制观察者接收的事件的数量
     */
    private void take() {
        Observable.just(1, 2, 3, 4)
                .take(2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, "accept: " + integer);
                    }
                });
    }

    /**
     * 如果两件事件发送的时间间隔小于设定的时间间隔则前一件事件就不会发送给观察者
     */
    private void debounce() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                Thread.sleep(900);
                emitter.onNext(2);
            }
        })
                .debounce(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, "accept: " + integer);
                    }
                });
    }

    /**
     * firstElement() 取事件序列的第一个元素，lastElement() 取事件序列的最后一个元素
     */
    private void firstElement_lastElement() {
        Observable.just(1, 2, 3, 4)
                .firstElement()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, "accept: " + integer);
                    }
                });
        Observable.just(1, 2, 3, 4)
                .lastElement()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, "accept: " + integer);
                    }
                });
    }

    /**
     * elementAt() 可以指定取出事件序列中事件，但是输入的 index 超出事件序列的总数的话就不会出现任何结果。这种情况下，你想发出异常信息的话就用 elementAtOrError()
     */
    private void elementAt() {
        Observable.just(1, 2, 3, 4)
                .elementAt(2)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, "accept: " + integer);
                    }
                });
    }
}
