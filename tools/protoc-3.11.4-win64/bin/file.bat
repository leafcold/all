cd /d %~dp0
::./protoc.exe -I=../file/ --java_out=../../../common/src/main/java/ ../file/addressbook.proto
protoc.exe -I=../file/ --java_out=../../../common/src/main/java/ ../file/move.proto
pause
