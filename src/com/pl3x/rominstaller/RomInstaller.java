package com.pl3x.rominstaller;

import com.pl3x.rominstaller.utils.Constants;
import com.google.ads.AdView;
import com.google.ads.AdSize;
import com.google.ads.AdRequest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RomInstaller extends Activity {
    public String mCurrentDir;
    public Queue sdQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rominstaller);

        // Get Google Ads
        AdView adView = new AdView(this, AdSize.BANNER, "a14dcc557197b4b");
        ((LinearLayout)findViewById(R.id.LinearLayout01)).addView(adView);
        adView.loadAd(new AdRequest());

        // Check for CWM recovery
          // XXX: TODO: Might just bypass this altogether :/

        // Check for mounted SDCard
        if (!(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage(R.string.sdcard_not_mounted)
                .setTitle(R.string.no_sdcard)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        RomInstaller.this.finish();
                    }
                });
            dialog.create().show();
        }

        // Install from Internet button
        this.findViewById(R.id.btn_download).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /* TODO */
            }
        });

        // Install from SDCard button
        this.findViewById(R.id.btn_install).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sdQueue = new Queue(RomInstaller.this);
                installFromSDCardDialog(Constants.SDCARD);
            }
        });

        // Backup button (opens dialog)
        this.findViewById(R.id.btn_backup).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(RomInstaller.this);
                dialog.setMessage(R.string.sure_to_backup)
                    .setTitle(R.string.btn_backup)
                    .setCancelable(false)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Create extendedcommand and reboot to recovery to perform backup.
                            Queue queue = new Queue(RomInstaller.this);
                            queue.addQue("backup_rom " + Constants.BACKUP_DIR + new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss").format(new Date()));
                            queue.writeToCache();
                            queue.rebootRecovery();
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                dialog.create().show();
            }
        });

        // Restore button (opens dialog)
        this.findViewById(R.id.btn_restore).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String[] items = Helper.getDirList(RomInstaller.this, Constants.BACKUP_DIR);
                AlertDialog.Builder dialog = new AlertDialog.Builder(RomInstaller.this);
                if (!items[0].equals(getString(R.string.no_backups_found))) {
                    // Backups found. Display them.
                    dialog.setTitle(R.string.btn_restore)
                        .setCancelable(true)
                        .setItems(items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Create extendedcommand and reboot to recovery to perform restore
                                Queue queue = new Queue(RomInstaller.this);
                                queue.addQue("restore_rom " + Constants.BACKUP_DIR + items[id] + " boot system data cache sd-ext");
                                queue.writeToCache();
                                queue.rebootRecovery();
                            }
                        });
                } else {
                    // No backups found.
                    dialog.setMessage(R.string.no_backups_found)
                        .setTitle(R.string.btn_restore)
                        .setCancelable(true)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                }
                dialog.create().show();
            }
        });
    }

    // Create the Options Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    // Actions for Options Menu items
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.manual_recovery:
                // TODO
                break;
            case R.id.clear_download_cache:
                // TODO
                break;
            case R.id.donate:
                // TODO
                break;
        }
        return true;
    }

    public void installFromSDCardDialog(String path) {
        if (new File(path).isDirectory()) {
            mCurrentDir = path;
            ListAdapter listAdapter = new ListAdapter(RomInstaller.this, Helper.getDirListFullPath(path));
            AlertDialog.Builder dialog = new AlertDialog.Builder(RomInstaller.this);
            dialog.setTitle(R.string.locate_zip)
                .setCancelable(true)
                .setAdapter(listAdapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String[] dir = Helper.getDirListFullPath(mCurrentDir);
                        installFromSDCardDialog(dir[id]);
                    }
                });
            dialog.create().show();
        } else {
            // TODO: start ZIP integrity checks and installation dialogs
            Toast.makeText(RomInstaller.this, "Installing Rom\n\n\n(coming soon)", Toast.LENGTH_LONG).show();
        }
    }

    public void preZipInstallation() {
        // TODO: dialog
    }
}
