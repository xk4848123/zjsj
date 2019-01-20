package com.zhongjian.common;

public class ResultUtil {
	public static Result<Object> success(Object object) {
		Result<Object> result = new Result<Object>();
		result.setError_code(Status.Success.getStatenum());
		result.setError_message("成功");
		result.setData(object);
		return result;
	}

	public static Result<Object> success() {
		return success(null);
	}

	public static Result<Object> error(Integer code, String msg) {
		Result<Object> result = new Result<Object>();
		result.setError_code(code);
		result.setError_message(msg);
		result.setData(null);
		return result;
	}

	public static Result<Object> error(Integer code, String msg, Object object) {
		Result<Object> result = new Result<Object>();
		result.setError_code(code);
		result.setError_message(msg);
		result.setData(object);
		return result;
	}

}