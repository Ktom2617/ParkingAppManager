package code;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.time.LocalDate;
public class ParkingManager {
    private ArrayList<ParkingTicket>danhSachVe=new ArrayList<>();
    public ParkingTicket checkIn(ParkingSlot slot,Vehicle xe){
        if(!slot.isEmpty()){
            throw new IllegalStateException("Vị trí đã có xe!");
        }
        if (xe==null){
            throw new IllegalArgumentException("Xe không hợp lệ!");
        }
        slot.parkVehicle(xe);
        ParkingTicket ve = new ParkingTicket(xe, slot.getId(), slot.getThoiGianVao());
        danhSachVe.add(ve);
        return ve;
    }
    public ParkingTicket checkOut(ParkingSlot slot){
        if(slot.isEmpty()){
            throw new IllegalStateException("Vị trí đang trống!");
        }
        for(ParkingTicket ve:danhSachVe){
            if(ve.isActive()&&ve.getMaViTri().equals(slot.getId())){
                ve.checkout(LocalDateTime.now());
                slot.removeVehicle();
                return ve;
            }
        }
        throw new IllegalStateException("Không tìm thấy vé đang gửi!");
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
    public ArrayList<ParkingTicket> getDanhSachVe() {
        return new ArrayList<>(danhSachVe);
    }

    public void suaThongTinXe(ParkingSlot slot, Vehicle xeMoi) {//edit xe đang gửi
        // 1. Kiem tra du lieu
        if (slot == null || slot.isEmpty()) {
            throw new IllegalStateException("Vị trí không có xe!");
        }
        if (xeMoi == null || xeMoi.getBienSo() == null
                || xeMoi.getBienSo().trim().isEmpty()) {
            throw new IllegalArgumentException("Biển số không hợp lệ!");
        }
        // 2. Tim ve dang gui cua vi tri
        ParkingTicket veCanSua = null;
        for (ParkingTicket ve : danhSachVe) {
            if (ve.isActive() && ve.getMaViTri().equals(slot.getId())) {
                veCanSua = ve;
                break;
            }
        }
        if (veCanSua == null) {
            throw new IllegalStateException(
                    "Không tìm thấy vé đang gửi!"
            );
        }
        // 3. Kiem tra bien so co trung voi xe khac khong
        String bienMoi = chuanHoaBienSo(xeMoi.getBienSo());
        for (ParkingTicket ve : danhSachVe) {
            if (ve.isActive() && ve != veCanSua) {
                String bienCu = chuanHoaBienSo(ve.getVehicle().getBienSo());
                if (bienMoi.equals(bienCu)) {
                    throw new IllegalArgumentException(
                            "Biển số đã tồn tại trong bãi!"
                    );
                }
            }
        }
        // 4. Cap nhat dong bo
        slot.capNhatXe(xeMoi);
        veCanSua.capNhatXe(xeMoi);
    }
    private String chuanHoaBienSo(String bienSo) {
        return bienSo.toUpperCase(java.util.Locale.ROOT)
                .replaceAll("[\\s.-]", "");
    }

}
