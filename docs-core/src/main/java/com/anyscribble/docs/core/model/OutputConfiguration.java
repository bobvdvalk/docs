/**
 * AnyScribble Docs Core - Writing for Developers by Developers
 * Copyright © 2016 AnyScribble (thomas.biesaart@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anyscribble.docs.core.model;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * This class represents the base configuration for any build target.
 *
 * @author Thomas Biesaart
 */
public abstract class OutputConfiguration {
    protected OutputConfiguration parent;
    private Path outputFile;
    private Boolean chapters;
    private Boolean smart;
    private String defaultImageExtension;
    private Integer baseLevelHeaders;
    private Boolean fileScope;
    private Boolean normalize;
    private Boolean preserveTabs;
    private Integer tabStop;
    private Boolean standalone;
    private Path template;
    private WrapStyle wrap;
    private Integer columns;
    private Boolean toc;
    private Integer tocDepth;
    private String highlightStyle;
    private List<Path> headers;
    private List<Path> beforeBody;
    private List<Path> afterBody;

    protected static OutputConfiguration buildDefaultConfiguration() {
        OutputConfiguration configuration = new OutputConfiguration() {
        };

        configuration.setSmart(true);
        configuration.setBaseLevelHeaders(1);
        configuration.setFileScope(false);
        configuration.setNormalize(true);
        configuration.setPreserveTabs(false);
        configuration.setTabStop(4);
        configuration.setStandalone(true);
        configuration.setWrap(WrapStyle.AUTO);
        configuration.setColumns(80);
        configuration.setToc(true);
        configuration.setHeaders(new ArrayList<>());
        configuration.setBeforeBody(new ArrayList<>());
        configuration.setAfterBody(new ArrayList<>());

        return configuration;
    }

    /**
     * Treat top-level headers as chapters in LaTeX, ConTeXt, and DocBook output. When the LaTeX document class is set
     * to report, book, or memoir (unless the article option is specified), this option is implied.
     *
     * @return true if chapters are enabled
     */
    public Boolean getChapters() {
        return getOrParent(OutputConfiguration::getChapters, chapters, parent);
    }

    /**
     * Treat top-level headers as chapters in LaTeX, ConTeXt, and DocBook output. When the LaTeX document class is set
     * to report, book, or memoir (unless the article option is specified), this option is implied.
     *
     * @param chapters true if chapters should be enabled
     */
    public void setChapters(Boolean chapters) {
        this.chapters = chapters;
    }

    /**
     * Get the target file location or null if it is not defined.
     * This will default to {project name}.{format}.
     *
     * @return the target file
     */
    public Path getOutputFile() {
        return getOrParent(OutputConfiguration::getOutputFile, outputFile, parent);
    }

    /**
     * Set the target file location.
     * This will default to {project name}.{format}.
     *
     * @param outputFile the target file
     */
    public void setOutputFile(Path outputFile) {
        this.outputFile = outputFile;
    }

    /**
     * Produce typographically correct output, converting straight quotes to curly quotes, --- to em-dashes,
     * -- to en-dashes, and ... to ellipses. Nonbreaking spaces are inserted after certain abbreviations, such
     * as “Mr.” (Note: This option is selected automatically when the output format is latex or context,
     * unless {@link PDFOutputConfiguration#noTexLigatures} is used. It has no effect for latex input.)
     *
     * @return true if smart is enabled
     */
    public Boolean getSmart() {
        return getOrParent(OutputConfiguration::getSmart, smart, parent);
    }

    /**
     * Produce typographically correct output, converting straight quotes to curly quotes, --- to em-dashes,
     * -- to en-dashes, and ... to ellipses. Nonbreaking spaces are inserted after certain abbreviations, such
     * as “Mr.” (Note: This option is selected automatically when the output format is latex or context,
     * unless {@link PDFOutputConfiguration#noTexLigatures} is used. It has no effect for latex input.)
     *
     * @param smart true if smart should be enabled
     */
    public void setSmart(Boolean smart) {
        this.smart = smart;
    }

    /**
     * Specify a default extension to use when image paths/URLs have no extension. This allows you to use the same
     * source for formats that require different kinds of images. Currently this option only affects the
     * Markdown and LaTeX readers.
     *
     * @return the default image extension
     */
    public String getDefaultImageExtension() {
        return getOrParent(OutputConfiguration::getDefaultImageExtension, defaultImageExtension, parent);
    }

    /**
     * Specify a default extension to use when image paths/URLs have no extension. This allows you to use the same
     * source for formats that require different kinds of images. Currently this option only affects the
     * Markdown and LaTeX readers.
     *
     * @param defaultImageExtension the default image extension
     */
    public void setDefaultImageExtension(String defaultImageExtension) {
        this.defaultImageExtension = defaultImageExtension;
    }

    /**
     * Specify the base level for headers (defaults to 1).
     *
     * @return the base level
     */
    public Integer getBaseLevelHeaders() {
        return getOrParent(OutputConfiguration::getBaseLevelHeaders, baseLevelHeaders, parent);
    }

    /**
     * Specify the base level for headers (defaults to 1).
     *
     * @param baseLevelHeaders the base level
     */
    public void setBaseLevelHeaders(Integer baseLevelHeaders) {
        this.baseLevelHeaders = baseLevelHeaders;
    }

    /**
     * Parse each file individually before combining for multifile documents. This will allow footnotes in different
     * files with the same identifiers to work as expected. If this option is set, footnotes and links will not work
     * across files.
     *
     * @return true if file scope is enabled.
     */
    public Boolean getFileScope() {
        return getOrParent(OutputConfiguration::getFileScope, fileScope, parent);
    }

    /**
     * Parse each file individually before combining for multifile documents. This will allow footnotes in different
     * files with the same identifiers to work as expected. If this option is set, footnotes and links will not work
     * across files.
     *
     * @param fileScope true if file scope should be enabled.
     */
    public void setFileScope(Boolean fileScope) {
        this.fileScope = fileScope;
    }

    /**
     * Normalize the document after reading: merge adjacent Str or Emph elements, for example, and remove repeated
     * Spaces.
     *
     * @return true if normalization is enabled
     */
    public Boolean getNormalize() {
        return getOrParent(OutputConfiguration::getNormalize, normalize, parent);
    }

    /**
     * Normalize the document after reading: merge adjacent Str or Emph elements, for example, and remove repeated
     * Spaces.
     *
     * @param normalize false if normalization should be enabled
     */
    public void setNormalize(Boolean normalize) {
        this.normalize = normalize;
    }

    /**
     * Preserve tabs instead of converting them to spaces (the default). Note that this will only affect tabs in
     * literal code spans and code blocks; tabs in regular text will be treated as spaces.
     *
     * @return true if tabs are preserved
     */
    public Boolean getPreserveTabs() {
        return getOrParent(OutputConfiguration::getPreserveTabs, preserveTabs, parent);
    }

    /**
     * Preserve tabs instead of converting them to spaces (the default). Note that this will only affect tabs in
     * literal code spans and code blocks; tabs in regular text will be treated as spaces.
     *
     * @param preserveTabs true if tabs should be preserved
     */
    public void setPreserveTabs(Boolean preserveTabs) {
        this.preserveTabs = preserveTabs;
    }

    /**
     * Specify the number of spaces per tab (default is 4).
     *
     * @return the tab stop
     */
    public Integer getTabStop() {
        return getOrParent(OutputConfiguration::getTabStop, tabStop, parent);
    }

    /**
     * Specify the number of spaces per tab (default is 4).
     *
     * @param tabStop the tab stop
     */
    public void setTabStop(Integer tabStop) {
        this.tabStop = tabStop;
    }

    /**
     * Produce output with an appropriate header and footer (e.g. a standalone HTML, LaTeX, TEI, or RTF file, not a
     * fragment). This option is set automatically for pdf, epub, epub3, fb2, docx, and odt output.
     *
     * @return true if the output is standalone
     */
    public Boolean getStandalone() {
        return getOrParent(OutputConfiguration::getStandalone, standalone, parent);
    }

    /**
     * Produce output with an appropriate header and footer (e.g. a standalone HTML, LaTeX, TEI, or RTF file, not a
     * fragment). This option is set automatically for pdf, epub, epub3, fb2, docx, and odt output.
     *
     * @param standalone true if the output should be standalone
     */
    public void setStandalone(Boolean standalone) {
        this.standalone = standalone;
    }

    /**
     * Use FILE as a custom template for the generated document. Implies <pre>setStandalone(true)</pre>. If no extension
     * is specified, an extension corresponding to the writer will be
     * added, so that <pre>setTemplate("special")</pre> looks for special.html for HTML output. If the template is not found,
     * pandoc will search for it in the templates subdirectory of the user data directory. If this
     * option is not used, a default template appropriate for the output format will be
     * used.
     *
     * @return the template path
     */
    public Path getTemplate() {
        return getOrParent(OutputConfiguration::getTemplate, template, parent);
    }

    /**
     * Use FILE as a custom template for the generated document. Implies <pre>setStandalone(true)</pre>. If no extension
     * is specified, an extension corresponding to the writer will be
     * added, so that <pre>setTemplate("special")</pre> looks for special.html for HTML output. If the template is not found,
     * pandoc will search for it in the templates subdirectory of the user data directory. If this
     * option is not used, a default template appropriate for the output format will be
     * used.
     *
     * @param template the template path
     */
    public void setTemplate(Path template) {
        this.template = template;
    }

    /**
     * Determine how text is wrapped in the output (the source code, not the rendered version). With
     * {@link WrapStyle#AUTO} (the default), pandoc will attempt to wrap lines to the column width specified by
     * {@link #setColumns(Integer)} (default 80). With {@link WrapStyle#NONE}, pandoc will not wrap lines at all.
     * With {@link WrapStyle#PRESERVE}, pandoc will attempt to preserve the wrapping from the source document
     * (that is, where there are nonsemantic newlines in the source, there will be nonsemantic newlines in the
     * output as well).
     *
     * @return the wrapping style
     */
    public WrapStyle getWrap() {
        return getOrParent(OutputConfiguration::getWrap, wrap, parent);
    }

    /**
     * Determine how text is wrapped in the output (the source code, not the rendered version). With
     * {@link WrapStyle#AUTO} (the default), pandoc will attempt to wrap lines to the column width specified by
     * {@link #setColumns(Integer)} (default 80). With {@link WrapStyle#NONE}, pandoc will not wrap lines at all.
     * With {@link WrapStyle#PRESERVE}, pandoc will attempt to preserve the wrapping from the source document
     * (that is, where there are nonsemantic newlines in the source, there will be nonsemantic newlines in the
     * output as well).
     *
     * @param wrap the wrapping style
     */
    public void setWrap(WrapStyle wrap) {
        this.wrap = wrap;
    }

    /**
     * Specify length of lines in characters. This affects text wrapping in the generated source code (see
     * {@link #setWrap(WrapStyle)}). It also affects calculation of column widths for plain text tables.
     *
     * @return the number of columns
     */
    public Integer getColumns() {
        return getOrParent(OutputConfiguration::getColumns, columns, parent);
    }

    /**
     * Specify length of lines in characters. This affects text wrapping in the generated source code (see
     * {@link #setWrap(WrapStyle)}). It also affects calculation of column widths for plain text tables.
     *
     * @param columns the number of columns
     */
    public void setColumns(Integer columns) {
        this.columns = columns;
    }

    /**
     * Include an automatically generated table of contents in the output document.
     *
     * @return true if toc is enabled
     */
    public Boolean getToc() {
        return getOrParent(OutputConfiguration::getToc, toc, parent);
    }

    /**
     * Include an automatically generated table of contents in the output document.
     *
     * @param toc true if toc should be enabled
     */
    public void setToc(Boolean toc) {
        this.toc = toc;
    }

    /**
     * Specify the number of section levels to include in the table of contents. The default is 3 (which means that
     * level 1, 2, and 3 headers will be listed in the contents).
     *
     * @return the toc depth
     */
    public Integer getTocDepth() {
        return getOrParent(OutputConfiguration::getTocDepth, tocDepth, parent);
    }

    /**
     * Specify the number of section levels to include in the table of contents. The default is 3 (which means that
     * level 1, 2, and 3 headers will be listed in the contents).
     *
     * @param tocDepth the toc depth
     */
    public void setTocDepth(Integer tocDepth) {
        this.tocDepth = tocDepth;
    }

    /**
     * Specifies the coloring style to be used in highlighted source code.
     *
     * @return the highlight style
     */
    public String getHighlightStyle() {
        return getOrParent(OutputConfiguration::getHighlightStyle, highlightStyle, parent);
    }

    /**
     * Specifies the coloring style to be used in highlighted source code.
     *
     * @param highlightStyle the highlight style
     */
    public void setHighlightStyle(String highlightStyle) {
        this.highlightStyle = highlightStyle;
    }

    /**
     * Include contents of files, verbatim, at the end of the header. This can be used, for example, to include special
     * CSS or javascript in HTML documents.They will be included in the order specified. Implies <pre>setStandalone(true)</pre>.
     *
     * @return the headers
     */
    public List<Path> getHeaders() {
        return getOrParentList(headers, parent == null ? null : parent.getHeaders());
    }

    /**
     * Include contents of files, verbatim, at the end of the header. This can be used, for example, to include special
     * CSS or javascript in HTML documents.They will be included in the order specified. Implies <pre>setStandalone(true)</pre>.
     *
     * @param headers the headers
     */
    public void setHeaders(List<Path> headers) {
        this.headers = headers;
    }

    /**
     * Include contents of files, verbatim, at the beginning of the document body (e.g. after the {@code <body>} tag in
     * HTML, or the \begin{document} command in LaTeX). Implies <pre>setStandalone(true)</pre>.
     *
     * @return the files
     */
    public List<Path> getBeforeBody() {
        return getOrParentList(beforeBody, parent == null ? null : parent.getBeforeBody());
    }

    /**
     * Include contents of files, verbatim, at the beginning of the document body (e.g. after the {@code <body>} tag in HTML,
     * or the \begin{document} command in LaTeX). Implies <pre>setStandalone(true)</pre>.
     *
     * @param beforeBody the files
     */
    public void setBeforeBody(List<Path> beforeBody) {
        this.beforeBody = beforeBody;
    }

    /**
     * Include contents of files, verbatim, at the end of the document body (before the {@code </body>} tag in HTML, or the
     * \end{document} command in LaTeX).  Implies <pre>setStandalone(true)</pre>.
     *
     * @return the files
     */
    public List<Path> getAfterBody() {
        return getOrParentList(afterBody, parent == null ? null : parent.getAfterBody());
    }

    /**
     * Include contents of files, verbatim, at the end of the document body (before the {@code </body>} tag in HTML, or the
     * \end{document} command in LaTeX).  Implies <pre>setStandalone(true)</pre>.
     *
     * @param afterBody the files
     */
    public void setAfterBody(List<Path> afterBody) {
        this.afterBody = afterBody;
    }

    private List<Path> getOrParentList(List<Path> mine, List<Path> parent) {
        if (parent == null) {
            return mine;
        }
        List<Path> result = new ArrayList<>(parent);
        if (mine != null) {
            result.addAll(mine);
        }
        return result;
    }

    protected <T> T getOrParent(Function<OutputConfiguration, T> getter, T thisValue, OutputConfiguration parent) {
        if (parent == null || thisValue != null) {
            return thisValue;
        }
        return getter.apply(parent);
    }

    /**
     * Set the parent configuration for this configuration.
     *
     * @param parent the parent
     */
    public void attachParentConfiguration(OutputConfiguration parent) {
        this.parent = parent;
    }

}
