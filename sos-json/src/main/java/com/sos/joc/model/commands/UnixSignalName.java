//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Aenderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.03.24 um 02:41:35 PM CET 
//


package com.sos.joc.model.commands;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse for Unix_signal_name.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="Unix_signal_name">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *     &lt;enumeration value="SIGHUP"/>
 *     &lt;enumeration value="SIGINT"/>
 *     &lt;enumeration value="SIGQUIT"/>
 *     &lt;enumeration value="SIGILL"/>
 *     &lt;enumeration value="SIGTRAP"/>
 *     &lt;enumeration value="SIGABRT"/>
 *     &lt;enumeration value="SIGIOT"/>
 *     &lt;enumeration value="SIGBUS"/>
 *     &lt;enumeration value="SIGFPE"/>
 *     &lt;enumeration value="SIGKILL"/>
 *     &lt;enumeration value="SIGUSR1"/>
 *     &lt;enumeration value="SIGSEGV"/>
 *     &lt;enumeration value="SIGUSR2"/>
 *     &lt;enumeration value="SIGPIPE"/>
 *     &lt;enumeration value="SIGALRM"/>
 *     &lt;enumeration value="SIGTERM"/>
 *     &lt;enumeration value="SIGSTKFLT"/>
 *     &lt;enumeration value="SIGCHLD"/>
 *     &lt;enumeration value="SIGCONT"/>
 *     &lt;enumeration value="SIGSTOP"/>
 *     &lt;enumeration value="SIGTSTP"/>
 *     &lt;enumeration value="SIGTTIN"/>
 *     &lt;enumeration value="SIGTTOU"/>
 *     &lt;enumeration value="SIGURG"/>
 *     &lt;enumeration value="SIGXCPU"/>
 *     &lt;enumeration value="SIGXFSZ"/>
 *     &lt;enumeration value="SIGVTALRM"/>
 *     &lt;enumeration value="SIGPROF"/>
 *     &lt;enumeration value="SIGWINCH"/>
 *     &lt;enumeration value="SIGPOLL"/>
 *     &lt;enumeration value="SIGIO"/>
 *     &lt;enumeration value="SIGPWR"/>
 *     &lt;enumeration value="SIGSYS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Unix_signal_name")
@XmlEnum
public enum UnixSignalName {

    SIGHUP("SIGHUP"),
    SIGINT("SIGINT"),
    SIGQUIT("SIGQUIT"),
    SIGILL("SIGILL"),
    SIGTRAP("SIGTRAP"),
    SIGABRT("SIGABRT"),
    SIGIOT("SIGIOT"),
    SIGBUS("SIGBUS"),
    SIGFPE("SIGFPE"),
    SIGKILL("SIGKILL"),
    @XmlEnumValue("SIGUSR1")
    SIGUSR_1("SIGUSR1"),
    SIGSEGV("SIGSEGV"),
    @XmlEnumValue("SIGUSR2")
    SIGUSR_2("SIGUSR2"),
    SIGPIPE("SIGPIPE"),
    SIGALRM("SIGALRM"),
    SIGTERM("SIGTERM"),
    SIGSTKFLT("SIGSTKFLT"),
    SIGCHLD("SIGCHLD"),
    SIGCONT("SIGCONT"),
    SIGSTOP("SIGSTOP"),
    SIGTSTP("SIGTSTP"),
    SIGTTIN("SIGTTIN"),
    SIGTTOU("SIGTTOU"),
    SIGURG("SIGURG"),
    SIGXCPU("SIGXCPU"),
    SIGXFSZ("SIGXFSZ"),
    SIGVTALRM("SIGVTALRM"),
    SIGPROF("SIGPROF"),
    SIGWINCH("SIGWINCH"),
    SIGPOLL("SIGPOLL"),
    SIGIO("SIGIO"),
    SIGPWR("SIGPWR"),
    SIGSYS("SIGSYS");
    private final String value;

    UnixSignalName(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static UnixSignalName fromValue(String v) {
        for (UnixSignalName c: UnixSignalName.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
