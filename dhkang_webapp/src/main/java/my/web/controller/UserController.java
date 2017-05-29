package my.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.WebUtils;

import my.web.domain.UserVO;
import my.web.dto.LoginDTO;
import my.web.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Inject
	private UserService service;

	// 로그인 화면 호출
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public void loginGET(@ModelAttribute("dto") LoginDTO dto) {

	}

	// 로그인 처리
	@RequestMapping(value = "/loginPost", method = RequestMethod.POST)
	public void loginPOST(@RequestBody LoginDTO dto, HttpSession session, Model model) throws Exception {

		logger.info(dto.toString());

		UserVO vo = service.login(dto);

		if(vo == null) {
			return;
		}

		logger.info(vo.toString());
		model.addAttribute("userVO", vo);

		if(dto.isUseCookie()) {
			int amount = 60 * 60 * 24 * 7;	// 일주일
			Date sessionLimit = new Date(System.currentTimeMillis() + (1000 * amount));

			// 쿠키 정보를 DB에 저장
			service.keepLogin(vo.getUid(), session.getId(), sessionLimit);
		}
	}

	// 로그인 후 처리
	@RequestMapping(value = "/loginAfter", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> loginAfterPOST(@RequestParam final String login,
																@RequestParam final String dest) throws Exception {

		ResponseEntity<Map<String, Object>> entity = null;

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("dest", dest);

		try {
			if("fail".equals(login)) {
				result.put("result", "FAIL");
				//entity = new ResponseEntity<String>("FAIL", HttpStatus.OK);
			} else if("success".equals(login)) {
				result.put("result", "SUCCESS");
				//entity = new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
			}

			logger.info(result.toString());

			entity = new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("result", "UNKNOWN");
			entity = new ResponseEntity<Map<String, Object>>(result, HttpStatus.BAD_REQUEST);
		}

		logger.info(entity.toString());

		return entity;
	}

	// 로그아웃 처리
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

		Object obj = session.getAttribute("login");

		if(obj != null) {
			UserVO vo = (UserVO) obj;

			session.removeAttribute("login");
			session.invalidate();

			Cookie loginCookie = WebUtils.getCookie(request, "loginCookie");

			// 쿠키정보가 있는 경우 쿠키의 유효시간을 변경 
			if (loginCookie != null) {
				loginCookie.setPath("/");
				loginCookie.setMaxAge(0);
				response.addCookie(loginCookie);
				service.keepLogin(vo.getUid(), session.getId(), new Date());
			}
		}

		return "user/logout";
	}

	// 회원가입 화면 호출
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public void registerGET() {

		//return "register";
	}

	// 회원가입 처리
	@RequestMapping(value = "/registerPost", method = RequestMethod.POST)
	public ResponseEntity<String> registerPOST(@RequestBody UserVO vo) {

		ResponseEntity<String> entity = null;

		try {
			// 이미 등록된 회원이 있는지 체크
			int count = service.getUser(vo.getUid());

			if(count == 0) {
				service.register(vo);
				entity = new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
			} else {
				entity = new ResponseEntity<String>("FAIL", HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return entity;
	}
}
