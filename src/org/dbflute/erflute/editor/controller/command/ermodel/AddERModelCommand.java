package org.dbflute.erflute.editor.controller.command.ermodel;

import org.dbflute.erflute.editor.controller.command.AbstractCommand;
import org.dbflute.erflute.editor.model.ERDiagram;
import org.dbflute.erflute.editor.model.diagram_contents.element.node.ermodel.ERModel;

/**
 * @author modified by jflute (originated in ermaster)
 */
public class AddERModelCommand extends AbstractCommand {

    private final String name;
    private final ERDiagram diagram;

    public AddERModelCommand(ERDiagram diagram, String name) {
        super();
        this.diagram = diagram;
        this.name = name;
    }

    @Override
    protected void doExecute() {
        final ERModel ermodel = new ERModel(diagram);
        ermodel.setName(name);
        diagram.addErmodel(ermodel);
    }

    @Override
    protected void doUndo() {
        // ??? by jflute
    }
}