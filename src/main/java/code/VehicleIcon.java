package code;

import javax.swing.*;
import java.awt.*;
//XỬ LÝ ICON XE(GIAO DIỆN)
public class VehicleIcon implements Icon {//interface

    private final boolean laOTo;

    public VehicleIcon(boolean laOTo) {
        this.laOTo = laOTo;
    }

    @Override
    public int getIconWidth() {
        return 64;
    }

    @Override
    public int getIconHeight() {
        return 30;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();//cong cu ve hinh 2d
        g2.translate(x, y);

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        if (laOTo) {
            //Thân ô tô nhìn từ trên xuống
            g2.setColor(new Color(154, 163, 169));
            g2.fillRoundRect(2, 3, 60, 24, 12, 12);

            //Kính xe
            g2.setColor(new Color(26, 32, 36));
            g2.fillRoundRect(15, 7, 27, 16, 6, 6);
            g2.fillRoundRect(45, 9, 9, 12, 5, 5);

        } else {
            //Hai bánh xe máy
            g2.setColor(new Color(21, 25, 27));
            g2.fillRoundRect(9, 10, 12, 10, 6, 6);
            g2.fillRoundRect(45, 10, 11, 10, 6, 6);

            //Thân xe máy
            g2.setColor(new Color(181, 69, 58));
            g2.fillRoundRect(17, 11, 32, 8, 8, 8);

            //Tay lái
            g2.setColor(new Color(21, 25, 27));
            g2.fillRoundRect(43, 4, 4, 22, 4, 4);
        }

        g2.dispose();
    }
}
