package main;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/11/4.
 */
public class Main {
    static String sql = null;
    static String sqlUpdate = null;
    static MysqlConnect mysqlConnect = null;
    static MysqlConnect mysqlConnectUpdate = null;
    static ResultSet resultSet = null;
    static ResultSet resultSetUpdate = null;
    static String Path_phone_image = null;
    static String DownloadImageName = "imagephone";

    public static void main(String[] args) throws IOException, SQLException {

        File directory = new File("");
        Path_phone_image = directory.getAbsolutePath() + "\\58ocr\\src\\phone_image\\";

        sql = "select id,phone from shop_detail";
        mysqlConnect = new MysqlConnect(sql);
        Integer times = 60;
        String[] phoneArray = {"13", "14", "15", "17", "18"};

        try {
            resultSet = mysqlConnect.preparedStatement.executeQuery();
            while (resultSet.next()) {
                String id = resultSet.getString(1);
                String urlPhone = resultSet.getString(2);

                String phone = "";
                if (urlPhone != null && urlPhone.indexOf("http://image.58.com/") == -1) {
                    phone = urlPhone;
                } else if (urlPhone != null && urlPhone.indexOf("http://image.58.com/") == 0) {
                    System.out.println("urlPhone   " + urlPhone);
                    for (Integer i = 0; i < times; i++) {
                        phone = Img2Phone(urlPhone);
                        phone = phone.replace(" ", "");

                        if (phone.length() == 11 && Arrays.asList(phoneArray).contains(phone.substring(0, 2)) && isNumeric(phone)) { // 手机
                            break;
                        } else if (phone.length() == 13 && "-".equals(phone.substring(4, 5)) && "0".equals(phone.substring(0, 1))) { // 座机
                            break;
                        } else if (i == (times - 1)) {
                            phone = "";
                        } else if (phone.length() == 12 && "0".equals(phone.substring(0, 1))) { //不规范的座机号，不含-
                            break;
                        }
                        System.out.println("校验结果 " + phone);
                    }
                }

                sqlUpdate = "UPDATE shop_detail set phone3 = '" + phone + "' WHERE id = '" + id + "'";
                System.out.println(sqlUpdate);
                mysqlConnectUpdate = new MysqlConnect(sqlUpdate);
                mysqlConnectUpdate.preparedStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static String Img2Phone(String url) throws MalformedURLException {
        String imageName = downloadNet(url);

        try {
            String phone = new Ocr().recognizeText(new File(Path_phone_image + DownloadImageName + ".gif"), "gif");
            System.out.println("识别结果 " + phone);
            return phone;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String downloadNet(String imgUrl) throws MalformedURLException {
        int bytesum = 0;
        int byteread = 0;

        URL url = new URL(imgUrl);

        try {
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            FileOutputStream fs = new FileOutputStream(Path_phone_image + "/" + DownloadImageName + ".gif");

            byte[] buffer = new byte[1204];
            int length;
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                fs.write(buffer, 0, byteread);
            }
            return Path_phone_image + "/" + DownloadImageName + ".gif";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
}
