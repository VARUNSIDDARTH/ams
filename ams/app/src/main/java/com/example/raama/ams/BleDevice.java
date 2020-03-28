package com.example.raama.ams;

public class BleDevice
{
    private int major_id;
    private int minor_id;
    private String uuid;


    BleDevice(String uuid,int major,int minor)
    {
        this.uuid=uuid;
        this.major_id=major;
        this.minor_id=minor;
    }

    public int getMajor_id() {
        return major_id;
    }

    public void setMajor_id(int major_id) {
        this.major_id = major_id;
    }

    public int getMinor_id() {
        return minor_id;
    }

    public void setMinor_id(int minor_id) {
        this.minor_id = minor_id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public int hashCode() {
        System.out.println("In hashcode");
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        BleDevice e = null;
        if(obj instanceof BleDevice){
            e = (BleDevice) obj;
        }
        System.out.println("In equals");
        if(this.getUuid().contentEquals(e.getUuid())){
            return true;
        } else {
            return false;
        }
    }


}
