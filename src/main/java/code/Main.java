package code;
import java.time.format.DateTimeFormatter;//làm đẹp cho time
import java.time.LocalDateTime;//lấy tgian
import javax.swing.*;//cho phép sử dụng các class giao diện như JFrame, JPanel, JButton.
import java.awt.*;//cho phép sử dụng những class như Color, Font, BorderLayout, GridLayout.

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createWindow);//chương trình tạo cửa sổ.
    }

    private static void createWindow() {
        Color MAU_TRONG = new Color(150, 220, 170);//màu của slot khi empty
        Color MAU_CO_XE = new Color(240, 80, 80);//màu của slot khi có xe
        JFrame frame = new JFrame("Smart Parking System - Group 6");//tạo frame cửa số
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("PARKING MANAGER - PARKING MAP", SwingConstants.CENTER);//title
        title.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(title, BorderLayout.NORTH);

        JPanel map = new JPanel(new GridLayout(3, 4, 12, 12));//3 hàng 4 cột(12 nút total) cách nhau 12 pixel
        String[] bienSoXe = new String[12];//mảng lưu biển số xe
        ParkingManager quanLy = new ParkingManager();//quan lý đỗ xe bên class ParkingManagẻr
        for (int i = 1; i <= 12; i++) {//mỗi lần lặp tạo 1 nút mới JButton
            String id = "A" + i;
            ParkingSlot parkingSlot = new ParkingSlot(id);
            if (i == 1) {
                quanLy.checkIn(parkingSlot, new Car("29A-67676"));//du lieu demo thử, hệ thống quản lý sẽ thục hiện việc check-in xe
            }
            JButton slot = new JButton();//thêm 1 button
            slot.setOpaque(true);

            if (parkingSlot.isEmpty()) {
                slot.setText(id + " - TRONG");//nếu slot empty thì cho trống
                slot.setBackground(MAU_TRONG);
            } else {
                //làm đẹp time
                String gioVao = parkingSlot.getThoiGianVao().format(
                        DateTimeFormatter.ofPattern("HH:mm")
                );
                slot.setText("<html><center>"+id + " - " + parkingSlot.getVehicle().getLoaiXe() + " - " + parkingSlot.getVehicle().getBienSo()+ "<br>Gio vao: "+gioVao+ "</center></html>");//nếu có xe thì hiện biển số xe, giờ vào; dùng thêm html để xuống dòng và căn giữa
                slot.setBackground(MAU_CO_XE);
            }

            slot.addActionListener(e -> {//gắn một hành động vào nút để Java biết phải làm gì khi người dùng tương tác

                // Neu o dang empty
                if (parkingSlot.isEmpty()) {

                    String bienSo = JOptionPane.showInputDialog(
                            frame, "Nhap bien so xe cho o " + id
                    );

                    //Cancel
                    if (bienSo == null) {
                        return;
                    }

                    bienSo = bienSo.trim();//xóa khoảng trắng

                    // ko dc nhap bien so xe rong
                    if (bienSo.isEmpty()) {
                        JOptionPane.showMessageDialog(
                                frame, "Bien so xe khong duoc de trong!"
                        );
                        return;
                    }
                    String[] danhSachLoaiXe = {"Xe may", "O to"};

                    String loaiXe = (String) JOptionPane.showInputDialog(//tạo giao diện chọn loại xe
                            frame,
                            "Chon loai xe:",
                            "Loai xe",
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            danhSachLoaiXe,
                            danhSachLoaiXe[0]
                    );

                    if (loaiXe == null) {
                        return;
                    }
                    //tinh da hinh, tao 1 lop xe
                    Vehicle xe;
                    if (loaiXe.equals("O to")) {
                        xe = new Car(bienSo);
                    } else {
                        xe = new Motorbike(bienSo);
                    }
                    quanLy.checkIn(parkingSlot, xe);
                    //làm đẹp time
                    String gioVao = parkingSlot.getThoiGianVao().format(
                            DateTimeFormatter.ofPattern("HH:mm")
                    );
                    System.out.println("Slot " + id + ": " + parkingSlot.getThoiGianVao());
                    //cap nhat giao dien
                    slot.setText("<html><center>"+id + " - " + bienSo + " - "+ loaiXe +"<br>Gio vao: " + gioVao + "</center></html>");//dùng thêm html để xuống dòng và căn giữa
                    slot.setBackground(MAU_CO_XE);
                    System.out.println("Da them xe tren o "+ id);
                } else {
                    //làm đẹp time
                    String gioVaoFull = parkingSlot.getThoiGianVao().format(
                            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
                    );
                    double soGioGui = parkingSlot.getSoGioGui();//tính h gửi
                    long phiGui = parkingSlot.getPhiGui();//lấy phí gửi
                    //neu slot dang co xe, hoi co muon cho ra khong
                    int luaChon = JOptionPane.showConfirmDialog(
                            frame,
                            "Bien so: " + parkingSlot.getVehicle().getBienSo()
                                    +"\nLoai xe: "+parkingSlot.getVehicle().getLoaiXe()
                                    + "\nVi tri do: " + parkingSlot.getId()
                                    + "\nGio vao: " + gioVaoFull
                                    + "\nThoi gian gui: " + String.format("%.2f", soGioGui) + " gio"
                                    + "\nPhi gui xe: "+phiGui+ "VND"
                                    + "\n\nCho xe ra khoi bai?",
                            "Xac nhan cho ra",
                            JOptionPane.YES_NO_OPTION
                    );
                    if (luaChon == JOptionPane.YES_OPTION) {//khi user chọn yes -> tiến hành remove
                        ParkingTicket ve = quanLy.checkOut(parkingSlot);//hệ thống class quản lý thực hiện việc check out
                        slot.setText(id + " - TRONG");
                        slot.setBackground(MAU_TRONG);
                        System.out.println("Da xoa xe tren o "+ id);
                        System.out.println("Da thu: " + ve.getPhiGui() + " VND");
                        System.out.println("Tong luot gui: " + quanLy.getSoLuotGui());
                    }
                }
            });


            map.add(slot);//đưa nút slot vừa tạo vào trong vùng chứa map(12 nút)
        }

        panel.add(map, BorderLayout.CENTER);//đặt bản đồ bãi xe ở giữa
//Minh họa cấu trúc lồng nhau: JFrame → panel → map → JButton.
        frame.setContentPane(panel);
        frame.setVisible(true);
    }
}
