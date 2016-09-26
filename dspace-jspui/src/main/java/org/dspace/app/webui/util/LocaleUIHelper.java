package org.dspace.app.webui.util;

import javax.servlet.http.HttpServletRequest;

public class LocaleUIHelper {
    /**
     * Private constructor - util class.
     */
    private LocaleUIHelper() {
    }

    /**
     * If current language is RTL returns ltrString, otherwise rtlString.
     *
     * @param request servlet request
     * @param ltrString string to return when language is LTR
     * @param rtlString string to return when language is RTL
     * @return ltrString or rtlString depending on current locale
     */
    public static String ifLtr(HttpServletRequest request, String ltrString, String rtlString) {
        if (UIUtil.isLtrLanguage(request)) {
            return ltrString;
        } else {
            return rtlString;
        }
    }
}
