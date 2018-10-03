package org.jleopard.mvc;


import org.jleopard.mvc.core.AppContext;


/**
 * Unit test for simple App.
 */
public class AppTest {

    private static AppContext appContext = new DefaultAppContext();

    public static void main(String[] args) {
        System.out.println(appContext.getBasePackage());
    }
}
