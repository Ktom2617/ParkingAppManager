
package code;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class ParkingFrame {
    private static JButton nutDangChon = null;//nhớ nút hiện đang được chọn.
    private static javax.swing.border.Border vienGoc = null;//nhớ viền trắng ban đầu của nút đó để có thể khôi phục
    private static ParkingSlot viTriDangChon = null;//nhớ parking slot nào đang đc chon
    public static void createWindow() {
        Color MAU_TRONG = new Color(33, 40, 45);//màu của slot khi empty
        Color MAU_CO_XE = new Color(72, 68, 45);//màu của slot khi có xe
        JFrame frame = new JFrame("Smart Parking System - Group 6");//tạo frame cửa số
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("PARKING MANAGER - PARKING MAP", SwingConstants.CENTER);//title
        title.setFont(new Font("Arial", Font.BOLD, 24));


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
        JLabel soDangCo = new JLabel("0");//phục vụ cho hiển thị số liệu thống kê ở giao diện chính
        JLabel soConTrong = new JLabel("12");//phục vụ cho hiển thị số liệu thống kê ở giao diện chính
        JLabel soDoanhThu = new JLabel("0đ");//phuc vu hien thi doanh thu
        JLabel thongTinViTri = new JLabel("Chon mot o de xem chi tiet");//phuc vu hien thi doanh thu
        thongTinViTri.setForeground(Color.WHITE);
        thongTinViTri.setFont(new Font("SansSerif", Font.PLAIN, 15));
        JButton nutTraXe = new JButton("TRẢ XE");//nút trả xe
        JButton nutNhanXe = new JButton("NHẬN XE");//nút nhận xe
//tạo ô nhâp liệu
        JTextField nhapBienSo = new JTextField();
        JComboBox<String> chonLoaiXe = new JComboBox<>(
                new String[]{"Xe may", "O to"}
        );
        JPanel formNhanXe = new JPanel(new GridLayout(4, 1, 0, 8));
        formNhanXe.setOpaque(false);
        JLabel nhanBienSo = new JLabel("BIỂN SỐ");
        JLabel nhanLoaiXe = new JLabel("LOẠI XE");
        formNhanXe.add(nhanBienSo);
        formNhanXe.add(nhapBienSo);
        formNhanXe.add(nhanLoaiXe);
        formNhanXe.add(chonLoaiXe);
//làm đẹp
        Color mauChuPhu = new Color(152, 162, 168);
        Color mauNenInput = new Color(34, 39, 43);

        nhanBienSo.setForeground(mauChuPhu);
        nhanLoaiXe.setForeground(mauChuPhu);

        nhapBienSo.setBackground(mauNenInput);
        nhapBienSo.setForeground(Color.WHITE);
        nhapBienSo.setCaretColor(Color.WHITE);
        nhapBienSo.setFont(new Font("SansSerif", Font.BOLD, 16));

        chonLoaiXe.setBackground(mauNenInput);
        chonLoaiXe.setForeground(Color.WHITE);
        chonLoaiXe.setFont(new Font("SansSerif", Font.PLAIN, 15));

        nhapBienSo.putClientProperty(
                "JTextField.placeholderText", "VD: 29A-12345");

        nhapBienSo.putClientProperty("FlatLaf.style", "arc: 12");
        chonLoaiXe.putClientProperty("FlatLaf.style", "arc: 12");

        formNhanXe.setVisible(false);
//lặp tạo button
        for (int i = 1; i <= 12; i++) {//mỗi lần lặp tạo 1 nút mới JButton
            String id = "A" + i;
            ParkingSlot parkingSlot = new ParkingSlot(id);
            if (i == 1) {
                quanLy.checkIn(parkingSlot, new Car("29A-67676"));//du lieu demo thử, hệ thống quản lý sẽ thục hiện việc check-in xe
            }
            JButton slot = new JButton();//thêm 1 button
            slot.setFocusPainted(false);
            slot.setFont(new Font("SansSerif", Font.BOLD, 16));
            slot.setForeground(Color.WHITE);
            slot.setForeground(new Color(25, 35, 45));
            slot.setOpaque(true);

            if (parkingSlot.isEmpty()) {
                slot.setText("<html><font color='white'>"+id + " - TRONG");//nếu slot empty thì cho trống;
                slot.setBackground(MAU_TRONG);
            } else {
                //làm đẹp time
                String gioVao = parkingSlot.getThoiGianVao().format(
                        DateTimeFormatter.ofPattern("HH:mm")
                );
                slot.setText("<html><center><font color='white'>"+id + " - " + parkingSlot.getVehicle().getLoaiXe() + " - " + parkingSlot.getVehicle().getBienSo()+ "<br>Gio vao: "+gioVao+ "</center></html>");//nếu có xe thì hiện biển số xe, giờ vào; dùng thêm html để xuống dòng và căn giữa
                slot.setBackground(MAU_CO_XE);
            }

            slot.addActionListener(e -> {//gắn một hành động vào nút để Java biết phải làm gì khi người dùng tương tác
               //Hệ thống xử lý chọn các ô
                //Nếu trước đó đã chọn ô khác → trả ô cũ về viền trắng gốc
                if (nutDangChon != null) {
                    nutDangChon.setBorder(vienGoc);
                }
                //Lưu viền trắng gốc của ô mới và thêm viền vàng vào bên trong
                vienGoc = slot.getBorder();

                slot.setBorder(BorderFactory.createCompoundBorder(
                        vienGoc,
                        BorderFactory.createLineBorder(new Color(242, 183, 5), 3)
                ));
                hienThiChiTiet(parkingSlot, thongTinViTri);//hien thi vi tri
                nutDangChon = slot;  //Ghi nhớ ô mới là nutDangChon
                viTriDangChon = parkingSlot;//Ghi nhớ vị chon
                nutTraXe.setEnabled(!parkingSlot.isEmpty());//chọn ô có xe thì nút trả xe được bật. Chọn ô trống thì nút bị khóa
                nutNhanXe.setEnabled(parkingSlot.isEmpty());//choọn ô trống thì bật nút này lên
                formNhanXe.setVisible(parkingSlot.isEmpty());//chỉ hiện form khi ô trống
            });


            int soO = Integer.parseInt(id.substring(1));//lấy phần sau chữ A để so sánh số

            if (soO <= 4) {
                cot1.add(slot);//A1–A4 → cot1
            } else if (soO <= 8) {
                cot2.add(slot);//A5–A8 → cot2
            } else {
                cot3.add(slot);//- A9–A12 → cot3
            }
            int vienDuoi = (soO % 4 == 0) ? 3 : 0;//vienDuoi chỉ bằng 3 tại A4, A8, A12 để đóng cạnh dưới của mỗi dãy

            slot.setBorder(BorderFactory.createMatteBorder(//tao duong vien
                    3, 3, vienDuoi, 3,//tren duoi trai phai
                    new Color(235, 235, 225)
            ));
            int viTriTrongCot = (soO - 1) % 4;

            int tren = 3;
            int trai = 3;
            int phai = 3;
            int duoi = (viTriTrongCot == 3) ? 3 : 0;


        }

        JPanel header = new JPanel(new BorderLayout());//tạo giao diện - tạo tiêu đề headẻr
        header.setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 18));
        header.putClientProperty("FlatLaf.style",
                "arc: 18; background: #292E33");

        JLabel logo = new JLabel("P   ParkingManager");
        logo.setFont(new Font("SansSerif", Font.BOLD, 22));
        logo.setForeground(new Color(245, 183, 0));

        header.add(logo, BorderLayout.WEST);

        JPanel stats = new JPanel(new GridLayout(1, 4, 12, 0));//tạo giao dien thẻ thống kê/thông tin
        stats.add(createStatCard("SUC CHUA", new JLabel("12")));
        stats.add(createStatCard("DANG CO XE", soDangCo));
        stats.add(createStatCard("CON TRONG", soConTrong));
        stats.add(createStatCard("DOANH THU", soDoanhThu));
        capNhatThongKe(quanLy, soDangCo, soConTrong, soDoanhThu);


        JPanel top = new JPanel(new BorderLayout(0, 12));
        top.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        top.add(header, BorderLayout.NORTH);
        top.add(stats, BorderLayout.CENTER);

        panel.add(top, BorderLayout.NORTH);

        JPanel details = new JPanel(new BorderLayout());//bảng thông tin
        details.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        details.putClientProperty("FlatLaf.style",
                "arc: 18; background: #1A1E21");

        //panel
        details.add(thongTinViTri, BorderLayout.NORTH);
        JPanel vungNhapXe = new JPanel(new BorderLayout());
        vungNhapXe.setOpaque(false);
        vungNhapXe.add(formNhanXe, BorderLayout.NORTH);
        details.add(vungNhapXe, BorderLayout.CENTER);

        nutTraXe.setBackground(new Color(242, 183, 5));
        nutTraXe.setForeground(new Color(28, 22, 0));
        nutTraXe.setFont(new Font("SansSerif", Font.BOLD, 16));
        nutNhanXe.setBackground(new Color(31, 127, 70));
        nutNhanXe.setForeground(new Color(214, 206, 206));
        nutNhanXe.setFont(new Font("SansSerif", Font.BOLD, 16));

        nutTraXe.putClientProperty("JButton.buttonType", "roundRect");
        nutTraXe.setEnabled(false);

        JPanel actions = new JPanel(new GridLayout(2, 1, 0, 10));//taạo khung 2 nút
        actions.setOpaque(false);

        nutNhanXe.setEnabled(false); // Chưa gắn chức năng
        nutNhanXe.putClientProperty("JButton.buttonType", "roundRect");

        actions.add(nutNhanXe);
        actions.add(nutTraXe);

        details.add(actions, BorderLayout.SOUTH);

        nutNhanXe.addActionListener(e -> {//hành động xử lý nút nhận xe

            if (viTriDangChon == null || !viTriDangChon.isEmpty()) {
                return;
            }

            String bienSo = nhapBienSo.getText().trim();//oo nhâp biển số

            if (bienSo.isEmpty()) {
                JOptionPane.showMessageDialog(
                        frame, "Bien so khong duoc de trong!"
                );
                return;
            }

            String loaiXe = (String) chonLoaiXe.getSelectedItem();


            // Tạo đối tượng xe
            Vehicle xe;

            if (loaiXe.equals("O to")) {
                xe = new Car(bienSo);
            } else {
                xe = new Motorbike(bienSo);
            }

            // Check-in-lấy vị trí người dùng đã chọn và gửi xe vào đó
            ParkingSlot viTri = viTriDangChon;
            quanLy.checkIn(viTri, xe);

            // Cập nhật giao diện
            String gioVao = viTri.getThoiGianVao()
                    .format(DateTimeFormatter.ofPattern("HH:mm"));

            nutDangChon.setText("<html><center><font color='white'>"
                    + viTri.getId() + " - " + xe.getBienSo()
                    + "<br>" + xe.getLoaiXe()
                    + "<br>Gio vao: " + gioVao
                    + "</font></center></html>");

            nutDangChon.setBackground(MAU_CO_XE);

            hienThiChiTiet(viTri, thongTinViTri);
            capNhatThongKe(quanLy, soDangCo, soConTrong, soDoanhThu);

            nutNhanXe.setEnabled(false);
            nutTraXe.setEnabled(true);
            nhapBienSo.setText("");
            formNhanXe.setVisible(false);//sau khi nhận xe, ô nhập sẽ được xóa và form tự ẩn
        });


        nutTraXe.addActionListener(e -> {//hành động xử lý nút trả xe

            if (viTriDangChon == null || viTriDangChon.isEmpty()) {
                return;
            }

            Vehicle xe = viTriDangChon.getVehicle();

            String thongBao = "Bien so: " + xe.getBienSo()
                    + "\nLoai xe: " + xe.getLoaiXe()
                    + "\nVi tri: " + viTriDangChon.getId()
                    + "\nPhi gui: " + viTriDangChon.getPhiGui() + "đ"
                    + "\n\nXac nhan tra xe?";

            int luaChon = JOptionPane.showConfirmDialog(//tạo lua chọn confỉrm
                    frame, thongBao, "Xac nhan tra xe",
                    JOptionPane.YES_NO_OPTION
            );

            if (luaChon != JOptionPane.YES_OPTION) {
                return;
            }

            ParkingTicket ve = quanLy.checkOut(viTriDangChon);

            nutDangChon.setText("<html><center><font color='white'>"
                    + viTriDangChon.getId()
                    + " - TRONG</font></center></html>");

            nutDangChon.setBackground(MAU_TRONG);
            hienThiChiTiet(viTriDangChon, thongTinViTri);
            capNhatThongKe(quanLy, soDangCo, soConTrong, soDoanhThu);

            nutTraXe.setEnabled(false);
            nutNhanXe.setEnabled(true);
            formNhanXe.setVisible(true);
            System.out.println("Da thu: " + ve.getPhiGui() + " VND");
        });


        JSplitPane split = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT, map, details//tạo một vùng chia đôi, với map ở bên trái và details ở bên phải
        );
        split.setResizeWeight(0.67);
        split.setDividerSize(10);
        split.setBorder(null);

        panel.add(split, BorderLayout.CENTER);//đưa vùng vừa chia vào chính giưa cửa sổ

        frame.setContentPane(panel);
        frame.setVisible(true);
        split.setDividerLocation(0.68);
    }

    private static JPanel createStatCard(String ten, JLabel number) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBorder(BorderFactory.createEmptyBorder(15, 18, 15, 18));
        card.putClientProperty("FlatLaf.style",
                "arc: 18; background: #292E33");

        JLabel title = new JLabel(ten);
        title.setForeground(new Color(170, 175, 185));

        number.setFont(new Font("SansSerif", Font.BOLD, 26));

        card.add(title, BorderLayout.NORTH);
        card.add(number, BorderLayout.CENTER);

        return card;
    }
    private static void capNhatThongKe(ParkingManager quanLy, JLabel soDangCo, JLabel soConTrong, JLabel soDoanhThu){
        int soXe=quanLy.getSoXeDangGui();//cập nhật số xe cho các ô tổng hợp trên giao diện
        soDangCo.setText(""+soXe);//chuyển int sang string, lay soXe
        soConTrong.setText(""+(12-soXe));//chuyển int sang string, lay cho trong
        long doanhThu = quanLy.getDoanhThuHomNay();
        soDoanhThu.setText(doanhThu + "đ");//lay doanh thu
    }
    private static void hienThiChiTiet(ParkingSlot viTri, JLabel thongTin) {//ham hiển thị thong tin

        if (viTri.isEmpty()) {
            thongTin.setText("<html><h2>" + viTri.getId()
                    + "</h2>Trang thai: TRONG</html>");
        } else {
            Vehicle xe = viTri.getVehicle();

            String noiDung = "<html>"
                    + "<font color='#98A2A8'>VỊ TRÍ</font><br>"
                    + "<font size='7' color='#E7EAEB'><b>"
                    + viTri.getId() + "</b></font><br><br>"

                    + "<font size='5' color='#E7EAEB'><b>"
                    + xe.getBienSo() + "</b></font><br><br>"

                    + "<font color='#98A2A8'>Loại xe:</font> "
                    + xe.getLoaiXe() + "<br><br>"

                    + "<font color='#98A2A8'>Giờ vào:</font> "
                    + viTri.getThoiGianVao()
                    .format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"))
                    + "<br><br><hr>"

                    + "<font color='#98A2A8'>PHÍ HIỆN TẠI</font><br>"
                    + "<font size='6' color='#F2B705'><b>"
                    + viTri.getPhiGui() + "đ</b></font>"
                    + "</html>";

            thongTin.setText(noiDung);
        }
    }



}
