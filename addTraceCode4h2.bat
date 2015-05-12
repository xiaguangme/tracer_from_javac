@echo off
echo "给h2 databse 增加trace代码用于分析其代码"
if exist workDir rmdir workDir /S /Q
mkdir workDir
cd workDir
set h2SrcPath="D:\600.self\05.code\04.java\09.h2\h2_src_study_4trace\src"
set h2BinPath="D:\600.self\05.code\04.java\09.h2\h2_src_study_4trace\bin"
set srcPath="D:\600.self\05.code\04.java\09.h2\h2_src_study_4trace\src"
set traceCode="org.simonme.tracer.logger.Tracer.traceMethodInvoke"
set batDir=%cd%
cd %h2SrcPath%
dir /s /q /b /a-d *.java>%batDir%\javaFiles4h2.txt
cd %batDir%\
java -jar ../TraceFromJavac.jar @javaFiles4h2.txt -d %h2BinPath% -encoding "UTF-8" -traceCode %traceCode% -srcPath %srcPath%
cd ..
