package org.insightech.er.wizard.page;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.insightech.er.Activator;
import org.insightech.er.DisplayMessages;
import org.insightech.er.editor.model.ERDiagram;
import org.insightech.er.editor.persistent.Persistent;

/**
 * #analyzed 新規ER図の作成のファイル名入力の画面
 * @author ermaster
 * @author jflute
 */
public class NewDiagramWizardPage1 extends WizardNewFileCreationPage {

    private static final String EXTENSION = ".erm";
    private ERDiagram diagram;

    public NewDiagramWizardPage1(IStructuredSelection selection) {
        super(DisplayMessages.getMessage("wizard.new.diagram.title"), selection);
        this.setTitle(DisplayMessages.getMessage("wizard.new.diagram.title"));
    }

    @Override
    public void createControl(Composite parent) {
        super.createControl(parent);
        this.setFileName("newfile");
    }

    @Override
    protected boolean validatePage() {
        boolean valid = super.validatePage();
        if (valid) {
            String fileName = this.getFileName();
            if (fileName.indexOf(".") != -1 && !fileName.endsWith(EXTENSION)) {
                this.setErrorMessage(DisplayMessages.getMessage("error.erm.extension"));
                valid = false;
            }
        }
        if (valid) {
            String fileName = this.getFileName();
            if (fileName.indexOf(".") == -1) {
                fileName = fileName + EXTENSION;
            }
            IWorkspace workspace = ResourcesPlugin.getWorkspace();
            IWorkspaceRoot root = workspace.getRoot();

            IPath containerPath = this.getContainerFullPath();
            IPath newFilePath = containerPath.append(fileName);

            if (root.getFile(newFilePath).exists()) {
                this.setErrorMessage("'" + fileName + "' " + DisplayMessages.getMessage("error.file.already.exists"));
                valid = false;
            }
        }
        if (valid) {
            this.setMessage(DisplayMessages.getMessage("wizard.new.diagram.message"));
        }
        return valid;
    }

    public void createERDiagram(String database) {
        this.diagram = new ERDiagram(database);
        this.diagram.init();
    }

    @Override
    protected InputStream getInitialContents() {
        Persistent persistent = Persistent.getInstance();
        try {
            InputStream in = persistent.createInputStream(this.diagram);
            return in;
        } catch (IOException e) {
            Activator.showExceptionDialog(e);
        }
        return null;
    }

    @Override
    public IFile createNewFile() {
        String fileName = this.getFileName();
        if (fileName.indexOf(".") == -1) {
            this.setFileName(fileName + EXTENSION);
        }
        return super.createNewFile();
    }
}
