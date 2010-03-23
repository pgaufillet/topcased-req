/*****************************************************************************
 * Copyright (c) 2008 TOPCASED consortium.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Quentin Glineur (Obeo) <quentin.glineur@obeo.fr>
 *    
 ******************************************************************************/
package org.topcased.requirement.sam.views.preview;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.text.source.VerticalRuler;
import org.eclipse.jface.text.source.projection.IProjectionPosition;
import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.jface.text.source.projection.ProjectionAnnotationModel;
import org.eclipse.jface.text.source.projection.ProjectionSupport;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.Page;
import org.topcased.requirement.AnonymousRequirement;
import org.topcased.requirement.CurrentRequirement;
import org.topcased.requirement.HierarchicalElement;
import org.topcased.requirement.Requirement;
import org.topcased.requirement.RequirementProject;
import org.topcased.requirement.SpecialChapter;
import org.topcased.requirement.core.RequirementCorePlugin;
import org.topcased.requirement.core.utils.RequirementUtils;
import org.topcased.requirement.core.views.current.CurrentPage;
import org.topcased.requirement.core.views.current.CurrentRequirementView;
import org.topcased.sam.System;

import fr.obeo.acceleo.ecore.factories.FactoryException;
import fr.obeo.acceleo.gen.template.Template;
import fr.obeo.acceleo.gen.template.TemplateSyntaxExceptions;
import fr.obeo.acceleo.gen.template.eval.ENode;
import fr.obeo.acceleo.gen.template.eval.ENodeException;
import fr.obeo.acceleo.gen.template.eval.LaunchManager;
import fr.obeo.acceleo.gen.template.scripts.SpecificScript;
import fr.obeo.acceleo.gen.ui.AcceleoEcoreGenUiPlugin;
import fr.obeo.acceleo.gen.ui.AcceleoGenUIMessages;
import fr.obeo.acceleo.gen.ui.editors.reflective.AcceleoReflectiveSettings;
import fr.obeo.acceleo.gen.ui.editors.source.AcceleoSourceAnnotationModel;
import fr.obeo.acceleo.tools.plugins.AcceleoModuleProvider;
import fr.obeo.acceleo.tools.strings.Int2;
import fr.obeo.acceleo.tools.ui.graphics.ColorManager;

/**
 * Defines a page for the Document View.
 * 
 * @author <a href="mailto:quentin.glineur@obeo.fr">Quentin Glineur</a>
 * @author <a href="mailto:steve.monnier@obeo.fr">Steve Monnier</a>
 * 
 */
public class PreviewPage extends Page implements IPreviewPage
{

    /**
     * Color manager used for the content of the Document View
     */
    protected static final ColorManager COLOR_MANAGER = new ColorManager();

    /**
     * Foreground color for the text which is not in the model.
     */
    protected static final Color STATIC_TEXT_FOREGROUND = COLOR_MANAGER.getColor(new RGB(150, 50, 170));

    /**
     * Foreground color for the text which is comment
     */
    protected static final Color COMMENT_FOREGROUND = COLOR_MANAGER.getColor(new RGB(100, 180, 100));

    /**
     * Background color for the selected text
     */
    protected static final Color SELECTED_TEXT_BACKGROUND = COLOR_MANAGER.getColor(new RGB(230, 200, 250));

    /**
     * Foreground color for the selected text
     */
    protected static final Color SELECTED_TEXT_FOREGROUND = COLOR_MANAGER.getColor(new RGB(0, 0, 0));

    /**
     * Path of the acceleo template to generate the content of the Document View
     */
    protected static final String DOCUMENT_GENERATOR_PATH = "/org.topcased.requirement.sam/src/org/topcased/requirement/sam/views/preview/template/Document.mt";

    private Action modeSelectionAction;

    private List<ISelectionChangedListener> listeners = new ArrayList<ISelectionChangedListener>();

    private int updateSelection = -1;

    /**
     * Selection cache.
     */
    private Map<EObject, ENode> mapReqTexT = new HashMap<EObject, ENode>();

    private ProjectionViewer documentSourceViewer;

    /**
     * Current node of generation.
     */
    private ENode eval = null;

    /**
     * Object currently selected.
     */
    private EObject previousSelectionObject = null;

    /**
     * Object which is currently generated.
     */
    private EObject evalEObject = null;

    /**
     * Annotation model.
     */
    private AcceleoSourceAnnotationModel problemHandler = null;

    /**
     * Reflective editor settings that know available generators.
     */
    private AcceleoReflectiveSettings settings = new AcceleoReflectiveSettings();

    private TreeSelection currentRequirementViewSelection;

    private VerticalRuler ruler = new VerticalRuler(15);

    private ProjectionSupport projectionSupport;

    private ArrayList<Position> hiddenAnnotations;

    private boolean forceRefresh = false;

    private HierarchicalElement hierarchicalElementToFocusAfter = null;

    private ArrayList<HierarchicalElement> hierarchicalElementToForceRefresh = new ArrayList<HierarchicalElement>();

    private MouseListener fMouseListener = new MouseListener()
    {
        public void mouseUp(MouseEvent event)
        {
            int lineNumber = ruler.toDocumentLineNumber(event.y);

            if (1 == event.button)
            {
                ProjectionAnnotation annotation = findAnnotation(lineNumber, true);
                if (annotation != null)
                {
                    if (!(annotation instanceof PreviewPageProjectionAnnotation))
                    {
                        ProjectionAnnotationModel model = (ProjectionAnnotationModel) getModel();
                        model.toggleExpansionState(annotation);
                        // try
                        // {
                        // documentSourceViewer.setTextColor(STATIC_TEXT_FOREGROUND,
                        // documentSourceViewer.getDocument().getLineOffset(lineNumber), 19, false);
                        // documentSourceViewer.setTextColor(STATIC_TEXT_FOREGROUND,
                        // documentSourceViewer.getDocument().getLineOffset(lineNumber + 1) - 3, 1, true);
                        // }
                        // catch (BadLocationException e)
                        // {
                        // e.printStackTrace();
                        // }
                    }
                }
            }
        }

        public void mouseDoubleClick(MouseEvent e)
        {
            // Nothing happens
        }

        public void mouseDown(MouseEvent e)
        {
            // Nothing happens
        }
    };

    private INotifyChangedListener notifyChangedListener = new INotifyChangedListener()
    {
        public void notifyChanged(Notification notification)
        {
            if ((notification.getEventType() == Notification.ADD || notification.getEventType() == Notification.MOVE) && notification.getNewValue() instanceof System)
            {
                if (currentRequirementViewSelection != null)
                {
                    EObject currentSystem = (EObject) (notification.getNewValue());
                    do
                    {
                        HierarchicalElement hierarchicalElementFor = RequirementUtils.getHierarchicalElementFor(currentSystem);
                        if(hierarchicalElementFor != null){  
                            mapReqTexT.remove(hierarchicalElementFor);
                        }
                        //RequirementUtils.getHierarchicalElementFor(currentEObject)
                        currentSystem = currentSystem.eContainer();
                    }
                    while (currentSystem instanceof System && currentSystem != EcoreUtil.getRootContainer(currentSystem));

                    if (modeSelectionAction.isChecked())
                    {
                        forceRefresh = true;
                        handleCurrentRequirementViewSelectionChange((EObject) notification.getNewValue());
                    }
                    else
                    {
                        previousSelectionObject = null;
                        evalEObject = null;
                    }                    
                }
            }
            else if (notification.getEventType() == Notification.REMOVE || (notification.getEventType() == Notification.SET && notification.getNewValue() == null))
            {
                if (notification.getOldValue() instanceof Requirement && notification.getNewValue() == null)
                {
                    EObject hierarchicalTemp = hierarchicalElementToFocusAfter;
                    do
                    {
                        // Refresh when you remove a requirement in current requirement view
                        mapReqTexT.remove(hierarchicalTemp);
                        if (modeSelectionAction.isChecked())
                        {
                            // we refresh the view from the parent of this requirement
                            forceRefresh = true;
                            handleCurrentRequirementViewSelectionChange(hierarchicalElementToFocusAfter);
                        }
                        else
                        {
                            hierarchicalElementToForceRefresh.add(hierarchicalElementToFocusAfter);
                        }
                        hierarchicalTemp = hierarchicalTemp.eContainer();
                    }while(!(hierarchicalTemp instanceof RequirementProject));
                }
                else
                {
                    // Being notified of an object suppression in current requirement view, but not a requirement
                    previousSelectionObject = null;
                    evalEObject = null;
                    mapReqTexT.clear();
                    PreviewPage.this.documentSourceViewer.getControl().getDisplay().syncExec(new Runnable()
                    {
                        public void run()
                        {
                            PreviewPage.this.documentSourceViewer.setDocument(new Document());
                        }
                    });
                }
            }
            else
            {
                if (currentRequirementViewSelection != null)
                {
                    Object element = currentRequirementViewSelection.getFirstElement();
                    if ((notification.getEventType() == Notification.ADD || notification.getEventType() == Notification.MOVE) && notification.getNewValue() instanceof Requirement)
                    {
                        // Refresh when you add a new requirement to a system by drag and drop from the Upstream Requirement view
                        EObject hierarchicalTemp = ((Requirement) notification.getNewValue()).eContainer();
                        do
                        {
                            mapReqTexT.remove(hierarchicalTemp);
                            if (modeSelectionAction.isChecked())
                            {
                                forceRefresh = true;
                                handleCurrentRequirementViewSelectionChange((EObject) notification.getNewValue());
                            }
                            else
                            {
                                hierarchicalElementToForceRefresh.add((HierarchicalElement) ((Requirement) notification.getNewValue()).eContainer());
                                if(hierarchicalElementToFocusAfter != null && !((HierarchicalElement) ((Requirement) notification.getNewValue()).eContainer()).equals(hierarchicalElementToFocusAfter)){ 
                                    hierarchicalElementToForceRefresh.add(hierarchicalElementToFocusAfter);
                                }
                            }
                        hierarchicalTemp = hierarchicalTemp.eContainer();
                        }while(!(hierarchicalTemp instanceof RequirementProject));
                    }
                    else if (element instanceof EObject)
                    {
                        EObject eObject = (EObject) element;
                        handleCurrentRequirementViewSelectionChange(eObject);
                    }
                }
            }
        }
    };

    /**
     * Defines the action provided by a page to set or unset the Automatic refresh mode.
     */
    private class AutomaticRefreshAction extends Action
    {

        public AutomaticRefreshAction()
        {
            super("Automatic Refresh", Action.AS_CHECK_BOX);
            this.setImageDescriptor(RequirementCorePlugin.getImageDescriptor("icons/link_obj.gif"));
        }

        @Override
        public void run()
        {
            if (isChecked())
            {
                PreviewPage.this.refresh();
            }
        }
    }

    /**
     * Defines the action provided by a page to Refresh the generation.
     */
    protected class ManualRefreshAction extends Action
    {

        /**
         * initializes manual refresh
         */
        public ManualRefreshAction()
        {
            super("Refresh", Action.AS_PUSH_BUTTON);
            this.setImageDescriptor(RequirementCorePlugin.getImageDescriptor("icons/update.gif"));
        }

        /**
         * Executes manual refresh
         * 
         * @see org.eclipse.jface.action.Action#run()
         */
        @Override
        public void run()
        {
            if (hierarchicalElementToFocusAfter != null && PreviewPage.this.getCurrentRequirementPage() != null)
            {
                if (PreviewPage.this.getCurrentRequirementPage().getViewer().getSelection() instanceof TreeSelection)
                {
                    TreeSelection treeSelection = (TreeSelection) PreviewPage.this.getCurrentRequirementPage().getViewer().getSelection();
                    setCurrentRequirementViewSelection((TreeSelection) PreviewPage.this.getCurrentRequirementPage().getViewer().getSelection());
                    Object firstElement = treeSelection.getFirstElement();
                    if(firstElement instanceof Requirement)
                    {
                        firstElement = ((Requirement)firstElement).eContainer();
                    }
                    if (hierarchicalElementToForceRefresh.contains(firstElement))
                    {
                        PreviewPage.this.forceRefresh = true;
                        hierarchicalElementToForceRefresh.remove(firstElement);
                    }
                }
            }
            if (getCurrentRequirementViewSelection().getFirstElement() instanceof Requirement)
            {
                Requirement requirement = (Requirement) getCurrentRequirementViewSelection().getFirstElement();
                if (requirement.eContainer() instanceof HierarchicalElement)
                {
                    hierarchicalElementToFocusAfter = (HierarchicalElement) requirement.eContainer();
                }
            }
            else
            {
                hierarchicalElementToFocusAfter = null;
            }

            PreviewPage.this.refresh();

            if (PreviewPage.this.forceRefresh)
            {
                PreviewPage.this.forceRefresh = false;
            }
        }
    }

    private void refresh()
    {
        if (currentRequirementViewSelection != null)
        {
            Object element = currentRequirementViewSelection.getFirstElement();
            if (element instanceof EObject)
            {
                EObject eObject = (EObject) element;
                selectEObject(eObject);
            }
        }
    }

    /**
     * Programmatically execute refresh. Used by the Document View Filter
     */
    public void refreshFilter()
    {
        forceRefresh = true;
        this.refresh();
        forceRefresh = false;
    }

    private CurrentPage getCurrentRequirementPage()
    {
        IViewPart currentReqViewPart = PlatformUI.getWorkbench().getWorkbenchWindows()[0].getActivePage().findView(org.topcased.requirement.core.views.current.CurrentRequirementView.VIEW_ID);
        if (currentReqViewPart instanceof CurrentRequirementView)
        {
            CurrentRequirementView currentRequirementView = (CurrentRequirementView) currentReqViewPart;
            IPage ipage = currentRequirementView.getCurrentPage();
            if (ipage instanceof CurrentPage)
            {
                CurrentPage currentPage = (CurrentPage) ipage;
                return currentPage;
            }
        }
        return null;
    }

    public TreeSelection getCurrentRequirementViewSelection()
    {
        return currentRequirementViewSelection;
    }

    public void setCurrentRequirementViewSelection(TreeSelection currentRequirementViewSelection)
    {
        this.currentRequirementViewSelection = currentRequirementViewSelection;
    }

    /**
     * @see org.eclipse.ui.part.Page#init(org.eclipse.ui.part.IPageSite)
     */
    @Override
    public void init(IPageSite pageSite)
    {
        super.init(pageSite);
        try
        {
            IPath path = new Path(DOCUMENT_GENERATOR_PATH);
            File file = AcceleoModuleProvider.getDefault().getFile(path);
            if (file != null && file.exists())
            {
                SpecificScript script = new SpecificScript(file);
                script.reset();
                settings.setScript(script);
            }
            else
            {
                throw new IOException("Template not found " + DOCUMENT_GENERATOR_PATH);
            }
        }
        catch (IOException e)
        {
            RequirementCorePlugin.log(e);
        }
        catch (TemplateSyntaxExceptions e)
        {
            RequirementCorePlugin.log(e);
        }
    }

    /**
     * @see org.eclipse.ui.part.Page#setFocus()
     */
    @Override
    public void setFocus()
    {
        // do nothing
    }

    /**
     * Search parent object which can be generated for given object.
     * 
     * @param object selected
     * @return parent object which can be generated
     */
    private EObject getEvalEObject(EObject object)
    {
        EObject result = object;
        while (result != null && !settings.getScript().isGenerated(result))
        {
            result = result.eContainer();
        }
        return result;
    }

    private void doGenerate(EObject object)
    {
        this.previousSelectionObject = object;
        // Search parent object which can be generated
        EObject newEvalEObject = getEvalEObject(object);
        if (newEvalEObject == null)
        {
            // Parent object not found -> empty text
            previousSelectionObject = null;
            evalEObject = null;
            eval = null;
            documentSourceViewer.setDocument(new Document(""), problemHandler); //$NON-NLS-1$
            // applyTextPresentation();
            // problemHandler.acceptProblems(eval);
        }
        else if (evalEObject != newEvalEObject || forceRefresh)
        {
            // Parent object found and different -> generate text
            evalEObject = newEvalEObject;
            IRunnableWithProgress runnable = new IRunnableWithProgress()
            {
                public void run(IProgressMonitor monitor)
                {
                    try
                    {
                        LaunchManager mode = LaunchManager.create("run", true); //$NON-NLS-1$
                        mode.setMonitor(monitor);
                        eval = evaluate(evalEObject, mode);
                    }
                    catch (ENodeException e)
                    {
                        eval = null;
                    }
                    catch (FactoryException e)
                    {
                        eval = null;
                    }
                }
            };
            try
            {

                PlatformUI.getWorkbench().getProgressService().run(true, true, runnable);
            }
            catch (InvocationTargetException e)
            {
                AcceleoEcoreGenUiPlugin.getDefault().log(e, true);
                evalEObject = null;
                eval = null;
            }
            catch (InterruptedException e)
            {
                AcceleoEcoreGenUiPlugin.getDefault().log(AcceleoGenUIMessages.getString("AcceleoSourceEditor.EvaluationInterrupted"), true); //$NON-NLS-1$
                evalEObject = null;
                eval = null;
            }
            if (eval != null)
            {
                String text = eval.asString();

                documentSourceViewer.setDocument(new Document(text), new ProjectionAnnotationModel(), -1, -1);

                updateFoldingStructure(findAnnotations(evalEObject));

                ruler.setModel(documentSourceViewer.getProjectionAnnotationModel());

                // applyTextPresentation();
            }
        }
    }

    private Int2 findPositionInSelection(Int2[] positions, Int2 currentPos)
    {
        Int2 result = currentPos;

        if (documentSourceViewer.getSelection() != null)
        {        
            TextSelection selection = (TextSelection) documentSourceViewer.getSelection();
            for (int i = 0; i < positions.length && result == null; i++)
            {
                if (positions[i].b() >= selection.getOffset() + selection.getLength())
                {
                    result = positions[i];
                }
            }
        }
        return result;
    }

    private Int2 findPositionEnclosingSelection(Int2[] positions, int enclosedSelection)
    {
        Int2 pos = null;
        for (int i = 0; i < positions.length && pos == null; i++)
        {
            if (positions[i].b() <= enclosedSelection && positions[i].e() > enclosedSelection)
            {
                pos = positions[i];
            }
        }
        return pos;
    }

    private void doHighlightSelectedObjectGeneration(Int2[] positions, boolean sameObject)
    {
        Int2 pos = null;
        if (updateSelection > -1)
        {
            pos = findPositionEnclosingSelection(positions, updateSelection);
        }
        else
        {
            if (sameObject)
            {
                pos = findPositionInSelection(positions, pos);
            }
        }
        if (pos == null)
        {
            pos = positions[0];
        }
        ISourceViewer viewer = documentSourceViewer;
        StyledText widget = viewer.getTextWidget();
        widget.setRedraw(false);
        documentSourceViewer.setSelectedRange(pos.b(), pos.e() - pos.b());
        
        selectAndReveal(pos.b(), pos.e() - pos.b());
        widget.setRedraw(true);
    }

    /**
     * An object is selected in the model. Search parent object which can be generated. The editor contains the text
     * generated for parent object. If it isn't found, editor becomes empty. Update error markers and highlight selected
     * range.
     * 
     * @param object selected
     */
    private void selectEObject(EObject object)
    {
        boolean sameObject = true;
        if (object != this.previousSelectionObject || forceRefresh)
        {
            sameObject = false;
            doGenerate(object);

        }
        Int2[] positions = getPositions(object);
        if (positions.length > 0)
        {
            doHighlightSelectedObjectGeneration(positions, sameObject);
        }
        else
        {
            TextSelection selection = (TextSelection) documentSourceViewer.getSelection();
            if (selection != null)
            {
                selectAndReveal(selection.getOffset(), 0);
            }
            else
            {
                selectAndReveal(0, 0);
            }
        }
    }

    private void selectAndReveal(int start, int length)
    {
        documentSourceViewer.getTextWidget().setSelectionForeground(SELECTED_TEXT_FOREGROUND);
        documentSourceViewer.getTextWidget().setSelectionBackground(SELECTED_TEXT_BACKGROUND);
        documentSourceViewer.revealRange(start, length);
    }

    // /**
    // * Apply presentation settings.
    // */
    // private void applyTextPresentation()
    // {
    // if (eval != null && eval.getTextModelMapping() != null)
    // {
    // Int2[] pos = eval.getTextModelMapping().getHighlightedPos(TextModelMapping.HIGHLIGHTED_STATIC_TEXT);
    // for (int i = 0; i < pos.length; i++)
    // {
    // documentSourceViewer.setTextColor(STATIC_TEXT_FOREGROUND, pos[i].b(), pos[i].e() - pos[i].b(), false);
    // }
    // pos = eval.getTextModelMapping().getHighlightedPos(TextModelMapping.HIGHLIGHTED_COMMENT);
    // for (int i = 0; i < pos.length; i++)
    // {
    // documentSourceViewer.setTextColor(COMMENT_FOREGROUND, pos[i].b(), pos[i].e() - pos[i].b(), false);
    // }
    // documentSourceViewer.getTextWidget().redraw();
    // }
    // }

    /**
     * Generate text for given object.
     * 
     * @param inputObject is an object which can be generated
     * @param mode is the mode
     * @return a node of generation
     * @throws FactoryException
     * @throws ENodeException
     */
    private ENode evaluate(EObject inputObject, LaunchManager mode) throws FactoryException, ENodeException
    {
        ENode evaluation = mapReqTexT.get(inputObject);
//        if (inputObject instanceof HierarchicalElement)
//        {
//            HierarchicalElement elt = (HierarchicalElement) inputObject;
//            for (Requirement req : elt.getRequirement())
//            {
//                
//            }
//        }
        if (evaluation == null)
        {
            Template template = settings.getScript().getRootTemplate(inputObject, true);
            if (template != null)
            {
                boolean withComment = settings.getScript().isDefault() || !settings.getScript().hasFileTemplate();
                if (withComment)
                {
                    evaluation = template.evaluateWithComment(inputObject, mode);
                }
                else
                {
                    evaluation = template.evaluate(inputObject, mode);
                }
                mapReqTexT.put(inputObject, evaluation);
            }
            else
            {
                evaluation = new ENode(ENode.EMPTY, inputObject, Template.EMPTY, true);
            }
        }
        return evaluation;
    }

    /**
     * Returns the positions of the given object
     * 
     * @param object is the model object
     * @return the positions
     */
    private Int2[] getPositions(EObject object)
    {
        if (eval != null && eval.getTextModelMapping() != null)
        {
            Int2[] result = eval.getTextModelMapping().eObject2Positions(object);
            EObject cursor = object;
            while (cursor != null && result.length == 0)
            {
                cursor = cursor.eContainer();
                result = eval.getTextModelMapping().eObject2Positions(cursor);
            }
            return result;
        }
        else
        {
            return new Int2[] {};
        }
    }

    private void fireSelectionChanged(SelectionChangedEvent event)
    {
        for (ISelectionChangedListener listener : listeners)
        {
            listener.selectionChanged(event);
        }
    }

    private void handleCurrentRequirementViewSelectionChange(final EObject selectionObject)
    {
        if (selectionObject instanceof Requirement && ((Requirement) selectionObject) instanceof HierarchicalElement)
        {
            hierarchicalElementToFocusAfter = (HierarchicalElement) ((Requirement) selectionObject).eContainer();
        }
        if (selectionObject instanceof HierarchicalElement && ((HierarchicalElement) selectionObject).eContainer() instanceof RequirementProject )
        {
            hierarchicalElementToFocusAfter = (HierarchicalElement) selectionObject;
        }
        if (selectionObject instanceof Requirement && ((Requirement) selectionObject).eContainer() instanceof HierarchicalElement)
        {
            hierarchicalElementToFocusAfter = (HierarchicalElement) ((Requirement) selectionObject).eContainer();
        }
        if (modeSelectionAction.isChecked())
        {
            Display display = getControl().getDisplay();
            display.asyncExec(new Runnable()
            {
                public void run()
                {
                    if (selectionObject instanceof Requirement)
                    {
                        Requirement requirement = (Requirement) selectionObject;
                        EObject currentChapter = getCurrentChapter(requirement);

                        if (currentChapter == evalEObject)
                        {
                            selectEObject(requirement);
                        }
                        else
                        {
                            selectEObject(currentChapter);
                            selectEObject(requirement);
                        }
                    }
                    else
                    {
                        selectEObject(selectionObject);
                    }
                }
            });
        }
    }

    private EObject getCurrentChapter(Requirement requirement)
    {
        EObject result = requirement;
        while (result.eContainer() != null)
        {
            if (result instanceof HierarchicalElement)
            {
                HierarchicalElement hierarchicalElement = (HierarchicalElement) result;
                EObject samElement = hierarchicalElement.getElement();
                if (samElement instanceof org.topcased.sam.System || samElement instanceof org.topcased.sam.DataStorage)
                {
                    return hierarchicalElement;
                }
            }
            else
            {
                if (result instanceof SpecialChapter)
                {
                    return result;
                }
            }
            result = result.eContainer();
        }
        return result;
    }

    /**
     * Select the object at the given position.
     * 
     * @param pos is the position in the text
     */
    private void updateSelection(int pos)
    {
        updateSelection = pos;
        try
        {
            if (eval != null && pos > -1)
            {
                // EObject
                if (eval.getTextModelMapping() != null)
                {
                    EObject object = eval.getTextModelMapping().index2EObject(pos);
                    if (object != null)
                    {
                        fireSelectionChanged(new SelectionChangedEvent(this, new StructuredSelection(object)));
                    }
                }
            }
        }
        finally
        {
            updateSelection = -1;
        }
    }

    /**
     * @see org.eclipse.ui.part.Page#createControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createControl(Composite parent)
    {
        documentSourceViewer = new ProjectionViewer(parent, ruler, null, true, SWT.V_SCROLL | SWT.H_SCROLL | SWT.WRAP);
        documentSourceViewer.setDocument(new Document(), new ProjectionAnnotationModel(), -1, -1);

        ruler.getControl().addMouseListener(fMouseListener);

        projectionSupport = new ProjectionSupport(documentSourceViewer, null, EditorsUI.getSharedTextColors());
        projectionSupport.install();

        // turn projection mode on
        documentSourceViewer.doOperation(ProjectionViewer.TOGGLE);

        documentSourceViewer.setEditable(false);

        SourceViewerConfiguration configuration = new SourceViewerConfiguration()
        {

            @Override
            public ITextDoubleClickStrategy getDoubleClickStrategy(ISourceViewer sourceViewer, String contentType)
            {
                return new ITextDoubleClickStrategy()
                {

                    public void doubleClicked(ITextViewer viewer)
                    {
                        int offset = viewer.getSelectedRange().x;
                        updateSelection(offset);
                    }

                };
            }
        };

        documentSourceViewer.configure(configuration);

        IPageSite site = getSite();
        updateSelectionProvider(site);
        IActionBars actionBars = site.getActionBars();
        registerToolBarActions(actionBars);
        registerModelListener();

    }

    private void registerModelListener()
    {
        AdapterFactory adapterFactory = RequirementUtils.getAdapterFactory();
        if (adapterFactory instanceof ComposedAdapterFactory)
        {
            ComposedAdapterFactory composedAdapterFactory = (ComposedAdapterFactory) adapterFactory;
            composedAdapterFactory.addListener(notifyChangedListener);
        }
    }

    private void removeModelListener()
    {
        AdapterFactory adapterFactory = RequirementUtils.getAdapterFactory();
        if (adapterFactory instanceof ComposedAdapterFactory)
        {
            ComposedAdapterFactory composedAdapterFactory = (ComposedAdapterFactory) adapterFactory;
            composedAdapterFactory.removeListener(notifyChangedListener);
        }
    }

    private void registerToolBarActions(IActionBars actionBars)
    {
        IToolBarManager toolBarManager = actionBars.getToolBarManager();
        modeSelectionAction = new AutomaticRefreshAction();
        toolBarManager.add(modeSelectionAction);
        toolBarManager.add(new ManualRefreshAction());
    }

    /**
     * @see org.eclipse.ui.part.Page#getControl()
     */
    @Override
    public Control getControl()
    {
        return documentSourceViewer.getControl();
    }

    /**
     * @see org.eclipse.jface.viewers.ISelectionProvider#addSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
     */
    public void addSelectionChangedListener(ISelectionChangedListener listener)
    {
        listeners.add(listener);
    }

    /**
     * @see org.eclipse.jface.viewers.ISelectionProvider#getSelection()
     */
    public ISelection getSelection()
    {
        if (documentSourceViewer == null)
        {
            return TextSelection.emptySelection();
        }
        return documentSourceViewer.getSelection();
    }

    /**
     * @see org.eclipse.jface.viewers.ISelectionProvider#removeSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
     */
    public void removeSelectionChangedListener(ISelectionChangedListener listener)
    {
        listeners.remove(listener);
    }

    /**
     * @see org.eclipse.ui.part.Page#dispose()
     */
    @Override
    public void dispose()
    {
        removeModelListener();
        super.dispose();
    }

    /**
     * @see org.eclipse.jface.viewers.ISelectionProvider#setSelection(org.eclipse.jface.viewers.ISelection)
     */
    public void setSelection(ISelection selection)
    {
        if (selection instanceof TreeSelection)
        {
            TreeSelection treeSelection = (TreeSelection) selection;
            currentRequirementViewSelection = treeSelection;
            Object element = treeSelection.getFirstElement();
            if (element instanceof EObject)
            {
                EObject eObject = (EObject) element;
                handleCurrentRequirementViewSelectionChange(eObject);
            }
        }
    }

    private void updateSelectionProvider(IPageSite site)
    {
        ISelectionProvider provider = this;
        site.setSelectionProvider(provider);
    }

    private void updateFoldingStructure(ArrayList<Position> positions)
    {
        HashMap<ProjectionAnnotation, Position> newAnnotations = new HashMap<ProjectionAnnotation, Position>();

        Position beginHidePosition = null;
        Position endHidePosition = null;

        for (int i = 0; i < positions.size(); i++)
        {
            if (hiddenAnnotations.contains(positions.get(i)))
            {
                if (beginHidePosition == null)
                {
                    beginHidePosition = positions.get(i);
                    endHidePosition = positions.get(i);
                }
                else
                {
                    endHidePosition = positions.get(i);
                }
            }
            else
            {
                if (endHidePosition != null)
                {
                    PreviewPageProjectionAnnotation hiddenAnnotation = new PreviewPageProjectionAnnotation(true);
                    newAnnotations.put(hiddenAnnotation, new Position(beginHidePosition.getOffset(), endHidePosition.getOffset() + endHidePosition.getLength() - beginHidePosition.getOffset()));
                    beginHidePosition = null;
                    endHidePosition = null;
                }
                ProjectionAnnotation annotation = new ProjectionAnnotation();
                newAnnotations.put(annotation, positions.get(i));
            }
        }
        if (endHidePosition != null)
        {
            PreviewPageProjectionAnnotation hiddenAnnotation = new PreviewPageProjectionAnnotation(true);
            newAnnotations.put(hiddenAnnotation, new Position(beginHidePosition.getOffset(), endHidePosition.getOffset() + endHidePosition.getLength() - beginHidePosition.getOffset() - 2));
        }
        if (documentSourceViewer != null)
        {
            if (documentSourceViewer.getProjectionAnnotationModel() == null)
            {
                documentSourceViewer.enableProjection();
            }
            documentSourceViewer.getProjectionAnnotationModel().modifyAnnotations(null, newAnnotations, null);
        }
    }

    private ArrayList<Position> findAnnotations(EObject currentEObject)
    {
        ArrayList<Position> annotations = new ArrayList<Position>();

        hiddenAnnotations = new ArrayList<Position>();

        TreeIterator<EObject> treeIterator = currentEObject.eAllContents();
        while (treeIterator.hasNext())
        {
            EObject current = treeIterator.next();
            if (current instanceof CurrentRequirement)
            {
                CurrentRequirement currentReq = (CurrentRequirement) current;
                Int2[] pos = getPositions(currentReq);
                for (int i = 0; i < pos.length; i++)
                {
                    annotations.add(new Position(pos[i].b(), pos[i].e() - pos[i].b()));
                }
            }
            else if (current instanceof AnonymousRequirement)
            {
                AnonymousRequirement anonyReq = (AnonymousRequirement) current;
                Int2[] pos = getPositions(anonyReq);
                for (int i = 0; i < pos.length; i++)
                {
                    annotations.add(new Position(pos[i].b(), pos[i].e() - pos[i].b()));
                }
            }

        }

        return annotations;
    }

    private IAnnotationModel getModel()
    {
        return documentSourceViewer.getProjectionAnnotationModel();
    }

    private ProjectionAnnotation findAnnotation(int line, boolean exact)
    {

        ProjectionAnnotation previousAnnotation = null;

        IAnnotationModel model = getModel();
        if (model != null)
        {
            IDocument document = documentSourceViewer.getDocument();

            int previousDistance = Integer.MAX_VALUE;

            Iterator e = model.getAnnotationIterator();
            while (e.hasNext())
            {
                Object next = e.next();
                if (next instanceof ProjectionAnnotation)
                {
                    ProjectionAnnotation annotation = (ProjectionAnnotation) next;
                    Position p = model.getPosition(annotation);
                    if (p == null)
                    {
                        continue;
                    }
                    int distance = getDistance(annotation, p, document, line);
                    if (distance == -1)
                    {
                        continue;
                    }
                    if (!exact)
                    {
                        if (distance < previousDistance)
                        {
                            previousAnnotation = annotation;
                            previousDistance = distance;
                        }
                    }
                    else if (distance == 0)
                    {
                        previousAnnotation = annotation;
                    }
                }
            }
        }

        return previousAnnotation;
    }

    private int getDistance(ProjectionAnnotation annotation, Position position, IDocument document, int line)
    {
        if (position.getOffset() > -1 && position.getLength() > -1)
        {
            try
            {
                int startLine = document.getLineOfOffset(position.getOffset());
                int endLine = document.getLineOfOffset(position.getOffset() + position.getLength());
                if (startLine <= line && line < endLine)
                {
                    if (annotation.isCollapsed())
                    {
                        int captionOffset;
                        if (position instanceof IProjectionPosition)
                        {
                            captionOffset = ((IProjectionPosition) position).computeCaptionOffset(document);
                        }
                        else
                        {
                            captionOffset = 0;
                        }

                        int captionLine = document.getLineOfOffset(position.getOffset() + captionOffset);
                        if (startLine <= captionLine && captionLine < endLine)
                        {
                            return Math.abs(line - captionLine);
                        }
                    }
                    return line - startLine;
                }
            }
            catch (BadLocationException x)
            {
                RequirementCorePlugin.log(x);
            }
        }
        return -1;
    }

}
