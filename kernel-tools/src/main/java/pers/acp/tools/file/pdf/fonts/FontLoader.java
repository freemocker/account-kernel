package pers.acp.tools.file.pdf.fonts;

import pers.acp.tools.file.common.FileCommon;
import com.itextpdf.text.pdf.BaseFont;
import org.apache.log4j.Logger;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FontLoader {

    private static Logger log = Logger.getLogger(FontLoader.class);

    public static Map<String, String> fontName = new HashMap<>();

    /**
     * 字体信息装载入内存
     *
     * @param font 字体文件
     */
    private static void LoadFonts(File font) throws Exception {
        Font dynamicFont = Font.createFont(Font.TRUETYPE_FONT, font);
        if (!fontName.containsKey(dynamicFont.getFamily())) {
            fontName.put(dynamicFont.getFamily(), dynamicFont.getPSName());
        }
    }

    /**
     * 获取字体文件夹路径
     *
     * @return 文件夹路径
     */
    private static String getFontFold() {
        String path = FileCommon.getAbsPath(FileCommon.getProperties("fonts.fold", "/files/resource/font"));
        File fold = new File(path);
        if (!fold.exists()) {
            fold.mkdirs();
        }
        return path;
    }

    /**
     * 初始化字体信息
     */
    public static void InitFonts() {
        try {
            log.info("start load pdf fonts...");
            String fontsFoldPath = getFontFold();
            log.info("fonts fold path: " + fontsFoldPath);
            File fontsFold = new File(fontsFoldPath);
            File[] fonts = fontsFold.listFiles();
            int filecount = 0;
            if (fonts != null) {
                for (File font : fonts) {
                    if (font.isFile()) {
                        if (!font.getName().toLowerCase().endsWith(".java")
                                && !font.getName().toLowerCase().endsWith(".class")) {
                            LoadFonts(font);
                            filecount++;
                        }
                    }
                }
            }
            log.info("load pdf " + filecount + " fonts success");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 生成PDF之前将字体文件引用进renderer
     *
     * @param renderer renderer对象
     * @return renderer renderer对象
     */
    public static ITextRenderer loadFonts(ITextRenderer renderer) {
        try {
            ITextFontResolver fontResolver = renderer.getFontResolver();
            File fontsFold = new File(getFontFold());
            File[] fonts = fontsFold.listFiles();
            if (fonts != null) {
                for (File font : fonts) {
                    if (font.isFile()) {
                        if (!font.getName().toLowerCase().endsWith(".java") && !font.getName().toLowerCase().endsWith(".class")) {
                            fontResolver.addFont(font.getAbsolutePath(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return renderer;
    }

    /**
     * 获取宋体字体
     *
     * @return 字体对象
     */
    public static BaseFont getSIMSUNFont() {
        try {
            File font = new File(getFontFold() + File.separator + "SIMSUN.TTC");
            return BaseFont.createFont(font.getAbsolutePath() + ",1", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}