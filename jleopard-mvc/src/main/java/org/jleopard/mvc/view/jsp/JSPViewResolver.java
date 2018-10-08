/**
 * @author (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @date 2018-10-08  上午11:57
 *
 * <p>
 * Find a way for success and not make excuses for failure.
 * </p>
 */

package org.jleopard.mvc.view.jsp;

import org.jleopard.mvc.view.View;
import org.jleopard.mvc.view.ViewResolver;

public class JSPViewResolver implements ViewResolver {

    private View view;


    @Override
    public View resolveView(){
        return this.view;
    }

    public JSPViewResolver(View view) {
        this.view = view;
    }
    public JSPViewResolver() {
        this.view = new JSPView();
    }

    public void setView(View view) {
        this.view = view;
    }
}
