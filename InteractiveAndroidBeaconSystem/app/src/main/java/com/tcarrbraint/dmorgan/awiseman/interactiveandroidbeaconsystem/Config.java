package com.tcarrbraint.dmorgan.awiseman.interactiveandroidbeaconsystem;

/**
 * Created by Dan on 14/01/2016.
 */
public class Config {

    //Address of our scripts of the CRUD
    public static final String URL_ADD="http://interactivebluetooth.ddns.net/Android/498_Project/addEmp.php";
    public static final String URL_GET_ALL = "http://interactivebluetooth.ddns.net/Android/498_Project/getAllEmp.php";
    public static final String URL_GET_EMP = "http://interactivebluetooth.ddns.net/Android/498_Project/getEmp.php?id=";
    public static final String URL_UPDATE_EMP = "http://interactivebluetooth.ddns.net/Android/498_Project/updateEmp.php";
    public static final String URL_DELETE_EMP = "http://interactivebluetooth.ddns.net/Android/498_Project/deleteEmp.php?id=";

    //Keys that will be used to send the request to php scripts
    public static final String KEY_EMP_ID = "id";
    public static final String KEY_EMP_NAME = "name";
    public static final String KEY_EMP_DESG = "loc";
    public static final String KEY_EMP_SAL = "score";

    //JSON Tags
    public static final String TAG_JSON_ARRAY="result";
    public static final String TAG_ID = "id";
    public static final String TAG_NAME = "name";
    public static final String TAG_LOC = "loc";
    public static final String TAG_SCORE = "score";

    //employee id to pass with intent
    public static final String EMP_ID = "emp_id";
}
