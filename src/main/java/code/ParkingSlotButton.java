package code;

import javax.swing.*;
import java.awt.*;
//Xử lý nút Slot(A1,A2,A3,...)
public class ParkingSlotButton extends JButton {//ParkingSlotButton kế thừa toàn bộ chức năng của JButton

    private final String maViTri;

    public ParkingSlotButton(String maViTri) {
        this.maViTri = maViTri;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);//vẽ nút
        Graphics2D g2 = (Graphics2D) g.create();//vẽ id

        g2.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        );

        g2.setFont(new Font("SansSerif", Font.BOLD, 13));
        g2.setColor(new Color(231, 234, 235));

        // Vẽ mã vị trí ở góc trái phía trên
        g2.drawString(maViTri, 12, 20);

        g2.dispose();
    }
}
