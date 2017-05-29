package my.web.controller;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class CommonExceptionAdvice {

	private static final Logger logger = LoggerFactory.getLogger(CommonExceptionAdvice.class);

	// 모든 예외처리
//	@ExceptionHandler(Exception.class)
//	private ModelAndView errorModelAndView(Exception ex) {
//		ModelAndView modelAndView = new ModelAndView();
//		modelAndView.setViewName("/error_common");
//		modelAndView.addObject("exception", ex);
//
//		return modelAndView;
//	}

	// 응답코드에 따른 예외처리
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public ModelAndView not_found_Exception(Exception ex, HttpServletResponse response) {
		logger.info("Handlng Not_found_Exception - Catching: " + ex.getClass().getSimpleName());

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/error_common");
		modelAndView.addObject("exception", ex);

		return modelAndView;
	}

	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public ModelAndView internal_server_error_Exception(Exception ex, HttpServletResponse response) {
		logger.info("Handlng Internal_server_error_Exception - Catching: " + ex.getClass().getSimpleName());

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/error_common");
		modelAndView.addObject("exception", ex);

		return modelAndView;
	}
}