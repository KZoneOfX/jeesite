/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.web;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.beanvalidator.BeanValidators;
import com.thinkgem.jeesite.common.utils.*;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.entity.UserExtend;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.security.shiro.session.SessionDAO;
import com.thinkgem.jeesite.common.servlet.ValidateCodeServlet;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.security.FormAuthenticationFilter;
import com.thinkgem.jeesite.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 登录Controller
 * @author ThinkGem
 * @version 2013-5-31
 */
@Controller
public class LoginController extends BaseController{
	
	@Autowired
	private SessionDAO sessionDAO;

	@Autowired
	private SystemService systemService;
	/**
	 * 管理登录
	 */
	@RequestMapping(value = "${adminPath}/login", method = RequestMethod.GET)
	public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
		Principal principal = UserUtils.getPrincipal();
		System.out.println("hello");
//		// 默认页签模式
//		String tabmode = CookieUtils.getCookie(request, "tabmode");
//		if (tabmode == null){
//			CookieUtils.setCookie(response, "tabmode", "1");
//		}
		if (logger.isDebugEnabled()){
			logger.debug("login, active session size: {}", sessionDAO.getActiveSessions(false).size());
		}
		
		// 如果已登录，再次访问主页，则退出原账号。
		if (Global.TRUE.equals(Global.getConfig("notAllowRefreshIndex"))){
			CookieUtils.setCookie(response, "LOGINED", "false");
		}
		
		// 如果已经登录，则跳转到管理首页
		if(principal != null && !principal.isMobileLogin()){
			request.getSession().setAttribute("loginName",principal.getLoginName());
			return "redirect:" + adminPath;
		}
//		String view;
//		view = "/WEB-INF/views/modules/sys/sysLogin.jsp";
//		view = "classpath:";
//		view += "jar:file:/D:/GitHub/jeesite/src/main/webapp/WEB-INF/lib/jeesite.jar!";
//		view += "/"+getClass().getName().replaceAll("\\.", "/").replace(getClass().getSimpleName(), "")+"view/sysLogin";
//		view += ".jsp";
		return "modules/sys/sysLogin";
	}

	/**
	 * 登录失败，真正登录的POST请求由Filter完成
	 */
	@RequestMapping(value = "${adminPath}/login", method = RequestMethod.POST)
	public String loginFail(HttpServletRequest request, HttpServletResponse response, Model model) {
		Principal principal = UserUtils.getPrincipal();
		System.out.println("hello");
		// 如果已经登录，则跳转到管理首页
		if(principal != null){
			request.getSession().setAttribute("loginName",principal.getLoginName());

			return "redirect:" + adminPath;
		}

		String username = WebUtils.getCleanParam(request, FormAuthenticationFilter.DEFAULT_USERNAME_PARAM);
		boolean rememberMe = WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM);
		boolean mobile = WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_MOBILE_PARAM);
		String exception = (String)request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
		String message = (String)request.getAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM);
		
		if (StringUtils.isBlank(message) || StringUtils.equals(message, "null")){
			message = "用户或密码错误, 请重试.";
		}

		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, username);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM, rememberMe);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_MOBILE_PARAM, mobile);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME, exception);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM, message);
		
		if (logger.isDebugEnabled()){
			logger.debug("login fail, active session size: {}, message: {}, exception: {}", 
					sessionDAO.getActiveSessions(false).size(), message, exception);
		}
		
		// 非授权异常，登录失败，验证码加1。
		if (!UnauthorizedException.class.getName().equals(exception)){
			model.addAttribute("isValidateCodeLogin", isValidateCodeLogin(username, true, false));
		}
		
		// 验证失败清空验证码
		request.getSession().setAttribute(ValidateCodeServlet.VALIDATE_CODE, IdGen.uuid());
		
		// 如果是手机登录，则返回JSON字符串
		if (mobile){
	        return renderString(response, model);
		}
		
		return "modules/sys/sysLogin";
	}

	/**
	 * 登录成功，进入管理首页
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "${adminPath}")
	public String index(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("in the adminPath index");
		Principal principal = UserUtils.getPrincipal();

		// 登录成功后，验证码计算器清零
		isValidateCodeLogin(principal.getLoginName(), false, true);
		
		if (logger.isDebugEnabled()){
			logger.debug("show index, active session size: {}", sessionDAO.getActiveSessions(false).size());
		}

		// 如果已登录，再次访问主页，则退出原账号。
		if (Global.TRUE.equals(Global.getConfig("notAllowRefreshIndex"))){
			String logined = CookieUtils.getCookie(request, "LOGINED");
			logger.debug("logind:::"+logined);
			if (StringUtils.isBlank(logined) || "false".equals(logined)){
				CookieUtils.setCookie(response, "LOGINED", "true");
				logger.debug("CookieUtils.setCookie(response, \"LOGINED\", \"true\");");
			}else if (StringUtils.equals(logined, "true")){
				UserUtils.getSubject().logout();
				logger.debug("return \"redirect:\" + adminPath + \"/login\";");
				return "redirect:" + adminPath + "/login";
			}
		}
		
		// 如果是手机登录，则返回JSON字符串
		if (principal.isMobileLogin()){
			if (request.getParameter("login") != null){
				logger.debug("return renderString(response, principal);");
				return renderString(response, principal);
			}
			if (request.getParameter("index") != null){
				logger.debug("return \"modules/sys/sysIndex\"");
				return "modules/sys/sysIndex";
			}
			logger.debug("\t\t\treturn \"redirect:\" + adminPath + \"/login\";");
			return "redirect:" + adminPath + "/login";
		}
		
//		// 登录成功后，获取上次登录的当前站点ID
//		UserUtils.putCache("siteId", StringUtils.toLong(CookieUtils.getCookie(request, "siteId")));

//		System.out.println("==========================a");
//		try {
//			byte[] bytes = com.thinkgem.jeesite.common.utils.FileUtils.readFileToByteArray(
//					com.thinkgem.jeesite.common.utils.FileUtils.getFile("c:\\sxt.dmp"));
//			UserUtils.getSession().setAttribute("kkk", bytes);
//			UserUtils.getSession().setAttribute("kkk2", bytes);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
////		for (int i=0; i<1000000; i++){
////			//UserUtils.getSession().setAttribute("a", "a");
////			request.getSession().setAttribute("aaa", "aa");
////		}
//		System.out.println("==========================b");
		return "modules/sys/sysIndex";
	}
	
	/**
	 * 获取主题方案
	 */
	@RequestMapping(value = "/theme/{theme}")
	public String getThemeInCookie(@PathVariable String theme, HttpServletRequest request, HttpServletResponse response){
		if (StringUtils.isNotBlank(theme)){
			CookieUtils.setCookie(response, "theme", theme);
		}else{
			theme = CookieUtils.getCookie(request, "theme");
		}
		return "redirect:"+request.getParameter("url");
	}
	
	/**
	 * 是否是验证码登录
	 * @param useruame 用户名
	 * @param isFail 计数加1
	 * @param clean 计数清零
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isValidateCodeLogin(String useruame, boolean isFail, boolean clean){
		Map<String, Integer> loginFailMap = (Map<String, Integer>)CacheUtils.get("loginFailMap");
		if (loginFailMap==null){
			loginFailMap = Maps.newHashMap();
			CacheUtils.put("loginFailMap", loginFailMap);
		}
		Integer loginFailNum = loginFailMap.get(useruame);
		if (loginFailNum==null){
			loginFailNum = 0;
		}
		if (isFail){
			loginFailNum++;
			loginFailMap.put(useruame, loginFailNum);
		}
		if (clean){
			loginFailMap.remove(useruame);
		}
		return loginFailNum >= 3;
	}
	/**
	 * 管理登录
	 */
	@RequestMapping(value = "${adminPath}/register", method = RequestMethod.POST)
	public String register(HttpServletRequest request,
	                       @RequestParam(required=false) String class_no ,
	                       @RequestParam(required=false) String name,
	                       @RequestParam(required=false) String university_name,
	                       @RequestParam(required=false) String major_name,
	                       @RequestParam(required=false) String email,
	                       @RequestParam(required=false) String mobile,
	                       @RequestParam(required=false) String qq,
	                       @RequestParam(required=false) String wechat,
	                       @RequestParam(required=false) String classTeacherName,
	                       HttpServletResponse response, Model model) {

//		String major_name = request.getParameter("major_name");.
//		String email = request.getParameter("email");
//		String mobile = request.getParameter("mobile");
//		String classTeacherName = request.getParameter("classTeacherName");
		System.out.println("hello");
		UserExtend userExtend = new UserExtend();
		userExtend.setClassNo(class_no);
		userExtend.setUniversityName(university_name);
		userExtend.setMajorName(major_name);
		userExtend.setQq(qq);
		userExtend.setWechat(wechat);
		userExtend.setClassTeacherName(classTeacherName);
		final ResultMapper resultMapper = new ResultMapper();
		Map<String,String> datas = Maps.newHashMap();

		System.out.println(name);
		System.out.println(class_no);
		System.out.println(email);
		System.out.println(userExtend.toString());
		User user = new User();
		user.setPassword(SystemService.entryptPassword("123456"));
		user.setLoginName(class_no+name);
		user.setEmail(email);
		user.setPhone(mobile);
		user.setName(name);
		user.setDelFlag("0");
//		user.setOffice();
		Office office = new Office("f0d9f980f5c8416081b0db4386cb387e");

		Office company = new Office("c3141d6a55d54f1ab10292465a3d86a2");
		user.setCompany(company);
		user.setOffice(office);
		User user1 = new User("1");
		user.setCreateBy(user1);
		user.setUpdateBy(user1);
//		Role role   = new Role("22d873067bbb481499c41be46e408f67");//校友
		Role role =systemService.getRoleByName("校友");
		List<String> rolelist = Lists.newArrayList();
		rolelist.add(role.getId());
		user.setRoleIdList(rolelist);
		user.setRole(role);
		if ("true".equals(checkLoginName("", user.getLoginName()))) {
			user.setPassword(SystemService.entryptPassword("123456"));
			BeanValidators.validateWithException(validator, user);

			systemService.saveUser(user);
			user = systemService.getUserByLoginName(user.getLoginName());
			userExtend.setId(user.getId());
			systemService.insertUserExtend(userExtend);
			datas.put("username",class_no+name);
			resultMapper.setDatas(datas);
		}

//		BeanValidators.validateWithException(validator, user);
//		systemService.saveUser(user);
		if (StringUtils.isNoneBlank(name)) {
			return renderString(response, resultMapper.getResultMap());
		}
		return "redirect:" + adminPath + "/login";
	}

	public String checkLoginName(String oldLoginName, String loginName) {
		if (loginName !=null && loginName.equals(oldLoginName)) {
			return "true";
		} else if (loginName !=null && systemService.getUserByLoginName(loginName) == null) {
			return "true";
		}
		return "false";
	}

}
