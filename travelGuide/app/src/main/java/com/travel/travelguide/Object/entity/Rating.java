package com.travel.travelguide.Object.entity;

import com.travel.travelguide.Ulti.Constants;

/**
 * @author Duc Nguyen
 * @version 1.0
 * @since 10/11/16
 */
public final class Rating {
    String FromUserId = Constants.EMPTY_STRING;
    String ToUserId = Constants.EMPTY_STRING;
    float RatingNumber = 0;

    public Rating() {
    }

    public Rating(String fromUserId, String toUserId, float ratingNumber) {
        this.FromUserId = fromUserId;
        this.ToUserId = toUserId;
        this.RatingNumber = ratingNumber;
    }

    public String getFromUserId() {
        return FromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.FromUserId = fromUserId;
    }

    public String getToUserId() {
        return ToUserId;
    }

    public void setToUserId(String toUserId) {
        this.ToUserId = toUserId;
    }

    public float getRatingNumber() {
        return RatingNumber;
    }

    public void setRatingNumber(float ratingNumber) {
        this.RatingNumber = ratingNumber;
    }
}
