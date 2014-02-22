package com.orz.tool.vcs.command.impl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.orz.tool.vcs.PlugInUtil;
import com.orz.tool.vcs.command.GitCommandException;
import com.orz.tool.vcs.command.IGitCommand;
import com.orz.tool.vcs.entity.FileElement;
import com.orz.tool.vcs.ui.Log;

@Component("command")
public class GitCommandImpl implements IGitCommand {

	@Autowired
	@Qualifier("location")
	public File location;
	
	@Autowired
	private Log logging;

	@Override
	public void dcommit() {
		Runtime run = Runtime.getRuntime();
		try {
			String command = "git svn dcommit";
			this.logging.info(command);
			Process p = run.exec(command, null, this.location);
			if(p.waitFor() != 0){
				throw new GitCommandException(this.error(p));
			}
		} catch (Exception e) {
			throw new GitCommandException(e);
		}
	}

	@Override
	public void commit(String message) {
		Runtime run = Runtime.getRuntime();
		try {
            //如果命令行中存在空格，则需要将命令行转成数组形式传入
			String[] command = {"git","commit","-m","'"+message+"'"};
			this.logging.info(PlugInUtil.join(command, " "));
			Process p = run.exec(command, null, this.location);
			if(p.waitFor() != 0){
				throw new GitCommandException(this.error(p));
			}
		} catch (Exception e) {
			throw new GitCommandException(e);
		}
	}

	@Override
	public void add(FileElement element) {
		Runtime run = Runtime.getRuntime();
		try {
			String[] command = new String[]{"git","add" ,element.getSrc()};
			this.logging.info(PlugInUtil.join(command, " "));
			Process p = run.exec(command, null, this.location);
			if(p.waitFor() != 0){
				throw new GitCommandException(this.error(p));
			}
		} catch (Exception e) {
			throw new GitCommandException(e);
		}
	}

	@Override
	public void checkout(FileElement element) {
		Runtime run = Runtime.getRuntime();
		try {
			String[] command = new String[]{"git","checkout",element.getSrc()};
			this.logging.info(PlugInUtil.join(command, " "));
			Process p = run.exec(command, null, this.location);
			if(p.waitFor() != 0){
				throw new GitCommandException(this.error(p));
			}
		} catch (Exception e) {
			throw new GitCommandException(e);
		}
	}

	@Override
	public void reset(FileElement element) {
		Runtime run = Runtime.getRuntime();
		try {
			String[] command = new String[]{"git","reset","HEAD",element.getSrc()};
			this.logging.info(PlugInUtil.join(command, " "));
			Process p = run.exec(command, null, this.location);
			if(p.waitFor() != 0){
				throw new GitCommandException(this.error(p));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new GitCommandException(e);
		}
	}

	@Override
	public List<FileElement> status() {
		List<FileElement> result = new ArrayList<FileElement>();
		Runtime run = Runtime.getRuntime();// 返回与当前 Java 应用程序相关的运行时对象
		try {
			String command = "git status -s";
			this.logging.info(command);
			Process p = run.exec(command, null, this.location);// 启动另一个进程来执行命令
			BufferedInputStream in = new BufferedInputStream(p.getInputStream());
			BufferedReader inBr = new BufferedReader(new InputStreamReader(in));
			String lineStr;
			while ((lineStr = inBr.readLine()) != null) {
				// 获得命令执行后在控制台的输出信息
				String line = lineStr.trim();
				if (line.indexOf(" ") != -1) {
					FileElement element = new FileElement();
					element.setState(line.substring(0, line.indexOf(" ")).trim());
					element.setSrc(line.substring(line.indexOf(" ")).trim());
					result.add(element);
				}
			}
			inBr.close();
			in.close();
			if(p.waitFor() != 0){
				throw new GitCommandException(this.error(p));
			}
		} catch (Exception e) {
			throw new GitCommandException(e);
		}
		return result;
	}

	private String error(Process p) throws Exception {
		StringBuffer result = new StringBuffer();
		BufferedInputStream in = new BufferedInputStream(p.getErrorStream());
		BufferedReader inBr = new BufferedReader(new InputStreamReader(in));
		String lineStr;
		while ((lineStr = inBr.readLine()) != null) {
			// 获得命令执行后在控制台的输出信息
			String line = lineStr.trim();
			if (line.indexOf(" ") != -1) {
				result.append(line).append("\r\n");
			}
		}
		inBr.close();
		in.close();
		return result.toString();
	}
	
	public static void main(String[] args){
		try {
			System.out.println(">" + new String(" ".getBytes("GBK"),"UNICODE") + "<");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
