package com.pl3x.rominstaller;

import com.pl3x.rominstaller.utils.Constants;
import android.content.Context;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;

public class Helper {
    public static Process runSuCommandAsync(Context context, String command) throws IOException {
        DataOutputStream fout = new DataOutputStream(context.openFileOutput(Constants.SCRIPT_NAME, 0));
        fout.writeBytes(command);
        fout.close();
        String[] args = new String[] { "su", "-c", ". " + context.getFilesDir().getAbsolutePath() + "/" + Constants.SCRIPT_NAME };
        Process proc = Runtime.getRuntime().exec(args);
        return proc;
    }

    public static int runSuCommand(Context context, String command) throws IOException, InterruptedException {
        return runSuCommandAsync(context, command).waitFor();
    }

    public static int runSuCommandNoScriptWrapper(Context context, String command) throws IOException, InterruptedException {
        String[] args = new String[] { "su", "-c", command };
        Process proc = Runtime.getRuntime().exec(args);
        return proc.waitFor();
    }

    public static String[] getDirList(Context context, String s) {
        File f = new File(s);
        String[] dirList = new String[1];
        dirList[0] = context.getString(R.string.no_backups_found);
        if (f.exists()) {
            String[] l = f.list(filter);
            if (l != null) {
                String[] tmp = new String[l.length + 1];
                int dirs = 0;
                for (String dir : l) {
                    if ((new File(s + "/" + dir).isDirectory())) {
                        tmp[dirs] = dir; dirs++;
                    }
                }
                if (dirs > 0) {
                    dirList = new String[dirs];
                    for (int i = 0; i < dirList.length; i++) {
                        dirList[i] = tmp[i];
                    }
                }
            }
        }
        Arrays.sort(dirList); return dirList;
    }

    public static String[] getDirListFullPath(String s) {
        // Strip any trailing "../" items
        String[] q = s.split("/");
        if (q[q.length - 1].equals("..")) {
            String[] r = s.split("/");
            StringBuffer sb = new StringBuffer();
            if (r.length > 2) {
                sb.append(r[0]);
                for (int i=1; i<(r.length-2); i++) {
                    sb.append("/");
                    sb.append(r[i]);
                }
            } else {
                sb.append("/");
            }
            s = sb.toString(); 
        }
        File f = new File(s);
        String[] dirList = new String[1];
        String[] fileList = new String[1];
        String[] finalList = new String[1];
        if (f.exists()) {
            if (f.isDirectory()) {
                String[] l = f.list(filter);
                if (l != null) {
                    String[] tmp = new String[l.length + 1];
                    int dirs = 0;
                    String[] tmpf = new String[l.length + 1];
                    int files = 0;
                    if (!s.equals(Constants.SDCARD)) {
                        // Add the "parent folder" item to go back
                        tmp[0] = s + "/" + "..";
                        dirs = 1;
                    }
                    for (String dir : l) {
                        if (new File(s + "/" + dir).canRead()) {
                            if (new File(s + "/" + dir).isDirectory()) {
                                if (s.equals("/")) {
                                    tmp[dirs] = "/" + dir;
                                } else {
                                    tmp[dirs] = s + "/" + dir;
                                }
                                dirs++;
                            } else if (isZip(s + "/" + dir)) {
                                tmpf[files] = s + "/" + dir;
                                files++;
                            }
                        }
                    }
                    dirList = new String[dirs];
                    for (int i = 0; i < dirs; i++) {
                        dirList[i] = tmp[i];
                    }
                    fileList = new String[files];
                    for (int i = 0; i < files; i++) {
                        fileList[i] = tmpf[i];
                    }
                }
            }
        }
        Arrays.sort(dirList);
        Arrays.sort(fileList);
        finalList = new String[dirList.length + fileList.length];
        for (int i = 0; i < finalList.length; i++) {
            finalList[i] = (i < dirList.length) ? dirList[i] : fileList[i - dirList.length];
        }
        return finalList;
    }

    public static boolean isZip(String s) {
        if (s.contains(".")) {
            String[] r = s.split("\\.");
            String ext = r[r.length - 1];
            if (ext.toUpperCase().equals("ZIP")) {
                return true;
            }
        }
        return false;
    }

    private static FilenameFilter filter = new FilenameFilter() { public boolean accept(File dir, String name) { return !name.startsWith("."); } };
    
}