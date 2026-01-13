package com.aa.crewcomp.jobs.abstr;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class Cc2HashedEntity {
    private String hashId;
    private String uniqueHash;

    public abstract String hashIdFunction();

    public abstract void modifyNodeBeforeUniqueHashing(ObjectNode node);

    public String getHashId() {
        return hashId;
    }

    public void setHashId(String hashId) {
        this.hashId = hashId;
    }

    public String getUniqueHash() {
        return uniqueHash;
    }

    public void setUniqueHash(String uniqueHash) {
        this.uniqueHash = uniqueHash;
    }

    private String uniqueHashFunction() throws NoSuchAlgorithmException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.valueToTree(this);
        this.modifyNodeBeforeUniqueHashing(node);
        String jsonString = node.toString();
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(jsonString.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes)
            hexString.append(String.format("%02x", b));
        return hexString.toString();
    }

    public void refreshHashIdAndUniqueHash() {
        this.setHashId(this.hashIdFunction());
        try {
            this.setUniqueHash(this.uniqueHashFunction());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}