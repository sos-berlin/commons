package sos.configuration;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import sos.util.SOSLogger;
import sos.util.SOSString;
import sos.xml.SOSXMLXPath;

public class SOSConfigurationRequiredItem {

	/** name der Konfigurationdatei*/
	public static String	REQUIRED_DEFAULT_PARAMETERS_FILENAME	= "configuration.xml";

	/** sos.util.SOSString Object*/
	private SOSString		sosString								= new SOSString();

	/** sos.xml.SOSXMLXPath Object */
	private SOSXMLXPath		xPath									= null;

	/** Hilfsvariable für Messages*/
	private String			msgUnknownParameter						= "";

	/** Liste der zu überprüfende Ids. Siehe Pfad in der XML ->
	 * <Configurations>
	 * 	<items>
	 * 		<!-- Folgende Parameter kennt SOSFTP -->
	 * 		<item name="operation" default_value="" itemId="operation_id"/>
	 * 		<item name="settings" default_value="" itemId="settings_id" checkId="settings_and_profile_id"/>
	 * 		<item name="profile" default_value="false" itemId="profile_id" checkId="settings_and_profile_id"/>
	 * 		<item name="verbose" default_value="10" itemId="verbose_id"/>
	 * ...
	 * </items>
	 * ...
	 * <!-- überprüfen der Abhängigkeiten mit Hilfe der boolean Operationen wie and und or-->
	 * 	<checkParameter>
	 * 		<check checkId="settings_and_profile_id" bool_operation="and">
	 * 			<item itemId="settings_id"/>
	 * 			<item itemId="profile_id"/>
	 * 		</check>
	 * ...
	 * 
	 * */
	private ArrayList		checkIds								= null;

	/** Hilfsvariable für das schnelle zugreifen der einzelnen ConfigurationItem Objekte 
	 * über den ItemId */
	private HashMap			quickConfigurationsItem					= null;

	/** sos.logger.SOSLogger Objekt*/
	private SOSLogger		sosLogger								= null;

	/** Liste aller Passwortnamen*/
	private ArrayList		passwordNames							= null;

	/** Sind Parameternamen erlaubt die nicht in der Konfigurationsdatei vorkommen?
	 * 
	 * //Configurations/items[@check_params_names='yes'] -> Es dürfen nur die Parameternamen vorkommen werden,
	 * die in der Konfigurations datei definiert sind.
	 * 
	 * */
	private boolean			allowOtherParamsNames					= false;

	/**
	 * Konstructor
	 * 
	 * @param SOSLogger Objekt
	 */
	public SOSConfigurationRequiredItem(SOSLogger sosLogger_) throws Exception {
		this.sosLogger = sosLogger_;
		init();
	}

	/**
	 * Konstructor 
	 * 
	 * @param filename Name der Konfigurationsdatei
	 * @param sosLogger_ SOSLogger Objekt
	 * @throws Exception
	 */
	public SOSConfigurationRequiredItem(String filename, SOSLogger sosLogger_) throws Exception {
		this.sosLogger = sosLogger_;
		REQUIRED_DEFAULT_PARAMETERS_FILENAME = filename;
		init();
	}

	/**
	 * init
	 * @throws Exception
	 */
	private void init() throws Exception {
		try {

			checkIds = new ArrayList();

			quickConfigurationsItem = new HashMap();

		}
		catch (Exception e) {
			throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.toString());
		}
	}

	/**
	 * Folgende Überprüfungen finden statt:
	 * 
	 * 0.   Sollen Parameternamen, die nicht in der Configurations.xml vorkommen erlaubt sein?
	 * a.	Existiert diese Parametername in der Configuration.xml 
	 * b.	Ist der Parameterwert gültig
	 * c.	Alle abhängigen Parameter, die keinen Wert haben werden ggf. mit Defaultwerten besetzt
	 * d.	Muss dieser Parameter in Abhängigkeit zu einem anderen Parameter vorkommen (booleische Ausdruck)
	 * e.	Existieren zu dieser Parameter andere abhängige Parameter. Wenn ja wiederhole für jeden Parameter ab 2a.
	 * 
	 *  
	 * @param configurationItem
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public SOSConfigurationItem[] check(SOSConfigurationItem[] configurationItem) throws Exception {
		allowOtherParamsNames =  true;
//		return configurationItem;
		
		try {
			if (xPath == null) {
//				InputStream in = getInputStreamFromRequiredFile();
//				getLogger().debug("got inputstream");
//				xPath = new SOSXMLXPath(in, true);
//				getLogger().debug("created xml-Document (SOSXMLXPath)");
			}

			allowOtherParamsNames = true; /* checkParameterNames(configurationItem); */ // sollen Parameternamen überprüft werden

			for (int i = 0; i < configurationItem.length; i++) {
				String name = configurationItem[i].getName();
				String value = configurationItem[i].getValue();

				getLogger().debug("check parameter [" + name + "=" + value + "]");

//				String itemId = checkParameterNames(name, configurationItem[i]);
				String itemId = name + "_id";
				quickConfigurationsItem.put(itemId, configurationItem[i]);
				quickConfigurationsItem.put(name, configurationItem[i]);
//				if (itemId != null) {
//					checkParameterValue(itemId, name, value);
//				}
//				if (msgUnknownParameter != null && msgUnknownParameter.length() > 0) {
//					throw new Exception(msgUnknownParameter);
//				}
			}

//			configurationItem = checkParameters(configurationItem);

			// überprüft die abhängigkeit. Falls ein ConfigurationItem nicht existiert, dann wird ggf. dieser mit default_wert ersetzt
//			configurationItem = checkdependencies(configurationItem);

//			checkRequiredItem();

			if (msgUnknownParameter != null && msgUnknownParameter.length() > 0) {
				throw new Exception(msgUnknownParameter);
			}

			return configurationItem;
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.toString());
		}
	}

	/**
	 * Abhängige Parameter überprüfen
	 * 
	 * <dependencies>
	 * 		<item refId="operation_id">
	 * 			<required>
	 * 				<item itemId="protocol_id"/>
	 * 				<item itemId="host_id"/>
	 * 				<item itemId="port_id"/>
	 * 				<item itemId="user_id"/>
	 *          </required>
	 *     			<optional>
	 *     				<item itemId=""/>
	 *     			</optional>
	 *		</item>
	 *
	 * 
	 * @param itemId
	 */
	private SOSConfigurationItem[] checkdependencies(SOSConfigurationItem[] configurationsItem) throws Exception {
		SOSConfigurationItem[] newconfigurationItem = configurationsItem;
		ArrayList listOfMissingItemWithDefaults = new ArrayList();
		try {
			Iterator iIDs = quickConfigurationsItem.keySet().iterator();
			while (iIDs.hasNext()) {
				String itemId = sosString.parseToString(iIDs.next());
				String value = ((SOSConfigurationItem) (quickConfigurationsItem.get(itemId))).getValue();
				NodeList list = xPath.selectNodeList("//Configurations/dependencies/item[@refId='" + itemId + "' and @value='" + value + "']/required/item ");
				if (list.getLength() == 0)
					list = xPath.selectNodeList("//Configurations/dependencies/item[@refId='" + itemId + "' and @value='']/required/item ");

				for (int j = 0; j < list.getLength(); j++) {
					Node child = list.item(j);
					org.w3c.dom.NamedNodeMap cAtr = child.getAttributes();
					for (int k = 0; k < cAtr.getLength(); k++) {
						// getLogger().debug9(k + "'te ahhängige attribut : " + cAtr.item(k));
						String depItem = cAtr.item(k).getNodeValue();
						boolean missingparam = false;
						// --> Überprüfe, ob depItem in HashMap vorhanden ist. Wenn nicht nacht Defaults suchen
						if (!quickConfigurationsItem.containsKey(depItem)) {
							SOSConfigurationItem item = getNewConfigurationItem(depItem);
							if (sosString.parseToString(item.getValue()).trim().length() == 0
									&& sosString.parseToString(item.getDefaults()).trim().length() == 0) {
								missingparam = true;
								msgUnknownParameter = (msgUnknownParameter.length() > 0 ? msgUnknownParameter + "\n" : "") + "missing dependencies Parameter "
										+ item.getName() + " for Parameter " + ((SOSConfigurationItem) (quickConfigurationsItem.get(itemId))).getName();
							}
							else {
								getLogger().debug("try to use default Parameter, if default_value exist");
								if (sosString.parseToString(item.getValue()).length() > 0) {
									getLogger().debug1("use Defaults Parameter [" + item.getName() + "=" + item.getDefaults() + "]");
									listOfMissingItemWithDefaults.add(item);
								}
								getLogger().debug("Defaults Parameter has no default_value[" + item.getName() + "]");
							}
						}
						else {
							SOSConfigurationItem item = (SOSConfigurationItem) quickConfigurationsItem.get(depItem);
							if (sosString.parseToString(item.getValue()).trim().length() == 0
									&& sosString.parseToString(item.getDefaults()).trim().length() == 0) {
								missingparam = true;
							}
							if (missingparam)
								msgUnknownParameter = (msgUnknownParameter.length() > 0 ? msgUnknownParameter + "\n" : "") + "missing dependencies Parameter "
										+ depItem;
						}
					}
				}
			}
			newconfigurationItem = mergeListOfMissingItemWithDefaults(listOfMissingItemWithDefaults, configurationsItem);

			return newconfigurationItem;
		}
		catch (Exception e) {
			throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.toString());
		}
	}

	/**
	 * 
	 * 
	 * @param listOfMissingItemWithDefaults
	 * @param configurationsItem
	 * @return
	 * @throws Exception
	 */
	private SOSConfigurationItem[] mergeListOfMissingItemWithDefaults(ArrayList listOfMissingItemWithDefaults, SOSConfigurationItem[] configurationsItem)
			throws Exception {
		SOSConfigurationItem[] newconfigurationItem = configurationsItem;
		try {
			if (listOfMissingItemWithDefaults.size() > 0) {

				// mit den alten Werten mergen
				newconfigurationItem = new SOSConfigurationItem[configurationsItem.length + listOfMissingItemWithDefaults.size()];

				for (int j = 0; j < configurationsItem.length; j++) {
					newconfigurationItem[j] = configurationsItem[j];
				}

				for (int i = 0; i < listOfMissingItemWithDefaults.size(); i++) {
					newconfigurationItem[configurationsItem.length + i] = (SOSConfigurationItem) listOfMissingItemWithDefaults.get(i);
				}
				// Überprüfe weil Parameter sich durch die Abhängigkeit verändert haben
				getLogger().debug("check again, cause new Defaultvalues change the Conditions");
				newconfigurationItem = check(newconfigurationItem);

			}
			return newconfigurationItem;
		}
		catch (Exception e) {
			throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.toString());
		}
	}

	/**
	 * Sucht in der Konfigurationdatei nach itemId und liefert einen ConfigurationsItem Objekt
	 * 
	 * entsprechende XML:
	 * 
	 * <Configurations>
	 * 	<items>
	 * 		<!-- Folgende Parameter kennt SOSFTP -->
	 * 		<item name="operation" default_value="" itemId="operation_id"/>
	 * 		<item name="settings" default_value="" itemId="settings_id" checkId="settings_and_profile_id"/>
	 * 		<item name="profile" default_value="false" itemId="profile_id" checkId="settings_and_profile_id"/>
	 * 		<item name="verbose" default_value="10" itemId="verbose_id"/>
	 * ...
	 * 
	 */
	private SOSConfigurationItem getNewConfigurationItem(String itemId) throws Exception {
		SOSConfigurationItem item = null;
		try {
			Node n = xPath.selectSingleNode("//Configurations/items/item[@itemId='" + itemId + "'] ");

			if (n == null) {
				if (msgUnknownParameter.length() > 0)
					msgUnknownParameter = msgUnknownParameter + "\n";
				msgUnknownParameter = msgUnknownParameter + " invalid parameter itemId: " + itemId + ".";
				return null;
			}
			else {
				item = new SOSConfigurationItem();
				item.setItemId(itemId);
				org.w3c.dom.NamedNodeMap map = n.getAttributes();
				for (int j = 0; j < map.getLength(); j++) {
					// getLogger().debug9(j + "'te attribut : " + map.item(j).getNodeName());
					if (map.item(j).getNodeName().equalsIgnoreCase("default_value")) {
						item.setDefaults(map.item(j).getNodeValue());
						item.setValue(map.item(j).getNodeValue());
					}
					else
						if (map.item(j).getNodeName().equalsIgnoreCase("checkId")) {
							String checkName = map.item(j).getNodeValue();
							if (!checkIds.contains(checkName))
								checkIds.add(checkName);
						}
						else
							if (map.item(j).getNodeName().equalsIgnoreCase("name")) {
								item.setName(map.item(j).getNodeValue());

							}
				}
			}
			return item;
		}
		catch (Exception e) {
			throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.toString());
		}

	}

	/**
	 * Überprüft die Abhängigkeiten der Parameter mit Hilfe der boolischen Ausdrücke
	 * 
	 * entsprechende XML:
	 * 
	 * <Configurations>
	 *      <checkParameter>
	 *  		<check checkId="settings_and_profile_id" bool_operation="and">
	 *  			<item itemId="settings_id"/>
	 *  			<item itemId="profile_id"/>
	 *  		</check>
	 *     </checkParameter>
	 *     ...
	 * <Configurations>
	 * 
	 */
	private SOSConfigurationItem[] checkParameters(SOSConfigurationItem[] configurationsItem) throws Exception {

		String boolOperation = "";
		String boolStr = "";
		HashMap values4condition = new HashMap();
		ArrayList listOfMissingItemWithDefaults = new ArrayList(); // bool Operation kann Parametern haben, die nicht vorhanden sind. In
																	// diesem fall müssen diese Parameter erstellt und ggf. mit Defaults
																	// belegt werden

		try {
			for (int i = 0; i < checkIds.size(); i++) {
				// existiert der checkId?
				boolOperation = "";
				boolStr = "";
				values4condition = new HashMap();
				String checkId = sosString.parseToString(checkIds.get(i));
				Node n = xPath.selectSingleNode("//Configurations/checkParameter/check[@checkId='" + checkId + "'] ");
				if (n == null) {
					throw new Exception("missing Item [checkId=" + checkId + "]");
				}
				NamedNodeMap map = n.getAttributes();
				for (int j = 0; j < map.getLength(); j++) {
					// welche boolische Ausdruck
					if (map.item(j).getNodeName().equalsIgnoreCase("bool_operation")) {
						boolOperation = map.item(j).getNodeValue();
					}
				}
				// welche Attribute
				NodeList list = xPath.selectNodeList("//Configurations/checkParameter/check[@checkId='" + checkId + "']/item ");

				for (int j = 0; j < list.getLength(); j++) {
					Node child = list.item(j);
					org.w3c.dom.NamedNodeMap cAtr = child.getAttributes();
					for (int k = 0; k < cAtr.getLength(); k++) {
						// getLogger().debug9(k + "'te attribut : " + cAtr.item(k));
						if (cAtr.item(k).getNodeName().equalsIgnoreCase("itemId")) {
							String itemId = cAtr.item(k).getNodeValue();
							SOSConfigurationItem item_ = (SOSConfigurationItem) (quickConfigurationsItem.get(itemId));
							if (item_ != null && item_.getValue() != null && item_.getValue().length() > 0) {
								values4condition.put(item_.getName(), item_.getValue());
								boolStr = (boolStr.length() > 0 ? boolStr + " " + boolOperation : boolStr) + " " + item_.getName(); // + (k
																																	// <
																																	// cAtr.getLength()
																																	// + 1 ?
																																	// boolOperation
																																	// :
																																	// "");
							}
							else {
								SOSConfigurationItem newItem = getNewConfigurationItem(itemId);// Parameter mit der itemId existiert nicht.
																								// Generiere mit der Defaultwert
								String newName = sosString.parseToString(newItem.getName());
								String newValue = sosString.parseToString(newItem.getValue()).trim().length() == 0 ? sosString.parseToString(newItem.getDefaults())
										: sosString.parseToString(newItem.getValue());
								if (newValue.length() > 0) {// Neue Items dürfen nur berücksichtigt werden, wenn dieser auch einen Wert
															// haben.
									listOfMissingItemWithDefaults.add(newItem);

								}

								if (boolOperation.equalsIgnoreCase("and")) {
									newValue = (newValue.length() > 0 ? newValue : "1");
									values4condition.put(newName, newValue);
									// getLogger().debug(itemId + " not exist and replace with [" + newName+"="+newValue+"]");
									boolStr = (boolStr.length() > 0 ? boolStr + " " + boolOperation : boolStr) + " " + newName;
								}
								else {
									newValue = (newValue.length() > 0 ? newValue : "0");
									values4condition.put(newName, newValue);
									// getLogger().debug(itemId + " not exist and replace with [" + newName+"="+newValue+"]");
									boolStr = (boolStr.length() > 0 ? boolStr + " " + boolOperation : boolStr) + " " + newName;
								}
							}
						}
					}
				}

				configurationsItem = mergeListOfMissingItemWithDefaults(listOfMissingItemWithDefaults, configurationsItem);
				listOfMissingItemWithDefaults.clear();

				getLogger().debug("Boolean expression: " + boolStr);
				sos.settings.SOSCheckSettings c = new sos.settings.SOSCheckSettings(boolStr, values4condition, sosLogger);
				if (c.process()) {
					getLogger().debug("Boolean expression is OK ");
				}
				else {
					throw new Exception("error in Boolean expression " + boolStr);
				}
			}
			return configurationsItem;
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.toString());
		}
	}

	/**
	 * Überprüfen, ob auch Parameternamen erlaubt sind, die nicht in der XML Datei vorkommen.
	 * 
	 * Es gibt zwei Möglichkeiten das zu überprüfen:
	 * 
	 * 1. Wird als Parameter übergeben; SOSConfigurationItem[] configurationItem enthält einen Parametername = check_params_names=yes oder no  
	 * 2. Im Meta Datei wie
	 * 
	 * <Configurations>
	 * 	  <!-- Das Attribut check_params_names="no" ermöglich das auch weitere Parametername ohne Überprüfung akzeptiert werden. -->
	 *    <!-- Wenn das Attribut check_params_names="yes" ist, dann dürfen nur die Parameter weiter übergeben werden, wenn       -->
	 *    <!-- dieser in dieser XML Meta Datei vorkommen. Sonst wird ein Fehler verworfen -->
	 *    <items check_params_names="no">
	 * 
	 */
	private boolean checkParameterNames(SOSConfigurationItem[] configurationItem) throws Exception {

		try {

			if (configurationItem != null)
				for (int i = 0; i < configurationItem.length; i++) {
					SOSConfigurationItem item = configurationItem[i];
					if (item.getName().equalsIgnoreCase("check_params_names") && item.getValue().equalsIgnoreCase("no")) {
						getLogger().debug("other params is allowed");
						return true;
					}
				}

			Node n = xPath.selectSingleNode("//Configurations/items[@check_params_names='no'] ");

			if (n != null) {
				getLogger().debug("other params is allowed");
				return true;
			}
			getLogger().debug9("Other Parameternames is not allowed.");
			return false;
		}
		catch (Exception e) {
			throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.toString());
		}

	}

	/**
	 * Überprüfen, ob dieser Parametername ein gültiger Name ist
	 * 
	 * beispiel XML:
	 * <Configurations>
	 * 	<items>
	 * 		<!-- Folgende Parameter kennt SOSFTP -->
	 * 		<item name="operation" default_value="" itemId="operation_id"/>
	 * 		<item name="settings" default_value="" itemId="settings_id" checkId="settings_and_profile_id"/>
	 * 		<item name="profile" default_value="false" itemId="profile_id" checkId="settings_and_profile_id"/>
	 * 		<item name="verbose" default_value="10" itemId="verbose_id"/>
	 * ...
	 * 
	 */
	private String checkParameterNames(String name, SOSConfigurationItem item) throws Exception {
		String itemId = "";
		try {
			// getLogger().debug("check Parameter name=" + name );

			if (passwordNames == null)
				passwordNames = new ArrayList();

			Node n = xPath.selectSingleNode("//Configurations/items/item[@name='" + name + "'] ");

			if (n == null) {
				// Beim SchedulerBetrieb können beliebige Auftragsparameter mit übergeben werden.
				// Die Überprüfung der gültigen Parametername soll indem Fall ignoriert werden oder man kann die Konfigurationsdatei
				// erweitern.
				// FAll ignorieren
				if (allowOtherParamsNames) {
					item.setItemId(name + "_id");
					return name + "_id";
				}
				// Parametername unbekannt. Überprüfe ob dieser parameter einen gültigen Präfix hat
				String itemIdfromPrefix = validItemPrefix(name, item);
				if (itemIdfromPrefix != null && itemIdfromPrefix.length() > 0) {
					return itemIdfromPrefix;
				}
				else {
					if (msgUnknownParameter.length() > 0)
						msgUnknownParameter = msgUnknownParameter + "\n";
					msgUnknownParameter = msgUnknownParameter + " invalid parameter name: " + name + ".";
					return null;
				}
			}
			else {
				org.w3c.dom.NamedNodeMap map = n.getAttributes();
				for (int j = 0; j < map.getLength(); j++) {
					// getLogger().debug9(j + "'te attribut : " + map.item(j).getNodeName());
					if (map.item(j).getNodeName().equalsIgnoreCase("itemId")) {
						itemId = map.item(j).getNodeValue();
						item.setItemId(itemId);
					}
					else
						if (map.item(j).getNodeName().equalsIgnoreCase("default_value")) {
							item.setDefaults(map.item(j).getNodeValue());
						}
						else
							if (map.item(j).getNodeName().equalsIgnoreCase("checkId")) {
								String checkName = map.item(j).getNodeValue();
								if (!checkIds.contains(checkName))
									checkIds.add(checkName);
							}
							else
								if (map.item(j).getNodeName().equalsIgnoreCase("password")) {
									boolean isPassword = sosString.parseToBoolean(sosString.parseToString(map.item(j).getNodeValue()));
									item.setPassword(isPassword);
									if (!passwordNames.contains(name))
										passwordNames.add(name);

								}
				}

			}
			return itemId;

		}
		catch (Exception e) {
			throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.toString());
		}
	}

	/**
	 * Parametername unbekannt. 
	 * Überprüfe ob dieser parameter einen gültigen Präfix hat.
	 * Überprüfung findet im XML wie folgt statt:
	 * <Configuration> 
	 *  ...
	 * 	 <!-- Alle Parameter die den Präfix haben sind gültige Parameter -->
	 * 	 <prefix>
	 * 	    <item name="history_entry_"/>
	 *      ...
	 * 	 </prefix>
	 * ...
	 * </Configuration>
	 * 
	 * @param name
	 * @return
	 */
	private String validItemPrefix(String name, SOSConfigurationItem item) throws Exception {
		try {
			getLogger().debug("check Prefix for Item name=" + name);
			NodeList list = xPath.selectNodeList("//Configurations/prefix/item");
			for (int i = 0; i < list.getLength(); i++) {
				Node n = list.item(i);
				org.w3c.dom.NamedNodeMap map = n.getAttributes();
				for (int j = 0; j < map.getLength(); j++) {
					// getLogger().debug9(j + "'te prefix attribut : " + map.item(j));
					if (name.startsWith(map.item(j).getNodeValue())) {
						item.setItemId(name + "Id");
						return (name + "Id");
					}
				}
			}
			return "";
		}
		catch (Exception e) {
			throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.toString());
		}
	}

	/**
	 * Überprüfen, ob dieser Parametername ein gültiger Name ist
	 * 
	 * zu überprüfende XML:
	 * 
	 * <Configurations>
	 *      <checkParameterValue>
	 *  		<check itemId="protocol_id">
	 *  			<paramvalue value="ftp"/>
	 *  			<paramvalue value="sftp"/>
	 *  			<paramvalue value="ftps"/>
	 *  		</check>
	 *          ...
	 *       </checkParameterValue>
	 *  <Configurations>
	 *  
	 */
	private boolean checkParameterValue(String itemId, String name, String value) throws Exception {
		try {

			Node n = xPath.selectSingleNode("//Configurations/checkParameterValue/check[@itemId='" + itemId + "']");
			if (n != null && sosString.parseToString(value).length() > 0) {
				n = xPath.selectSingleNode("//Configurations/checkParameterValue/check[@itemId='" + itemId + "']/paramvalue[@value='" + value + "'] ");
				if (n == null) {
					if (msgUnknownParameter.length() > 0)
						msgUnknownParameter = msgUnknownParameter + "\n";
					msgUnknownParameter = msgUnknownParameter + " invalid parameter value: [name=" + name + "], [value=" + value + "].";
					return false;
				}
			}
			return true;
		}
		catch (Exception e) {
			throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.toString());
		}
	}

	/**
	 * Überprrüfen ob ein Pflichtfeld existieren muss:
	 * entsprechende XML:
	 * <Configurations>
	 * 	<items>
	 * 		<!-- Folgende Parameter kennt SOSFTP -->
	 * 		<item name="operation" default_value="" itemId="operation_id" required="true"/>
	 * 
	 * 
	 */
	private void checkRequiredItem() throws Exception {
		try {
			NodeList list = xPath.selectNodeList("//Configurations/items/item[@required='true']");
			for (int i = 0; i < list.getLength(); i++) {
				Node n = list.item(i);
				org.w3c.dom.NamedNodeMap map = n.getAttributes();
				for (int j = 0; j < map.getLength(); j++) {
					// getLogger().debug9(j + "'te prefix attribut : " + map.item(j).getNodeName());
					if (map.item(j).getNodeName().equalsIgnoreCase("itemId")) {
						String itemId = map.item(j).getNodeValue();
						if (!quickConfigurationsItem.containsKey(itemId)) {
							throw new Exception("missing parameter: " + map.getNamedItem("name").getNodeValue());
						}
					}
				}
			}

		}
		catch (Exception e) {
			throw new Exception("error in " + sos.util.SOSClassUtil.getMethodName() + ": cause: " + e.toString());
		}
	}

	/**
	 * Die required_defaults_parameters.xml wird in der Library geliefert.
	 * @return
	 * @throws Exception
	 */
	private InputStream getInputStreamFromRequiredFile() throws Exception {
		try {
			// InputStream in =
			// getClass().getClassLoader().getSystemResourceAsStream("sos/parameters/"+REQUIRED_DEFAULT_PARAMETERS_FILENAME);//aus
			// Klassenpfad holen
			InputStream in = null;

			try {
				File objFile = new File(REQUIRED_DEFAULT_PARAMETERS_FILENAME);
				getLogger().debug9("Configuration.xml is on Path : " + objFile.getAbsolutePath());
				in = new java.io.FileInputStream(objFile);
			}
			catch (Exception e) {
				// throw new Exception ("error while reading  " + REQUIRED_DEFAULT_PARAMETERS_FILENAME + ": " + e.toString(), e);
				getLogger().debug9(e.toString());
			}

			if (in == null) {
				getLogger().debug("try again to Read " + REQUIRED_DEFAULT_PARAMETERS_FILENAME + " from Class path.");
				in = getClass().getClassLoader().getSystemResourceAsStream(REQUIRED_DEFAULT_PARAMETERS_FILENAME);// aus Klassenpfad holen
			}

			getLogger().debug9("get InputStream from " + REQUIRED_DEFAULT_PARAMETERS_FILENAME + "=" + in);
			if (in == null) {
				getLogger().debug9("try again to read InputStream from Library. " + REQUIRED_DEFAULT_PARAMETERS_FILENAME);
				in = getClass().getClassLoader().getResourceAsStream(REQUIRED_DEFAULT_PARAMETERS_FILENAME);// aus der Bibliothel holen
				getLogger().debug9("InputStream is =" + in);
			}

			if (in == null) {
				throw new Exception("could not read File " + REQUIRED_DEFAULT_PARAMETERS_FILENAME);
			}

			return in;
		}
		catch (Exception e) {
			throw new Exception("error while reading  " + REQUIRED_DEFAULT_PARAMETERS_FILENAME + ": " + e.toString(), e);
		}
	}

	/**
	 * @return the sosLogger
	 */
	public SOSLogger getLogger() {
		return sosLogger;
	}

	/**
	 * @param sosLogger the sosLogger to set
	 */
	public void setLogger(SOSLogger sosLogger) {
		this.sosLogger = sosLogger;
	}

	/**
	 * @return the quickConfigurationsItem
	 */
	public HashMap getQuickConfigurationsItem() {
		return quickConfigurationsItem;
	}

	public static void main(String[] args) {
		try {
			sos.util.SOSLogger sosLogger = new sos.util.SOSStandardLogger(9);

			SOSConfigurationItem pOperation = new SOSConfigurationItem();
			pOperation.setName("operation");
			pOperation.setValue("send");
			// ConfigurationItem[] requiredOperation = pOperation.required();

			SOSConfigurationItem pProtocol = new SOSConfigurationItem();
			pProtocol.setName("protocol");
			pProtocol.setValue("ftp");

			SOSConfigurationItem pHost = new SOSConfigurationItem();
			pHost.setName("host");
			pHost.setValue("localhost");

			SOSConfigurationItem pUser = new SOSConfigurationItem();
			pUser.setName("user");
			pUser.setValue("sos");

			SOSConfigurationItem pfilePath = new SOSConfigurationItem();
			pfilePath.setName("file_path");
			pfilePath.setValue("c:/temp/1.xml");
			// pfilePath.setValue("");

			SOSConfigurationItem sshProxyHost = new SOSConfigurationItem();
			sshProxyHost.setName("ssh_proxy_host");
			sshProxyHost.setValue("ssh_proxy_host.SOS");

			SOSConfigurationItem sshProxyPort = new SOSConfigurationItem();
			sshProxyPort.setName("ssh_proxy_port");
			sshProxyPort.setValue("1000");

			SOSConfigurationItem sshProxyUser = new SOSConfigurationItem();
			sshProxyUser.setName("ssh_proxy_user");
			sshProxyUser.setValue("sos");

			SOSConfigurationItem[] p1 = new SOSConfigurationItem[] { pOperation, pfilePath, pProtocol, pUser, sshProxyHost, sshProxyPort, sshProxyUser };

			System.out.println("***************************Start configuration Item vorher************************");
			for (int i = 0; i < p1.length; i++) {
				System.out.println(i + "'te Parameter");
				System.out.println("name    =\t " + p1[i].getName());
				System.out.println("value   =\t " + p1[i].getValue());
				System.out.println("default =\t " + p1[i].getDefaults());
				System.out.println("itemId  =\t " + p1[i].getItemId());
				System.out.println();
			}
			System.out.println("**********************************************************************************");

			SOSConfigurationRequiredItem ri = new SOSConfigurationRequiredItem("J:/E/java/mo/doc/sosftp/Redesign/Configuration.xml", sosLogger);
			p1 = ri.check(p1);

			System.out.println("***************************Start configuration Item nachher************************");
			for (int i = 0; i < p1.length; i++) {
				System.out.println(i + "'te Parameter");
				System.out.println("name    =\t " + p1[i].getName());
				System.out.println("value   =\t " + p1[i].getValue());
				System.out.println("default =\t " + p1[i].getDefaults());
				System.out.println("itemId  =\t " + p1[i].getItemId());
				System.out.println();
			}
			System.out.println("**********************************************************************************");

		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	/**
	 * @return the passwordNames
	 */
	public ArrayList getPasswordNames() {
		return passwordNames;
	}

}
