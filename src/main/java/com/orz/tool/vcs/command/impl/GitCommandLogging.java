package com.orz.tool.vcs.command.impl;

import java.io.File;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.orz.tool.vcs.command.GitCommandException;
import com.orz.tool.vcs.entity.FileElement;
import com.orz.tool.vcs.ui.Log;

/**
 * GitCommand的日志切面
 * 
 * @author obladi
 * 
 */
@Aspect
@Component
public class GitCommandLogging {

	@Autowired
	private Log logging;

	@Autowired
	@Qualifier("location")
	private File location;

	@Around("dcommit()")
	public void dcommit(ProceedingJoinPoint point) throws Throwable {
		this.logging.info("执行dcommit开始  路径:" + this.location);
		try{
			point.proceed();
		}catch(GitCommandException e){
			this.logging.error(e.getMessage());
		}
		this.logging.info("执行dcommit结束  路径:" + this.location);
	}

	@Around("commit()")
	public void commit(ProceedingJoinPoint point) throws Throwable {
		this.logging.info("执行commit开始  路径:" + this.location);
		try{
			point.proceed();
		}catch(GitCommandException e){
			this.logging.error(e.getMessage());
		}
		this.logging.info("执行commit结束  路径:" + this.location);
	}

	@Around("add()")
	public void add(ProceedingJoinPoint point) throws Throwable {
		FileElement element = (FileElement) point.getArgs()[0];
		this.logging.info("执行add开始  路径:" + this.location + ", 文件:" + element.getSrc());
		try{
			point.proceed();
		}catch(GitCommandException e){
			this.logging.error(e.getMessage());
		}
		this.logging.info("执行add结束  路径:" + this.location + ", 文件:" + element.getSrc());
	}

	@Around("checkout()")
	public void checkout(ProceedingJoinPoint point) throws Throwable {
		FileElement element = (FileElement) point.getArgs()[0];
		this.logging.info("执行checkout开始  路径:" + this.location + ", 文件:" + element.getSrc());
		try{
			point.proceed();
		}catch(GitCommandException e){
			this.logging.error(e.getMessage());
		}
		this.logging.info("执行checkout结束  路径:" + this.location + ", 文件:" + element.getSrc());
	}

	@Around("reset()")
	public void reset(ProceedingJoinPoint point) throws Throwable {
		FileElement element = (FileElement) point.getArgs()[0];
		this.logging.info("执行reset开始  路径:" + this.location + ", 文件:" + element.getSrc());
		try{
			point.proceed();
		}catch(GitCommandException e){
			this.logging.error(e.getMessage());
		}
		this.logging.info("执行reset结束  路径:" + this.location + ", 文件:" + element.getSrc());
	}

	@Around("status()")
	public Object status(ProceedingJoinPoint point) throws Throwable {
		Object result = null;
		this.logging.info("执行status开始  路径:" + this.location );
		try{
			result = point.proceed();
		}catch(GitCommandException e){
			this.logging.error(e.getMessage());
		}
		this.logging.info("执行status结束  路径:" + this.location);
		return result;
	}

	@Pointcut("execution(* com.orz.tool.vcs.command.impl.GitCommandImpl.dcommit(..))")
	private void dcommit() {

	}

	@Pointcut("execution(* com.orz.tool.vcs.command.impl.GitCommandImpl.commit(..))")
	private void commit() {

	}

	@Pointcut("execution(* com.orz.tool.vcs.command.impl.GitCommandImpl.add(..))")
	private void add() {
		
	}

	@Pointcut("execution(* com.orz.tool.vcs.command.impl.GitCommandImpl.checkout(..))")
	private void checkout() {

	}

	@Pointcut("execution(* com.orz.tool.vcs.command.impl.GitCommandImpl.reset(..))")
	private void reset() {

	}

	@Pointcut("execution(* com.orz.tool.vcs.command.impl.GitCommandImpl.status(..))")
	private void status() {
		
	}

}
