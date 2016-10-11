package org.dbflute.erflute.editor.model.diagram_contents.element.node.ermodel;

import java.util.ArrayList;
import java.util.List;

import org.dbflute.erflute.editor.model.ERDiagram;
import org.dbflute.erflute.editor.model.diagram_contents.element.connection.Relationship;
import org.dbflute.erflute.editor.model.diagram_contents.element.node.DiagramWalker;
import org.dbflute.erflute.editor.model.diagram_contents.element.node.note.Note;
import org.dbflute.erflute.editor.model.diagram_contents.element.node.table.ERTable;
import org.dbflute.erflute.editor.model.diagram_contents.element.node.table.ERVirtualTable;
import org.dbflute.erflute.editor.model.diagram_contents.element.node.table.TableView;

/**
 * @author modified by jflute (originated in ermaster)
 */
public class ERVirtualDiagram extends DiagramWalker {

    private static final long serialVersionUID = 1L;
    public static final String PROPERTY_CHANGE_VTABLES = "vtables";

    private int[] defaultColor;
    private String name;

    private List<ERVirtualTable> tables;
    private List<Note> notes;
    private List<VGroup> groups;

    public ERVirtualDiagram(ERDiagram diagram) {
        setDiagram(diagram);
        tables = new ArrayList<ERVirtualTable>();
        notes = new ArrayList<Note>();
        groups = new ArrayList<VGroup>();
    }

    @Override
    public String getObjectType() {
        return "ermodel";
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

    public void setDefaultColor(int red, int green, int blue) {
        this.defaultColor = new int[3];
        this.defaultColor[0] = red;
        this.defaultColor[1] = green;
        this.defaultColor[2] = blue;
    }

    public ERVirtualTable findVirtualTable(TableView table) {
        for (final ERVirtualTable vtable : tables) {
            if (vtable.getRawTable().getPhysicalName().equals(table.getPhysicalName())) {
                return vtable;
            }
        }
        return null;
    }

    public void deleteRelation(Relationship relation) {
        for (final ERVirtualTable vtable : tables) {
            vtable.removeOutgoing(relation);
            vtable.removeIncoming(relation);
        }
    }

    public void createRelation(Relationship relation) {
        boolean dirty = false;
        for (final ERVirtualTable vtable : tables) {
            if (relation.getSourceTableView().equals(vtable.getRawTable())) {
                dirty = true;
            } else if (relation.getTargetTableView().equals(vtable.getRawTable())) {
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

    public void addNewContent(DiagramWalker element) {
        if (element instanceof Note) {
            ((Note) element).setVirtualDiagram(this);
        } else if (element instanceof VGroup) {
            ((VGroup) element).setModel(this);
        } else {
            getDiagram().addContent(element);
        }
        int[] color = defaultColor;
        if (color == null) {
            color = getDiagram().getDefaultColor();
        }
        element.setColor(color[0], color[1], color[2]);
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
        if (element instanceof Note) {
            notes.add((Note) element);
            firePropertyChange(PROPERTY_CHANGE_VTABLES, null, null);
        } else if (element instanceof VGroup) {
            groups.add(((VGroup) element));
            firePropertyChange(PROPERTY_CHANGE_VTABLES, null, null);
        }
    }

    public void addGroup(VGroup group) {
        groups.add(group);
        firePropertyChange(PROPERTY_CHANGE_VTABLES, null, null);
    }

    public void remove(VGroup element) {
        groups.remove(element);
        this.firePropertyChange(PROPERTY_CHANGE_VTABLES, null, null);
    }

    public void remove(Note element) {
        notes.remove(element);
        this.firePropertyChange(PROPERTY_CHANGE_VTABLES, null, null);
    }

    @Override
    public int getPersistentOrder() {
        return 6;
    }

    @Override
    public boolean isUsePersistentId() {
        return true;
    }

    @Override
    public boolean isIndenpendentOnModel() {
        return false;
    }

    public String buildVirtualDiagramId() {
        return name + "_" + hashCode();
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

    public List<ERVirtualTable> getTables() {
        return tables;
    }

    public void setTables(List<ERVirtualTable> tables) { // when e.g. loading XML
        this.tables = tables;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) { // when e.g. loading XML
        this.notes = notes;
    }

    public List<VGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<VGroup> groups) {
        this.groups = groups;
        this.firePropertyChange(PROPERTY_CHANGE_VTABLES, null, null);
    }
}