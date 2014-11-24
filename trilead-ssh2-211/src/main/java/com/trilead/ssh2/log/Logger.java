
package com.trilead.ssh2.log;

/**
 * Logger - a very simple logger, mainly used during development.
 * Is not based on log4j (to reduce external dependencies).
 * However, if needed, something like log4j could easily be
 * hooked in.
 *
 * @author Christian Plattner, plattner@trilead.com
 * @version $Id: Logger.java,v 1.1 2007/10/15 12:49:56 cplattne Exp $
 */

public class Logger
{
	public static final boolean enabled = false;
	public static final int logLevel = 99;

	private final String className;

	public final static Logger getLogger(final Class x)
	{
		return new Logger(x);
	}

	public Logger(final Class x)
	{
		className = x.getName();
	}

	public final boolean isEnabled()
	{
		return enabled;
	}

	public final void log(final int level, final String message)
	{
		if (enabled && level <= logLevel)
		{
			long now = System.currentTimeMillis();

			synchronized (this)
			{
				System.err.println(now + " : " + className + ": " + message);
				// or send it to log4j or whatever...
			}
		}
	}
}
