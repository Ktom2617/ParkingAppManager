//class quản lý chỗ đỗ xe
package code;
import java.time.LocalDateTime; // thời gian
import java.time.Duration; // tính h

public class ParkingSlot {
    private String id;
    private Vehicle vehicle;
    private LocalDateTime thoiGianVao;//xử lý time
    public LocalDateTime getThoiGianVao(){// hàm gettẻ lấy time
        return thoiGianVao;
    }
    public double getSoGioGui(){
        if (isEmpty()){
            return 0;
        }
        Duration thoiGianGui = Duration.between(thoiGianVao,LocalDateTime.now());//tgian gửi = tgian bây giờ - tgian vào
    return thoiGianGui.toSeconds()/3600.0;//1h = 3600s
    }
    public ParkingSlot(String id) {//constructỏ
        this.id = id;
        this.vehicle = null;
        this.thoiGianVao=null;
    }
//các hàm getter
    public String getId() {//getter lấy id
        return id;
    }
    public Vehicle getVehicle(){
        return vehicle;
    }
    public long getPhiGui(){
        if(isEmpty()){return 0;}
        return vehicle.tinhPhi(thoiGianVao,LocalDateTime.now());
    }
    public boolean isEmpty() {//check empty, nếu có trả về 0
        return vehicle == null;
    }
    public void parkVehicle(Vehicle vehicle){//hàm cho xe vào đỗ
        if (isEmpty()){
            this.vehicle=vehicle;
            this.thoiGianVao=LocalDateTime.now();
        }
    }
    public void removeVehicle(){//bỏ xe khỏi chỗ đỗ
        this.vehicle=null;
        this.thoiGianVao = null;
    }
    public void capNhatXe(Vehicle xeMoi) {//edit xe đang gửi
        if (isEmpty()) {
            throw new IllegalStateException("Vi trí đang trống!");
        }

        if (xeMoi == null) {
            throw new IllegalArgumentException("Xe mới không hợp lệ!");
        }

        this.vehicle = xeMoi;
    }
}
