package com.epam.campus.gymcrm;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.epam.MainMenu;
import com.epam.campus.gymcrm.config.Config;
import com.epam.campus.gymcrm.facade.GymFacade;

public class App {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        GymFacade gymFacade = context.getBean(GymFacade.class);
        MainMenu menu = new MainMenu(gymFacade);
        menu.run();
        ((AnnotationConfigApplicationContext) context).close();
    }
}