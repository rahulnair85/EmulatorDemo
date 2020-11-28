@echo off

rem Use %SendKeys% to send keys to the keyboard buffer
set SendKeys=CScript //nologo //E:JScript "%~F0"

%SendKeys% "echo off^C"

set /P "=Wait and send a command: " < NUL
ping -n 5 -w 1 127.0.0.1 > NUL
%SendKeys% "echo Y{ENTER}"


set /P "=] Wait and send an Enter key:" < NUL
ping -n 5 -w 1 127.0.0.1 > NUL
%SendKeys% "{ENTER}"

%SendKeys% "exit{ENTER}"

goto :EOF


@end


// JScript section

var WshShell = WScript.CreateObject("WScript.Shell");
WshShell.SendKeys(WScript.Arguments(0));