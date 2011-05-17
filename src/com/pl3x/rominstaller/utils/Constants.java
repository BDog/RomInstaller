package com.pl3x.rominstaller.utils;

public interface Constants {
    public static final String CACHE_FILE = "/cache/recovery/extendedcommand";
    public static final String SCRIPT_NAME = "script.sh";
    public static final String VERSION_LINE = "Rom Installer v1.0.0 by Pl3x";

    public static final String SDCARD = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String DOWNLOAD_DIR = SDCARD + "/clockworkmod/download/";
    public static final String BACKUP_DIR = SDCARD + "/clockworkmod/backup/";

    // Hard code /sdcard/ for CWM's needs...
    public static final String CWM_BACKUP_DIR = "/sdcard/clockworkmod/backup/";
}
