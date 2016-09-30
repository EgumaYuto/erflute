package org.insightech.er.wizard;

import org.dbflute.erflute.Activator;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.ide.IDE;
import org.insightech.er.wizard.page.NewDiagramWizardPage1;
import org.insightech.er.wizard.page.NewDiagramWizardPage2;

/**
 * @author modified by jflute (originated in ermaster)
 */
public class NewDiagramWizard extends Wizard implements INewWizard {

    private NewDiagramWizardPage1 page1;
    private NewDiagramWizardPage2 page2;
    private IStructuredSelection selection;
    private IWorkbench workbench;

    @Override
    public boolean performFinish() {
        try {
            final String database = this.page2.getDatabase();
            this.page1.createERDiagram(database);
            final IFile file = this.page1.createNewFile();
            if (file == null) {
                return false;
            }
            final IWorkbenchPage page = this.workbench.getActiveWorkbenchWindow().getActivePage();
            IDE.openEditor(page, file, true);
        } catch (final Exception e) {
            Activator.showExceptionDialog(e);
        }
        return true;
    }

    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.selection = selection;
        this.workbench = workbench;
    }

    @Override
    public void addPages() {
        this.page1 = new NewDiagramWizardPage1(this.selection);
        this.addPage(this.page1);

        this.page2 = new NewDiagramWizardPage2(this.selection);
        this.addPage(this.page2);
    }
}
