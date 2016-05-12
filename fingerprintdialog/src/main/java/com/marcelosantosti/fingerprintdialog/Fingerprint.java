package com.marcelosantosti.fingerprintdialog;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.SecureRandom;

/**
 * Created by mlsantos on 12/05/2016.
 */
public class Fingerprint {

    private long id;

    public Fingerprint() {

        this.id = new SecureRandom().nextLong();
    }

    public byte[] toByteArray() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = null;
        try {
            dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            dataOutputStream.writeLong(id);
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
