package org.dbflute.erflute.db.impl.mysql;

import org.dbflute.erflute.editor.model.diagram_contents.element.node.table.properties.TableProperties;

public class MySQLTableProperties extends TableProperties {

    private static final long serialVersionUID = 1L;

    private String storageEngine;

    private String characterSet;

    private String collation;

    private Integer primaryKeyLengthOfText;

    public String getStorageEngine() {
        return storageEngine;
    }

    public void setStorageEngine(String storageEngine) {
        this.storageEngine = storageEngine;
    }

    public String getCharacterSet() {
        return characterSet;
    }

    public void setCharacterSet(String characterSet) {
        this.characterSet = characterSet;
    }

    public String getCollation() {
        return collation;
    }

    public void setCollation(String collation) {
        this.collation = collation;
    }

    public Integer getPrimaryKeyLengthOfText() {
        return primaryKeyLengthOfText;
    }

    public void setPrimaryKeyLengthOfText(Integer primaryKeyLengthOfText) {
        this.primaryKeyLengthOfText = primaryKeyLengthOfText;
    }
}
