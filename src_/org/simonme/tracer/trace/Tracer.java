
package org.simonme.tracer.trace;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Tracer
{
    
    static List<String> logs = new ArrayList<String>();
    
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS");
    
    public static void traceMethodInvoke(Object... args)
    {
        if(Thread.currentThread().getStackTrace() == null
            || Thread.currentThread().getStackTrace().length < 3)
        {
            return;
        }
        String log = Thread.currentThread().getStackTrace()[2].toString();
        if(log.startsWith("com.sun.tools.javac.file.ZipFileIndex"))
        {
            return;
        }
        
        if(log.startsWith("com.sun.tools.javac.file.RelativePath"))
        {
            return;
        }
        
            
        logs.add(log);
        logs.add("\n");
//        for(Object arg : args){}
        writeLog(1024000);
        
	}
    
    public static void flush()
    {
        writeLog(0);
    }


    /** 
     * <一句话功能简述>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    protected static void writeLog(int limit)
    {
        if(logs.size() >= limit)
        {
            String fileName = "d:/compile_log/" + sdf.format(new Date(System.currentTimeMillis())) +".log";
            
            FileWriter fw = null;
            
            BufferedWriter bw = null;
            
            
            {
                try
                {
                    fw = new FileWriter(fileName);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                bw = new BufferedWriter(fw);
            }
            try
            {
                for(String s : logs)
                {
                    bw.write(s);
                }
                bw.flush();
                logs.clear();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            if(fw != null)
            {
                try
                {
                    fw.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            
            if(bw != null)
            {
                try
                {
                    bw.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
                                                                                                                              