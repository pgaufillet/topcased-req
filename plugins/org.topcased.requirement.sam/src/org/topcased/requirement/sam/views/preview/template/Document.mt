<%
metamodel http://org.topcased.requirement/1.0
import org.topcased.requirement.sam.views.preview.template.SamDoc
import org.topcased.requirement.sam.views.preview.template.TraceabilityDocument
import org.topcased.requirement.sam.views.preview.template.HTMLFormatter
%>

<%script type="requirement.HierarchicalElement" name="default" file="result.txt"%>
<%if (sectionNumber() != "") {%><%sectionNumber()%> <%}%><%if (children == null) {%>#Function <%}%><%samElement.getIdentifier()%>

<%if (samElement.getComment() != "") {%>
<%replaceAllHTMLTags(samElement.getComment())%>

<%}%>
<%if (requirement.nSize() > 0){%><%requirement.default.sep("\n")%>
<%}%>
<%if (children.nSize() > 0){%><%children.default()%><%}%>
<%if (children == null) {%>#EndFunction

<%}%>

<%script type="requirement.SpecialChapter" name="default" file="result.txt"%>
<%if (eAllContents().nSize() > 0){%><%eContents().default().sep("\n")%><%}%>

<%script type="requirement.Requirement" name="default"%>
<%if (eContainer().filter("requirement.ProblemChapter").nSize()>0 || eContainer().filter("requirement.UntracedChapter").nSize()>0){%>
<%for (attribute[isValuated]){%><%--.nSort("default()")--%>
<%default()%>
<%}%>
<%}else{%>
<%if (identifier != null){%><%identifier%> <%}%>

<%if (shortDescription != null){%><%replaceAllHTMLTags(shortDescription)%>
<%}%>#EndText
<%for (attribute[isValuated]){%><%--.nSort("default()")--%>
<%default()%>
<%}%>
<%}%>

<%script type="requirement.AnonymousRequirement" name="default"%>
<%if (shortDescription != null){%><%replaceAllHTMLTags(shortDescription)%>
<%}%>
<%for (attribute[isValuated]){%><%--.nSort("default()")--%>
<%default()%>
<%}%>

<%script type="requirement.HierarchicalElement" name="sectionNumber"%>
<%if (parent.filter("HierarchicalElement") != null) {%><%if (parent.parent.filter("HierarchicalElement") != null) {%><%parent.sectionNumber()%>.<%}%><%index()%><%}%>

<%script type="EObject" name="default"%>

<%script type="EObject" name="index"%>
<%push()%><%for (parent.child()) {%><%if (self == peek()) {%><%i()+1%><%}%><%}%><%pop()%>

<%script type="requirement.Attribute" name="isValuated"%>

<%script type="requirement.ObjectAttribute" name="isValuated"%>
<%value != null%>

<%script type="requirement.TextAttribute" name="isValuated"%>
<%value != null%>

<%script type="requirement.Attribute" name="default"%>

<%script type="requirement.ObjectAttribute" name="default"%>
<%if (name != null){%><%name%> <%}%><%value.getIdentifier()%>

<%script type="requirement.TextAttribute" name="default"%>
<%if (name != null){%><%name%> <%}%><%value%>

<%script type="requirement.IdentifiedElement" name="getIdentifier"%>
<%identifier%>
