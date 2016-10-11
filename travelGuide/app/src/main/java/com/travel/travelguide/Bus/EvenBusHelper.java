package com.travel.travelguide.Bus;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;
import com.travel.travelguide.Bus.object.RatingChangedBusObject;
import com.travel.travelguide.Object.User;

/**
 * Created by user on 5/17/16.
 */
public class EvenBusHelper {
    private static EvenBusHelper instance;
    private Bus bus;
    public static EvenBusHelper getInstance(){
        if(instance == null){
            synchronized (EvenBusHelper.class){
                if(instance == null){
                    instance = new EvenBusHelper();
                }
            }
        }

        return instance;
    }

    private EvenBusHelper() {
        bus = new Bus(ThreadEnforcer.ANY);
    }

    public void registerEventBus(Object object){
        try{
            bus.register(object);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void unRegisterEventBus(Object object){
        try{
            bus.unregister(object);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void notifyUserDataChanged(User user) throws Exception{
        if(user != null){
            bus.post(user);
        }else {
            new Throwable("Current user data is null.");
        }
    }

    public void notifyRatingChanged(RatingChangedBusObject ratingChangedBusObject) throws Exception{
        if(ratingChangedBusObject != null){
            bus.post(ratingChangedBusObject);
        }else {
            new Throwable("Rating object can not be null.");
        }
    }


}
