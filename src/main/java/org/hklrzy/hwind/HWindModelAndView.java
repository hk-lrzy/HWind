package org.hklrzy.hwind;

import org.springframework.http.HttpStatus;
import org.springframework.ui.ModelMap;

import java.util.Map;


/**
 * Created 2018/7/5.
 * Author ke.hao
 * 视图转换层
 */
public class HWindModelAndView {

    private Object view;

    private ModelMap model;

    private HttpStatus status;

    private boolean cleared = false;

    public HWindModelAndView() {
    }

    public void setViewName(String viewName) {
        this.view = viewName;
    }

    public String getViewName() {
        return (this.view instanceof String ? (String) this.view : null);
    }

    public void setView(HWindView view) {
        this.view = view;
    }

    public HWindView getView() {
        return (this.view instanceof HWindView ? (HWindView) this.view : null);
    }

    public boolean hasView() {
        return (this.view != null);
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public ModelMap getModelMap() {
        if (this.model == null) {
            this.model = new ModelMap();
        }
        return this.model;
    }

    public Map<String, Object> getModel() {
        return getModelMap();
    }

    public HWindModelAndView addObject(String attributeName, Object attributeValue) {
        getModelMap().addAttribute(attributeName, attributeValue);
        return this;
    }
}
