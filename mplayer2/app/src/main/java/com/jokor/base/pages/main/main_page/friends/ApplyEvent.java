package com.jokor.base.pages.main.main_page.friends;

import com.jokor.base.model.bean.AppliesBean;

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
