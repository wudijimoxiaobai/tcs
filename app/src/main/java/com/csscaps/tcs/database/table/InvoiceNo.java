package com.csscaps.tcs.database.table;

import com.csscaps.tcs.database.TcsDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by tl on 2018/5/22.
 */
@Table(database = TcsDatabase.class)
public class InvoiceNo extends BaseModel {

    @PrimaryKey(autoincrement = true)
    @Column
    int id;
    @Column
    String invoice_type_code;
    @Column
    String segment_cipher;
    @Column
    String invoice_num;

    public String getInvoice_type_code() {
        return invoice_type_code;
    }

    public void setInvoice_type_code(String invoice_type_code) {
        this.invoice_type_code = invoice_type_code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSegment_cipher() {
        return segment_cipher;
    }

    public void setSegment_cipher(String segment_cipher) {
        this.segment_cipher = segment_cipher;
    }

    public String getInvoice_num() {
        return invoice_num;
    }

    public void setInvoice_num(String invoice_num) {
        this.invoice_num = invoice_num;
    }
}
