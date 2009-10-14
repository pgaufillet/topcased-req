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
import org.topcased.model2doc.templates.acceleo.TemplateServices
import org.topcased.requirement.gendoc.templates.FormattingUtils
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
<table frame='none'><tgroup><tbody>
<%for (attribute){%>
	<%attribute_template%>
<%}%>
</tbody></tgroup></table>

<%script type="requirement.Attribute" name="attribute_template" %>

<%script type="requirement.TextAttribute" name="attribute_template" %>
<row>
<entry><para><phrase><markup role="Attributes"><%getTabChar()%><%getFormattedName(name)%>&#160;: </markup></phrase></para></entry>
<entry><%value.insertFormattedAttribute(getStyleNameFromName(name))%></entry>
</row>

<%script type="requirement.AttributeLink" name="attribute_template" %>
<%if (isFirstAttributeLink()){%>
	<row>
	<%setFirstAttributeLink(false)%>
	<entry><para><phrase><markup role="Attributes"><%getTabChar()%><%getFormattedName(name)%>&#160;: </markup></phrase></para></entry>
	<%getStyleNameFromName(name).put("attributeNameStyle")%>
	<entry><para><phrase><markup role="<%get("attributeNameStyle")%>"><%for (eContainer().getLinkedUpstreamRequirements()){%><%if (i() != 0){%>, <%}%><%ident%><%}%></markup></phrase></para></entry>
	</row>
<%}%>

<%script type="requirement.ObjectAttribute" name="attribute_template" %>
<row>
<entry><para><phrase><markup role="Attributes"><%getTabChar()%><%getFormattedName(name)%>&#160;: </markup></phrase></para></entry>
<entry><para><phrase><markup role="<%getStyleNameFromName(name)%>"><%value%></markup></phrase></para></entry>
</row>

<%script type="requirement.AttributeAllocate" name="attribute_template" %>
<row>
<entry><para><phrase><markup role="Attributes"><%getTabChar()%><%getFormattedName(name)%>&#160;: </markup></phrase></para></entry>
<entry><para><phrase><markup role="<%getStyleNameFromName(name)%>"><%value%></markup></phrase></para></entry>
</row>
