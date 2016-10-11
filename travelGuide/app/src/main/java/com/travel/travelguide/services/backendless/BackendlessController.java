package com.travel.travelguide.services.backendless;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.travel.travelguide.Object.entity.Rating;

import java.util.ArrayList;

import rx.Observable;

/**
 * @author Duc Nguyen
 * @version 1.0
 * @since 10/11/16
 */
public class BackendlessController {

    public Observable<ArrayList<Rating>> getRatingEntityObject(String userId){
        return Observable.create(subscriber -> {
            String whereClause = "ToUserId = '" + userId + "'";
            BackendlessDataQuery dataQuery = new BackendlessDataQuery(whereClause);
            Backendless.Persistence.of(Rating.class).find(dataQuery, new AsyncCallback<BackendlessCollection<Rating>>() {
                @Override
                public void handleResponse(BackendlessCollection<Rating> response) {
                    ArrayList<Rating> objectArraylist = (ArrayList<Rating>) response.getData();
                    subscriber.onNext(objectArraylist);
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    subscriber.onError(new Throwable(fault.getMessage()));
                }
            });
        });
    }

    public Observable<Float> calculateAverageRatingNumber(ArrayList<Rating> ratingEntityObjects) {
        return Observable.create(subscriber -> {
            float averange = 0;
            if(ratingEntityObjects != null && ratingEntityObjects.size() > 0){
                float totalPoint = 0;
                for(Rating ratingEntityObject : ratingEntityObjects){
                    totalPoint += ratingEntityObject.getRatingNumber();
                }
                averange = totalPoint/ratingEntityObjects.size();
            }

            subscriber.onNext(averange);
        });
    }


    public Observable<Rating> submitRating(String fromUserId, String toUserId, float ratingNumber) {
        final Rating ratingObjectTmp = new Rating(fromUserId, toUserId, ratingNumber);

        return Observable.create(subscriber -> {
            Backendless.Persistence.of(Rating.class).save(ratingObjectTmp, new AsyncCallback<Rating>() {
                @Override
                public void handleResponse(Rating response) {
                    subscriber.onNext(response);
                    subscriber.onCompleted();
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    subscriber.onError(new Throwable(fault.getMessage()));
                }
            });


        });
    }
}
