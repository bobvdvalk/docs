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
    private boolean smart = true;
    private String defaultImageExtension;
    private int baseLevelHeaders;
    private boolean fileScope;
    private boolean normalize;
    private boolean preserveTabs;
    private int tabStop;
    private boolean standalone;
    private Path template;
    private Map<String, String> templateConfig;
    private WrapStyle warp;
    private int columns;
    private boolean toc;
    private int tocDepth;
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

    public boolean isSmart() {
        return smart;
    }

    public void setSmart(boolean smart) {
        this.smart = smart;
    }

    public String getDefaultImageExtension() {
        return defaultImageExtension;
    }

    public void setDefaultImageExtension(String defaultImageExtension) {
        this.defaultImageExtension = defaultImageExtension;
    }

    public int getBaseLevelHeaders() {
        return baseLevelHeaders;
    }

    public void setBaseLevelHeaders(int baseLevelHeaders) {
        this.baseLevelHeaders = baseLevelHeaders;
    }

    public boolean isFileScope() {
        return fileScope;
    }

    public void setFileScope(boolean fileScope) {
        this.fileScope = fileScope;
    }

    public boolean isNormalize() {
        return normalize;
    }

    public void setNormalize(boolean normalize) {
        this.normalize = normalize;
    }

    public boolean isPreserveTabs() {
        return preserveTabs;
    }

    public void setPreserveTabs(boolean preserveTabs) {
        this.preserveTabs = preserveTabs;
    }

    public int getTabStop() {
        return tabStop;
    }

    public void setTabStop(int tabStop) {
        this.tabStop = tabStop;
    }

    public boolean isStandalone() {
        return standalone;
    }

    public void setStandalone(boolean standalone) {
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

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public boolean isToc() {
        return toc;
    }

    public void setToc(boolean toc) {
        this.toc = toc;
    }

    public int getTocDepth() {
        return tocDepth;
    }

    public void setTocDepth(int tocDepth) {
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
