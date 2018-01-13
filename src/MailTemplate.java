import java.io.Serializable;

public class MailTemplate implements Serializable {
    private String mailFrom;
    private String rcptto;
    private String subject;
    private String body;
    private String digitalSignature;
    private byte[] publickey;

    public MailTemplate( String mailFrom, String rcptto, String subject,String body,String digitalSignature){
        this.mailFrom = mailFrom;
        this.rcptto = rcptto;
        this.subject = subject;
        this.body = body;
        this.digitalSignature = digitalSignature;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public String getRcptto() {
        return rcptto;
    }

    public void setRcptto(String rcptto) {
        this.rcptto = rcptto;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDigitalSignature() {
        return digitalSignature;
    }

    public void setDigitalSignature(String digitalSignature) {
        this.digitalSignature = digitalSignature;
    }

    public byte[] getPublickey() {
        return publickey;
    }

    public void setPublickey(byte[] publickey) {
        this.publickey = publickey;
    }
}
