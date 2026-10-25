package code;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.swing.*;
import java.awt.*;
//QUẢN LÝ TOÀN BỘ PHIẾU THÔNG TIN GỬI XE
public class SlotDetailPanel extends JPanel {//Định nghĩa từ Jpanel

    // Các thành phần hiển thị của bảng chi tiết
    private JLabel maViTri = new JLabel("--");//phuc vu hien thi ma vi tri
    private JLabel trangThai = new JLabel("CHƯA CHỌN");//phuc vu hien thi trang thai
    private JLabel thongTinViTri = new JLabel("Chọn một ô để xem chi tiết");//phuc vu hien thi thong tin
    private JLabel bienSoHienThi = new JLabel("");//phuc vu hien thi bien so
    private JLabel phiHienThi = new JLabel("0đ");//phuc vu hien thi phi
    private JLabel loaiXeHienThi = new JLabel("--");
    private JLabel gioVaoHienThi = new JLabel("--");


    public SlotDetailPanel() {
        super(new BorderLayout(0, 15));
        setOpaque(false);

        //Thiết lập màu sắc và font
        maViTri.setFont(AppFonts.display(44f));

        thongTinViTri.setForeground(Color.WHITE);
        thongTinViTri.setFont(AppFonts.body(15f));

        bienSoHienThi.setFont(AppFonts.BienSo(24f));
        bienSoHienThi.setForeground(new Color(17, 20, 22));
        bienSoHienThi.setBackground(new Color(248, 248, 243));
        bienSoHienThi.setOpaque(true);
        bienSoHienThi.setBorder(
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        );
        bienSoHienThi.setVisible(false);//ban đầu chưa chọn ô nào nên ẩn biển số

        phiHienThi.setFont(AppFonts.display(42f));
        phiHienThi.setForeground(new Color(242, 183, 5));
        phiHienThi.setVisible(false);

        loaiXeHienThi.setForeground(Color.WHITE);
        gioVaoHienThi.setForeground(Color.WHITE);

        //Hàng tiêu đề
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

        //Khu vực biển số
        JPanel hangBienSo = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 0, 0)
        );
        hangBienSo.setOpaque(false);
        hangBienSo.add(bienSoHienThi);

        //Bảng loại xe và giờ vào
        JPanel bangThongTin = new JPanel(
                new GridLayout(2, 2, 10, 12)
        );
        bangThongTin.setOpaque(false);

        JLabel nhanLoaiXe = new JLabel("Loại xe");
        JLabel nhanGioVao = new JLabel("Giờ vào");

        Color mauChuPhu = new Color(152, 162, 168);
        nhanLoaiXe.setForeground(mauChuPhu);
        nhanGioVao.setForeground(mauChuPhu);

        bangThongTin.add(nhanLoaiXe);
        bangThongTin.add(loaiXeHienThi);
        bangThongTin.add(nhanGioVao);
        bangThongTin.add(gioVaoHienThi);

        JPanel khuThongTin = new JPanel(new BorderLayout(0, 10));
        khuThongTin.setOpaque(false);
        khuThongTin.add(thongTinViTri, BorderLayout.NORTH);
        khuThongTin.add(bangThongTin, BorderLayout.CENTER);

        // Khu vực phí gửi xe
        JPanel khuPhi = new JPanel(new BorderLayout(0, 6));
        khuPhi.setOpaque(false);

        khuPhi.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(
                        1, 0, 0, 0, new Color(50, 58, 63)
                ),
                BorderFactory.createEmptyBorder(14, 0, 0, 0)
        ));

        JLabel nhanPhi = new JLabel("PHÍ HIỆN TẠI");
        nhanPhi.setForeground(mauChuPhu);

        khuPhi.add(nhanPhi, BorderLayout.NORTH);
        khuPhi.add(phiHienThi, BorderLayout.CENTER);

        //Ghép nội dung phiếu
        JPanel noiDungPhieu = new JPanel(new BorderLayout(0, 10));
        noiDungPhieu.setOpaque(false);
        noiDungPhieu.setBorder(
                BorderFactory.createEmptyBorder(8, 0, 12, 0)
        );

        noiDungPhieu.add(hangBienSo, BorderLayout.NORTH);
        noiDungPhieu.add(khuThongTin, BorderLayout.CENTER);
        noiDungPhieu.add(khuPhi, BorderLayout.SOUTH);

        add(hangTieuDe, BorderLayout.NORTH);
        add(noiDungPhieu, BorderLayout.CENTER);
    }

    //Cập nhật toàn bộ phiếu theo vị trí được chọn
    public void capNhat(ParkingSlot viTri) {
        if (viTri == null) {
            maViTri.setText("--");
            trangThai.setText("CHƯA CHỌN");
            trangThai.setForeground(new Color(177, 161, 161));
            return;
        }
        maViTri.setText(viTri.getId());
        if (viTri.isEmpty()) {
            trangThai.setText("TRỐNG");
            trangThai.setForeground(new Color(31, 230, 71));
            thongTinViTri.setText("Sẵn sàng nhận xe.");

            bienSoHienThi.setVisible(false);
            phiHienThi.setVisible(false);

            loaiXeHienThi.setText("--");
            gioVaoHienThi.setText("--");

        } else {
            Vehicle xe = viTri.getVehicle();

            trangThai.setText("ĐANG CÓ XE");
            trangThai.setForeground(new Color(242, 183, 5));
            thongTinViTri.setText("");

            bienSoHienThi.setText(xe.getBienSo());
            bienSoHienThi.setVisible(true);

            loaiXeHienThi.setText(xe.getLoaiXe());

            String gioVao = viTri.getThoiGianVao()
                    .format(DateTimeFormatter.ofPattern(
                            "HH:mm:ss dd/MM/yyyy"
                    ));
            gioVaoHienThi.setText(gioVao);

            String tien = String.format(
                    Locale.US, "%,d", viTri.getPhiGui()
            ).replace(",", ".");

            phiHienThi.setText(tien + "đ");
            phiHienThi.setVisible(true);
        }

        revalidate();
        repaint();
    }
}