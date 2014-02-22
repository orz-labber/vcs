package com.orz.tool.vcs.command.impl;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.orz.tool.vcs.command.IGitCommand;
import com.orz.tool.vcs.entity.FileElement;
import com.orz.tool.vcs.ui.Log;

@Aspect
@Component
public class ComplexGitSvnDcommit {

	@Pointcut("execution(* com.orz.tool.vcs.command.impl.GitCommandImpl.dcommit(..))")
	private void dcommit() {

	}
	@Autowired
	private Log logging;
	
	/**
	 * 不能为空，在注入时控制
	 */
	private List<FileElement> listData;

	/**
	 * 不能为空，在注入时控制
	 */
	private Object[] checkedDatas;

	private String comment;

	@Autowired
	@Qualifier("location")
	private File location;

	private final static String FILENAME = "__{0}__";

	@Around("dcommit()")
	public void dcommit(ProceedingJoinPoint point) throws Throwable {
		IGitCommand command = (IGitCommand) point.getThis();
		ArrayList<FileElement> checkedElements = new ArrayList<FileElement>(this.checkedDatas.length);
		for (Object element : this.checkedDatas) {
			checkedElements.add((FileElement) element);
		}
		if (this.checkedDatas.length > 0) {
			for (Object element : checkedElements) {
				command.add((FileElement) element);
			}
		}

		ArrayList<FileElement> add = new ArrayList<>();
		ArrayList<FileElement> update = new ArrayList<>();
		for (FileElement element : this.listData) {
			if (!checkedElements.contains(element)) {// 没有被选中
				if ("M".equalsIgnoreCase(element.getState())) {
					update.add(element);
				} else if ("A".equalsIgnoreCase(element.getState())) {
					add.add(element);
				}
			}
		}
		for (FileElement element : update) {
			File file = new File(this.location, element.getSrc());
			String fileName = file.getName();
			File renameFile = new File(file.getParent(), MessageFormat.format(FILENAME, new Object[]{fileName}));
			this.logging.info("backup " + file + ",to: " +renameFile);
			FileUtils.copyFile(file, renameFile);
			if (renameFile.exists() && renameFile.isFile()) {
				command.checkout(element);
			}
		}

		for (FileElement element : add) {
			command.reset(element);
		}
		if(this.checkedDatas.length > 0)
		command.commit(this.comment);
		point.proceed();

		for (FileElement element : add) {
			command.add(element);
		}

		for (FileElement element : update) {
			File file = new File(this.location, element.getSrc());
			File parent = file.getParentFile();
			String fileName = file.getName();
			File tmp = new File(parent, MessageFormat.format(FILENAME, new Object[]{fileName}));
			if (tmp.exists() && tmp.isFile()) {
				FileUtils.copyFile(tmp, file);
				this.logging.info("recover " + tmp + ",to: " +file);
				tmp.delete();
			}
		}
	}


	@Autowired(required=false)
	@Qualifier("listData")
	public void setListData(List<FileElement> listData) {
		this.listData = listData;
	}

	@Autowired(required=false)
	@Qualifier("checkedDatas")
	public void setCheckedDatas(Object[] checkedDatas) {
		this.checkedDatas = checkedDatas;
	}

	@Autowired(required=false)
	@Qualifier("comment")
	public void setComment(String comment){
		this.comment = comment;
	}


}
