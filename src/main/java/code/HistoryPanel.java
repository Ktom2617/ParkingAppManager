package code;
//miha
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HistoryPanel extends JPanel {

    // Bảng màu chuẩn theo thiết kế Dark Mode trong ParkManager.html
    private static final Color MAU_NEN_CARD = new Color(26, 30, 33);     // --surface: #1A1E21
    private static final Color MAU_NEN_HOVER = new Color(34, 39, 43);    // --surface-2: #22272B
    private static final Color MAU_CHU_CHINH = new Color(231, 234, 235); // --ink: #E7EAEB
    private static final Color MAU_CHU_PHU = new Color(152, 162, 168);   // --ink-2: #98A2A8
    private static final Color MAU_DUONG_KE = new Color(50, 58, 63);     // --rule: #323A3F
    private static final Color MAU_VANG = new Color(242, 183, 5);        // --signal: #F2B705

    private DefaultTableModel modelBang;
    private JTable bangLichSu;
    private JButton nutThuGon;

    // Lưu danh sách vé để phục vụ nút "Xem tất cả / Thu gọn"
    private List<ParkingTicket> danhSachHienTai = new ArrayList<>();
    private boolean hienTatCa = false; // Mặc định hiển thị 5 lượt gần nhất giống HTML

    // Định dạng thời gian và tiền tệ
    private static final DateTimeFormatter DINH_DANG_GIO = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DINH_DANG_NGAY_GIO = DateTimeFormatter.ofPattern("dd/MM HH:mm");
    private final DecimalFormat dinhDangTien;

    public HistoryPanel() {
        // Cấu hình dấu chấm phân cách hàng nghìn cho tiền tệ (VD: 20.000đ)
        DecimalFormatSymbols kyHieu = new DecimalFormatSymbols(new Locale("vi", "VN"));
        kyHieu.setGroupingSeparator('.');
        dinhDangTien = new DecimalFormat("#,###", kyHieu);

        setLayout(new BorderLayout(0, 0));
        setBackground(MAU_NEN_CARD);
        putClientProperty("FlatLaf.style", "arc: 18; background: #1A1E21");
        setBorder(BorderFactory.createLineBorder(MAU_DUONG_KE, 1));

        // 1. Tạo phần tiêu đề (card-head)
        add(taoPhanDau(), BorderLayout.NORTH);

        // 2. Tạo phần bảng dữ liệu (hist-wrap)
        add(taoPhanBang(), BorderLayout.CENTER);
        Timer timerLichSu = new Timer(60_000, e -> {//tính tgian gửi
            doDuLieuVaoBang();
        });

        timerLichSu.start();
    }

    /**
     * Tạo thanh tiêu đề gồm tên bảng, dòng mô tả phụ và nút Xem tất cả / Thu gọn.
     */
    private JPanel taoPhanDau() {
        JPanel header = new JPanel(new BorderLayout(16, 4));
        header.setOpaque(false);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, MAU_DUONG_KE),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)
        ));

        // Cụm tiêu đề bên trái
        JPanel cumTieuDe = new JPanel(new GridLayout(2, 1, 0, 3));
        cumTieuDe.setOpaque(false);

        JLabel tieuDe = new JLabel("Lịch sử gần đây");
        tieuDe.setFont(AppFonts.display(24f));
        tieuDe.setForeground(MAU_CHU_CHINH);

        JLabel moTaPhu = new JLabel("Xe mới vào nằm trên cùng.");
        moTaPhu.setFont(AppFonts.body(13f));
        moTaPhu.setForeground(MAU_CHU_PHU);

        cumTieuDe.add(tieuDe);
        cumTieuDe.add(moTaPhu);

        // Nút Xem tất cả / Thu gọn bên phải (ẩn khi số vé <= 5)
        nutThuGon = new JButton("Xem tất cả");
        nutThuGon.setFont(AppFonts.body(13f));
        nutThuGon.setForeground(MAU_CHU_CHINH);
        nutThuGon.setContentAreaFilled(false);
        nutThuGon.setBorderPainted(false);
        nutThuGon.setFocusPainted(false);
        nutThuGon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nutThuGon.setVisible(false);

        nutThuGon.addActionListener(e -> {
            hienTatCa = !hienTatCa;
            doDuLieuVaoBang();
        });


        JButton nutXuatCsv = new JButton("Xuất CSV");//excel
        nutXuatCsv.setBackground(new Color(242, 183, 5));
        nutXuatCsv.setForeground(new Color(17, 20, 22));

        nutXuatCsv.addActionListener(e -> xuatCsv());

        JPanel cumNut = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        cumNut.setOpaque(false);

        cumNut.add(nutThuGon);
        cumNut.add(nutXuatCsv);

        header.add(cumTieuDe, BorderLayout.WEST);
        header.add(cumNut, BorderLayout.EAST);

        return header;
    }

    /**
     * Tạo JTable và JScrollPane hiển thị danh sách lượt gửi xe.
     */
    private JScrollPane taoPhanBang() {
        String[] tenCot = {
                "VÉ", "GIỜ VÀO", "GIỜ RA", "BIỂN SỐ", "LOẠI XE",
                "VỊ TRÍ", "THỜI GIAN GỬI", "PHÍ", "TRẠNG THÁI"
        };

        // Khóa không cho người dùng sửa trực tiếp các ô trong bảng
        modelBang = new DefaultTableModel(tenCot, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        bangLichSu = new JTable(modelBang);
        bangLichSu.setBackground(MAU_NEN_CARD);
        bangLichSu.setForeground(MAU_CHU_CHINH);
        bangLichSu.setSelectionBackground(MAU_NEN_HOVER);
        bangLichSu.setSelectionForeground(MAU_CHU_CHINH);
        bangLichSu.setRowHeight(40);
        bangLichSu.setFont(AppFonts.body(14f));
        bangLichSu.setShowVerticalLines(false);
        bangLichSu.setShowHorizontalLines(true);
        bangLichSu.setGridColor(MAU_DUONG_KE);
        bangLichSu.setIntercellSpacing(new Dimension(0, 1));
        bangLichSu.setFillsViewportHeight(true);

        // Tùy chỉnh thanh tiêu đề cột (thead th)
        JTableHeader tableHeader = bangLichSu.getTableHeader();
        tableHeader.setReorderingAllowed(false);
        tableHeader.setPreferredSize(new Dimension(0, 38));
        tableHeader.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                lbl.setBackground(MAU_NEN_CARD);
                lbl.setForeground(MAU_CHU_PHU);
                lbl.setFont(AppFonts.bodyBold(14f));

                // Căn phải cho cột THỜI GIAN GỬI (cột 6) và PHÍ (cột 7) giống class .num trong HTML
                if (column == 6 || column == 7) {
                    lbl.setHorizontalAlignment(SwingConstants.RIGHT);
                } else {
                    lbl.setHorizontalAlignment(SwingConstants.LEFT);
                }

                // Đường viền đậm dưới header giống border-bottom: 2px solid var(--ink)
                lbl.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 2, 0, MAU_DUONG_KE),
                        BorderFactory.createEmptyBorder(0, 14, 0, 14)
                ));
                return lbl;
            }
        });

        // Tùy chỉnh cách hiển thị từng ô dữ liệu (tbody td)
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            private final Border padding = BorderFactory.createEmptyBorder(0, 14, 0, 14);

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                lbl.setBackground(isSelected ? MAU_NEN_HOVER : MAU_NEN_CARD);
                lbl.setForeground(MAU_CHU_CHINH);
                lbl.setFont(AppFonts.body(14f));
                lbl.setBorder(padding);

                // Căn lề: Thời gian gửi (6) và Phí (7) căn phải
                if (column == 6 || column == 7) {
                    lbl.setHorizontalAlignment(SwingConstants.RIGHT);
                } else {
                    lbl.setHorizontalAlignment(SwingConstants.LEFT);
                }

                // Làm nổi bật cột Biển số (cột 3)
                if (column == 3) {
                    lbl.setFont(AppFonts.bodyBold(14f));
                }

                // Đổi màu cột Trạng thái (cột 8): vàng nếu đang gửi, xám nếu đã trả
                if (column == 8 && value != null) {
                    String text = value.toString();
                    if (text.contains("Đang gửi")) {
                        lbl.setForeground(MAU_VANG);
                        lbl.setFont(AppFonts.bodyBold(14f));
                    } else {
                        lbl.setForeground(MAU_CHU_PHU);
                    }
                }

                return lbl;
            }
        };

        for (int i = 0; i < bangLichSu.getColumnCount(); i++) {
            bangLichSu.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        // Đặt độ rộng ưu tiên cho các cột
        bangLichSu.getColumnModel().getColumn(0).setPreferredWidth(60);  // Vé
        bangLichSu.getColumnModel().getColumn(1).setPreferredWidth(95);  // Giờ vào
        bangLichSu.getColumnModel().getColumn(2).setPreferredWidth(95);  // Giờ ra
        bangLichSu.getColumnModel().getColumn(3).setPreferredWidth(110); // Biển số
        bangLichSu.getColumnModel().getColumn(4).setPreferredWidth(85);  // Loại xe
        bangLichSu.getColumnModel().getColumn(5).setPreferredWidth(65);  // Vị trí
        bangLichSu.getColumnModel().getColumn(6).setPreferredWidth(110); // Thời gian gửi
        bangLichSu.getColumnModel().getColumn(7).setPreferredWidth(140); // Phí
        bangLichSu.getColumnModel().getColumn(8).setPreferredWidth(130); // Trạng thái

        JScrollPane scrollPane = new JScrollPane(bangLichSu);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(MAU_NEN_CARD);
        return scrollPane;
    }

    /**
     * Phương thức kết nối chính: nhận danh sách ParkingTicket và cập nhật lại bảng lịch sử.
     */
    public void capNhatLichSu(List<ParkingTicket> danhSachVe) {
        if (danhSachVe == null) {
            this.danhSachHienTai = new ArrayList<>();
        } else {
            this.danhSachHienTai = new ArrayList<>(danhSachVe);
        }
        doDuLieuVaoBang();
    }

    /**
     * Đổ dữ liệu từ danhSachHienTai vào JTable theo thứ tự xe mới vào nằm trên cùng.
     */
    private void doDuLieuVaoBang() {
        modelBang.setRowCount(0);
        int tongSoVe = danhSachHienTai.size();

        // Cập nhật nút "Xem tất cả (N)" / "Thu gọn" nếu có nhiều hơn 5 vé
        if (tongSoVe > 5) {
            nutThuGon.setVisible(true);
            nutThuGon.setText(hienTatCa ? "Thu gọn" : "Xem tất cả (" + tongSoVe + ")");
        } else {
            nutThuGon.setVisible(false);
        }

        int soLuongHienThi = hienTatCa ? tongSoVe : Math.min(5, tongSoVe);
        int dem = 0;

        // Duyệt ngược từ cuối danh sách về đầu để vé mới nhất nằm ở dòng trên cùng
        for (int i = tongSoVe - 1; i >= 0 && dem < soLuongHienThi; i--) {
            ParkingTicket ve = danhSachHienTai.get(i);
            Vehicle xe = ve.getVehicle();
            boolean dangGui = ve.isActive();

            // 1. Mã số vé (định dạng 4 chữ số giống HTML: 0001, 0002...)
            String maVe = String.format("%04d", i + 1);

            // 2. Giờ vào & Giờ ra
            LocalDateTime gioVao = ve.getGioVao();
            LocalDateTime mocKetThuc = dangGui ? LocalDateTime.now() : ve.getGioRa();
            String chuoiGioVao = dinhDangMocThoiGian(gioVao);
            String chuoiGioRa = dangGui ? "--" : dinhDangMocThoiGian(ve.getGioRa());

            // 3. Biển số, Loại xe, Vị trí
            String bienSo = (xe != null) ? xe.getBienSo() : "--";
            String loaiXe = (xe != null) ? xe.getLoaiXe() : "--";
            String viTri = ve.getMaViTri();

            // 4. Thời gian gửi (VD: 2h15p hoặc 48p)
            String thoiGianGui = tinhChuoiThoiGianGui(gioVao, mocKetThuc);

            // 5. Phí gửi (sử dụng đúng phương thức tinhPhi có sẵn của Vehicle nếu xe đang gửi)
            String chuoiPhi;
            if (dangGui) {
                long phiTamTinh = (xe != null) ? xe.tinhPhi(gioVao, mocKetThuc) : 0;
                chuoiPhi = "<html>" + dinhDangTien.format(phiTamTinh) + "đ "
                        + "<font color='#98A2A8' size='3'>tạm tính</font></html>";
            } else {
                chuoiPhi = dinhDangTien.format(ve.getPhiGui()) + "đ";
            }

            // 6. Trạng thái
            String trangThai = dangGui
                    ? "● Đang gửi"
                    : "✓ Đã trả " + ve.getGioRa().format(DINH_DANG_GIO);

            modelBang.addRow(new Object[]{
                    maVe, chuoiGioVao, chuoiGioRa, bienSo, loaiXe,
                    viTri, thoiGianGui, chuoiPhi, trangThai
            });

            dem++;
        }
    }

    /**
     * Định dạng thời gian giống hàm stamp() trong HTML:
     * Nếu trong cùng ngày hôm nay thì chỉ hiện HH:mm, nếu khác ngày thì hiện dd/MM HH:mm.
     */
    private String dinhDangMocThoiGian(LocalDateTime thoiGian) {
        if (thoiGian == null) {
            return "--";
        }
        if (thoiGian.toLocalDate().equals(LocalDate.now())) {
            return thoiGian.format(DINH_DANG_GIO);
        }
        return thoiGian.format(DINH_DANG_NGAY_GIO);
    }

    /**
     * Tính khoảng thời gian gửi xe giống hàm dur() trong HTML (VD: "2h15p" hoặc "45p").
     */
    private String tinhChuoiThoiGianGui(LocalDateTime batDau, LocalDateTime ketThuc) {
        if (batDau == null || ketThuc == null || ketThuc.isBefore(batDau)) {
            return "0p";
        }
        long tongPhut = Duration.between(batDau, ketThuc).toMinutes();
        long gio = tongPhut / 60;
        long phutLe = tongPhut % 60;

        if (gio > 0) {
            return String.format("%dh%02dp", gio, phutLe);
        }
        return phutLe + "p";
    }

    private void xuatCsv() {
        if (danhSachHienTai.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chưa có lịch sử để xuất!");
            return;
        }

        JFileChooser chonFile = new JFileChooser();
        chonFile.setSelectedFile(new java.io.File("lich-su-gui-xe.csv"));

        if (chonFile.showSaveDialog(this)
                != JFileChooser.APPROVE_OPTION) {
            return;
        }

        java.io.File file = chonFile.getSelectedFile();

        if (!file.getName().toLowerCase(java.util.Locale.ROOT)
                .endsWith(".csv")) {
            file = new java.io.File(
                    file.getParentFile(), file.getName() + ".csv");
        }

        if (file.exists()) {
            int xacNhan = JOptionPane.showConfirmDialog(
                    this, "File đã tồn tại. Bạn muốn ghi đè?",
                    "Xác nhận", JOptionPane.YES_NO_OPTION);

            if (xacNhan != JOptionPane.YES_OPTION) return;
        }

        try {
            HistoryCsvExporter.xuat(danhSachHienTai, file);
            JOptionPane.showMessageDialog(this, "Xuất CSV thành công!");
        } catch (java.io.IOException ex) {
            JOptionPane.showMessageDialog(
                    this, "Không thể xuất file: " + ex.getMessage());
        }
    }

}