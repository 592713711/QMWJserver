package model;

/**
 * Created by zsg on 2016/3/3.
 */
public class MyLocation {
    public double latitude=0;        //γ��
    public double longitude=0;       //����
    public long   time=0;            //ʱ��

    public MyLocation(double mlat,double mlong,long mtime){
        this.latitude=mlat;
        this.longitude=mlong;
        this.time=mtime;
    }
}
