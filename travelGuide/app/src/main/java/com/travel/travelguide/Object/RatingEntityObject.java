package com.travel.travelguide.Object;

import com.j256.ormlite.field.DatabaseField;
import com.travel.travelguide.Ulti.Constants;

/**
 * @author Duc Nguyen
 * @version 1.0
 * @since 10/11/16
 */
public class RatingEntityObject {
    @DatabaseField(columnName = "FromUserId")
    String fromUserId = Constants.EMPTY_STRING;
    @DatabaseField(columnName = "ToUserId")
    String toUserId = Constants.EMPTY_STRING;
    @DatabaseField(columnName = "RatingNumber")
    float ratingNumber = 0;

    public RatingEntityObject() {
    }

    public RatingEntityObject(String fromUserId, String toUserId, float ratingNumber) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.ratingNumber = ratingNumber;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public float getRatingNumber() {
        return ratingNumber;
    }

    public void setRatingNumber(float ratingNumber) {
        this.ratingNumber = ratingNumber;
    }
}
