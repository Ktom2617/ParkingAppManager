package code;
import java.time.format.DateTimeFormatter;//làm đẹp cho time
import java.time.LocalDateTime;//lấy tgian
import javax.swing.*;//cho phép sử dụng các class giao diện như JFrame, JPanel, JButton.
import java.awt.*;//cho phép sử dụng những class như Color, Font, BorderLayout, GridLayout.
import com.formdev.flatlaf.FlatDarkLaf;//giao diện
public class Main {

    public static void main(String[] args) {
        FlatDarkLaf.setup();//giao diện
        SwingUtilities.invokeLater(ParkingFrame::createWindow);//chương trình tạo cửa sổ.
    }
}
