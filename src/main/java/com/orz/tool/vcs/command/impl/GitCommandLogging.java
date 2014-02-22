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
 * GitCommand����־����
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
		this.logging.info("ִ��dcommit��ʼ  ·��:" + this.location);
		try{
			point.proceed();
		}catch(GitCommandException e){
			this.logging.error(e.getMessage());
		}
		this.logging.info("ִ��dcommit����  ·��:" + this.location);
	}

	@Around("commit()")
	public void commit(ProceedingJoinPoint point) throws Throwable {
		this.logging.info("ִ��commit��ʼ  ·��:" + this.location);
		try{
			point.proceed();
		}catch(GitCommandException e){
			this.logging.error(e.getMessage());
		}
		this.logging.info("ִ��commit����  ·��:" + this.location);
	}

	@Around("add()")
	public void add(ProceedingJoinPoint point) throws Throwable {
		FileElement element = (FileElement) point.getArgs()[0];
		this.logging.info("ִ��add��ʼ  ·��:" + this.location + ", �ļ�:" + element.getSrc());
		try{
			point.proceed();
		}catch(GitCommandException e){
			this.logging.error(e.getMessage());
		}
		this.logging.info("ִ��add����  ·��:" + this.location + ", �ļ�:" + element.getSrc());
	}

	@Around("checkout()")
	public void checkout(ProceedingJoinPoint point) throws Throwable {
		FileElement element = (FileElement) point.getArgs()[0];
		this.logging.info("ִ��checkout��ʼ  ·��:" + this.location + ", �ļ�:" + element.getSrc());
		try{
			point.proceed();
		}catch(GitCommandException e){
			this.logging.error(e.getMessage());
		}
		this.logging.info("ִ��checkout����  ·��:" + this.location + ", �ļ�:" + element.getSrc());
	}

	@Around("reset()")
	public void reset(ProceedingJoinPoint point) throws Throwable {
		FileElement element = (FileElement) point.getArgs()[0];
		this.logging.info("ִ��reset��ʼ  ·��:" + this.location + ", �ļ�:" + element.getSrc());
		try{
			point.proceed();
		}catch(GitCommandException e){
			this.logging.error(e.getMessage());
		}
		this.logging.info("ִ��reset����  ·��:" + this.location + ", �ļ�:" + element.getSrc());
	}

	@Around("status()")
	public Object status(ProceedingJoinPoint point) throws Throwable {
		Object result = null;
		this.logging.info("ִ��status��ʼ  ·��:" + this.location );
		try{
			result = point.proceed();
		}catch(GitCommandException e){
			this.logging.error(e.getMessage());
		}
		this.logging.info("ִ��status����  ·��:" + this.location);
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
