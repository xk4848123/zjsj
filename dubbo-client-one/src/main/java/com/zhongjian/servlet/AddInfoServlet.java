package com.zhongjian.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gson.JsonPrimitive;
import com.zhongjian.common.GsonUtil;
import com.zhongjian.common.JsonReader;
import com.zhongjian.common.ResponseHandle;
import com.zhongjian.common.SpringContextHolder;
import com.zhongjian.common.Status;
import com.zhongjian.executor.ThreadPoolExecutorSingle;
import com.zhongjian.service.DeviceService;
import com.zhongjian.service.UserService;

@WebServlet(value = "/addinfo", asyncSupported = true)
public class AddInfoServlet extends HttpServlet {
	
	private static Logger log = Logger.getLogger(AddInfoServlet.class);
	
	private UserService userService = (UserService) SpringContextHolder.getBean(UserService.class);

	private DeviceService deviceService = (DeviceService) SpringContextHolder.getBean(DeviceService.class);
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
        
		AsyncContext asyncContext = request.startAsync();
		ServletInputStream inputStream = request.getInputStream();
		Map<String, JsonPrimitive> postMap = null;
		postMap = JsonReader.receivePost(inputStream);
		String realName = postMap.get("realname").getAsString();
		String phoneNum = postMap.get("phonenum").getAsString();
		Integer userId =  postMap.get("uid").getAsInt();
		inputStream.setReadListener(new ReadListener() {
			@Override
			public void onDataAvailable() throws IOException {
			}

			@Override
			public void onAllDataRead() {
				ThreadPoolExecutorSingle.executor.execute(() -> {
					String result = null;
					HashMap<String, Object> respData = null;
					// 收矿
					try {
					HashMap<String, Object> resultMap = new HashMap<>();
					Integer additionInfo = userService.additionInfo(realName, phoneNum, userId);
					Boolean isSuccess = false;
					if(additionInfo ==1){
						isSuccess =true;
					}
					resultMap.put("isSuccess", isSuccess);
					respData = new HashMap<>();
					respData.put("data", resultMap);
					respData.put("error_code", Status.Success.getStatenum());
					result = GsonUtil.GsonString(respData);
					}catch (Exception e) {
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
