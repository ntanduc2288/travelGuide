package com.travel.travelguide.Bus.object;

import com.travel.travelguide.Object.RatingEntityObject;

/**
 * @author Duc Nguyen
 * @version 1.0
 * @since 10/11/16
 */
public class RatingChangedBusObject {
    RatingEntityObject ratingObject;

    public RatingChangedBusObject(RatingEntityObject ratingObject) {
        this.ratingObject = ratingObject;
    }

    public RatingEntityObject getRatingObject() {
        return ratingObject;
    }

    public void setRatingObject(RatingEntityObject ratingObject) {
        this.ratingObject = ratingObject;
    }
}
