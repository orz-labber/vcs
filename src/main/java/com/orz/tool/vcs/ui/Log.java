package com.orz.tool.vcs.ui;

import java.io.IOException;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.springframework.stereotype.Component;

@Component
public class Log {
	
	private MessageConsoleStream consoleStream;
	
	public Log() {
		MessageConsole console = new MessageConsole("git-svn dcommit console", null);
		ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[] { console });
		consoleStream = console.newMessageStream();
		ConsolePlugin.getDefault().getConsoleManager().showConsoleView(console);
	}
	
    public void write(String message){
    	try {
			this.consoleStream.write(message);
			this.consoleStream.write("\r\n");
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
    }
    
    public void info(String message){
    	this.write("INFO: " + message);
    }
    
    public void error(String message){
    	this.write("ERROR: " + message);
    }

}
