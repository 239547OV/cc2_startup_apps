package com.aa.crewcomp.jobs.abstr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.aa.crewcomp.jobs.mock.MyHashedEntity;
import com.aa.crewcomp.jobs.mock.SubObject;

public class Cc2HashedEntityTests {
    @Test
    public void testUniqueHashEquals() {
        MyHashedEntity myEntity = new MyHashedEntity();
        myEntity.setFieldA(100);
        myEntity.setFieldB("test");
        SubObject subObject = new SubObject();
        subObject.setSubField("other");
        subObject.setExcludeSubField("excluded");
        myEntity.setSubObject(subObject);

        MyHashedEntity myEntity2 = new MyHashedEntity();
        myEntity2.setFieldA(100);
        myEntity2.setFieldB("test");
        SubObject subObject2 = new SubObject();
        subObject2.setSubField("other");
        subObject.setExcludeSubField("not excluded");
        myEntity2.setSubObject(subObject2);
        myEntity.refreshHashIdAndUniqueHash();
        myEntity2.refreshHashIdAndUniqueHash();
        assertEquals(myEntity.getHashId(), myEntity2.getHashId());
        assertEquals(myEntity.getUniqueHash(), myEntity2.getUniqueHash());
    }

    @Test
    public void testUniqueHashNotEquals() {
        MyHashedEntity myEntity = new MyHashedEntity();
        myEntity.setFieldA(100);
        myEntity.setFieldB("test");
        SubObject subObject = new SubObject();
        subObject.setSubField("other");
        subObject.setExcludeSubField("excluded");
        myEntity.setSubObject(subObject);

        MyHashedEntity myEntity2 = new MyHashedEntity();
        myEntity2.setFieldA(100);
        myEntity2.setFieldB("test");
        SubObject subObject2 = new SubObject();
        subObject2.setSubField("other difference");
        subObject.setExcludeSubField("not excluded");
        myEntity2.setSubObject(subObject2);
        myEntity.refreshHashIdAndUniqueHash();
        myEntity2.refreshHashIdAndUniqueHash();
        assertEquals(myEntity.getHashId(), myEntity2.getHashId());
        assertNotEquals(myEntity.getUniqueHash(), myEntity2.getUniqueHash());
    }

    @Test
    public void testHashIdNotEquals() {
        MyHashedEntity myEntity = new MyHashedEntity();
        myEntity.setFieldA(100);
        myEntity.setFieldB("test");
        SubObject subObject = new SubObject();
        subObject.setSubField("other");
        subObject.setExcludeSubField("excluded");
        myEntity.setSubObject(subObject);

        MyHashedEntity myEntity2 = new MyHashedEntity();
        myEntity2.setFieldA(100);
        myEntity2.setFieldB("test2");
        SubObject subObject2 = new SubObject();
        subObject2.setSubField("other difference");
        subObject.setExcludeSubField("not excluded");
        myEntity2.setSubObject(subObject2);
        myEntity.refreshHashIdAndUniqueHash();
        myEntity2.refreshHashIdAndUniqueHash();
        assertNotEquals(myEntity.getHashId(), myEntity2.getHashId());
        assertNotEquals(myEntity.getUniqueHash(), myEntity2.getUniqueHash());
    }

    @Test
    public void testHashIdException() {
        try (MockedStatic<MessageDigest> mockedStatic = mockStatic(MessageDigest.class)) {
            mockedStatic.when(() -> MessageDigest.getInstance(anyString()))
                    .thenThrow(new NoSuchAlgorithmException("Algorithm not found"));
            MyHashedEntity myEntity = new MyHashedEntity();
            myEntity.setFieldA(100);
            myEntity.setFieldB("test");
            SubObject subObject = new SubObject();
            subObject.setSubField("other");
            subObject.setExcludeSubField("excluded");
            myEntity.setSubObject(subObject);
            assertThrows(NoSuchAlgorithmException.class, () -> {
                MessageDigest.getInstance("SHA-256");
            });
            myEntity.refreshHashIdAndUniqueHash();
        }
    }
}
