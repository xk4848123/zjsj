package com.zhongjian.servlet;

import javax.servlet.AsyncContext;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.zhongjian.common.GsonUtil;
import com.zhongjian.common.ResponseHandle;
import com.zhongjian.common.SpringContextHolder;
import com.zhongjian.common.Status;
import com.zhongjian.executor.ThreadPoolExecutorSingle;
import com.zhongjian.pojo.User;
import com.zhongjian.service.DeviceService;
import com.zhongjian.service.UserService;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

@WebServlet(value = "/otherhomepage", asyncSupported = true)
public class OtherHomePageServlet extends HttpServlet {

	private static Logger log = Logger.getLogger(OtherHomePageServlet.class);

	private UserService userService = (UserService) SpringContextHolder.getBean(UserService.class);

	private DeviceService deviceService = (DeviceService) SpringContextHolder.getBean(DeviceService.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		AsyncContext asyncContext = request.startAsync();
		ServletInputStream inputStream = request.getInputStream();
		inputStream.setReadListener(new ReadListener() {

			@Override
			public void onDataAvailable() throws IOException {
			}

			@Override
			public void onAllDataRead() {
				ThreadPoolExecutorSingle.executor.execute(() -> {
					String result = null;
					HashMap<String, Object> respData = null;
					try {
						String otherId = asyncContext.getRequest().getParameter("otherId");
						Integer otherIdInt = Integer.valueOf(otherId);
						String uid = asyncContext.getRequest().getParameter("uid");
						Integer uidint = Integer.valueOf(uid);
						// 耗时操作
						Date now = new Date();
						HashMap<String, Object> resultMap = new HashMap<>();
						User user = userService.getUserInfoByUid(otherIdInt);
						resultMap.put("user", user);
						resultMap.put("deviceCorrelation", deviceService.getBaseDeviceCanStealByOtherUid(uidint,user.getId()));
						respData = new HashMap<>();
						respData.put("data", resultMap);
						respData.put("error_code", Status.Success.getStatenum());
						result = GsonUtil.GsonString(respData);
					} catch (Exception e) {
						respData = new HashMap<>();
						respData.put("error_code", Status.GeneralError.getStatenum());
						respData.put("error_message","异常啦！");
						result = GsonUtil.GsonString(respData);
					}
					try {
						ResponseHandle.wrappedResponse(asyncContext.getResponse(), result);
					} catch (IOException e) {
						log.error("fail sevlet3.1: " + e.getMessage());
					}
					asyncContext.complete();
				});
			}

			@Override
			public void onError(Throwable t) {
				asyncContext.complete();
			}
		});

	}

}