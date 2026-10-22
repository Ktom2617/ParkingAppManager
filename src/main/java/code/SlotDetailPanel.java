package code;

import javax.swing.*;
import java.awt.*;
//QUẢN LÝ TOÀN BỘ PHIẾU THÔNG TIN GỬI XE
public class SlotDetailPanel extends JPanel {//Định nghĩa từ Jpanel

    private JLabel maViTri;
    private JLabel trangThai;

    public SlotDetailPanel(JLabel maViTri, JLabel trangThai) {
        super(new BorderLayout(0, 15));
        setOpaque(false);

        this.maViTri = maViTri;
        this.trangThai = trangThai;

        // Tạo phần tiêu đề
        JPanel hangTieuDe = new JPanel(new BorderLayout());
        hangTieuDe.setOpaque(false);

        hangTieuDe.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(
                        0, 0, 1, 0, new Color(50, 58, 63)
                ),
                BorderFactory.createEmptyBorder(0, 0, 14, 0)
        ));

        hangTieuDe.add(maViTri, BorderLayout.WEST);
        hangTieuDe.add(trangThai, BorderLayout.EAST);

        add(hangTieuDe, BorderLayout.NORTH);
    }

    public void setNoiDung(JPanel noiDung) {
        add(noiDung, BorderLayout.CENTER);
    }

    public void capNhat(ParkingSlot viTri) {
        maViTri.setText(viTri.getId());

        if (viTri.isEmpty()) {
            trangThai.setText("TRỐNG");
        } else {
            trangThai.setText("ĐANG CÓ XE");
        }
    }
}