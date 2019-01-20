package com.zhongjian.executor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorSingle {
	
	public static ThreadPoolExecutor executor = new ThreadPoolExecutor(4, 20, 50000L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(100));
}
