<%--/*****************************************************************************
 * Copyright (c) 2009 atos origin.
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Emilien Perico (Atos Origin) emilien.perico@atosorigin.com - Initial
 * API and implementation
 * 
 *****************************************************************************/--%>

<%
metamodel http://www.eclipse.org/emf/2002/Ecore
import org.topcased.model2doc.templates.acceleo.TemplateServices
import org.topcased.requirement.gendoc.templates.acceleotemplates.elements_requirements
%>

<%-- A template to display children name and associated requirements of a specified element --%>
<%script type="ecore.EObject" name="elements_requirements" file="<%getOutputFile(getRootContainer(getProperty("root_container")), getProperty("resultFileName"))%>"%>
<book>
	<chapter>
		<title><![CDATA[<%getLabelForEObject()%>]]></title>
		<%for (eContents()){%>
			<%displayElementAndRequirements%>
		<%}%>
	</chapter>
</book>
