Set WshShell = WScript.CreateObject("WScript.Shell")
WshShell.SendKeys "^%c"
Wscript.Sleep(1000)
WshShell.SendKeys "{ENTER}"
Wscript.Sleep(1000)
WshShell.SendKeys "Y"
WshShell.SendKeys "{ENTER}"
Wscript.Sleep(1000)
WshShell.SendKeys "exit"
Set WshShell = nothing