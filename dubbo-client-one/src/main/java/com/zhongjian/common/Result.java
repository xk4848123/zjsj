package com.zhongjian.common;
public class Result<T>{
        /*错误码*/
        private Integer error_code;
        /*提示信息*/
        private String error_message;
        /*具体的内容*/
        private T data;

    public Integer getError_code() {
			return error_code;
		}

		public void setError_code(Integer error_code) {
			this.error_code = error_code;
		}

		public String getError_message() {
			return error_message;
		}

		public void setError_message(String error_message) {
			this.error_message = error_message;
		}

	public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}