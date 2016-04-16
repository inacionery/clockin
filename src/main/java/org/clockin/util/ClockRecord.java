/**
 *
 */
package org.clockin.util;

import java.time.LocalDateTime;

/**
 * @author Miguel Angelo Caldas Gallindo
 *
 */
public class ClockRecord {

    public static final String RECORD_FORMAT = "(\\d{9})(3)(\\d{8})(\\d{4})(\\d{12})";
    private String originalRecord;
    private String nsr;
    private String type;
    private LocalDateTime dateTime;
    private String pis;

    public ClockRecord(String line) {
        this.originalRecord = line;

    }

    public String getOriginalRecord() {

        return originalRecord;
    }

    /**
     * @return the nsr
     */
    public String getNsr() {

        return nsr;
    }

    /**
     * @param nsr the nsr to set
     */
    public void setNsr(String nsr) {

        this.nsr = nsr;
    }

    /**
     * @return the type
     */
    public String getType() {

        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {

        this.type = type;
    }

    /**
     * @return the dateTime
     */
    public LocalDateTime getDateTime() {

        return dateTime;
    }

    /**
     * @param dateTime the dateTime to set
     */
    public void setDateTime(LocalDateTime dateTime) {

        this.dateTime = dateTime;
    }

    /**
     * @return the pis
     */
    public String getPis() {

        return pis;
    }

    /**
     * @param pis the pis to set
     */
    public void setPis(String pis) {

        this.pis = pis;
    }

    /**
     * @param originalRecord the originalRecord to set
     */
    public void setOriginalRecord(String originalRecord) {

        this.originalRecord = originalRecord;
    }
}
