package com.aa.crewcomp.jobs.service;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import java.util.ArrayList;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.mongodb.BulkOperationException;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.aa.crewcomp.jobs.impl.Cc2JobRepositoryImpl;
import com.mongodb.bulk.BulkWriteError;
import com.mongodb.bulk.BulkWriteResult;

@RunWith(MockitoJUnitRunner.class)
public class MongoBulkInsertServiceTests {
    class MyEntity{

    }
    class MyBulkInsertServiceImpl extends MongoBulkInsertService<MyEntity>{
        public MyBulkInsertServiceImpl(Class<MyEntity> entityClassType) {
            super(entityClassType);
        }
    }

    @InjectMocks
    private MyBulkInsertServiceImpl myBulkInsertServiceImpl;

    @Mock
    private MongoTemplate outputMongoTemplate;

	@Mock
    private Cc2JobRepositoryImpl cc2JobRepository;

    @BeforeEach
    public void setUp(){
        this.myBulkInsertServiceImpl = new MyBulkInsertServiceImpl(MyEntity.class);
        this.cc2JobRepository = mock(Cc2JobRepositoryImpl.class);
		MockitoAnnotations.openMocks(this);
        BulkWriteResult results = mock(BulkWriteResult.class);
		BulkOperations bulkOps = mock(BulkOperations.class);
		Mockito.doReturn(null).when(bulkOps).insert(anyList());
		Mockito.doReturn(results).when(bulkOps).execute();
		Mockito.doReturn(bulkOps).when(this.outputMongoTemplate).bulkOps(BulkOperations.BulkMode.UNORDERED,
				MyEntity.class);
    }

    @Test
    public void testSave(){
        this.myBulkInsertServiceImpl.save(new ObjectId(), new MyEntity());
    }

    @Test
	public void testInsertMany() throws Exception {
		for (int i = 0; i < this.myBulkInsertServiceImpl.CHUNK_SIZE + 10; i++)
			this.myBulkInsertServiceImpl.save(null, new MyEntity());
		this.myBulkInsertServiceImpl.flush(null);
	}

	@Test
	public void testInsertNone() throws Exception {
		this.myBulkInsertServiceImpl.flush(null);
	}

	@Test
	public void testInsertManyWithDelay() throws Exception {
		this.myBulkInsertServiceImpl.save(null, new MyEntity());
		Thread.sleep(10000);
		this.myBulkInsertServiceImpl.save(null, new MyEntity());
		this.myBulkInsertServiceImpl.flush(null);
	}

	@Test
	public void testInsertException() throws Exception {
		BulkWriteResult results = mock(BulkWriteResult.class);
		BulkOperationException exception = mock(BulkOperationException.class);
		Mockito.doReturn(results).when(exception).getResult();
		Mockito.doReturn(new ArrayList<BulkWriteError>()).when(exception).getErrors();
		BulkOperations bulkOps = mock(BulkOperations.class);
		Mockito.doReturn(results).when(bulkOps).execute();
		Mockito.doReturn(bulkOps).when(this.outputMongoTemplate).bulkOps(BulkOperations.BulkMode.UNORDERED,
				MyEntity.class);
		Mockito.doThrow(exception).when(bulkOps).execute();
		this.myBulkInsertServiceImpl.save(null, new MyEntity());
		this.myBulkInsertServiceImpl.flush(null);
	}
}
