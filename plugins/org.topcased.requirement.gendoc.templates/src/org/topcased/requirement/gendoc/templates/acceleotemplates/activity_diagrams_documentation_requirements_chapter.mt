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
metamodel http://www.eclipse.org/uml2/3.0.0/UML
import org.topcased.model2doc.templates.acceleo.TemplateServices
import org.topcased.requirement.gendoc.templates.acceleotemplates.activity_diagrams_documentation_requirements
%>

<%-- A template to display an activity diagram, its documentation and the one of elements in diagram, 
	the affected requirements if any. Also search recursively in call behavior actions --%>
<%script type="uml.Activity" name="activity_diagrams_documentation_requirements" file="<%getOutputFile(getRootContainer(getProperty("root_container")), getProperty("resultFileName"))%>"%>
<book>
	<chapter>
		<%if (name != null && name.length() != 0){%>
			<title><![CDATA[<%name%>]]></title>
		<%}%>
		<%loopOnActivityTemplate%>
	</chapter>
</book>
