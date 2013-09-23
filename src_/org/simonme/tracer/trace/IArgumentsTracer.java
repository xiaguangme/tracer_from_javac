/*
 * 文 件 名:  IArgumentsTracer.java
 * 描    述:  <描述>
 * 创 建 人:  Chenxiaguang
 * 创建时间: 2013-9-21
 * 修 改 人:  
 * 修改时间: 
 * 修改内容:  <修改内容>
 */
package org.simonme.tracer.trace;

import java.util.List;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  Chenxiaguang
 * @version [版本号, 2013-9-21]
 * @see     [相关类/方法]
 * @since   [产品/模块版本]
 */
public interface IArgumentsTracer
{
    void traceArguments(List<String> logs, Object... args);
}
