package vip.jokor.im.pages.main.main_page.friends;

import vip.jokor.im.model.bean.AppliesBean;

public class ApplyEvent {

    AppliesBean appliesBean;

    public ApplyEvent(AppliesBean appliesBean) {
        this.appliesBean = appliesBean;
    }

    public AppliesBean getAppliesBean() {
        return appliesBean;
    }

    public void setAppliesBean(AppliesBean appliesBean) {
        this.appliesBean = appliesBean;
    }
}
