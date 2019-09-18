
package com.sos.joc.model.joe.job;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.sos.joc.model.joe.common.IJSObject;
import com.sos.joc.model.joe.common.Params;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * job without name, visible, temporary, spooler_id, log_append, separate_process, mail_xslt_stylesheet attributes
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "job")
@JsonPropertyOrder({
    "priority",
    "title",
    "order",
    "tasks",
    "minTasks",
    "timeout",
    "idleTimeout",
    "forceIdleTimeout",
    "processClass",
    "javaOptions",
    "stopOnError",
    "settings",
    "lockUses",
    "params",
    "environment",
    "login",
    "script",
    "monitors",
    "monitorUses",
    "startWhenDirectoriesChanged",
    "delayAfterErrors",
    "delayOrderAfterSetbacks",
    "runTime",
    "commands"
})
public class Job implements IJSObject
{

    @JsonProperty("priority")
    @JacksonXmlProperty(localName = "priority", isAttribute = true)
    private String priority;
    @JsonProperty("title")
    @JacksonXmlProperty(localName = "title", isAttribute = true)
    private String title;
    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("order")
    @JsonPropertyDescription("possible values: yes, no, 1, 0, true, false")
    @JacksonXmlProperty(localName = "order", isAttribute = true)
    private String order;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("tasks")
    @JacksonXmlProperty(localName = "tasks", isAttribute = true)
    private Integer tasks;
    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("minTasks")
    @JacksonXmlProperty(localName = "min_tasks", isAttribute = true)
    private Integer minTasks;
    @JsonProperty("timeout")
    @JacksonXmlProperty(localName = "timeout", isAttribute = true)
    private String timeout;
    @JsonProperty("idleTimeout")
    @JacksonXmlProperty(localName = "idle_timeout", isAttribute = true)
    private String idleTimeout;
    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("forceIdleTimeout")
    @JsonPropertyDescription("possible values: yes, no, 1, 0, true, false")
    @JacksonXmlProperty(localName = "force_idle_timeout", isAttribute = true)
    private String forceIdleTimeout;
    /**
     * path of a process class object
     * 
     */
    @JsonProperty("processClass")
    @JsonPropertyDescription("path of a process class object")
    @JacksonXmlProperty(localName = "process_class", isAttribute = true)
    private String processClass;
    @JsonProperty("javaOptions")
    @JacksonXmlProperty(localName = "java_options", isAttribute = true)
    private String javaOptions;
    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("stopOnError")
    @JsonPropertyDescription("possible values: yes, no, 1, 0, true, false")
    @JacksonXmlProperty(localName = "stop_on_error", isAttribute = true)
    private String stopOnError;
    /**
     * job settings
     * <p>
     * 
     * 
     */
    @JsonProperty("settings")
    @JacksonXmlProperty(localName = "settings", isAttribute = false)
    private Settings settings;
    @JsonProperty("lockUses")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "lock.use", isAttribute = false)
    private List<LockUse> lockUses = null;
    /**
     * parameters
     * <p>
     * 
     * 
     */
    @JsonProperty("params")
    @JacksonXmlProperty(localName = "params", isAttribute = false)
    private Params params;
    /**
     * environment
     * <p>
     * 
     * 
     */
    @JsonProperty("environment")
    @JacksonXmlProperty(localName = "environment", isAttribute = false)
    private EnviromentVariables environment;
    @JsonProperty("login")
    @JacksonXmlProperty(localName = "login", isAttribute = false)
    private Login login;
    /**
     * job script TODO it's incomplete
     * <p>
     * 
     * 
     */
    @JsonProperty("script")
    @JacksonXmlProperty(localName = "script", isAttribute = false)
    private Script script;
    @JsonProperty("monitors")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "monitors", isAttribute = false)
    private List<Monitor> monitors = null;
    @JsonProperty("monitorUses")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "monitor.use", isAttribute = false)
    private List<MonitorUse> monitorUses = null;
    @JsonProperty("startWhenDirectoriesChanged")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "start_when_directory_changed", isAttribute = false)
    private List<StartWhenDirectoryChanged> startWhenDirectoriesChanged = null;
    @JsonProperty("delayAfterErrors")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "delay_after_error", isAttribute = false)
    private List<DelayAfterError> delayAfterErrors = null;
    @JsonProperty("delayOrderAfterSetbacks")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "delay_order_after_setback", isAttribute = false)
    private List<DelayOrderAfterSetback> delayOrderAfterSetbacks = null;
    @JsonProperty("runTime")
    @JacksonXmlProperty(localName = "run_time", isAttribute = false)
    private String runTime = "";
    @JsonProperty("commands")
    @JacksonXmlProperty(localName = "commands", isAttribute = false)
    private Commands commands;

    @JsonProperty("priority")
    @JacksonXmlProperty(localName = "priority", isAttribute = true)
    public String getPriority() {
        return priority;
    }

    @JsonProperty("priority")
    @JacksonXmlProperty(localName = "priority", isAttribute = true)
    public void setPriority(String priority) {
        this.priority = priority;
    }

    @JsonProperty("title")
    @JacksonXmlProperty(localName = "title", isAttribute = true)
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    @JacksonXmlProperty(localName = "title", isAttribute = true)
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("order")
    @JacksonXmlProperty(localName = "order", isAttribute = true)
    public String getOrder() {
        return order;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("order")
    @JacksonXmlProperty(localName = "order", isAttribute = true)
    public void setOrder(String order) {
        this.order = order;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("tasks")
    @JacksonXmlProperty(localName = "tasks", isAttribute = true)
    public Integer getTasks() {
        return tasks;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("tasks")
    @JacksonXmlProperty(localName = "tasks", isAttribute = true)
    public void setTasks(Integer tasks) {
        this.tasks = tasks;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("minTasks")
    @JacksonXmlProperty(localName = "min_tasks", isAttribute = true)
    public Integer getMinTasks() {
        return minTasks;
    }

    /**
     * non negative integer
     * <p>
     * 
     * 
     */
    @JsonProperty("minTasks")
    @JacksonXmlProperty(localName = "min_tasks", isAttribute = true)
    public void setMinTasks(Integer minTasks) {
        this.minTasks = minTasks;
    }

    @JsonProperty("timeout")
    @JacksonXmlProperty(localName = "timeout", isAttribute = true)
    public String getTimeout() {
        return timeout;
    }

    @JsonProperty("timeout")
    @JacksonXmlProperty(localName = "timeout", isAttribute = true)
    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    @JsonProperty("idleTimeout")
    @JacksonXmlProperty(localName = "idle_timeout", isAttribute = true)
    public String getIdleTimeout() {
        return idleTimeout;
    }

    @JsonProperty("idleTimeout")
    @JacksonXmlProperty(localName = "idle_timeout", isAttribute = true)
    public void setIdleTimeout(String idleTimeout) {
        this.idleTimeout = idleTimeout;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("forceIdleTimeout")
    @JacksonXmlProperty(localName = "force_idle_timeout", isAttribute = true)
    public String getForceIdleTimeout() {
        return forceIdleTimeout;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("forceIdleTimeout")
    @JacksonXmlProperty(localName = "force_idle_timeout", isAttribute = true)
    public void setForceIdleTimeout(String forceIdleTimeout) {
        this.forceIdleTimeout = forceIdleTimeout;
    }

    /**
     * path of a process class object
     * 
     */
    @JsonProperty("processClass")
    @JacksonXmlProperty(localName = "process_class", isAttribute = true)
    public String getProcessClass() {
        return processClass;
    }

    /**
     * path of a process class object
     * 
     */
    @JsonProperty("processClass")
    @JacksonXmlProperty(localName = "process_class", isAttribute = true)
    public void setProcessClass(String processClass) {
        this.processClass = processClass;
    }

    @JsonProperty("javaOptions")
    @JacksonXmlProperty(localName = "java_options", isAttribute = true)
    public String getJavaOptions() {
        return javaOptions;
    }

    @JsonProperty("javaOptions")
    @JacksonXmlProperty(localName = "java_options", isAttribute = true)
    public void setJavaOptions(String javaOptions) {
        this.javaOptions = javaOptions;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("stopOnError")
    @JacksonXmlProperty(localName = "stop_on_error", isAttribute = true)
    public String getStopOnError() {
        return stopOnError;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("stopOnError")
    @JacksonXmlProperty(localName = "stop_on_error", isAttribute = true)
    public void setStopOnError(String stopOnError) {
        this.stopOnError = stopOnError;
    }

    /**
     * job settings
     * <p>
     * 
     * 
     */
    @JsonProperty("settings")
    @JacksonXmlProperty(localName = "settings", isAttribute = false)
    public Settings getSettings() {
        return settings;
    }

    /**
     * job settings
     * <p>
     * 
     * 
     */
    @JsonProperty("settings")
    @JacksonXmlProperty(localName = "settings", isAttribute = false)
    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    @JsonProperty("lockUses")
    @JacksonXmlProperty(localName = "lock.use", isAttribute = false)
    public List<LockUse> getLockUses() {
        return lockUses;
    }

    @JsonProperty("lockUses")
    @JacksonXmlProperty(localName = "lock.use", isAttribute = false)
    public void setLockUses(List<LockUse> lockUses) {
        this.lockUses = lockUses;
    }

    /**
     * parameters
     * <p>
     * 
     * 
     */
    @JsonProperty("params")
    @JacksonXmlProperty(localName = "params", isAttribute = false)
    public Params getParams() {
        return params;
    }

    /**
     * parameters
     * <p>
     * 
     * 
     */
    @JsonProperty("params")
    @JacksonXmlProperty(localName = "params", isAttribute = false)
    public void setParams(Params params) {
        this.params = params;
    }

    /**
     * environment
     * <p>
     * 
     * 
     */
    @JsonProperty("environment")
    @JacksonXmlProperty(localName = "environment", isAttribute = false)
    public EnviromentVariables getEnvironment() {
        return environment;
    }

    /**
     * environment
     * <p>
     * 
     * 
     */
    @JsonProperty("environment")
    @JacksonXmlProperty(localName = "environment", isAttribute = false)
    public void setEnvironment(EnviromentVariables environment) {
        this.environment = environment;
    }

    @JsonProperty("login")
    @JacksonXmlProperty(localName = "login", isAttribute = false)
    public Login getLogin() {
        return login;
    }

    @JsonProperty("login")
    @JacksonXmlProperty(localName = "login", isAttribute = false)
    public void setLogin(Login login) {
        this.login = login;
    }

    /**
     * job script TODO it's incomplete
     * <p>
     * 
     * 
     */
    @JsonProperty("script")
    @JacksonXmlProperty(localName = "script", isAttribute = false)
    public Script getScript() {
        return script;
    }

    /**
     * job script TODO it's incomplete
     * <p>
     * 
     * 
     */
    @JsonProperty("script")
    @JacksonXmlProperty(localName = "script", isAttribute = false)
    public void setScript(Script script) {
        this.script = script;
    }

    @JsonProperty("monitors")
    @JacksonXmlProperty(localName = "monitors", isAttribute = false)
    public List<Monitor> getMonitors() {
        return monitors;
    }

    @JsonProperty("monitors")
    @JacksonXmlProperty(localName = "monitors", isAttribute = false)
    public void setMonitors(List<Monitor> monitors) {
        this.monitors = monitors;
    }

    @JsonProperty("monitorUses")
    @JacksonXmlProperty(localName = "monitor.use", isAttribute = false)
    public List<MonitorUse> getMonitorUses() {
        return monitorUses;
    }

    @JsonProperty("monitorUses")
    @JacksonXmlProperty(localName = "monitor.use", isAttribute = false)
    public void setMonitorUses(List<MonitorUse> monitorUses) {
        this.monitorUses = monitorUses;
    }

    @JsonProperty("startWhenDirectoriesChanged")
    @JacksonXmlProperty(localName = "start_when_directory_changed", isAttribute = false)
    public List<StartWhenDirectoryChanged> getStartWhenDirectoriesChanged() {
        return startWhenDirectoriesChanged;
    }

    @JsonProperty("startWhenDirectoriesChanged")
    @JacksonXmlProperty(localName = "start_when_directory_changed", isAttribute = false)
    public void setStartWhenDirectoriesChanged(List<StartWhenDirectoryChanged> startWhenDirectoriesChanged) {
        this.startWhenDirectoriesChanged = startWhenDirectoriesChanged;
    }

    @JsonProperty("delayAfterErrors")
    @JacksonXmlProperty(localName = "delay_after_error", isAttribute = false)
    public List<DelayAfterError> getDelayAfterErrors() {
        return delayAfterErrors;
    }

    @JsonProperty("delayAfterErrors")
    @JacksonXmlProperty(localName = "delay_after_error", isAttribute = false)
    public void setDelayAfterErrors(List<DelayAfterError> delayAfterErrors) {
        this.delayAfterErrors = delayAfterErrors;
    }

    @JsonProperty("delayOrderAfterSetbacks")
    @JacksonXmlProperty(localName = "delay_order_after_setback", isAttribute = false)
    public List<DelayOrderAfterSetback> getDelayOrderAfterSetbacks() {
        return delayOrderAfterSetbacks;
    }

    @JsonProperty("delayOrderAfterSetbacks")
    @JacksonXmlProperty(localName = "delay_order_after_setback", isAttribute = false)
    public void setDelayOrderAfterSetbacks(List<DelayOrderAfterSetback> delayOrderAfterSetbacks) {
        this.delayOrderAfterSetbacks = delayOrderAfterSetbacks;
    }

    @JsonProperty("runTime")
    @JacksonXmlProperty(localName = "run_time", isAttribute = false)
    public String getRunTime() {
        return runTime;
    }

    @JsonProperty("runTime")
    @JacksonXmlProperty(localName = "run_time", isAttribute = false)
    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    @JsonProperty("commands")
    @JacksonXmlProperty(localName = "commands", isAttribute = false)
    public Commands getCommands() {
        return commands;
    }

    @JsonProperty("commands")
    @JacksonXmlProperty(localName = "commands", isAttribute = false)
    public void setCommands(Commands commands) {
        this.commands = commands;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("priority", priority).append("title", title).append("order", order).append("tasks", tasks).append("minTasks", minTasks).append("timeout", timeout).append("idleTimeout", idleTimeout).append("forceIdleTimeout", forceIdleTimeout).append("processClass", processClass).append("javaOptions", javaOptions).append("stopOnError", stopOnError).append("settings", settings).append("lockUses", lockUses).append("params", params).append("environment", environment).append("login", login).append("script", script).append("monitors", monitors).append("monitorUses", monitorUses).append("startWhenDirectoriesChanged", startWhenDirectoriesChanged).append("delayAfterErrors", delayAfterErrors).append("delayOrderAfterSetbacks", delayOrderAfterSetbacks).append("runTime", runTime).append("commands", commands).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(delayOrderAfterSetbacks).append(delayAfterErrors).append(startWhenDirectoriesChanged).append(forceIdleTimeout).append(processClass).append(title).append(login).append(minTasks).append(timeout).append(lockUses).append(runTime).append(tasks).append(commands).append(order).append(monitorUses).append(settings).append(priority).append(params).append(script).append(stopOnError).append(environment).append(idleTimeout).append(javaOptions).append(monitors).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Job) == false) {
            return false;
        }
        Job rhs = ((Job) other);
        return new EqualsBuilder().append(delayOrderAfterSetbacks, rhs.delayOrderAfterSetbacks).append(delayAfterErrors, rhs.delayAfterErrors).append(startWhenDirectoriesChanged, rhs.startWhenDirectoriesChanged).append(forceIdleTimeout, rhs.forceIdleTimeout).append(processClass, rhs.processClass).append(title, rhs.title).append(login, rhs.login).append(minTasks, rhs.minTasks).append(timeout, rhs.timeout).append(lockUses, rhs.lockUses).append(runTime, rhs.runTime).append(tasks, rhs.tasks).append(commands, rhs.commands).append(order, rhs.order).append(monitorUses, rhs.monitorUses).append(settings, rhs.settings).append(priority, rhs.priority).append(params, rhs.params).append(script, rhs.script).append(stopOnError, rhs.stopOnError).append(environment, rhs.environment).append(idleTimeout, rhs.idleTimeout).append(javaOptions, rhs.javaOptions).append(monitors, rhs.monitors).isEquals();
    }

}
