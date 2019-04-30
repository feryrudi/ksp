package com.bpm.ksp;

public class RowItem {
    private String tanggal;
    private String nominal;
    private String angsuran;

    public RowItem(String tanggal, String nominal, String angsuran) {
        this.tanggal = tanggal;
        this.nominal = nominal;
        this.angsuran = angsuran;
    }
    public String getTanggal() {
        return tanggal;
    }
    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
    public String getNominal() {
        return nominal;
    }
    public void setNominal(String nominal) {
        this.nominal = nominal;
    }
    public String getAngsuran() {
        return angsuran;
    }
    public void setAngsuran(String angsuran) {
        this.angsuran = angsuran;
    }
    @Override
    public String toString() {
        return tanggal + "\n" + nominal;
    }
}
