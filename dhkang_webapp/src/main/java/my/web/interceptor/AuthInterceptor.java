package my.web.interceptor;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

import my.web.domain.UserVO;
import my.web.service.UserService;

public class AuthInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);

	@Inject
	private UserService service;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();

		// 1. 현재 사용자의 세션에 login이 존재하지 않지만, 쿠키 중에서 loginCookie가 존재할 때 처리가 진행
		if(session.getAttribute("login") == null) {
			logger.info("current user is not logined");

			saveDest(request);

			Cookie loginCookie = WebUtils.getCookie(request, "loginCookie");

			// 2. 과거에 보관한 쿠키가 있다면 UserService 객체를 이용해 사용자의 정보가 존재하는지 확인
			if(loginCookie != null) {
				UserVO userVO = service.checkLoginBefore(loginCookie.getValue());
				logger.info("USERVO: " + userVO);

				// 3. 사용자의 정보가 존재하면 HttpSession에 다시 사용자의 정보를 저장
				if(userVO != null) {
					session.setAttribute("login", userVO);
					return true;
				}
			}

			response.sendRedirect("/user/login");
			return false;
		}

		return true;
	}

	// 원래 가려고 했던 URI 저장
	private void saveDest(HttpServletRequest req) {
		String uri = req.getRequestURI();
		String query = req.getQueryString();

		if(query == null || query.equals("null")) {
			query = "";
		} else {
			query = "?" + query;
		}

		if(req.getMethod().equals("GET")) {
			logger.info("dest: " + (uri + query));

			req.getSession().setAttribute("dest", uri + query);
		}
	}

}
