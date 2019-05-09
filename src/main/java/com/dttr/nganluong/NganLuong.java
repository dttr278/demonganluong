/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dttr.nganluong;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author DO TAN TRUNG
 */
public class NganLuong {

    public static String nganluong_url = "https://sandbox.nganluong.vn:8088/nl35/checkout.php";
    public static String merchant_site_code = "47538";	//thay mã merchant site mà b?n dã dang ký vào dây
    public static String secure_pass = "b915a1c6a150c70e1877030d50fc2c25";
    public static String return_url = "http://localhost:8080/nganluong/nl_return.jsp";
    public static String receiver = "dttr278@gmail.com";

    public static String GetMD5Hash(String message) {
        String digest = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(message.getBytes("UTF-8"));
            // converting byte array to Hexadecimal String
            StringBuilder sb = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                sb.append(String.format("%02x", b & 0xff));
            }
            digest = sb.toString();
        } catch (UnsupportedEncodingException ex) {
            digest = "";
            // Logger.getLogger(StringReplace.class.getName()).log(Level.SEVERE,
            // null, ex);
        } catch (NoSuchAlgorithmException ex) {
            // Logger.getLogger(StringReplace.class.getName()).log(Level.SEVERE,
            // null, ex);
            digest = "";
        }
        return digest;
    }

    public static String buildCheckoutUrlNew(String transaction_info, String order_code, String price, String currency, float quantity, float tax, float discount, float fee_cal, float fee_shipping, String order_description, String buyer_info, String affiliate_code) throws UnsupportedEncodingException {
        //order_description = "Bình nước & Lock Lock 1.2 lít";
        //buyer_info = "Họ tên người mua *|* Địa chỉ Email *|* Điện thoại *|* Địa chỉ nhận hàng";// "Họ tên người mua *|* Địa chỉ Email *|* Điện thoại *|* Địa chỉ nhận hàng";
        String str_return_url = return_url.toLowerCase();
        String security_code = "";
        security_code += "" + merchant_site_code;
        security_code += " " + str_return_url;
        security_code += " " + receiver;//tài khoản ngân lượng
        security_code += " " + transaction_info;
        security_code += " " + order_code;
        security_code += " " + price;
        security_code += " " + currency;//hỗ trợ 2 loại tiền tệ currency usd,vnd
        security_code += " " + quantity;//số lượng mặc định 1
        security_code += " " + tax;
        security_code += " " + discount;
        security_code += " " + fee_cal;
        security_code += " " + fee_shipping;
        security_code += " " + order_description;
        String payinfo = "";// Convert.ToString("Họ tên người mua *|* Địa chỉ Email *|* Điện thoại *|* Địa chỉ nhận hàng");
        security_code += " " + buyer_info;
        security_code += " " + affiliate_code;
        security_code += " " + secure_pass;
        //return security_code;
        String md5 = GetMD5Hash(security_code);

        //security_code += " " + md5;
        Map<String, Object> ht = new HashMap<String, Object>();

        ht.put("merchant_site_code", merchant_site_code);
        ht.put("return_url", URLEncoder.encode(str_return_url, "UTF-8").toLowerCase());
        ht.put("receiver", URLEncoder.encode(receiver, "UTF-8"));
        ht.put("transaction_info", transaction_info);
        ht.put("order_code", order_code);
        ht.put("price", price);
        ht.put("currency", currency);
        ht.put("quantity", quantity);
        ht.put("tax", tax);
        ht.put("discount", discount);
        ht.put("fee_cal", fee_cal);
        ht.put("fee_shipping", fee_shipping);
        ht.put("order_description", URLEncoder.encode(order_description, "UTF-8"));
        ht.put("buyer_info", URLEncoder.encode(buyer_info, "UTF-8"));// "Họ tên người mua *|* Địa chỉ Email *|* Điện thoại *|* Địa chỉ nhận hàng");// "Họ tên người mua *|* Địa chỉ Email *|* Điện thoại *|* Địa chỉ nhận hàng");
        ht.put("affiliate_code", affiliate_code);
        ht.put("secure_code", md5);
        // T?o url redirect

        String redirect_url = nganluong_url;

        if (redirect_url.indexOf("?") == -1) {
            redirect_url += "?";
        } else if (redirect_url.substring(redirect_url.length() - 1, 1) != "?" && redirect_url.indexOf("&") == -1) {
            redirect_url += "&";
        }

        String url = "";

        // Duy?t các ph?n t? trong m?ng bam ht1 d? t?o redirect url
        List fieldNames = new ArrayList(ht.keySet());
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            if (url == "") {
                url += fieldName + "=" + ht.get(fieldName);
            } else {
                url += "&" + fieldName + "=" + ht.get(fieldName);
            }
        }
        String rdu = redirect_url + url;
        return rdu;
    }
}
