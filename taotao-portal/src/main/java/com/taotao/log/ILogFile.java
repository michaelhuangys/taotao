package com.taotao.log;

/**
 * <p>
 * This interface is to write different logs into corresponding files.
 * </p>
 */
public interface ILogFile {

	/**
	 * <p>
	 * Write data self-defined into the certain log.
	 * </p>
	 * 
	 * @param data
	 * @throws LogException
	 */
	public void writeLog(String data);

}
