/*
 * 文 件 名:  ITracerConfig.java
 * 描    述:  <描述>
 * 创 建 人:  Chenxiaguang
 * 创建时间: 2013-9-21
 * 修 改 人:  
 * 修改时间: 
 * 修改内容:  <修改内容>
 */
package org.simonme.tracer.trace;

/**
 * <一句话功能简述>
 * Tracer的配置接口
 * <功能详细描述>
 * 
 * @author  Chenxiaguang
 * @version [版本号, 2013-9-21]
 * @see     [相关类/方法]
 * @since   [产品/模块版本]
 */
public interface ITracerConfig
{
    /**
     * <一句话功能简述>
     * trace log日志的存放路径
     * <功能详细描述>
     * @return
     * @see [类、类#方法、类#成员]
     */
    String getTraceLogFilePath();
    
    /**
     * <一句话功能简述>
     * 达到多少条trace log的时候做flush动作 
     * <功能详细描述>
     * @return
     * @see [类、类#方法、类#成员]
     */
    int getTracerLogBufferSize();
}
