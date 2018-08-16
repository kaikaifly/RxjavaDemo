package com.ablecloud.rxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * author : dukai
 * date  : 2018/8/16
 * describe:
 */
public class ComposeOperationActivity extends AppCompatActivity {

    private static final String TAG = "ComposeOperation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.concat, R.id.merge, R.id.zip, R.id.combineLatest, R.id.reduce, R.id.collect,
            R.id.startWith, R.id.count})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.concat:
                concat();
                break;
            case R.id.merge:
                merge();
                break;
            case R.id.zip:
                zip();
                break;
            case R.id.combineLatest:
                combineLatest();
                break;
            case R.id.reduce:
                reduce();
                break;
            case R.id.collect:
                collect();
                break;
            case R.id.startWith:
                startWith();
                break;
            case R.id.count:
                count();
                break;
        }
    }

    /**
     * 可以将多个观察者组合在一起，然后按照之前发送顺序发送事件。需要注意的是，concat() 最多只可以发送4个事件
     * concatArray 与 concat() 作用一样，不过 concatArray() 可以发送多于 4 个被观察者。
     */
    private void concat() {
        Observable.concat(
                Observable.just(1, 2),
                Observable.just(3, 4),
                Observable.just(5, 6),
                Observable.just(7, 8))
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.e(TAG, "onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 这个方法和 concat() 作用基本一样，只是 concat() 是串行发送事件，而 merge() 并行发送事件
     * mergeArray() 与 merge() 的作用是一样的，只是它可以发送4个以上的被观察者
     * <p>
     * <p>
     * <p>
     * 在 concatArray() 和 mergeArray() 两个方法当中，如果其中有一个被观察者发送了一个 Error 事件，那么就会停止发送事件，如果你想 onError() 事件延迟到所有被观察者都发送完事件后再执行的话，就可以使用  concatArrayDelayError() 和 mergeArrayDelayError()
     */
    private void merge() {
        Observable.merge(
                Observable.interval(1, TimeUnit.SECONDS).map(new Function<Long, String>() {
                    @Override
                    public String apply(Long aLong) throws Exception {
                        return "A" + aLong;
                    }
                }),
                Observable.interval(1, TimeUnit.SECONDS).map(new Function<Long, String>() {
                    @Override
                    public String apply(Long aLong) throws Exception {
                        return "B" + aLong;
                    }
                }))
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        Log.e(TAG, "onNext: " + s);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 会将多个被观察者合并，根据各个被观察者发送事件的顺序一个个结合起来，最终发送的事件数量会与源 Observable 中最少事件的数量一样
     */
    private void zip() {
        Observable.zip(Observable.intervalRange(1, 5, 1, 1, TimeUnit.SECONDS)
                        .map(new Function<Long, String>() {
                            @Override
                            public String apply(Long aLong) throws Exception {
                                return "A" + aLong;
                            }
                        }),
                Observable.intervalRange(1, 6, 1, 1, TimeUnit.SECONDS)
                        .map(new Function<Long, String>() {
                            @Override
                            public String apply(Long aLong) throws Exception {
                                return "B" + aLong;
                            }
                        }), new BiFunction<String, String, String>() {
                    @Override
                    public String apply(String s, String s2) throws Exception {
                        return s + s2;
                    }
                })
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        Log.e(TAG, "onNext: " + s);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * combineLatest() 的作用与 zip() 类似，但是 combineLatest() 发送事件的序列是与发送的时间线有关的，
     * 当 combineLatest() 中所有的 Observable 都发送了事件，只要其中有一个 Observable 发送事件，这个事件就会和其他 Observable 最近发送的事件结合起来发送
     * <p>
     * <p>
     * combineLatestDelayError() 就是多了延迟发送 onError() 功能
     */
    private void combineLatest() {
        Observable.combineLatest(
                Observable.intervalRange(1, 4, 1, 1, TimeUnit.SECONDS)
                        .map(new Function<Long, String>() {
                            @Override
                            public String apply(Long aLong) throws Exception {
                                String s1 = "A" + aLong;
                                Log.d(TAG, "===================A 发送的事件 " + s1);
                                return s1;
                            }
                        }),
                Observable.intervalRange(1, 5, 2, 2, TimeUnit.SECONDS)
                        .map(new Function<Long, String>() {
                            @Override
                            public String apply(Long aLong) throws Exception {
                                String s2 = "B" + aLong;
                                Log.d(TAG, "===================B 发送的事件 " + s2);
                                return s2;
                            }
                        }),
                new BiFunction<String, String, String>() {
                    @Override
                    public String apply(String s, String s2) throws Exception {
                        String res = s + s2;
                        return res;
                    }
                })
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "===================onSubscribe ");
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(TAG, "===================最终接收到的事件 " + s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "===================onError ");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "===================onComplete ");
                    }
                });
    }

    /**
     * 与 scan() 操作符的作用也是将发送数据以一定逻辑聚合起来，
     * 这两个的区别在于 scan() 每处理一次数据就会将事件发送给观察者，而 reduce() 会将所有数据聚合在一起才会发送事件给观察者。
     */
    private void reduce() {
        Observable.just(1, 2, 3, 4, 5)
                .reduce(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer, Integer integer2) throws Exception {
                        Log.e(TAG, "integer: " + integer);
                        Log.e(TAG, "integer2: " + integer2);
                        Log.e(TAG, "integer + integer2 : " + (integer + integer2));
                        return integer + integer2;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, "accept: " + integer);
                    }
                });
    }

    /**
     * 将数据收集到数据结构当中
     */
    private void collect() {
        Observable.just(1, 2, 3, 4, 5)
                .collect(new Callable<ArrayList<Integer>>() {
                    @Override
                    public ArrayList<Integer> call() throws Exception {
                        return new ArrayList<>();
                    }
                }, new BiConsumer<ArrayList<Integer>, Integer>() {
                    @Override
                    public void accept(ArrayList<Integer> integers, Integer integer) throws Exception {
                        integers.add(integer);
                    }
                })
                .subscribe(new BiConsumer<ArrayList<Integer>, Throwable>() {
                    @Override
                    public void accept(ArrayList<Integer> integers, Throwable throwable) throws Exception {
                        Log.e(TAG, "accept: " + integers);
                    }
                });

//        Observable.just(1, 2, 3, 4, 5)
//                .collectInto(new ArrayList<String>(), new BiConsumer<ArrayList<String>, Integer>() {
//                    @Override
//                    public void accept(ArrayList<String> strings, Integer integer) throws Exception {
//                        strings.add(String.valueOf(integer));
//                    }
//                })
//                .subscribe(new BiConsumer<ArrayList<String>, Throwable>() {
//                    @Override
//                    public void accept(ArrayList<String> strings, Throwable throwable) throws Exception {
//                        Log.e(TAG, "accept: " + strings);
//                    }
//                });
    }

    /**
     * 在发送事件之前追加事件，startWith() 追加一个事件，startWithArray() 可以追加多个事件。追加的事件会先发出
     */
    private void startWith() {
        Observable.just(1, 2, 3, 4, 5)
                .startWith(6)
                .startWithArray(7, 8, 9)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.e(TAG, "onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 返回被观察者发送事件的数量
     */
    private void count() {
        Observable.just(1, 2, 3, 4, 5)
                .count()
                .subscribe(new BiConsumer<Long, Throwable>() {
                    @Override
                    public void accept(Long aLong, Throwable throwable) throws Exception {
                        Log.e(TAG, "accept: " + aLong);
                    }
                });
    }
}
