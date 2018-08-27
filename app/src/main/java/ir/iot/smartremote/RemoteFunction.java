package ir.iot.smartremote;

/**
 * Created by Dreamz on 25-08-2018.
 */

public class RemoteFunction {
    String title;
    String code;
    int numberOfBits;
    String protocol;

    public RemoteFunction(String title, String code, int numberOfBits, String protocol) {
        this.title = title;
        this.code = code;
        this.numberOfBits = numberOfBits;
        this.protocol = protocol;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getNumberOfBits() {
        return numberOfBits;
    }

    public void setNumberOfBits(int numberOfBits) {
        this.numberOfBits = numberOfBits;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}
