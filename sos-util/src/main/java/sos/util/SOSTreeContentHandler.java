package sos.util;

/** <p>
 * Title: SOSTreeHelper
 * </p>
 * <p>
 * Description: ContentHandler zum dynamischen f&uuml;llen des SOSTree Baumes.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: SOS-Berlin GmbH
 * </p>
 * 
 * @author Titus Meyer
 * @version 1.0.0 */
public class SOSTreeContentHandler {

    /** Konstruktor */
    public SOSTreeContentHandler() {

    }

    /** Wird zu Anfang eines leeren Knotenelementes aufgerufen. Hier kann z.B. zu
     * dem Knotenelement ein passendes SQL-Statement ausgef&uuml;hrt werden und
     * das Result-Set in einer Instanzvariable abgespeichert werden.
     *
     * @param node Knotenelement, dessen Inhalt ben&ouml;tigt wird */
    public void startNode(SOSTreeElement node) throws Exception {

    }

    /** Diese Methode wird nach der startNode Methode sooft aufgerufen, bis sie
     * null zur&uuml;ck liefert. Die zur&uuml;ck gelieferten Elemente werden in
     * das Knotenelement eingef&uuml;gt.
     *
     * @param parent Knotenelment, in welches die Elemente eingef&uuml;gt werden
     * @param before Element, welches ggf. zuvor eingef&uuml;gt wurde
     * @return einzuf&uuml;gendes Element */
    public SOSTreeElement newElement(SOSTreeElement parent, SOSTreeElement before) throws Exception {
        return null;
    }
}
