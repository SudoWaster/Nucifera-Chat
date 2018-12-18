package pl.cezaryregec.filter;

import javax.servlet.http.HttpServletRequest;

public class FingerprintFactory {

    /**
     * Creates client fingerprint
     *
     * @return String of distinct user client data
     */
    public static String create(HttpServletRequest request) {
        return request.getRemoteAddr() + "\n" + request.getRemoteHost() + "\n" + request.getHeader("User-Agent");
    }

}
