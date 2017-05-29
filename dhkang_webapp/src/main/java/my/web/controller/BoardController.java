package my.web.controller;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import my.web.domain.BoardVO;
import my.web.domain.Criteria;
import my.web.domain.PageMaker;
import my.web.domain.SearchCriteria;
import my.web.service.BoardService;

@Controller
@RequestMapping("/board/*")
public class BoardController {

	private static final Logger logger = LoggerFactory.getLogger(BoardController.class);

	@Inject
	private BoardService service;

	// 글 입력 화면
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public void registerGET(BoardVO board, Model model) throws Exception {
		logger.info("register get ...........");
	}

	// 글 등록 후 전체글 조회
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String registerPOST(BoardVO board, RedirectAttributes rttr) throws Exception {
		logger.info("register post ...........");
		logger.info(board.toString());

		service.regist(board);

		// 한 번만 사용되는 데이터 전송
		rttr.addFlashAttribute("msg", "success");

		// page 경로 (/WEB-INF/views/board/success.jsp)
		//return "/board/success";

		return "redirect:/board/list";
	}

	// 전체글 조회
//	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
//	public void listAll(Model model) throws Exception {
//		logger.info("show all list ...........");
//
//		model.addAttribute("list", service.listAll());
//	}

	// 글 상세 조회
//	@RequestMapping(value = "/read", method = RequestMethod.GET)
//	public void read(@RequestParam("idx") int idx, Model model) throws Exception {
//		logger.info("read ...........");
//
//		model.addAttribute(service.read(idx));
//	}

	// 글 삭제 후 전체글 조회
//	@RequestMapping(value = "/remove", method = RequestMethod.POST)
//	public String remove(@RequestParam("idx") int idx, RedirectAttributes rttr) throws Exception {
//		logger.info("remove ...........");
//
//		service.remove(idx);
//
//		rttr.addFlashAttribute("msg", "success");
//
//		return "redirect:/board/listAll";
//	}

	// 글 수정 화면
//	@RequestMapping(value = "/modify", method = RequestMethod.GET)
//	public void modifyGET(int idx, Model model) throws Exception {
//		logger.info("modify get ...........");
//
//		model.addAttribute(service.read(idx));
//	}

	// 글 수정
//	@RequestMapping(value = "/modify", method = RequestMethod.POST)
//	public String modifyPOST(BoardVO board, RedirectAttributes rttr) throws Exception {
//		logger.info("modify post ...........");
//
//		service.modify(board);
//
//		rttr.addFlashAttribute("msg", "success");
//
//		return "redirect:/board/listAll";
//	}

	// 전체 글 페이징
//	@RequestMapping(value = "/listCri", method = RequestMethod.GET)
//	public void listAll(Criteria cri, Model model) throws Exception {
//		logger.info("show list Page with Criteria......................");
//
//		model.addAttribute("list", service.listCriteria(cri));
//	}

	// 전체 글 페이징
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public void listPage(@ModelAttribute("cri") SearchCriteria cri, Model model) throws Exception {
		logger.info(cri.toString());

		//model.addAttribute("list", service.listCriteria(cri));
		model.addAttribute("list", service.listSearchCriteria(cri));
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		//pageMaker.setTotalCount(service.listCountCriteria(cri));
		pageMaker.setTotalCount(service.listSearchCount(cri));

		model.addAttribute("pageMaker", pageMaker);
	}

	// 글 상세 조회 (+ 페이징)
	@RequestMapping(value = "/readPage", method = RequestMethod.GET)
	public void read(@RequestParam("idx") int idx, @ModelAttribute("cri") SearchCriteria cri, Model model) throws Exception {
		logger.info("read ...........");

		model.addAttribute(service.read(idx));
	}

	// 글 삭제 후 전체글 조회 (+ 페이징)
	@RequestMapping(value = "/removePage", method = RequestMethod.POST)
	public String remove(@RequestParam("idx") int idx, SearchCriteria cri, RedirectAttributes rttr) throws Exception {
		logger.info("remove ...........");

		service.remove(idx);

		rttr.addAttribute("page", cri.getPage());
		rttr.addAttribute("perPageNum", cri.getPerPageNum());
		rttr.addAttribute("searchType", cri.getSearchType());
		rttr.addAttribute("keyword", cri.getKeyword());

		rttr.addFlashAttribute("msg", "success");

		return "redirect:/board/list";
	}

	@RequestMapping(value = "/modifyPage", method = RequestMethod.GET)
	public void modifyPagingGET(@RequestParam("idx") int idx, @ModelAttribute("cri") SearchCriteria cri, Model model) throws Exception {
		model.addAttribute(service.read(idx));
	}

	@RequestMapping(value = "/modifyPage", method = RequestMethod.POST)
	public String modifyPagingPOST(BoardVO board, SearchCriteria cri, RedirectAttributes rttr) throws Exception {
		service.modify(board);

		rttr.addAttribute("page", cri.getPage());
		rttr.addAttribute("perPageNum", cri.getPerPageNum());
		rttr.addAttribute("searchType", cri.getSearchType());
		rttr.addAttribute("keyword", cri.getKeyword());

		rttr.addFlashAttribute("msg", "success");

		logger.info(rttr.toString());

		return "redirect:/board/list";
	}

	@RequestMapping("/getAttach/{idx}")
	@ResponseBody
	public List<String> getAttach(@PathVariable("idx") int idx) throws Exception {
		return service.getAttach(idx);
	}
}