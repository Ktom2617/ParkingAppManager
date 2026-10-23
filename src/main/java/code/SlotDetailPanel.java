package code;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.swing.*;
import java.awt.*;
//QUẢN LÝ TOÀN BỘ PHIẾU THÔNG TIN GỬI XE
public class SlotDetailPanel extends JPanel {//Định nghĩa từ Jpanel

    private JLabel maViTri;
    private JLabel trangThai;
    private JLabel bienSoHienThi;
    private JLabel thongTinViTri;
    private JLabel phiHienThi;
    private JLabel loaiXeHienThi;
    private JLabel gioVaoHienThi;

    public SlotDetailPanel(JLabel maViTri, JLabel trangThai, JLabel bienSoHienThi) {
        super(new BorderLayout(0, 15));
        setOpaque(false);

        this.maViTri = maViTri;
        this.trangThai = trangThai;
        this.bienSoHienThi=bienSoHienThi;

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

    public void setNoiDung(
            JPanel noiDung,
            JLabel thongTinViTri,
            JLabel phiHienThi,
            JLabel loaiXeHienThi,
            JLabel gioVaoHienThi) {

        this.thongTinViTri = thongTinViTri;
        this.phiHienThi = phiHienThi;
        this.loaiXeHienThi = loaiXeHienThi;
        this.gioVaoHienThi = gioVaoHienThi;

        add(noiDung, BorderLayout.CENTER);
    }


    public void capNhat(ParkingSlot viTri) {//ham hiển thị thong tin chi tiet
        maViTri.setText(viTri.getId());

        if (viTri.isEmpty()) {
            trangThai.setText("TRỐNG");
            thongTinViTri.setText("Sẵn sàng nhận xe.");

            bienSoHienThi.setVisible(false);
            phiHienThi.setVisible(false);

            loaiXeHienThi.setText("--");
            gioVaoHienThi.setText("--");
            return;
        }

        Vehicle xe = viTri.getVehicle();

        trangThai.setText("ĐANG CÓ XE");
        thongTinViTri.setText("");

        bienSoHienThi.setText(xe.getBienSo());
        bienSoHienThi.setVisible(true);

        loaiXeHienThi.setText(xe.getLoaiXe());

        String gioVao = viTri.getThoiGianVao()
                .format(DateTimeFormatter.ofPattern(
                        "HH:mm:ss dd/MM/yyyy"
                ));

        gioVaoHienThi.setText(gioVao);

        String tien = String.format(Locale.US, "%,d",
                viTri.getPhiGui()).replace(",", ".");

        phiHienThi.setText(tien + "đ");
        phiHienThi.setVisible(true);
    }

}