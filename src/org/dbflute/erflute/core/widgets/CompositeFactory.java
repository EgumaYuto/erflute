package org.dbflute.erflute.core.widgets;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Panel;
import java.nio.charset.Charset;

import org.dbflute.erflute.core.DesignResources;
import org.dbflute.erflute.core.DisplayMessages;
import org.dbflute.erflute.core.dialog.AbstractDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;

public class CompositeFactory {

    public static SpinnerWithScale createSpinnerWithScale(
            AbstractDialog dialog, Composite composite, String title, int minimum, int maximum) {
        return createSpinnerWithScale(dialog, composite, title, "%", minimum, maximum);
    }

    public static SpinnerWithScale createSpinnerWithScale(
            AbstractDialog dialog, Composite composite, String title, String unit, int minimum, int maximum) {
        if (title != null) {
            final Label label = new Label(composite, SWT.RIGHT);
            label.setText(DisplayMessages.getMessage(title));
        }

        final GridData scaleGridData = new GridData();
        final Scale scale = new Scale(composite, SWT.NONE);
        scale.setLayoutData(scaleGridData);

        int diff = 0;
        if (minimum < 0) {
            scale.setMinimum(0);
            scale.setMaximum(-minimum + maximum);
            diff = minimum;
        } else {
            scale.setMinimum(minimum);
            scale.setMaximum(maximum);
        }

        scale.setPageIncrement((maximum - minimum) / 10);
        final GridData spinnerGridData = new GridData();

        final Spinner spinner = new Spinner(composite, SWT.RIGHT | SWT.BORDER);
        spinner.setLayoutData(spinnerGridData);
        spinner.setMinimum(minimum);
        spinner.setMaximum(maximum);

        final Label label = new Label(composite, SWT.NONE);
        label.setText(unit);

        ListenerAppender.addModifyListener(scale, spinner, diff, dialog);

        return new SpinnerWithScale(spinner, scale, diff);
    }

    public static Combo createReadOnlyCombo(AbstractDialog dialog, Composite composite, String title) {
        return createReadOnlyCombo(dialog, composite, title, 1, -1);
    }

    public static Combo createReadOnlyCombo(AbstractDialog dialog, Composite composite, String title, int span, int width) {
        if (title != null) {
            final Label label = new Label(composite, SWT.RIGHT);
            label.setText(DisplayMessages.getMessage(title));
        }

        final GridData gridData = new GridData();
        gridData.horizontalSpan = span;

        if (width > 0) {
            gridData.widthHint = width;
        } else {
            gridData.horizontalAlignment = GridData.FILL;
            gridData.grabExcessHorizontalSpace = true;
        }

        final Combo combo = new Combo(composite, SWT.READ_ONLY);
        combo.setLayoutData(gridData);

        ListenerAppender.addComboListener(combo, dialog, false);

        return combo;
    }

    public static Combo createCombo(AbstractDialog dialog, Composite composite, String title, int span) {
        if (title != null) {
            final Label label = new Label(composite, SWT.RIGHT);
            label.setText(DisplayMessages.getMessage(title));
        }

        final GridData gridData = new GridData();
        gridData.horizontalSpan = span;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;

        final Combo combo = new Combo(composite, SWT.NONE);
        combo.setLayoutData(gridData);

        ListenerAppender.addComboListener(combo, dialog, false);

        return combo;
    }

    // ===================================================================================
    //                                                                 File Encoding Combo
    //                                                                 ===================
    public static Combo createFileEncodingCombo(
            IEditorPart editorPart, AbstractDialog dialog, Composite composite, String title, int span, String selectedCharset) {
        final Combo fileEncodingCombo = createReadOnlyCombo(dialog, composite, title, span, -1);
        for (final Charset charset : Charset.availableCharsets().values()) {
            fileEncodingCombo.add(charset.displayName());
        }
        // #for_erflute default is specified
        //final IFile file = ((IFileEditorInput) editorPart.getEditorInput()).getFile();
        //final IProject project = file.getProject();
        final Charset defautlCharset = Charset.forName(selectedCharset);
        fileEncodingCombo.setText(defautlCharset.displayName());
        return fileEncodingCombo;
    }

    // ===================================================================================
    //                                                                               Text
    //                                                                              ======
    public static Text createText(AbstractDialog dialog, Composite composite, String title, boolean imeOn) {
        return createText(dialog, composite, title, 1, imeOn);
    }

    public static Text createText(AbstractDialog dialog, Composite composite, String title, int span, boolean imeOn) {
        return createText(dialog, composite, title, span, -1, imeOn);
    }

    public static Text createText(AbstractDialog dialog, Composite composite, String title, int span, int width, boolean imeOn) {
        return createText(dialog, composite, title, span, width, SWT.BORDER, imeOn);
    }

    public static Text createNumText(AbstractDialog dialog, Composite composite, String title) {
        return createNumText(dialog, composite, title, -1);
    }

    public static Text createNumText(AbstractDialog dialog, Composite composite, String title, int width) {
        return createNumText(dialog, composite, title, 1, width);
    }

    public static Text createNumText(AbstractDialog dialog, Composite composite, String title, int span, int width) {
        return createText(dialog, composite, title, span, width, SWT.BORDER | SWT.RIGHT, false);
    }

    public static Text createText(AbstractDialog dialog, Composite composite, String title, int span, int width, int style, boolean imeOn) {
        if (title != null) {
            final Label label = new Label(composite, SWT.NONE);
            label.setText(DisplayMessages.getMessage(title));
        }
        final GridData textGridData = new GridData();
        textGridData.horizontalSpan = span;
        if (width > 0) {
            textGridData.widthHint = width;
        } else {
            textGridData.horizontalAlignment = GridData.FILL;
            textGridData.grabExcessHorizontalSpace = true;
        }
        final Text text = new Text(composite, style);
        text.setLayoutData(textGridData);
        ListenerAppender.addTextListener(text, dialog, imeOn);
        return text;
    }

    // ===================================================================================
    //                                                                               Label
    //                                                                               =====
    public static Label createExampleLabel(Composite composite, String title) {
        return createExampleLabel(composite, title, -1);
    }

    public static Label createExampleLabel(Composite composite, String title, int span) {
        final Label label = new Label(composite, SWT.NONE);
        label.setText(DisplayMessages.getMessage(title));

        if (span > 0) {
            final GridData gridData = new GridData();
            gridData.horizontalSpan = span;
            label.setLayoutData(gridData);
        }

        final FontData fontData = Display.getCurrent().getSystemFont().getFontData()[0];
        final Font font = new Font(Display.getCurrent(), fontData.getName(), 8, SWT.NORMAL);
        label.setFont(font);

        return label;
    }

    public static void filler(Composite composite, int span) {
        filler(composite, span, -1);
    }

    public static void filler(Composite composite, int span, int width) {
        final GridData gridData = new GridData();
        gridData.horizontalSpan = span;
        if (width > 0) {
            gridData.widthHint = width;
        }
        final Label label = new Label(composite, SWT.NONE);
        label.setLayoutData(gridData);
    }

    public static Label createLabel(Composite composite, String title) {
        return createLabel(composite, title, -1);
    }

    public static Label createLabel(Composite composite, String title, int span) {
        return createLabel(composite, title, span, -1);
    }

    public static Label createLabel(Composite composite, String title, int span, int width) {
        final Label label = new Label(composite, SWT.NONE);
        label.setText(DisplayMessages.getMessage(title));
        if (span > 0 || width > 0) {
            final GridData gridData = new GridData();
            if (span > 0) {
                gridData.horizontalSpan = span;
            }
            if (width > 0) {
                gridData.widthHint = width;
            }
            label.setLayoutData(gridData);
        }
        return label;
    }

    // ===================================================================================
    //                                                                            Checkbox
    //                                                                            ========
    public static Button createCheckbox(AbstractDialog dialog, Composite composite, String title) {
        return createCheckbox(dialog, composite, title, -1);
    }

    public static Button createCheckbox(AbstractDialog dialog, Composite composite, String title, int span) {
        final Button checkbox = new Button(composite, SWT.CHECK);
        checkbox.setText(DisplayMessages.getMessage(title));
        if (span != -1) {
            final GridData gridData = new GridData();
            gridData.horizontalSpan = span;
            checkbox.setLayoutData(gridData);
        }
        ListenerAppender.addCheckBoxListener(checkbox, dialog);
        return checkbox;
    }

    // ===================================================================================
    //                                                                               Radio
    //                                                                               =====
    public static Button createRadio(AbstractDialog dialog, Composite composite, String title) {
        return createRadio(dialog, composite, title, -1);
    }

    public static Button createRadio(AbstractDialog dialog, Composite composite, String title, int span) {
        return createRadio(dialog, composite, title, span, false);
    }

    public static Button createRadio(AbstractDialog dialog, Composite composite, String title, int span, boolean indent) {
        final Button radio = new Button(composite, SWT.RADIO);
        radio.setText(DisplayMessages.getMessage(title));
        final GridData gridData = new GridData();
        if (span != -1) {
            gridData.horizontalSpan = span;
        }
        if (indent) {
            gridData.horizontalIndent = DesignResources.INDENT;
        }
        radio.setLayoutData(gridData);
        ListenerAppender.addCheckBoxListener(radio, dialog);
        return radio;
    }

    // ===================================================================================
    //                                                                           Text Area
    //                                                                           =========
    public static Text createTextArea(
            AbstractDialog dialog, Composite composite, String title, int width, int height, int span, boolean selectAll, boolean imeOn) {
        if (title != null) {
            final Label label = new Label(composite, SWT.NONE);
            label.setText(DisplayMessages.getMessage(title));
        }
        final GridData textAreaGridData = new GridData();
        textAreaGridData.heightHint = height;
        textAreaGridData.grabExcessHorizontalSpace = true;
        textAreaGridData.horizontalSpan = span;
        textAreaGridData.horizontalAlignment = GridData.FILL;
        textAreaGridData.widthHint = width;
        final Text text = new Text(composite, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
        text.setLayoutData(textAreaGridData);
        ListenerAppender.addTextAreaListener(text, dialog, selectAll, imeOn);
        return text;
    }

    public static Text createTextArea(
            AbstractDialog dialog, Composite composite, String title, int width, int height, int span, boolean imeOn) {
        return createTextArea(dialog, composite, title, width, height, span, true, imeOn);
    }

    // ===================================================================================
    //                                                                              Button
    //                                                                              ======
    public static Button createButton(Composite composite, String text) {
        return createButton(composite, text, -1);
    }

    public static Button createButton(Composite composite, String text, int span) {
        final GridData gridData = new GridData();
        if (span != -1) {
            gridData.horizontalSpan = span;
        } else {
            gridData.widthHint = DesignResources.BUTTON_WIDTH;
        }

        final Button button = new Button(composite, SWT.NONE);
        button.setText(DisplayMessages.getMessage(text));
        button.setLayoutData(gridData);

        return button;
    }

    public static Button createAddButton(Composite composite) {
        final GridData gridData = new GridData();
        gridData.grabExcessVerticalSpace = true;
        gridData.verticalAlignment = GridData.END;
        gridData.widthHint = DesignResources.BUTTON_WIDTH;

        final Button button = new Button(composite, SWT.NONE);
        button.setText(DisplayMessages.getMessage("label.right.arrow"));
        button.setLayoutData(gridData);

        return button;
    }

    public static Button createRemoveButton(Composite composite) {
        final GridData gridData = new GridData();
        gridData.grabExcessVerticalSpace = true;
        gridData.verticalAlignment = GridData.BEGINNING;
        gridData.widthHint = DesignResources.BUTTON_WIDTH;

        final Button button = new Button(composite, SWT.NONE);
        button.setText(DisplayMessages.getMessage("label.left.arrow"));
        button.setLayoutData(gridData);

        return button;
    }

    // ===================================================================================
    //                                                                               Table
    //                                                                               =====
    public static Table createTable(Composite composite, int height, int span) {
        final GridData gridData = new GridData();
        gridData.horizontalSpan = span;
        gridData.heightHint = height;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;

        final Table table = new Table(composite, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
        table.setLayoutData(gridData);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        return table;
    }

    public static TableColumn createTableColumn(Table table, String text, int width, int style) {
        final TableColumn tableColumn = new TableColumn(table, style);
        tableColumn.setText(DisplayMessages.getMessage(text));
        tableColumn.setWidth(width);
        tableColumn.setAlignment(style);

        return tableColumn;
    }

    public static TableEditor createCheckBoxTableEditor(TableItem tableItem, boolean selection, int column) {
        final Table table = tableItem.getParent();

        final Button checkBox = new Button(table, SWT.CHECK);
        checkBox.pack();

        final TableEditor editor = new TableEditor(table);
        editor.minimumWidth = checkBox.getSize().x;
        editor.horizontalAlignment = SWT.CENTER;
        editor.setEditor(checkBox, tableItem, column);

        checkBox.setSelection(selection);

        return editor;
    }

    public static RowHeaderTable createRowHeaderTable(Composite parent, int width, int height, int rowHeaderWidth, int rowHeight, int span,
            boolean iconEnable, boolean editable) {
        final Composite composite = new Composite(parent, SWT.EMBEDDED);
        final GridData gridData = new GridData();
        gridData.horizontalSpan = span;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.heightHint = height;
        composite.setLayoutData(gridData);

        return createTable(composite, width, height, rowHeaderWidth, rowHeight, iconEnable, editable);
    }

    private static RowHeaderTable createTable(Composite composite, int width, int height, int rowHeaderWidth, int rowHeight,
            boolean iconEnable, boolean editable) {
        final Frame frame = SWT_AWT.new_Frame(composite);
        frame.setLayout(new FlowLayout());

        final Panel panel = new Panel();
        panel.setLayout(new FlowLayout());
        frame.add(panel);
        final RowHeaderTable table = new RowHeaderTable(width, height, rowHeaderWidth, rowHeight, iconEnable, editable);
        panel.add(table);

        return table;
    }

    // ===================================================================================
    //                                                                     Child Composite
    //                                                                     ===============
    public static Composite createChildComposite(Composite parent, int span, int numColumns) {
        return createChildComposite(parent, -1, span, numColumns);
    }

    public static Composite createChildComposite(Composite parent, int height, int span, int numColumns) {
        final Composite composite = new Composite(parent, SWT.NONE);
        final GridData gridData = new GridData();
        gridData.horizontalSpan = span;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        if (height >= 0) {
            gridData.heightHint = height;
        }
        composite.setLayoutData(gridData);
        final GridLayout gridLayout = new GridLayout();
        gridLayout.marginWidth = 0;
        gridLayout.numColumns = numColumns;
        composite.setLayout(gridLayout);
        return composite;
    }
}
