/**
 * AnyScribble Core - Writing for Developers by Developers
 * Copyright © 2016 Thomas Biesaart (thomas.biesaart@gmail.com)
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
package com.anyscribble.core.model;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * This class represents the base configuration for any build target.
 *
 * @author Thomas Biesaart
 */
public class OutputConfiguration {
    protected OutputConfiguration parent;
    private Path outputFile;
    private Boolean smart;
    private String defaultImageExtension;
    private Integer baseLevelHeaders;
    private Boolean fileScope;
    private Boolean normalize;
    private Boolean preserveTabs;
    private Integer tabStop;
    private Boolean standalone;
    private Path template;
    private Map<String, String> templateConfig;
    private WrapStyle wrap;
    private Integer columns;
    private Boolean toc;
    private Integer tocDepth;
    private String highlightStyle;
    private List<Path> headers;
    private List<Path> beforeBody;
    private List<Path> afterBody;

    public Path getOutputFile() {
        return getOrParent(OutputConfiguration::getOutputFile, outputFile, parent);
    }

    public void setOutputFile(Path outputFile) {
        this.outputFile = outputFile;
    }

    public Boolean getSmart() {
        return getOrParent(OutputConfiguration::getSmart, smart, parent);
    }

    public void setSmart(Boolean smart) {
        this.smart = smart;
    }

    public String getDefaultImageExtension() {
        return getOrParent(OutputConfiguration::getDefaultImageExtension, defaultImageExtension, parent);
    }

    public void setDefaultImageExtension(String defaultImageExtension) {
        this.defaultImageExtension = defaultImageExtension;
    }

    public Integer getBaseLevelHeaders() {
        return getOrParent(OutputConfiguration::getBaseLevelHeaders, baseLevelHeaders, parent);
    }

    public void setBaseLevelHeaders(Integer baseLevelHeaders) {
        this.baseLevelHeaders = baseLevelHeaders;
    }

    public Boolean getFileScope() {
        return getOrParent(OutputConfiguration::getFileScope, fileScope, parent);
    }

    public void setFileScope(Boolean fileScope) {
        this.fileScope = fileScope;
    }

    public Boolean getNormalize() {
        return getOrParent(OutputConfiguration::getNormalize, normalize, parent);
    }

    public void setNormalize(Boolean normalize) {
        this.normalize = normalize;
    }

    public Boolean getPreserveTabs() {
        return getOrParent(OutputConfiguration::getPreserveTabs, preserveTabs, parent);
    }

    public void setPreserveTabs(Boolean preserveTabs) {
        this.preserveTabs = preserveTabs;
    }

    public Integer getTabStop() {
        return getOrParent(OutputConfiguration::getTabStop, tabStop, parent);
    }

    public void setTabStop(Integer tabStop) {
        this.tabStop = tabStop;
    }

    public Boolean getStandalone() {
        return getOrParent(OutputConfiguration::getStandalone, standalone, parent);
    }

    public void setStandalone(Boolean standalone) {
        this.standalone = standalone;
    }

    public Path getTemplate() {
        return getOrParent(OutputConfiguration::getTemplate, template, parent);
    }

    public void setTemplate(Path template) {
        this.template = template;
    }

    public Map<String, String> getTemplateConfig() {
        return getOrParent(OutputConfiguration::getTemplateConfig, templateConfig, parent);
    }

    public void setTemplateConfig(Map<String, String> templateConfig) {
        this.templateConfig = templateConfig;
    }

    public WrapStyle getWrap() {
        return getOrParent(OutputConfiguration::getWrap, wrap, parent);
    }

    public void setWrap(WrapStyle wrap) {
        this.wrap = wrap;
    }

    public Integer getColumns() {
        return getOrParent(OutputConfiguration::getColumns, columns, parent);
    }

    public void setColumns(Integer columns) {
        this.columns = columns;
    }

    public Boolean getToc() {
        return getOrParent(OutputConfiguration::getToc, toc, parent);
    }

    public void setToc(Boolean toc) {
        this.toc = toc;
    }

    public Integer getTocDepth() {
        return getOrParent(OutputConfiguration::getTocDepth, tocDepth, parent);
    }

    public void setTocDepth(Integer tocDepth) {
        this.tocDepth = tocDepth;
    }

    public String getHighlightStyle() {
        return getOrParent(OutputConfiguration::getHighlightStyle, highlightStyle, parent);
    }

    public void setHighlightStyle(String highlightStyle) {
        this.highlightStyle = highlightStyle;
    }

    public List<Path> getHeaders() {
        return getOrParentList(headers, parent == null ? null : parent.getHeaders());
    }

    public void setHeaders(List<Path> headers) {
        this.headers = headers;
    }

    public List<Path> getBeforeBody() {
        return getOrParentList(beforeBody, parent == null ? null : parent.getBeforeBody());
    }

    public void setBeforeBody(List<Path> beforeBody) {
        this.beforeBody = beforeBody;
    }

    public List<Path> getAfterBody() {
        return getOrParentList(afterBody, parent == null ? null : parent.getAfterBody());
    }

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
        if (thisValue != null) {
            return thisValue;
        }
        if (parent != null) {
            return getter.apply(parent);
        }
        return null;
    }

    public void attachParentConfiguration(OutputConfiguration parent) {
        this.parent = parent;
    }

}
