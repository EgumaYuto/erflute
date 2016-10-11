package org.dbflute.erflute.editor.model.diagram_contents.element.node.table;

import java.util.ArrayList;
import java.util.List;

import org.dbflute.erflute.editor.model.ERDiagram;
import org.dbflute.erflute.editor.model.diagram_contents.element.connection.Relationship;
import org.dbflute.erflute.editor.model.diagram_contents.element.connection.WalkerConnection;
import org.dbflute.erflute.editor.model.diagram_contents.element.node.DiagramWalker;
import org.dbflute.erflute.editor.model.diagram_contents.element.node.Location;
import org.dbflute.erflute.editor.model.diagram_contents.element.node.ermodel.ERVirtualDiagram;
import org.dbflute.erflute.editor.model.diagram_contents.element.node.note.WalkerNote;
import org.dbflute.erflute.editor.model.diagram_contents.element.node.table.column.ERColumn;
import org.dbflute.erflute.editor.model.diagram_contents.element.node.table.column.NormalColumn;
import org.dbflute.erflute.editor.model.diagram_contents.element.node.table.index.ERIndex;
import org.dbflute.erflute.editor.model.diagram_contents.element.node.table.properties.TableViewProperties;
import org.dbflute.erflute.editor.model.diagram_contents.element.node.table.unique_key.ComplexUniqueKey;

/**
 * @author modified by jflute (originated in ermaster)
 */
public class ERVirtualTable extends ERTable {

    private static final long serialVersionUID = 1L;

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    private final ERVirtualDiagram vdiagram;
    private ERTable rawTable;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public ERVirtualTable(ERVirtualDiagram vdiagram, ERTable rawTable) {
        super();
        this.vdiagram = vdiagram;
        this.rawTable = rawTable;
    }

    // ===================================================================================
    //                                                                        Change Table
    //                                                                        ============
    public void changeTable() {
        firePropertyChange(PROPERTY_CHANGE_COLUMNS, null, null);
    }

    // ===================================================================================
    //                                                               Delegate to Raw Table
    //                                                               =====================
    @Override
    public void setColor(int red, int green, int blue) {
        rawTable.setColor(red, green, blue);
    }

    @Override
    public int[] getColor() {
        return rawTable.getColor();
    }

    @Override
    public ERDiagram getDiagram() {
        return rawTable.getDiagram();
    }

    public void setPoint(int x, int y) {
        this.setLocation(new Location(x, y, getWidth(), getHeight()));
    }

    @Override
    public int getWidth() {
        return rawTable.getWidth();
    }

    @Override
    public int getHeight() {
        return rawTable.getHeight();
    }

    @Override
    public List<WalkerConnection> getIncomings() {
        final List<WalkerConnection> elements = new ArrayList<WalkerConnection>();
        final List<ERVirtualTable> modelTables = vdiagram.getVirtualTables();
        for (final WalkerConnection el : rawTable.getIncomings()) {
            final DiagramWalker findEl = el.getWalkerSource();
            if (findEl instanceof WalkerNote) {
                if (((WalkerNote) findEl).getVirtualDiagram().equals(vdiagram)) {
                    elements.add(el);
                }
            } else {
                for (final ERVirtualTable vtable : modelTables) {
                    if (vtable.getRawTable().equals(findEl)) {
                        elements.add(el);
                        break;
                    }
                }
            }
        }
        return elements;
    }

    @Override
    public List<WalkerConnection> getOutgoings() {
        final List<WalkerConnection> elements = new ArrayList<WalkerConnection>();
        final List<ERVirtualTable> modelTables = vdiagram.getVirtualTables();
        for (final WalkerConnection el : rawTable.getOutgoings()) {
            final DiagramWalker findEl = el.getWalkerTarget();
            if (findEl instanceof WalkerNote) {
                if (((WalkerNote) findEl).getVirtualDiagram().equals(vdiagram)) {
                    elements.add(el);
                }
                elements.add(el);
            } else {
                for (final ERVirtualTable vtable : modelTables) {
                    if (vtable.getRawTable().equals(findEl)) {
                        elements.add(el);
                        break;
                    }
                }
            }
        }
        return elements;
    }

    @Override
    public NormalColumn getAutoIncrementColumn() {
        return rawTable.getAutoIncrementColumn();
    }

    @Override
    public TableViewProperties getTableViewProperties() {
        return rawTable.getTableViewProperties();
    }

    @Override
    public String getPhysicalName() {
        return rawTable.getPhysicalName();
    }

    @Override
    public List<DiagramWalker> getReferringElementList() {
        return rawTable.getReferringElementList();
    }

    @Override
    public TableViewProperties getTableViewProperties(String database) {
        return rawTable.getTableViewProperties(database);
    }

    @Override
    public String getLogicalName() {
        return rawTable.getLogicalName();
    }

    @Override
    public List<DiagramWalker> getReferedElementList() {
        return rawTable.getReferedElementList();
    }

    @Override
    public String getName() {
        return rawTable.getName();
    }

    @Override
    public String getDescription() {
        return rawTable.getDescription();
    }

    @Override
    public List<ERColumn> getColumns() {
        return rawTable.getColumns();
    }

    @Override
    public List<NormalColumn> getExpandedColumns() {
        return rawTable.getExpandedColumns();
    }

    @Override
    public List<Relationship> getIncomingRelationshipList() {
        final List<Relationship> elements = new ArrayList<Relationship>();
        final List<ERVirtualTable> modelTables = vdiagram.getVirtualTables();
        for (final Relationship el : rawTable.getIncomingRelationshipList()) {
            final DiagramWalker findEl = el.getWalkerSource();
            for (final ERVirtualTable vtable : modelTables) {
                if (vtable.getRawTable().equals(findEl)) {
                    elements.add(el);
                    break;
                }
            }
        }
        return elements;
    }

    @Override
    public List<Relationship> getOutgoingRelationshipList() {
        final List<Relationship> elements = new ArrayList<Relationship>();
        final List<ERVirtualTable> modelTables = vdiagram.getVirtualTables();
        for (final Relationship el : rawTable.getOutgoingRelationshipList()) {
            final DiagramWalker findEl = el.getWalkerSource();
            for (final ERVirtualTable vtable : modelTables) {
                if (vtable.getRawTable().equals(findEl)) {
                    elements.add(el);
                    break;
                }
            }
        }
        return elements;
    }

    @Override
    public List<NormalColumn> getNormalColumns() {
        return rawTable.getNormalColumns();
    }

    @Override
    public int getPrimaryKeySize() {
        return rawTable.getPrimaryKeySize();
    }

    @Override
    public ERColumn getColumn(int index) {
        return rawTable.getColumn(index);
    }

    @Override
    public List<NormalColumn> getPrimaryKeys() {
        return rawTable.getPrimaryKeys();
    }

    @Override
    public ERIndex getIndex(int index) {
        return rawTable.getIndex(index);
    }

    @Override
    public List<ERIndex> getIndexes() {
        return rawTable.getIndexes();
    }

    @Override
    public List<ComplexUniqueKey> getComplexUniqueKeyList() {
        return rawTable.getComplexUniqueKeyList();
    }

    @Override
    public String getConstraint() {
        return rawTable.getConstraint();
    }

    @Override
    public String getPrimaryKeyName() {
        return rawTable.getPrimaryKeyName();
    }

    @Override
    public String getOption() {
        return rawTable.getOption();
    }

    @Override
    public String getNameWithSchema(String database) {
        return rawTable.getNameWithSchema(database);
    }

    public ERTable getRawTable() {
        return rawTable;
    }

    public void setRawTable(ERTable rawTable) {
        this.rawTable = rawTable;
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    @Override
    public String getObjectType() {
        return "vtable";
    }
}
