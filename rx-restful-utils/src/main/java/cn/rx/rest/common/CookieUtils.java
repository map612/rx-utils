package cn.rx.rest.common;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {

    public final static String ACCESS_TOKEN = "access_token";

    private static Cookie getCookieFromRequest(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase(name))
                return cookie;
        }
        return null;
    }

    private static void addCookieToResponse(HttpServletResponse response, Cookie... cookies) {
        for (Cookie cookie : cookies) {
            response.addCookie(cookie);
        }
    }

    private static void removeCookieToResponse(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        addCookieToResponse(response, cookie);
    }

    /**
     * Extract token from user agent.
     *
     * @param request
     * @return
     * @throws NullPointerException When extract token failed.
     */
    public static String extractToken(HttpServletRequest request) throws NullPointerException {
        Cookie cookie = getCookieFromRequest(request, ACCESS_TOKEN);
        return cookie == null ? "" : cookie.getValue();
    }

    /**
     * Eliminate token from user agent.
     */
    public static void eliminateToken(HttpServletResponse response) {
        removeCookieToResponse(response, ACCESS_TOKEN);
    }

    /**
     * Embed token into user agent.
     * cookie depends on session
     */
    public static void embedToken(String token, HttpServletResponse response) {
        embedToken(token, response, null);
    }

    /**
     * Embed token into user agent.
     */
    public static void embedToken(String token, HttpServletResponse response, Integer expiry) {
        Cookie cookie = new Cookie(ACCESS_TOKEN, token);
//        cookie.setMaxAge();
        cookie.setPath("/");
        if (expiry != null) {
            cookie.setMaxAge(expiry);
        }
        addCookieToResponse(response, cookie);
    }
}
