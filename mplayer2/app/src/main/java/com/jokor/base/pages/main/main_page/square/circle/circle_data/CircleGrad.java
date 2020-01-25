package com.jokor.base.pages.main.main_page.square.circle.circle_data;

public class CircleGrad {

    /**
     * Id : 1
     * CircleId : 1
     * GradnName : 1级
     * GradCount : 0
     * IsManager : false
     */

    private long Id;
    private long CircleId;
    private String GradnName;
    private int GradCount;
    private boolean IsManager;

    public long getId() {
        return Id;
    }

    public void setId(long Id) {
        this.Id = Id;
    }

    public long getCircleId() {
        return CircleId;
    }

    public void setCircleId(long CircleId) {
        this.CircleId = CircleId;
    }

    public String getGradnName() {
        return GradnName;
    }

    public void setGradnName(String GradnName) {
        this.GradnName = GradnName;
    }

    public int getGradCount() {
        return GradCount;
    }

    public void setGradCount(int GradCount) {
        this.GradCount = GradCount;
    }

    public boolean isIsManager() {
        return IsManager;
    }

    public void setIsManager(boolean IsManager) {
        this.IsManager = IsManager;
    }
}
