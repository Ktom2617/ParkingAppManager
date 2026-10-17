package code;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
}
