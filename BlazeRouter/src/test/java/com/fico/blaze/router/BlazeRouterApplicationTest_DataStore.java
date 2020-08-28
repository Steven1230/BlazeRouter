package com.fico.blaze.router;

import com.fico.blaze.com.fico.blaze.data.com.fico.blaze.data.service.BlazeDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BlazeRouterApplicationTest_DataStore {

	@Autowired
	BlazeDataService blazeDataService;

	@Test
	void testNameSpaceXML() throws Exception {
		int insertResult = blazeDataService.insertBlazeData("ideInsert", "{}", "{}");
		System.out.println(insertResult);
	}

}
