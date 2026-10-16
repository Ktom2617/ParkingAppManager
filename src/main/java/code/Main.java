package code;
import java.time.format.DateTimeFormatter;//làm đẹp cho time
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
        for (int i = 1; i <= 12; i++) {//mỗi lần lặp tạo 1 nút mới JButton
            String id = "A" + i;
            ParkingSlot parkingSlot = new ParkingSlot(id);
            if (i == 1) {
                parkingSlot.parkVehicle("29A-12345");//dữ liệu demo thử
            }
            JButton slot = new JButton();//thêm 1 button
            slot.setOpaque(true);

            if (parkingSlot.isEmpty()) {
                slot.setText(id + " - TRONG");//nếu slot empty thì cho trống
                slot.setBackground(MAU_TRONG);
            } else {
                slot.setText(id + " - " + parkingSlot.getBienSo());//nếu có xe thì hiện biển số xe
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
                                frame, "Bien so khong duoc de trong!"
                        );
                        return;
                    }

                    //luu bien so vao mang
                    parkingSlot.parkVehicle(bienSo);
                    System.out.println("Slot " + id + ": " + parkingSlot.getThoiGianVao());
                    //cap nhat giao dien
                    slot.setText(id + " - " + bienSo);
                    slot.setBackground(MAU_CO_XE);
                    System.out.println("Da them xe tren o "+ id);
                } else {
                    //làm đẹp time
                    String gioVao = parkingSlot.getThoiGianVao().format(
                            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
                    );
                    //neu o dang co xe, hoi co muon cho ra khong
                    int luaChon = JOptionPane.showConfirmDialog(
                            frame,
                            "Bien so: " + parkingSlot.getBienSo()
                                    + "\nVi tri do: " + parkingSlot.getId()
                                    + "\nGio vao: " + gioVao
                                    + "\n\nCho xe ra khoi bai?",
                            "Xac nhan cho ra",
                            JOptionPane.YES_NO_OPTION
                    );
                    if (luaChon == JOptionPane.YES_OPTION) {//khi user chọn yes -> tiến hành remove
                        parkingSlot.removeVehicle();
                        slot.setText(id + " - TRONG");
                        slot.setBackground(MAU_TRONG);
                        System.out.println("Da xoa xe tren o "+ id);

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
