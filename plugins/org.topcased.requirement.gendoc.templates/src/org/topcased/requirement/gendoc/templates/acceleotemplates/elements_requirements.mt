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
import org.topcased.requirement.gendoc.templates.RequirementsUtils
import org.topcased.requirement.gendoc.templates.acceleotemplates.display_requirements
%>

<%-- A template to display children name and associated requirements of a specified element --%>
<%script type="uml.NamedElement" name="elements_requirements" file="<%getOutputFile(getRootContainer(getProperty("root_container")), getProperty("resultFileName"))%>"%>
<book>
	<chapter>
		<%if (name != null && name.length() != 0){%>
			<title><![CDATA[<%name%>]]></title>
		<%}%>
		<%for (allOwnedElements().filter("NamedElement")){%>
			<%displayElementAndRequirements%>
		<%}%>
	</chapter>
</book>

<%-- this script is only used with NamedElement --%>
<%script type="uml.NamedElement" name="displayElementAndRequirements"%>
<%getCurrentRequirementsForEObject().put("RequirementsList")%>
<%if (get("RequirementsList") != null && get("RequirementsList").length() != 0){%>
	<section>
		<title><![CDATA[<%name%> (<%getObjectTypeName()%>)]]></title>
		<%for (get("RequirementsList")){%>
			<%display_requirements%>
		<%}%>
	</section>
<%}%>


