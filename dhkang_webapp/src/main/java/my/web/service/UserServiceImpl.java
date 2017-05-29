package my.web.service;

import java.util.Date;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import my.web.dao.UserDAO;
import my.web.domain.UserVO;
import my.web.dto.LoginDTO;

@Service
public class UserServiceImpl implements UserService {

	@Inject
	private UserDAO dao;

	@Override
	public UserVO login(LoginDTO dto) throws Exception {
		return dao.login(dto);
	}

	@Override
	public void keepLogin(String uid, String sessionId, Date next) throws Exception {
		dao.keepLogin(uid, sessionId, next);
	}

	@Override
	public UserVO checkLoginBefore(String value) throws Exception {
		return dao.checkUserWithSessionKey(value);
	}

	@Override
	public void register(UserVO vo) throws Exception {
		dao.register(vo);
	}

	@Override
	public int getUser(String uid) throws Exception {
		return dao.selectUser(uid);
	}
}
