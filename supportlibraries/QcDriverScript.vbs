'General Header
'#######################################################################################################################
'Script Description		: Script to invoke the corresponding test script in the local machine
'Test Tool/Version		: VAPI-XP
'Test Tool Settings		: N.A.
'Application Automated	: Mercury Tours
'Author					: Cognizant
'Date Created			: 26/12/2012
'#######################################################################################################################
Option Explicit	'Forcing Variable declarations

'#######################################################################################################################
'Class Description   	: DriverScript class
'Author					: Cognizant
'Date Created			: 09/11/2012
'#######################################################################################################################
Class DriverScript
	
	Private m_strRelativePath
	Private m_blnDebug, m_objCurrentTestSet, m_objCurrentTSTest, m_objCurrentRun
	Private m_strCurrentTestSetName, m_strTimeStamp
	Private m_strCurrentTestParentFolder, m_strCurrentTestName
	Private m_strTestStatus
	
	'###################################################################################################################
	Public Property Let RelativePath(strRelativePath)
		m_strRelativePath = strRelativePath
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Let Debug(blnDebug)
		m_blnDebug = blnDebug
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Set CurrentTestSet(objCurrentTestSet)
		Set m_objCurrentTestSet = objCurrentTestSet
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Set CurrentTSTest(objCurrentTSTest)
		Set m_objCurrentTSTest = objCurrentTSTest
	End Property
	'###################################################################################################################
	
	'###################################################################################################################
	Public Property Set CurrentRun(objCurrentRun)
		Set m_objCurrentRun = objCurrentRun
	End Property
	'###################################################################################################################
	
	
	'###################################################################################################################
	'Function Description   : Function to drive the test execution
	'Input Parameters 		: None
	'Return Value    		: None
	'Author					: Cognizant
	'Date Created			: 11/10/2012
	'###################################################################################################################
   	Public Function DriveTestExecution()
		InvokeBatchFile()
		WrapUp()
		DriveTestExecution = m_strTestStatus
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	Private Sub InvokeBatchFile()
		Dim strScriptPath
		strScriptPath = m_strRelativePath & "\allocator\RunTest.bat"
		
		m_strCurrentTestSetName = m_objCurrentTestSet.Name
		
		m_strTimeStamp = Replace(m_objCurrentRun.Name, " ", "_")
		m_strTimeStamp = Replace(m_strTimeStamp, "/", "_")
		m_strTimeStamp = Replace(m_strTimeStamp, ":", "_")
		
		Dim objTestFactory: Set objTestFactory = TDConnection.TestFactory
		Dim objCurrentTest: Set objCurrentTest = objTestFactory.Item(m_objCurrentTSTest.Field("TC_TEST_ID"))
		
		m_strCurrentTestParentFolder = objCurrentTest.Field("TS_SUBJECT").Name
		
		m_strCurrentTestName = objCurrentTest.Name
		
		Dim strCurrentTestDescription
		TDConnection.IgnoreHTMLFormat = True
		strCurrentTestDescription = objCurrentTest.Field("TS_DESCRIPTION")
		
		Dim strIterationMode, intStartIteration, intEndIteration 
		strIterationMode = m_objCurrentTSTest.Params.ParamValue("IterationMode")
		intStartIteration = m_objCurrentTSTest.Params.ParamValue("StartIteration")
		intEndIteration = m_objCurrentTSTest.Params.ParamValue("EndIteration")
		
		Dim strBrowser, strBrowserVersion, strPlatform
		strBrowser = m_objCurrentTSTest.Params.ParamValue("Browser")
		strBrowserVersion = m_objCurrentTSTest.Params.ParamValue("BrowserVersion")
		strPlatform = m_objCurrentTSTest.Params.ParamValue("Platform")
		
		Dim strCommandLineArguments
		strCommandLineArguments = """" & m_strCurrentTestSetName & """ " & m_strTimeStamp & " """ &_
									m_strCurrentTestParentFolder & """ """ & m_strCurrentTestName & """ """ &_
									strCurrentTestDescription & """ " &_
									strIterationMode & " " & intStartIteration & " " & intEndIteration & " " &_
									strBrowser & " " & strBrowserVersion & " " & strPlatform
		
		XTools.run strScriptPath, strCommandLineArguments
	End Sub
	'###################################################################################################################
	
	'###################################################################################################################
	Private Sub WrapUp()
		UpdateCurrentTestStatus()
		UploadResultsToQc()
		DeleteTempResults()
	End Sub
	'###################################################################################################################
	
	'###################################################################################################################
	Private Sub UpdateCurrentTestStatus()
		m_strTestStatus = GetTestStatusFromRegistry()
	End Sub
	'###################################################################################################################
	
	'###################################################################################################################
	Private Function GetTestStatusFromRegistry()
		Dim objShell: Set objShell = CreateObject("WScript.Shell")
		GetTestStatusFromRegistry =_
						objShell.RegRead("HKEY_CURRENT_USER\Software\Cognizant\Framework\QC Integration\TestStatus")
		
		Set objShell = Nothing
	End Function
	'###################################################################################################################
	
	'###################################################################################################################
	Private Sub UploadResultsToQc()
		TDOutput.Print "Uploading result files..."
		
		Dim strReportPath, strReportName
		strReportPath = m_strRelativePath & "\Results\" & m_strCurrentTestSetName & "\" & m_strTimeStamp
		strReportName = m_strCurrentTestParentFolder & "_" & m_strCurrentTestName
		
		Dim objFso: Set objFso = CreateObject("Scripting.FileSystemObject")
		
		If objFso.FolderExists(strReportPath & "\Excel Results") Then
			AttachFileToTestRun strReportPath & "\Excel Results\" & strReportName & ".xls"
		End If
		
		If objFso.FolderExists(strReportPath & "\HTML Results") Then
			AttachFileToTestRun strReportPath & "\HTML Results\" & strReportName & ".html"
		End If
		
		AttachFolderToTestRun strReportPath & "\Screenshots"
		
		If objFso.FolderExists(strReportPath & "\Datatables") Then
			AttachFileToTestRun strReportPath & "\Datatables\" & m_strCurrentTestParentFolder & ".xls"
			AttachFileToTestRun strReportPath & "\Datatables\Common Testdata.xls"
		End If
		
		If Err.Number = 0 Then
			TDOutput.Print "Result files uploaded successfully!"
		End If
		
		Set objFso = Nothing
	End Sub
	'###################################################################################################################
	
	'###################################################################################################################
	Private Sub AttachFileToTestRun(strFilePath)
		Dim objFso: Set objFso = CreateObject("Scripting.FileSystemObject")
		If Not objFso.FileExists(strFilePath) Then
			TDOutput.Print "The file to be attached (" & strFilePath & ") is not found!"
			m_strTestStatus = "Failed"
			Exit Sub
		End If
		Set objFso = Nothing
		
		Dim objFoldAttachments: Set objFoldAttachments = m_objCurrentRun.Attachments
		Dim objFoldAttachment: Set objFoldAttachment = objFoldAttachments.AddItem(Null)
		objFoldAttachment.FileName = strFilePath
		objFoldAttachment.Type = 1
		objFoldAttachment.Post
		
		Set objFoldAttachment = Nothing
		Set objFoldAttachments = Nothing
	End Sub
	'###################################################################################################################
	
	'###################################################################################################################
	Private Sub AttachFolderToTestRun(strFolderPath)
		Dim objFso: Set objFso = CreateObject("Scripting.FileSystemObject")
		Dim objFolder: Set objFolder = objFso.GetFolder(strFolderPath)
		Dim objFileList: Set objFileList = objFolder.Files
		Dim objFile
		For each objFile in objFileList
			AttachFileToTestRun objFile.Path
		Next
		
		'Release all objects
		Set objFile = Nothing
		Set objFileList = Nothing
		Set objFolder = Nothing
		Set objFso = Nothing
	End Sub
	'###################################################################################################################
	
	'###################################################################################################################
	Private Sub DeleteTempResults()
		Dim strReportPath
		strReportPath = m_strRelativePath & "\Results\" & m_strCurrentTestSetName & "\" & m_strTimeStamp
		
		Dim objFso: Set objFso = CreateObject("Scripting.FileSystemObject")
		objFso.DeleteFolder(strReportPath)
		
		If Err.Number = 0 Then
			TDOutput.Print "Temp result files deleted from local machine."
		End If
		
		Set objFso = Nothing
	End Sub
	'###################################################################################################################
	
End Class
'#######################################################################################################################