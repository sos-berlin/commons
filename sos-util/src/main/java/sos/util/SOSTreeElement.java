package sos.util;

import java.util.HashMap;
import java.util.Map;

/** @author Titus Meyer */
public class SOSTreeElement {

    private SOSTreeElement prev = null;
    private SOSTreeElement next = null;
    private SOSTreeElement list = null;
    private SOSTreeElement parent = null;
    private String name = null;
    private String title = null;
    private String url = null;
    private String prefix = null;
    private String postfix = null;
    private Map<String, String> parameters = null;
    private Object data = null;
    private int id = -1;
    private int size = 0;
    private String path = null;
    private boolean leaf = false;
    private boolean newBool = true;
    private boolean open = false;
    private String cssClass = null;
    private String imgDir = null;
    private String imgOpenNode = null;
    private String imgCloseNode = null;
    private String imgLeaf = null;
    private String separating = ";";
    private String hrefName = null;
    private String hrefAnker = null;
    private String activeStyle = null;

    public SOSTreeElement() {
        //
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
        if (leaf) {
            throw new IllegalArgumentException("This element is set as leaf");
        }
        element.setParent(this);
        element.setId(size);
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
        size++;
    }

    public void clear() {
        list = null;
        newBool = true;
        size = 0;
        open = false;
    }

    public void setParameter(String name, String val) {
        if (name == null) {
            throw new IllegalArgumentException("setParameter: parameter name is null");
        }
        if (parameters == null) {
            parameters = new HashMap<String, String>();
        }
        parameters.put(name, val);
    }

    public void deleteParameter(String name) {
        if (name == null) {
            throw new IllegalArgumentException("deleteParameter: parameter name is null");
        }
        parameters.remove(name);
        if (parameters.isEmpty()) {
            clearParameters();
        }
    }

    public void clearParameters() {
        parameters.clear();
    }

    protected Map<String, String> getParameters() {
        return parameters;
    }

    protected void setPrev(SOSTreeElement prev) {
        this.prev = prev;
    }

    protected void setNext(SOSTreeElement next) {
        this.next = next;
    }

    protected void setParent(SOSTreeElement parent) {
        this.parent = parent;
    }

    protected void setList(SOSTreeElement element) {
        if (leaf) {
            throw new IllegalArgumentException("This element is set as leaf");
        }
        list = element;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLeaf(boolean leaf) {
        clear();
        this.leaf = leaf;
    }

    public void setOpen(boolean open) {
        if (leaf) {
            throw new IllegalArgumentException("This element is set as leaf");
        }
        this.open = open;
    }

    protected void setNew(boolean isNew) {
        this.newBool = isNew;
    }

    protected void setId(int id) {
        this.id = id;
    }

    protected void setSize(int size) {
        this.size = size;
    }

    protected void setPath() {
        String path = null;
        SOSTreeElement parent = this.getParent();
        while (parent != null) {
            if (path != null) {
                path = parent.getId() + separating + path;
            } else {
                path = String.valueOf(parent.getId());
            }
            parent = parent.getParent();
        }
        if (path == null) {
            path = String.valueOf(id);
        } else {
            path = path + separating + id;
        }
        this.path = path;
    }

    public void setURL(String url) {
        this.url = url;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setPostfix(String postfix) {
        this.postfix = postfix;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public void setImgDir(String imgDir) {
        this.imgDir = imgDir;
    }

    public void setImgOpenNode(String img) {
        this.imgOpenNode = img;
    }

    public void setImgCloseNode(String img) {
        this.imgCloseNode = img;
    }

    public void setImgLeaf(String img) {
        this.imgLeaf = img;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public SOSTreeElement getPrev() {
        return prev;
    }

    public SOSTreeElement getNext() {
        return next;
    }

    public SOSTreeElement getParent() {
        return parent;
    }

    public SOSTreeElement getList() {
        if (leaf) {
            throw new IllegalArgumentException("This element is set as leaf");
        }
        return list;
    }

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public boolean isNode() {
        return !leaf;
    }

    public boolean isOpen() {
        return open;
    }

    public boolean isNew() {
        return newBool;
    }

    public int getId() {
        return id;
    }

    public int getSize() {
        return size;
    }

    public String getPath() {
        if (path == null) {
            setPath();
        }
        return path;
    }

    public String getURL() {
        return url;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getPostfix() {
        return postfix;
    }

    public String getCssClass() {
        return cssClass;
    }

    public String getImgDir() {
        return imgDir;
    }

    public String getImgOpenNode() {
        return imgOpenNode;
    }

    public String getImgCloseNode() {
        return imgCloseNode;
    }

    public String getImgLeaf() {
        return imgLeaf;
    }

    public Object getData() {
        return data;
    }

    public String getHrefAnker() {
        return hrefAnker;
    }

    public void setHrefAnker(String anker) {
        this.hrefAnker = anker;
    }

    public String getHrefName() {
        return hrefName;
    }

    public void setHrefName(String name) {
        this.hrefName = name;
    }

    public String getActivStyle() {
        return activeStyle;
    }

    public void setActivStyle(String style) {
        this.activeStyle = style;
    }

}