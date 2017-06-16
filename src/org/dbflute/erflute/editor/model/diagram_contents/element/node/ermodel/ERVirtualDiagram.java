package org.dbflute.erflute.editor.model.diagram_contents.element.node.ermodel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.dbflute.erflute.core.DesignResources;
import org.dbflute.erflute.editor.model.ERDiagram;
import org.dbflute.erflute.editor.model.diagram_contents.element.connection.Relationship;
import org.dbflute.erflute.editor.model.diagram_contents.element.node.DiagramWalker;
import org.dbflute.erflute.editor.model.diagram_contents.element.node.note.WalkerNote;
import org.dbflute.erflute.editor.model.diagram_contents.element.node.table.ERTable;
import org.dbflute.erflute.editor.model.diagram_contents.element.node.table.ERVirtualTable;
import org.dbflute.erflute.editor.model.diagram_contents.element.node.table.TableView;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Color;

/**
 * @author modified by jflute (originated in ermaster)
 */
public class ERVirtualDiagram extends DiagramWalker {

    private static final long serialVersionUID = 1L;
    public static final String PROPERTY_CHANGE_VTABLES = "vtables";

    private int[] defaultColor;
    private String name;

    private List<ERVirtualTable> tables;
    private List<WalkerNote> notes;
    private List<WalkerGroup> groups;
    private Point mousePoint = new Point();

    public ERVirtualDiagram(ERDiagram diagram) {
        setDiagram(diagram);
        tables = new ArrayList<>();
        notes = new ArrayList<>();
        groups = new ArrayList<>();
    }

    @Override
    public String getObjectType() {
        return "virtual_diagram";
    }

    public boolean containsTable(ERTable table) {
        for (final ERVirtualTable vtable : tables) {
            if (vtable.getRawTable().equals(table)) {
                return true;
            }
        }
        return false;
    }

    public void remove(ERVirtualTable element) {
        tables.remove(element);
        firePropertyChange(PROPERTY_CHANGE_VTABLES, null, null);
    }

    public void changeAll() {
        firePropertyChange(PROPERTY_CHANGE_VTABLES, null, null);
    }

    public void addTable(ERVirtualTable virtualTable) {
        tables.add(virtualTable);
    }

    public int[] getDefaultColor() {
        return defaultColor;
    }

    public void setDefaultColor(Color color) {
        this.defaultColor = new int[3];
        this.defaultColor[0] = color.getRed();
        this.defaultColor[1] = color.getGreen();
        this.defaultColor[2] = color.getBlue();
    }

    public ERVirtualTable findVirtualTable(TableView table) {
        for (final ERVirtualTable vtable : tables) {
            if (vtable.getRawTable().getPhysicalName().equals(table.getPhysicalName())) {
                return vtable;
            }
        }
        return null;
    }

    public void deleteRelationship(Relationship relation) {
        for (final ERVirtualTable vtable : tables) {
            vtable.removeOutgoing(relation);
            vtable.removeIncoming(relation);
        }
    }

    public void createRelationship(Relationship relationship) {
        boolean dirty = false;
        for (final ERVirtualTable vtable : tables) {
            if (relationship.getSourceTableView().equals(vtable.getRawTable())) {
                dirty = true;
            } else if (relationship.getTargetTableView().equals(vtable.getRawTable())) {
                dirty = true;
            }
        }
        if (dirty) {
            changeAll();
        }
    }

    @Override
    public boolean needsUpdateOtherModel() {
        return false;
    }

    public void addNewWalker(DiagramWalker element) {
        if (element instanceof WalkerNote) {
            ((WalkerNote) element).setVirtualDiagram(this);
        } else if (element instanceof WalkerGroup) {
            ((WalkerGroup) element).setVirtualDiagram(this);
        } else {
            getDiagram().addWalkerPlainly(element);
        }
        int[] color = defaultColor;

        if (element instanceof WalkerNote) {
            element.setColor(DesignResources.NOTE_DEFAULT_COLOR);
        } else {
            if (color == null) {
                color = getDiagram().getDefaultColor();
            }
            element.setColor(color[0], color[1], color[2]);
        }
        if (getFontName() != null) {
            element.setFontName(getFontName());
        } else {
            element.setFontName(getDiagram().getFontName());
        }
        if (getFontSize() != 0) {
            element.setFontSize(this.getFontSize());
        } else {
            element.setFontSize(getDiagram().getFontSize());
        }
        if (element instanceof WalkerNote) {
            notes.add((WalkerNote) element);
            firePropertyChange(PROPERTY_CHANGE_VTABLES, null, null);
        } else if (element instanceof WalkerGroup) {
            groups.add(((WalkerGroup) element));
            firePropertyChange(PROPERTY_CHANGE_VTABLES, null, null);
        }
    }

    public void addWalkerGroup(WalkerGroup group) {
        groups.add(group);
        firePropertyChange(PROPERTY_CHANGE_VTABLES, null, null);
    }

    public void remove(WalkerGroup element) {
        groups.remove(element);
        this.firePropertyChange(PROPERTY_CHANGE_VTABLES, null, null);
    }

    public void remove(WalkerNote element) {
        notes.remove(element);
        this.firePropertyChange(PROPERTY_CHANGE_VTABLES, null, null);
    }

    @Override
    public int getPersistentOrder() {
        return 6;
    }

    @Override
    public boolean isUsePersistentId() {
        return false;
    }

    @Override
    public boolean isIndenpendentOnModel() {
        return false;
    }

    public String buildVirtualDiagramId() {
        return name + "_" + hashCode();
    }

    // ===================================================================================
    //                                                                      Basic Override
    //                                                                      ==============
    @Override
    public String toString() {
        return getClass().getSimpleName() + ":{" + name + ", tables=" + (tables != null ? tables.size() : null) + "}";
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return ""; // unsupported here
    }

    public List<ERVirtualTable> getVirtualTables() {
        return tables;
    }

    public void setVirtualTables(List<ERVirtualTable> tables) { // when e.g. loading XML
        this.tables = tables;
    }

    public List<WalkerNote> getWalkerNotes() {
        return notes;
    }

    public void setWalkerNotes(List<WalkerNote> notes) { // when e.g. loading XML
        this.notes = notes;
    }

    public List<WalkerGroup> getWalkerGroups() {
        return groups;
    }

    public void setWalkerGroups(List<WalkerGroup> groups) {
        this.groups = groups;
        this.firePropertyChange(PROPERTY_CHANGE_VTABLES, null, null);
    }

    public Set<ERVirtualTable> getVirtualTableSet() {
        final TreeSet<ERVirtualTable> set = new TreeSet<>(Comparator.comparing(o -> o.getRawTable()));
        set.addAll(getVirtualTables());
        return set;
    }

    public Point getMousePoint() {
        return mousePoint;
    }

    public void setMousePoint(Point mousePoint) {
        this.mousePoint = mousePoint;
    }
}
