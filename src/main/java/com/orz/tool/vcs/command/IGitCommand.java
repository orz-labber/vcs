package com.orz.tool.vcs.command;

import java.io.IOException;
import java.util.List;

import com.orz.tool.vcs.entity.FileElement;

public interface IGitCommand {

	public void dcommit() throws IOException;

	public void commit(String comment) throws IOException;

	public void add(FileElement element) throws IOException;

	public void checkout(FileElement element) throws IOException;

	public void reset(FileElement element) throws IOException;

	public List<FileElement> status();

}
