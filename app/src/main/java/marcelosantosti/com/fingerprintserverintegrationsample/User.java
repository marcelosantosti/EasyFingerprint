package marcelosantosti.com.fingerprintserverintegrationsample;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by mlsantos on 10/05/2016.
 */
public class User {

    private String username;
    private long nonce;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getNonce() {
        return nonce;
    }

    public void setNonce(long nonce) {
        this.nonce = nonce;
    }

    public byte[] toByteArray() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = null;
        try {
            dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            dataOutputStream.writeUTF(username);
            dataOutputStream.writeLong(nonce);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
            } catch (IOException ignore) {
            }
            try {
                byteArrayOutputStream.close();
            } catch (IOException ignore) {
            }
        }
    }
}
