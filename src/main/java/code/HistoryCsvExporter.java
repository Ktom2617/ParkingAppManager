package code;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
//XUẤT EXCEL
public class HistoryCsvExporter {

    public static void xuat(List<ParkingTicket> danhSachVe, File file)
            throws IOException {

        DateTimeFormatter df =
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        try (BufferedWriter writer = Files.newBufferedWriter(
                file.toPath(), StandardCharsets.UTF_8)) {

            // Giúp Excel nhận diện chữ tiếng Việt
            writer.write('\uFEFF');

            ghiDong(writer, "Mã vé", "Giờ vào", "Giờ ra",
                    "Biển số", "Loại xe", "Vị trí",
                    "Thời gian gửi (phút)", "Phí (VND)", "Trạng thái");

            for (int i = danhSachVe.size() - 1; i >= 0; i--) {
                ParkingTicket ve = danhSachVe.get(i);
                Vehicle xe = ve.getVehicle();

                LocalDateTime ketThuc = ve.isActive()
                        ? LocalDateTime.now() : ve.getGioRa();

                long soPhut = Duration.between(
                        ve.getGioVao(), ketThuc).toMinutes();

                long phi = ve.isActive()
                        ? xe.tinhPhi(ve.getGioVao(), ketThuc)
                        : ve.getPhiGui();

                ghiDong(writer,
                        String.format("%04d", i + 1),
                        ve.getGioVao().format(df),
                        ve.isActive() ? "" : ve.getGioRa().format(df),
                        xe.getBienSo(),
                        xe.getLoaiXe(),
                        ve.getMaViTri(),
                        String.valueOf(soPhut),
                        String.valueOf(phi),
                        ve.isActive() ? "Đang gửi (tạm tính)" : "Đã trả");
            }
        }
    }

    private static void ghiDong(BufferedWriter writer, String... cot)
            throws IOException {

        for (int i = 0; i < cot.length; i++) {
            if (i > 0) writer.write(";");

            writer.write("\"");
            writer.write(cot[i].replace("\"", "\"\""));
            writer.write("\"");
        }
        writer.newLine();
    }
}
