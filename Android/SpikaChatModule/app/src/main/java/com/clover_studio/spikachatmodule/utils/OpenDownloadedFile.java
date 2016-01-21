package com.clover_studio.spikachatmodule.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.clover_studio.spikachatmodule.R;
import com.clover_studio.spikachatmodule.dialogs.NotifyDialog;

import java.io.File;

/**
 * Created by ubuntu_ivo on 13.08.15..
 */
public class OpenDownloadedFile {

    /**
     *
     * dialog for open file
     *
     * @param file file to open
     * @param context
     */
    public static void downloadedFileDialog(final File file, final Context context){
        NotifyDialog dialog = NotifyDialog.startConfirm(context, context.getString(R.string.file_downloaded), context.getString(R.string.open_file));
        dialog.setButtonsText(context.getString(R.string.NO_CAPITAL), context.getString(R.string.YES_CAPITAL));
        dialog.setTwoButtonListener(new NotifyDialog.TwoButtonDialogListener() {
            @Override
            public void onOkClicked(NotifyDialog dialog) {
                Uri uri = Uri.fromFile(file);
                Intent intent = new Intent(Intent.ACTION_VIEW);

                if (uri.toString().contains(".doc") || uri.toString().contains(".docx")) {
                    // Word document
                    intent.setDataAndType(uri, "application/msword");
                } else if (uri.toString().contains(".pdf")) {
                    // PDF file
                    intent.setDataAndType(uri, "application/pdf");
                } else if (uri.toString().contains(".ppt") || uri.toString().contains(".pptx")) {
                    // Powerpoint file
                    intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
                } else if (uri.toString().contains(".xls") || uri.toString().contains(".xlsx")) {
                    // Excel file
                    intent.setDataAndType(uri, "application/vnd.ms-excel");
                } else if (uri.toString().contains(".zip")) {
                    // ZIP audio file
                    intent.setDataAndType(uri, "application/zip");
                } else if (uri.toString().contains(".rar")) {
                    // ZIP audio file
                    intent.setDataAndType(uri, "application/x-rar-compressed");
                } else if (uri.toString().contains(".gz")) {
                    // ZIP audio file
                    intent.setDataAndType(uri, "application/gzip");
                } else if (uri.toString().contains(".rtf")) {
                    // RTF file
                    intent.setDataAndType(uri, "application/rtf");
                } else if (uri.toString().contains(".wav") || uri.toString().contains(".mp3")) {
                    // WAV audio file
                    intent.setDataAndType(uri, "audio/x-wav");
                } else if (uri.toString().contains(".gif")) {
                    // GIF file
                    intent.setDataAndType(uri, "image/gif");
                } else if (uri.toString().contains(".jpg") || uri.toString().contains(".jpeg") || uri.toString().contains(".png")) {
                    // JPG file
                    intent.setDataAndType(uri, "image/jpeg");
                } else if (uri.toString().contains(".txt")) {
                    // Text file
                    intent.setDataAndType(uri, "text/plain");
                } else if (uri.toString().contains(".3gp") || uri.toString().contains(".mpg") || uri.toString().contains(".mpeg") || uri.toString().contains(".mpe")
                        || uri.toString().contains(".mp4") || uri.toString().contains(".avi")) {
                    // Video files
                    intent.setDataAndType(uri, "video/*");
                } else {
                    // if you want you can also define the intent type for
                    // any
                    // other file

                    // additionally use else clause below, to manage other
                    // unknown extensions
                    // in this case, Android will show all applications
                    // installed on the device
                    // so you can choose which application to use
                    intent.setDataAndType(uri, "*/*");
                }

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);

                dialog.dismiss();
            }

            @Override
            public void onCancelClicked(NotifyDialog dialog) {
                dialog.dismiss();
            }
        });
    }

    /**
     * open dialog for import contact
     * @param vCard vCard of contact
     * @param context
     */
    public static void selectedContactDialog(final String vCard, final Context context){
        NotifyDialog dialog = NotifyDialog.startConfirm(context, "Add to contact", "Do You want to add this contact to Your address book?");
        dialog.setButtonsText(context.getString(R.string.NO_CAPITAL), context.getString(R.string.YES_CAPITAL));
        dialog.setTwoButtonListener(new NotifyDialog.TwoButtonDialogListener() {
            @Override
            public void onOkClicked(NotifyDialog dialog) {
                String vCardTempPath = Tools.getTempFolderPath() + "/" + System.currentTimeMillis() + "_vCard.vcf";
                File vCardTempFile = new File(vCardTempPath);
                Tools.saveStringToFile(vCard, vCardTempPath);
                Uri uri = Uri.fromFile(vCardTempFile);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "text/x-vcard");
                context.startActivity(intent);
                dialog.dismiss();
            }

            @Override
            public void onCancelClicked(NotifyDialog dialog) {
                dialog.dismiss();
            }
        });
    }

}
