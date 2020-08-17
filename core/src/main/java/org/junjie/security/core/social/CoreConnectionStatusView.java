package org.junjie.security.core.social;

import com.google.gson.Gson;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("connect/status")
public class CoreConnectionStatusView extends AbstractView {

    private Gson gson = new Gson();

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        model.get("providerIds");
        Map<String, List<Connection<?>>> connectionMap = (Map<String, List<Connection<?>>>) model.get("connectionMap");
        HashMap<String, Boolean> result = new HashMap<>();
        for (String key : connectionMap.keySet()) {
            result.put(key, CollectionUtils.isNotEmpty(connectionMap.get(key)));
        }
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(gson.toJson(result));
    }
}
