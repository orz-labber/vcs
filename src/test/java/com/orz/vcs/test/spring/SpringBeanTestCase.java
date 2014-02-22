package com.orz.vcs.test.spring;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.orz.tool.vcs.command.IGitCommand;
import com.orz.tool.vcs.entity.FileElement;

public class SpringBeanTestCase {

	private ClassPathXmlApplicationContext xmlContext;

	@Test
	public void beanLoad() {
		try {
			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
			context.refresh();
			DefaultListableBeanFactory factory = (DefaultListableBeanFactory) context.getAutowireCapableBeanFactory();
			factory.registerSingleton("location", new File("/Users/obladi/Workspace/git/server/orgChart"));
			xmlContext = new ClassPathXmlApplicationContext(new String[] { "application-context.xml" }, context);
			IGitCommand c = (IGitCommand) xmlContext.getBean("command");
			List<FileElement> elements = c.status();
			System.out.println(elements.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
