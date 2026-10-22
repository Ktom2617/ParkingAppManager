package code;

public class Car extends Vehicle{
    public Car(String bienSo){
        super (bienSo);
    }
    @Override
    public String getLoaiXe() {
        return "Ô tô";
    }

    @Override
    public int getGiaBanNgay() {
        return 20000;
    }

    @Override
    public int getGiaBanDem() {
        return 30000;
    }

}
