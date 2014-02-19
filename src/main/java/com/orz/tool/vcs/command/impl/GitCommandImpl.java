package com.orz.tool.vcs.command.impl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.orz.tool.vcs.command.IGitCommand;
import com.orz.tool.vcs.entity.FileElement;

public class GitCommandImpl implements IGitCommand {

	private File root;

	@Override
	public void dcommit() throws IOException {
		Runtime run = Runtime.getRuntime();
		run.exec("git svn dcommit", null, this.root);
	}

	@Override
	public void commit(String comment) throws IOException {
		Runtime run = Runtime.getRuntime();
		run.exec("git commit -a -m \"" + comment + "\"", null, this.root);
	}

	@Override
	public void add(FileElement element) throws IOException {
		Runtime run = Runtime.getRuntime();
		run.exec("git add " + element.getSrc(), null, this.root);
	}

	@Override
	public void checkout(FileElement element) throws IOException {
		Runtime run = Runtime.getRuntime();
		run.exec("git checkout " + element.getSrc(), null, this.root);
	}

	@Override
	public void reset(FileElement element) throws IOException {
		Runtime run = Runtime.getRuntime();
		run.exec("git reset HEAD " + element.getSrc(), null, this.root);
	}

	@Override
	public List<FileElement> status() {
		List<FileElement> result = new ArrayList<FileElement>();
		Runtime run = Runtime.getRuntime();// 返回与当前 Java 应用程序相关的运行时对象
		try {
			Process p = run.exec("git status -s", null, this.getRoot());// 启动另一个进程来执行命令
			BufferedInputStream in = new BufferedInputStream(p.getInputStream());
			BufferedReader inBr = new BufferedReader(new InputStreamReader(in));
			String lineStr;
			while ((lineStr = inBr.readLine()) != null) {
				// 获得命令执行后在控制台的输出信息
				String line = lineStr.trim();
				if (line.indexOf(" ") != -1) {
					FileElement element = new FileElement();
					element.setState(line.substring(0, line.indexOf(" ")).trim());
					element.setSrc(line.substring(line.lastIndexOf(" ")).trim());
					result.add(element);
				}
			}
			// 检查命令是否执行失败。
			if (p.waitFor() != 0 && p.exitValue() == 1) {// p.exitValue()==0表示正常结束，1：非正常结束
				throw new RuntimeException("命令执行失败!");
			}
			inBr.close();
			in.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	public File getRoot() {
		return root;
	}

	public void setRoot(File root) {
		this.root = root;
	}

}
