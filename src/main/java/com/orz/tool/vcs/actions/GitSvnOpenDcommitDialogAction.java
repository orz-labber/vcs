/**
 * 
 */
package com.orz.tool.vcs.actions;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.actions.ActionDelegate;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.orz.tool.vcs.PlugInConstant;
import com.orz.tool.vcs.command.IGitCommand;
import com.orz.tool.vcs.entity.FileElement;
import com.orz.tool.vcs.ui.GitSvnDcommitFileListDialog;

/**
 * @author obladi
 * 
 */
public class GitSvnOpenDcommitDialogAction extends ActionDelegate implements IViewActionDelegate {

	private IViewPart viewPart;

	private IStructuredSelection selection;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IViewActionDelegate#init(org.eclipse.ui.IViewPart)
	 */
	@Override
	public void init(IViewPart viewPart) {
		this.viewPart = viewPart;
	}

	@Override
	public void run(IAction action) {
		if (this.selection.getFirstElement() instanceof IProject) {
			action.setEnabled(true);
			IProject project = (IProject) this.selection.getFirstElement();
			GitSvnDcommitFileListDialog dialog = new GitSvnDcommitFileListDialog(this.viewPart.getSite().getShell(), project, this.getElments(project));
			dialog.open();
		} else {
			action.setEnabled(false);
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			this.selection = (IStructuredSelection) selection;
		}
	}


	
	private List<FileElement> getElments(IProject project) {
		ClassPathXmlApplicationContext paramContext = new ClassPathXmlApplicationContext();
		paramContext.refresh();
		DefaultListableBeanFactory factory = (DefaultListableBeanFactory) paramContext.getAutowireCapableBeanFactory();
		factory.registerSingleton("location", project.getLocation().toFile());
		ClassPathXmlApplicationContext beanContext = new ClassPathXmlApplicationContext(PlugInConstant.applicationContextXML, paramContext);
		IGitCommand command = (IGitCommand) beanContext.getBean("command");
		List<FileElement> elements = command.status();
		beanContext.close();
		paramContext.close();
		return elements;
	}

}
