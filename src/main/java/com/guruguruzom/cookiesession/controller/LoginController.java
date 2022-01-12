package com.guruguruzom.cookiesession.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class LoginController {
	@PostMapping("/userlogin")
	public Map<String, Object> userlogin(HttpServletRequest request, @RequestBody UserVo userVo) {

		// UserDefault result = loginService.login(userDefault);

		Map<String, Object> response = new HashMap<>();
		response.put("success", "success");
		response.put("userSerialId", userVo.getUserSerial());
		HttpSession session = request.getSession();
		session.setAttribute("userSerialId", Integer.toString(userVo.getUserSerial()));
	}

	@GetMapping(value = { "main", "/main" })
	public String main(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String sessionUserSeq = (String) session.getAttribute("userSerialId");
		String cookieUserSeq = "";
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("userSaveId".equals(cookie.getName())) {
					cookieUserSeq = (String) cookie.getValue();
				}
			}
		}

		try {
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
		}

		String cookieStr = null;
		// 세션 쿠키 아무것도 존재하지 않으면 로그인페이지로 돌아감
		if (!StringUtils.isBlank(sessionUserSeq)) {
			cookieStr = sessionUserSeq;
		} else if (!StringUtils.isBlank(cookieUserSeq)) {
			session.setAttribute("userSerialId", cookieUserSeq);
			cookieStr = cookieUserSeq;
		}

		if (StringUtils.isBlank(cookieStr) || !StringUtils.isNumeric(cookieStr)) {
			return "login/login";
		}

		UserVo result = null;//유저 정보 가져오기
		if (result == null || result.getUserID() == null) {
			return "login/login";
		}

		request.setAttribute("userId", result.getUserID());
		request.setAttribute("userRole", result.getUserRole());
		session.setAttribute("userRole", result.getUserRole());

		return "layout/main";
	}
}
