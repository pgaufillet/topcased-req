/*******************************************************************************
 * Copyright (c) 2010, 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *     Matthieu Boivineau (Atos) - custom operations
 *******************************************************************************/
package org.topcased.requirement.current2upstream.files;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.m2m.atl.common.ATLExecutionException;
import org.eclipse.m2m.atl.core.ATLCoreException;
import org.eclipse.m2m.atl.core.IExtractor;
import org.eclipse.m2m.atl.core.IInjector;
import org.eclipse.m2m.atl.core.IModel;
import org.eclipse.m2m.atl.core.IReferenceModel;
import org.eclipse.m2m.atl.core.ModelFactory;
import org.eclipse.m2m.atl.core.emf.EMFExtractor;
import org.eclipse.m2m.atl.core.emf.EMFInjector;
import org.eclipse.m2m.atl.core.emf.EMFModel;
import org.eclipse.m2m.atl.core.emf.EMFModelFactory;
import org.eclipse.m2m.atl.core.launch.ILauncher;
import org.eclipse.m2m.atl.engine.emfvm.launch.EMFVMLauncher;
import org.topcased.requirement.UpstreamModel;
import org.topcased.requirement.current2upstream.RequirementCurrent2UpstreamPlugin;

/**
 * Entry point of the 'Cur2Up' transformation module.
 */
public class Cur2Up {

	/**
	 * The property file. Stores module list, the metamodel and library locations.
	 * @generated
	 */
	private Properties properties;
	
	/**
	 * The IN model.
	 * @generated
	 */
	protected IModel inModel;	
	
	/**
	 * The OUT model.
	 * @generated
	 */
	protected IModel outModel;	
		
	/**
	 * The main method.
	 * 
	 * @param args
	 *            are the arguments
	 * @generated
	 */
	public static void main(String[] args) {
		try {
			if (args.length < 2) {
				System.out.println("Arguments not valid : {IN_model_path, OUT_model_path}.");
			} else {
				Cur2Up runner = new Cur2Up();
				runner.loadModels(args[0]);
				runner.doCur2Up(new NullProgressMonitor());
				runner.saveModels(args[1]);
			}
		} catch (ATLCoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ATLExecutionException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Constructor.
	 *
	 * @generated
	 */
	public Cur2Up() throws IOException {
		properties = new Properties();
		InputStream propertiesInputStream = getFileURL("Cur2Up.properties").openStream();
		properties.load(propertiesInputStream);
		propertiesInputStream.close();
		EPackage.Registry.INSTANCE.put(getMetamodelUri("RequirementModel"), org.topcased.requirement.RequirementPackage.eINSTANCE);
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
	}
	
	/**
	 * Transforms current requirements into upstream requirements 
	 * @param inputURI the IN model URI
	 * @param outputURI the OUT model URI
	 * @throws ATLCoreException if a problem occurs while loading models
	 * @throws IOException if a module cannot be read
	 * @throws ATLExecutionException if an error occurs during the execution
	 * @return true if the transformation was successful, else false
	 */
	public boolean transform(URI inputURI, URI outputURI){
		// Load the input and input/output models, initialize output models.
		ModelFactory factory = new EMFModelFactory();
		IInjector injector = new EMFInjector();
	 	IReferenceModel requirementmodelMetamodel;
		try {
			requirementmodelMetamodel = factory.newReferenceModel();
			injector.inject(requirementmodelMetamodel, getMetamodelUri("RequirementModel"));
			this.inModel = factory.newModel(requirementmodelMetamodel);
			ResourceSet rSet = new ResourceSetImpl();
			Resource res = rSet.getResource(inputURI, true);
			((EMFInjector)injector).inject(inModel, res);
			this.outModel = factory.newModel(requirementmodelMetamodel);
			
			// Transformation from current to upstream
			doCur2Up(new NullProgressMonitor());
			
			// The new file is created
			IExtractor extractor = new EMFExtractor();
			extractor.extract(outModel, outputURI.toString());
		} catch (ATLCoreException e) {
			return false;
		} catch (ATLExecutionException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * Transforms current requirements into upstream requirements 
	 * @param inputResource the IN model resource
	 * @return the RequirementProject object containing the upstream requirements. Return null if the transformation fails.
	 * @throws ATLCoreException if a problem occurs while loading models
	 * @throws IOException if a module cannot be read
	 * @throws ATLExecutionException if an error occurs during the execution
	 * @return the requirementProject if the transformation was successful, else null
	 */
	public UpstreamModel transform(Resource inputResource){
		// Load the input and input/output models, initialize output models.
		ModelFactory factory = new EMFModelFactory();
		IInjector injector = new EMFInjector();
	 	IReferenceModel requirementmodelMetamodel;
		try {
			requirementmodelMetamodel = factory.newReferenceModel();
			injector.inject(requirementmodelMetamodel, getMetamodelUri("RequirementModel"));
			this.inModel = factory.newModel(requirementmodelMetamodel);
			((EMFInjector)injector).inject(inModel, inputResource);
			this.outModel = factory.newModel(requirementmodelMetamodel);
			
			// Transformation from current to upstream
			doCur2Up(new NullProgressMonitor());
			
			// Returns the RequirementProject from the model
			if(		((EMFModel)outModel).getResource() != null
				&&  ((EMFModel)outModel).getResource().getContents() !=null
				&&  ((EMFModel)outModel).getResource().getContents().size() > 0
				&&  ((EMFModel)outModel).getResource().getContents().get(0) instanceof UpstreamModel){
				return (UpstreamModel) ((EMFModel)outModel).getResource().getContents().get(0);
			}
		} catch (ATLCoreException e) {
			RequirementCurrent2UpstreamPlugin.log("ATL Transformation error", IStatus.WARNING, e);
			return null;
		} catch (ATLExecutionException e) {
			RequirementCurrent2UpstreamPlugin.log("ATL Execution error", IStatus.WARNING, e);
			return null;
		} catch (IOException e) {
			return null;
		}
		return null;
	}
	
	
	/**
	 * Load the input and input/output models, initialize output models.
	 * 
	 * @param inModelPath
	 *            the IN model path
	 * @throws ATLCoreException
	 *             if a problem occurs while loading models
	 *
	 * @generated
	 */
	public void loadModels(String inModelPath) throws ATLCoreException {
		ModelFactory factory = new EMFModelFactory();
		IInjector injector = new EMFInjector();
	 	IReferenceModel requirementmodelMetamodel = factory.newReferenceModel();
		injector.inject(requirementmodelMetamodel, getMetamodelUri("RequirementModel"));
		this.inModel = factory.newModel(requirementmodelMetamodel);
		injector.inject(inModel, inModelPath);
		this.outModel = factory.newModel(requirementmodelMetamodel);
	}
	
	/**
	 * Save the output and input/output models.
	 * 
	 * @param outModelPath
	 *            the OUT model path
	 * @throws ATLCoreException
	 *             if a problem occurs while saving models
	 *
	 * @generated
	 */
	public void saveModels(String outModelPath) throws ATLCoreException {
		IExtractor extractor = new EMFExtractor();
		extractor.extract(outModel, outModelPath);
	}

	/**
	 * Transform the models.
	 * 
	 * @param monitor
	 *            the progress monitor
	 * @throws ATLCoreException
	 *             if an error occurs during models handling
	 * @throws IOException
	 *             if a module cannot be read
	 * @throws ATLExecutionException
	 *             if an error occurs during the execution
	 *
	 * @generated
	 */
	public Object doCur2Up(IProgressMonitor monitor) throws ATLCoreException, IOException, ATLExecutionException {
		ILauncher launcher = new EMFVMLauncher();
		List<InputStream> inputStreamsToClose = new ArrayList<InputStream>();
		Map<String, Object> launcherOptions = getOptions();
		launcher.initialize(launcherOptions);
		launcher.addInModel(inModel, "IN", "RequirementModel");
		launcher.addOutModel(outModel, "OUT", "RequirementModel");
		InputStream[] modulesStreams = getModulesList();
		inputStreamsToClose.addAll(Arrays.asList(modulesStreams));
		Object result = launcher.launch("run", monitor, launcherOptions, (Object[]) modulesStreams);
		for (InputStream inputStream : inputStreamsToClose) {
			inputStream.close();
		}

		// filter root elements that are not UpstreamModel
		Iterator<EObject> rootElems = ((EMFModel)outModel).getResource().getContents().iterator();
		while(rootElems.hasNext()) {
			EObject next = rootElems.next();
			if (!(next instanceof UpstreamModel)) {
				rootElems.remove();
			}
		}

		return result;
	}
	
	/**
	 * Returns an Array of the module input streams, parameterized by the
	 * property file.
	 * 
	 * @return an Array of the module input streams
	 * @throws IOException
	 *             if a module cannot be read
	 *
	 * @generated
	 */
	protected InputStream[] getModulesList() throws IOException {
		InputStream[] modules = null;
		String modulesList = properties.getProperty("Cur2Up.modules");
		if (modulesList != null) {
			String[] moduleNames = modulesList.split(",");
			modules = new InputStream[moduleNames.length];
			for (int i = 0; i < moduleNames.length; i++) {
				String asmModulePath = new Path(moduleNames[i].trim()).removeFileExtension().addFileExtension("asm").toString();
				modules[i] = getFileURL(asmModulePath).openStream();
			}
		}
		return modules;
	}
	
	/**
	 * Returns the URI of the given metamodel, parameterized from the property file.
	 * 
	 * @param metamodelName
	 *            the metamodel name
	 * @return the metamodel URI
	 *
	 * @generated
	 */
	protected String getMetamodelUri(String metamodelName) {
		return properties.getProperty("Cur2Up.metamodels." + metamodelName);
	}
	
	/**
	 * Returns the file name of the given library, parameterized from the property file.
	 * 
	 * @param libraryName
	 *            the library name
	 * @return the library file name
	 *
	 * @generated
	 */
	protected InputStream getLibraryAsStream(String libraryName) throws IOException {
		return getFileURL(properties.getProperty("Cur2Up.libraries." + libraryName)).openStream();
	}
	
	/**
	 * Returns the options map, parameterized from the property file.
	 * 
	 * @return the options map
	 *
	 * @generated
	 */
	protected Map<String, Object> getOptions() {
		Map<String, Object> options = new HashMap<String, Object>();
		for (Entry<Object, Object> entry : properties.entrySet()) {
			if (entry.getKey().toString().startsWith("Cur2Up.options.")) {
				options.put(entry.getKey().toString().replaceFirst("Cur2Up.options.", ""), 
				entry.getValue().toString());
			}
		}
		return options;
	}
	
	/**
	 * Finds the file in the plug-in. Returns the file URL.
	 * 
	 * @param fileName
	 *            the file name
	 * @return the file URL
	 * @throws IOException
	 *             if the file doesn't exist
	 * 
	 * @generated
	 */
	protected static URL getFileURL(String fileName) throws IOException {
		final URL fileURL;
		if (isEclipseRunning()) {
			URL resourceURL = Cur2Up.class.getResource(fileName);
			if (resourceURL != null) {
				fileURL = FileLocator.toFileURL(resourceURL);
			} else {
				fileURL = null;
			}
		} else {
			fileURL = Cur2Up.class.getResource(fileName);
		}
		if (fileURL == null) {
			throw new IOException("'" + fileName + "' not found");
		} else {
			return fileURL;
		}
	}

	/**
	 * Tests if eclipse is running.
	 * 
	 * @return <code>true</code> if eclipse is running
	 *
	 * @generated
	 */
	public static boolean isEclipseRunning() {
		try {
			return Platform.isRunning();
		} catch (Throwable exception) {
			// Assume that we aren't running.
		}
		return false;
	}
}
