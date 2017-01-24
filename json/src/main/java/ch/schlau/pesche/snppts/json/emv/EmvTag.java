package ch.schlau.pesche.snppts.json.emv;

/**
 * EMV Tags
 */
public enum EmvTag implements MappableEnum<String> {

    /**
     * Tag 84 - The EMV Dedicated filename a.k.a the EMV AID as reported by ICC
     * <p>
     * See also {@link #APPLICATION_IDENTIFIER}
     */
    DEDICATED_FILE_NAME("84"),

    /**
     * Tag 9F26 - EMV Application Cryptogram (a.k.a AC / ARPC / ARQC)
     */
    APPLICATION_CRYPTOGRAM("9F26"),

    /**
     * Tag 9F27 - EMC Field Cryptogram information data
     */
    CRYPTOGRAM_INFORMATION_DATA("9F27"),

    /**
     * Tag 9F10 - Issuer Application Data (a.k.a IAD)
     */
    ISSUER_APPLICATION_DATA("9F10"),

    /**
     * CVR (no Tag, part of IAD) - Card Verification Result (a.k.a CVR)
     *
     * Note: the EMV standard has no tag for the CVR; for a Common Core IAD
     * ({@link #ISSUER_AUTHENTICATION_DATA}) with Format Code 'A' the CVR
     * uses the bytes 4..8 of the IAD.
     */
    CARD_VERIFICATION_RESULT("9F10.4"),

    /**
     * Tag 9F37 - EMV Unpredictable number
     */
    UNPREDICTABLE_NUMBER("9F37"),

    /**
     * Tag 9F36 - Application Transaction Counter (a.k.a ATC)
     */
    APPLICATION_TRANSACTION_COUNTER("9F36"),

    /**
     * Tag 95 - Terminal Verification Result (a.k.a TVR)
     */
    TERMINAL_VERIFICATION_RESULT("95"),

    /**
     * Tag 9A - Transaction Date
     */
    TRANSACTION_DATE("9A"),

    /**
     * Tag 9C - Transaction Type (see 15.2.1.1)
     */
    TRANSACTION_TYPE("9C"),

    /**
     * Tag 9F02 - Amount Authorized
     */
    AMOUNT_AUTHORIZED("9F02"),

    /**
     * Tag 5F2A - Transaction Currency Code
     */
    TRANSACTION_CURRENCY_CODE("5F2A"),

    /**
     * Tag 82 - Application Interchange Profile
     */
    APPLICATION_INTERCHANGE_PROFILE("82"),

    /**
     * Tag 9F1A - Terminal Country Code
     */
    TERMINAL_COUNTRY_CODE("9F1A"),

    /**
     * Tag 9F34 - Cardholder Verification Method (CVM) Results
     */
    CARDHOLDER_VERIFICATION_METHOD("9F34"),

    /**
     * Tag 9F33 - Terminal Capabilities
     */
    TERMINAL_CAPABILITIES("9F33"),

    /**
     * Tag 9F35 - Terminal Type (see 15.2.1.2)
     */
    TERMINAL_TYPE("9F35"),

    /**
     * Tag 9F1E - Interface Device (IFD) Serial Number
     */
    INTERFACE_DEVICE_SERIAL_NUMBER("9F1E"),

    /**
     * Tag 9F53 - Transaction Category Code
     */
    TRANSACTION_CATEGORY_CODE("9F53"),

    /**
     * Tag 9F09 - Application Version Number
     */
    APPLICATION_VERSION_NUMBER("9F09"),

    /**
     * Tag 9F41 - Transaction Sequence Counter
     */
    TRANSACTION_SEQUENCE_COUNTER("9F41"),

    /**
     * Tag 9F03 - Amount Other
     */
    AMOUNT_OTHER("9F03"),

    /**
     * Tag 91 - Issuer Authentication Data
     */
    ISSUER_AUTHENTICATION_DATA("91"),

    /**
     * Tag 71 - Issuer Script Template 1
     */
    ISSUER_SCRIPT_TEMPLATE_1("71"),

    /**
     * Tag 72 - Issuer Script Template 2
     */
    ISSUER_SCRIPT_TEMPLATE_2("72"),

    /**
     * Tag 8A - Authorization Response Code
     */
    AUTHORIZATION_RESPONSE_CODE("8A"),

    /**
     * Tag 9F06 - Application Identifier (AID) as reported by the Terminal
     * <p>
     * Often also called TERMINAL_APPLICATION_IDENTIFIER
     * <p>
     * In most cases this value is the same as the Dedicated Filename (84),
     * see EMV Book 1, Application Selection 12.4 Final Selection
     */
    APPLICATION_IDENTIFIER("9F06"),

    /**
     * Tag 57 - Track 2 Equivalent Data
     */
    TRACK_2_EQUIVALENT_DATA("57"),

    /**
     * Tag 5A - Application Primary Account Number (PAN)
     */
    APPLICATION_PRIMARY_ACCOUNT_NUMBER("5A"),

    /**
     * Tag 5F34 - Application PAN Sequence Number
     */
    APPLICATION_PAN_SEQUENCE_NUMBER("5F34"),

    /**
     * Tag 9F5B - Issuer Script Result
     */
    ISSUER_SCRIPT_RESULT("9F5B"),

    /**
     * Tag 9F7C - Customer Exclusive Data (CED)
     * <p>
     * Only Paywave (VISA Contactless)
     */
    CUSTOMER_EXCLUSIVE_DATA("9F7C"),

    /**
     * Tag 9F6E - Form Factor Indicator (FFI)
     * <p>
     * Only Paywave (VISA Contactless)
     */
    FORM_FACTOR_INDICATOR("9F6E"),

    UNKNOWN("");

    public static final EnumMapper<EmvTag, String> MAP = new EnumMapper<>(EmvTag.class, UNKNOWN);

    private String tag;

    private EmvTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String getValue() {
        return tag;
    }

    public static EmvTag fromString(String internalValue) {
        return MAP.get(internalValue);
    }

}
