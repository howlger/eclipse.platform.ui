/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.tests.menus;

import org.eclipse.core.resources.*;
import org.eclipse.core.resources.mapping.*;
import org.eclipse.core.runtime.*;
import org.eclipse.ui.IContributorResourceAdapter;
import org.eclipse.ui.IContributorResourceAdapter2;

public class ObjectContributionClasses implements IAdapterFactory {
	
	public static final String PROJECT_NAME = "testContributorResourceAdapter";
	
	public static interface ICommon {
	}
	
	public static class Common implements ICommon {		
	}
	
	public static interface IA {
	}
	
	public static class A implements IA {	
	}
	
	public static class A1 extends A {
	}
	
	public static class A11 extends A1 {
	}
	
	public static interface IB {
	}
	
	public static class B implements IB {
	}
	
	public static class B2 implements IB {
	}
	
	public static class D extends Common implements IA {
	}
	
	public static class C implements ICommon {
	}
	
	public static class CResource implements IAdaptable {
		public Object getAdapter(Class adapter) {
			if(adapter == IContributorResourceAdapter.class) {
				return new ResourceAdapter();
			}			
			return null;
		}		
	}
	
	public static class CFile implements IAdaptable {
		public Object getAdapter(Class adapter) {
			if(adapter == IContributorResourceAdapter.class) {
				return new ResourceAdapter();
			}			
			return null;
		}		
	}
    
    public interface IModelElement {
    }
	
    public static class ModelElement extends PlatformObject implements IModelElement {
    }
    
	// Default contributor adapter
	
	public static class ResourceAdapter implements IContributorResourceAdapter2 {
		public IResource getAdaptedResource(IAdaptable adaptable) {
			if(adaptable instanceof CResource) {
				return ResourcesPlugin.getWorkspace().getRoot();
			}
			if(adaptable instanceof CFile) {
				return ResourcesPlugin.getWorkspace().getRoot().getProject(PROJECT_NAME).getFile("dummy");
			}
			return null;
		}
        public ResourceMapping getAdaptedResourceMapping(IAdaptable adaptable) {
            return (ResourceMapping)getAdaptedResource(adaptable).getAdapter(ResourceMapping.class);
        }	
	}
	
	// Adapter methods
	
	public Object getAdapter(final Object adaptableObject, Class adapterType) {
		if(adapterType == IContributorResourceAdapter.class) {
			return new ResourceAdapter();
		}
		if(adaptableObject instanceof IA && adapterType == IA.class) {
			return new A();
		}
		if(adapterType == IResource.class) {
			return ResourcesPlugin.getWorkspace().getRoot();
		}
		if(adapterType == ICommon.class) {
			return new Common();
		}
        if(adapterType == ResourceMapping.class) {
            return new ResourceMapping() {    
                public ResourceTraversal[] getTraversals(ResourceMappingContext context, IProgressMonitor monitor) {
                    return new ResourceTraversal[] {
                            new ResourceTraversal(new IResource[] {ResourcesPlugin.getWorkspace().getRoot()}, IResource.DEPTH_INFINITE, IResource.NONE)
                    };
                }
                public IProject[] getProjects() {
                    return ResourcesPlugin.getWorkspace().getRoot().getProjects();
                }
                public Object getModelObject() {
                    return adaptableObject;
                }
            };
        }
        
		return null;
	}

	public Class[] getAdapterList() {
		return new Class[] { ICommon.class, IResource.class, IFile.class, IContributorResourceAdapter.class, ResourceMapping.class};
	}
}
