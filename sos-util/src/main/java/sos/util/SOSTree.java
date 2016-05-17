package sos.util;

import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** @author Titus Meyer */
public class SOSTree {

    private HttpServletRequest request = null;
    private HttpServletResponse response = null;
    private Writer out = null;
    private String site = null;
    private SOSTreeElement root = null;
    private SOSTreeContentHandler handler = null;
    private String indent = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
    private String space = "&nbsp;&nbsp;";
    private boolean showRoot = true;
    private boolean textOpenNode = false;
    private boolean nodeOpenNode = true;
    private boolean postfixOpenNode = false;
    private boolean closeLowerNodes = true;
    private boolean subTables = false;
    private String cssClass = "tree";
    private boolean disabled = false;
    private String imgDir = "images/";
    private String imgOpenNode = "openFolder.gif\" border=\"0";
    private String imgCloseNode = "closeFolder.gif\" border=\"0";
    private String imgLeaf = "leaf.gif\" border=\"0";
    private String separating = ";";
    private String tdWidth = "400";
    private ArrayList tableHead = null;
    private String openNode = null;
    private String closeNode = null;
    private String activeStyle = null;
    private boolean autoFlush = true;

    public SOSTree() {
        init();
    }

    public SOSTree(HttpServletRequest request, HttpServletResponse response, Writer out) {
        this();
        this.request = request;
        this.response = response;
        this.out = out;
    }

    public void setRequest(HttpServletRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("setRequest: parameter request is null");
        }
        this.request = request;
        this.site = request.getRequestURI();
    }

    public void setResponse(HttpServletResponse response) {
        if (response == null) {
            throw new IllegalArgumentException("setResponse: parameter response is null");
        }
        this.response = response;
    }

    public void setOut(Writer out) {
        if (out == null) {
            throw new IllegalArgumentException("setOut: parameter out is null");
        }
        this.out = out;
    }

    public void setContentHandler(SOSTreeContentHandler handler) {
        if (handler == null) {
            throw new IllegalArgumentException("setContentHandler: parameter handler is null");
        }
        this.handler = handler;
    }

    public void setRootVisible(boolean visible) {
        this.showRoot = visible;
    }

    public boolean isRootVisible() {
        return showRoot;
    }

    public void setRootName(String name) {
        this.root.setName(name);
    }

    public void setRootTitle(String title) {
        this.root.setTitle(title);
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public String getCssClass() {
        return cssClass;
    }

    public void setImgDir(String imgDir) {
        this.imgDir = imgDir;
    }

    public String getImgDir() {
        return imgDir;
    }

    public void setImgOpenNode(String img) {
        this.imgOpenNode = img;
    }

    public String getImgOpenNode() {
        return imgOpenNode;
    }

    public void setImgCloseNode(String img) {
        this.imgCloseNode = img;
    }

    public String getImgCloseNode() {
        return imgCloseNode;
    }

    public void setImgLeaf(String img) {
        this.imgLeaf = img;
    }

    public String getImgLeaf() {
        return imgLeaf;
    }

    public void setTextOpenNode(boolean textOpenNode) {
        this.textOpenNode = textOpenNode;
    }

    public boolean isTextOpenNode() {
        return textOpenNode;
    }

    public void setNodeOpenNode(boolean nodeOpenNode) {
        this.nodeOpenNode = nodeOpenNode;
    }

    public boolean isNodeOpenNode() {
        return nodeOpenNode;
    }

    public void setPostfixOpenNode(boolean postfixOpenNode) {
        this.postfixOpenNode = postfixOpenNode;
    }

    public boolean isPostfixOpenNode() {
        return postfixOpenNode;
    }

    public void setCloseLowerNodes(boolean closeLowerNodes) {
        this.closeLowerNodes = closeLowerNodes;
    }

    public boolean isCloseLowerNodes() {
        return closeLowerNodes;
    }

    public void setTdWidth(String width) {
        this.tdWidth = width;
    }

    public String getTdWidth() {
        return tdWidth;
    }

    public void setSubTables(boolean subTables) {
        this.subTables = subTables;
    }

    public boolean isSubTables() {
        return subTables;
    }

    public SOSTreeElement getRootNode() {
        return root;
    }

    public void setHeadline(ArrayList headline) {
        this.tableHead = headline;
    }

    public ArrayList getHeadline() {
        return tableHead;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isDisabled() {
        return disabled;
    }

    private void init() {
        root = new SOSTreeElement();
        root.setOpen(true);
        root.setId(0);
        root.setName("ROOT");
        root.setTitle("ROOT");
    }

    public void clear() {
        init();
    }

    public String get() throws Exception {
        if (request == null) {
            throw new NullPointerException("get: request is not set");
        }
        if (response == null) {
            throw new NullPointerException("get: response is not set");
        }
        if (handler == null) {
            throw new NullPointerException("get: handler is not set");
        }
        try {
            StringBuilder out = new StringBuilder();
            out.append("<!-- TreeView Start -->\n<table class=\"").append(disabled ? "dis" : "").append(cssClass).append("\">\n");
            if (tableHead != null) {
                out.append("<tr class=\"").append(disabled ? "dis" : "").append(cssClass).append("_head\">");
                for (int i = 0; i < tableHead.size(); i++) {
                    out.append("<th class=\"").append(disabled ? "dis" : "").append(cssClass).append("_head\">").append(tableHead.get(i)).append(
                            "</th>");
                }
                out.append("</tr>\n");
            }
            int indent = 0;
            if (showRoot) {
                out.append(formatElement(root, ""));
                indent++;
            }
            if (root.isOpen()) {
                out.append(printNodeContent(root, indent));
            }
            out.append("</table>\n<!-- TreeView End -->\n");
            return out.toString();
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        }
    }

    public void show() throws Exception {
        if (out == null) {
            throw new NullPointerException("show: out is not set");
        }
        process();
        out.write(get());
        if (autoFlush) {
            out.flush();
        }
    }

    public void print() throws Exception {
        if (out == null) {
            throw new NullPointerException("print: out is not set");
        }
        out.write(get());
        if (autoFlush) {
            out.flush();
        }
    }

    public void process() throws Exception {
        openNode = request.getParameter("openNode");
        closeNode = request.getParameter("closeNode");
        if (showRoot) {
            toggleNode(root);
        }
        process(root);
    }

    private void process(SOSTreeElement node) throws Exception {
        if (node.isOpen()) {
            fillNode(node);
        }
        SOSTreeElement current = node.getList();
        while (current != null) {
            if (current.isNode()) {
                toggleNode(current);
            }
            if (current.isOpen()) {
                process(current);
            }
            current = current.getNext();
        }
    }

    private String printNodeContent(SOSTreeElement node, int level) throws Exception {
        if (node == null) {
            throw new IllegalArgumentException("printElement: parameter node is null");
        }
        if (node.isLeaf()) {
            throw new IllegalArgumentException("printElement: parameter node is set as leaf");
        }
        try {
            StringBuilder indent = new StringBuilder();
            for (int i = 0; i < level; i++) {
                indent.append(indent);
            }
            SOSTreeElement current = node.getList();
            StringBuilder out = new StringBuilder();
            if (subTables) {
                out.append("<tr class=\"").append(disabled ? "dis" : "").append(cssClass).append("\"><td colspan=\"10\" class=\"").append(
                        disabled ? "dis" : "").append(cssClass).append("\"><table class=\"").append(disabled ? "dis" : "").append(cssClass).append(
                        "\">");
            }
            while (current != null) {
                out.append(formatElement(current, indent.toString()));
                if (current.isOpen()) {
                    out.append(printNodeContent(current, level + 1));
                }
                current = current.getNext();
            }
            if (subTables) {
                out.append("</table></td></tr>");
            }
            return out.toString();
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        }
    }

    private String formatElement(SOSTreeElement element, String indent) {
        if (element == null || indent == null) {
            throw new IllegalArgumentException("getElementStr: parameter element or indent is null");
        }
        String cssClass = (element.getCssClass() != null) ? element.getCssClass() : this.cssClass;
        String imgDir = (element.getImgDir() != null) ? element.getImgDir() : this.imgDir;
        String imgOpenNode = (element.getImgOpenNode() != null) ? element.getImgOpenNode() : this.imgOpenNode;
        String imgCloseNode = (element.getImgCloseNode() != null) ? element.getImgCloseNode() : this.imgCloseNode;
        String imgLeaf = (element.getImgLeaf() != null) ? element.getImgLeaf() : this.imgLeaf;
        String hrefName = (element.getHrefName() != null) ? element.getHrefName() : "";
        String hrefAnker = (element.getHrefAnker() != null) ? element.getHrefAnker() : "";
        String activStyle = (element.getActivStyle() != null) ? element.getActivStyle() : "";
        StringBuilder str = new StringBuilder();
        String url = formatURL(element, true);
        StringBuilder image = new StringBuilder();
        image.append("<img src=\"").append(imgDir);
        if (element.isLeaf()) {
            image.append(imgLeaf);
        } else if (element.isOpen()) {
            image.append(imgOpenNode);
        } else {
            image.append(imgCloseNode);
        }
        image.append("\">");
        element.setActivStyle(null);
        str.append("<tr class=\"").append(disabled ? "dis" : "").append(cssClass).append("\"><td class=\"").append(disabled ? "dis" : "").append(
                cssClass).append("\"").append(tdWidth != null ? " width=\"" + tdWidth + "\"" : "").append(">").append(indent);
        if (element.isLeaf() && (element.getImgLeaf() == null || !"".equals(element.getImgLeaf()))) {
            str.append(image).append(space);
        }
        if (nodeOpenNode && element.isNode()) {
            str.append("<a ").append(hrefName).append(" class=\"").append(disabled ? "dis" : "").append(cssClass).append("\" href=\"").append(url).append(
                    hrefAnker).append("\">").append(image).append("</a>").append(space);
        } else if (element.getURL() != null && element.isNode()) {
            str.append("<a ").append(hrefName).append(" class=\"").append(disabled ? "dis" : "").append(cssClass).append("\" href=\"").append(
                    formatURL(element, false)).append(hrefAnker).append("\">").append(image).append("</a>").append(space);
        } else if (element.isNode()) {
            str.append(image).append(space);
        }
        if (element.getPrefix() != null && !"".equals(element.getPrefix())) {
            str.append(element.getPrefix()).append(space);
        }
        if (textOpenNode && element.isNode()) {
            str.append("<a ").append(activStyle).append(" ").append(hrefName).append(" class=\"").append(disabled ? "dis" : "").append(cssClass)
                .append("\" href=\"").append(url).append(hrefAnker).append("\">").append(element.getTitle()).append("</a>");
        } else if (element.getURL() != null) {
            str.append("<a ").append(activStyle).append(" ").append(hrefName).append(" class=\"").append(disabled ? "dis" : "").append(cssClass)
                .append("\" href=\"").append(formatURL(element, false)).append(hrefAnker).append("\">").append(element.getTitle()).append("</a>");
        } else {
            if (activStyle != null && !activStyle.isEmpty()) {
                str.append("<font ").append(activStyle).append(">").append(element.getTitle()).append("</font>");
            } else {
                str.append(element.getTitle());
            }
        }
        str.append("</td>");
        if (element.getPostfix() != null) {
            str.append("<td class=\"").append(disabled ? "dis" : "").append(cssClass).append("\">");
            if (element.isNode() && postfixOpenNode) {
                str.append("<a ").append(hrefName).append(" class=\"").append(disabled ? "dis" : "").append(cssClass).append("\"href=\"").append(url).append(
                        hrefAnker).append("\">").append(element.getPostfix()).append("</a>");
            } else {
                str.append(element.getPostfix());
            }
            str.append("</td>");
        }
        str.append("</tr>");
        str.append("\n");
        return str.toString();
    }

    private String formatURL(SOSTreeElement element, boolean openClose) {
        if (element == null) {
            throw new IllegalArgumentException("formatURL: parameter element is null");
        }
        StringBuilder url = new StringBuilder();
        String and = "";
        if (openClose) {
            url.append(site).append("?").append(element.isOpen() ? "closeNode=" + element.getPath() : "openNode=" + element.getPath());
            and = "&";
        } else {
            url.append(element.getURL());
            if (url.indexOf("?") < 0) {
                url.append("?");
            } else {
                and = "&";
            }
        }
        HashMap parameters = element.getParameters();
        if (parameters != null) {
            for (Iterator it = parameters.keySet().iterator(); it.hasNext();) {
                String name = (String) it.next();
                url.append(and).append(name).append("=").append((String) parameters.get(name));
                and = "&";
            }
        }
        return response.encodeURL(url.toString());
    }

    private void toggleNode(SOSTreeElement node) {
        if (node == null) {
            throw new IllegalArgumentException("toggleNode: parameter node is null");
        }
        if (node.isLeaf()) {
            throw new IllegalArgumentException("toggleNode: parameter node is not set as node");
        }
        if (node.isNode() && openNode != null && openNode.equals(node.getPath())) {
            node.setOpen(true);
            node.setActivStyle(this.activeStyle);
        }
        if (node.isNode() && closeNode != null && closeNode.equals(node.getPath())) {
            node.setOpen(false);
            node.setActivStyle(this.activeStyle);
            if (closeLowerNodes) {
                closeLowerNodes(node);
            }
        }
    }

    private void fillNode(SOSTreeElement node) throws Exception {
        if (node == null) {
            throw new IllegalArgumentException("fillNode: parameter node is null");
        }
        if (node.isLeaf()) {
            throw new IllegalArgumentException("fillNode: parameter node is not set as node");
        }
        if (handler == null) {
            throw new NullPointerException("fillNode: content handler is null");
        }
        if (node.isNew()) {
            handler.startNode(node);
            SOSTreeElement newElement = null;
            newElement = handler.newElement(node, newElement);
            while (newElement != null) {
                node.insert(newElement);
                newElement = handler.newElement(node, newElement);
            }
            node.setNew(false);
        }
    }

    private void closeLowerNodes(SOSTreeElement node) {
        if (node == null) {
            throw new IllegalArgumentException("closeLowerNodes: parameter node is null");
        }
        if (node.isLeaf()) {
            throw new IllegalArgumentException("closeLowerNodes: parameter node is not set as node");
        }
        SOSTreeElement current = node.getList();
        while (current != null) {
            if (!current.isLeaf()) {
                current.setOpen(false);
            }
            if (!current.isLeaf() && current.getList() != null) {
                closeLowerNodes(current);
            }
            current = current.getNext();
        }
    }

    public void openToElement(SOSTreeElement element) {
        if (element == null) {
            return;
        }
        SOSTreeElement parent = element.getParent();
        while (parent != null) {
            parent.setOpen(true);
            parent = parent.getParent();
        }
    }

    public SOSTreeElement getElementByIdPath(String path) throws Exception {
        return getElementByPath(root, path, false);
    }

    public SOSTreeElement getElementByNamePath(String namePath) throws Exception {
        return getElementByPath(root, namePath, true);
    }

    private SOSTreeElement getElementByPath(SOSTreeElement element, String path, boolean names) throws Exception {
        try {
            if (element == null || path == null) {
                return null;
            }
            int id = -1;
            String name = null;
            String[] paths = path.split(separating, 3);
            if (names) {
                name = paths[0].trim();
            } else {
                id = Integer.parseInt(paths[0].trim());
            }
            if (!names && id != element.getId()) {
                return null;
            } else if (names) {
                if (element.getName() == null) {
                    throw new Exception("TreeElement(" + element.getPath() + "): name is n + ");
                }
                if (!name.equals(element.getName())) {
                    return null;
                }
            }
            if (paths.length == 1) {
                return element;
            }
            fillNode(element);
            if (element.isLeaf()) {
                return element;
            }
            SOSTreeElement current = element.getList();
            while (current != null) {
                int id2 = -1;
                String name2 = null;
                if (names) {
                    name2 = paths[1].trim();
                } else {
                    id2 = Integer.parseInt(paths[1].trim());
                }
                if (names && current.getName() == null) {
                    throw new Exception("TreeElement(" + current.getPath() + "): name is null");
                }
                if ((names && name2.equals(current.getName())) || (!names && id2 == current.getId())) {
                    if (paths.length == 2) {
                        return current;
                    } else {
                        return getElementByPath(current, paths[1] + separating + paths[2], names);
                    }
                }
                if (!names && id2 < current.getId()) {
                    return null;
                }
                current = current.getNext();
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        }
        return null;
    }

    public SOSTreeElement getElementByName(String name) {
        return getElementByName(root, name);
    }

    private SOSTreeElement getElementByName(SOSTreeElement node, String name) {
        if (name == null || node == null) {
            return null;
        }
        if (node.isLeaf()) {
            throw new IllegalArgumentException("getElementByName: parameter node is not set as node");
        }
        SOSTreeElement current = node.getList();
        while (current != null) {
            if (current.getName() != null && current.getName().equals(name)) {
                return current;
            }
            if (current.isNode() && current.getList() != null) {
                SOSTreeElement ret = getElementByName(current, name);
                if (ret != null) {
                    return ret;
                }
            }
            current = current.getNext();
        }
        return null;
    }

    public String getActivStyle() {
        return activeStyle;
    }

    public void setActivStyle(String style) {
        this.activeStyle = style;
    }

    public boolean isAutoFlush() {
        return autoFlush;
    }

    public void setAutoFlush(boolean flush) {
        this.autoFlush = flush;
    }

}