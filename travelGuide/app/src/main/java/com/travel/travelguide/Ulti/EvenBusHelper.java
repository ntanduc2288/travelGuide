package com.travel.travelguide.Ulti;

import com.squareup.otto.Bus;
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
        bus = new Bus();
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


}
