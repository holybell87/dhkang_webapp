package my.web.dao;

import java.util.Date;

import my.web.domain.BoardVO;
import my.web.domain.UserVO;
import my.web.dto.LoginDTO;

public interface UserDAO {

	public UserVO login(LoginDTO dto) throws Exception;

	public void keepLogin(String uid, String sessionId, Date next) throws Exception;

	public UserVO checkUserWithSessionKey(String value) throws Exception;

	public void register(UserVO vo) throws Exception;

	public int selectUser(String uid) throws Exception;
}
