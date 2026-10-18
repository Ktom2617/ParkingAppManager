package code;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.time.LocalDate;
public class ParkingManager {
    private ArrayList<ParkingTicket>danhSachVe=new ArrayList<>();
    public ParkingTicket checkIn(ParkingSlot slot,Vehicle xe){
        if(!slot.isEmpty()){
            throw new IllegalStateException("Vi tri da co xe!");
        }
        if (xe==null){
            throw new IllegalArgumentException("Xe khong hop le!");
        }
        slot.parkVehicle(xe);
        ParkingTicket ve = new ParkingTicket(xe, slot.getId(), slot.getThoiGianVao());
        danhSachVe.add(ve);
        return ve;
    }
    public ParkingTicket checkOut(ParkingSlot slot){
        if(slot.isEmpty()){
            throw new IllegalStateException("Vi tri dang trong!");
        }
        for(ParkingTicket ve:danhSachVe){
            if(ve.isActive()&&ve.getMaViTri().equals(slot.getId())){
                ve.checkout(LocalDateTime.now());
                slot.removeVehicle();
                return ve;
            }
        }
        throw new IllegalStateException("Khong tim thay ve dang gui");
    }
    public int getSoLuotGui() {
        return danhSachVe.size();
    }
    public int getSoXeDangGui(){
        int dem=0;
        for(ParkingTicket ve : danhSachVe){
            if (ve.isActive()){
                dem++;
            }
        }
        return dem;
    }
    public long getDoanhThuHomNay() {
        long tongTien = 0;
        LocalDate homNay = LocalDate.now();
        for (ParkingTicket ve : danhSachVe) {
            if (!ve.isActive() && ve.getGioRa().toLocalDate().equals(homNay)) {//kiểm tra xe được trả trong hôm nay.
                tongTien += ve.getPhiGui();
            }
        }
        return tongTien;
    }
}
