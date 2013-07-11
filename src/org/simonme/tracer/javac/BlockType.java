/*
 * 文 件 名:  BlockType.java
 * 描    述:  <描述>
 * 创 建 人:  Chenxiaguang
 * 创建时间: 2013-7-11
 * 修 改 人:  
 * 修改时间: 
 * 修改内容:  <修改内容>
 */
package org.simonme.tracer.javac;

/**
 * <一句话功能简述>
 * 块类型，有可能是方法，有可能是独立的，有可能是if的块
 * <功能详细描述>
 * 
 * @author  Chenxiaguang
 * @version [版本号, 2013-7-11]
 * @see     [相关类/方法]
 * @since   [产品/模块版本]
 */
public enum BlockType
{
    METHOD, TRY, SYNCHRONIZED, CATCH_CLAUSE, CLASS_OR_INTERFACE_BODY, OTHER;
}
