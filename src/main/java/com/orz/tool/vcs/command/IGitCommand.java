package com.orz.tool.vcs.command;

import java.util.List;

import com.orz.tool.vcs.entity.FileElement;

public interface IGitCommand {

	public void dcommit();

	public void commit(String message);

	public void add(FileElement element);

	public void checkout(FileElement element);

	public void reset(FileElement element);

	public List<FileElement> status();

}
