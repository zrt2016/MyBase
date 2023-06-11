package com.zrt.nfcapp.nfc;

/**
 * @author：Zrt
 * @date: 2023/4/25
 */
public class NfcResult {
    public String error = "200";
    public String result = "";

    public String tag_id_hex = "";
    public String tag_id_dec = "";
    public String id_reveresd = "";
    public String technologies = "";

    @Override
    public String toString() {
        return "NfcResult{" +
                "error='" + error + '\'' +
                ", result='" + result + '\'' +
                ", tag_id_hex='" + tag_id_hex + '\'' +
                ", tag_id_dec='" + tag_id_dec + '\'' +
                ", id_reveresd='" + id_reveresd + '\'' +
                ", technologies='" + technologies + '\'' +
                '}';
    }
}
