package com.mine.west.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * 图片工具类
 * 功能一：判断图片是否正常
 * 功能二：清洗图片（添加透明水印）
 */
public class ImageUtil {
    /**
     * 判断该文件是不是图片、图片是否损坏
     *
     * @param imageFile
     * @return
     */
    public static boolean PictureIsLegal(File imageFile) {
        Image img;
        try {
            img = ImageIO.read(imageFile);
            if (img == null || img.getWidth(null) <= 0 || img.getHeight(null) <= 0)
                return false;
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 给图片添加水印
     *
     * @param logoText   水印文字
     * @param srcImgPath 源图片路径
     * @param targetPath 目标图片路径
     * @param degree     水印旋转角度
     * @param width      宽度（与左相比）
     * @param height     高度（与上相比）
     * @param clarity    透明度（0~1），越接近0越透明
     */
    public static void waterMarkByText(String logoText, String srcImgPath,
                                       String targetPath, Integer degree,
                                       Integer width, Integer height,
                                       Float clarity) {
        // 主图片的路径
        InputStream is = null;
        OutputStream os = null;
        try {
            Image srcImg = ImageIO.read(new File(srcImgPath));
            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null),
                    srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);

            Graphics2D g = buffImg.createGraphics();// 得到画笔对象
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);// 设置对线段的锯齿状边缘处理
            g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null),
                    srcImg.getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);
            if (null != degree) {
                // 设置水印旋转
                g.rotate(Math.toRadians(degree),
                        (double) buffImg.getWidth() / 2,
                        (double) buffImg.getHeight() / 2);
            }
            g.setColor(Color.red);// 设置颜色
            g.setFont(new Font("宋体", Font.BOLD, 30));// 设置 Font
            float alpha = clarity;
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            g.drawString(logoText, width, height);//文字内容、文字的坐标
            g.dispose();
            os = new FileOutputStream(targetPath);
            ImageIO.write(buffImg, "JPG", os);// 生成图片
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != is)
                    is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (null != os)
                    os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
