package my.web.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;

public class UploadFileUtils {

	/**
	 * 파일 업로드 순서
	 * 
	 * 1. UUID를 이용한 고유한 값 생성
	 * 2. UUID와 결합한 업로드 파일 이름 생성
	 * 3. 파일이 저장될 '년/월/일' 정보 생성
	 * 4. 업로드 기본 경로(uploadPath)와 '/년/월/일' 폴더 생성
	 * 5. 기본 경로 + 폴더 경로 + 파일 이름으로 파일 저장
	 * 6. 저장한 파일이 이미지 타입의 파일인가?
	 *   6-1. YES : 썸네일 이미지 생성, 생성된 썸네일 파일 이름 반환
	 *   6-2. NO : 생성된 파일 이름 반환
	 * */
	private static final Logger logger = LoggerFactory.getLogger(UploadFileUtils.class);

	// 파일의 저장경로, 원본파일명, 파일 데이터
//	public static String uploadFile(String uploadPath, String originalName, byte[] fileData)throws Exception{
//
//		return null;
//	}

	public static String uploadFile(String uploadPath, String originalName, byte[] fileData) throws Exception {
		UUID uid = UUID.randomUUID();
		String savedName = uid.toString() + "_" + originalName;
		String savedPath = calcPath(uploadPath);	// 저장될 경로를 계산

		File target = new File(uploadPath + savedPath, savedName);
		FileCopyUtils.copy(fileData, target);		// 원본 파일을 저장

		String formatName = originalName.substring(originalName.lastIndexOf(".") + 1);	// 원본 파일의 확장자
		String uploadedFileName = null;

		if(MediaUtils.getMediaType(formatName) != null) {
			// 썸네일 생성
			uploadedFileName = makeThumbnail(uploadPath, savedPath, savedName);
		} else {
			uploadedFileName = makeIcon(uploadPath, savedPath, savedName);
		}

		// 이미지 파일인 경우 : 썸네일 파일명
		// 일반파일인 경우 : 일반 파일명
		return uploadedFileName;
	}

	private static String makeIcon(String uploadPath, String path, String fileName) throws Exception {
		String iconName = uploadPath + path + File.separator + fileName;

		return iconName.substring(uploadPath.length()).replace(File.separatorChar, '/');
	}

	// 썸네일 생성
	private static String makeThumbnail(String uploadPath, String path, String fileName) throws Exception {
		BufferedImage sourceImg = ImageIO.read(new File(uploadPath + path, fileName));

		// 썸네일 이미지 파일의 높이를 100px로 동일하게 만듬
		BufferedImage destImg = Scalr.resize(sourceImg, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_HEIGHT, 100);
		String thumbnailName = uploadPath + path + File.separator + "s_" + fileName;

		File newFile = new File(thumbnailName);
		String formatName = fileName.substring(fileName.lastIndexOf(".") + 1);

		ImageIO.write(destImg, formatName.toUpperCase(), newFile);

		// 브라우저에서 윈도우의 경로로 사용하는 '\'문자가 정상적인 경로로 인식되지 않기 때문에 '/'로 치환
		return thumbnailName.substring(uploadPath.length()).replace(File.separatorChar, '/');
	}

	private static String calcPath(String uploadPath) {
		Calendar cal = Calendar.getInstance();

		String yearPath = File.separator + cal.get(Calendar.YEAR);
		String monthPath = yearPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.MONTH) + 1);
		String datePath = monthPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.DATE));

		makeDir(uploadPath, yearPath, monthPath, datePath);

		logger.info(datePath);

		return datePath;
	}

	// 폴더생성
	private static void makeDir(String uploadPath, String... paths) {
		if(new File(paths[paths.length - 1]).exists()) {
			return;
		}

		for(String path : paths) {
			File dirPath = new File(uploadPath + path);

			if (!dirPath.exists()) {
				dirPath.mkdir();
			}
		}
	}

}
