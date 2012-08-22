package se.ya2o.targetderivator.actions;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

/**
 * Our sample action implements workbench action delegate.
 * The action proxy will be created by the workbench and
 * shown in the UI. When the user tries to use the action,
 * this delegate will be created and execution will be
 * delegated to it.
 * 
 * @see IWorkbenchWindowActionDelegate
 */
public class TargetDerivatorAction implements IWorkbenchWindowActionDelegate {
    private static final String TARGET = "target";
    private IWorkbenchWindow window;

    /**
     * The constructor.
     */
    public TargetDerivatorAction() {}

    /**
     * The action has been activated. The argument of the
     * method represents the 'real' action sitting
     * in the workbench UI.
     * 
     * @see IWorkbenchWindowActionDelegate#run
     */
    @Override
    public void run(IAction action) {
        //        MessageDialog.openInformation(
        //                window.getShell(),
        //                "Targetderivator",
        //                "Marking target folders as derivated resources...");

        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        List<IProject> projects = Arrays.asList(workspace.getRoot().getProjects());

        IProgressMonitor monitor = new IProgressMonitor() {

            @Override
            public void beginTask(String arg0, int arg1) {}

            @Override
            public void done() {}

            @Override
            public void internalWorked(double arg0) {}

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public void setCanceled(boolean arg0) {}

            @Override
            public void setTaskName(String arg0) {}

            @Override
            public void subTask(String arg0) {}

            @Override
            public void worked(int arg0) {}
        };

        for (IProject project : projects) {
            if (project.isOpen()) {
                try {
                    markAllTargetFoldersDerived(project.members(), monitor);
                } catch (CoreException e) {}
            }
        }

    }

    private void markAllTargetFoldersDerived(IResource[] resources, IProgressMonitor monitor) throws CoreException {
        for (IResource resource : resources) {
            if (resource instanceof IFolder) {
                IFolder folder = (IFolder) resource;
                if (TARGET.equals(folder.getName())) {
                    folder.setDerived(true, monitor);
                } else {
                    markAllTargetFoldersDerived(folder.members(), monitor);
                }
            }
        }
    }

    /**
     * Selection in the workbench has been changed. We
     * can change the state of the 'real' action here
     * if we want, but this can only happen after
     * the delegate has been created.
     * 
     * @see IWorkbenchWindowActionDelegate#selectionChanged
     */
    @Override
    public void selectionChanged(IAction action, ISelection selection) {}

    /**
     * We can use this method to dispose of any system
     * resources we previously allocated.
     * 
     * @see IWorkbenchWindowActionDelegate#dispose
     */
    @Override
    public void dispose() {}

    /**
     * We will cache window object in order to
     * be able to provide parent shell for the message dialog.
     * 
     * @see IWorkbenchWindowActionDelegate#init
     */
    @Override
    public void init(IWorkbenchWindow window) {
        this.window = window;
    }
}
