package sos.util;

import java.util.HashMap;

/**
 * <p>Title: SOSTreeElement</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: SOS-Berlin GmbH</p>
 * @author Titus Meyer
 * @version 1.1.2
 */
public class SOSTreeElement {

  /** Vorherige Element in der Liste */
  private SOSTreeElement _prev     = null;
  /** N&auml;chstes Element in der Liste */
  private SOSTreeElement _next     = null;
  /** Erste Element der Liste unter diesem Element (Knoteninhalt) */
  private SOSTreeElement _list     = null;
  /** Elternelement dieses Elementes */
  private SOSTreeElement _parent   = null;

  /** Name des Elements zur Identifikation */
  private String _name             = null;
  /** Titel des Elements zur Anzeige */
  private String _title            = null;
  /** Evtl. URL des Elementes */
  private String _url              = null;
  /** Der Prefix wird nach dem Icon und vor dem Titel eingef&uuml;gt */
  private String _prefix           = null;
  /** Der Postfix wird in eine weitere Spalte eingef&uuml;gt */
  private String _postfix          = null;
  /** Parameter werden den URL angef&uuml;gt */
  private HashMap _parameters      = null;
  /** Evtl. zus&auml;tzliche Daten */
  private Object _data             = null;

  /** ID des Elementes */
  private int _id                  = -1;
  /** Anzahl der Elemente in der Liste unterhalb des Knotenelementes */
  private int _size                = 0;
  /** Pfad des Elementes */
  private String _path             = null;
  /** Gibt an, ob das Element ein Blatt darstellt */
  private boolean _leaf            = false;
  /** Gibt an, ob die Liste unter dem Knotenelement schon eingelesen worden ist */
  private boolean _new             = true;
  /** Gibt an, ob das Knotenelement ge&ouml;ffnet ist */
  private boolean _open            = false;

  /** CSS-Klasse - bei ungleich null wird die des Baumes &uuml;berschrieben */
  private String _cssClass         = null;
  /** URL des Imageverzeichnisses - bei ungleich null wird die des Baumes &uuml;berschrieben */
  private String _imgDir           = null;
  /** Icon f&uuml;r einen offenen Knoten - bei ungleich null wird der des Baumes &uuml;berschrieben */
  private String _imgOpenNode      = null;
  /** Icon f&uuml;r einen geschlossenen Knoten - bei ungleich null wird der des Baumes &uuml;berschrieben */
  private String _imgCloseNode     = null;
  /** Icon f&uuml;r ein Blatt - bei ungleich null wird der des Baumes &uuml;berschrieben */
  private String _imgLeaf          = null;
  /** Trennzeichen f&uuml;r einen Pfad */
  private String _separating       = ";";
  
  /** Attribute name für a href*/
  private String _hrefName		   = null;
  
  /** Anker für a href*/
  private String _hrefAnker		   = null;

  private String _activStyle		   = null;
  
  /**
   * Konstruktor
   *
   */
  public SOSTreeElement() {

  }


  /**
   * F&uuml;gt ein Element auf der gleichen Ebenen an das Ende der Liste hinzu.
   *
   * @param element neues Element
   */
  protected void append(SOSTreeElement element) {
    SOSTreeElement current = this;
    SOSTreeElement parent  = current.getParent();

    // das letzte Element der Liste
    while(current.getNext() != null) current = current.getNext();

    // parent und id setzen
    element.setParent(parent);
    element.setId(parent.getSize());

    // element einfuegen
    element.setPrev(current);
    element.setNext(current.getNext());
    current.setNext(element);

    // pfad und listengroesse anpassen
    element.setPath();
    parent.setSize(parent.getSize() + 1);
  }


  /**
   * F&uuml;gt ein Element An das Ende der Liste unter diesem Element (nur bei
   * Knotenelementen m&ouml;glich).
   *
   * @param element
   */
  protected void insert(SOSTreeElement element) {
    if(_leaf) throw new IllegalArgumentException("This element is set as leaf");

    // parent und id setzen
    element.setParent(this);
    element.setId(_size);

    SOSTreeElement current = this.getList();
    if(current == null) {
      // neue Liste erstellen
      element.setPrev(null);
      this.setList(element);
    } else {
      // das Ende der Liste ermitteln
      while(current.getNext() != null) current = current.getNext();

      // element einfuegen
      element.setPrev(current);
      element.setNext(current.getNext());
      current.setNext(element);
    }

    // pfad speichern
    element.setPath();
    _size++;
  }


  /**
   * L&ouml;scht den Inhalt eines Knotenelementes und setzt den Status auf
   * uneingelesen.
   *
   */
  public void clear() {
    _list = null;
    _new  = true;
    _size = 0;
    _open = false;
  }


  /**
   * Speichert/&Uuml;berschreibt einen Parameter, der an die URLs angeh&auml;gt wird.
   *
   * @param name Name des Parameters
   * @param val Wert des Parameters
   */
  public void setParameter(String name, String val) {
    if(name == null) throw new IllegalArgumentException("setParameter: parameter name is null");
    if(_parameters == null) _parameters = new HashMap();
    _parameters.put(name, val);
  }


  /**
   * L&ouml;scht einen Parameter aus der Parameterliste.
   *
   * @param name Name des Parameters
   */
  public void deleteParameter(String name) {
    if(name == null) throw new IllegalArgumentException("deleteParameter: parameter name is null");
    _parameters.remove(name);
    if(_parameters.size() == 0) clearParameters();
  }


  /**
   * L&ouml;scht die gesamte Parameterliste.
   *
   */
  public void clearParameters() {
    _parameters.clear();
  }


  /**
   * Liefert die HashMap Liste der Parameter.
   *
   * @return Parameterliste
   */
  protected HashMap getParameters() {
    return _parameters;
  }


  /**
   * Setzt das vorige Element in der Liste.
   *
   * @param prev vorige Element
   */
  protected void setPrev(SOSTreeElement prev) {
    _prev = prev;
  }


  /**
   * Setzt das nachfolgende Element in der Liste.
   *
   * @param next nachfolgende Element
   */
  protected void setNext(SOSTreeElement next) {
    _next = next;
  }


  /**
   * Setzt das Elternelement.
   *
   * @param parent Elternelement
   */
  protected void setParent(SOSTreeElement parent) {
    _parent = parent;
  }


  /**
   * Setzt das erste Element der darunter liegenden Liste.
   * Nur als Knotenelement m&ouml;glich!
   *
   * @param element Element
   */
  protected void setList(SOSTreeElement element) {
    if(_leaf) throw new IllegalArgumentException("This element is set as leaf");
    _list = element;
  }


  /**
   * Setzt den Titel des Elementes. Dies ist der Text, der in dem Baum angezeigt
   * wird.
   *
   * @param title Elementtitel
   */
  public void setTitle(String title) {
    _title = title;
  }


  /**
   * Setzt den Namen des Elementes. Dieser kann die Identifikation bzw. die
   * Navigation in dem Baum vereinfachen.
   *
   * @param name Elementname
   */
  public void setName(String name) {
    _name = name;
  }


  /**
   * Setzt die Eigenschaft des Elementes. Dabei werden evtl. Unterelemente
   * gel&ouml;scht und der Status auf uneingelesen gesetzt.
   *
   * @param leaf true = Blattelement / false = Knotenelement
   */
  public void setLeaf(boolean leaf) {
    clear();
    _leaf = leaf;
  }


  /**
   * Setzt das Knotenelement auf offen/geschlossen.
   * Nur als Knotenelement m&ouml;glich!
   *
   * @param open true = offen / false = geschlossen
   */
  public void setOpen(boolean open) {
    if(_leaf) throw new IllegalArgumentException("This element is set as leaf");
    _open = open;
  }


  /**
   * Setzt den Status des Elementes.
   *
   * @param isNew true = Inhalt nicht eingelesen / false = Inhalt eingelesen
   */
  protected void setNew(boolean isNew) {
    _new = isNew;
  }


  /**
   * Setzt die interne ID des Elementes.
   *
   * @param id ID
   */
  protected void setId(int id) {
    _id = id;
  }


  /**
   * Setzt die Anzahl der Elemente in der darunter liegenden Ebene.
   *
   * @param size Anzahl
   */
  protected void setSize(int size) {
    _size = size;
  }


  /**
   * Ermittelt den Pfad des Elementes und speichert ihn ab.
   *
   */
  protected void setPath() {
    String path = null;
    SOSTreeElement parent = this.getParent();
    while(parent != null) {
      if(path != null)
        path = parent.getId() + _separating + path;
      else
        path = String.valueOf(parent.getId());
      parent = parent.getParent();
    }
    if(path == null) path = String.valueOf(_id);
    else path = path + _separating + _id;
    _path = path;
  }


  /**
   * Setzt die URL des Elementes. Evtl. Parameter werden automatisch angef&uuml;gt.
   * Ist dies ein Knotenelement, dann wird diese URL nicht beachtet, wenn die
   * Knoten auch &uuml;ber den Titel ge&ouml;ffnet werden.
   *
   * @param url URL
   */
  public void setURL(String url) {
    _url = url;
  }


  /**
   * Setzt den Prefix des Elementes. Dieser kann aus HTML bestehen und wird nach
   * dem Icon und vor dem Titel eingef&uuml;gt.
   *
   * @param prefix Prefix
   */
  public void setPrefix(String prefix) {
    _prefix = prefix;
  }


  /**
   * Setzt den Postfix des Elementes. Dieser kann aus HTML bestehen und wird nach
   * dem Titel in eine neue Tabellenspalte eingef&uuml;gt.
   *
   * @param postfix Postfix
   */
  public void setPostfix(String postfix) {
    _postfix = postfix;
  }


  /**
   * Setzt die CSS-Klasse dieses Elementes. Ist diese ungleich null, so
   * &uuml;berschreibt sie den default Wert des Baumobjektes.
   *
   * @param cssClass CSS-Klasse
   */
  public void setCssClass(String cssClass) {
    _cssClass = cssClass;
  }


  /**
   * Setzt die URL des Image-Verzeichnisses. Ist diese ungleich null, so
   * &uuml;berschreibt sie den default Wert des Baumobjektes.
   *
   * @param imgDir URL
   */
  public void setImgDir(String imgDir) {
    _imgDir = imgDir;
  }


  /**
   * Setzt den Bildnamen f&uuml;r ein offenes Knotenelement. Ist dieser ungleich
   * null, so &uuml;schreibt er den default Wert des Baumobjektes.
   *
   * @param img Bildname
   */
  public void setImgOpenNode(String img) {
    _imgOpenNode = img;
  }


  /**
   * Setzt den Bildnamen f&uuml;r ein geschlossenes Knotenelement. Ist dieser ungleich
   * null, so &uuml;schreibt er den default Wert des Baumobjektes.
   *
   * @param img Bildname
   */
  public void setImgCloseNode(String img) {
    _imgCloseNode = img;
  }


  /**
   * Setzt den Bildnamen f&uuml;r ein Blattelement. Ist dieser ungleich
   * null, so &uuml;schreibt er den default Wert des Baumobjektes. Wird ein leerer
   * String gespeichert, so wird f&uuml;r dieses Blattelement kein Bild
   * eingef&uuml;gt.
   *
   * @param img Bildname
   */
  public void setImgLeaf(String img) {
    _imgLeaf = img;
  }


  /**
   * Zus&auml;tzliche Speicherung von speziellen Daten zu einem Element.
   *
   * @param data Objekt
   */
  public void setData(Object data) {
    _data = data;
  }


  /**
   * Liefert das vorige Element der gleichen Ebene.
   *
   * @return Baumelement
   */
  public SOSTreeElement getPrev() {
    return _prev;
  }


  /**
   * Liefert das n&auml;chste Element der gleichen Ebene.
   *
   * @return Baumelement
   */
  public SOSTreeElement getNext() {
    return _next;
  }


  /**
   * Liefert das Elternelement.
   *
   * @return Baumelement
   */
  public SOSTreeElement getParent() {
    return _parent;
  }


  /**
   * Liefert das erste Element der darunter liegenden Liste.
   *
   * @return Baumelement
   */
  public SOSTreeElement getList() {
    if(_leaf) throw new IllegalArgumentException("This element is set as leaf");
    return _list;
  }


  /**
   * Liefert den Titel des Elementes.
   *
   * @return Titel
   */
  public String getTitle() {
    return _title;
  }


  /**
   * Liefert den Namen des Elementes.
   *
   * @return Name
   */
  public String getName() {
    return _name;
  }


  /**
   * Gibt an, ob das Element ein Blattelement ist.
   *
   * @return true = Blattelement / false = Knotenelment
   */
  public boolean isLeaf() {
    return _leaf;
  }


  /**
   * Gibt an, ob das Element ein Knotenelement ist.
   *
   * @return true = Blattelement / false = Knotenelment
   */
  public boolean isNode() {
    return !_leaf;
  }


  /**
   * Gibt an, ob das Element ge&ouml;ffnet ist.
   *
   * @return true = offen / false = geschlossen
   */
  public boolean isOpen() {
    return _open;
  }


  /**
   * Gibt an, ob der Inhalt des Elementes bereits eingelesen worden ist.
   *
   * @return true = nicht eingelesen / false = eingelesen
   */
  public boolean isNew() {
    return _new;
  }


  /**
   * Liefert die interne ID des Elementes.
   *
   * @return ID
   */
  public int getId() {
    return _id;
  }


  /**
   * Liefert die Anzahl der Elemente in der darunter liegenden Ebene.
   *
   * @return Anzahl
   */
  public int getSize() {
    return _size;
  }


  /**
   * Liefert den ID-Pfad des Elementes.
   *
   * @return ID-Pfad
   */
  public String getPath() {
    if(_path == null) setPath();
    return _path;
  }


  /**
   * Liefert die URL des Elementes.
   *
   * @return URL
   */
  public String getURL() {
    return _url;
  }


  /**
   * Liefert den Prefix des Elementes.
   *
   * @return Prefix
   */
  public String getPrefix() {
    return _prefix;
  }


  /**
   * Liefert den Postfix des Elementes.
   *
   * @return Postfix
   */
  public String getPostfix() {
    return _postfix;
  }


  /**
   * Liefert die CSS-Klasse des Elementes.
   *
   * @return CSS-Klasse
   */
  public String getCssClass() {
    return _cssClass;
  }


  /**
   * Liefert die URL des Image-Verzeichnisses.
   *
   * @return URL
   */
  public String getImgDir() {
    return _imgDir;
  }


  /**
   * Liefert den Bildnamen f&uuml;r offene Knoten.
   *
   * @return Bildname
   */
  public String getImgOpenNode() {
    return _imgOpenNode;
  }


  /**
   * Liefert den Bildnamen f&uuml;r geschlossene Knoten.
   *
   * @return Bildname
   */
  public String getImgCloseNode() {
    return _imgCloseNode;
  }


  /**
   * Liefert den Bildnamen f&uuml;r Bl&auml;tter.
   *
   * @return Bildname
   */
  public String getImgLeaf() {
    return _imgLeaf;
  }


  /**
   * Liefert das gespeicherte Datenobjekt.
   *
   * @return Objekt / null
   */
  public Object getData() {
    return _data;
  }
/**
 * @return Returns the _hrefAnker.
 */
public String getHrefAnker() {
    return _hrefAnker;
}
/**
 * @param anker The _hrefAnker to set.
 */
public void setHrefAnker(String anker) {
    _hrefAnker = anker;
}
/**
 * @return Returns the _hrefName.
 */
public String getHrefName() {
    return _hrefName;
}
/**
 * @param name The _hrefName to set.
 */
public void setHrefName(String name) {
    _hrefName = name;
}
/**
 * @return Returns the _activStyle.
 */
public String getActivStyle() {
    return _activStyle;
}
/**
 * @param style The _activStyle to set.
 */
public void setActivStyle(String style) {
    _activStyle = style;
}
}
