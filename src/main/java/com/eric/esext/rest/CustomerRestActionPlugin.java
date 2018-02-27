package com.eric.esext.rest;


import org.elasticsearch.plugins.ActionPlugin;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.rest.RestHandler;

import java.util.Collections;
import java.util.List;

public class CustomerRestActionPlugin extends Plugin implements ActionPlugin {
    public CustomerRestActionPlugin(){
        super();
        System.out.println("Plugin for node name search");
    }


    @Override
    public List<Class<? extends RestHandler>> getRestHandlers() {
        return Collections.singletonList(CustomerRestAction.class);
    }
}
