package sos.util;

import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** <p>
 * Title: SOSTree
 * </p>
 * <p>
 * Description: HTML TreeView mit einer dynamischen Baumstruktur.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: SOS-Berlin GmbH
 * </p>
 * 
 * @author Titus Meyer
 * @version 1.2.1 */
public class SOSTree {

    /** Servlet Request-Objekt */
    private HttpServletRequest _request = null;
    /** Servlet Response-Objekt */
    private HttpServletResponse _response = null;
    /** Writer-Objekt f&uuml;r die Ausgabe (JspWriter oder PrintWriter) */
    private Writer _out = null;
    /** URL dieser Seite */
    private String _site = null;

    /** Root-Element des Baumes */
    private SOSTreeElement _root = null;
    /** Content-Handler zum Bef&uuml;llen des Baumes */
    private SOSTreeContentHandler _handler = null;

    /** String f&uuml;r eine einmalige Einr&uuml;ckung von Elementen */
    private String _indent = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
    /** Trennstring zwischen Icon und Titel bzw. Prefix eines Elementes */
    private String _space = "&nbsp;&nbsp;";
    /** Gibt an, ob das Root-Element angezeigt werden soll */
    private boolean _showRoot = true;
    /** Gibt an, ob auch der Titel einen Knoten &ouml;ffnen/schlie&szlig;en soll */
    private boolean _textOpenNode = false;
    /** Gibt an, ob das Knoten-Bild diesen &ouml;ffnet/schlie&szlig;t */
    private boolean _nodeOpenNode = true;
    /** Gibt an, ob auch der Postfix einen Knoten &ouml;ffnen/schlie&szlig;en
     * soll */
    private boolean _postfixOpenNode = false;
    /** Gibt an, ob beim Schlie&szlig;en eines Knotens auch alle unteren Knoten
     * geschlossen werden sollen */
    private boolean _closeLowerNodes = true;
    /** Gibt an, ob f&uuml;r jede Ebenen im Baum eine eigene HTML-Tabelle
     * verwendet werden soll */
    private boolean _subTables = false;
    /** Zu Verwendende CSS-Klasse f&uuml;r die Baum-Tags */
    private String _cssClass = "tree";
    /** f&uuml;hrt zu einem 'dis' vor der CSS-Klasse */
    private boolean _disabled = false;
    /** URL zum Image-Verzeichnis, in dem sich die Incons befinden */
    private String _imgDir = "images/";
    /** Icon f&uuml;r einen offenen Knoten */
    private String _imgOpenNode = "openFolder.gif\" border=\"0";
    /** Icon f&uuml;r einen geschlossenen Knoten */
    private String _imgCloseNode = "closeFolder.gif\" border=\"0";
    /** Icon f&uuml;r ein Blatt-Element */
    private String _imgLeaf = "leaf.gif\" border=\"0";
    /** Trennzeichen f&uuml;r einen Pfad */
    private String _separating = ";";
    /** Min. Breite der ersten Tabellenspalte */
    private String _tdWidth = "400";
    /** ArrayList of Strings f&uuml;r evtl. Tabellen&uuml;berschriften */
    private ArrayList _tableHead = null;

    /** URL-Parameter zum &Ouml;ffnen eines Knotens */
    private String _openNode = null;
    /** URL-Parameter zum Schliessen eines Knotens */
    private String _closeNode = null;

    /** default kein sonst in der form style=\"color:red\" */
    private String _activStyle = null;

    /** Output Stream automatish flushen */
    private boolean _autoFlush = true;

    /** Konstruktor */
    public SOSTree() {
        init();
    }

    /** Konstruktor
     *
     * @param request Servlet Request-Objekt
     * @param response Servlet Response-Objekt
     * @param out Writer-Objekt f&uuml;r Ausgaben (JspWriter oder PrintWriter) */
    public SOSTree(HttpServletRequest request, HttpServletResponse response, Writer out) {
        this();
        _request = request;
        _response = response;
        _out = out;
    }

    /** &Uuml;bergibt dem Baum das ben&ouml;tigte Request Objekt.
     *
     * @param request Servlet-Request Objekt */
    public void setRequest(HttpServletRequest request) {
        if (request == null)
            throw new IllegalArgumentException("setRequest: parameter request is null");
        _request = request;
        _site = _request.getRequestURI();
    }

    /** &Uuml;bergibt dem Baum das ben&ouml;tigte Response Objekt.
     *
     * @param response Servlet-Response Objekt */
    public void setResponse(HttpServletResponse response) {
        if (response == null)
            throw new IllegalArgumentException("setResponse: parameter response is null");
        _response = response;
    }

    /** &Uuml;bergibt dem Baum das ben&ouml;tigte Writer Objekt.
     *
     * @param out Writer Objekt */
    public void setOut(Writer out) {
        if (out == null)
            throw new IllegalArgumentException("setOut: parameter out is null");
        _out = out;
    }

    /** &Uuml;bergibt dem Baum das ben&ouml;tigte ContentHandler Objekt, welches
     * das dynamische Bef&uuml;llen des Baumes &uuml;bernimmt.
     *
     * @param handler ContentHandler Objekt */
    public void setContentHandler(SOSTreeContentHandler handler) {
        if (handler == null)
            throw new IllegalArgumentException("setContentHandler: parameter handler is null");
        _handler = handler;
    }

    /** Setzt die Sichbarkeit des Root-Elementes.
     *
     * @param visible true = sichtbar / false = versteckt */
    public void setRootVisible(boolean visible) {
        _showRoot = visible;
    }

    /** Gibt die Sichtbarkeit des Root-Elementes an.
     *
     * @return true = sichtbar / false = versteckt */
    public boolean isRootVisible() {
        return _showRoot;
    }

    /** Setzt den Namen f&uuml;r das Root-Element.
     *
     * @param name Name des Elementes zur Identifikation */
    public void setRootName(String name) {
        _root.setName(name);
    }

    /** Setzt den Titel des Root-Elementes.
     *
     * @param title der angezeigte Titel des Elementes */
    public void setRootTitle(String title) {
        _root.setTitle(title);
    }

    /** Setzt den default Namen der CSS-Klasse f&uuml;r den Baum.
     *
     * @param cssClass CSS-Klassenname */
    public void setCssClass(String cssClass) {
        _cssClass = cssClass;
    }

    /** Liefert den default Namen der verwendeten CSS-Klasse.
     *
     * @return CSS-Klassenname */
    public String getCssClass() {
        return _cssClass;
    }

    /** Setzt den default URL-Pfad des Image-Verzeichnisses f&uuml;r die Icons.
     *
     * @param imgDir URL */
    public void setImgDir(String imgDir) {
        _imgDir = imgDir;
    }

    /** Liefert die verwendete default URL zum Image-Verzeichnis.
     *
     * @return URL */
    public String getImgDir() {
        return _imgDir;
    }

    /** Setzt den default Bildnamen f&uuml;r einen offenen Knoten.
     *
     * @param img Bildname */
    public void setImgOpenNode(String img) {
        _imgOpenNode = img;
    }

    /** Liefert den verwendeten default Bildnamen f&uuml;r einen offenen Knoten.
     * 
     * @return Bildname */
    public String getImgOpenNode() {
        return _imgOpenNode;
    }

    /** Setzt den default Bildnamen f&uuml;r einen geschlossenen Knoten.
     *
     * @param img Bildname */
    public void setImgCloseNode(String img) {
        _imgCloseNode = img;
    }

    /** Liefert den verwendeten default Bildnamen f&uuml;r einen geschlossenen
     * Knoten.
     *
     * @return Bildname */
    public String getImgCloseNode() {
        return _imgCloseNode;
    }

    /** Setzt den default Bildnamen f&uuml;r ein Blatt-Element.
     *
     * @param img Bildname */
    public void setImgLeaf(String img) {
        _imgLeaf = img;
    }

    /** Liefert den verwendeten default Bildnamen f&uuml;r ein Blatt-Element.
     *
     * @return Bildname */
    public String getImgLeaf() {
        return _imgLeaf;
    }

    /** Setzt das Verhalten der Text-URLs von Knoten-Elementen.<br>
     * Default: die Element-URL wird verwendet
     *
     * @param textOpenNode true = Text &ouml;ffnet Knoten / false = Element-URL
     *            wird verwendet */
    public void setTextOpenNode(boolean textOpenNode) {
        _textOpenNode = textOpenNode;
    }

    /** Liefert das verwendete Verhalten der Text-URLs der Knoten-Elemente.
     *
     * @return true = Text &ouml;ffnet Knoten / false = Element-URL wird
     *         verwendet */
    public boolean isTextOpenNode() {
        return _textOpenNode;
    }

    /** Setzt das Verhalten der Image-URLs von Knoten-Elementen.<br>
     * Default: die Open/Close URL wird verwendet
     * 
     * @since 1.2.1
     * @param nodeOpenNode */
    public void setNodeOpenNode(boolean nodeOpenNode) {
        _nodeOpenNode = nodeOpenNode;
    }

    /** Liefert das verwendete Verhalten der Image-URLs der Knoten-Elemente.
     * 
     * @since 1.2.1
     * @return true = Knoten &ouml;ffnet Knoten / false = Element-URL wird
     *         verwendet */
    public boolean isNodeOpenNode() {
        return _nodeOpenNode;
    }

    /** Gibt an, ob bei Knotenelementen um den Postfix ein
     * &ouml;ffnen/schlie&szlig;en Link gesetzt werden soll.
     *
     * @param postfixOpenNode true = Link / false = kein Link */
    public void setPostfixOpenNode(boolean postfixOpenNode) {
        _postfixOpenNode = postfixOpenNode;
    }

    /** Liefert das Verhalten des Postfix bei Knotenelementen.
     *
     * @return true = open/close Link / false = kein Link */
    public boolean isPostfixOpenNode() {
        return _postfixOpenNode;
    }

    /** Setzt das Verhalten beim Schlie&szlig;en von Knoten-Elementen.
     *
     * @param closeLowerNodes true = Unterknoten werden geschlossen / false =
     *            Unterknoten werden nicht anger&uuml;hrt */
    public void setCloseLowerNodes(boolean closeLowerNodes) {
        _closeLowerNodes = closeLowerNodes;
    }

    /** Liefert das verwendete Verhalten beim Schlie&szlig;en von
     * Knoten-Elementen.
     *
     * @return true = Unterknoten werden geschlossen / false = Unterknoten
     *         werden nicht anger&uuml;hrt */
    public boolean isCloseLowerNodes() {
        return _closeLowerNodes;
    }

    /** Setzt die minimale Spaltenbreite f&uuml;r die erste Spalte (Icon, Prefix
     * und Titel).
     *
     * @param width HTML Breitenangabe */
    public void setTdWidth(String width) {
        _tdWidth = width;
    }

    /** Liefert die verwendete Spaltenbreite f&uuml;r die erste Spalte (Icon,
     * Prefix und Titel).
     *
     * @return HTML Breitenangabe */
    public String getTdWidth() {
        return _tdWidth;
    }

    /** Gibt an, ob f&uuml;r jede Baumebene eine eigene Tabelle verwendet werden
     * soll. Dies kann bei "breiten" Tabellenstrukturen hilfreich sein, damit
     * der Abstand zwischen der ersten Spalte (Icon, Prefix, Titel) und der
     * weiteren (Postfix) nicht zu gro&szlig; wird. Hierbei sollte die Tabelle
     * das CSS-Style "border-spacing: 0px" bekommen.
     *
     * @param subTables true = Untertabellen / false = eine einzige Tabelle */
    public void setSubTables(boolean subTables) {
        _subTables = subTables;
    }

    /** Gibt an, ob Untertabellen verwendet werden.
     *
     * @return true = Untertabellen / false = eine einzige Tabelle */
    public boolean isSubTables() {
        return _subTables;
    }

    /** Liefert das verwendete Root-Element des Baumes.
     *
     * @return Root-Element */
    public SOSTreeElement getRootNode() {
        return _root;
    }

    /** Jedes Element wird als Kopfspalte in die Baumtabelle eingef&uuml;gt.
     * Hierbei sollte der Baum nicht aus Untertabellen bestehen -
     * setSubTables(false)
     * 
     * @param headline ArrayList of Strings
     * @since 1.2.0 */
    public void setHeadline(ArrayList headline) {
        _tableHead = headline;
    }

    /** Liefert die Kopfspaltenelemente
     * 
     * @return ArrayList of Strings
     * @since 1.2.0 */
    public ArrayList getHeadline() {
        return _tableHead;
    }

    /** Wenn der Baum disabled ist, dann wird vor die CSS-Klasse ein 'dis'
     * geschrieben.
     * 
     * @param disabled Disabled
     * @since 1.2.0 */
    public void setDisabled(boolean disabled) {
        _disabled = disabled;
    }

    /** Ob der Baum disabled ist und ein 'dis' vor der CSS-Klasse steht.
     * 
     * @return Disabled
     * @since 1.2.0 */
    public boolean isDisabled() {
        return _disabled;
    }

    /** Initialisierung des Baumes. */
    private void init() {
        _root = new SOSTreeElement();
        _root.setOpen(true);
        _root.setId(0);
        _root.setName("ROOT");
        _root.setTitle("ROOT");
    }

    /** L&ouml;scht den Bauminhalt. */
    public void clear() {
        init();
    }

    /** Gibt den Baum in einer HTML-Tabelle zur&uuml;ck. Ggf. werden hierbei
     * Knoten ge&ouml;ffnet/geschlossen bzw. eingelesen.
     *
     * @throws Exception
     * @since 1.2.0 */
    public String get() throws Exception {
        if (_request == null)
            throw new NullPointerException("get: request is not set");
        if (_response == null)
            throw new NullPointerException("get: response is not set");
        if (_handler == null)
            throw new NullPointerException("get: handler is not set");

        try {
            // Parameter (in process())
            // _openNode = _request.getParameter("openNode");
            // _closeNode = _request.getParameter("closeNode");

            StringBuffer out = new StringBuffer();
            out.append("<!-- TreeView Start -->\n<table class=\"" + (_disabled ? "dis" : "") + _cssClass + "\">\n");

            // tabellenkopf
            if (_tableHead != null) {
                out.append("<tr class=\"" + (_disabled ? "dis" : "") + _cssClass + "_head\">");
                for (int i = 0; i < _tableHead.size(); i++) {
                    out.append("<th class=\"" + (_disabled ? "dis" : "") + _cssClass + "_head\">" + _tableHead.get(i) + "</th>");
                }
                out.append("</tr>\n");
            }

            int indent = 0;
            if (_showRoot) {
                // in process() ausgelagert (tm)
                // toggleNode(_root);

                // Root Element ausgeben
                out.append(formatElement(_root, ""));
                indent++;
            }

            // Rootinhalt ausgeben
            if (_root.isOpen())
                out.append(printNodeContent(_root, indent));

            out.append("</table>\n<!-- TreeView End -->\n");

            return out.toString();
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        }
    }

    /** Gibt den Baum in einer HTML-Tabelle aus. Ggf. werden hierbei Knoten
     * ge&ouml;ffnet/geschlossen bzw. eingelesen.
     *
     * @throws Exception */
    public void show() throws Exception {
        if (_out == null)
            throw new NullPointerException("show: out is not set");
        process();
        _out.write(get());

        if (_autoFlush) {
            _out.flush();
        }
    }

    /** Gibt den Baum in einer HTML-Tabelle aus. Das &ouml;ffnen/schlie&szlig;en
     * bzw. einlesen von Elementen muss zuvor &uuml;ber process()
     * ausgef&uuml;hrt werden.
     *
     * @throws Exception
     * @since 1.2.0 */
    public void print() throws Exception {
        if (_out == null)
            throw new NullPointerException("print: out is not set");
        _out.write(get());
        if (_autoFlush) {
            _out.flush();
        }
    }

    /** Ben&ouml;tigte Elemente werden eingeselen und ge&ouml;ffnet bzw.
     * geschlossen.
     *
     * @since 1.2.0 */
    public void process() throws Exception {
        // Parameter
        _openNode = _request.getParameter("openNode");
        _closeNode = _request.getParameter("closeNode");

        if (_showRoot) {
            toggleNode(_root);
        }
        process(_root);
    }

    /** Ben&ouml;tigte Elemente werden eingeselen und ge&ouml;ffnet bzw.
     * geschlossen - Rekursion.
     *
     * @since 1.2.0 */
    private void process(SOSTreeElement node) throws Exception {
        // wenn noetig, die Unterelemente des akt. Knotens lesen

        if (node.isOpen())
            fillNode(node);

        // Unterelemente des Knotens auflisten
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

    /** Rekursive Methode zur Ausgabe eines Knoteninhaltes.
     *
     * @param node Knotenelement, dessen Inhalt ausgegeben werden soll
     * @param level Einr&uuml;ckungsstufe des Knotens
     * @throws Exception */
    private StringBuffer printNodeContent(SOSTreeElement node, int level) throws Exception {
        if (node == null)
            throw new IllegalArgumentException("printElement: parameter node is null");
        if (node.isLeaf())
            throw new IllegalArgumentException("printElement: parameter node is set as leaf");

        try {
            // Einrueckung erstellen
            StringBuffer indent = new StringBuffer();
            for (int i = 0; i < level; i++)
                indent.append(_indent);

            // wenn noetig, die Unterelemente des akt. Knotens lesen
            // in process() ausgelagert (tm)
            // if(node.isOpen()) fillNode(node);

            // Unterelemente des Knotens auflisten
            SOSTreeElement current = node.getList();

            StringBuffer out = new StringBuffer();
            if (_subTables)
                out.append("<tr class=\"" + (_disabled ? "dis" : "") + _cssClass + "\"><td colspan=\"10\" class=\"" + (_disabled ? "dis" : "") + _cssClass
                        + "\"><table class=\"" + (_disabled ? "dis" : "") + _cssClass + "\">");
            while (current != null) {
                // in process() ausgelagert (tm)
                // if(current.isNode()) toggleNode(current);

                // Element ausgeben
                out.append(formatElement(current, indent.toString()));

                // wenn Node offen, dann diesen auch ausgeben - Rekursion
                if (current.isOpen())
                    out.append(printNodeContent(current, level + 1));

                current = current.getNext();
            }
            if (_subTables)
                out.append("</table></td></tr>");

            return out;
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        }
    }

    /** Liefert eine fertig formatierte Tabellenzeile eines Elementes.
     *
     * @param element Element
     * @param indent Einr&uuml;ckungsstring
     * @return */
    private String formatElement(SOSTreeElement element, String indent) {
        if (element == null || indent == null)
            throw new IllegalArgumentException("getElementStr: parameter element or indent is null");

        // default des Baumes evtl. mit Angabem im Element ueberschreiben
        String cssClass = (element.getCssClass() != null) ? element.getCssClass() : _cssClass;
        String imgDir = (element.getImgDir() != null) ? element.getImgDir() : _imgDir;
        String imgOpenNode = (element.getImgOpenNode() != null) ? element.getImgOpenNode() : _imgOpenNode;
        String imgCloseNode = (element.getImgCloseNode() != null) ? element.getImgCloseNode() : _imgCloseNode;
        String imgLeaf = (element.getImgLeaf() != null) ? element.getImgLeaf() : _imgLeaf;

        String hrefName = (element.getHrefName() != null) ? element.getHrefName() : "";
        String hrefAnker = (element.getHrefAnker() != null) ? element.getHrefAnker() : "";
        String activStyle = (element.getActivStyle() != null) ? element.getActivStyle() : "";

        StringBuffer str = new StringBuffer();

        // open/close URL
        String url = formatURL(element, true);

        // Element Icon
        StringBuffer image = new StringBuffer();
        image.append("<img src=\"" + imgDir);
        if (element.isLeaf())
            image.append(imgLeaf);
        else if (element.isOpen())
            image.append(imgOpenNode);
        else
            image.append(imgCloseNode);
        image.append("\">");

        // activStyle war oben geholt und kann zurückgesetzt werden
        element.setActivStyle(null);

        str.append("<tr class=\"" + (_disabled ? "dis" : "") + cssClass + "\"><td class=\"" + (_disabled ? "dis" : "") + cssClass + "\""
                + ((_tdWidth != null) ? " width=\"" + _tdWidth + "\"" : "") + ">" + indent);

        // Icon
        if (element.isLeaf() && (element.getImgLeaf() == null || !element.getImgLeaf().equals("")))
            str.append(image + _space);
        if (_nodeOpenNode && element.isNode())
            str.append("<a " + hrefName + " class=\"" + (_disabled ? "dis" : "") + cssClass + "\" href=\"" + url + hrefAnker + "\">" + image + "</a>" + _space);
        else if (element.getURL() != null && element.isNode())
            str.append("<a " + hrefName + " class=\"" + (_disabled ? "dis" : "") + cssClass + "\" href=\"" + formatURL(element, false) + hrefAnker + "\">"
                    + image + "</a>" + _space);
        else if (element.isNode())
            str.append(image + _space);
        // Prefix
        if (element.getPrefix() != null && !element.getPrefix().equals("")) {
            str.append(element.getPrefix() + _space);
        }
        // Title
        if (_textOpenNode && element.isNode()) {
            str.append("<a " + activStyle + " " + hrefName + " class=\"" + (_disabled ? "dis" : "") + cssClass + "\" href=\"" + url + hrefAnker + "\">"
                    + element.getTitle() + "</a>");
        } else if (element.getURL() != null) {
            // sonst url des Elementes verwenden
            str.append("<a " + activStyle + " " + hrefName + " class=\"" + (_disabled ? "dis" : "") + cssClass + "\" href=\"" + formatURL(element, false)
                    + hrefAnker + "\">" + element.getTitle() + "</a>");
        } else {
            if (activStyle != null && activStyle.length() > 0) {
                str.append("<font " + activStyle + ">" + element.getTitle() + "</font>");
            } else {
                str.append(element.getTitle());
            }
        }
        str.append("</td>");
        // Postfix
        if (element.getPostfix() != null) {
            str.append("<td class=\"" + (_disabled ? "dis" : "") + cssClass + "\">");
            if (element.isNode() && _postfixOpenNode)
                str.append("<a " + hrefName + " class=\"" + (_disabled ? "dis" : "") + cssClass + "\"href=\"" + url + hrefAnker + "\">" + element.getPostfix()
                        + "</a>");
            else
                str.append(element.getPostfix());
            str.append("</td>");
        }

        str.append("</tr>");
        str.append("\n");

        return str.toString();
    }

    /** Liefert die fertige URL eines Elementes. Es wird eine Open/Close URL
     * f&uuml;r Knotenelemente oder die Element URL erstellt. Die URL wird durch
     * die evtl. Elementparameter erg&auml;nzt und mit dem Servlet-Response
     * Objekt encoded.
     *
     * @param element Baumelement
     * @param openClose true = open/close URL / false = Element-URL
     * @return */
    private String formatURL(SOSTreeElement element, boolean openClose) {
        if (element == null)
            throw new IllegalArgumentException("formatURL: parameter element is null");

        StringBuffer url = new StringBuffer();
        String and = "";

        if (openClose) {
            // open/close URL erstellen
            url.append(_site + "?" + ((element.isOpen()) ? "closeNode=" + element.getPath() : "openNode=" + element.getPath()));
            and = "&";
        } else {
            // URL des Elementes verwenden
            url.append(element.getURL());
            if (url.indexOf("?") < 0)
                url.append("?");
            else
                and = "&";
        }

        // weitere Parameter anhaengen
        HashMap parameters = element.getParameters();
        if (parameters != null) {
            for (Iterator it = parameters.keySet().iterator(); it.hasNext();) {
                String name = (String) it.next();
                url.append(and + name + "=" + (String) parameters.get(name));
                and = "&";
            }
        }

        // URL encoden
        return _response.encodeURL(url.toString());
    }

    /** Diese Methode &ouml;ffnet bzw. schlie&szlig;t ggf. einen Knoten anhand
     * der Parameter. Das Schlie&szlig;en der Unterknoten wird hierbei ggf. mit
     * ber&uuml;cksichtig.
     *
     * @param node Knotenelement */
    private void toggleNode(SOSTreeElement node) {
        if (node == null)
            throw new IllegalArgumentException("toggleNode: parameter node is null");
        if (node.isLeaf())
            throw new IllegalArgumentException("toggleNode: parameter node is not set as node");

        // open node
        if (node.isNode() && _openNode != null && _openNode.equals(node.getPath())) {
            node.setOpen(true);
            node.setActivStyle(this._activStyle);
        }
        // close node
        if (node.isNode() && _closeNode != null && _closeNode.equals(node.getPath())) {
            node.setOpen(false);
            node.setActivStyle(this._activStyle);
            if (_closeLowerNodes) {
                closeLowerNodes(node);
            }
        }
    }

    /** Diese Methode veranla&szlig;t ggf. das Einlesen des Knotens, wenn dieses
     * noch nicht geschehen ist.
     *
     * @param node Knotenelement */
    private void fillNode(SOSTreeElement node) throws Exception {
        if (node == null)
            throw new IllegalArgumentException("fillNode: parameter node is null");
        if (node.isLeaf())
            throw new IllegalArgumentException("fillNode: parameter node is not set as node");
        if (_handler == null)
            throw new NullPointerException("fillNode: content handler is null");

        if (node.isNew()) {
            // einmalig startNode aufrufen
            _handler.startNode(node);
            SOSTreeElement newElement = null;
            // newElement wird sooft aufgerufen, bis es null liefert
            newElement = _handler.newElement(node, newElement);
            while (newElement != null) {
                // Element hinzufuegen
                node.insert(newElement);
                newElement = _handler.newElement(node, newElement);
            }
            node.setNew(false);
        }

    }

    /** Schlie&szlig;t alle unter einem Knoten liegende Knotenelemente.
     *
     * @param node Knotenelement */
    private void closeLowerNodes(SOSTreeElement node) {
        if (node == null)
            throw new IllegalArgumentException("closeLowerNodes: parameter node is null");
        if (node.isLeaf())
            throw new IllegalArgumentException("closeLowerNodes: parameter node is not set as node");

        SOSTreeElement current = node.getList();
        while (current != null) {
            if (!current.isLeaf())
                current.setOpen(false);
            if (!current.isLeaf() && current.getList() != null)
                closeLowerNodes(current);
            current = current.getNext();
        }
    }

    /** Diese Methode &ouml;ffnet den Baum bis zu dem Elternelement des
     * Elementes.
     *
     * @param element Element im Baum */
    public void openToElement(SOSTreeElement element) {
        if (element == null)
            return;

        SOSTreeElement parent = element.getParent();
        while (parent != null) {
            parent.setOpen(true);
            parent = parent.getParent();
        }
    }

    /** Liefert das passende Element zu dem Pfad, der aus den IDs besteht, die
     * durch das Semikolon getrennt sind (inklusive Rootelement).
     *
     * @param path Pfad aus Element IDs
     * @return gefundenes Element - sonst null
     * @throws Exception */
    public SOSTreeElement getElementByIdPath(String path) throws Exception {
        return getElementByPath(_root, path, false);
    }

    /** Liefert das passende Element zu dem Pfad, der aus den Namen besteht, die
     * durch das Semikolon getrennt sind (inklusive Rootelement).
     *
     * @param namePath Pfad aus Element Namen
     * @return gefundenes Element - sonst null
     * @throws Exception */
    public SOSTreeElement getElementByNamePath(String namePath) throws Exception {
        return getElementByPath(_root, namePath, true);
    }

    /** Liefert das passende Element zu dem Pfad, der aus IDs oder Namen
     * bestehene kann.
     *
     * @param element Element, ab dem gesucht werden soll
     * @param path Pfad, beginnend mit dem Element (durch Semikolons getrennt)
     * @param names true = Pfad besteht aus Namen / false = Pfad besteht aus IDs
     * @return gefundenes Element - sonst null
     * @throws Exception */
    private SOSTreeElement getElementByPath(SOSTreeElement element, String path, boolean names) throws Exception {
        try {
            if (element == null || path == null)
                return null;

            int id = -1;
            String name = null;

            // die ersten beiden Elemente und der Rest
            String[] paths = path.split(_separating, 3);

            if (names)
                name = paths[0].trim();
            else
                id = Integer.parseInt(paths[0].trim());

            // Das Element mit dem ersten Pfadeintrag pruefen
            if (!names && id != element.getId())
                return null;
            else if (names) {
                if (element.getName() == null)
                    throw new Exception("TreeElement(" + element.getPath() + "): name is null");
                if (!name.equals(element.getName()))
                    return null;
            }

            // Wenn der Pfad nicht mehr enthaelt, dann ist diese Element das
            // Ergebnis
            if (paths.length == 1)
                return element;

            // ggf. das Element mit Inhal fuellen
            fillNode(element);

            // Wenn das Element ein Blatt ist, dann weiteren Pfand ignorieren
            if (element.isLeaf())
                return element;

            // Elementinhalt nach dem zweiten Pfadeintrag pruefen
            SOSTreeElement current = element.getList();
            while (current != null) {
                int id2 = -1;
                String name2 = null;

                if (names)
                    name2 = paths[1].trim();
                else
                    id2 = Integer.parseInt(paths[1].trim());

                if (names && current.getName() == null)
                    throw new Exception("TreeElement(" + current.getPath() + "): name is null");

                // wenn passend..
                if ((names && name2.equals(current.getName())) || (!names && id2 == current.getId())) {
                    // wenn kein weiteres Element im Pfad, ist diese das
                    // Ergebnis
                    if (paths.length == 2) {
                        return current;
                    } else {
                        // sonst rekursiv weiter suchen...
                        return getElementByPath(current, paths[1] + _separating + paths[2], names);
                    }
                }
                // bei IDs suchen wir nicht weiter, wenn diese inzwischen
                // kleiner ist
                if (!names && id2 < current.getId())
                    return null;

                // sonst naechstes Element pruefen...
                current = current.getNext();
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        }
        return null;
    }

    /** Sucht ein Element anhand des Namens im Baum - z.B. zum &Auml;ndern eines
     * einzelnen Elmentens. <b>Achtung:</b> das Element muss bereits in den Baum
     * eingelesen worden sein.
     *
     * @param name Name des Elementes
     * @return gefundenes Element - sonst null */
    public SOSTreeElement getElementByName(String name) {
        return getElementByName(_root, name);
    }

    /** Sucht ein Element anhand des Namens in einem Knotenelement, z.B. zum
     * &Auml;ndern eines einzelnen Elementes. <b>Achtung</b>: das Element muss
     * bereits in den Baum eingelesen worden sein.
     *
     * @param node Knotenelement, indem gesucht werden soll
     * @param name Name des gesuchten Elementes
     * @return gefundenes Element - sonst null */
    private SOSTreeElement getElementByName(SOSTreeElement node, String name) {
        if (name == null || node == null)
            return null;

        if (node.isLeaf())
            throw new IllegalArgumentException("getElementByName: parameter node is not set as node");

        SOSTreeElement current = node.getList();

        while (current != null) {
            if (current.getName() != null && current.getName().equals(name))
                return current;

            // Rekursion
            if (current.isNode() && current.getList() != null) {
                SOSTreeElement ret = getElementByName(current, name);
                if (ret != null)
                    return ret;
            }
            current = current.getNext();
        }
        return null;
    }

    /** @return Returns the _aktivStyle. */
    public String getActivStyle() {
        return _activStyle;
    }

    /** @param style The _aktivStyle to set. */
    public void setActivStyle(String style) {
        _activStyle = style;
    }

    /** @return Returns the _autoFlush. */
    public boolean isAutoFlush() {
        return _autoFlush;
    }

    /** @param flush The _autoFlush to set. */
    public void setAutoFlush(boolean flush) {
        _autoFlush = flush;
    }
}
