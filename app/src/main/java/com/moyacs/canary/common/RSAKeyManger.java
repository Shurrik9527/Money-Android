package com.moyacs.canary.common;

import android.util.Log;

import org.apache.mina.util.Base64;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RSAKeyManger {
    public static final String priKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCYZrK8YUnaUc0UMzgvHCXI00uhdl6jQDiVcERAW/5R99d1vOLrTdahgd3fIDIIJZiLJsg+CwKAwDnLzkg/1p/wCrCSt6tt90MNIGXQWVHbJowevojPlFhQ9F4OfhZhqbW2LAFvmIcjPhKRl9z/Q/aj8pIAwv8DafbA9NKzbBly9wqZwEwOQ2SSpGPGhM6cEKxb+tAVYZzODPng1oud0RZsgeEvbUN2r3DbjhyvFVzBbsx1K3cbbXNUc2mGJ1XPf8IRnUebBxAFT/ZqUUcZGsonbE+Tj2SvZfFB532QZqPctZxqXU9NadKktV4e2oig2pys1X92IE8yee0oWrcKkryrAgMBAAECggEAbtKXfOdeVhUQBXVtkMxuKYiFQeea0Cn+O6VE7Gw5FxAcLPDrLmMPBakzV3LJDyWPtPjBumQ1ML0YCj16SnnfGiYb2sUXM0MQDjaV8xlQrgVs9VpmkFR2mH/q+az0x5YqnkuHy8fl1VnpFNiTC0k3zGazhzxFRw1CM3SU14ZvRyp/Dj6BUyI6PVXJOaQaj5QphZ/TNkYiICEdVt4qy5/GEYpesfcGk+sKBlhSyA7OxxoX9DLRecyb2BSGM17iwRvieAOFQbqA50iqH+2TyB9kNZB84S6ai6Xcy7Z8I17L2n67mBdms2MPZ4HUNdr1jp9y5ZbLX9lV1ICoiBYCEmzsGQKBgQDcvAB8Af0bYL6+g7FoLED4X2tZ6S/l33xRwjEc7Uo+O2bK8kR0LNa82K1WYr+rGQsmfoWMTbWp2lN3mr4spsm+Hgm7bllxY0ZciptQb2AqpVg98LuptSY3lxbb4oBMQ2kUggdIhZPa7bWfXzdmSjhZZTF6oQj1hH9iaNeQ+1EF1QKBgQCwv+Em4juGPHEMpJFb8S9v8xPTtxggftkfwjvbwg/1ksyE0x4o+ZhW7DO+2Hauvc02h+1uwayP4hZI/uYfVYz470NPCaPH2I6xprqtE9MriG9MviqhUMN9u10dEYYmTQ4ULMFyJBMrMyCfHcEUSco/QZgDZmnz9Mv60HOpQZF4fwKBgQCiiKiOp2Q5QZ6SaCQ8wcFBYfmJJgSH7mz7QtOpKizt8A17yLKRHOct/Rp9ro7VrMYiHS23Jw1qOYIWYtI/zLxminW+Fr4zmNcZk1JUAv67yC5WxalZiDLQ4icJ2BmCjmyUlj5Ir1s07K484P7JCkO9x56MXoFbT6oAy5RXhEaf1QKBgGhUGnZ1O2V079XindqDdtjBxoZFoPwtY8QXRnXlCr6NtCMC6t6M1ZsZOUIBS6zBZAw4F7I0p8MWVMkpjHQab2fQ8w4IRqCD91Ztb+sRtKgRlIDcDxXBMmNY4RFzcpfRC+pksJrLKHVrbGn/iiSLmdHUygHXxdq2OGEIAf16Mn/JAoGAMrjBXCBzysxUDtzqMpqYzxTVE6ozyaGDHXuwbXh1JMuNcMZZTjt6Tn9cnr3NcU6JXuzgL36effvO76xqidSl6J8iHri76ZlyUUAZJ91k9UV9nzqMgDFNQ1bjiby2AHbyTeuZigykuAnpJ+lim5kQxJxPDiCxAJxg96O9eWsrIlY=";

    public static final String pubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmGayvGFJ2lHNFDM4LxwlyNNLoXZeo0A4lXBEQFv+UffXdbzi603WoYHd3yAyCCWYiybIPgsCgMA5y85IP9af8AqwkrerbfdDDSBl0FlR2yaMHr6Iz5RYUPReDn4WYam1tiwBb5iHIz4SkZfc/0P2o/KSAML/A2n2wPTSs2wZcvcKmcBMDkNkkqRjxoTOnBCsW/rQFWGczgz54NaLndEWbIHhL21Ddq9w244crxVcwW7MdSt3G21zVHNphidVz3/CEZ1HmwcQBU/2alFHGRrKJ2xPk49kr2XxQed9kGaj3LWcal1PTWnSpLVeHtqIoNqcrNV/diBPMnntKFq3CpK8qwIDAQAB";

    /**
     * 签名
     *
     * @param data 待签名数据
     * @return 签名
     */
    public static String sign(String data) throws Exception {
        Log.e("TAG", "========要签名的串===========" + data);
        PrivateKey privateKey = getPrivateKey(priKey);
        byte[] keyBytes = privateKey.getEncoded();
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey key = keyFactory.generatePrivate(keySpec);
//        Signature signature = Signature.getInstance("MD5withRSA");
        Signature signature = Signature.getInstance("SHA256WithRSA");
        signature.initSign(key);
        signature.update(data.getBytes());
        String sing = new String(Base64.encodeBase64(signature.sign()));
        Log.e("TAG", "========签好的串===========" + sing);
        return sing;
    }

    private static PrivateKey getPrivateKey(String privateKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodedKey = Base64.decodeBase64(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        return keyFactory.generatePrivate(keySpec);
    }


    //    获取公钥
    public static PublicKey getPublicKey(String publicKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodedKey = Base64.decodeBase64(publicKey.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 将map 按照字典方式排序
     *
     * @param params map
     * @return 返回排序map
     */
    public static String getFormatParams(Map<String, Object> params) {
        List<Map.Entry<String, Object>> infoIds = new ArrayList<Map.Entry<String, Object>>(params.entrySet());
        Collections.sort(infoIds, (arg0, arg1) -> (arg0.getKey()).compareTo(arg1.getKey()));
        String ret = "";

        for (Map.Entry<String, Object> entry : infoIds) {
            ret += entry.getKey();
            ret += ":";
            ret += entry.getValue();
            ret += ",";
        }
        ret = ret.substring(0, ret.length() - 1);
        return ret;
    }


    /**
     * 验签
     *
     * @param srcData   原始字符串
     * @param publicKey 公钥
     * @param sign      签名
     * @return 是否验签通过
     */
    public static boolean verify(String srcData, PublicKey publicKey, String sign) throws Exception {
        byte[] keyBytes = publicKey.getEncoded();
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey key = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance("SHA256WithRSA");
//        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initVerify(key);
        signature.update(srcData.getBytes());
        return signature.verify(Base64.decodeBase64(sign.getBytes()));
    }
}
