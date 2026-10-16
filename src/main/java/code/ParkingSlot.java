//class quản lý chỗ đỗ xe
package code;
import java.time.LocalDateTime; // thời gian

public class ParkingSlot {
    private String id;
    private String bienSo;
    private LocalDateTime thoiGianVao;//xử lý time
    public LocalDateTime getThoiGianVao(){// hàm gettẻ lấy time
        return thoiGianVao;
    }
    public ParkingSlot(String id) {//constructỏ
        this.id = id;
        this.bienSo = null;
    }

    public String getId() {//getter lấy id
        return id;
    }
    public String getBienSo(){//getter lấy biển số xe
        return bienSo;
    }

    public boolean isEmpty() {//check empty, nếu có trả về 0
        return bienSo == null;
    }
    public void parkVehicle(String bienSo){//hàm cho xe vào đỗ
        if (isEmpty()){
            this.bienSo=bienSo;
            this.thoiGianVao=LocalDateTime.now();
        }
    }
    public void removeVehicle(){//bỏ xe khỏi chỗ đỗ
        this.bienSo=null;
        this.thoiGianVao = null;
    }

}
