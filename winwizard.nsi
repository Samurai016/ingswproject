############################################################################################
#      NSIS Installation Script created by NSIS Quick Setup Script Generator v1.09.18
#               Entirely Edited with NullSoft Scriptable Installation System
#              by Vlasis K. Barkas aka Red Wine red_wine@freemail.gr Sep 2006
############################################################################################

!define APP_NAME "Progetto Ingegneria del Software"
!define COMP_NAME "Università degli Studi di Brescia"
!define WEB_SITE "https://github.com/Samurai016/ingswproject/"
!define VERSION "03.00.00.00"
!define COPYRIGHT "Università degli Studi di Brescia  © 2025"
!define DESCRIPTION "Progetto universitario per il corso di Ingegneria del Software (A.A. 2023/24)"
!define LICENSE_TXT "LICENSE.md"
!define MAIN_APP_EXE "ingswproject.exe"
!define INSTALLER_NAME "target\${MAIN_APP_EXE}-setup-nsis.exe"
!define INSTALL_TYPE "SetShellVarContext current"
!define REG_ROOT "HKCU"
!define REG_APP_PATH "Software\Microsoft\Windows\CurrentVersion\App Paths\${MAIN_APP_EXE}"
!define UNINSTALL_PATH "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APP_NAME}"
!define APPDATA_FOLDER "ingsw_project"

!define JRE_VERSION "1.8.0"
!define TEMP $R0
!define TEMP2 $R1
!define TEMP3 $R4
!define VAL1 $R2
!define VAL2 $R3
!define DOWNLOAD_JRE_FLAG $8
!define JRE_URL "wizard\jre-8u431-windows-i586-iftw.exe"

######################################################################

VIProductVersion  "${VERSION}"
VIAddVersionKey "ProductName"  "${APP_NAME}"
VIAddVersionKey "CompanyName"  "${COMP_NAME}"
VIAddVersionKey "LegalCopyright"  "${COPYRIGHT}"
VIAddVersionKey "FileDescription"  "${DESCRIPTION}"
VIAddVersionKey "FileVersion"  "${VERSION}"

######################################################################

SetCompressor ZLIB
Name "${APP_NAME}"
Caption "${APP_NAME}"
OutFile "${INSTALLER_NAME}"
BrandingText "${APP_NAME}"
InstallDirRegKey "${REG_ROOT}" "${REG_APP_PATH}" ""
InstallDir "$PROGRAMFILES\ingswproject"

######################################################################

; Include Modern UI macros
!include "MUI2.nsh"
!include "Sections.nsh"
!include "InstallOptions.nsh"

; Modern UI Configuration
!define MUI_ABORTWARNING
!define MUI_UNABORTWARNING

!define MUI_LANGDLL_REGISTRY_ROOT "${REG_ROOT}"
!define MUI_LANGDLL_REGISTRY_KEY "${UNINSTALL_PATH}"
!define MUI_LANGDLL_REGISTRY_VALUENAME "Installer Language"

; Setup Modern UI pages
!insertmacro MUI_PAGE_WELCOME
!ifdef LICENSE_TXT
    !insertmacro MUI_PAGE_LICENSE "${LICENSE_TXT}"
!endif

; Check JRE Page Configuration
Page custom CheckInstalledJRE
!insertmacro MUI_PAGE_INSTFILES
!define MUI_PAGE_CUSTOMFUNCTION_PRE PreInstallationFiles
!define MUI_PAGE_CUSTOMFUNCTION_LEAVE RestoreSections

; Directory Configuration
!insertmacro MUI_PAGE_DIRECTORY

; Start Menu Configuration
var SM_Folder
!define MUI_STARTMENUPAGE_DEFAULTFOLDER "${APP_NAME}"
!define MUI_STARTMENUPAGE_REGISTRY_ROOT "${REG_ROOT}"
!define MUI_STARTMENUPAGE_REGISTRY_KEY "${UNINSTALL_PATH}"
!define MUI_STARTMENUPAGE_REGISTRY_VALUENAME "StartMenuFolder"
!insertmacro MUI_PAGE_STARTMENU Application $SM_Folder

!insertmacro MUI_PAGE_INSTFILES

; Finish Page Configuration
!define MUI_FINISHPAGE_RUN "$INSTDIR\${MAIN_APP_EXE}"
!define MUI_FINISHPAGE_SHOWREADME ""
!define MUI_FINISHPAGE_SHOWREADME_NOTCHECKED
!define MUI_FINISHPAGE_SHOWREADME_TEXT "Crea collegamento sul desktop"
!define MUI_FINISHPAGE_SHOWREADME_FUNCTION finishpageaction
!insertmacro MUI_PAGE_FINISH

; Uninstaller Configuration
!insertmacro MUI_UNPAGE_CONFIRM
!insertmacro MUI_UNPAGE_INSTFILES
!insertmacro MUI_UNPAGE_FINISH

; Disabled english language by default
; !insertmacro MUI_LANGUAGE "English"
!insertmacro MUI_LANGUAGE "Italian"

!insertmacro MUI_RESERVEFILE_LANGDLL

######################################################################

; Display language selection dialog
Function .onInit
    !insertmacro MUI_LANGDLL_DISPLAY
FunctionEnd

######################################################################

; Create desktop shortcut
Function finishpageaction
    CreateShortCut "$DESKTOP\${APP_NAME}.lnk" "$INSTDIR\${MAIN_APP_EXE}"
FunctionEnd

######################################################################
###### JRE

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
Function CheckInstalledJRE
    Call DetectJRE
    Pop ${TEMP}
    StrCmp ${TEMP} "OK" NoDownloadJRE
    Pop ${TEMP2}
    StrCmp ${TEMP2} "None" NoFound FoundOld

    FoundOld:
        !insertmacro INSTALLOPTIONS_WRITE "jre.ini" "Field 1" "Text" "JRE Test requires a more recent version of the Java Runtime Environment than the one found on your computer. The installation of JRE ${JRE_VERSION} will start."
        !insertmacro MUI_HEADER_TEXT "$(TEXT_JRE_TITLE)" "$(TEXT_JRE_SUBTITLE)"
        !insertmacro INSTALLOPTIONS_DISPLAY_RETURN "jre.ini"
        Goto DownloadJRE

    NoFound:
        !insertmacro INSTALLOPTIONS_WRITE "jre.ini" "Field 1" "Text" "No Java Runtime Environment could be found on your computer. The installation of JRE v${JRE_VERSION} will start."
        !insertmacro MUI_HEADER_TEXT "$(TEXT_JRE_TITLE)" "$(TEXT_JRE_SUBTITLE)"
        !insertmacro INSTALLOPTIONS_DISPLAY "jre.ini"
        Goto DownloadJRE

    DownloadJRE:
        StrCpy ${DOWNLOAD_JRE_FLAG} "Download"
        Return

    NoDownloadJRE:
        Pop ${TEMP2}
        StrCpy ${DOWNLOAD_JRE_FLAG} "NoDownload"
        !insertmacro INSTALLOPTIONS_WRITE "jre.ini" "UserDefinedSection" "JREPath" ${TEMP2}
        Return

    ExitInstall:
        Quit
FunctionEnd
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
Function DetectJRE
    ReadRegStr ${TEMP2} HKLM "SOFTWARE\JavaSoft\Java Runtime Environment" "CurrentVersion"
    StrCmp ${TEMP2} "" DetectTry2
    ReadRegStr ${TEMP3} HKLM "SOFTWARE\JavaSoft\Java Runtime Environment\${TEMP2}" "JavaHome"
    StrCmp ${TEMP3} "" DetectTry2
    Goto GetJRE

    DetectTry2:
        ReadRegStr ${TEMP2} HKLM "SOFTWARE\JavaSoft\Java Development Kit" "CurrentVersion"
        StrCmp ${TEMP2} "" NoFound
        ReadRegStr ${TEMP3} HKLM "SOFTWARE\JavaSoft\Java Development Kit\${TEMP2}" "JavaHome"
        StrCmp ${TEMP3} "" NoFound

    GetJRE:
        IfFileExists "${TEMP3}\bin\java.exe" 0 NoFound
        StrCpy ${VAL1} ${TEMP2} 1
        StrCpy ${VAL2} ${JRE_VERSION} 1
        IntCmp ${VAL1} ${VAL2} 0 FoundOld FoundNew
        StrCpy ${VAL1} ${TEMP2} 1 2
        StrCpy ${VAL2} ${JRE_VERSION} 1 2
        IntCmp ${VAL1} ${VAL2} FoundNew FoundOld FoundNew

    NoFound:
        Push "None"
        Push "NOK"
        Return

    FoundOld:
        Push ${TEMP2}
        Push "NOK"
        Return

    FoundNew:
        Push "${TEMP3}\bin\java.exe"
        Push "OK"
        Return
FunctionEnd
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
Function RestoreSections
  !insertmacro UnselectSection ${jre}
  !insertmacro SelectSection ${SecJRETest}
  !insertmacro SelectSection ${SecCreateShortcut}
FunctionEnd
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
Function SetupSections
  !insertmacro SelectSection ${jre}
  !insertmacro UnselectSection ${SecJRETest}
  !insertmacro UnselectSection ${SecCreateShortcut}
FunctionEnd
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
Function PreInstallationFiles
  Call RestoreSections
  SetAutoClose true
FunctionEnd
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; 

######################################################################

Section -MainProgram
    ${INSTALL_TYPE}
    SetOverwrite ifnewer
    SetOutPath "$INSTDIR"
    File "target\${MAIN_APP_EXE}"

    ; Example files to AppData
    SetOutPath "$APPDATA\${APPDATA_FOLDER}"
    File /r ".example\*"
SectionEnd

######################################################################

Section -Icons_Reg
    SetOutPath "$INSTDIR"
    WriteUninstaller "$INSTDIR\uninstall.exe"

    !insertmacro MUI_STARTMENU_WRITE_BEGIN Application
        CreateDirectory "$SMPROGRAMS\$SM_Folder"
        CreateShortCut "$SMPROGRAMS\$SM_Folder\${APP_NAME}.lnk" "$INSTDIR\${MAIN_APP_EXE}"
    !insertmacro MUI_STARTMENU_WRITE_END

    WriteRegStr ${REG_ROOT} "${REG_APP_PATH}" "" "$INSTDIR\${MAIN_APP_EXE}"
    WriteRegStr ${REG_ROOT} "${UNINSTALL_PATH}"  "DisplayName" "${APP_NAME}"
    WriteRegStr ${REG_ROOT} "${UNINSTALL_PATH}"  "UninstallString" "$INSTDIR\uninstall.exe"
    WriteRegStr ${REG_ROOT} "${UNINSTALL_PATH}"  "DisplayIcon" "$INSTDIR\${MAIN_APP_EXE}"
    WriteRegStr ${REG_ROOT} "${UNINSTALL_PATH}"  "DisplayVersion" "${VERSION}"
    WriteRegStr ${REG_ROOT} "${UNINSTALL_PATH}"  "Publisher" "${COMP_NAME}"

    !ifdef WEB_SITE
        WriteRegStr ${REG_ROOT} "${UNINSTALL_PATH}"  "URLInfoAbout" "${WEB_SITE}"
    !endif
SectionEnd

######################################################################

Section Uninstall
    ${INSTALL_TYPE}
    Delete "$INSTDIR\${MAIN_APP_EXE}"
    Delete "$INSTDIR\application.yaml"
    Delete "$INSTDIR\uninstall.exe"
    RmDir "$INSTDIR"

    Delete "$SMPROGRAMS\$SM_Folder\${APP_NAME}.lnk"
    Delete "$DESKTOP\${APP_NAME}.lnk"
    RmDir "$SMPROGRAMS\$SM_Folder"

    DeleteRegKey ${REG_ROOT} "${REG_APP_PATH}"
    DeleteRegKey ${REG_ROOT} "${UNINSTALL_PATH}"
SectionEnd

######################################################################

Function un.onInit
    !insertmacro MUI_UNGETLANGUAGE
FunctionEnd

######################################################################

