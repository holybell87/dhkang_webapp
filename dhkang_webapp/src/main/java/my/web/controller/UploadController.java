package my.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import my.web.util.MediaUtils;
import my.web.util.UploadFileUtils;

@Controller
public class UploadController {
	private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

	@Resource(name = "uploadPath")
	private String uploadPath;
	
	// 확장자
	//String ext = item.getName().substring(item.getName().lastIndexOf(".")+1);

	// 파일 기본경로
	//String defaultPath = request.getServletContext().getRealPath("/");

	// 파일 기본경로 _ 상세경로
	//String path = defaultPath + "uploadedFiles" + File.separator;

	@RequestMapping(value = "/uploadForm", method = RequestMethod.GET)
	public void uploadForm() {

	}

	@RequestMapping(value = "/uploadForm", method = RequestMethod.POST)
	public String uploadForm(MultipartFile file, Model model) throws Exception {
		logger.info("originalName: " + file.getOriginalFilename());	// 파일명
		logger.info("size: " + file.getSize());						// 단위 : byte
		logger.info("contentType: " + file.getContentType());		// 파일 타입

		String savedName = uploadFile(file.getOriginalFilename(), file.getBytes());
		model.addAttribute("savedName", savedName);

		return "uploadResult";
	}

	// 파일 업로드
	private String uploadFile(String originalName, byte[] fileData) throws Exception {
		UUID uid = UUID.randomUUID();
		String savedName = uid.toString() + "_" + originalName;

		File target = new File(uploadPath, savedName);
		FileCopyUtils.copy(fileData, target);	// 데이터가 담긴 바이트의 배열(in)을 파일에 기록

		return savedName;
	}

	@RequestMapping(value = "/uploadAjax", method = RequestMethod.GET)
	public void uploadAjax() {

	}

	@ResponseBody
	@RequestMapping(value ="/uploadAjax", method=RequestMethod.POST, produces = "text/plain; charset=UTF-8")
	public ResponseEntity<String> uploadAjax(MultipartFile file) throws Exception {
		logger.info("originalName: " + file.getOriginalFilename());	// 파일명
		logger.info("size: " + file.getSize());						// 단위 : byte
		logger.info("contentType: " + file.getContentType());		// 파일 타입

		return new ResponseEntity<>(UploadFileUtils.uploadFile(uploadPath, file.getOriginalFilename(), file.getBytes()),
									HttpStatus.CREATED);
	}

	@ResponseBody
	@RequestMapping("/displayFile")
	public ResponseEntity<byte[]> displayFile(String fileName) throws Exception {
		InputStream in = null;
		ResponseEntity<byte[]> entity = null;

		logger.info("FILE NAME: " + fileName);	// /년/월/일/파일명

		try {
			String formatName = fileName.substring(fileName.lastIndexOf(".") + 1);	// 확장자
			MediaType mType = MediaUtils.getMediaType(formatName);
			HttpHeaders headers = new HttpHeaders();
			in = new FileInputStream(uploadPath + fileName);

			if(mType != null) {
				// 이미지 타입인 경우
				headers.setContentType(mType);
			} else {
				// 이미지 타입이 아닌 경우
				fileName = fileName.substring(fileName.indexOf("_") + 1);

				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
				headers.add("Content-Disposition",
							"attachment; filename=\""
							+ new String(fileName.getBytes("UTF-8"), "ISO-8859-1")	// 한글 인코딩
							+ "\"");
			}

			entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in), headers, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
		} finally {
			in.close();
		}

		return entity;
	}

	// 파일 삭제
	@ResponseBody
	@RequestMapping(value = "/deleteFile", method = RequestMethod.POST)
	public ResponseEntity<String> deleteFile(String fileName) {
		logger.info("delete file: " + fileName);

		String formatName = fileName.substring(fileName.lastIndexOf(".") + 1); // 확장자
		MediaType mType = MediaUtils.getMediaType(formatName);

		// 이미지 파일
		if(mType != null) {
			String front = fileName.substring(0, 12);	// '/년/월/일' 추출
			String end = fileName.substring(14);		// 's_' 제거 (썸네일이 아닌 원본 파일명 추출)
			new File(uploadPath + (front + end).replace('/', File.separatorChar)).delete();
		}

		// 일반 파일
		new File(uploadPath + fileName.replace('/', File.separatorChar)).delete();

		return new ResponseEntity<String>("deleted", HttpStatus.OK);
	}


	@ResponseBody
	@RequestMapping(value = "/deleteAllFiles", method = RequestMethod.POST)
	public ResponseEntity<String> deleteFile(@RequestParam("files[]") String[] files) {
		logger.info("delete all files: " + files);

		if(files == null || files.length == 0) {
			return new ResponseEntity<String>("deleted", HttpStatus.OK);
		}

		for(String fileName : files) {
			String formatName = fileName.substring(fileName.lastIndexOf(".") + 1);	// 확장자

			MediaType mType = MediaUtils.getMediaType(formatName);

			if(mType != null) {
				String front = fileName.substring(0, 12);	// '/년/월/일' 추출
				String end = fileName.substring(14);		// 's_' 제거 (썸네일이 아닌 원본 파일명 추출)

				new File(uploadPath + (front + end).replace('/', File.separatorChar)).delete();
			}

			new File(uploadPath + fileName.replace('/', File.separatorChar)).delete();
		}

		return new ResponseEntity<String>("deleted", HttpStatus.OK);
	}
}
