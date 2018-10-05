package org.jleopard.mvc;


import java.util.regex.Pattern;


/**
 * Unit test for simple App.
 */
public class AppTest {


    public static void main(String[] args) {

        String pattern = "/user/.?";
        String content = "/user/2/55";
        System.out.println(Pattern.matches(pattern,content));
    }
}
