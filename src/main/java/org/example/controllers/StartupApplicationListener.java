package org.example.controllers;

import org.example.utils.Persist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class StartupApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private Persist persist;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        persist.createInitialData();
    }
}