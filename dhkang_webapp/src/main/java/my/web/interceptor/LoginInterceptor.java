package my.web.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoginInterceptor extends HandlerInterceptorAdapter {

	private static final String LOGIN = "login";
	private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();

		// HttpSession에 남아있는 정보가 있는 경우 정보 삭제
		if(session.getAttribute(LOGIN) != null) {
			logger.info("clear login data before");
			session.removeAttribute(LOGIN);
		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
							Object handler, ModelAndView modelAndView) throws Exception {

		HttpSession session = request.getSession();
		ModelMap modelMap = modelAndView.getModelMap();
		Object userVO = modelMap.get("userVO");

		// 로그인 성공
		if(userVO != null) {
			logger.info("new login success");
			session.setAttribute(LOGIN, userVO);

			// 사용자가 '자동 로그인'을 선택한 경우 쿠키를 생성하고 생성된 쿠키의 이름은 loginCookie로 지정
			if(request.getParameter("useCookie") != null) {
				logger.info("remember me................");
				Cookie loginCookie = new Cookie("loginCookie", session.getId());
				loginCookie.setPath("/");
				loginCookie.setMaxAge(60 * 60 * 24 * 7);	// 보관기간 일주일 (단위: 초)

				response.addCookie(loginCookie);
			} else {
				logger.info("do not remember me................");
				Cookie cookies[] = request.getCookies();
				for(int i = 0; i < cookies.length; i++) {
					Cookie cookie = cookies[i];

					if(cookie.getName().equals("loginCookie")) {
						cookie.setMaxAge(0); // 0으로 하면 쿠기 넘기면서 사라진다.

						response.addCookie(cookie); // 같은 이름으로 재생성해서 없앤다.
					}
				}
			}

			//response.sendRedirect("/");
			Object dest = session.getAttribute("dest");
			//logger.info(dest.toString());
			dest = dest != null ? (String) dest : "/";

			//response.sendRedirect(dest != null ? (String) dest : "/");
			response.sendRedirect("/user/loginAfter?login=success&dest="+dest);
		} else {
			response.sendRedirect("/user/loginAfter?login=fail&dest=");
		}
	}

}
