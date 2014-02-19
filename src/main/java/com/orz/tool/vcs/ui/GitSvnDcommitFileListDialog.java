package com.orz.tool.vcs.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
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

import com.orz.tool.vcs.entity.FileElement;

public class GitSvnDcommitFileListDialog extends Dialog {

	protected Object result;
	protected Shell shlGitsvnDcommit;
	private Text text;
	private Table table;
	private Composite composite;
	private IProject project;
	private final List<FileElement> listData;

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
		btnSelectAll.setBounds(402, 10, 69, 18);
		btnSelectAll.setText("select all");


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
		table.setLocation(0, 33);
		table.setSize(567, 245);
		table.setBounds(0, 35, 645, 359);
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
		

		Button btnDcommit = new Button(composite_1, SWT.NONE);
		btnDcommit.setBounds(477, 6, 80, 28);
		btnDcommit.setText("dcommit");
		btnDcommit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String comment = text.getText();
				if (checkboxTableViewer.getCheckedElements().length > 0) {
					try {
						if(comment.length() == 0 ){
							alert("请填写日志!");
							text.forceFocus();
							return;
						}
						addToIndex(checkboxTableViewer.getCheckedElements());
					} catch (Exception e1) {
						e1.printStackTrace();
						alert("execute git commit error");
					}
				}else{
					if(comment.length()>0){
						alert("请在列表中选择记录!");
						return;
					}
				}
				ArrayList<FileElement> add = new ArrayList<>();
				ArrayList<FileElement> update = new ArrayList<>();
				for (FileElement element : listData) {
					if (!checkboxTableViewer.getChecked(element)) {// 没有被选中
						if ("M".equalsIgnoreCase(element.getState())) {
							update.add(element);
						} else if ("A".equalsIgnoreCase(element.getState())) {
							add.add(element);
						}
					}
				}

				try {
					if (update.size() > 0) {
						backupUpdateFiles(update.toArray(new FileElement[] {}));
					}

					if (add.size() > 0) {
						removeFromIndex(add.toArray(new FileElement[] {}));
					}
					 executeGitDcommit(comment);
					if (update.size() > 0) {
						recoverBackupFiles(update.toArray(new FileElement[] {}));
					}

					if (add.size() > 0) {
						addToIndex(add.toArray(new FileElement[] {}));
					}
					
					//刷新列表 alert dcommit成功
				} catch (Exception e1) {
					e1.printStackTrace();
					alert("git svn dcommit error!");
				}

			}

		});

	}

	// 执行commit
	private void addToIndex(Object[] elements) throws IOException, InterruptedException {
		for(Object element: elements){
			FileElement _e = (FileElement)element;
			this.addToIndex(_e);
		}
	}
	
	private void addToIndex(FileElement element) throws IOException, InterruptedException{
		Runtime run = Runtime.getRuntime();
//		if(!"M".equalsIgnoreCase(element.getState()) && !"A".equalsIgnoreCase(element.getState()) && !"D".equalsIgnoreCase(element.getState())){
			Process process = run.exec("git add " + element.getSrc(), null, project.getLocation().toFile());
			process.waitFor();
//		}
	}

	// 备份没有选中的更新状态的文件
	private void backupUpdateFiles(FileElement[] elements) throws IOException, InterruptedException {
		Runtime run = Runtime.getRuntime();// 返回与当前 Java 应用程序相关的运行时对象
		for (FileElement element : elements) {
			File file = new File(project.getLocation().toFile(),element.getSrc());
			String fileName = file.getName();
			File renameFile = new File(file.getParent(), "__"+ fileName + "__");
		    FileUtils.copyFile(file, renameFile);
			if (renameFile.exists() && renameFile.isFile()) {
				Process process = run.exec("git checkout " + element.getSrc(), null, project.getLocation().toFile());
				process.waitFor();
			}
		}
	}

	// dcommit后恢复backup文件
	private void recoverBackupFiles(FileElement[] elements) throws IOException {
		for (FileElement element : elements) {
			File file = new File(project.getLocation().toFile(),element.getSrc());
			File parent = file.getParentFile();
			File tmp = new File(parent, "__" + file.getName() +"__");
			if (tmp.exists() && tmp.isFile()) {
				FileUtils.copyFile(tmp, file);
				tmp.delete();
			}
		}
	}

	private void removeFromIndex(FileElement[] elements) throws IOException, InterruptedException {
		Runtime run = Runtime.getRuntime();
		for (FileElement element : elements) {
			Process process = run.exec("git reset HEAD " + element.getSrc(), null, project.getLocation().toFile());
			process.waitFor();
		}
	}

	private void addToIndex(FileElement[] elements) throws IOException, InterruptedException {
		for (FileElement element : elements) {
			this.addToIndex(element);
		}
	}

	private void executeGitDcommit(String comment) throws IOException, InterruptedException {
		Runtime run = Runtime.getRuntime();
		if(comment.length() > 0){
			Process process = run.exec("git commit -a -m \""+comment+"\"", null, project.getLocation().toFile());
			process.waitFor();
		}
		Process process =  run.exec("git svn dcommit", null, project.getLocation().toFile());
		process.waitFor();
	}
	
	private void alert(String message){
		 MessageBox messageBox = new MessageBox(this.getParent(), SWT. YES | SWT.ERROR );
		 messageBox.setMessage(message);
		 messageBox.open();
	}
}
