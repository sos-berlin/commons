
package com.sos.joc.model.joe.job;

import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
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
@Generated("org.jsonschema2pojo")
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
public class Job {

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
    @JacksonXmlProperty(localName = "force_idle_timeout", isAttribute = true)
    private String forceIdleTimeout;
    /**
     * path of a process class object
     * 
     */
    @JsonProperty("processClass")
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
    /**
     * 
     */
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

    /**
     * 
     * @return
     *     The priority
     */
    @JsonProperty("priority")
    @JacksonXmlProperty(localName = "priority", isAttribute = true)
    public String getPriority() {
        return priority;
    }

    /**
     * 
     * @param priority
     *     The priority
     */
    @JsonProperty("priority")
    @JacksonXmlProperty(localName = "priority", isAttribute = true)
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * 
     * @return
     *     The title
     */
    @JsonProperty("title")
    @JacksonXmlProperty(localName = "title", isAttribute = true)
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    @JsonProperty("title")
    @JacksonXmlProperty(localName = "title", isAttribute = true)
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     * @return
     *     The order
     */
    @JsonProperty("order")
    @JacksonXmlProperty(localName = "order", isAttribute = true)
    public String getOrder() {
        return order;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     * @param order
     *     The order
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
     * @return
     *     The tasks
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
     * @param tasks
     *     The tasks
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
     * @return
     *     The minTasks
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
     * @param minTasks
     *     The minTasks
     */
    @JsonProperty("minTasks")
    @JacksonXmlProperty(localName = "min_tasks", isAttribute = true)
    public void setMinTasks(Integer minTasks) {
        this.minTasks = minTasks;
    }

    /**
     * 
     * @return
     *     The timeout
     */
    @JsonProperty("timeout")
    @JacksonXmlProperty(localName = "timeout", isAttribute = true)
    public String getTimeout() {
        return timeout;
    }

    /**
     * 
     * @param timeout
     *     The timeout
     */
    @JsonProperty("timeout")
    @JacksonXmlProperty(localName = "timeout", isAttribute = true)
    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    /**
     * 
     * @return
     *     The idleTimeout
     */
    @JsonProperty("idleTimeout")
    @JacksonXmlProperty(localName = "idle_timeout", isAttribute = true)
    public String getIdleTimeout() {
        return idleTimeout;
    }

    /**
     * 
     * @param idleTimeout
     *     The idleTimeout
     */
    @JsonProperty("idleTimeout")
    @JacksonXmlProperty(localName = "idle_timeout", isAttribute = true)
    public void setIdleTimeout(String idleTimeout) {
        this.idleTimeout = idleTimeout;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     * @return
     *     The forceIdleTimeout
     */
    @JsonProperty("forceIdleTimeout")
    @JacksonXmlProperty(localName = "force_idle_timeout", isAttribute = true)
    public String getForceIdleTimeout() {
        return forceIdleTimeout;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     * @param forceIdleTimeout
     *     The forceIdleTimeout
     */
    @JsonProperty("forceIdleTimeout")
    @JacksonXmlProperty(localName = "force_idle_timeout", isAttribute = true)
    public void setForceIdleTimeout(String forceIdleTimeout) {
        this.forceIdleTimeout = forceIdleTimeout;
    }

    /**
     * path of a process class object
     * 
     * @return
     *     The processClass
     */
    @JsonProperty("processClass")
    @JacksonXmlProperty(localName = "process_class", isAttribute = true)
    public String getProcessClass() {
        return processClass;
    }

    /**
     * path of a process class object
     * 
     * @param processClass
     *     The processClass
     */
    @JsonProperty("processClass")
    @JacksonXmlProperty(localName = "process_class", isAttribute = true)
    public void setProcessClass(String processClass) {
        this.processClass = processClass;
    }

    /**
     * 
     * @return
     *     The javaOptions
     */
    @JsonProperty("javaOptions")
    @JacksonXmlProperty(localName = "java_options", isAttribute = true)
    public String getJavaOptions() {
        return javaOptions;
    }

    /**
     * 
     * @param javaOptions
     *     The javaOptions
     */
    @JsonProperty("javaOptions")
    @JacksonXmlProperty(localName = "java_options", isAttribute = true)
    public void setJavaOptions(String javaOptions) {
        this.javaOptions = javaOptions;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     * @return
     *     The stopOnError
     */
    @JsonProperty("stopOnError")
    @JacksonXmlProperty(localName = "stop_on_error", isAttribute = true)
    public String getStopOnError() {
        return stopOnError;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     * @param stopOnError
     *     The stopOnError
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
     * @return
     *     The settings
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
     * @param settings
     *     The settings
     */
    @JsonProperty("settings")
    @JacksonXmlProperty(localName = "settings", isAttribute = false)
    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    /**
     * 
     * @return
     *     The lockUses
     */
    @JsonProperty("lockUses")
    @JacksonXmlProperty(localName = "lock.use", isAttribute = false)
    public List<LockUse> getLockUses() {
        return lockUses;
    }

    /**
     * 
     * @param lockUses
     *     The lockUses
     */
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
     * @return
     *     The params
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
     * @param params
     *     The params
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
     * @return
     *     The environment
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
     * @param environment
     *     The environment
     */
    @JsonProperty("environment")
    @JacksonXmlProperty(localName = "environment", isAttribute = false)
    public void setEnvironment(EnviromentVariables environment) {
        this.environment = environment;
    }

    /**
     * 
     * @return
     *     The login
     */
    @JsonProperty("login")
    @JacksonXmlProperty(localName = "login", isAttribute = false)
    public Login getLogin() {
        return login;
    }

    /**
     * 
     * @param login
     *     The login
     */
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
     * @return
     *     The script
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
     * @param script
     *     The script
     */
    @JsonProperty("script")
    @JacksonXmlProperty(localName = "script", isAttribute = false)
    public void setScript(Script script) {
        this.script = script;
    }

    /**
     * 
     * @return
     *     The monitors
     */
    @JsonProperty("monitors")
    @JacksonXmlProperty(localName = "monitors", isAttribute = false)
    public List<Monitor> getMonitors() {
        return monitors;
    }

    /**
     * 
     * @param monitors
     *     The monitors
     */
    @JsonProperty("monitors")
    @JacksonXmlProperty(localName = "monitors", isAttribute = false)
    public void setMonitors(List<Monitor> monitors) {
        this.monitors = monitors;
    }

    /**
     * 
     * @return
     *     The monitorUses
     */
    @JsonProperty("monitorUses")
    @JacksonXmlProperty(localName = "monitor.use", isAttribute = false)
    public List<MonitorUse> getMonitorUses() {
        return monitorUses;
    }

    /**
     * 
     * @param monitorUses
     *     The monitorUses
     */
    @JsonProperty("monitorUses")
    @JacksonXmlProperty(localName = "monitor.use", isAttribute = false)
    public void setMonitorUses(List<MonitorUse> monitorUses) {
        this.monitorUses = monitorUses;
    }

    /**
     * 
     * @return
     *     The startWhenDirectoriesChanged
     */
    @JsonProperty("startWhenDirectoriesChanged")
    @JacksonXmlProperty(localName = "start_when_directory_changed", isAttribute = false)
    public List<StartWhenDirectoryChanged> getStartWhenDirectoriesChanged() {
        return startWhenDirectoriesChanged;
    }

    /**
     * 
     * @param startWhenDirectoriesChanged
     *     The startWhenDirectoriesChanged
     */
    @JsonProperty("startWhenDirectoriesChanged")
    @JacksonXmlProperty(localName = "start_when_directory_changed", isAttribute = false)
    public void setStartWhenDirectoriesChanged(List<StartWhenDirectoryChanged> startWhenDirectoriesChanged) {
        this.startWhenDirectoriesChanged = startWhenDirectoriesChanged;
    }

    /**
     * 
     * @return
     *     The delayAfterErrors
     */
    @JsonProperty("delayAfterErrors")
    @JacksonXmlProperty(localName = "delay_after_error", isAttribute = false)
    public List<DelayAfterError> getDelayAfterErrors() {
        return delayAfterErrors;
    }

    /**
     * 
     * @param delayAfterErrors
     *     The delayAfterErrors
     */
    @JsonProperty("delayAfterErrors")
    @JacksonXmlProperty(localName = "delay_after_error", isAttribute = false)
    public void setDelayAfterErrors(List<DelayAfterError> delayAfterErrors) {
        this.delayAfterErrors = delayAfterErrors;
    }

    /**
     * 
     * @return
     *     The delayOrderAfterSetbacks
     */
    @JsonProperty("delayOrderAfterSetbacks")
    @JacksonXmlProperty(localName = "delay_order_after_setback", isAttribute = false)
    public List<DelayOrderAfterSetback> getDelayOrderAfterSetbacks() {
        return delayOrderAfterSetbacks;
    }

    /**
     * 
     * @param delayOrderAfterSetbacks
     *     The delayOrderAfterSetbacks
     */
    @JsonProperty("delayOrderAfterSetbacks")
    @JacksonXmlProperty(localName = "delay_order_after_setback", isAttribute = false)
    public void setDelayOrderAfterSetbacks(List<DelayOrderAfterSetback> delayOrderAfterSetbacks) {
        this.delayOrderAfterSetbacks = delayOrderAfterSetbacks;
    }

    /**
     * 
     * @return
     *     The runTime
     */
    @JsonProperty("runTime")
    @JacksonXmlProperty(localName = "run_time", isAttribute = false)
    public String getRunTime() {
        return runTime;
    }

    /**
     * 
     * @param runTime
     *     The runTime
     */
    @JsonProperty("runTime")
    @JacksonXmlProperty(localName = "run_time", isAttribute = false)
    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    /**
     * 
     * @return
     *     The commands
     */
    @JsonProperty("commands")
    @JacksonXmlProperty(localName = "commands", isAttribute = false)
    public Commands getCommands() {
        return commands;
    }

    /**
     * 
     * @param commands
     *     The commands
     */
    @JsonProperty("commands")
    @JacksonXmlProperty(localName = "commands", isAttribute = false)
    public void setCommands(Commands commands) {
        this.commands = commands;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(priority).append(title).append(order).append(tasks).append(minTasks).append(timeout).append(idleTimeout).append(forceIdleTimeout).append(processClass).append(javaOptions).append(stopOnError).append(settings).append(lockUses).append(params).append(environment).append(login).append(script).append(monitors).append(monitorUses).append(startWhenDirectoriesChanged).append(delayAfterErrors).append(delayOrderAfterSetbacks).append(runTime).append(commands).toHashCode();
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
        return new EqualsBuilder().append(priority, rhs.priority).append(title, rhs.title).append(order, rhs.order).append(tasks, rhs.tasks).append(minTasks, rhs.minTasks).append(timeout, rhs.timeout).append(idleTimeout, rhs.idleTimeout).append(forceIdleTimeout, rhs.forceIdleTimeout).append(processClass, rhs.processClass).append(javaOptions, rhs.javaOptions).append(stopOnError, rhs.stopOnError).append(settings, rhs.settings).append(lockUses, rhs.lockUses).append(params, rhs.params).append(environment, rhs.environment).append(login, rhs.login).append(script, rhs.script).append(monitors, rhs.monitors).append(monitorUses, rhs.monitorUses).append(startWhenDirectoriesChanged, rhs.startWhenDirectoriesChanged).append(delayAfterErrors, rhs.delayAfterErrors).append(delayOrderAfterSetbacks, rhs.delayOrderAfterSetbacks).append(runTime, rhs.runTime).append(commands, rhs.commands).isEquals();
    }

}
