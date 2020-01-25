package com.jokor.base.pages.main.main_page.square.circle.circle_data;

public class CircleData {
    private Circle circle;
    private CircleUser circleUser;

    public CircleData(Circle circle, CircleUser circleUser) {
        this.circle = circle;
        this.circleUser = circleUser;
    }

    public Circle getCircle() {
        return circle;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    public CircleUser getCircleUser() {
        return circleUser;
    }

    public void setCircleUser(CircleUser circleUser) {
        this.circleUser = circleUser;
    }
}
