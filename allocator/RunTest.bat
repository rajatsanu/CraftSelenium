@ECHO OFF

SET TESTSETNAME=%~1
SET TIMESTAMP=%~2
SET CURRENTSCENARIO=%~3
SET CURRENTTESTCASE=%~4
SET CURRENTTESTDESC=%~5
SET ITERATIONMODE=%~6
SET STARTITERATION=%~7
SET ENDITERATION=%~8
SET BROWSER=%~9
SHIFT
SHIFT
SHIFT
SHIFT
SHIFT
SHIFT
SHIFT
SHIFT
SHIFT
SET BROWSERVERSION=%~1
SET PLATFORM=%~2

@ECHO ON

D:
cd "D:\Vijay\Eclipse Workspace\CRAFT_SelWebDrv"
java -cp ".;.\supportlibraries\Framework_Core.jar;.\supportlibraries\Framework_DataTable.jar;.\supportlibraries\Framework_Reporting.jar;.\supportlibraries\Framework_Utilities.jar;D:\Vijay\Javalibs\poi-3.8\poi-3.8-20120326.jar;D:\Vijay\Javalibs\Selenium\Selenium Server\selenium-server-standalone-2.31.0.jar" allocator.QcTestRunner "%TESTSETNAME%" %TIMESTAMP% "%CURRENTSCENARIO%" "%CURRENTTESTCASE%" "%CURRENTTESTDESC%" %ITERATIONMODE% %STARTITERATION% %ENDITERATION% %BROWSER% %BROWSERVERSION% %PLATFORM%