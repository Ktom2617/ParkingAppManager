package code;
import java.time.LocalDate;
import java.time.LocalDateTime;
public abstract class Vehicle {
    private String bienSo;
    public Vehicle(String bienSo){
        this.bienSo=bienSo;
    }
    public String getBienSo(){
        return bienSo;
    }
    public abstract String getLoaiXe();
    public abstract int getGiaBanNgay();
    public abstract int getGiaBanDem();
    public long tinhPhi(LocalDateTime gioVao, LocalDateTime gioRa){//tính phí đỗ xe, xem xe vào/ra ngày hay đêm
        if (gioVao==null||gioRa==null|| gioRa.isBefore(gioVao)){
            throw new IllegalArgumentException("Thoi gian khong hop le");
        }
        LocalDate ngay = gioVao.toLocalDate().minusDays(1);//lùi lại 1 ngày để lấy cả khoảng đêm trc đó
        while(!ngay.isAfter(gioRa.toLocalDate())){//trong khi ngày chưa vượt quá ngày xe ra
            LocalDateTime batDauDem=ngay.atTime(22,0);//tgian đêm
            LocalDateTime ketThucDem=ngay.plusDays(1).atTime(6,0);//tgian kết thúc đêm(sáng hôm sau)
            if(gioVao.isBefore(ketThucDem)&&gioRa.isAfter(batDauDem)){//nếu giờ vào/ra lúc ban đêm thì:
                return getGiaBanDem();//tính phí theo ban đêm
            }
            ngay=ngay.plusDays(1);//tăng thêm ngày tiếp theo để tính tiếp
        }
        return getGiaBanNgay();//trả về giá ban ngày nếu ko thấy giờ ban đêm
    }

}
