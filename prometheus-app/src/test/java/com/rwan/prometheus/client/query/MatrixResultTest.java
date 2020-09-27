package com.rwan.prometheus.client.query;


import com.rwan.prometheus.client.converter.ConvertUtil;
import com.rwan.prometheus.client.converter.Data;
import com.rwan.prometheus.client.converter.query.DefaultQueryResult;
import com.rwan.prometheus.client.converter.query.ScalarData;
import junit.framework.TestCase;

public class MatrixResultTest extends TestCase {

	private String testScalarData="{\"status\":\"success\",\"data\":{\"resultType\":\"scalar\",\"result\":[1536200364.286,\"1\"]}}";

	public void testParser() {
		DefaultQueryResult<ScalarData> result = ConvertUtil.convertQueryResultString(testScalarData);
		System.out.println("-----" +result.getResult().size());
		for(Data data : result.getResult()) {
			System.out.println("=======>" + data);
		}
	}
}
