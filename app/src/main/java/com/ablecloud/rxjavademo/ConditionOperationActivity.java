package com.ablecloud.rxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * author : dukai
 * date  : 2018/8/16
 * describe:
 */
public class ConditionOperationActivity extends AppCompatActivity {
    private static final String TAG = "ConditionOperation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condition);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.all, R.id.takeWhile, R.id.skipWhile, R.id.takeUntil, R.id.skipUntil, R.id.sequenceEqual,
            R.id.contains, R.id.isEmpty, R.id.amb, R.id.defaultIfEmpty})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.all:
                all();
                break;
            case R.id.takeWhile:
                takeWhile();
                break;
            case R.id.skipWhile:
                skipWhile();
                break;
            case R.id.takeUntil:
                takeUntil();
                break;
            case R.id.skipUntil:
                skipUntil();
                break;
            case R.id.sequenceEqual:
                sequenceEqual();
                break;
            case R.id.contains:
                contains();
                break;
            case R.id.isEmpty:
                isEmpty();
                break;
            case R.id.amb:
                amb();
                break;
            case R.id.defaultIfEmpty:
                defaultIfEmpty();
                break;
        }
    }

    /**
     * 判断事件序列是否全部满足某个事件，如果都满足则返回 true，反之则返回 false
     */
    private void all() {
        Observable.just(1, 2, 3)
                .all(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        if (integer <= 3) {
                            return true;
                        }
                        return false;
                    }
                }).subscribe(new BiConsumer<Boolean, Throwable>() {
            @Override
            public void accept(Boolean aBoolean, Throwable throwable) throws Exception {
                Log.e(TAG, "accept: " + aBoolean);
            }
        });
    }

    /**
     * 可以设置条件，当某个数据满足条件时就会发送该数据，反之则不发送
     */
    private void takeWhile() {
        Observable.just(1, 2, 3)
                .takeWhile(new Predicate<Integer>() {
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
     * 可以设置条件，当某个数据满足条件时不发送该数据，反之则发送
     */
    private void skipWhile() {
        Observable.just(1, 2, 3)
                .skipWhile(new Predicate<Integer>() {
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
     * 可以设置条件，当事件满足此条件时，下一次的事件就不会被发送了
     */
    private void takeUntil() {
        Observable.just(1, 2, 3)
                .takeUntil(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        if (integer >= 2) {
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
     * 当 skipUntil() 中的 Observable 发送事件了，原来的 Observable 才会发送事件给观察者
     */
    private void skipUntil() {
        Observable.intervalRange(1, 5, 0, 1, TimeUnit.SECONDS)
                .skipUntil(Observable.intervalRange(6, 5, 3, 1, TimeUnit.SECONDS))
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "========================onSubscribe ");
                    }

                    @Override
                    public void onNext(Long along) {
                        Log.d(TAG, "========================onNext " + along);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "========================onError ");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "========================onComplete ");
                    }
                });

    }

    /**
     * 判断两个 Observable 发送的事件是否相同
     */
    private void sequenceEqual() {
        Observable.sequenceEqual(Observable.just(1, 2, 3, 4),
                Observable.just(1, 2, 3))
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        Log.e(TAG, "========================onNext " + aBoolean);
                    }
                });
    }

    /**
     * 判断事件序列中是否含有某个元素，如果有则返回 true，如果没有则返回 false
     */
    private void contains() {
        Observable.just(1, 2, 3)
                .contains(5)
                .subscribe(new BiConsumer<Boolean, Throwable>() {
                    @Override
                    public void accept(Boolean aBoolean, Throwable throwable) throws Exception {
                        Log.e(TAG, "accept: " + aBoolean);
                    }
                });
    }

    /**
     * 判断事件序列是否为空
     */
    private void isEmpty() {
        Observable.empty()
                .isEmpty()
                .subscribe(new BiConsumer<Boolean, Throwable>() {
                    @Override
                    public void accept(Boolean aBoolean, Throwable throwable) throws Exception {
                        Log.e(TAG, "accept: " + aBoolean);
                    }
                });
    }

    /**
     * amb() 要传入一个 Observable 集合，但是只会发送最先发送事件的 Observable 中的事件，其余 Observable 将会被丢弃
     */
    private void amb() {
        ArrayList<Observable<Long>> list = new ArrayList<>();

        list.add(Observable.intervalRange(1, 5, 2, 1, TimeUnit.SECONDS));
        list.add(Observable.intervalRange(6, 5, 0, 1, TimeUnit.SECONDS));

        Observable.amb(list)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.e(TAG, "aLong " + aLong);
                    }
                });
    }

    /**
     * 如果观察者只发送一个 onComplete() 事件，则可以利用这个方法发送一个值
     */
    private void defaultIfEmpty() {
        Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onComplete();
            }
        })
                .defaultIfEmpty(111)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, "accept: " + integer);
                    }
                });
    }
}
