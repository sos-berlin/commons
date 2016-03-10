package sos.util;

import java.util.HashMap;

/** @author Titus Meyer */
public class SOSTreeElement {

    private SOSTreeElement _prev = null;
    private SOSTreeElement _next = null;
    private SOSTreeElement _list = null;
    private SOSTreeElement _parent = null;
    private String _name = null;
    private String _title = null;
    private String _url = null;
    private String _prefix = null;
    private String _postfix = null;
    private HashMap _parameters = null;
    private Object _data = null;
    private int _id = -1;
    private int _size = 0;
    private String _path = null;
    private boolean _leaf = false;
    private boolean _new = true;
    private boolean _open = false;
    private String _cssClass = null;
    private String _imgDir = null;
    private String _imgOpenNode = null;
    private String _imgCloseNode = null;
    private String _imgLeaf = null;
    private String _separating = ";";
    private String _hrefName = null;
    private String _hrefAnker = null;
    private String _activStyle = null;

    public SOSTreeElement() {

    }

    protected void append(SOSTreeElement element) {
        SOSTreeElement current = this;
        SOSTreeElement parent = current.getParent();
        while (current.getNext() != null) {
            current = current.getNext();
        }
        element.setParent(parent);
        element.setId(parent.getSize());
        element.setPrev(current);
        element.setNext(current.getNext());
        current.setNext(element);
        element.setPath();
        parent.setSize(parent.getSize() + 1);
    }

    protected void insert(SOSTreeElement element) {
        if (_leaf) {
            throw new IllegalArgumentException("This element is set as leaf");
        }
        element.setParent(this);
        element.setId(_size);
        SOSTreeElement current = this.getList();
        if (current == null) {
            element.setPrev(null);
            this.setList(element);
        } else {
            while (current.getNext() != null) {
                current = current.getNext();
            }
            element.setPrev(current);
            element.setNext(current.getNext());
            current.setNext(element);
        }
        element.setPath();
        _size++;
    }

    public void clear() {
        _list = null;
        _new = true;
        _size = 0;
        _open = false;
    }

    public void setParameter(String name, String val) {
        if (name == null) {
            throw new IllegalArgumentException("setParameter: parameter name is null");
        }
        if (_parameters == null) {
            _parameters = new HashMap();
        }
        _parameters.put(name, val);
    }

    public void deleteParameter(String name) {
        if (name == null) {
            throw new IllegalArgumentException("deleteParameter: parameter name is null");
        }
        _parameters.remove(name);
        if (_parameters.isEmpty()) {
            clearParameters();
        }
    }

    public void clearParameters() {
        _parameters.clear();
    }

    protected HashMap getParameters() {
        return _parameters;
    }

    protected void setPrev(SOSTreeElement prev) {
        _prev = prev;
    }

    protected void setNext(SOSTreeElement next) {
        _next = next;
    }

    protected void setParent(SOSTreeElement parent) {
        _parent = parent;
    }

    protected void setList(SOSTreeElement element) {
        if (_leaf) {
            throw new IllegalArgumentException("This element is set as leaf");
        }
        _list = element;
    }

    public void setTitle(String title) {
        _title = title;
    }

    public void setName(String name) {
        _name = name;
    }

    public void setLeaf(boolean leaf) {
        clear();
        _leaf = leaf;
    }

    public void setOpen(boolean open) {
        if (_leaf) {
            throw new IllegalArgumentException("This element is set as leaf");
        }
        _open = open;
    }

    protected void setNew(boolean isNew) {
        _new = isNew;
    }

    protected void setId(int id) {
        _id = id;
    }

    protected void setSize(int size) {
        _size = size;
    }

    protected void setPath() {
        String path = null;
        SOSTreeElement parent = this.getParent();
        while (parent != null) {
            if (path != null) {
                path = parent.getId() + _separating + path;
            } else {
                path = String.valueOf(parent.getId());
            }
            parent = parent.getParent();
        }
        if (path == null) {
            path = String.valueOf(_id);
        } else {
            path = path + _separating + _id;
        }
        _path = path;
    }

    public void setURL(String url) {
        _url = url;
    }

    public void setPrefix(String prefix) {
        _prefix = prefix;
    }

    public void setPostfix(String postfix) {
        _postfix = postfix;
    }

    public void setCssClass(String cssClass) {
        _cssClass = cssClass;
    }

    public void setImgDir(String imgDir) {
        _imgDir = imgDir;
    }

    public void setImgOpenNode(String img) {
        _imgOpenNode = img;
    }

    public void setImgCloseNode(String img) {
        _imgCloseNode = img;
    }

    public void setImgLeaf(String img) {
        _imgLeaf = img;
    }

    public void setData(Object data) {
        _data = data;
    }

    public SOSTreeElement getPrev() {
        return _prev;
    }

    public SOSTreeElement getNext() {
        return _next;
    }

    public SOSTreeElement getParent() {
        return _parent;
    }

    public SOSTreeElement getList() {
        if (_leaf) {
            throw new IllegalArgumentException("This element is set as leaf");
        }
        return _list;
    }

    public String getTitle() {
        return _title;
    }

    public String getName() {
        return _name;
    }

    public boolean isLeaf() {
        return _leaf;
    }

    public boolean isNode() {
        return !_leaf;
    }

    public boolean isOpen() {
        return _open;
    }

    public boolean isNew() {
        return _new;
    }

    public int getId() {
        return _id;
    }

    public int getSize() {
        return _size;
    }

    public String getPath() {
        if (_path == null) {
            setPath();
        }
        return _path;
    }

    public String getURL() {
        return _url;
    }

    public String getPrefix() {
        return _prefix;
    }

    public String getPostfix() {
        return _postfix;
    }

    public String getCssClass() {
        return _cssClass;
    }

    public String getImgDir() {
        return _imgDir;
    }

    public String getImgOpenNode() {
        return _imgOpenNode;
    }

    public String getImgCloseNode() {
        return _imgCloseNode;
    }

    public String getImgLeaf() {
        return _imgLeaf;
    }

    public Object getData() {
        return _data;
    }

    public String getHrefAnker() {
        return _hrefAnker;
    }

    public void setHrefAnker(String anker) {
        _hrefAnker = anker;
    }

    public String getHrefName() {
        return _hrefName;
    }

    public void setHrefName(String name) {
        _hrefName = name;
    }

    public String getActivStyle() {
        return _activStyle;
    }

    public void setActivStyle(String style) {
        _activStyle = style;
    }

}