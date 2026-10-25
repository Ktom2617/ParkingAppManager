package code;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
//XỬ LÝ GIAO DIỆN SLOT BUTTON
public class ParkingSlotButton extends JButton {

    private final String maViTri;
    private Vehicle xe;
    private LocalDateTime gioVao;

    private static final DateTimeFormatter DINH_DANG_GIO =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    public ParkingSlotButton(String maViTri) {
        this.maViTri = maViTri;
        setFocusPainted(false);
    }

    // Nhận dữ liệu để cập nhật hình vẽ
    public void capNhat(ParkingSlot viTri) {
        this.xe = viTri.getVehicle();
        this.gioVao = viTri.getThoiGianVao();

        setText(null);
        setIcon(null);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        try {
            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int w = getWidth();
            int h = getHeight();

            // 1. Mã vị trí ở góc trái trên
            g2.setFont(AppFonts.bodyBold(12f));
            g2.setColor(new Color(233, 232, 223));
            g2.drawString(maViTri, 10, 18);

            // 2. Ô đang trống
            if (xe == null) {
                g2.setFont(AppFonts.bodyBold(14f));

                String chu = "TRỐNG";
                FontMetrics fm = g2.getFontMetrics();

                g2.drawString(
                        chu,
                        (w - fm.stringWidth(chu)) / 2,
                        (h + fm.getAscent()) / 2
                                - fm.getDescent()
                );
                return;
            }

            // 3. Chuẩn hóa biển số cho font BienSo
            String bienSo = xe.getBienSo()
                    .toUpperCase(Locale.ROOT);


            // 4. Tạo icon theo loại xe
            Icon icon = new VehicleIcon(xe instanceof Car);

            int rongIcon = icon.getIconWidth();
            int caoIcon = icon.getIconHeight();

            // 5. Chọn cỡ biển số vừa chiều rộng ô
            float coChu = 20f;
            Font fontBienSo = AppFonts.BienSo(coChu);
            FontMetrics fmBien = g2.getFontMetrics(fontBienSo);

            while (coChu > 13f
                    && rongIcon + fmBien.stringWidth(bienSo)
                    + 30 > w) {

                coChu -= 1f;
                fontBienSo = AppFonts.BienSo(coChu);
                fmBien = g2.getFontMetrics(fontBienSo);
            }

            int rongBien = fmBien.stringWidth(bienSo) + 16;
            int caoBien = 28;
            int khoangCach = 8;

            int tongRong = rongIcon + khoangCach + rongBien;
            int xBatDau = (w - tongRong) / 2;
            int yIcon = Math.max(19, (h - caoIcon) / 2 - 9);

            // 6. Vẽ icon
            icon.paintIcon(this, g2, xBatDau, yIcon);

            // 7. Vẽ nền biển số trắng
            int xBien = xBatDau + rongIcon + khoangCach;
            int yBien = yIcon + (caoIcon - caoBien) / 2;

            g2.setColor(new Color(248, 248, 243));
            g2.fillRoundRect(
                    xBien, yBien, rongBien, caoBien, 4, 4
            );

            g2.setColor(new Color(17, 20, 22));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(
                    xBien, yBien, rongBien, caoBien, 4, 4
            );

            // 8. Vẽ chữ trên biển số
            g2.setFont(fontBienSo);

            int yChu = yBien
                    + (caoBien - fmBien.getHeight()) / 2
                    + fmBien.getAscent()+4;

            g2.drawString(
                    bienSo,
                    xBien + (rongBien - fmBien.stringWidth(bienSo)) / 2,
                    yChu
            );

            bienSo = bienSo.replaceAll(
                    "^(\\d{2}[A-Z]{1,2}\\d?)-?(\\d{3})\\.?([0-9]{2})$",
                    "$1-$2.$3"
            );//format biển số

            // 9. Giờ vào nằm dưới cùng
            if (gioVao != null) {
                String thoiGian =
                        "VÀO " + gioVao.format(DINH_DANG_GIO);

                g2.setFont(AppFonts.bodyBold(14f));
                g2.setColor(new Color(233, 232, 223));

                FontMetrics fmGio = g2.getFontMetrics();

                g2.drawString(
                        thoiGian,
                        (w - fmGio.stringWidth(thoiGian)) / 2,
                        h - 6
                );
            }

        } finally {
            g2.dispose();
        }
    }
}