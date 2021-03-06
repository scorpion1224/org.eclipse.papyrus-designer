/*******************************************************************************
 * Copyright (c) 2017 CEA LIST
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ansgar Radermacher (CEA LIST) - initial API and implementation
 *
 *******************************************************************************/

package org.eclipse.papyrus.designer.transformation.languages.cpp.tests;

import org.eclipse.core.resources.IProject;
import org.eclipse.papyrus.designer.languages.common.testutils.TestConstants;
import org.eclipse.papyrus.designer.languages.common.testutils.TransformationTestSupport;
import org.eclipse.papyrus.junit.utils.rules.HouseKeeper;
import org.eclipse.papyrus.junit.utils.rules.PapyrusEditorFixture;
import org.eclipse.papyrus.junit.utils.rules.PluginResource;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

@PluginResource("org.eclipse.papyrus.designer.transformation.languages.cpp.library:/models/examples/HelloWorldCpp/HelloWorldCpp.di")
public class TestHelloWorldCppExample {

	@SuppressWarnings("nls")
	public static final String HELLO_WORLD_CPP_DEFAULT_NODE = "HelloWorldCpp_defaultNode_SystemDepPlan";

	@SuppressWarnings("nls")
	public static final String DEPLOYMENT_MONO_PULL = "HelloWorldCpp::deployment::SystemDepPlan";

	@Rule
	/** The model set fixture. */
	public final PapyrusEditorFixture modelSetFixture = new PapyrusEditorFixture();

	@ClassRule
	public static HouseKeeper.Static houseKeeper = new HouseKeeper.Static();

	@Test
	public void testProducerConsumerMonoPull() throws InterruptedException {

		TransformationTestSupport tts = new TransformationTestSupport(this.getClass(), houseKeeper, modelSetFixture);

		// create generated project. For generation purposes, it is not important that this is not a CDT project
		IProject genProject = houseKeeper.createProject(HELLO_WORLD_CPP_DEFAULT_NODE);

		
		tts.runTransformation(DEPLOYMENT_MONO_PULL);
		
		tts.validateResults(genProject, TestConstants.EXPECTED_RESULT + TestConstants.FILE_SEP +
				HELLO_WORLD_CPP_DEFAULT_NODE + TestConstants.FILE_SEP + TestConstants.SRC_GEN, TestConstants.SRC_GEN);
	}

}
