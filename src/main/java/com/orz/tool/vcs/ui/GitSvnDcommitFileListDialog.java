package com.orz.tool.vcs.ui;

import java.util.List;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.orz.tool.vcs.PlugInConstant;
import com.orz.tool.vcs.command.IGitCommand;
import com.orz.tool.vcs.entity.FileElement;

public class GitSvnDcommitFileListDialog extends Dialog {

	protected Object result;
	protected Shell shlGitsvnDcommit;
	private Text text;
	private Table table;
	private Composite composite;
	private IProject project;
	private final List<FileElement> listData;

	private ClassPathXmlApplicationContext paramContext;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public GitSvnDcommitFileListDialog(Shell parent, IProject project, List<FileElement> datas) {
		super(parent);
		setText("SWT Dialog");
		this.project = project;
		this.listData = datas;
		paramContext = new ClassPathXmlApplicationContext();
		paramContext.refresh();
		parent.addListener(SWT.Close, new Listener() {
			@Override
			public void handleEvent(Event event) {
				paramContext.close();
			}

		});
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlGitsvnDcommit.open();
		shlGitsvnDcommit.layout();
		Display display = getParent().getDisplay();
		while (!shlGitsvnDcommit.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	@SuppressWarnings("rawtypes")
	private void createContents() {
		shlGitsvnDcommit = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL);
		shlGitsvnDcommit.setSize(567, 577);
		shlGitsvnDcommit.setText("git-svn dcommit");
		shlGitsvnDcommit.setLayout(new FillLayout(SWT.VERTICAL));

		text = new Text(shlGitsvnDcommit, SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
		text.setFont(SWTResourceManager.getFont("Lucida Grande", 13, SWT.NORMAL));

		composite = new Composite(shlGitsvnDcommit, SWT.NONE);

		Composite composite_1 = new Composite(composite, SWT.NONE);
		composite_1.setBounds(0, 0, 567, 33);

		Button btnSelectAll = new Button(composite_1, SWT.CHECK);
		btnSelectAll.setBounds(433, 10, 49, 18);
		btnSelectAll.setText("全选");

		final CheckboxTableViewer checkboxTableViewer = CheckboxTableViewer.newCheckList(composite, SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION | SWT.MULTI);
		checkboxTableViewer.setContentProvider(new IStructuredContentProvider() {
			public void dispose() {
			}

			public void inputChanged(Viewer arg0, Object arg1, Object arg2) {

			}

			public Object[] getElements(Object element) {
				if (element instanceof List) {
					return ((List) element).toArray();
				}
				return new Object[0];
			}

		});

		table = checkboxTableViewer.getTable();
		table.setBounds(0, 35, 567, 243);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		TableViewerColumn stateColumn = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn sc = stateColumn.getColumn();
		sc.setAlignment(SWT.LEFT);
		sc.setWidth(40);
		stateColumn.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				FileElement fileElement = (FileElement) element;
				return fileElement.getState();
			}

		});

		TableViewerColumn filePathColumn = new TableViewerColumn(checkboxTableViewer, SWT.NONE);
		TableColumn tableColumn = filePathColumn.getColumn();
		tableColumn.setAlignment(SWT.LEFT);
		tableColumn.setWidth(380);
		tableColumn.setText("File List");

		filePathColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				FileElement fileElement = (FileElement) element;
				return fileElement.getSrc();
			}

		});

		Menu menu = new Menu(table);
		table.setMenu(menu);

		MenuItem packageMenu = new MenuItem(menu, SWT.NONE);
		packageMenu.setText("package");

		MenuItem packageZipMenu = new MenuItem(menu, SWT.NONE);
		packageZipMenu.setText("package zip");

		checkboxTableViewer.setInput(this.listData);

		final Button btnDcommit = new Button(composite_1, SWT.NONE);
		btnDcommit.setBounds(477, 6, 80, 28);
		btnDcommit.setText("提交SVN");
		btnDcommit.addSelectionListener(new SelectionAdapter() {
			private ClassPathXmlApplicationContext beanContext;
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnDcommit.setEnabled(false);
				String comment = text.getText();
				Object[] elements = checkboxTableViewer.getCheckedElements();
				if (elements.length > 0 && comment.length() == 0) {
					alert("请填写日志!");
					text.forceFocus();
					return;
				}
				if (elements.length == 0 && comment.length() > 0) {
					alert("请在列表中选择记录!");
					return;
				}
				DefaultListableBeanFactory factory = (DefaultListableBeanFactory) paramContext.getAutowireCapableBeanFactory();
				factory.destroySingletons();
				factory.registerSingleton("location", project.getLocation().toFile());
				factory.registerSingleton("checkedDatas", elements == null || elements.length==0?new Object[]{}:elements);
				factory.registerSingleton("listData", listData);
				factory.registerSingleton("comment", comment);
				beanContext = new ClassPathXmlApplicationContext(PlugInConstant.applicationContextXML, paramContext);
				IGitCommand command = (IGitCommand) beanContext.getBean("command");
				command.dcommit();
				beanContext.close();
				btnDcommit.setEnabled(true);
			}

		});

	}

	private void alert(String message) {
		MessageBox messageBox = new MessageBox(this.getParent(), SWT.YES | SWT.ERROR);
		messageBox.setMessage(message);
		messageBox.open();
	}

}
