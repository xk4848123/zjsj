package com.zhongjian.common;

import java.io.IOException;

import javax.servlet.ServletResponse;

public class ResponseHandle {

	public static void wrappedResponse(ServletResponse resp,String result) throws IOException  {
    	resp.setContentType("text/json; charset=utf-8");
    	resp.getWriter().write(result);
	}
}
