/**
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */
package org.eclipse.papyrus.designer.languages.java.reverse.ui.dialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.papyrus.designer.languages.java.reverse.ui.Activator;
import org.eclipse.papyrus.designer.languages.java.reverse.ui.preference.ReversePreference;
import org.eclipse.papyrus.designer.languages.java.reverse.umlparser.CreationPackageCatalog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * @author dumoulin
 *
 */
public class ReverseCodeDialog extends InputDialog {

	/** Internal dialog to show list of searchpaths */
	private InputListDialog listDialog;

	/** Internal dialog to show list of creation paths */
	private InputListDialog creationPathsDialog;

	/** Internal selector to select the parser */
	private Combo parserSelectorCombo;
	
	/** Returned searchpaths */
	private String[] searchPath;
	/** creationPaths read by the list. One line pattern by element */
	private String[] creationPaths;
	/** splitted creationPaths. pattern lines are splitted in small path */
	private List<String> splittedCreationPaths;
	/** Names of available parsers */
	private String[] availableParsers = new String[] {"Default Parser", "JavaParser", "JDT Parser"};

	private String SEARCHPATHS_UID = ":searchpaths";
	private String CREATIONPATHS_UID = ":creationpaths";
	private static final String PROJECTNAME_UID = ":projectName";
	private static final String SELECTEDPARSER_UID = ":selectedParserIndex";

	
	protected String MODEL_UID = "nomodeluid";

	/**
	 * then name of the selected project
	 */
	private String projectName;

	/**
	 * Returned value indicating the index of the selected parser.
	 */
	private int selectedParserIndex;
	
	private static String textMsg = "Default creation package.";
	@SuppressWarnings("unused")
	private static String creationPackageTooltips = "The default creation package is used when no matching creation package are found.";


	private static String dialogTitle = "Reverse Code";

	private static String listMsg = "search paths  - list of model packages used to search for already existing classes (ex: p1/p2)";
	private static String listTooltips = "search paths  - list of model packages used to search for already existing classes (ex: p1/p2)";

	private static String creationPathMsg = "creation paths \n"
			+ "pattern: includePath ; excludePath ; destination (use ';' as separator)\n";

	private static String creationPathTooltips = "creation paths  - list of path describing the models inside which reversed classes will be generated.\n"
			+ "pattern: includeJavaPackage ; excludeJavaPackage ; destinationPath (use ';' as separator)\n"
			+ "includeJavaPackage: the java package that should be mapped. Can contain a '*'.\n"
			+ "excludeJavaPackage: the java package that should be excluded. Can contain a '*'.\n"
			+ "destinationPath: the uml model inside which matching java packages will be created. Can contain a '*'.\n"
			+ "if destinationPath contains a '*', it will be replaced by the partname found at the place of '*'\n"
			+ "in the includeJavaPackage\n"
			+ "\n"
			+ "ex: eclipse.org.* ; eclipse.org.papyrus ; *";


	/**
	 * 
	 * Constructor.
	 *
	 * @param parentShell
	 * @param modelUid UID used to identify preferences storage. Must be linked to the model.
	 * @param projectName
	 * @param searchPathsInitialValues
	 */
	public ReverseCodeDialog(Shell parentShell, String modelUid, String projectName, List<String> searchPathsInitialValues) {
		super(parentShell, dialogTitle, textMsg, getInitialValue(modelUid, projectName), null);
		// TODO Auto-generated constructor stub
		IDialogSettings settings = Activator.getDefault().getDialogSettings();

		MODEL_UID = modelUid;
		this.projectName = projectName;

		// Look for generationPackageName if none is provided.
		// if(initialValue == null)
		// {
		// String generationPackageName = settings.get("generationPackageName");
		// getText().setText(generationPackageName);
		//
		// }
		
		// Find default values for search path and creation path
		ReversePreference preference = new ReversePreference();
		
		if (searchPathsInitialValues == null) {
			String[] savedSearchPath = settings.getArray( getRootSettingKey() + SEARCHPATHS_UID);
			if (savedSearchPath != null) {
				searchPathsInitialValues = Arrays.asList(savedSearchPath);
			} else
			{
				searchPathsInitialValues = getDefaultSearchPath(preference);
			}
		}

		// Look for saved creationPaths if none is provided.
		if (creationPaths == null) {
			String[] savedSearchPath = settings.getArray( getRootSettingKey() + CREATIONPATHS_UID);
			if(savedSearchPath != null)
				creationPaths = savedSearchPath;
			else
				creationPaths = getDefaultCreationPath(preference);
			}

		// Look for saved creationPaths if none is provided.
		if(creationPaths == null ) {
			String[] savedSearchPath = settings.getArray( getRootSettingKey() + CREATIONPATHS_UID);
			if (savedSearchPath != null) {
				creationPaths = savedSearchPath;
			} else {
				creationPaths = CreationPackageCatalog.getDefaultPackageCreationPatterns(" ; ");
			}
		}

		listDialog = new InputListDialog(listMsg, searchPathsInitialValues);
		listDialog.setTooltips(listTooltips);
		creationPathsDialog = new InputListDialog(creationPathMsg, Arrays.asList(creationPaths));
		creationPathsDialog.setTooltips(creationPathTooltips);
		
		// Selected parser
		try {
			selectedParserIndex = settings.getInt(getRootSettingKey() + SELECTEDPARSER_UID);
		} catch (NumberFormatException e) {
			// Use default parser
			selectedParserIndex = 0;
		}
	}

	/**
	 *
	 * @return the root of the setting key
	 */
	private String getRootSettingKey() {
		return MODEL_UID + projectName;
	}

	/**
	 * 
	 * @param preference
	 *        the eclipse preferences
	 * @return default values of SearchPath from eclipse preferences
	 */
	private List<String> getDefaultSearchPath(ReversePreference preference) {
		String[] defaultSearchPath = preference.getSearchPath();
		LinkedList<String> listSearchPath = new LinkedList<String>();
		for(String path : defaultSearchPath) {
			listSearchPath.add(path);
		}
		return listSearchPath;
	}

	/**
	 * 
	 * @param preference
	 *        the eclipse preferences
	 * @return default values of CreationPath from eclipse preferences
	 */
	private String[] getDefaultCreationPath(ReversePreference preference) {
		String[] defaultCreationPath = preference.getCreationPath();
		return defaultCreationPath;
	}

	/**
	 * 
	 * @param modelUid
	 * @param projectName
	 *        the name of the reversed project
	 * @return initialValue contained into setting, or <code>projectName</code> if it doesn't exists
	 */
	private static String getInitialValue(String modelUid, String projectName) {
		IDialogSettings settings = Activator.getDefault().getDialogSettings();

		// Look for generationPackageName
		String generationPackageName = settings.get(modelUid + projectName + PROJECTNAME_UID);
		if(generationPackageName != null) {
			return generationPackageName;

		}
		return projectName;
	}

	/**
	 * Allows resizing.
	 */
	@Override
	protected boolean isResizable() {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * Get the returned searchpaths.
	 *
	 * @return
	 */
	public String[] getSearchPath() {
		return searchPath;
	}

	/**
	 * Get the returned creationPaths, one pattern for each entry, separated by ';'.
	 *
	 * @return
	 */
	public List<String> getCreationPaths() {

		return splittedCreationPaths;
	}

	/**
	 * Get the index of the selected Parser. 
	 * 
	 * @return the index of the selected parser, or -1 if none is selected.
	 */
	public int getSelectedParserIndex() {
		return selectedParserIndex;
	}

	/**
	 * Create additional list of searchpaths.
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		
		IDialogSettings settings = Activator.getDefault().getDialogSettings();
		// create composite
		Composite composite = (Composite) super.createDialogArea(parent);

		listDialog.createDialogArea(composite);
		creationPathsDialog.createDialogArea(composite);
		
		// Create dropdown allowing to choose the parser
		parserSelectorCombo = new Combo (composite, SWT.READ_ONLY);
		parserSelectorCombo.setItems(availableParsers);
		parserSelectorCombo.select(selectedParserIndex);
				
		return composite;
	}

	/**
	 * Save the searchpath after the button is pressed.
	 */
	@Override
	protected void okPressed() {
		// Save the list before the control is disposed
		searchPath = listDialog.getList();
		creationPaths = creationPathsDialog.getList();
		String defaultCreationPath = getValue();
		if (defaultCreationPath == null || defaultCreationPath.length() == 0) {
			defaultCreationPath = CreationPackageCatalog.getDefaultCreationPath();
		}

		// Check inputs
		try {
			// Check paths
			splittedCreationPaths = computeCreationPaths(creationPaths);
			CreationPackageCatalog.validateCreationPath(splittedCreationPaths);
			// add default searchPaths
			searchPath = addDefaultSearchPaths(Arrays.asList(searchPath), splittedCreationPaths).toArray(new String[0]);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			showError(e.getMessage());
			return;
		}
		
		// Get selected parser
		selectedParserIndex = parserSelectorCombo.getSelectionIndex();
 
		
		// save values
		IDialogSettings settings = Activator.getDefault().getDialogSettings();
		settings.put(getRootSettingKey() + SEARCHPATHS_UID, searchPath);
		settings.put(getRootSettingKey() + CREATIONPATHS_UID, creationPaths);
		settings.put(getRootSettingKey() + PROJECTNAME_UID, getValue());
		settings.put(getRootSettingKey() + SELECTEDPARSER_UID, selectedParserIndex);

		super.okPressed();
	}


	/**
	 * Add the defaultSearchPath extracted from the splittedCreationPath to the searchPath.
	 *
	 * @param splittedCreationPaths2
	 */
	private List<String> addDefaultSearchPaths(List<String> searchPath, List<String> splittedCreationPaths) {


		List<String> result = new ArrayList<String>(searchPath.size() + splittedCreationPaths.size());
		result.addAll(searchPath);

		List<String> additionalPaths = CreationPackageCatalog.extractCreationPaths(splittedCreationPaths.toArray(new String[0]));
		for (String toAdd : additionalPaths) {
			if (!result.contains(toAdd)) {
				result.add(toAdd);
			}
		}

		return result;
	}

	/**
	 * Show an error message
	 *
	 * @param message
	 */
	private void showError(String message) {
		System.err.println("Error: " + message);

	}


	/**
	 * Compute and check the creationsPaths
	 *
	 * @param creationPaths
	 *            An array with one creationPattern by element
	 * @return An array with one path by elements
	 * @throws Exception
	 */
	private List<String> computeCreationPaths(String[] creationPaths) throws Exception {

		List<String> result = new ArrayList<String>();

		// Iterate line by line
		// For each line, separate the 3 patterns
		// If a pattern is empty, put a null
		for (String line : creationPaths) {
			String[] eles = line.split(";");
			if (eles.length != 3) {
				throw new Exception("A line must contains 3 pattern separated by ';' :" + line);
			}
			for (String ele : eles)
			{
				ele = ele.trim();
				if (ele.length() == 0) {
					ele = null;
				}
				// add ele
				result.add(ele);
			}
		}
		return result;
	}


}
