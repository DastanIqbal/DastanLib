package com.dastanapps.dastanlib.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import com.dastanapps.dastanlib.DastanApp;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;

/**
 * Created by dastaniqbal on 17/02/2017.
 * dastanIqbal@marvelmedia.com
 * 17/02/2017 3:28
 */

public class FileUtils {

    public static String getBasename(String path) {
        if (path == null) {
            return null;
        }
        int startIndex = path.lastIndexOf('/') + 1;
        int endIndex = path.lastIndexOf('.');
        if (startIndex < 0) {
            startIndex = 0;
        }
        if (endIndex < 0) {
            endIndex = path.length() - 1;
        }
        if (startIndex > endIndex) {
            startIndex = endIndex;
        }
        return path.substring(startIndex, endIndex);
    }

    public static boolean saveFile(String path, String filename, String content) {
        try {
            FileOutputStream fos = DastanApp.getInstance().openFileOutput(filename + ".txt", Context.MODE_PRIVATE);
            Writer out = new OutputStreamWriter(fos);
            out.write(content);
            out.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String saveBitmap(Bitmap bitmap, String path) {
        File file = new File(path);
        file.getParentFile().mkdirs();
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            return path;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getRealPathFromUri(Context context, Uri tempUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(tempUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static void deleteFolder(String folderPath) {
        deleteFolder(new File(folderPath));
    }

    public static void deleteFolder(File operationDir) {
        if (operationDir.isDirectory() && operationDir.listFiles() != null)
            for (File child : operationDir.listFiles())
                deleteFolder(child);
        operationDir.delete();
    }

    public static void deleteFile(File file) {
        if (file == null) return;
        if (file.isFile()) file.delete();
    }

    public static String getHumanReadableFileSize(String path) {
        File file = new File(path);
        if (file.length() > 0) {
            return humanReadableByteCount(file.length(), true);
        }
        return "";
    }

    public static String getFileSize(long size) {
        if (size <= 0)
            return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public static String[] getFileNames(String folder, final String filter) {
        File folderDir = new File(folder);
        return folderDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name.endsWith("." + filter)) return true;
                return false;
            }
        });
    }

    public static File[] getFiles(String folder, final String filter) {
        File folderDir = new File(folder);
        return folderDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name.endsWith("." + filter)) return true;
                return false;
            }
        });
    }

    public static String readFileContent(String filePath) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            FileInputStream fileOutputStream = new FileInputStream(filePath);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileOutputStream));
            String receiveString = "";
            while ((receiveString = bufferedReader.readLine()) != null) {
                stringBuilder.append(receiveString);
            }
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static void writeFileContent(String filePath, String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(filePath));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public static String readTextFileFromStream(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
        return outputStream.toString();
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (Build.VERSION.SDK_INT > 19) {
            try (InputStream in = new FileInputStream(sourceFile)) {
                try (OutputStream out = new FileOutputStream(destFile)) {
                    // Transfer bytes from in to out
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                }
            }
        } else {
            if (!destFile.getParentFile().exists())
                destFile.getParentFile().mkdirs();

            if (!destFile.exists()) {
                destFile.createNewFile();
            }

            FileChannel source = null;
            FileChannel destination = null;

            try {
                source = new FileInputStream(sourceFile).getChannel();
                destination = new FileOutputStream(destFile).getChannel();
                destination.transferFrom(source, 0, source.size());
            } finally {
                if (source != null) {
                    source.close();
                }
                if (destination != null) {
                    destination.close();
                }
            }
        }
    }
}
