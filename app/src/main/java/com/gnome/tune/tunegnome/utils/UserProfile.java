package com.gnome.tune.tunegnome.utils;


public class UserProfile {

    private Integer callRingtoneSoundLevel;
    private Integer startTimeHours;
    private Integer startTimeMinutes;
    private Integer endTimeHours;
    private Integer endTimeMinutes;

    private Integer id;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {

        return id;
    }

    public Integer getCallRingtoneSoundLevel() {
        return callRingtoneSoundLevel;
    }

    public Integer getStartTimeHours() {
        return startTimeHours;
    }

    public Integer getStartTimeMinutes() {
        return startTimeMinutes;
    }

    public Integer getEndTimeHours() {
        return endTimeHours;
    }

    public Integer getEndTimeMinutes() {
        return endTimeMinutes;
    }

    public void setCallRingtoneSoundLevel(Integer callRingtoneSoundLevel) {
        this.callRingtoneSoundLevel = callRingtoneSoundLevel;
    }

    public void setStartTimeHours(Integer startTimeHours) {
        this.startTimeHours = startTimeHours;
    }

    public void setStartTimeMinutes(Integer startTimeMinutes) {
        this.startTimeMinutes = startTimeMinutes;
    }

    public void setEndTimeHours(Integer endTimeHours) {
        this.endTimeHours = endTimeHours;
    }

    public void setEndTimeMinutes(Integer endTimeMinutes) {
        this.endTimeMinutes = endTimeMinutes;
    }

    public UserProfile() {

    }

    @Override
    public String toString() {
        return "From: " + startTimeHours + ":" + startTimeMinutes +
                " to: " + endTimeHours + ":" + endTimeMinutes +
                " ringtone level: " + callRingtoneSoundLevel;
    }
}
