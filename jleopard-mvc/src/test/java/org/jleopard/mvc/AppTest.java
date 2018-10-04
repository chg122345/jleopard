package org.jleopard.mvc;


import org.jleopard.mvc.core.ApplicationInitializer;


/**
 * Unit test for simple App.
 */
public class AppTest {

    private static ApplicationInitializer appContext = new DefaultAppContext();

    public static void main(String[] args) {
        System.out.println(appContext.getBasePackage());
    }
}
