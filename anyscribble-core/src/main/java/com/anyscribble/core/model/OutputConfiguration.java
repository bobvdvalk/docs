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
import java.util.List;
import java.util.Map;

public class OutputConfiguration {
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
    private WrapStyle warp;
    private Integer columns;
    private Boolean toc;
    private Integer tocDepth;
    private String highlightStyle;
    private List<Path> headers;
    private List<Path> beforeBody;
    private List<Path> afterBody;

    public Path getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(Path outputFile) {
        this.outputFile = outputFile;
    }

    public Boolean getSmart() {
        return smart;
    }

    public void setSmart(Boolean smart) {
        this.smart = smart;
    }

    public String getDefaultImageExtension() {
        return defaultImageExtension;
    }

    public void setDefaultImageExtension(String defaultImageExtension) {
        this.defaultImageExtension = defaultImageExtension;
    }

    public Integer getBaseLevelHeaders() {
        return baseLevelHeaders;
    }

    public void setBaseLevelHeaders(Integer baseLevelHeaders) {
        this.baseLevelHeaders = baseLevelHeaders;
    }

    public Boolean getFileScope() {
        return fileScope;
    }

    public void setFileScope(Boolean fileScope) {
        this.fileScope = fileScope;
    }

    public Boolean getNormalize() {
        return normalize;
    }

    public void setNormalize(Boolean normalize) {
        this.normalize = normalize;
    }

    public Boolean getPreserveTabs() {
        return preserveTabs;
    }

    public void setPreserveTabs(Boolean preserveTabs) {
        this.preserveTabs = preserveTabs;
    }

    public Integer getTabStop() {
        return tabStop;
    }

    public void setTabStop(Integer tabStop) {
        this.tabStop = tabStop;
    }

    public Boolean getStandalone() {
        return standalone;
    }

    public void setStandalone(Boolean standalone) {
        this.standalone = standalone;
    }

    public Path getTemplate() {
        return template;
    }

    public void setTemplate(Path template) {
        this.template = template;
    }

    public Map<String, String> getTemplateConfig() {
        return templateConfig;
    }

    public void setTemplateConfig(Map<String, String> templateConfig) {
        this.templateConfig = templateConfig;
    }

    public WrapStyle getWarp() {
        return warp;
    }

    public void setWarp(WrapStyle warp) {
        this.warp = warp;
    }

    public Integer getColumns() {
        return columns;
    }

    public void setColumns(Integer columns) {
        this.columns = columns;
    }

    public Boolean getToc() {
        return toc;
    }

    public void setToc(Boolean toc) {
        this.toc = toc;
    }

    public Integer getTocDepth() {
        return tocDepth;
    }

    public void setTocDepth(Integer tocDepth) {
        this.tocDepth = tocDepth;
    }

    public String getHighlightStyle() {
        return highlightStyle;
    }

    public void setHighlightStyle(String highlightStyle) {
        this.highlightStyle = highlightStyle;
    }

    public List<Path> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Path> headers) {
        this.headers = headers;
    }

    public List<Path> getBeforeBody() {
        return beforeBody;
    }

    public void setBeforeBody(List<Path> beforeBody) {
        this.beforeBody = beforeBody;
    }

    public List<Path> getAfterBody() {
        return afterBody;
    }

    public void setAfterBody(List<Path> afterBody) {
        this.afterBody = afterBody;
    }
}
