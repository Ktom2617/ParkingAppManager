package code;

public class Motorbike extends Vehicle{
    public Motorbike(String bienSo){
        super(bienSo);
    }

    @Override
    public String getLoaiXe() {
        return "Xe may";
    }

    @Override
    public int getGiaBanNgay() {
        return 3000;
    }

    @Override
    public int getGiaBanDem() {
        return 8000;
    }

}
