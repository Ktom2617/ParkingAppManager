package code;

import java.awt.*;
import java.io.*;

public class AppFonts {

    private static final Font BODY =
            docFont("Barlow-Regular.ttf", Font.PLAIN);

    private static final Font DISPLAY =
            docFont("BarlowCondensed-Bold.ttf", Font.BOLD);
    private static final Font BienSo =
            docFont("BienSo.ttf", Font.PLAIN);

    private static Font docFont(String tenFile, int kieu) {
        String duongDan = "/fonts/" + tenFile;

        try (InputStream in =
                     AppFonts.class.getResourceAsStream(duongDan)) {

            if (in == null) {
                throw new IOException("Khong tim thay " + duongDan);
            }

            return Font.createFont(Font.TRUETYPE_FONT, in);

        } catch (IOException | FontFormatException e) {
            System.err.println("Loi font: " + e.getMessage());
            return new Font("SansSerif", kieu, 14);
        }
    }

    public static Font body(float kichThuoc) {
        return BODY.deriveFont(kichThuoc);
    }

    public static Font display(float kichThuoc) {
        return DISPLAY.deriveFont(kichThuoc);
    }
    public static Font bodyBold(float kichThuoc) {
        return BODY.deriveFont(Font.BOLD, kichThuoc);
    }
    public static Font BienSo(float kichThuoc) {
        return BienSo.deriveFont(kichThuoc);
    }
}
