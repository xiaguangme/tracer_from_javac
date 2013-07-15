/*
 * 文 件 名:  Hello.java
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
 * <功能详细描述>
 * 
 * @author  Chenxiaguang
 * @version [版本号, 2013-7-11]
 * @see     [相关类/方法]
 * @since   [产品/模块版本]
 */
public class Hello
{
    
    public Hello()
    {
        
    }
    
    /** 
     * <一句话功能简述>
     * <功能详细描述>
     * @param args
     * @see [类、类#方法、类#成员]
     */
    public static void main(String[] args)
    {
        
     
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(args);
	}
    
    public void m1(){ 
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	};
    
    public int m2(){
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke();
	return 1;    
    }
    
    public String m3(String a1, String... a2)
    {
         
		org.simonme.tracer.trace.Tracer.traceMethodInvoke(a1, a2);
	try{}catch(Exception e)
        {
          //
            
  
        }
        finally
        {
            //
        }
        return a1;
    }
    
    public static class TestSClass
    {
        public TestSClass()
        {}
    }
}
                                                                                                                                                                      