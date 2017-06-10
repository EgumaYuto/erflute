package org.dbflute.erflute.editor.controller.command.diagram_contents.element.node.category;

import java.util.ArrayList;
import java.util.List;

import org.dbflute.erflute.editor.controller.command.AbstractCommand;
import org.dbflute.erflute.editor.model.ERDiagram;
import org.dbflute.erflute.editor.model.diagram_contents.element.node.category.Category;
import org.dbflute.erflute.editor.model.settings.CategorySettings;

public class DeleteCategoryCommand extends AbstractCommand {

    private ERDiagram diagram;

    private CategorySettings categorySettings;

    private Category category;

    private List<Category> oldAllCategories;

    private List<Category> oldSelectedCategories;

    public DeleteCategoryCommand(ERDiagram diagram, Category category) {
        this.diagram = diagram;
        this.categorySettings = diagram.getDiagramContents().getSettings().getCategorySetting();
        this.category = category;
    }

    @Override
    protected void doExecute() {
        this.oldAllCategories = new ArrayList<Category>(this.categorySettings.getAllCategories());
        this.oldSelectedCategories = new ArrayList<Category>(this.categorySettings.getSelectedCategories());

        this.diagram.removeCategory(category);
    }

    @Override
    protected void doUndo() {
        this.categorySettings.setAllCategories(oldAllCategories);
        this.categorySettings.setSelectedCategories(oldSelectedCategories);
        this.diagram.restoreCategories();
    }
}
