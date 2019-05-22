package com.taotao.log;

import com.taotao.log.ILogFile;

/**
 * <p>
 * Log Factory class is to create different loggers corresponding different
 * requirements.
 * </p>
 * 
 */
public class LogFileFactory {

	/**
	 * <p>
	 * Create the log object by its corresponding name.
	 * </p>
	 * 
	 * @param logName
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public static ILogFile createLog(String logName)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		return (ILogFile) Class.forName(logName).newInstance();
	}

}