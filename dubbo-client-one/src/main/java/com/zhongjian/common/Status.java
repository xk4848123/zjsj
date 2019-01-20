package com.zhongjian.common;

public enum Status {
	BussinessError(-1), Success(0), GeneralError(1), SeriousError(2), TokenError(3), SMSError(4);

	private final int statenum;

	Status(int statenum) {
		this.statenum = statenum;
	}
	public int getStatenum() {
	    return statenum;
	}
}
