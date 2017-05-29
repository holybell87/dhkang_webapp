package my.web.service;

import java.util.Date;

import my.web.domain.UserVO;
import my.web.dto.LoginDTO;

public interface UserService {

	public UserVO login(LoginDTO dto) throws Exception;

	public void keepLogin(String uid, String sessionId, Date next) throws Exception;

	public UserVO checkLoginBefore(String value) throws Exception;

	public void register(UserVO vo) throws Exception;

	public int getUser(String uid) throws Exception;
}
