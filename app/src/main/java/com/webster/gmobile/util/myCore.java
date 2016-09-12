package com.webster.gmobile.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by weby on 3/15/2015.
 */
public class myCore {

    //region validateRegistrationStrings
    public static String validateRegistrationStrings(String nm_dpn, String nm_blkg, String username, String password,
                                                     String conf_password, String dob,
                                                     String email, String mobile_no){
        if(nm_dpn.trim().equals(""))
            return "Nama Depan tidak boleh kosong";
        else if(nm_dpn.trim().length() < 3)
            return "Nama Depan minimal 4 karakter";
        else if(nm_blkg.trim().equals(""))
            return "Nama Belakang tidak boleh kosong";
        else if(nm_blkg.trim().length() < 2)
            return "Nama Depan minimal 3 karakter";
        else if(username.trim().equals(""))
            return "Username tidak boleh kosong";
        else if(username.trim().length() < 5)
            return "Username minimal 6 karakter";
        else if(password.trim().equals(""))
            return "Password tidak boleh kosong";
        else if(password.trim().length() < 5)
            return "Password minimal 6 karakter";
        else if(conf_password.trim().equals(""))
            return "Konfirmasi password tidak boleh kosong";
        else if(conf_password.trim().length() < 5)
            return "Konfirmasi Password minimal 6 karakter";
        else if(!password.trim().equals(conf_password.trim()))
            return "Konfirmasi password anda tidak match dengan password anda";
        else if(dob.trim().equals(""))
            return "Tanggal Lahir tidak boleh kosong";
        else if(email.trim().equals(""))
            return "Email tidak boleh kosong";
        else if(mobile_no.trim().equals(""))
            return "Mobile No. tidak boleh kosong";
        else
            return "";
    }
    //endregion

    //region validateLoginStrings
    public static String validateLoginStrings(String username, String password){
        if(username.trim().equals(""))
            return "Username tidak boleh kosong";
        else if(username.trim().length() < 5)
            return "Username minimal 6 karakter";
        else if(password.trim().equals(""))
            return "Password tidak boleh kosong";
        else if(password.trim().length() < 5)
            return "Password minimal 6 karakter";
        else
            return "";
    }
    //endregion

    //region validateServiceScheduleStrings
    public static String validateServiceScheduleStrings(String service_dt, String theme, String preacher_nm, String worship_ldr){
        if(service_dt.trim().equals(""))
            return "Tanggal Pelayanan tidak boleh kosong";
        else if(theme.trim().equals(""))
            return "Tema tidak boleh kosong";
        else if(theme.trim().length() < 2)
            return "Tema minimal 3 karakter";
        else if(preacher_nm.trim().equals(""))
            return "Nama Pengkhotbah tidak boleh kosong";
        else if(preacher_nm.trim().length() < 3)
            return "Nama Pengkhotbah minimal 4 karakter";
        else if(worship_ldr.trim().equals(""))
            return "Worship Leader tidak boleh kosong";
        else if(worship_ldr.trim().length() < 3)
            return "Worship Leader minimal 4 karakter";
        /* else if(additional_svcr.trim().equals(""))
            return "Tambahan Pelayan tidak boleh kosong";
        else if(additional_svcr.trim().length() < 5)
            return "Tambahan Pelayan minimal 6 karakter"; */
        else
            return "";
    }
    //endregion

    //region Generate MD5 Hash
    public static String MD5(String text)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md;
        md = MessageDigest.getInstance("MD5");
        byte[] md5hash = new byte[32];
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        md5hash = md.digest();
        return convertToHex(md5hash);
    }
    //endregion

    //region ConnectToDB
    public static String connectToDB(String strMethod){
        //"http://gkrgedongmobile.com/registration.php";
        String LOGIN_URL = "http://gkrgedongmobile.com/" + strMethod + ".php";
//        String LOGIN_URL = "http://192.168.1.105:7777/GKRMobile/" + strMethod + ".php";
        return LOGIN_URL;
    }
    //endregion

    //region convertToHex
    private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while(two_halfs++ < 1);
        }
        return buf.toString();
    }
    //endregion

    //region checkInternetConnection
    public static boolean checkInternetConnection(Context c){
        c.getSystemService(Context.CONNECTIVITY_SERVICE);
        ConnectivityManager connectivityManager = (ConnectivityManager)c.getSystemService(Context.CONNECTIVITY_SERVICE);;
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        }
        else
            return false;
    }
    //endregion


}
