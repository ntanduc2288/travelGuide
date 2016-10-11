package com.travel.travelguide.Bus.object;

import com.travel.travelguide.Object.entity.Rating;

/**
 * @author Duc Nguyen
 * @version 1.0
 * @since 10/11/16
 */
public class RatingChangedBusObject {
    Rating ratingObject;
    float averageNumber = 0;

    public RatingChangedBusObject(Rating ratingObject, float averageNumber) {
        this.ratingObject = ratingObject;
        this.averageNumber = averageNumber;
    }

    public Rating getRatingObject() {
        return ratingObject;
    }

    public void setRatingObject(Rating ratingObject) {
        this.ratingObject = ratingObject;
    }

    public float getAverageNumber() {
        return averageNumber;
    }

    public void setAverageNumber(float averageNumber) {
        this.averageNumber = averageNumber;
    }
}
