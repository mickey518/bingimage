package com.mickey.bingimage.service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsRequest;
import com.aliyuncs.alidns.model.v20150109.DescribeDomainRecordsResponse;
import com.aliyuncs.alidns.model.v20150109.UpdateDomainRecordRequest;
import com.aliyuncs.alidns.model.v20150109.UpdateDomainRecordResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author mickey wang
 */
public class DDNS {
    /**
     * 获取主域名的所有解析记录列表
     */
    private DescribeDomainRecordsResponse describeDomainRecords(DescribeDomainRecordsRequest request, IAcsClient client) {
        try {
            // 调用SDK发送请求
            return client.getAcsResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
            // 发生调用错误，抛出运行时异常
            throw new RuntimeException();
        }
    }

    /**
     * 获取当前主机公网IP
     */
    private String getCurrentHostIP() {
        // 这里使用jsonip.com第三方接口获取本地IP
        String jsonip = "https://jsonip.com/";
        // 接口返回结果
        String result = "";
        BufferedReader in = null;
        try {
            // 使用HttpURLConnection网络请求第三方接口
            URL url = new URL(jsonip);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            in = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }

        }
        // 正则表达式，提取xxx.xxx.xxx.xxx，将IP地址从接口返回结果中提取出来
        String rexp = "(\\d{1,3}\\.){3}\\d{1,3}";
        Pattern pat = Pattern.compile(rexp);
        Matcher mat = pat.matcher(result);
        String res = "";
        while (mat.find()) {
            res = mat.group();
            break;
        }
        return res;
    }

    /**
     * 修改解析记录
     */
    private UpdateDomainRecordResponse updateDomainRecord(UpdateDomainRecordRequest request, IAcsClient client) {
        try {
            // 调用SDK发送请求
            return client.getAcsResponse(request);
        } catch (ClientException e) {
            e.printStackTrace();
            // 发生调用错误，抛出运行时异常
            throw new RuntimeException();
        }
    }

    private static void log_print(String functionName, Object result) {
        Gson gson = new Gson();
        System.out.println("-------------------------------" + functionName + "-------------------------------");
        System.out.println(gson.toJson(result));
    }

    public static String sync() {
        StringBuilder stringBuilder = new StringBuilder();
        // 设置鉴权参数，初始化客户端
        DefaultProfile profile = DefaultProfile.getProfile(
                "cn-hangzhou",// 地域ID
                "LTAI5tBqX9RXt5XRg4eWSXrX",// 您的AccessKey ID
                "ygScEVY6y5SOVyZK5HcDdIKHRQP479");// 您的AccessKey Secret
        IAcsClient client = new DefaultAcsClient(profile);

        DDNS ddns = new DDNS();

        // 查询指定二级域名的最新解析记录
        DescribeDomainRecordsRequest describeDomainRecordsRequest = new DescribeDomainRecordsRequest();
        // 主域名
        describeDomainRecordsRequest.setDomainName("mickey.wang");
        // 主机记录
//        describeDomainRecordsRequest.setRRKeyWord("ddnstest");
        // 解析记录类型
        describeDomainRecordsRequest.setType("A");
        DescribeDomainRecordsResponse describeDomainRecordsResponse = ddns.describeDomainRecords(describeDomainRecordsRequest, client);
        log_print("describeDomainRecords", describeDomainRecordsResponse);
        stringBuilder.append("-------------------------------describeDomainRecords-------------------------------");
        stringBuilder.append(new Gson().toJson(describeDomainRecordsResponse));

        List<DescribeDomainRecordsResponse.Record> domainRecords = describeDomainRecordsResponse.getDomainRecords();
        // 最新的一条解析记录
        if (domainRecords.size() != 0) {
            // 当前主机公网IP
            String currentHostIP = ddns.getCurrentHostIP();
            stringBuilder.append("-------------------------------当前主机公网IP为：" + currentHostIP + "-------------------------------");

            for (DescribeDomainRecordsResponse.Record domainRecord : domainRecords) {
                if (!currentHostIP.equals(domainRecord.getValue())) {
                    // 修改解析记录
                    UpdateDomainRecordRequest updateDomainRecordRequest = new UpdateDomainRecordRequest();
                    // 主机记录
                    updateDomainRecordRequest.setRR(domainRecord.getRR());
                    // 记录ID
                    updateDomainRecordRequest.setRecordId(domainRecord.getRecordId());
                    // 将主机记录值改为当前主机IP
                    updateDomainRecordRequest.setValue(currentHostIP);
                    // 解析记录类型
                    updateDomainRecordRequest.setType(domainRecord.getType());
                    UpdateDomainRecordResponse updateDomainRecordResponse = ddns.updateDomainRecord(updateDomainRecordRequest, client);
                    log_print("updateDomainRecord", updateDomainRecordResponse);
                    stringBuilder.append("-------------------------------updateDomainRecord-------------------------------");
                    stringBuilder.append(new Gson().toJson(updateDomainRecordResponse));
                }
            }
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        sync();
    }
}
