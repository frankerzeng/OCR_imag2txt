package test1;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sun.image.codec.jpeg.*;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageEncodeParam;
import com.sun.media.jai.codec.ImageEncoder;
import main.ImageHelper;
import main.MysqlConnect;

import javax.imageio.ImageIO;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;

/**
 * Created by Administrator on 2016/11/6.
 */
public class main {
    static String sql = null;
    static MysqlConnect mysqlConnect = null;
    static ResultSet resultSet = null;
    static String DownloadImageName = "imagephone";

    public static void main(String[] args) throws SQLException, IOException {
        // 下载演示图片
        sql = "select phone from shop_detail WHERE phone LIKE 'http://image.58%' LIMIT 1,10";
        mysqlConnect = new MysqlConnect(sql);

        resultSet = mysqlConnect.preparedStatement.executeQuery();
        Integer name = 1;
        while (resultSet.next()) {
            String urlPhone = resultSet.getString(1);
            System.out.println(urlPhone);
            downloadNet(urlPhone, name);
            name++;
        }

        for (Integer k = 1; k < 11; k++) {
            File tempImage = ImageHelper.createImage(new File("C:\\ocr\\TesseractOCR\\train\\" + k + ".gif"), "gif");
            System.out.println(tempImage.getAbsolutePath());
        }

        // 变黑白
        for (Integer i = 1; i < 11; i++) {
            File file = new File("C:\\ocr\\TesseractOCR\\train\\" + i + ".gif");
            changeImge(file);
        }

        //转格式jpg
        for (Integer k = 1; k < 11; k++) {
//            File fi = new File("C:\\ocr\\TesseractOCR\\train\\" + k + ".gif");
//            BufferedImage im = ImageIO.read(fi);
//            File fo = new File("C:\\ocr\\TesseractOCR\\train\\" + k + ".jpg");
//            ImageIO.write(im, "jpg", fo);
            String input2 = "C:\\ocr\\TesseractOCR\\train\\" + k + "0.tif";
            String output2 = "C:\\ocr\\TesseractOCR\\train\\" + k + ".jpg";
            RenderedOp src2 = JAI.create("fileload", input2);
            OutputStream os2 = new FileOutputStream(output2);
            JPEGEncodeParam param2 = new JPEGEncodeParam() {
                @Override
                public Object clone() {
                    return null;
                }

                @Override
                public int getWidth() {
                    return 0;
                }

                @Override
                public int getHeight() {
                    return 0;
                }

                @Override
                public int getHorizontalSubsampling(int component) {
                    return 0;
                }

                @Override
                public int getVerticalSubsampling(int component) {
                    return 0;
                }

                @Override
                public JPEGQTable getQTable(int tableNum) {
                    return null;
                }

                @Override
                public JPEGQTable getQTableForComponent(int component) {
                    return null;
                }

                @Override
                public JPEGHuffmanTable getDCHuffmanTable(int tableNum) {
                    return null;
                }

                @Override
                public JPEGHuffmanTable getDCHuffmanTableForComponent(int component) {
                    return null;
                }

                @Override
                public JPEGHuffmanTable getACHuffmanTable(int tableNum) {
                    return null;
                }

                @Override
                public JPEGHuffmanTable getACHuffmanTableForComponent(int component) {
                    return null;
                }

                @Override
                public int getDCHuffmanComponentMapping(int component) {
                    return 0;
                }

                @Override
                public int getACHuffmanComponentMapping(int component) {
                    return 0;
                }

                @Override
                public int getQTableComponentMapping(int component) {
                    return 0;
                }

                @Override
                public boolean isImageInfoValid() {
                    return false;
                }

                @Override
                public boolean isTableInfoValid() {
                    return false;
                }

                @Override
                public boolean getMarker(int marker) {
                    return false;
                }

                @Override
                public byte[][] getMarkerData(int marker) {
                    return new byte[0][];
                }

                @Override
                public int getEncodedColorID() {
                    return 0;
                }

                @Override
                public int getNumComponents() {
                    return 0;
                }

                @Override
                public int getRestartInterval() {
                    return 0;
                }

                @Override
                public int getDensityUnit() {
                    return 0;
                }

                @Override
                public int getXDensity() {
                    return 0;
                }

                @Override
                public int getYDensity() {
                    return 0;
                }

                @Override
                public void setHorizontalSubsampling(int component, int subsample) {

                }

                @Override
                public void setVerticalSubsampling(int component, int subsample) {

                }

                @Override
                public void setQTable(int tableNum, JPEGQTable qTable) {

                }

                @Override
                public void setDCHuffmanTable(int tableNum, JPEGHuffmanTable huffTable) {

                }

                @Override
                public void setACHuffmanTable(int tableNum, JPEGHuffmanTable huffTable) {

                }

                @Override
                public void setDCHuffmanComponentMapping(int component, int table) {

                }

                @Override
                public void setACHuffmanComponentMapping(int component, int table) {

                }

                @Override
                public void setQTableComponentMapping(int component, int table) {

                }

                @Override
                public void setImageInfoValid(boolean flag) {

                }

                @Override
                public void setTableInfoValid(boolean flag) {

                }

                @Override
                public void setMarkerData(int marker, byte[][] data) {

                }

                @Override
                public void addMarkerData(int marker, byte[] data) {

                }

                @Override
                public void setRestartInterval(int restartInterval) {

                }

                @Override
                public void setDensityUnit(int unit) {

                }

                @Override
                public void setXDensity(int density) {

                }

                @Override
                public void setYDensity(int density) {

                }

                @Override
                public void setQuality(float quality, boolean forceBaseline) {

                }
            };
            //指定格式类型，jpg 属于 JPEG 类型
            ImageEncoder enc2 = ImageCodec.createImageEncoder("JPEG", os2, (ImageEncodeParam) param2);
            enc2.encode(src2);
            os2.close();

        }
    }

    public static void changeImge(File img) {
        try {
            Image image = ImageIO.read(img);
            int srcH = image.getHeight(null);
            int srcW = image.getWidth(null);
            BufferedImage bufferedImage = new BufferedImage(srcW, srcH, BufferedImage.TYPE_3BYTE_BGR);
            bufferedImage.getGraphics().drawImage(image, 0, 0, srcW, srcH, null);
            bufferedImage = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null).filter(bufferedImage, null);
            FileOutputStream fos = new FileOutputStream(img);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fos);
            encoder.encode(bufferedImage);
            fos.close();
            // System.out.println("转换成功...");
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("图片转换出错！", e);
        }
    }

    public static String downloadNet(String imgUrl, int imgName) throws MalformedURLException {
        int bytesum = 0;
        int byteread = 0;

        URL url = new URL(imgUrl);

        try {
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            FileOutputStream fs = new FileOutputStream("C:\\ocr\\TesseractOCR\\train\\" + imgName + ".gif");

            byte[] buffer = new byte[1204];
            int length;
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                fs.write(buffer, 0, byteread);
            }
            return "";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
