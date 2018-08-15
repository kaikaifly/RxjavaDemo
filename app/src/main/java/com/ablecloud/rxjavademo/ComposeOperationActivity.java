package com.ablecloud.rxjavademo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * author : dukai
 * date  : 2018/8/15
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

    @OnClick({R.id.map, R.id.flatMap, R.id.concatMap})
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
        planArrayList.add(plan1);
        person.setName("小明");
        person.setPlanList(planArrayList);

        ArrayList<Person> personArrayList = new ArrayList<>();
        personArrayList.add(person);

        Observable.fromIterable(personArrayList)
                .flatMap(new Function<Person, ObservableSource<Plan>>() {
                    @Override
                    public ObservableSource<Plan> apply(Person person) {
                        if ("chan".equals(person.getName())) {
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


//        Observable.fromIterable(personArrayList)
//                .flatMap(new Function<Person, ObservableSource<Plan>>() {
//                    @Override
//                    public ObservableSource<Plan> apply(Person person) throws Exception {
//                        return Observable.fromIterable(person.getPlanList());
//                    }
//                })
//                .flatMap(new Function<Plan, ObservableSource<String>>() {
//                    @Override
//                    public ObservableSource<String> apply(Plan plan) throws Exception {
//                        return Observable.fromIterable(plan.getActionList());
//                    }
//                })
//                .subscribe(new Observer<String>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(String s) {
//                        Log.e(TAG, "onNext: " + s);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Log.e(TAG, "onComplete: ");
//                    }
//                });

    }


    public class Person {

        private String name;
        private List<Plan> planList = new ArrayList<>();

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Plan> getPlanList() {
            return planList;
        }

        public void setPlanList(List<Plan> planList) {
            this.planList = planList;
        }

    }

    public class Plan {
        private String content;
        private List<String> actionList = new ArrayList<>();

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public List<String> getActionList() {
            return actionList;
        }

        public void setActionList(List<String> actionList) {
            this.actionList = actionList;
        }
    }
}
