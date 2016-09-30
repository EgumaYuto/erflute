package org.insightech.er.editor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.MouseWheelZoomHandler;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.dnd.AbstractTransferDragSourceListener;
import org.eclipse.gef.dnd.AbstractTransferDropTargetListener;
import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.DirectEditAction;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.gef.ui.actions.ToggleGridAction;
import org.eclipse.gef.ui.actions.ZoomComboContributionItem;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.parts.GraphicalEditorWithPalette;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheetPage;
import org.insightech.er.Activator;
import org.insightech.er.DesignResources;
import org.insightech.er.editor.controller.editpart.element.ERDiagramEditPart;
import org.insightech.er.editor.controller.editpart.element.ERDiagramEditPartFactory;
import org.insightech.er.editor.controller.editpart.element.PagableFreeformRootEditPart;
import org.insightech.er.editor.controller.editpart.element.node.ERTableEditPart;
import org.insightech.er.editor.extension.ExtensionLoader;
import org.insightech.er.editor.model.ERDiagram;
import org.insightech.er.editor.model.diagram_contents.element.node.table.ERTable;
import org.insightech.er.editor.view.ERDiagramGotoMarker;
import org.insightech.er.editor.view.ERDiagramPopupMenuManager;
import org.insightech.er.editor.view.action.category.CategoryManageAction;
import org.insightech.er.editor.view.action.category.ChangeFreeLayoutAction;
import org.insightech.er.editor.view.action.category.ChangeShowReferredTablesAction;
import org.insightech.er.editor.view.action.dbexport.ExportToDBAction;
import org.insightech.er.editor.view.action.dbexport.ExportToDDLAction;
import org.insightech.er.editor.view.action.dbexport.ExportToImageAction;
import org.insightech.er.editor.view.action.dbimport.ImportFromDBAction;
import org.insightech.er.editor.view.action.dbimport.ImportFromFileAction;
import org.insightech.er.editor.view.action.edit.ChangeBackgroundColorAction;
import org.insightech.er.editor.view.action.edit.CopyAction;
import org.insightech.er.editor.view.action.edit.DeleteWithoutUpdateAction;
import org.insightech.er.editor.view.action.edit.EditAllAttributesAction;
import org.insightech.er.editor.view.action.edit.EditExcelAction;
import org.insightech.er.editor.view.action.edit.PasteAction;
import org.insightech.er.editor.view.action.edit.SelectAllContentsAction;
import org.insightech.er.editor.view.action.ermodel.ERModelAddAction;
import org.insightech.er.editor.view.action.ermodel.ERModelQuickOutlineAction;
import org.insightech.er.editor.view.action.group.GroupManageAction;
import org.insightech.er.editor.view.action.line.DefaultLineAction;
import org.insightech.er.editor.view.action.line.ERDiagramAlignmentAction;
import org.insightech.er.editor.view.action.line.ERDiagramMatchHeightAction;
import org.insightech.er.editor.view.action.line.ERDiagramMatchWidthAction;
import org.insightech.er.editor.view.action.line.HorizontalLineAction;
import org.insightech.er.editor.view.action.line.ResizeModelAction;
import org.insightech.er.editor.view.action.line.RightAngleLineAction;
import org.insightech.er.editor.view.action.line.VerticalLineAction;
import org.insightech.er.editor.view.action.option.OptionSettingAction;
import org.insightech.er.editor.view.action.option.notation.ChangeCapitalAction;
import org.insightech.er.editor.view.action.option.notation.ChangeNotationExpandGroupAction;
import org.insightech.er.editor.view.action.option.notation.ChangeStampAction;
import org.insightech.er.editor.view.action.option.notation.ChangeTitleFontSizeAction;
import org.insightech.er.editor.view.action.option.notation.LockEditAction;
import org.insightech.er.editor.view.action.option.notation.ToggleMainColumnAction;
import org.insightech.er.editor.view.action.option.notation.design.ChangeDesignToFrameAction;
import org.insightech.er.editor.view.action.option.notation.design.ChangeDesignToFunnyAction;
import org.insightech.er.editor.view.action.option.notation.design.ChangeDesignToSimpleAction;
import org.insightech.er.editor.view.action.option.notation.level.ChangeNotationLevelToColumnAction;
import org.insightech.er.editor.view.action.option.notation.level.ChangeNotationLevelToDetailAction;
import org.insightech.er.editor.view.action.option.notation.level.ChangeNotationLevelToExcludeTypeAction;
import org.insightech.er.editor.view.action.option.notation.level.ChangeNotationLevelToNameAndKeyAction;
import org.insightech.er.editor.view.action.option.notation.level.ChangeNotationLevelToOnlyKeyAction;
import org.insightech.er.editor.view.action.option.notation.level.ChangeNotationLevelToOnlyTitleAction;
import org.insightech.er.editor.view.action.option.notation.system.ChangeToIDEF1XNotationAction;
import org.insightech.er.editor.view.action.option.notation.system.ChangeToIENotationAction;
import org.insightech.er.editor.view.action.option.notation.type.ChangeViewToBothAction;
import org.insightech.er.editor.view.action.option.notation.type.ChangeViewToLogicalAction;
import org.insightech.er.editor.view.action.option.notation.type.ChangeViewToPhysicalAction;
import org.insightech.er.editor.view.action.printer.PageSettingAction;
import org.insightech.er.editor.view.action.printer.PrintImageAction;
import org.insightech.er.editor.view.action.search.SearchAction;
import org.insightech.er.editor.view.action.tracking.ChangeTrackingAction;
import org.insightech.er.editor.view.action.translation.TranslationManageAction;
import org.insightech.er.editor.view.action.zoom.ZoomAdjustAction;
import org.insightech.er.editor.view.contributor.ERDiagramActionBarContributor;
import org.insightech.er.editor.view.drag_drop.ERDiagramTransferDragSourceListener;
import org.insightech.er.editor.view.drag_drop.ERDiagramTransferDropTargetListener;
import org.insightech.er.editor.view.outline.ERDiagramOutlinePage;
import org.insightech.er.editor.view.outline.ERDiagramOutlinePopupMenuManager;
import org.insightech.er.editor.view.property_source.ERDiagramPropertySourceProvider;
import org.insightech.er.editor.view.tool.ERDiagramPaletteRoot;

/**
 * #analyze may be view diagram, created by ERDiagramMultiPageEditor
 * @author modified by jflute (originated in ermaster)
 */
public class ERDiagramEditor extends GraphicalEditorWithPalette {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    public static final String ACTION_OUTLINE = "_outline";

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    protected final ERDiagram diagram;
    protected final ERDiagramEditPartFactory editPartFactory;
    protected final ZoomComboContributionItem zoomComboContributionItem;
    protected final ERDiagramOutlinePage outlinePage;
    protected final PropertySheetPage propertySheetPage;

    protected ERDiagramActionBarContributor actionBarContributor;
    protected IGotoMarker gotoMaker;
    protected MenuManager outlineMenuMgr;
    protected ExtensionLoader extensionLoader;
    protected boolean isDirty;
    protected final Map<IMarker, Object> markedObjectMap = new HashMap<IMarker, Object>();

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    public ERDiagramEditor(ERDiagram diagram, ERDiagramEditPartFactory editPartFactory, ZoomComboContributionItem zoomComboContributionItem,
            ERDiagramOutlinePage outlinePage) {
        this.setEditDomain(new DefaultEditDomain(this));
        this.diagram = diagram;
        this.editPartFactory = editPartFactory;
        this.zoomComboContributionItem = zoomComboContributionItem;
        this.outlinePage = outlinePage;
        this.propertySheetPage = new PropertySheetPage();
        this.propertySheetPage.setPropertySourceProvider(new ERDiagramPropertySourceProvider());
        try {
            this.extensionLoader = new ExtensionLoader(this);
        } catch (final CoreException e) {
            Activator.showExceptionDialog(e);
        }
    }

    // ===================================================================================
    //                                                                               ???
    //                                                                            ========
    @Override
    public void dispose() {
        this.getSelectionSynchronizer().removeViewer(this.outlinePage.getViewer());
        super.dispose();
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
        this.getCommandStack().markSaveLocation();
        this.isDirty = false;
    }

    @Override
    public void commandStackChanged(EventObject eventObject) {
        this.firePropertyChange(IEditorPart.PROP_DIRTY);
        super.commandStackChanged(eventObject);
    }

    @Override
    protected void initializeGraphicalViewer() {
        final GraphicalViewer viewer = this.getGraphicalViewer();
        viewer.setEditPartFactory(editPartFactory);
        this.initViewerAction(viewer);
        this.initDragAndDrop(viewer);
        viewer.setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.MOD1), MouseWheelZoomHandler.SINGLETON);
        viewer.setProperty(SnapToGrid.PROPERTY_GRID_ENABLED, true);
        viewer.setProperty(SnapToGrid.PROPERTY_GRID_VISIBLE, true);
        viewer.setProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED, true);
        final MenuManager menuMgr = new ERDiagramPopupMenuManager(this.getActionRegistry(), this.diagram);
        this.extensionLoader.addERDiagramPopupMenu(menuMgr, this.getActionRegistry());
        viewer.setContextMenu(menuMgr);
        this.outlineMenuMgr = new ERDiagramOutlinePopupMenuManager(this.diagram, this.getActionRegistry(),
                this.outlinePage.getOutlineActionRegistory(), this.outlinePage.getViewer());
        this.gotoMaker = new ERDiagramGotoMarker(this);
    }

    @Override
    protected PaletteRoot getPaletteRoot() {
        return new ERDiagramPaletteRoot();
    }

    @Override
    public Object getAdapter(@SuppressWarnings("rawtypes") Class type) {
        if (type == ZoomManager.class) {
            return ((ScalableFreeformRootEditPart) getGraphicalViewer().getRootEditPart()).getZoomManager();
        }
        if (type == IContentOutlinePage.class) {
            return this.outlinePage;
        }
        if (type == IGotoMarker.class) {
            return this.gotoMaker;
        }
        if (type == IPropertySheetPage.class) {
            return this.propertySheetPage;
        }
        return super.getAdapter(type);
    }

    public void changeCategory() {
        this.outlinePage.setCategory(this.getEditDomain(), this.getGraphicalViewer(), this.outlineMenuMgr, this.getActionRegistry());
        this.getSelectionSynchronizer().addViewer(this.outlinePage.getViewer());
    }

    @Override
    public void setFocus() {
        if (getGraphicalViewer().getContents() == null) {
            getGraphicalViewer().setContents(diagram);
        }
        super.setFocus();
    }

    public void removeSelection() {
        this.getSelectionSynchronizer().removeViewer(this.outlinePage.getViewer());
    }

    // TODO jflute ermaster: 何度も呼ばれている疑惑、増えていく増えていく
    @Override
    @SuppressWarnings("unchecked")
    protected void createActions() {
        super.createActions();

        final ActionRegistry registry = this.getActionRegistry();
        final List<String> selectionActionList = this.getSelectionActions();

        final List<IAction> actionList = new ArrayList<IAction>(Arrays.asList(new IAction[] { new ChangeViewToLogicalAction(this),
                new ChangeViewToPhysicalAction(this), new ChangeViewToBothAction(this), new ChangeToIENotationAction(this),
                new ChangeToIDEF1XNotationAction(this), new ChangeNotationLevelToColumnAction(this),
                new ChangeNotationLevelToExcludeTypeAction(this), new ChangeNotationLevelToDetailAction(this),
                new ChangeNotationLevelToOnlyTitleAction(this), new ChangeNotationLevelToOnlyKeyAction(this),
                new ChangeNotationLevelToNameAndKeyAction(this), new ChangeNotationExpandGroupAction(this),
                new ChangeDesignToFunnyAction(this), new ChangeDesignToFrameAction(this), new ChangeDesignToSimpleAction(this),
                new ChangeCapitalAction(this), new ChangeTitleFontSizeAction(this), new ChangeStampAction(this),
                new GroupManageAction(this), new ChangeTrackingAction(this), new OptionSettingAction(this), new CategoryManageAction(this),
                new ChangeFreeLayoutAction(this), new ChangeShowReferredTablesAction(this), new TranslationManageAction(this),
                /* #deleted new TestDataCreateAction(this), */new ImportFromDBAction(this), new ImportFromFileAction(this),
                new ExportToImageAction(this), /* #deleted new ExportToExcelAction(this), */
                /* #deleted new ExportToHtmlAction(this), new ExportToJavaAction(this), */new ExportToDDLAction(this),
                /* #deleted new ExportToDictionaryAction(this), new ExportToTranslationDictionaryAction(this), */
                /* #deleted new ExportToTestDataAction(this), */new PageSettingAction(this), new EditAllAttributesAction(this),
                new DirectEditAction((IWorkbenchPart) this), new ERDiagramAlignmentAction(this, PositionConstants.LEFT),
                new ERDiagramAlignmentAction(this, PositionConstants.CENTER), new ERDiagramAlignmentAction(this, PositionConstants.RIGHT),
                new ERDiagramAlignmentAction(this, PositionConstants.TOP), new ERDiagramAlignmentAction(this, PositionConstants.MIDDLE),
                new ERDiagramAlignmentAction(this, PositionConstants.BOTTOM), new ERDiagramMatchWidthAction(this),
                new ERDiagramMatchHeightAction(this), new HorizontalLineAction(this), new VerticalLineAction(this),
                new RightAngleLineAction(this), new DefaultLineAction(this), new CopyAction(this), new PasteAction(this),
                new SearchAction(this), new ResizeModelAction(this), new PrintImageAction(this), new DeleteWithoutUpdateAction(this),
                new SelectAllContentsAction(this), new ERModelAddAction(this), new ERModelQuickOutlineAction(this),
                //						new ChangeNameAction(this),
        }));

        actionList.addAll(this.extensionLoader.createExtendedActions());
        for (final IAction action : actionList) {
            if (action instanceof SelectionAction) {
                final IAction originalAction = registry.getAction(action.getId());
                if (originalAction != null) {
                    selectionActionList.remove(originalAction);
                }
                selectionActionList.add(action.getId());
            }
            registry.registerAction(action);
        }
        this.addKeyHandler(registry.getAction(SearchAction.ID));
        this.addKeyHandler(registry.getAction(ERModelQuickOutlineAction.ID));
    }

    @SuppressWarnings("unchecked")
    protected void initViewerAction(GraphicalViewer viewer) {
        final ScalableFreeformRootEditPart rootEditPart = new PagableFreeformRootEditPart(this.diagram);
        viewer.setRootEditPart(rootEditPart);

        final ZoomManager manager = rootEditPart.getZoomManager();

        final double[] zoomLevels = new double[] { 0.1, 0.25, 0.5, 0.75, 0.8, 1.0, 1.5, 2.0, 2.5, 3.0, 4.0, 5.0, 10.0, 20.0 };
        manager.setZoomLevels(zoomLevels);

        final List<String> zoomContributions = new ArrayList<String>();
        zoomContributions.add(ZoomManager.FIT_ALL);
        zoomContributions.add(ZoomManager.FIT_HEIGHT);
        zoomContributions.add(ZoomManager.FIT_WIDTH);
        manager.setZoomLevelContributions(zoomContributions);

        final ZoomInAction zoomInAction = new ZoomInAction(manager);
        final ZoomOutAction zoomOutAction = new ZoomOutAction(manager);
        final ZoomAdjustAction zoomAdjustAction = new ZoomAdjustAction(manager);

        this.getActionRegistry().registerAction(zoomInAction);
        this.getActionRegistry().registerAction(zoomOutAction);
        this.getActionRegistry().registerAction(zoomAdjustAction);

        this.addKeyHandler(zoomInAction);
        this.addKeyHandler(zoomOutAction);

        final IFigure gridLayer = rootEditPart.getLayer(LayerConstants.GRID_LAYER);
        gridLayer.setForegroundColor(DesignResources.GRID_COLOR);

        IAction action = new ToggleGridAction(viewer);
        this.getActionRegistry().registerAction(action);

        action = new ChangeBackgroundColorAction(this, this.diagram);
        this.getActionRegistry().registerAction(action);
        this.getSelectionActions().add(action.getId());

        action = new EditExcelAction(this, this.diagram);
        this.getActionRegistry().registerAction(action);
        this.getSelectionActions().add(action.getId());

        action = new ToggleMainColumnAction(this);
        this.getActionRegistry().registerAction(action);

        action = new LockEditAction(this);
        this.getActionRegistry().registerAction(action);

        action = new ExportToDBAction(this);
        this.getActionRegistry().registerAction(action);

        this.actionBarContributor = new ERDiagramActionBarContributor(this.zoomComboContributionItem);
    }

    protected void initDragAndDrop(GraphicalViewer viewer) {
        final AbstractTransferDragSourceListener dragSourceListener =
                new ERDiagramTransferDragSourceListener(viewer, TemplateTransfer.getInstance());
        viewer.addDragSourceListener(dragSourceListener);
        final AbstractTransferDropTargetListener dropTargetListener =
                new ERDiagramTransferDropTargetListener(viewer, TemplateTransfer.getInstance());
        viewer.addDropTargetListener(dropTargetListener);
    }

    private void addKeyHandler(IAction action) {
        final IHandlerService service = this.getSite().getService(IHandlerService.class);
        service.activateHandler(action.getActionDefinitionId(), new ActionHandler(action));
    }

    @Override
    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        final IEditorPart editorPart = getSite().getPage().getActiveEditor();

        if (editorPart instanceof ERDiagramMultiPageEditor) {
            final ERDiagramMultiPageEditor multiPageEditorPart = (ERDiagramMultiPageEditor) editorPart;

            if (this.equals(multiPageEditorPart.getActiveEditor())) {
                updateActions(this.getSelectionActions());
            }

        } else {
            super.selectionChanged(part, selection);
        }
    }

    public Point getLocation() {
        final FigureCanvas canvas = (FigureCanvas) this.getGraphicalViewer().getControl();
        return canvas.getViewport().getViewLocation();
    }

    public void setLocation(int x, int y) {
        final FigureCanvas canvas = (FigureCanvas) this.getGraphicalViewer().getControl();
        canvas.scrollTo(x, y);
    }

    public Object getMarkedObject(IMarker marker) {
        return markedObjectMap.get(marker);
    }

    public void setMarkedObject(IMarker marker, Object markedObject) {
        this.markedObjectMap.put(marker, markedObject);
    }

    public void clearMarkedObject() {
        this.markedObjectMap.clear();
    }

    public String getProjectFilePath(String extention) {
        final IFile file = ((IFileEditorInput) this.getEditorInput()).getFile();
        final String filePath = file.getLocation().toOSString();
        return filePath.substring(0, filePath.lastIndexOf(".")) + extention;
    }

    public DefaultEditDomain getDefaultEditDomain() {
        return getEditDomain();
    }

    public ActionRegistry getDefaultActionRegistry() {
        return getActionRegistry();
    }

    public void reveal(ERTable table) {
        final ERDiagramEditPart editPart = (ERDiagramEditPart) getGraphicalViewer().getContents();
        final List<?> tableParts = editPart.getChildren();
        for (final Object tableEditPart : tableParts) {
            if (tableEditPart instanceof ERTableEditPart) {
                final ERTableEditPart vtableEditPart = (ERTableEditPart) tableEditPart;
                if (((ERTable) vtableEditPart.getModel()).equals(table)) {
                    getGraphicalViewer().reveal(vtableEditPart);
                    return;
                }
            }
        }
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    @Override
    public GraphicalViewer getGraphicalViewer() {
        return super.getGraphicalViewer();
    }

    public ERDiagramEditPartFactory getEditPartFactory() {
        return editPartFactory;
    }

    public ERDiagramActionBarContributor getActionBarContributor() {
        return actionBarContributor;
    }

    @Override
    public boolean isDirty() {
        return this.isDirty || super.isDirty();
    }

    public void setDirty(boolean isDirty) {
        this.isDirty = isDirty;
    }
}
