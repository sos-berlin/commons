package sos.util;

import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** @author Titus Meyer */
public class SOSTree {

    private HttpServletRequest _request = null;
    private HttpServletResponse _response = null;
    private Writer _out = null;
    private String _site = null;
    private SOSTreeElement _root = null;
    private SOSTreeContentHandler _handler = null;
    private String _indent = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
    private String _space = "&nbsp;&nbsp;";
    private boolean _showRoot = true;
    private boolean _textOpenNode = false;
    private boolean _nodeOpenNode = true;
    private boolean _postfixOpenNode = false;
    private boolean _closeLowerNodes = true;
    private boolean _subTables = false;
    private String _cssClass = "tree";
    private boolean _disabled = false;
    private String _imgDir = "images/";
    private String _imgOpenNode = "openFolder.gif\" border=\"0";
    private String _imgCloseNode = "closeFolder.gif\" border=\"0";
    private String _imgLeaf = "leaf.gif\" border=\"0";
    private String _separating = ";";
    private String _tdWidth = "400";
    private ArrayList _tableHead = null;
    private String _openNode = null;
    private String _closeNode = null;
    private String _activStyle = null;
    private boolean _autoFlush = true;

    public SOSTree() {
        init();
    }

    public SOSTree(HttpServletRequest request, HttpServletResponse response, Writer out) {
        this();
        _request = request;
        _response = response;
        _out = out;
    }

    public void setRequest(HttpServletRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("setRequest: parameter request is null");
        }
        _request = request;
        _site = _request.getRequestURI();
    }

    public void setResponse(HttpServletResponse response) {
        if (response == null) {
            throw new IllegalArgumentException("setResponse: parameter response is null");
        }
        _response = response;
    }

    public void setOut(Writer out) {
        if (out == null) {
            throw new IllegalArgumentException("setOut: parameter out is null");
        }
        _out = out;
    }

    public void setContentHandler(SOSTreeContentHandler handler) {
        if (handler == null) {
            throw new IllegalArgumentException("setContentHandler: parameter handler is null");
        }
        _handler = handler;
    }

    public void setRootVisible(boolean visible) {
        _showRoot = visible;
    }

    public boolean isRootVisible() {
        return _showRoot;
    }

    public void setRootName(String name) {
        _root.setName(name);
    }

    public void setRootTitle(String title) {
        _root.setTitle(title);
    }

    public void setCssClass(String cssClass) {
        _cssClass = cssClass;
    }

    public String getCssClass() {
        return _cssClass;
    }

    public void setImgDir(String imgDir) {
        _imgDir = imgDir;
    }

    public String getImgDir() {
        return _imgDir;
    }

    public void setImgOpenNode(String img) {
        _imgOpenNode = img;
    }

    public String getImgOpenNode() {
        return _imgOpenNode;
    }

    public void setImgCloseNode(String img) {
        _imgCloseNode = img;
    }

    public String getImgCloseNode() {
        return _imgCloseNode;
    }

    public void setImgLeaf(String img) {
        _imgLeaf = img;
    }

    public String getImgLeaf() {
        return _imgLeaf;
    }

    public void setTextOpenNode(boolean textOpenNode) {
        _textOpenNode = textOpenNode;
    }

    public boolean isTextOpenNode() {
        return _textOpenNode;
    }

    public void setNodeOpenNode(boolean nodeOpenNode) {
        _nodeOpenNode = nodeOpenNode;
    }

    public boolean isNodeOpenNode() {
        return _nodeOpenNode;
    }

    public void setPostfixOpenNode(boolean postfixOpenNode) {
        _postfixOpenNode = postfixOpenNode;
    }

    public boolean isPostfixOpenNode() {
        return _postfixOpenNode;
    }

    public void setCloseLowerNodes(boolean closeLowerNodes) {
        _closeLowerNodes = closeLowerNodes;
    }

    public boolean isCloseLowerNodes() {
        return _closeLowerNodes;
    }

    public void setTdWidth(String width) {
        _tdWidth = width;
    }

    public String getTdWidth() {
        return _tdWidth;
    }

    public void setSubTables(boolean subTables) {
        _subTables = subTables;
    }

    public boolean isSubTables() {
        return _subTables;
    }

    public SOSTreeElement getRootNode() {
        return _root;
    }

    public void setHeadline(ArrayList headline) {
        _tableHead = headline;
    }

    public ArrayList getHeadline() {
        return _tableHead;
    }

    public void setDisabled(boolean disabled) {
        _disabled = disabled;
    }

    public boolean isDisabled() {
        return _disabled;
    }

    private void init() {
        _root = new SOSTreeElement();
        _root.setOpen(true);
        _root.setId(0);
        _root.setName("ROOT");
        _root.setTitle("ROOT");
    }

    public void clear() {
        init();
    }

    public String get() throws Exception {
        if (_request == null) {
            throw new NullPointerException("get: request is not set");
        }
        if (_response == null) {
            throw new NullPointerException("get: response is not set");
        }
        if (_handler == null) {
            throw new NullPointerException("get: handler is not set");
        }
        try {
            StringBuilder out = new StringBuilder();
            out.append("<!-- TreeView Start -->\n<table class=\"").append(_disabled ? "dis" : "").append(_cssClass).append("\">\n");
            if (_tableHead != null) {
                out.append("<tr class=\"").append(_disabled ? "dis" : "").append(_cssClass).append("_head\">");
                for (int i = 0; i < _tableHead.size(); i++) {
                    out.append("<th class=\"").append(_disabled ? "dis" : "").append(_cssClass).append("_head\">").append(_tableHead.get(i)).append(
                            "</th>");
                }
                out.append("</tr>\n");
            }
            int indent = 0;
            if (_showRoot) {
                out.append(formatElement(_root, ""));
                indent++;
            }
            if (_root.isOpen()) {
                out.append(printNodeContent(_root, indent));
            }
            out.append("</table>\n<!-- TreeView End -->\n");
            return out.toString();
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        }
    }

    public void show() throws Exception {
        if (_out == null) {
            throw new NullPointerException("show: out is not set");
        }
        process();
        _out.write(get());
        if (_autoFlush) {
            _out.flush();
        }
    }

    public void print() throws Exception {
        if (_out == null) {
            throw new NullPointerException("print: out is not set");
        }
        _out.write(get());
        if (_autoFlush) {
            _out.flush();
        }
    }

    public void process() throws Exception {
        _openNode = _request.getParameter("openNode");
        _closeNode = _request.getParameter("closeNode");
        if (_showRoot) {
            toggleNode(_root);
        }
        process(_root);
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
                indent.append(_indent);
            }
            SOSTreeElement current = node.getList();
            StringBuilder out = new StringBuilder();
            if (_subTables) {
                out.append("<tr class=\"").append(_disabled ? "dis" : "").append(_cssClass).append("\"><td colspan=\"10\" class=\"").append(
                        _disabled ? "dis" : "").append(_cssClass).append("\"><table class=\"").append(_disabled ? "dis" : "").append(_cssClass).append(
                        "\">");
            }
            while (current != null) {
                out.append(formatElement(current, indent.toString()));
                if (current.isOpen()) {
                    out.append(printNodeContent(current, level + 1));
                }
                current = current.getNext();
            }
            if (_subTables) {
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
        String cssClass = (element.getCssClass() != null) ? element.getCssClass() : _cssClass;
        String imgDir = (element.getImgDir() != null) ? element.getImgDir() : _imgDir;
        String imgOpenNode = (element.getImgOpenNode() != null) ? element.getImgOpenNode() : _imgOpenNode;
        String imgCloseNode = (element.getImgCloseNode() != null) ? element.getImgCloseNode() : _imgCloseNode;
        String imgLeaf = (element.getImgLeaf() != null) ? element.getImgLeaf() : _imgLeaf;
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
        str.append("<tr class=\"").append(_disabled ? "dis" : "").append(cssClass).append("\"><td class=\"").append(_disabled ? "dis" : "").append(
                cssClass).append("\"").append(_tdWidth != null ? " width=\"" + _tdWidth + "\"" : "").append(">").append(indent);
        if (element.isLeaf() && (element.getImgLeaf() == null || !"".equals(element.getImgLeaf()))) {
            str.append(image).append(_space);
        }
        if (_nodeOpenNode && element.isNode()) {
            str.append("<a ").append(hrefName).append(" class=\"").append(_disabled ? "dis" : "").append(cssClass).append("\" href=\"").append(url).append(
                    hrefAnker).append("\">").append(image).append("</a>").append(_space);
        } else if (element.getURL() != null && element.isNode()) {
            str.append("<a ").append(hrefName).append(" class=\"").append(_disabled ? "dis" : "").append(cssClass).append("\" href=\"").append(
                    formatURL(element, false)).append(hrefAnker).append("\">").append(image).append("</a>").append(_space);
        } else if (element.isNode()) {
            str.append(image).append(_space);
        }
        if (element.getPrefix() != null && !"".equals(element.getPrefix())) {
            str.append(element.getPrefix()).append(_space);
        }
        if (_textOpenNode && element.isNode()) {
            str.append("<a ").append(activStyle).append(" ").append(hrefName).append(" class=\"").append(_disabled ? "dis" : "").append(cssClass).append(
                    "\" href=\"").append(url).append(hrefAnker).append("\">").append(element.getTitle()).append("</a>");
        } else if (element.getURL() != null) {
            str.append("<a ").append(activStyle).append(" ").append(hrefName).append(" class=\"").append(_disabled ? "dis" : "").append(cssClass).append(
                    "\" href=\"").append(formatURL(element, false)).append(hrefAnker).append("\">").append(element.getTitle()).append("</a>");
        } else {
            if (activStyle != null && activStyle.length() > 0) {
                str.append("<font ").append(activStyle).append(">").append(element.getTitle()).append("</font>");
            } else {
                str.append(element.getTitle());
            }
        }
        str.append("</td>");
        if (element.getPostfix() != null) {
            str.append("<td class=\"").append(_disabled ? "dis" : "").append(cssClass).append("\">");
            if (element.isNode() && _postfixOpenNode) {
                str.append("<a ").append(hrefName).append(" class=\"").append(_disabled ? "dis" : "").append(cssClass).append("\"href=\"").append(url).append(
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
            url.append(_site).append("?").append(element.isOpen() ? "closeNode=" + element.getPath() : "openNode=" + element.getPath());
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
        return _response.encodeURL(url.toString());
    }

    private void toggleNode(SOSTreeElement node) {
        if (node == null) {
            throw new IllegalArgumentException("toggleNode: parameter node is null");
        }
        if (node.isLeaf()) {
            throw new IllegalArgumentException("toggleNode: parameter node is not set as node");
        }
        if (node.isNode() && _openNode != null && _openNode.equals(node.getPath())) {
            node.setOpen(true);
            node.setActivStyle(this._activStyle);
        }
        if (node.isNode() && _closeNode != null && _closeNode.equals(node.getPath())) {
            node.setOpen(false);
            node.setActivStyle(this._activStyle);
            if (_closeLowerNodes) {
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
        if (_handler == null) {
            throw new NullPointerException("fillNode: content handler is null");
        }
        if (node.isNew()) {
            _handler.startNode(node);
            SOSTreeElement newElement = null;
            newElement = _handler.newElement(node, newElement);
            while (newElement != null) {
                node.insert(newElement);
                newElement = _handler.newElement(node, newElement);
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
        return getElementByPath(_root, path, false);
    }

    public SOSTreeElement getElementByNamePath(String namePath) throws Exception {
        return getElementByPath(_root, namePath, true);
    }

    private SOSTreeElement getElementByPath(SOSTreeElement element, String path, boolean names) throws Exception {
        try {
            if (element == null || path == null) {
                return null;
            }
            int id = -1;
            String name = null;
            String[] paths = path.split(_separating, 3);
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
                        return getElementByPath(current, paths[1] + _separating + paths[2], names);
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
        return getElementByName(_root, name);
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
        return _activStyle;
    }

    public void setActivStyle(String style) {
        _activStyle = style;
    }

    public boolean isAutoFlush() {
        return _autoFlush;
    }

    public void setAutoFlush(boolean flush) {
        _autoFlush = flush;
    }

}