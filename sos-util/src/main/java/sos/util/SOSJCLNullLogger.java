package sos.util;

import org.apache.commons.logging.Log;

/** Implementierung des Log Interface, das vom <i>Jakarta Commons Logging</i>
 * package verwendet wird.<br>
 * Diese Implementierung unterdrückt alle Logausgaben.
 * 
 * @author fs
 * @since 24.02.2006 */
public class SOSJCLNullLogger implements Log {

    public SOSJCLNullLogger() {
    }

    public SOSJCLNullLogger(String name) {
    }

    public void debug(Object arg0, Throwable arg1) {
    }

    public void debug(Object arg0) {
    }

    public void error(Object arg0, Throwable arg1) {
    }

    public void error(Object arg0) {
    }

    public void fatal(Object arg0, Throwable arg1) {
    }

    public void fatal(Object arg0) {
    }

    public void info(Object arg0, Throwable arg1) {
    }

    public void info(Object arg0) {
    }

    public boolean isDebugEnabled() {
        return false;
    }

    public boolean isErrorEnabled() {
        return false;
    }

    public boolean isFatalEnabled() {
        return false;
    }

    public boolean isInfoEnabled() {
        return false;
    }

    public boolean isTraceEnabled() {
        return false;
    }

    public boolean isWarnEnabled() {
        return false;
    }

    public void trace(Object arg0, Throwable arg1) {
    }

    public void trace(Object arg0) {
    }

    public void warn(Object arg0, Throwable arg1) {
    }

    public void warn(Object arg0) {
    }
}
