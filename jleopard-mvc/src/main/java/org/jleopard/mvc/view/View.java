/**
 * @author (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @date 2018-10-03  下午8:04
 *
 * <p>
 * Find a way for success and not make excuses for failure.
 * </p>
 */

package org.jleopard.mvc.view;

import org.apache.commons.fileupload.FileUploadException;
import org.jleopard.mvc.core.bean.Action;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public interface View {

    default String getContentType() {
        return "text/html;charset=UTF-8";
    }

    void render(Map<Action, ?> var1, HttpServletRequest var2, HttpServletResponse var3) throws ServletException, IOException;
}
