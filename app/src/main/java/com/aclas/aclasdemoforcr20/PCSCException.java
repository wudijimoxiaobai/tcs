package com.aclas.aclasdemoforcr20;

public final class PCSCException extends Exception {
	private static final long serialVersionUID = 4181137171979130432L;
	
	/**
     * The last PCSC exception integer code which occurred.
     */
    final int code;
    
    /**
     * Constructs a PCSCException object.
     * @param code the PCSC exception integer code.
     */
    PCSCException(int code) {
    	 super(PCSCErrorValues.toErrorString(code));
         this.code = code;
    }
}