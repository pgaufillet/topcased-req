/*******************************************************************************
 * Copyright (c) 2008 TOPCASED. All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Topcased contributors and others - initial API and implementation
 *******************************************************************************/
package org.topcased.requirement.sam.views.preview.template;

import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.emf.ecore.EObject;

/**
 * Acceleo service to format HTML tag for the Document View Creation : 8 dec. 2008
 * 
 * @author <a href="mailto:steve.monnier@obeo.fr">Steve Monnier</a>
 */
public class HTMLFormatter
{
    private static HashMap<String, String> htmlSpecialCharacters;
    static
    {
        htmlSpecialCharacters = new HashMap<String, String>();
        htmlSpecialCharacters.put("&lt;", "<");
        htmlSpecialCharacters.put("&gt;", ">");
        htmlSpecialCharacters.put("&amp;", "&");
        htmlSpecialCharacters.put("&quot;", "\"");
        htmlSpecialCharacters.put("&agrave;", "à");
        htmlSpecialCharacters.put("&Agrave;", "À");
        htmlSpecialCharacters.put("&acirc;", "â");
        htmlSpecialCharacters.put("&auml;", "ä");
        htmlSpecialCharacters.put("&Auml;", "Ä");
        htmlSpecialCharacters.put("&Acirc;", "Â");
        htmlSpecialCharacters.put("&aring;", "å");
        htmlSpecialCharacters.put("&Aring;", "Å");
        htmlSpecialCharacters.put("&aelig;", "æ");
        htmlSpecialCharacters.put("&AElig;", "Æ");
        htmlSpecialCharacters.put("&ccedil;", "ç");
        htmlSpecialCharacters.put("&Ccedil;", "Ç");
        htmlSpecialCharacters.put("&eacute;", "é");
        htmlSpecialCharacters.put("&Eacute;", "É");
        htmlSpecialCharacters.put("&egrave;", "è");
        htmlSpecialCharacters.put("&Egrave;", "È");
        htmlSpecialCharacters.put("&ecirc;", "ê");
        htmlSpecialCharacters.put("&Ecirc;", "Ê");
        htmlSpecialCharacters.put("&euml;", "ë");
        htmlSpecialCharacters.put("&Euml;", "Ë");
        htmlSpecialCharacters.put("&iuml;", "ï");
        htmlSpecialCharacters.put("&Iuml;", "�?");
        htmlSpecialCharacters.put("&ocirc;", "ô");
        htmlSpecialCharacters.put("&Ocirc;", "Ô");
        htmlSpecialCharacters.put("&ouml;", "ö");
        htmlSpecialCharacters.put("&Ouml;", "Ö");
        htmlSpecialCharacters.put("&oslash;", "ø");
        htmlSpecialCharacters.put("&Oslash;", "Ø");
        htmlSpecialCharacters.put("&szlig;", "ß");
        htmlSpecialCharacters.put("&ugrave;", "ù");
        htmlSpecialCharacters.put("&Ugrave;", "Ù");
        htmlSpecialCharacters.put("&ucirc;", "û");
        htmlSpecialCharacters.put("&Ucirc;", "Û");
        htmlSpecialCharacters.put("&uuml;", "ü");
        htmlSpecialCharacters.put("&Uuml;", "Ü");
        htmlSpecialCharacters.put("&nbsp;", " ");
    }

    private final String[] HTML_STARTING_TAGS_TO_ERASE = {"<span", "<font", "<A", "<HTML", "<META", "<DIV", "<P "};

    private String[] HTML_TAGS_TO_ERASE = {"\\\t", "\n", "\r", "</span>", "</font>", "</li>", "</A>", "<B>", "</B>", "<BIG>", "</BIG>", "<BODY>", "</BODY>", "</BQ>", "</BLOCKQUOTE>", "</CAPTION>", "</DIV>", "<FORM>",
            "</FORM>", "<H1>", "</H1>", "<H2>", "</H2>", "<H3>", "</H3>", "<HEAD>", "</HEAD>", "</HTML>", "<I>", "</I>", "</META>", "<SELECT>", "</SELECT>", "<SMALL>", "</SMALL>", "<STRONG>",
            "</STRONG>", "<TITLE>", "</TITLE>", "<U>", "</U>", "<p>", "<o:p>", "</o:p>"};

    private final String[] HTML_TAGS_TO_REPLACE_BY_LINE_FEED = {"<br>", "<br/>", "<br />", "</p>", "<tbody>", "</tbody>", "</ul>", "</ol>"};

    private final String[] HTML_STARTING_TAGS_TO_REPLACE_BY_LINE_TABULATION = {"<CAPTION"};

    private final String[] HTML_TAGS_TO_REPLACE_BY_LINE_TABULATION = {"<BQ>", "<BLOCKQUOTE>"};

    private final String IMAGE_TAG = "<img";

    private final String IMAGE_REPLACEMENT_TAG_START = "<IMAGE>";

    private final String IMAGE_REPLACEMENT_TAG_END = "</IMAGE>";

    private final String TABLE_REPLACEMENT_TAG_START = "<TABLE>\n";

    private final String TABLE_REPLACEMENT_TAG_END = "</TABLE>\n";

    /**
     * This method format HTML tags for the Document View
     * 
     * @param eOjbect
     * @param comments
     * @return
     */
    public String replaceAllHTMLTags(EObject eOjbect, final String comments, final String[] commentsToIgnore)
    {
        for (int i = 0; i < commentsToIgnore.length; i++)
        {
            for (int j = 0; j < HTML_TAGS_TO_ERASE.length; j++)
            {
                if (HTML_TAGS_TO_ERASE[j].equalsIgnoreCase(commentsToIgnore[i]))
                {
                    HTML_TAGS_TO_ERASE[j] = "";
                }
            }
            for (int j = 0; j < HTML_TAGS_TO_REPLACE_BY_LINE_FEED.length; j++)
            {
                if (HTML_TAGS_TO_REPLACE_BY_LINE_FEED[j].equalsIgnoreCase(commentsToIgnore[i]))
                {
                    HTML_TAGS_TO_REPLACE_BY_LINE_FEED[j] = "";
                }
            }
        }

        StringBuffer formatedComments = new StringBuffer().append(comments);

        // Remove line feed and tabulation of file format to use only the htlm tag
//        formatedComments = formatedComments.replace(0, formatedComments.length(), formatedComments.toString().replaceAll("\r", ""));
//        formatedComments = formatedComments.replace(0, formatedComments.length(), formatedComments.toString().replaceAll("\n", ""));
//        formatedComments = formatedComments.replaceAll("\n", "");
//        formatedComments = formatedComments.replaceAll("\\\t", "");
        
        
        // Formatting of tags that need to just removed
        for (int i = 0; i < HTML_TAGS_TO_ERASE.length; i++)
        {
            if (HTML_TAGS_TO_ERASE[i] != "")
            {
                formatedComments = formatedComments.replace(0, formatedComments.length(), formatedComments.toString().replaceAll(HTML_TAGS_TO_ERASE[i].toLowerCase(), ""));
                formatedComments = formatedComments.replace(0, formatedComments.length(), formatedComments.toString().replaceAll(HTML_TAGS_TO_ERASE[i].toUpperCase(), ""));
            }
        }
        
        formatedComments = formatedComments.replace(0, formatedComments.length(), replaceHTMLSpecialCharacters(formatedComments.toString()));

        // Formatting of tags that need to be replaced by a tabulation
        for (int i = 0; i < HTML_TAGS_TO_REPLACE_BY_LINE_TABULATION.length; i++)
        {
            formatedComments = formatedComments.replace(0, formatedComments.length(), formatedComments.toString().replaceAll(HTML_TAGS_TO_REPLACE_BY_LINE_TABULATION[i].toLowerCase(), ""));
            formatedComments = formatedComments.replace(0, formatedComments.length(), formatedComments.toString().replaceAll(HTML_TAGS_TO_REPLACE_BY_LINE_TABULATION[i].toUpperCase(), ""));
        }


        // Formatting of tags containing attributes that need to be removed
        for (int i = 0; i < HTML_STARTING_TAGS_TO_ERASE.length; i++)
        {
            formatedComments = formatedComments.replace(0, formatedComments.length(), replaceTagWithAttributes(formatedComments.toString(), HTML_STARTING_TAGS_TO_ERASE[i].toLowerCase(), ""));
            formatedComments = formatedComments.replace(0, formatedComments.length(), replaceTagWithAttributes(formatedComments.toString(), HTML_STARTING_TAGS_TO_ERASE[i].toUpperCase(), ""));
        }

        // Formatting of tags containing attributes that need to be replace by a
        // tabulation
        for (int i = 0; i < HTML_STARTING_TAGS_TO_REPLACE_BY_LINE_TABULATION.length; i++)
        {
            formatedComments = formatedComments.replace(0, formatedComments.length(), replaceTagWithAttributes(formatedComments.toString(), HTML_STARTING_TAGS_TO_REPLACE_BY_LINE_TABULATION[i].toLowerCase(), ""));
            formatedComments = formatedComments.replace(0, formatedComments.length(), replaceTagWithAttributes(formatedComments.toString(), HTML_STARTING_TAGS_TO_REPLACE_BY_LINE_TABULATION[i].toUpperCase(), ""));
        }

        // Formatting of tags that need to be replaced by a line feed
        for (int i = 0; i < HTML_TAGS_TO_REPLACE_BY_LINE_FEED.length; i++)
        {
            if (HTML_TAGS_TO_REPLACE_BY_LINE_FEED[i] != "")
            {
                formatedComments = formatedComments.replace(0, formatedComments.length(), formatedComments.toString().replaceAll(HTML_TAGS_TO_REPLACE_BY_LINE_FEED[i].toLowerCase(), ""));
                formatedComments = formatedComments.replace(0, formatedComments.length(), formatedComments.toString().replaceAll(HTML_TAGS_TO_REPLACE_BY_LINE_FEED[i].toUpperCase(), ""));
            }
        }

        return formatedComments.toString();
    }

    /**
     * This method format HTML tags for the Document View
     * 
     * @param eOjbect
     * @param comments
     * @return
     */
    public String replaceAllHTMLTags(EObject eOjbect, final String comments)
    {
        String formatedComments = comments;

        // Remove line feed and tabulation of file format to use only the htlm tag
        formatedComments = formatedComments.replaceAll("\r", "");
        formatedComments = formatedComments.replaceAll("\n", "");
        formatedComments = formatedComments.replaceAll("\\\t", "");
        formatedComments = replaceHTMLSpecialCharacters(formatedComments);

        // Formatting of tags that need to be replaced by a tabulation
        for (int i = 0; i < HTML_TAGS_TO_REPLACE_BY_LINE_TABULATION.length; i++)
        {
            formatedComments = formatedComments.replaceAll(HTML_TAGS_TO_REPLACE_BY_LINE_TABULATION[i].toLowerCase(), "\t");
            formatedComments = formatedComments.replaceAll(HTML_TAGS_TO_REPLACE_BY_LINE_TABULATION[i].toUpperCase(), "\t");
        }

        // Formatting of tags that need to just removed
        for (int i = 0; i < HTML_TAGS_TO_ERASE.length; i++)
        {
            formatedComments = formatedComments.replaceAll(HTML_TAGS_TO_ERASE[i].toLowerCase(), "");
            formatedComments = formatedComments.replaceAll(HTML_TAGS_TO_ERASE[i].toUpperCase(), "");
        }

        // Formatting of tags containing attributes that need to be removed
        for (int i = 0; i < HTML_STARTING_TAGS_TO_ERASE.length; i++)
        {
            formatedComments = replaceTagWithAttributes(formatedComments, HTML_STARTING_TAGS_TO_ERASE[i].toLowerCase(), "");
            formatedComments = replaceTagWithAttributes(formatedComments, HTML_STARTING_TAGS_TO_ERASE[i].toUpperCase(), "");
        }

        // Formatting of tags containing attributes that need to be replace by a
        // tabulation
        for (int i = 0; i < HTML_STARTING_TAGS_TO_REPLACE_BY_LINE_TABULATION.length; i++)
        {
            formatedComments = replaceTagWithAttributes(formatedComments, HTML_STARTING_TAGS_TO_REPLACE_BY_LINE_TABULATION[i].toLowerCase(), "\t");
            formatedComments = replaceTagWithAttributes(formatedComments, HTML_STARTING_TAGS_TO_REPLACE_BY_LINE_TABULATION[i].toUpperCase(), "\t");
        }

        // Formatting HTML Lists ( <ul> and <ol> )
        formatedComments = formatHTMLLists(formatedComments);

        // Formatting HTML tables formatedComments =
        formatedComments = replaceTagWithAttributes(formatedComments, "<table ", TABLE_REPLACEMENT_TAG_START);
        formatedComments = replaceTagWithAttributes(formatedComments, "<TABLE ", TABLE_REPLACEMENT_TAG_START);
        formatedComments = replaceTagWithAttributes(formatedComments, "<table>", TABLE_REPLACEMENT_TAG_START);
        formatedComments = replaceTagWithAttributes(formatedComments, "</table>", TABLE_REPLACEMENT_TAG_END);
        formatedComments = replaceTableTags(formatedComments);

        // Formatting HTML Images insertions
        formatedComments = replaceImageTags(formatedComments);

        // Formatting of tags that need to be replaced by a line feed
        for (int i = 0; i < HTML_TAGS_TO_REPLACE_BY_LINE_FEED.length; i++)
        {
            formatedComments = formatedComments.replaceAll(HTML_TAGS_TO_REPLACE_BY_LINE_FEED[i].toLowerCase(), "\n");
            formatedComments = formatedComments.replaceAll(HTML_TAGS_TO_REPLACE_BY_LINE_FEED[i].toUpperCase(), "\n");
        }

        return formatedComments;
    }

    /**
     * This method replace all tag starting with "startingTag" by "replacement"
     * 
     * @param comments string that need to be formatted
     * @param startingTag beginning of tag type that are going to be fully replaced
     * @param replacement
     * @return
     */
    private String replaceTagWithAttributes(final String comments, final String startingTag, final String replacement)
    {
        String formatedComments = comments;
        
        Pattern tableRowPattern = Pattern.compile(startingTag.trim()+" [^>]*>", Pattern.CASE_INSENSITIVE);
        Matcher tableRowMatcher = tableRowPattern.matcher(formatedComments);
        formatedComments = tableRowMatcher.replaceAll(replacement);

        return formatedComments;
    }

    private String replaceFirstAppearanceTagWithAttributes(final String comments, final String startingTag, final String replacement)
    {
        String formatedComments = comments;
        int start = formatedComments.indexOf(startingTag);
        int end = formatedComments.indexOf(">", start);
        if ((end + 1) < formatedComments.length())
        {
            formatedComments = formatedComments.substring(0, start) + replacement + formatedComments.substring(end + 1);
        }
        else
        {
            formatedComments = formatedComments.substring(0, start) + replacement;
        }
        return formatedComments;
    }

    private String replaceImageTags(final String comments)
    {
        String formatedComments = comments;

        formatedComments = formatedComments.replaceAll("<IMG ", "<img ");
        formatedComments = formatedComments.replaceAll("SRC=\"", "src=\"");

        while (formatedComments.contains("<img "))
        {
            int start = formatedComments.indexOf(IMAGE_TAG.toLowerCase());
            int end = formatedComments.indexOf(">", start);

            int startSource = formatedComments.indexOf("src=\"", start) + 5;
            int endSource = formatedComments.indexOf("\"", startSource);

            if ((end + 1) < formatedComments.length())
            {
                formatedComments = formatedComments.substring(0, start) + IMAGE_REPLACEMENT_TAG_START + formatedComments.substring(startSource, endSource) + IMAGE_REPLACEMENT_TAG_END
                        + formatedComments.substring(end + 1);
            }
            else
            {
                formatedComments = formatedComments.substring(0, start) + IMAGE_REPLACEMENT_TAG_START + formatedComments.substring(startSource, endSource) + IMAGE_REPLACEMENT_TAG_END;
            }
        }
        return formatedComments;
    }

    private String replaceTableTags(final String comments)
    {
        String formatedComments = comments;

        formatedComments = formatedComments.replaceAll("<TD", "<td");
        formatedComments = formatedComments.replaceAll("<TR", "<tr");
        formatedComments = formatedComments.replaceAll("</TD>", "</td>");
        formatedComments = formatedComments.replaceAll("</TR>", "</tr>");
        while (formatedComments.contains("</td>"))
        {
            int firstEndTd = formatedComments.indexOf("</td>");
            int nextTd = formatedComments.indexOf("<td", firstEndTd);
            int nextEndTr = formatedComments.indexOf("</tr", firstEndTd);
            if (nextTd == -1)
            {
                nextTd = Integer.MAX_VALUE;
            }
            if (nextEndTr == -1)
            {
                nextEndTr = Integer.MAX_VALUE;
            }
            if (nextTd < nextEndTr && nextTd < Integer.MAX_VALUE)
            {
                formatedComments = formatedComments.substring(0, firstEndTd) + "\t" + formatedComments.substring(nextTd);
            }
            else if (nextEndTr < nextTd && nextEndTr < Integer.MAX_VALUE)
            {
                formatedComments = formatedComments.substring(0, firstEndTd) + formatedComments.substring(nextEndTr);
            }
            else if (nextTd == Integer.MAX_VALUE && nextEndTr == Integer.MAX_VALUE)
            {
                formatedComments = formatedComments.replaceFirst("</td>", "");
            }
        }
        formatedComments = replaceTagWithAttributes(formatedComments, "<td", "");
        // remove br tags inside tr tags to keep table format on the Document view
        while (formatedComments.contains("<tr"))
        {
            int firstTr = formatedComments.indexOf("<tr");
            int nextEndTr = formatedComments.indexOf("</tr", firstTr);
            int nextBr = formatedComments.toLowerCase().indexOf("<br", firstTr);
            if (nextBr < nextEndTr && nextBr > -1)
            {
                int nextEndBr = formatedComments.indexOf(">", nextBr);
                formatedComments = formatedComments.substring(0, nextBr) + formatedComments.substring(nextEndBr + 1);
            }
            else
            {
                formatedComments = replaceFirstAppearanceTagWithAttributes(formatedComments, "<tr", "");
            }
        }
        // formatedComments = replaceTagWithAttributes(formatedComments, "<tr", "");
        formatedComments = replaceTagWithAttributes(formatedComments, "</tr", "\n");
        return formatedComments;
    }

    /**
     * 
     * @param comments : string that need to be formatted
     * @return the string with HTML lists formatted for the Document View
     */
    private String formatHTMLLists(final String comments)
    {
        String formatedComments = comments;
        formatedComments = formatedComments.replaceAll("<UL>".toUpperCase(), "<ul>");
        formatedComments = formatedComments.replaceAll("<OL>".toUpperCase(), "<ol>");
        formatedComments = formatedComments.replaceAll("<LI>".toUpperCase(), "<li>");

        Integer bulletNumber = 0;

        while (formatedComments.contains("<li>"))
        {
            // find the position of first appearance of each tag types
            int firstTag_ul = formatedComments.indexOf("<ul>");
            int firstTag_ol = formatedComments.indexOf("<ol>");
            int firstTag_li = formatedComments.indexOf("<li>");
            if (firstTag_ul == -1)
            {
                firstTag_ul = Integer.MAX_VALUE;
            }
            if (firstTag_ol == -1)
            {
                firstTag_ol = Integer.MAX_VALUE;
            }
            if (firstTag_li == -1)
            {
                firstTag_li = Integer.MAX_VALUE;
            }

            // find witch tag appears in first and replace it
            if ((firstTag_ul < firstTag_ol) && (firstTag_ul < firstTag_li))
            {
                bulletNumber = 0;
                formatedComments = formatedComments.replaceFirst("<ul>", "");
            }
            else if ((firstTag_ol < firstTag_ul) && (firstTag_ol < firstTag_li))
            {
                bulletNumber = 1;
                formatedComments = formatedComments.replaceFirst("<ol>", "");
            }
            else if ((firstTag_li < firstTag_ul) && (firstTag_li < firstTag_ol))
            {
                if (bulletNumber > 0)
                {
                    formatedComments = formatedComments.replaceFirst("<li>", "\n\t" + bulletNumber + ".\t");
                    bulletNumber++;
                }
                else
                {
                    formatedComments = formatedComments.replaceFirst("<li>", "\n\t*\t");
                }
            }
        }

        return formatedComments;
    }

    /**
     * This method replace the special characters provided by the hashmap "htmlSpecialCharacters" keys by its values
     * 
     * @param comments : string that need to be formatted
     * @return the string with html special characters replaced by regular characters
     */
    private String replaceHTMLSpecialCharacters(final String comments)
    {
        String formatedComments = comments;
        Iterator<String> htmlSpecialCharacterIterator = htmlSpecialCharacters.keySet().iterator();
        while (htmlSpecialCharacterIterator.hasNext())
        {
            String key = htmlSpecialCharacterIterator.next();
            String value = htmlSpecialCharacters.get(key);
            formatedComments = formatedComments.replaceAll(key, value);
        }
        return formatedComments;
    }
}