package com.ablecloud.rxjavademo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.ablecloud.rxjavademo.bean.Person;
import com.ablecloud.rxjavademo.bean.Plan;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.observables.GroupedObservable;

/**
 * author : dukai
 * date  : 2018/8/15
 * describe:
 */
public class TranslateOperationActivity extends AppCompatActivity {
    private static final String TAG = "TranslateOperation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.map, R.id.flatMap, R.id.concatMap, R.id.buffer, R.id.groupBy, R.id.scan, R.id.window})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.map:
                map();
                break;
            case R.id.flatMap:
                flatMap();
                break;
            case R.id.concatMap:
                concatMap();
                break;
            case R.id.buffer:
                buffer();
                break;
            case R.id.groupBy:
                groupBy();
                break;
            case R.id.scan:
                scan();
                break;
            case R.id.window:
                window();
                break;
        }
    }

    private void map() {
        Observable.just(1, 2, 3).map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                return integer + "";
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @SuppressLint("LongLogTag")
            @Override
            public void onNext(String s) {
                Log.e(TAG, "onNext: " + s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete: ");
            }
        });
    }

    /**
     * 将事件序列中的元素进行整合加工，返回一个新的被观察者
     */
    private void flatMap() {
        ArrayList<String> actionList = new ArrayList<>();
        actionList.add("洗菜");
        actionList.add("做饭");

        Plan plan = new Plan();
        plan.setContent("吃饭");
        plan.setActionList(actionList);

        ArrayList<String> actionList1 = new ArrayList<>();
        actionList1.add("洗菜1");
        actionList1.add("做饭1");

        Plan plan1 = new Plan();
        plan1.setContent("吃饭1");
        plan1.setActionList(actionList1);

        Person person = new Person();
        ArrayList<Plan> planArrayList = new ArrayList<>();
        planArrayList.add(plan);
        planArrayList.add(plan1);
        person.setName("小明");
        person.setPlanList(planArrayList);

        ArrayList<Person> personArrayList = new ArrayList<>();
        personArrayList.add(person);

        Observable.fromIterable(personArrayList)
                .flatMap(new Function<Person, ObservableSource<Plan>>() {
                    @Override
                    public ObservableSource<Plan> apply(Person person) throws Exception {
                        return Observable.fromIterable(person.getPlanList());
                    }
                })
                .flatMap(new Function<Plan, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Plan plan) throws Exception {
                        return Observable.fromIterable(plan.getActionList());
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
                        Log.e(TAG, "onComplete: ");
                    }
                });

    }

    private void concatMap() {
        ArrayList<String> actionList = new ArrayList<>();
        actionList.add("洗菜");
        actionList.add("做饭");

        Plan plan = new Plan();
        plan.setContent("吃饭");
        plan.setActionList(actionList);

        ArrayList<String> actionList1 = new ArrayList<>();
        actionList1.add("洗菜1");
        actionList1.add("做饭1");

        Plan plan1 = new Plan();
        plan1.setContent("吃饭1");
        plan1.setActionList(actionList1);

        Person person = new Person();
        ArrayList<Plan> planArrayList = new ArrayList<>();
        planArrayList.add(plan);
        person.setName("小明");
        person.setPlanList(planArrayList);

        Person person1 = new Person();
        ArrayList<Plan> planArrayList1 = new ArrayList<>();
        planArrayList1.add(plan1);
        person1.setName("小红");
        person1.setPlanList(planArrayList1);

        ArrayList<Person> personArrayList = new ArrayList<>();
        personArrayList.add(person);
        personArrayList.add(person1);

        Observable.fromIterable(personArrayList)
                .concatMap(new Function<Person, ObservableSource<Plan>>() {
                    @Override
                    public ObservableSource<Plan> apply(Person person) {
                        if ("小明".equals(person.getName())) {
                            return Observable.fromIterable(person.getPlanList()).delay(10, TimeUnit.MILLISECONDS);
                        }
                        return Observable.fromIterable(person.getPlanList());
                    }
                })
                .subscribe(new Observer<Plan>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Plan plan) {
                        Log.e(TAG, "onNext: " + plan.getContent());
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
     * 从需要发送的事件当中获取一定数量的事件，并将这些事件放到缓冲区当中一并发出
     * buffer 有两个参数，一个是 count，另一个 skip。count 缓冲区元素的数量，skip 就代表缓冲区满了之后，发送下一次事件序列的时候要跳过多少元素
     */
    private void buffer() {
        Observable.just(1, 2, 3, 4, 5)
                .buffer(2, 2)
                .subscribe(new Observer<List<Integer>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Integer> integers) {
                        for (Integer integer : integers) {
                            Log.e(TAG, "onNext: " + integer);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete: ");
                    }
                });
    }

    /**
     * 将发送的数据进行分组，每个分组都会返回一个被观察者
     */
    private void groupBy() {
        Observable.just(5, 2, 3, 4, 1, 6, 8, 9, 7, 10)
                .groupBy(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer) throws Exception {
                        return integer % 3;
                    }
                })
                .subscribe(new Observer<GroupedObservable<Integer, Integer>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(final GroupedObservable<Integer, Integer> integerIntegerGroupedObservable) {
                        integerIntegerGroupedObservable.subscribe(new Observer<Integer>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Integer integer) {
                                Log.e(TAG, "====================GroupedObservable onNext  groupName: " +
                                        integerIntegerGroupedObservable.getKey() + " value: " + integer);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
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
     * 将数据以一定的逻辑聚合起来
     */
    private void scan() {
        Observable.just(1, 2, 3, 4, 5)
                .scan(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer, Integer integer2) throws Exception {
                        Log.e(TAG, "integer: " + integer);
                        Log.e(TAG, " integer2: " + integer2);
                        Log.e(TAG, " integer + integer2: " + (integer + integer2));
                        return integer + integer2;
                    }
                })
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
                        Log.e(TAG, "onComplete: ");
                    }
                });
    }

    /**
     * 发送指定数量的事件时，就将这些事件分为一组。window 中的 count 的参数就是代表指定的数量
     * 例如将 count 指定为2，那么每发2个数据就会将这2个数据分成一组。
     */
    private void window() {
        Observable.just(1, 2, 3, 4, 5)
                .window(2)
                .subscribe(new Observer<Observable<Integer>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Observable<Integer> integerObservable) {
                        Log.e(TAG, "onNext: ");
                        integerObservable.subscribe(new Observer<Integer>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Integer integer) {
                                Log.e(TAG, "integerObservable  onNext: " + integer);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {
                                Log.e(TAG, "integerObservable  onComplete: ");
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "  onComplete: ");
                    }
                });
    }
}
