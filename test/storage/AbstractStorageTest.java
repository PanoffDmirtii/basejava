package storage;


import exceptions.ExistUuidException;
import exceptions.NotExistUuidException;
import model.Resume;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class AbstractStorageTest {
    protected Storage storage;

    public AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    private static final String UUID_1 = "uuid_1";
    private static final String UUID_2 = "uuid_2";
    private static final String UUID_3 = "uuid_3";
    private static final String UUID_4 = "uuid_4";
    private static final Resume RESUME_1 = new Resume(UUID_1, "DmitriiPanov");
    private static final Resume RESUME_2 = new Resume(UUID_2, "NikolayAprelev");
    private static final Resume RESUME_3 = new Resume(UUID_3, "DmitriiPanov");
    protected static final Resume RESUME_4 = new Resume(UUID_4,"EvgeniiPupkin");

    @Before
    public void setUp() {
        storage.clear();
        storage.save(RESUME_1);
        storage.save(RESUME_3);
        storage.save(RESUME_2);
    }

    @Test
    public void saveNewResume() {
        storage.save(RESUME_4);
        Assert.assertEquals(4, storage.size());
    }

    @Test(expected = ExistUuidException.class)
    public void saveExistResume() {
        storage.save(RESUME_1);
    }

    @Test(expected = NotExistUuidException.class)
    public void deleteNotExistResume() {
        storage.delete(UUID_4);
    }

    @Test
    public void deleteExistResume() {
        storage.delete(UUID_2);
        Assert.assertEquals(2, storage.size());
    }

    @Test
    public void clear() {
        storage.clear();
        Assert.assertEquals(0, storage.size());
    }

    @Test
    public void updateExistResume() {
        storage.update(RESUME_3);
        Assert.assertEquals(RESUME_3, storage.get(UUID_3));
    }

    @Test(expected = NotExistUuidException.class)
    public void updateNotExistResume() {
        storage.update(RESUME_4);
    }

    @Test(expected = NotExistUuidException.class)
    public void getNotExistResume() {
        storage.get(RESUME_4.getUuid());
    }

    @Test
    public void getExistResume() {
        Assert.assertEquals(RESUME_3, storage.get(UUID_3));
    }

    @Test
    public void getAllSorted() {
        List<Resume> sortedResume = new ArrayList<Resume>(Arrays.asList(RESUME_1,RESUME_2,RESUME_3));
        Collections.sort(sortedResume);
        Assert.assertEquals(sortedResume, storage.getAllSorted());
    }


    @Test
    public void size() {
        Assert.assertEquals(3,storage.size());
    }
}