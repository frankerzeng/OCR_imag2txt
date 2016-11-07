package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/4.
 */
public class Ocr {
    private final String LANG_OPTION = "-l";
    private final String EOL = System.getProperty("line.separator");
    private String tessPath = new File("C:\\ocr\\TesseractOCR").getAbsolutePath();

    public String recognizeText(File imageFile, String imageFormat) throws Exception {
        File tempImage = ImageHelper.createImage(imageFile, imageFormat);
        File outputFile = new File(imageFile.getParentFile(), "output");
        StringBuffer strB = new StringBuffer();
        List<String> cmd = new ArrayList<String>();
        cmd.add(tessPath + "\\tesseract.exe");
        cmd.add("");
        cmd.add(outputFile.getName());
//        cmd.add(LANG_OPTION);
//        cmd.add("chi_sim");
        cmd.add("phone");


        ProcessBuilder pb = new ProcessBuilder();
        pb.directory(imageFile.getParentFile());

        cmd.set(1, tempImage.getAbsolutePath());

        pb.command(cmd);
        pb.redirectErrorStream(true);

        Process process = pb.start();
        //tesseract.exe 1.jpg 1 -l chi_sim digits
        int w = process.waitFor();

        //删除临时正在工作文件
        tempImage.delete();

        if (w == 0) {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(outputFile.getAbsolutePath() + ".txt"), "UTF-8"));
            String str;
            while ((str = in.readLine()) != null) {
//                strB.append(str).append(EOL);
                strB.append(str);
            }
            in.close();
        } else {
            String msg;
            switch (w) {
                case 1:
                    msg = "Errors accessing files.There may be spaces in your image's filename.";
                    break;
                case 29:
                    msg = "Cannot recongnize the image or its selected region.";
                    break;
                case 31:
                    msg = "Unsupported image format.";
                    break;
                default:
                    msg = "Errors occurred.";
            }
            tempImage.delete();
            throw new RuntimeException(msg);
        }

//        new File(outputFile.getAbsolutePath() + ".txt").delete();/
        return strB.toString();
    }
}
