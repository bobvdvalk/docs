/**
 * AnyScribble Editor - Writing for Developers by Developers
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
package com.anyscribble.ide.editor;

import com.google.common.base.CaseFormat;
import com.google.inject.Inject;
import org.fxmisc.richtext.CodeArea;
import org.pegdown.PegDownProcessor;
import org.pegdown.ast.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

public class SyntaxHighlighter implements Visitor {
    private final CodeArea codeArea;
    private final PegDownProcessor processor;
    private final Deque<String> styleStack = new ArrayDeque<>();

    @Inject
    public SyntaxHighlighter(CodeArea codeArea, PegDownProcessor processor) {
        this.codeArea = codeArea;
        this.processor = processor;
    }

    public synchronized void update() {
        try {
            String text = codeArea.getText();
            RootNode rootNode = processor.parseMarkdown(text.toCharArray());
            codeArea.clearStyle(0, text.length());
            rootNode.accept(this);
        } finally {
            styleStack.clear();
        }
    }

    @Override
    public void visit(AbbreviationNode abbreviationNode) {
        generalVisit(abbreviationNode);
    }

    @Override
    public void visit(AnchorLinkNode anchorLinkNode) {
        generalVisit(anchorLinkNode);
    }

    @Override
    public void visit(AutoLinkNode autoLinkNode) {
        generalVisit(autoLinkNode);
    }

    @Override
    public void visit(BlockQuoteNode blockQuoteNode) {
        generalVisit(blockQuoteNode);
    }

    @Override
    public void visit(BulletListNode bulletListNode) {
        generalVisit(bulletListNode);
    }

    @Override
    public void visit(CodeNode codeNode) {
        generalVisit(codeNode);
    }

    @Override
    public void visit(DefinitionListNode definitionListNode) {
        generalVisit(definitionListNode);
    }

    @Override
    public void visit(DefinitionNode definitionNode) {
        generalVisit(definitionNode);
    }

    @Override
    public void visit(DefinitionTermNode definitionTermNode) {
        generalVisit(definitionTermNode);
    }

    @Override
    public void visit(ExpImageNode expImageNode) {
        generalVisit(expImageNode);
    }

    @Override
    public void visit(ExpLinkNode expLinkNode) {
        generalVisit(expLinkNode);
    }

    @Override
    public void visit(HeaderNode headerNode) {
        generalVisit(headerNode, "header" + headerNode.getLevel());
    }

    @Override
    public void visit(HtmlBlockNode htmlBlockNode) {
        generalVisit(htmlBlockNode);
    }

    @Override
    public void visit(InlineHtmlNode inlineHtmlNode) {
        generalVisit(inlineHtmlNode);
    }

    @Override
    public void visit(ListItemNode listItemNode) {
        generalVisit(listItemNode);
    }

    @Override
    public void visit(MailLinkNode mailLinkNode) {
        generalVisit(mailLinkNode);
    }

    @Override
    public void visit(OrderedListNode orderedListNode) {
        generalVisit(orderedListNode);
    }

    @Override
    public void visit(ParaNode paraNode) {
        generalVisit(paraNode);
    }

    @Override
    public void visit(QuotedNode quotedNode) {
        generalVisit(quotedNode);
    }

    @Override
    public void visit(ReferenceNode referenceNode) {
        generalVisit(referenceNode);
    }

    @Override
    public void visit(RefImageNode refImageNode) {
        generalVisit(refImageNode);
    }

    @Override
    public void visit(RefLinkNode refLinkNode) {
        generalVisit(refLinkNode);
    }

    @Override
    public void visit(RootNode rootNode) {
        generalVisit(rootNode);
    }

    @Override
    public void visit(SimpleNode simpleNode) {
        generalVisit(simpleNode);
    }

    @Override
    public void visit(SpecialTextNode specialTextNode) {
        generalVisit(specialTextNode);
    }

    @Override
    public void visit(StrikeNode strikeNode) {
        generalVisit(strikeNode);
    }

    @Override
    public void visit(StrongEmphSuperNode strongEmphSuperNode) {
        generalVisit(strongEmphSuperNode, strongEmphSuperNode.isStrong() ? "strong" : "emphasis");
    }

    @Override
    public void visit(TableBodyNode tableBodyNode) {
        generalVisit(tableBodyNode);
    }

    @Override
    public void visit(TableCaptionNode tableCaptionNode) {
        generalVisit(tableCaptionNode);
    }

    @Override
    public void visit(TableCellNode tableCellNode) {
        generalVisit(tableCellNode);
    }

    @Override
    public void visit(TableColumnNode tableColumnNode) {
        generalVisit(tableColumnNode);
    }

    @Override
    public void visit(TableHeaderNode tableHeaderNode) {
        generalVisit(tableHeaderNode);
    }

    @Override
    public void visit(TableNode tableNode) {
        generalVisit(tableNode);
    }

    @Override
    public void visit(TableRowNode tableRowNode) {
        generalVisit(tableRowNode);
    }

    @Override
    public void visit(VerbatimNode verbatimNode) {
        generalVisit(verbatimNode);
    }

    @Override
    public void visit(WikiLinkNode wikiLinkNode) {
        generalVisit(wikiLinkNode);
    }

    @Override
    public void visit(TextNode textNode) {
        generalVisit(textNode);
    }

    @Override
    public void visit(SuperNode superNode) {
        generalVisit(superNode);
    }

    @Override
    public void visit(Node node) {
        generalVisit(node);
    }

    private void generalVisit(Node node, String... classes) {

        // Put the styles on the stack
        for (int i = 0; i < classes.length; i++) {
            styleStack.push(classes[i]);
        }
        String nodeStyleName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, node.getClass().getSimpleName());
        styleStack.push(nodeStyleName);


        // Set the style
        int start = node.getStartIndex();
        int end = node.getEndIndex();
        codeArea.setStyle(start, end, new ArrayList<>(styleStack));

        // Process children
        for (Node child : node.getChildren()) {
            child.accept(this);
            //end = child.getEndIndex();
        }

        // Remove from style stack
        styleStack.pop();
        for (int i = classes.length - 1; i >= 0; i--) {
            styleStack.pop();
        }
    }
}
