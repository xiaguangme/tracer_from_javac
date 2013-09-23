/*
 * 文 件 名:  ISkip.java
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
 * 用户自行实现不需要追踪的场景
 * <功能详细描述>
 * 
 * @author  Chenxiaguang
 * @version [版本号, 2013-9-21]
 * @see     [相关类/方法]
 * @since   [产品/模块版本]
 */
public interface ISkip
{
    /**
     * 
     * <一句话功能简述>
     * 返回true时 表示不追踪这次方法调用
     * <功能详细描述>
     * @param log
     * @param args
     * @return
     * @see [类、类#方法、类#成员]
     */
    boolean skipTraceMethodInvoke(String log, Object... args);
}
