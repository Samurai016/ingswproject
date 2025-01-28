; Script generated by the Inno Setup Script Wizard.
; SEE THE DOCUMENTATION FOR DETAILS ON CREATING INNO SETUP SCRIPT FILES!

#define MyAppName "Progetto Ingegneria del Software"
#define MyAppVersion "4.0.0"
#define MyAppPublisher "Università degli Studi di Brescia"
#define MyAppURL "https://github.com/Samurai016/ingswproject/"
#define MyAppExeName "ingswproject.exe"
#define MyAppId "ingswproject"
#define AppdataFolder "ingsw_project"

[Setup]
; NOTE: The value of AppId uniquely identifies this application. Do not use the same AppId value in installers for other applications.
; (To generate a new GUID, click Tools | Generate GUID inside the IDE.)
AppId={{8E24BAAD-5999-4487-8C63-D14B12EA6A3F}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
AppVerName={#MyAppName} v{#MyAppVersion}
AppPublisher={#MyAppPublisher}
AppPublisherURL={#MyAppURL}
AppSupportURL={#MyAppURL}
AppUpdatesURL={#MyAppURL}
DefaultDirName={autopf}\{#MyAppId}
DisableProgramGroupPage=yes
LicenseFile=LICENSE.md
; Uncomment the following line to run in non administrative install mode (install for current user only.)
;PrivilegesRequired=lowest
PrivilegesRequiredOverridesAllowed=dialog
OutputDir=target
OutputBaseFilename={#MyAppId}-setup
SetupIconFile=docs\logo.ico
Compression=lzma
SolidCompression=yes
WizardStyle=modern
WizardImageFile=wizard\images\wizard-image-250.bmp
WizardSmallImageFile=wizard\images\wizard-smallimage-250.bmp
DisableWelcomePage=no
InfoAfterFile=wizard\infoafter.txt

[Languages]
Name: "italian"; MessagesFile: "compiler:Languages\Italian.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

[Files]
Source: "target\{#MyAppExeName}"; DestDir: "{app}"; Flags: ignoreversion
Source: ".example\*"; DestDir: "{userappdata}\{#AppdataFolder}\"; Flags: createallsubdirs recursesubdirs comparetimestamp
; NOTE: Don't use "Flags: ignoreversion" on any shared system files

[Icons]
Name: "{autoprograms}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"
Name: "{userdesktop}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; Tasks: desktopicon

[Run]
Filename: "{app}\{#MyAppExeName}"; Description: "{cm:LaunchProgram,{#StringChange(MyAppName, '&', '&&')}}"; Flags: nowait postinstall skipifsilent