package com.orz.tool.vcs.command;

public class GitCommandException extends RuntimeException {

	private static final long serialVersionUID = -6928026116823716158L;

	public GitCommandException() {
		super();
	}

	public GitCommandException(Exception e) {
		super(e);
	}
	
	public GitCommandException(String message){
		super(message);
	}

}
