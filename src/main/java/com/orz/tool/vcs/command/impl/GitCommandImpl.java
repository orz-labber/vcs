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
		Runtime run = Runtime.getRuntime();// �����뵱ǰ Java Ӧ�ó�����ص�����ʱ����
		try {
			Process p = run.exec("git status -s", null, this.getRoot());// ������һ��������ִ������
			BufferedInputStream in = new BufferedInputStream(p.getInputStream());
			BufferedReader inBr = new BufferedReader(new InputStreamReader(in));
			String lineStr;
			while ((lineStr = inBr.readLine()) != null) {
				// �������ִ�к��ڿ���̨�������Ϣ
				String line = lineStr.trim();
				if (line.indexOf(" ") != -1) {
					FileElement element = new FileElement();
					element.setState(line.substring(0, line.indexOf(" ")).trim());
					element.setSrc(line.substring(line.lastIndexOf(" ")).trim());
					result.add(element);
				}
			}
			// ��������Ƿ�ִ��ʧ�ܡ�
			if (p.waitFor() != 0 && p.exitValue() == 1) {// p.exitValue()==0��ʾ����������1������������
				throw new RuntimeException("����ִ��ʧ��!");
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
