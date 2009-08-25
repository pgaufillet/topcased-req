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
import templates.display_diagram
import templates.documentation_of_diagram
import org.topcased.requirement.gendoc.templates.acceleotemplates.display_requirements
%>

<%-- A template to get the diagrams, documentation and requirement associated with the given root_container --%>
<%script type="uml.Element" name="diagrams_documentation_requirements" file="<%getOutputFile(getRootContainer(getProperty("root_container")), getProperty("resultFileName"))%>"%>
<book>
	<chapter>
		<%display_diagrams_documentation_requirements%>
	</chapter>
</book>

<%script type="uml.Element" name="display_diagrams_documentation_requirements"%>
<%displayImage%>
<%docbookDocumentationOfDiagram%>
<%docbookRequirement%>

<%script type="ecore.EObject" name="docbookRequirement"%>
<%for (getCurrentRequirements()){%>
	<section>
		<%display_requirements%>
	</section>
<%}%>
