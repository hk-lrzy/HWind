package org.hklrzy.hwind.view;

import org.hklrzy.hwind.HWindModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created 2018/7/24.
 * Author ke.hao
 */
public class JSTLViewResolver implements ViewResolver {


    @Override
    public void resolver(HttpServletRequest request, HttpServletResponse response, HWindModelAndView modelAndView) throws Exception {
        mergeAttribute(request, modelAndView.getModel());
        request.getRequestDispatcher(modelAndView.getViewName()).forward(request, response);
    }

    private void mergeAttribute(HttpServletRequest request, Map<String, Object> model) {
        for (Map.Entry<String, Object> entry : model.entrySet()) {
            request.setAttribute(entry.getKey(), entry.getValue());
        }
    }
}
