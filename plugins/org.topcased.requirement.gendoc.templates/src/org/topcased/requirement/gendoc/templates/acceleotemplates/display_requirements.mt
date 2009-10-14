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
metamodel http://org.topcased.requirement/1.0
import org.topcased.requirement.gendoc.templates.RequirementsUtils
%>

<%script type="requirement.CurrentRequirement" name="display_requirements" %>
<%setFirstAttributeLink(true)%>
<para>
	<%if (shortDescription.length() == 0){%>
		<phrase><markup role="CAREIdent"><![CDATA[<%identifier%>]]></markup></phrase>
	<%}else{%>
		<phrase><markup role="CAREIdent"><![CDATA[<%identifier%> : ]]></markup></phrase>
		<phrase><markup role="NormalIdent"><![CDATA[<%shortDescription%>]]></markup></phrase>
	<%}%>
</para>
<%for (attribute){%>
	<para>
		<%attribute_template%>
	</para>	
<%}%>

<%script type="requirement.Attribute" name="attribute_template" %>

<%script type="requirement.TextAttribute" name="attribute_template" %>
<phrase><markup role="Attributes"><%getTabChar()%><%getFormattedName(name)%>&#160;: </markup></phrase><phrase><markup role="<%getStyleNameFromName(name)%>"><%value%></markup></phrase>

<%script type="requirement.AttributeLink" name="attribute_template" %>
<%if (isFirstAttributeLink()){%>
	<%setFirstAttributeLink(false)%>
	<phrase><markup role="Attributes"><%getTabChar()%><%getFormattedName(name)%>&#160;: </markup></phrase>
			<%getStyleNameFromName(name).put("attributeNameStyle")%>
	<%for (eContainer().getLinkedUpstreamRequirements()){%>
		<phrase>
			<markup role="<%get("attributeNameStyle")%>">
				<%if (i() != 0){%>,&#160;<%}%><%ident%>
			</markup>
		</phrase>
	<%}%>
			
<%}%>

<%script type="requirement.ObjectAttribute" name="attribute_template" %>
<phrase><markup role="Attributes"><%getTabChar()%><%getFormattedName(name)%>&#160;: </markup></phrase> <phrase><markup role="<%getStyleNameFromName(name)%>"><%value%></markup></phrase>

<%script type="requirement.AttributeAllocate" name="attribute_template" %>
<phrase><markup role="Attributes"><%getTabChar()%><%getFormattedName(name)%>&#160;: </markup></phrase><phrase><markup role="<%getStyleNameFromName(name)%>"><%value%></markup></phrase>
