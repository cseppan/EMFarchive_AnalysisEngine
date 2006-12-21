@echo off
::  Batch file to run the MIMS TableApp java program on Windows-NT/98/95

::  set TABLEAPPHOME to the location where MIMS is installed
set TABLEAPPHOME=<home-dir>
set R_HOME=<r-home>
set JAVA_HOME=<java-home>


::  add java bin directory to the path (prefered 1.5)
set PATH=%PATH%;%JAVA_HOME%\bin

:: add location of R executable to PATH
set PATH=%PATH%;%R_HOME%\bin

::  set needed jar files in CLASSPATH
set CLASSPATH=%TABLEAPPHOME%\analysis-engine.jar
set CLASSPATH=%CLASSPATH%;%TABLEAPPHOME%\lib\colt.jar
set CLASSPATH=%CLASSPATH%;%TABLEAPPHOME%\lib\cosu.jar;
set CLASSPATH=%CLASSPATH%;%TABLEAPPHOME%\lib\jlfgr-1_0.jar
set CLASSPATH=%CLASSPATH%;%TABLEAPPHOME%\lib\java_cup.jar;
set CLASSPATH=%CLASSPATH%;%TABLEAPPHOME%\lib\jh.jar
set CLASSPATH=%CLASSPATH%;%TABLEAPPHOME%\lib\connectorJ.jar
set CLASSPATH=%CLASSPATH%;%TABLEAPPHOME%\lib\weka.jar
set CLASSPATH=%CLASSPATH%;%TABLEAPPHOME%\lib\xstream-1.2.1.jar

::  argument definitions
::  -fileName <name>
::  -fileType <type>
::  -delimiter <delimiter>
::  -hRows <noOfColumnHeaderRows>
::  -startPos <integer>
::  -endtPos <integer>
::
:: Detail Description
:: name = name of the file;
:: type = one of these types: TRIM.FaTE Results File, TRIM.FaTE Sensitivity File, Generic Delimited File, TRIM.FaTE Monte Carlo Inputs, COSU File, DAVE Output File, SMOKE Report File
:: delimiter= a single character; Required only for DAVE Output files and Generic files, Optional for Smoke Report File
:: noOfColumnHeaderRows = An integer value indicates the number of column header rows in the file including the units rows: Required only for "Generic files"

@echo on

java -Xmx256M -classpath %CLASSPATH% -DUSER_PREFERENCES="%TABLEAPPHOME%\User_Prefs.txt" -DR_HOME="%R_HOME%\bin" gov.epa.mims.analysisengine.table.TableApp
