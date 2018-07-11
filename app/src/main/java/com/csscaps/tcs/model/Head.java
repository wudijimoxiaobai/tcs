package com.csscaps.tcs.model;

public class Head {

    private String INVOICECODE;
    private String DEVICENO;
    private String OBJECT;
    private String INVOICENUMBER;
    private String ISSUANCEDATE;

    public Head() {
    }

    ;

    public Head(String iNVOICECODE, String dEVICENO, String oBJECT,
                String iNVOICENUMBER, String iSSUANCEDATE) {
        super();
        INVOICECODE = iNVOICECODE;
        DEVICENO = dEVICENO;
        OBJECT = oBJECT;
        INVOICENUMBER = iNVOICENUMBER;
        ISSUANCEDATE = iSSUANCEDATE;
    }

    public String getINVOICECODE() {
        return INVOICECODE;
    }

    public void setINVOICECODE(String iNVOICECODE) {
        INVOICECODE = iNVOICECODE;
    }

    public String getDEVICENO() {
        return DEVICENO;
    }

    public void setDEVICENO(String dEVICENO) {
        DEVICENO = dEVICENO;
    }

    public String getOBJECT() {
        return OBJECT;
    }

    public void setOBJECT(String oBJECT) {
        OBJECT = oBJECT;
    }

    public String getINVOICENUMBER() {
        return INVOICENUMBER;
    }

    public void setINVOICENUMBER(String iNVOICENUMBER) {
        INVOICENUMBER = iNVOICENUMBER;
    }

    public String getISSUANCEDATE() {
        return ISSUANCEDATE;
    }

    public void setISSUANCEDATE(String iSSUANCEDATE) {
        ISSUANCEDATE = iSSUANCEDATE;
    }

}
