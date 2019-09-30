
package com.sos.joc.model.joe.job;

import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.sos.joc.model.joe.common.IJSObject;
import com.sos.joc.model.joe.common.Params;
import com.sos.joc.model.joe.schedule.RunTime;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * job without name, vtemporary, spooler_id, log_append, separate_process, mail_xslt_stylesheet, replace attributes
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
    "isOrderJob",
    "maxTasks",
    "minTasks",
    "timeout",
    "idleTimeout",
    "forceIdleTimeout",
    "processClass",
    "javaOptions",
    "stopOnError",
    "ignoreSignals",
    "warnIfLongerThan",
    "warnIfShorterThan",
    "stderrLogLevel",
    "credentialsKey",
    "loadUserProfile",
    "visible",
    "settings",
    "documentation",
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
    @JsonProperty("isOrderJob")
    @JacksonXmlProperty(localName = "order", isAttribute = true)
    private String isOrderJob;
    @JsonProperty("maxTasks")
    @JacksonXmlProperty(localName = "tasks", isAttribute = true)
    private Integer maxTasks;
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
     * possible values: positive Integer or SIGHUP, SIGINT, SIGQUIT, SIGILL, SIGTRAP, SIGABRT, SIGIOT, SIGBUS, SIGFPE, SIGKILL, SIGUSR1, SIGSEGV, SIGUSR2, SIGPIPE, SIGALRM, SIGTERM, SIGSTKFLT, SIGCHLD, SIGCONT, SIGSTOP, SIGTSTP, SIGTTIN, SIGTTOU, SIGURG, SIGXCPU, SIGXFSZ, SIGVTALRM, SIGPROF, SIGWINCH, SIGPOLL, SIGIO, SIGPWR, SIGSYS
     * 
     */
    @JsonProperty("ignoreSignals")
    @JacksonXmlProperty(localName = "ignore_signals", isAttribute = true)
    private String ignoreSignals;
    @JsonProperty("warnIfLongerThan")
    @JacksonXmlProperty(localName = "warn_if_longer_than", isAttribute = true)
    private String warnIfLongerThan;
    @JsonProperty("warnIfShorterThan")
    @JacksonXmlProperty(localName = "warn_if_shorter_than", isAttribute = true)
    private String warnIfShorterThan;
    /**
     * possible values: error, warn, info, debug or debug[0-9]
     * 
     */
    @JsonProperty("stderrLogLevel")
    @JacksonXmlProperty(localName = "stderr_log_level", isAttribute = true)
    private String stderrLogLevel;
    @JsonProperty("credentialsKey")
    @JacksonXmlProperty(localName = "credentials_key", isAttribute = true)
    private String credentialsKey;
    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     */
    @JsonProperty("loadUserProfile")
    @JacksonXmlProperty(localName = "load_user_profile", isAttribute = true)
    private String loadUserProfile;
    /**
     * possible values: yes, no, 1, 0, true, false or never
     * 
     */
    @JsonProperty("visible")
    @JacksonXmlProperty(localName = "visible", isAttribute = true)
    private String visible;
    /**
     * job settings
     * <p>
     * 
     * 
     */
    @JsonProperty("settings")
    @JacksonXmlProperty(localName = "settings", isAttribute = false)
    private Settings settings;
    @JsonProperty("documentation")
    @JacksonXmlProperty(localName = "description", isAttribute = false)
    private Description documentation;
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
     * job script
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
    /**
     * runTime
     * <p>
     * 
     * 
     */
    @JsonProperty("runTime")
    @JacksonXmlProperty(localName = "run_time", isAttribute = false)
    private RunTime runTime = new RunTime();
    @JsonProperty("commands")
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "commands", isAttribute = false)
    private List<Commands> commands = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Job() {
    }

    /**
     * 
     * @param delayOrderAfterSetbacks
     * @param delayAfterErrors
     * @param startWhenDirectoriesChanged
     * @param forceIdleTimeout
     * @param processClass
     * @param title
     * @param ignoreSignals
     * @param credentialsKey
     * @param login
     * @param minTasks
     * @param warnIfShorterThan
     * @param timeout
     * @param isOrderJob
     * @param lockUses
     * @param runTime
     * @param commands
     * @param warnIfLongerThan
     * @param monitorUses
     * @param settings
     * @param visible
     * @param maxTasks
     * @param loadUserProfile
     * @param documentation
     * @param priority
     * @param params
     * @param script
     * @param stopOnError
     * @param environment
     * @param idleTimeout
     * @param javaOptions
     * @param stderrLogLevel
     * @param monitors
     */
    public Job(String priority, String title, String isOrderJob, Integer maxTasks, Integer minTasks, String timeout, String idleTimeout, String forceIdleTimeout, String processClass, String javaOptions, String stopOnError, String ignoreSignals, String warnIfLongerThan, String warnIfShorterThan, String stderrLogLevel, String credentialsKey, String loadUserProfile, String visible, Settings settings, Description documentation, List<LockUse> lockUses, Params params, EnviromentVariables environment, Login login, Script script, List<Monitor> monitors, List<MonitorUse> monitorUses, List<StartWhenDirectoryChanged> startWhenDirectoriesChanged, List<DelayAfterError> delayAfterErrors, List<DelayOrderAfterSetback> delayOrderAfterSetbacks, RunTime runTime, List<Commands> commands) {
        this.priority = priority;
        this.title = title;
        this.isOrderJob = isOrderJob;
        this.maxTasks = maxTasks;
        this.minTasks = minTasks;
        this.timeout = timeout;
        this.idleTimeout = idleTimeout;
        this.forceIdleTimeout = forceIdleTimeout;
        this.processClass = processClass;
        this.javaOptions = javaOptions;
        this.stopOnError = stopOnError;
        this.ignoreSignals = ignoreSignals;
        this.warnIfLongerThan = warnIfLongerThan;
        this.warnIfShorterThan = warnIfShorterThan;
        this.stderrLogLevel = stderrLogLevel;
        this.credentialsKey = credentialsKey;
        this.loadUserProfile = loadUserProfile;
        this.visible = visible;
        this.settings = settings;
        this.documentation = documentation;
        this.lockUses = lockUses;
        this.params = params;
        this.environment = environment;
        this.login = login;
        this.script = script;
        this.monitors = monitors;
        this.monitorUses = monitorUses;
        this.startWhenDirectoriesChanged = startWhenDirectoriesChanged;
        this.delayAfterErrors = delayAfterErrors;
        this.delayOrderAfterSetbacks = delayOrderAfterSetbacks;
        this.runTime = runTime;
        this.commands = commands;
    }

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
     *     The isOrderJob
     */
    @JsonProperty("isOrderJob")
    @JacksonXmlProperty(localName = "order", isAttribute = true)
    public String getIsOrderJob() {
        return isOrderJob;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     * @param isOrderJob
     *     The isOrderJob
     */
    @JsonProperty("isOrderJob")
    @JacksonXmlProperty(localName = "order", isAttribute = true)
    public void setIsOrderJob(String isOrderJob) {
        this.isOrderJob = isOrderJob;
    }

    /**
     * 
     * @return
     *     The maxTasks
     */
    @JsonProperty("maxTasks")
    @JacksonXmlProperty(localName = "tasks", isAttribute = true)
    public Integer getMaxTasks() {
        return maxTasks;
    }

    /**
     * 
     * @param maxTasks
     *     The maxTasks
     */
    @JsonProperty("maxTasks")
    @JacksonXmlProperty(localName = "tasks", isAttribute = true)
    public void setMaxTasks(Integer maxTasks) {
        this.maxTasks = maxTasks;
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
     * possible values: positive Integer or SIGHUP, SIGINT, SIGQUIT, SIGILL, SIGTRAP, SIGABRT, SIGIOT, SIGBUS, SIGFPE, SIGKILL, SIGUSR1, SIGSEGV, SIGUSR2, SIGPIPE, SIGALRM, SIGTERM, SIGSTKFLT, SIGCHLD, SIGCONT, SIGSTOP, SIGTSTP, SIGTTIN, SIGTTOU, SIGURG, SIGXCPU, SIGXFSZ, SIGVTALRM, SIGPROF, SIGWINCH, SIGPOLL, SIGIO, SIGPWR, SIGSYS
     * 
     * @return
     *     The ignoreSignals
     */
    @JsonProperty("ignoreSignals")
    @JacksonXmlProperty(localName = "ignore_signals", isAttribute = true)
    public String getIgnoreSignals() {
        return ignoreSignals;
    }

    /**
     * possible values: positive Integer or SIGHUP, SIGINT, SIGQUIT, SIGILL, SIGTRAP, SIGABRT, SIGIOT, SIGBUS, SIGFPE, SIGKILL, SIGUSR1, SIGSEGV, SIGUSR2, SIGPIPE, SIGALRM, SIGTERM, SIGSTKFLT, SIGCHLD, SIGCONT, SIGSTOP, SIGTSTP, SIGTTIN, SIGTTOU, SIGURG, SIGXCPU, SIGXFSZ, SIGVTALRM, SIGPROF, SIGWINCH, SIGPOLL, SIGIO, SIGPWR, SIGSYS
     * 
     * @param ignoreSignals
     *     The ignoreSignals
     */
    @JsonProperty("ignoreSignals")
    @JacksonXmlProperty(localName = "ignore_signals", isAttribute = true)
    public void setIgnoreSignals(String ignoreSignals) {
        this.ignoreSignals = ignoreSignals;
    }

    /**
     * 
     * @return
     *     The warnIfLongerThan
     */
    @JsonProperty("warnIfLongerThan")
    @JacksonXmlProperty(localName = "warn_if_longer_than", isAttribute = true)
    public String getWarnIfLongerThan() {
        return warnIfLongerThan;
    }

    /**
     * 
     * @param warnIfLongerThan
     *     The warnIfLongerThan
     */
    @JsonProperty("warnIfLongerThan")
    @JacksonXmlProperty(localName = "warn_if_longer_than", isAttribute = true)
    public void setWarnIfLongerThan(String warnIfLongerThan) {
        this.warnIfLongerThan = warnIfLongerThan;
    }

    /**
     * 
     * @return
     *     The warnIfShorterThan
     */
    @JsonProperty("warnIfShorterThan")
    @JacksonXmlProperty(localName = "warn_if_shorter_than", isAttribute = true)
    public String getWarnIfShorterThan() {
        return warnIfShorterThan;
    }

    /**
     * 
     * @param warnIfShorterThan
     *     The warnIfShorterThan
     */
    @JsonProperty("warnIfShorterThan")
    @JacksonXmlProperty(localName = "warn_if_shorter_than", isAttribute = true)
    public void setWarnIfShorterThan(String warnIfShorterThan) {
        this.warnIfShorterThan = warnIfShorterThan;
    }

    /**
     * possible values: error, warn, info, debug or debug[0-9]
     * 
     * @return
     *     The stderrLogLevel
     */
    @JsonProperty("stderrLogLevel")
    @JacksonXmlProperty(localName = "stderr_log_level", isAttribute = true)
    public String getStderrLogLevel() {
        return stderrLogLevel;
    }

    /**
     * possible values: error, warn, info, debug or debug[0-9]
     * 
     * @param stderrLogLevel
     *     The stderrLogLevel
     */
    @JsonProperty("stderrLogLevel")
    @JacksonXmlProperty(localName = "stderr_log_level", isAttribute = true)
    public void setStderrLogLevel(String stderrLogLevel) {
        this.stderrLogLevel = stderrLogLevel;
    }

    /**
     * 
     * @return
     *     The credentialsKey
     */
    @JsonProperty("credentialsKey")
    @JacksonXmlProperty(localName = "credentials_key", isAttribute = true)
    public String getCredentialsKey() {
        return credentialsKey;
    }

    /**
     * 
     * @param credentialsKey
     *     The credentialsKey
     */
    @JsonProperty("credentialsKey")
    @JacksonXmlProperty(localName = "credentials_key", isAttribute = true)
    public void setCredentialsKey(String credentialsKey) {
        this.credentialsKey = credentialsKey;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     * @return
     *     The loadUserProfile
     */
    @JsonProperty("loadUserProfile")
    @JacksonXmlProperty(localName = "load_user_profile", isAttribute = true)
    public String getLoadUserProfile() {
        return loadUserProfile;
    }

    /**
     * possible values: yes, no, 1, 0, true, false
     * 
     * @param loadUserProfile
     *     The loadUserProfile
     */
    @JsonProperty("loadUserProfile")
    @JacksonXmlProperty(localName = "load_user_profile", isAttribute = true)
    public void setLoadUserProfile(String loadUserProfile) {
        this.loadUserProfile = loadUserProfile;
    }

    /**
     * possible values: yes, no, 1, 0, true, false or never
     * 
     * @return
     *     The visible
     */
    @JsonProperty("visible")
    @JacksonXmlProperty(localName = "visible", isAttribute = true)
    public String getVisible() {
        return visible;
    }

    /**
     * possible values: yes, no, 1, 0, true, false or never
     * 
     * @param visible
     *     The visible
     */
    @JsonProperty("visible")
    @JacksonXmlProperty(localName = "visible", isAttribute = true)
    public void setVisible(String visible) {
        this.visible = visible;
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
     *     The documentation
     */
    @JsonProperty("documentation")
    @JacksonXmlProperty(localName = "description", isAttribute = false)
    public Description getDocumentation() {
        return documentation;
    }

    /**
     * 
     * @param documentation
     *     The documentation
     */
    @JsonProperty("documentation")
    @JacksonXmlProperty(localName = "description", isAttribute = false)
    public void setDocumentation(Description documentation) {
        this.documentation = documentation;
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
     * job script
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
     * job script
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
     * runTime
     * <p>
     * 
     * 
     * @return
     *     The runTime
     */
    @JsonProperty("runTime")
    @JacksonXmlProperty(localName = "run_time", isAttribute = false)
    public RunTime getRunTime() {
        return runTime;
    }

    /**
     * runTime
     * <p>
     * 
     * 
     * @param runTime
     *     The runTime
     */
    @JsonProperty("runTime")
    @JacksonXmlProperty(localName = "run_time", isAttribute = false)
    public void setRunTime(RunTime runTime) {
        this.runTime = runTime;
    }

    /**
     * 
     * @return
     *     The commands
     */
    @JsonProperty("commands")
    @JacksonXmlProperty(localName = "commands", isAttribute = false)
    public List<Commands> getCommands() {
        return commands;
    }

    /**
     * 
     * @param commands
     *     The commands
     */
    @JsonProperty("commands")
    @JacksonXmlProperty(localName = "commands", isAttribute = false)
    public void setCommands(List<Commands> commands) {
        this.commands = commands;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(priority).append(title).append(isOrderJob).append(maxTasks).append(minTasks).append(timeout).append(idleTimeout).append(forceIdleTimeout).append(processClass).append(javaOptions).append(stopOnError).append(ignoreSignals).append(warnIfLongerThan).append(warnIfShorterThan).append(stderrLogLevel).append(credentialsKey).append(loadUserProfile).append(visible).append(settings).append(documentation).append(lockUses).append(params).append(environment).append(login).append(script).append(monitors).append(monitorUses).append(startWhenDirectoriesChanged).append(delayAfterErrors).append(delayOrderAfterSetbacks).append(runTime).append(commands).toHashCode();
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
        return new EqualsBuilder().append(priority, rhs.priority).append(title, rhs.title).append(isOrderJob, rhs.isOrderJob).append(maxTasks, rhs.maxTasks).append(minTasks, rhs.minTasks).append(timeout, rhs.timeout).append(idleTimeout, rhs.idleTimeout).append(forceIdleTimeout, rhs.forceIdleTimeout).append(processClass, rhs.processClass).append(javaOptions, rhs.javaOptions).append(stopOnError, rhs.stopOnError).append(ignoreSignals, rhs.ignoreSignals).append(warnIfLongerThan, rhs.warnIfLongerThan).append(warnIfShorterThan, rhs.warnIfShorterThan).append(stderrLogLevel, rhs.stderrLogLevel).append(credentialsKey, rhs.credentialsKey).append(loadUserProfile, rhs.loadUserProfile).append(visible, rhs.visible).append(settings, rhs.settings).append(documentation, rhs.documentation).append(lockUses, rhs.lockUses).append(params, rhs.params).append(environment, rhs.environment).append(login, rhs.login).append(script, rhs.script).append(monitors, rhs.monitors).append(monitorUses, rhs.monitorUses).append(startWhenDirectoriesChanged, rhs.startWhenDirectoriesChanged).append(delayAfterErrors, rhs.delayAfterErrors).append(delayOrderAfterSetbacks, rhs.delayOrderAfterSetbacks).append(runTime, rhs.runTime).append(commands, rhs.commands).isEquals();
    }

}
