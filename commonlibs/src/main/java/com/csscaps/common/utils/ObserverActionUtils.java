package com.csscaps.common.utils;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by tl on 2017/9/22.
 * 观察者模式工具类
 */

public class ObserverActionUtils {

    public static Map<String, Action1> mapAction = new HashMap<>();
    public static Map<String, Observable> mapObservable = new HashMap<>();

    public static void addAction(Action1 action1){
        mapAction.put(action1.getClass().toString(),action1);
    }

    public static void removeAction(Action1 action1){
        mapAction.remove(action1.getClass().toString());
    }


    public static void addObservable(Observable observable){
        mapObservable.put(observable.getClass().toString(),observable);
    }

    public static void removeObservable(Observable observable){
        mapObservable.remove(observable.getClass().toString());
    }

    /**
     * 订阅
     * @param observableClass  被观察者
     * @param actionClass 观察者
     */
    public static void subscribe(Class observableClass, Class actionClass) {
        Observable observable=   mapObservable.get(observableClass.toString());
        Action1 action1=mapAction.get(actionClass.toString());
        observable.subscribe(action1);
    }

    public static <T> void  subscribe(T t,Class actionClass){
        Observable observable=  Observable.just(t);
        Action1 action1=mapAction.get(actionClass.toString());
        observable.subscribe(action1);
    }

    public static <T> void  subscribe(T[] t,Class actionClass){
        Observable observable=  Observable.from(t);
        Action1 action1=mapAction.get(actionClass.toString());
        observable.subscribe(action1);
    }
}
