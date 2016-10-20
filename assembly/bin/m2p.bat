@set BIN=%~dp0

@set CLASSPATH="%BIN%\..\conf"

@set JAVA_OPTS="-server -Xss412k -Xms512m -Xmx512m -XX:MaxPermSize=256m"

@for %%a in (%BIN%\..\lib\*.jar) do @call %BIN%\add2cp.bat %%a

@set MAIN=com.shvid.m2p.MainStart

@java -classpath %CLASSPATH% %JAVA_OPTS% -Dcurrent.dir=%BIN% %MAIN% %1

@pause