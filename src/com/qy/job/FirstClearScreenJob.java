package com.qy.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.qy.service.ClearScreenService;

public class FirstClearScreenJob implements Job {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("第一次执行清屏任务:");
		new ClearScreenService().clearScreen();
	}
}
