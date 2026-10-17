//xu ly ticket
package code;
import java.time.LocalDateTime;

public class ParkingTicket {
    private Vehicle vehicle;
    private String maViTri;
    private LocalDateTime gioVao;
    private LocalDateTime gioRa;
    private long phiGui;

    public ParkingTicket(Vehicle vehicle, String maViTri, LocalDateTime gioVao){//constructỏ
        this.vehicle=vehicle;
        this.maViTri=maViTri;
        this.gioVao=gioVao;
        this.gioRa=null;
        this.phiGui=0;
    }
    public boolean isActive(){//nếu hợp lệ
        return gioRa==null;
    }
    public void checkout(LocalDateTime gioRa){//cho xe ra
        if(!isActive()){
            throw new IllegalStateException("Ve da duoc checkout");
        }
        if(gioRa==null||gioRa.isBefore(gioVao)){
            throw new IllegalArgumentException("Gio ra khong hop le");
        }
        this.phiGui=vehicle.tinhPhi(gioVao,gioRa);
        this.gioRa=gioRa;
    }
    //các hàm getter
    public Vehicle getVehicle(){
        return vehicle;
    }
    public String getMaViTri(){
        return maViTri;
    }
    public LocalDateTime getGioVao(){
        return gioVao;
    }
    public LocalDateTime getGioRa() {
        return gioRa;
    }
    public long getPhiGui() {
        return phiGui;
    }
}
