/*****************************************************************************
 * Copyright (c) 2016 CEA LIST.
 *
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Shuai Li (CEA LIST) <shuai.li@cea.fr> - Initial API and implementation
 *
 *****************************************************************************/

package org.eclipse.papyrus.designer.languages.cpp.reverse.ui.handler;

import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.model.ICProject;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.papyrus.designer.languages.cpp.reverse.reverse.ReverseCpp2Uml;
import org.eclipse.papyrus.designer.languages.cpp.reverse.ui.Activator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * Handler for the "Reverse to new model" command
 *
 */
public class ReverseToNewUmlModel extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			ISelection selection = HandlerUtil.getCurrentSelection(event);

			if (selection instanceof ITextSelection) {
				IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();

				if (editorPart instanceof AbstractTextEditor) {
					ITextEditor textEditor = (ITextEditor) editorPart;

					if (textEditor != null) {
						IFile resource = (IFile) textEditor.getEditorInput().getAdapter(IResource.class);

						if (resource != null) {
							scheduleReverse(resource.getProject());
							return Status.OK_STATUS;
						}
					}
				}
			}

			if (selection instanceof IStructuredSelection) {
				Object selectedObject = ((IStructuredSelection) selection).getFirstElement();
				if (selectedObject instanceof IResource) {
					scheduleReverse(((IResource) selectedObject).getProject());
					return Status.OK_STATUS;
				}
			}


		} catch (Exception e) {
			Activator.log.error(e);
		}

		return null;
	}

	private void scheduleReverse(final IProject project) {
		if (project != null) {
			final ICProject cProject = CoreModel.getDefault().create(project);
			Job job = new Job("Reversing project " + cProject.getElementName() + " to new UML model") { // $NON-NLS-1$

				@Override
				protected IStatus run(final IProgressMonitor monitor) {
					try {
						ReverseCpp2Uml reverser = null;
						reverser = new ReverseCpp2Uml(cProject, monitor);
						try {
							reverser.reverseProject(true);
						} catch (Exception e) {
							Activator.log.error(e);
						}
					} catch (Exception e) {
						Activator.log.error(e);
					}

					return Status.OK_STATUS;
				}

			};

			job.setUser(true);
			
			job.addJobChangeListener(new JobChangeAdapter() {
				@Override
				public void done(IJobChangeEvent event) {
					Display.getDefault().syncExec(new Runnable() {
						@Override
						public void run() {
							try {
								Shell shell = Display.getDefault().getActiveShell();
								if (shell != null) {
									MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
									messageBox.setMessage("Reversed to new UML model in reversed_models/ folder of project " + project.getName()); // $NON-NLS-1$
									messageBox.open();
								}
							} catch (Exception e) {
								Activator.log.error(e);
							}
							
						}
					});
				}
			});
			
			job.schedule();
		}
	}

}
