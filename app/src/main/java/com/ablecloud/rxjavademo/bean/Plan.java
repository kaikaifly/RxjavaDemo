package com.ablecloud.rxjavademo.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * author : dukai
 * date  : 2018/8/16
 * describe:
 */
public class Plan {
    private String content;
    private List<String> actionList = new ArrayList<>();

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getActionList() {
        return actionList;
    }

    public void setActionList(List<String> actionList) {
        this.actionList = actionList;
    }
}