package code;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.LinkedHashMap;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ParkingFrame {
    private static JButton nutDangChon = null;//nhớ nút hiện đang được chọn.
    private static javax.swing.border.Border vienGoc = null;//nhớ viền trắng ban đầu của nút đó để có thể khôi phục
    private static ParkingSlot viTriDangChon = null;//nhớ parking slot nào đang đc chon
    public static void createWindow() {
//        Color MAU_TRONG = new Color(33, 40, 45);//màu của slot khi empty
//        Color MAU_CO_XE = new Color(72, 68, 45);//màu của slot khi có xe
        JFrame frame = new JFrame("Parking Manager System - Group 6");//tạo frame cửa số
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new TrangCuonPanel(new BorderLayout(15, 15));
        panel.setBackground(new Color(17, 20, 22));//background
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("PARKING MANAGER - PARKING MAP", SwingConstants.CENTER);//title
        title.setFont(AppFonts.display(24f));


        JPanel map = new JPanel(new GridLayout(1, 3, 36, 0));//Bản đồ gồm 1 hàng, 3 dãy, cách nhau 36 pixel để dành chỗ cho làn đường
        map.setBackground(new Color(43, 49, 54));
        map.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        //Mỗi dãy có 4 ô xếp dọc, cách nhau 10 pixel
        JPanel cot1 = new JPanel(new GridLayout(4, 1, 0, 0));
        JPanel cot2 = new JPanel(new GridLayout(4, 1, 0, 0));
        JPanel cot3 = new JPanel(new GridLayout(4, 1, 0, 0));

        cot1.setOpaque(false);
        cot2.setOpaque(false);
        cot3.setOpaque(false);
        //đua vào bản đồ
        map.add(cot1);
        map.add(cot2);
        map.add(cot3);

        String[] bienSoXe = new String[12];//mảng lưu biển số xe
        ParkingManager quanLy = new ParkingManager();//quan lý đỗ xe bên class ParkingManagẻr
        HistoryPanel lichSuPanel = new HistoryPanel();//quản lý lịch sử
        JLabel soDangCo = new JLabel("0");//phục vụ cho hiển thị số liệu thống kê ở giao diện chính
        JLabel soConTrong = new JLabel("12");//phục vụ cho hiển thị số liệu thống kê ở giao diện chính
        JLabel soDoanhThu = new JLabel("0đ");//phuc vu hien thi doanh thu
        JButton nutTraXe = new JButton("TRẢ XE");//nút trả xe
        JButton nutNhanXe = new JButton("NHẬN XE");//nút nhận xe
        JButton nutSuaXe = new JButton("SỬA THÔNG TIN");//nút sửa thông tin xe đang gửi
        nutSuaXe.setEnabled(false);
        nutSuaXe.setFont(AppFonts.bodyBold(15f));
        nutSuaXe.putClientProperty("JButton.buttonType", "roundRect");
//tạo ô nhâp liệu
        JTextField nhapBienSo = new JTextField();
        JComboBox<String> chonLoaiXe = new JComboBox<>(
                new String[]{"Xe máy", "Ô tô"}
        );
        //làm đẹp
        nhapBienSo.setPreferredSize(new Dimension(190, 44));
        chonLoaiXe.setPreferredSize(new Dimension(190, 44));
        JPanel formNhanXe = new JPanel(new GridLayout(2, 1, 0, 14));
        formNhanXe.setOpaque(false);
        JLabel nhanBienSo = new JLabel("BIỂN SỐ");
        JLabel nhanLoaiXe = new JLabel("LOẠI XE");
        JPanel nhomBienSo = new JPanel(new BorderLayout(0, 6));
        JPanel nhomLoaiXe = new JPanel(new BorderLayout(0, 6));

        nhomBienSo.setOpaque(false);
        nhomLoaiXe.setOpaque(false);

        nhomBienSo.add(nhanBienSo, BorderLayout.NORTH);
        nhomBienSo.add(nhapBienSo, BorderLayout.CENTER);

        nhomLoaiXe.add(nhanLoaiXe, BorderLayout.NORTH);
        nhomLoaiXe.add(chonLoaiXe, BorderLayout.CENTER);

        formNhanXe.add(nhomBienSo);
        formNhanXe.add(nhomLoaiXe);
//làm đẹp
        Color mauChuPhu = new Color(152, 162, 168);
        Color mauNenInput = new Color(34, 39, 43);

        nhanBienSo.setForeground(mauChuPhu);
        nhanLoaiXe.setForeground(mauChuPhu);
        nhanLoaiXe.setFont(AppFonts.bodyBold(13f));


        nhapBienSo.setBackground(mauNenInput);
        nhapBienSo.setForeground(Color.WHITE);
        nhapBienSo.setCaretColor(Color.WHITE);
        nhanBienSo.setFont(AppFonts.bodyBold(13f));

        chonLoaiXe.setBackground(mauNenInput);
        chonLoaiXe.setForeground(Color.WHITE);
        chonLoaiXe.setFont(AppFonts.body(15f));

        nhapBienSo.putClientProperty(
                "JTextField.placeholderText", "VD: 29A-12345");

        nhapBienSo.putClientProperty("FlatLaf.style", "arc: 12");
        chonLoaiXe.putClientProperty("FlatLaf.style", "arc: 12");

        formNhanXe.setVisible(false);

        SlotDetailPanel chiTietXe = new SlotDetailPanel();
        Map<ParkingSlot, JButton> nutTheoViTri = new LinkedHashMap<>();//phục vụ search

        //lặp tạo button
        for (int i = 1; i <= 12; i++) {//mỗi lần lặp tạo 1 nút mới JButton
            String id = "A" + i;
            ParkingSlot parkingSlot = new ParkingSlot(id);
            if (i == 1) {
                quanLy.checkIn(parkingSlot, new Car("29A-67676"));//du lieu demo thử, hệ thống quản lý sẽ thục hiện việc check-in xe
            }
            ParkingSlotButton slot = new ParkingSlotButton(id);//thêm 1 button
            nutTheoViTri.put(parkingSlot, slot);
            slot.setFocusPainted(false);
            slot.setFont(AppFonts.bodyBold(14f));
            slot.setForeground(Color.WHITE);
            slot.setForeground(new Color(25, 35, 45));
            slot.setOpaque(true);
            slot.setHorizontalTextPosition(SwingConstants.CENTER);
            slot.setVerticalTextPosition(SwingConstants.BOTTOM);
            slot.setIconTextGap(2);//icon nằm phía trên, chữ nằm ở dưới, cách nhau 2 pixel

            capNhatHienThiO(slot, parkingSlot);

            slot.addActionListener(e -> {//gắn một hành động vào nút để Java biết phải làm gì khi người dùng tương tác
               //Hệ thống xử lý chọn các ô
                //Nếu trước đó đã chọn ô khác → trả ô cũ về viền trắng gốc
                        if (nutDangChon != slot) {
                            if (nutDangChon != null) {
                                nutDangChon.setBorder(vienGoc);
                            }
                //Lưu viền trắng gốc của ô mới và thêm viền vàng vào bên trong
                vienGoc = slot.getBorder();

                            slot.setBorder(BorderFactory.createCompoundBorder(
                                    vienGoc,
                                    BorderFactory.createLineBorder(
                                            new Color(242, 183, 5), 3
                                    )
                            ));
                        };

                nutDangChon = slot;  //Ghi nhớ ô mới là nutDangChon
                viTriDangChon = parkingSlot;//Ghi nhớ vị chon
                nutTraXe.setEnabled(!parkingSlot.isEmpty());//chọn ô có xe thì nút trả xe được bật. Chọn ô trống thì nút bị khóa
                nutNhanXe.setEnabled(parkingSlot.isEmpty());//choọn ô trống thì bật nút này lên
                nutSuaXe.setEnabled(!parkingSlot.isEmpty());//ô có xe thì bật nút
                formNhanXe.setVisible(parkingSlot.isEmpty());//chỉ hiện form khi ô trống
                chiTietXe.capNhat(viTriDangChon);//cap nhat thong tin vi tri khi click
            });


            int soO = Integer.parseInt(id.substring(1));//lấy phần sau chữ A để so sánh số

            if (soO <= 4) {
                cot1.add(slot);//A1–A4 → cot1
            } else if (soO <= 8) {
                cot2.add(slot);//A5–A8 → cot2
            } else {
                cot3.add(slot);//- A9–A12 → cot3
            }
//xử lý vạch kẻ slot
            int cot = (soO - 1) / 4;
            int vienTrai = (cot == 0) ? 3 : 0;
            int vienPhai = (cot == 2) ? 3 : 0;
            int vienDuoi = (soO % 4 == 0) ? 3 : 0;
            slot.setBorder(BorderFactory.createMatteBorder(
                    3, vienTrai, vienDuoi, vienPhai,
                    new Color(233, 232, 223)
            ));

        }

        JPanel header = new JPanel(new BorderLayout(16, 0));//tạo giao diện - tạo tiêu đề headẻr
        header.setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 18));
        header.putClientProperty("FlatLaf.style",
                "arc: 18; background: #1A1E21");


        JLabel logo = new JLabel("P", SwingConstants.CENTER);
        logo.setFont(AppFonts.display(28f));
        logo.setForeground(new Color(28, 22, 0));
        logo.setBackground(new Color(242, 183, 5));
        logo.setOpaque(true);
        logo.setPreferredSize(new Dimension(38, 38));
        logo.setBorder(BorderFactory.createLineBorder(
                new Color(28, 22, 0), 2
        ));


        JLabel tenApp = new JLabel("Parking Manager");
        tenApp.setFont(AppFonts.display(26f));
        tenApp.setForeground(new Color(236, 235, 227));

        JLabel moTaApp = new JLabel("PARKING SYSTEM");
        moTaApp.setFont(AppFonts.body(11f));
        moTaApp.setForeground(new Color(167, 174, 178));

        JPanel cumTen = new JPanel(new GridLayout(2, 1, 0, 0));
        cumTen.setOpaque(false);
        cumTen.add(tenApp);
        cumTen.add(moTaApp);

        JPanel cumLogo = new JPanel(new BorderLayout(10, 0));
        cumLogo.setOpaque(false);
        cumLogo.add(logo, BorderLayout.WEST);
        cumLogo.add(cumTen, BorderLayout.CENTER);

        header.add(cumLogo, BorderLayout.WEST);


        JTextField timBienSo = new JTextField();//seảrch
        timBienSo.putClientProperty(
                "JTextField.placeholderText", "Tìm biển số xe..."
        );
        timBienSo.putClientProperty(
                "JTextField.showClearButton", true
        );
        timBienSo.setPreferredSize(new Dimension(185, 34));

        JPanel khungTimKiem = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 0, 0)
        );
        khungTimKiem.setOpaque(false);
        khungTimKiem.add(timBienSo);
        header.add(khungTimKiem, BorderLayout.CENTER);

        timBienSo.addActionListener(e -> {
            String tuKhoa = timBienSo.getText()
                    .trim().toLowerCase();
            if (tuKhoa.isEmpty()) {
                return;
            }
            for (Map.Entry<ParkingSlot, JButton> item
                    : nutTheoViTri.entrySet()) {
                ParkingSlot viTri = item.getKey();
                if (!viTri.isEmpty()) {
                    String bienSo = viTri.getVehicle()
                            .getBienSo().toLowerCase();
                    if (bienSo.contains(tuKhoa)) {
                        item.getValue().doClick();//yêu cầu Java thực hiện một cú click lên nút tìm được
                        return;
                    }
                }
            }
            JOptionPane.showMessageDialog(
                    frame, "Không tìm thấy xe có biển số: "
                            + timBienSo.getText().trim()
            );
        });


//TẠO đồng hồ
        JLabel dongHo = new JLabel();
        JLabel ngayThang = new JLabel();

        dongHo.setFont(AppFonts.display(32f));
        dongHo.setForeground(new Color(233, 232, 223));
        dongHo.setHorizontalAlignment(SwingConstants.RIGHT);

        ngayThang.setFont(AppFonts.body(13f));
        ngayThang.setForeground(new Color(167, 174, 178));
        ngayThang.setHorizontalAlignment(SwingConstants.RIGHT);

        dongHo.setAlignmentX(Component.RIGHT_ALIGNMENT);
        ngayThang.setAlignmentX(Component.RIGHT_ALIGNMENT);

// Gom hai dong vao mot JPanel
        JPanel cumDongHo = new JPanel();
        cumDongHo.setOpaque(false);
        cumDongHo.setLayout(
                new BoxLayout(cumDongHo, BoxLayout.Y_AXIS)
        );

        cumDongHo.add(dongHo);
        cumDongHo.add(ngayThang);

// Dinh dang thoi gian
        DateTimeFormatter dinhDangGio =
                DateTimeFormatter.ofPattern("HH:mm:ss");

        DateTimeFormatter dinhDangNgay =
                DateTimeFormatter.ofPattern(
                        "EEEE, dd/MM/yyyy",
                        Locale.forLanguageTag("vi-VN")
                );
// Cap nhat ca hai dong
        Runnable capNhatDongHo = () -> {
            LocalDateTime hienTai = LocalDateTime.now();
            dongHo.setText(hienTai.format(dinhDangGio));
            ngayThang.setText(hienTai.format(dinhDangNgay));
        };
// Hien thi ngay khi mo ung dung
        capNhatDongHo.run();
// Cap nhat moi giay
        Timer timerDongHo = new Timer(
                1000,
                e -> capNhatDongHo.run()
        );
        timerDongHo.start();
        header.add(cumDongHo, BorderLayout.EAST);

        JPanel stats = new JPanel(new GridLayout(1, 4, 12, 0));//tạo giao dien thẻ thống kê/thông tin
        stats.setOpaque(false);
        stats.add(createStatCard("SỨC CHỨA", new JLabel("12")));
        stats.add(createStatCard("ĐANG CÓ XE", soDangCo));
        stats.add(createStatCard("CÒN TRỐNG", soConTrong));
        stats.add(createStatCard("DOANH THU", soDoanhThu));
        soDoanhThu.setForeground(new Color(242, 183, 5));
        capNhatThongKe(quanLy, soDangCo, soConTrong, soDoanhThu);
        lichSuPanel.capNhatLichSu(quanLy.getDanhSachVe());


        JPanel top = new JPanel(new BorderLayout(0, 12));
        top.setBackground(new Color(17, 20, 22));
        top.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        top.add(header, BorderLayout.NORTH);
        top.add(stats, BorderLayout.CENTER);

        panel.add(top, BorderLayout.NORTH);

        JPanel details = new JPanel(new BorderLayout());//bảng thông tin
        details.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        details.putClientProperty("FlatLaf.style",
                "arc: 18; background: #1A1E21");

        //panel cũ đã đc chuyển sang SlotDetailPanel

        JPanel vungNhapXe = new JPanel(new BorderLayout());
        vungNhapXe.setOpaque(false);
        vungNhapXe.add(formNhanXe, BorderLayout.NORTH);

        JPanel noiDungChiTiet = new JPanel(new BorderLayout(0, 12));
        noiDungChiTiet.setOpaque(false);

        noiDungChiTiet.add(chiTietXe, BorderLayout.NORTH);
        noiDungChiTiet.add(vungNhapXe, BorderLayout.CENTER);

        details.add(noiDungChiTiet, BorderLayout.CENTER);


        nutTraXe.setBackground(new Color(242, 183, 5));
        nutTraXe.setForeground(new Color(28, 22, 0));
        nutTraXe.setFont(AppFonts.bodyBold(15f));
        nutNhanXe.setBackground(new Color(31, 127, 70));
        nutNhanXe.setForeground(new Color(214, 206, 206));
        nutNhanXe.setFont(AppFonts.bodyBold(15f));

        nutTraXe.putClientProperty("JButton.buttonType", "roundRect");
        nutTraXe.setEnabled(false);

        JPanel actions = new JPanel(new GridLayout(3, 1, 0, 10));//taạo khung 3 nút
        actions.setOpaque(false);
        actions.setBorder(
                BorderFactory.createEmptyBorder(16, 0, 0, 0)
        );

        nutNhanXe.setEnabled(false); // Chưa gắn chức năng
        nutNhanXe.putClientProperty("JButton.buttonType", "roundRect");

        actions.add(nutNhanXe);
        actions.add(nutTraXe);
        actions.add(nutSuaXe);
        details.add(actions, BorderLayout.SOUTH);



        nutNhanXe.addActionListener(e -> {//hành động xử lý nút nhận xe

            if (viTriDangChon == null || !viTriDangChon.isEmpty()) {
                return;
            }

            String bienSo = nhapBienSo.getText().trim();//oo nhâp biển số

            if (bienSo.isEmpty()) {
                JOptionPane.showMessageDialog(
                        frame, "Biển số không được để trống!"
                );
                return;
            }

            String loaiXe = (String) chonLoaiXe.getSelectedItem();


            // Tạo đối tượng xe
            Vehicle xe;

            if (loaiXe.equals("Ô tô")) {
                xe = new Car(bienSo);
            } else {
                xe = new Motorbike(bienSo);
            }

            // Check-in-lấy vị trí người dùng đã chọn và gửi xe vào đó
            ParkingSlot viTri = viTriDangChon;
            quanLy.checkIn(viTri, xe);

            // Cập nhật giao diện(module hóa)
            capNhatHienThiO(nutDangChon, viTri);

            chiTietXe.capNhat(viTriDangChon);
            capNhatThongKe(quanLy, soDangCo, soConTrong, soDoanhThu);
            lichSuPanel.capNhatLichSu(quanLy.getDanhSachVe());

            nutNhanXe.setEnabled(false);
            nutTraXe.setEnabled(true);
            nutSuaXe.setEnabled(true);
            nhapBienSo.setText("");
            formNhanXe.setVisible(false);//sau khi nhận xe, ô nhập sẽ được xóa và form tự ẩn
        });


        nutTraXe.addActionListener(e -> {//hành động xử lý nút trả xe

            if (viTriDangChon == null || viTriDangChon.isEmpty()) {
                return;
            }

            Vehicle xe = viTriDangChon.getVehicle();

            String thongBao = "Biển số: " + xe.getBienSo()
                    + "\nLoại xe: " + xe.getLoaiXe()
                    + "\nVị trí: " + viTriDangChon.getId()
                    + "\nPhí gửi: " + viTriDangChon.getPhiGui() + "đ"
                    + "\n\nXác nhận trả xe?";

            int luaChon = JOptionPane.showConfirmDialog(//tạo lua chọn confỉrm
                    frame, thongBao, "Xác nhận trả xe",
                    JOptionPane.YES_NO_OPTION
            );

            if (luaChon != JOptionPane.YES_OPTION) {
                return;
            }

            ParkingTicket ve = quanLy.checkOut(viTriDangChon);

            capNhatHienThiO(nutDangChon, viTriDangChon);

            chiTietXe.capNhat(viTriDangChon);
            capNhatThongKe(quanLy, soDangCo, soConTrong, soDoanhThu);
            lichSuPanel.capNhatLichSu(quanLy.getDanhSachVe());

            nutTraXe.setEnabled(false);
            nutNhanXe.setEnabled(true);
            nutSuaXe.setEnabled(false);
            formNhanXe.setVisible(true);
            System.out.println("Đã thu: " + ve.getPhiGui() + " VND");
        });


        nutSuaXe.addActionListener(e -> {//hành động xử lý nút sửa xe
            // 1. Kiem tra vi tri dang chon
            if (viTriDangChon == null || viTriDangChon.isEmpty()) {
                return;
            }

            Vehicle xeCu = viTriDangChon.getVehicle();

            // 2. Tao cac truong nhap
            JTextField nhapBienSoMoi = new JTextField(xeCu.getBienSo());

            JComboBox<String> chonLoaiXeMoi = new JComboBox<>(new String[]{"Xe máy", "Ô tô"});

            // Chon san loai xe hien tai
            chonLoaiXeMoi.setSelectedIndex(xeCu instanceof Car ? 1 : 0);

            // 3. Tao giao dien hop thoai
            JPanel formSua = new JPanel(new GridLayout(0, 1, 0, 8));
            formSua.add(new JLabel("BIỂN SỐ"));
            formSua.add(nhapBienSoMoi);
            formSua.add(new JLabel("LOẠI XE"));
            formSua.add(chonLoaiXeMoi);
            formSua.add(new JLabel("Giờ vào được giữ nguyên."));

            // 4. Hien hop thoai
            int luaChon = JOptionPane.showConfirmDialog(
                    frame,
                    formSua,
                    "Sửa thông tin xe - " + viTriDangChon.getId(),
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );
            if (luaChon != JOptionPane.OK_OPTION) {
                return;
            }
            // 5. Lay du lieu moi
            String bienSoMoi = nhapBienSoMoi.getText()
                    .trim()
                    .toUpperCase(java.util.Locale.ROOT);

            if (bienSoMoi.isEmpty()) {
                JOptionPane.showMessageDialog(
                        frame, "Biển số không được để trống!"
                );
                return;
            }

            Vehicle xeMoi;
            if (chonLoaiXeMoi.getSelectedIndex() == 1) {
                xeMoi = new Car(bienSoMoi);
            } else {
                xeMoi = new Motorbike(bienSoMoi);
            }
            // 6. Yeu cau Manager sua thong tin
            try {
                quanLy.suaThongTinXe(viTriDangChon, xeMoi);//kiểm tra dữ liệu và đồng bộ xe trong ParkingSlot với vé đang hoạt động trong ParkingTicket

                // 7. Cap nhat giao dien
                capNhatHienThiO(nutDangChon, viTriDangChon);
                chiTietXe.capNhat(viTriDangChon);
                lichSuPanel.capNhatLichSu(quanLy.getDanhSachVe());

                JOptionPane.showMessageDialog(
                        frame, "Đã cập nhật thông tin xe!"
                );

            } catch (IllegalArgumentException | IllegalStateException ex) {
                JOptionPane.showMessageDialog(
                        frame,
                        ex.getMessage(),
                        "Không thể sửa thông tin",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });


        JPanel khungMap = new JPanel(new BorderLayout());//chuyển map vào trung tâm khung map
        khungMap.setBackground(new Color(26, 30, 33));
        khungMap.setBorder(BorderFactory.createLineBorder(
                new Color(50, 58, 63), 1
        ));
// Tieu de ban do
        JLabel tieuDeMap = new JLabel("SƠ ĐỒ BÃI XE");
        tieuDeMap.setFont(AppFonts.display(24f));
        tieuDeMap.setForeground(new Color(231, 234, 235));

        JLabel moTaMap = new JLabel(
                "Chọn một ô để xem chi tiết hoặc nhận xe."
        );
        moTaMap.setFont(AppFonts.body(13f));
        moTaMap.setForeground(new Color(152, 162, 168));

        JPanel cumTieuDeMap = new JPanel(new GridLayout(2, 1, 0, 4));
        cumTieuDeMap.setOpaque(false);
        cumTieuDeMap.add(tieuDeMap);
        cumTieuDeMap.add(moTaMap);

        JPanel dauMap = new JPanel(new BorderLayout());
        dauMap.setOpaque(false);
        dauMap.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(
                        0, 0, 1, 0, new Color(50, 58, 63)),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)
        ));
        dauMap.add(cumTieuDeMap, BorderLayout.WEST);
        khungMap.add(dauMap, BorderLayout.NORTH);
        khungMap.add(map, BorderLayout.CENTER);

        JPanel chuThichMap = new JPanel(//chú thích
                new FlowLayout(FlowLayout.LEFT, 20, 10)
        );
        chuThichMap.setOpaque(false);

        JLabel chuThichTrong = new JLabel("□  Ô trống");
        JLabel chuThichCoXe = new JLabel("■  Có xe");
        JLabel chuThichDangChon = new JLabel("□  Đang chọn");

        chuThichTrong.setFont(AppFonts.body(13f));
        chuThichCoXe.setFont(AppFonts.body(13f));
        chuThichDangChon.setFont(AppFonts.body(13f));

        chuThichTrong.setForeground(new Color(152, 162, 168));
        chuThichCoXe.setForeground(new Color(231, 234, 235));
        chuThichDangChon.setForeground(new Color(242, 183, 5));

        chuThichMap.add(chuThichTrong);
        chuThichMap.add(chuThichCoXe);
        chuThichMap.add(chuThichDangChon);

        khungMap.add(chuThichMap, BorderLayout.SOUTH);



        JSplitPane split = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT, khungMap, details//tạo một vùng chia đôi, với map ở bên trái và details ở bên phải
        );
        split.setResizeWeight(0.67);
        split.setDividerSize(10);
        split.setBorder(null);

        split.setPreferredSize(new Dimension(900, 560));
        lichSuPanel.setPreferredSize(new Dimension(900, 300));

        panel.add(split, BorderLayout.CENTER);
        panel.add(lichSuPanel, BorderLayout.SOUTH);

        lichSuPanel.capNhatLichSu(quanLy.getDanhSachVe());

        JScrollPane cuonTrang = new JScrollPane(panel);//scroll
        cuonTrang.setBorder(null);
        cuonTrang.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        cuonTrang.getVerticalScrollBar().setUnitIncrement(20);
        frame.setContentPane(cuonTrang);
        frame.setVisible(true);
        split.setDividerLocation(0.68);
    }

    private static JPanel createStatCard(String ten, JLabel number) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBorder(BorderFactory.createEmptyBorder(15, 18, 15, 18));
        card.putClientProperty("FlatLaf.style",
                "arc: 16; background: #1A1E21");

        JLabel title = new JLabel(ten);
        title.setFont(AppFonts.display(20f));
        title.setForeground(new Color(170, 175, 185));

        number.setFont(AppFonts.display(34f));
        number.setForeground(new Color(231, 234, 235));

        card.add(title, BorderLayout.NORTH);
        card.add(number, BorderLayout.CENTER);

        return card;
    }
    private static void capNhatThongKe(ParkingManager quanLy, JLabel soDangCo, JLabel soConTrong, JLabel soDoanhThu){
        int soXe=quanLy.getSoXeDangGui();//cập nhật số xe cho các ô tổng hợp trên giao diện
        soDangCo.setText(""+soXe);//chuyển int sang string, lay soXe
        soConTrong.setText(""+(12-soXe));//chuyển int sang string, lay cho trong
        long doanhThu = quanLy.getDoanhThuHomNay();
        soDoanhThu.setText(dinhDangTien(doanhThu));//lay doanh thu
    }

    //ham hiển thị thong tin chi tiet cu da dc chuyen sang SlotDetailPanel

    private static String dinhDangTien(long soTien) {//hàm định dạng tiền
        return String.format("%,d", soTien)
                .replace(",", ".") + "đ";
    }

    //hàm cập nhật slot chung(thay vì phải chia ra 3 tính huống tạo, nhận, trả)
    private static void capNhatHienThiO(JButton nut, ParkingSlot viTri) {
        String id = viTri.getId();

        if (viTri.isEmpty()) {
            nut.setIcon(null);
            nut.setText("<html><center><font color='#E7EAEB'>"
                    + "TRỐNG"
                    + "</font></center></html>");

            nut.setBackground(new Color(43, 49, 54));
        } else {
            Vehicle xe = viTri.getVehicle();
            nut.setIcon(new VehicleIcon(xe instanceof Car));//kiểm tra đối tượng xe có phải ô tô không, nếu ko thì vẽ xe máy

            String gioVao = viTri.getThoiGianVao()
                    .format(DateTimeFormatter.ofPattern("HH:mm:ss"));

            String bienSo = xe.getBienSo()//giúp ký tự đặc biệt trong biển số không bị Java Swing hiểu nhầm là mã html
                    .replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;");

            nut.setText("<html><center><font color='#E7EAEB'>"
                    + xe.getLoaiXe() + " - " + bienSo
                    + "<br><font size='2'>VÀO " + gioVao + "</font>"
                    + "</font></center></html>");

            nut.setBackground(new Color(53, 60, 66));
        }
        if (nut instanceof ParkingSlotButton) {
            ((ParkingSlotButton) nut).capNhat(viTri);
        }
    }

    private static class TrangCuonPanel extends JPanel implements Scrollable {//cuộn panel dể xem history
        public TrangCuonPanel(LayoutManager layout) {
            super(layout);
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(
                Rectangle r, int orientation, int direction) {
            return 20;
        }

        @Override
        public int getScrollableBlockIncrement(
                Rectangle r, int orientation, int direction) {
            return Math.max(20, r.height - 20);
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }


}
