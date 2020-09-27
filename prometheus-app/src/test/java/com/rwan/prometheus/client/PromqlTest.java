package com.rwan.prometheus.client;

import com.rwan.prometheus.client.builder.*;
import com.rwan.prometheus.client.converter.ConvertUtil;
import com.rwan.prometheus.client.converter.am.DefaultAlertManagerResult;
import com.rwan.prometheus.client.converter.label.DefaultLabelResult;
import com.rwan.prometheus.client.converter.query.DefaultQueryResult;
import com.rwan.prometheus.client.converter.query.MatrixData;
import com.rwan.prometheus.client.converter.query.QueryResultItemValue;
import com.rwan.prometheus.client.converter.query.VectorData;
import com.rwan.prometheus.client.converter.status.DefaultConfigResult;
import com.rwan.prometheus.client.converter.target.DefaultTargetResult;
import junit.framework.TestCase;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PromqlTest extends TestCase {
//	private final static String TARGET_SERVER = "http://10.10.10.72:9090";
	private final static String TARGET_SERVER = "http://prometheus.mockuai.net";

	private RestTemplate template = null;

	@Override
	protected void setUp() throws Exception {
		HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		httpRequestFactory.setConnectTimeout(1000);
		httpRequestFactory.setReadTimeout(2000);
		HttpClient httpClient = HttpClientBuilder.create()
		 .setMaxConnTotal(100)
		 .setMaxConnPerRoute(10)
		 .build();
		httpRequestFactory.setHttpClient(httpClient);

		template = new RestTemplate(httpRequestFactory);
	}

	private static String ConvertEpocToFormattedDate(String format, double epocTime) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(new Date(Math.round(epocTime*1000)));
	}


	public void test22(){
		String str = "sum+by+%28instance%2Cresource%29+%28irate%28sentinel_pass_request_total%7Bapplication%3D%22ec-messagecenter%22%2Cinstance%3D%7E%22prod.*%22%7D%5B1m%5D%29%29&partial_response=true&dedup=true&time=1600242324.185&_=1600244320025&time=1600242324.185";

        str = "http://10.10.10.72:3000/api/datasources/proxy/1/api/v1/query_range?query=sum(rate(http_server_requests_seconds_sum%7Binstance!~%22prod.*%22%2Capplication%3D%22ec-riskcenter%22%7D%5B1m%5D))%2Fsum(rate(http_server_requests_seconds_count%7Binstance!~%22prod.*%22%2Capplication%3D%22ec-riskcenter%22%7D%5B1m%5D))&start=1600320980&end=1600331780&step=20";
	// 	System.out.println(URLDecoder.decode(str));



		str = "aa$dddhttp_requests_request_duration{endpoint=\"/cookies\"}dddd${},ddddd${aa}";

		str = str.replaceAll("\\$\\{aa\\}","ddddd");

		System.out.println(str);
	}


	public void test222(){

		byte[] body = new byte[]{123, 34, 105, 112, 34, 58, 34, 49, 57, 50, 46, 49, 54, 56, 46, 49, 49, 49, 46, 55, 54, 34, 125};

		System.out.println(new String(body));
	}
	public void test223(){

	    String uri = "http://10.10.10.72:9090/api/v1/query?query=sum+by%28instance%2Capplication%2Curi%29+%28irate%28http_server_requests_seconds_count%7Binstance%21%7E%22prod.*%22%2Capplication%3D%22ec-riskcenter%22%2C+uri%21%7E%22.*actuator.*%22%2Curi%3D%7E%22%2Fserver%2Fstatus%2Fcheck%22%7D%5B1m%5D%29%29+%3E+2.0000&step=60&max_source_resolution=0s&partial_response=true&dedup=true&_=1600859346189";


        Map<String,String> param = new HashMap<String, String>();
        param.put("query","");
//        param.put("")
        String rtVal = template.getForObject(uri, String.class,param);

        System.out.println(rtVal);

	}

	public void testSimpleRangeQuery2() throws MalformedURLException {


		String expression = "sum by (instance,resource) (irate(sentinel_pass_request_total{application=\"ec-messagecenter\",instance=~\"prod.*\"}[1m]))";
//		String expression = "sum(round(irate(http_server_requests_seconds_count{application=\"ec-riskcenter\", instance=\"beta.ec-risk001\", uri!~\".*actuator.*\"}[1m])))";
		RangeQueryBuilder rangeQueryBuilder =  QueryBuilderType.RangeQuery.newInstance(TARGET_SERVER);
		URI targetUri = rangeQueryBuilder.withQuery(expression)
				.withStartEpochTime(System.currentTimeMillis() / 1000 - 60*20)
				.withEndEpochTime(System.currentTimeMillis() / 1000)
				.withStepTime("60s")
				.build();

		System.out.println(targetUri.toURL().toString());

		String rtVal = template.getForObject(targetUri, String.class);


		System.out.println(rtVal);
		DefaultQueryResult<MatrixData> result = ConvertUtil.convertQueryResultString(rtVal);

		for(MatrixData matrixData : result.getResult()) {
			System.out.println(String.format("%s", matrixData.getMetric().get("instance")));
			for(QueryResultItemValue itemValue : matrixData.getDataValues()) {
				System.out.println(String.format("%s %10.2f ",
						ConvertEpocToFormattedDate("yyyy-MM-dd hh:mm:ss", itemValue.getTimestamp()),
						itemValue.getValue()
				));
			}
		}

	}




	public void testSimpleRangeQuery() throws MalformedURLException {
		RangeQueryBuilder rangeQueryBuilder =  QueryBuilderType.RangeQuery.newInstance(TARGET_SERVER);
		URI targetUri = rangeQueryBuilder.withQuery("100 - avg(rate(node_cpu{application=\"node_exporter\", mode=\"idle\"}[1m])) by (instance)*100")
		                 .withStartEpochTime(System.currentTimeMillis() / 1000 - 60*10)
		                 .withEndEpochTime(System.currentTimeMillis() / 1000)
		                 .withStepTime("60s")
		                 .build();

		System.out.println(targetUri.toURL().toString());

		String rtVal = template.getForObject(targetUri, String.class);




		DefaultQueryResult<MatrixData> result = ConvertUtil.convertQueryResultString(rtVal);

		for(MatrixData matrixData : result.getResult()) {
			System.out.println(String.format("%s", matrixData.getMetric().get("instance")));
			for(QueryResultItemValue itemValue : matrixData.getDataValues()) {
				System.out.println(String.format("%s %10.2f ",
						ConvertEpocToFormattedDate("yyyy-MM-dd hh:mm:ss", itemValue.getTimestamp()),
						itemValue.getValue()
						));
			}
		}

	}

	public void testSimpleVectorQuery() throws MalformedURLException {
		InstantQueryBuilder iqb = QueryBuilderType.InstantQuery.newInstance(TARGET_SERVER);
		URI targetUri = iqb.withQuery("node_cpu{application=\"node_exporter\", mode=\"idle\"}[1m]").build();



		String rtVal = template.getForObject(targetUri, String.class);


		DefaultQueryResult<MatrixData> result = ConvertUtil.convertQueryResultString(rtVal);


		for(MatrixData matrixData : result.getResult()) {
			System.out.println(String.format("%s", matrixData.getMetric().get("instance")));
			for(QueryResultItemValue itemValue : matrixData.getDataValues()) {
				System.out.println(String.format("%s %10.2f ",
						ConvertEpocToFormattedDate("yyyy-MM-dd hh:mm:ss", itemValue.getTimestamp()),
						itemValue.getValue()
						));
			}
		}

		System.out.println(targetUri.toURL().toString());
//		System.out.println(result);
	}

	public void testSimpleInstantQuery() throws MalformedURLException {
		InstantQueryBuilder iqb = QueryBuilderType.InstantQuery.newInstance(TARGET_SERVER);
		URI targetUri = iqb.withQuery("100 - avg(rate(node_cpu{application=\"node_exporter\", mode=\"idle\"}[1m])) by (instance)*100").build();
		System.out.println(targetUri.toURL().toString());


		String rtVal = template.getForObject(targetUri, String.class);


		DefaultQueryResult<VectorData> result = ConvertUtil.convertQueryResultString(rtVal);


		for(VectorData vectorData : result.getResult()) {
			System.out.println(String.format("%s %s %10.2f",
					vectorData.getMetric().get("instance"),
					vectorData.getFormattedTimestamps("yyyy-MM-dd hh:mm:ss"),
					vectorData.getValue() ));
		}

		System.out.println(result);
	}

	public void testSimpleLabel() throws MalformedURLException {
		LabelMetaQueryBuilder lmqb = QueryBuilderType.LabelMetadaQuery.newInstance(TARGET_SERVER);
		URI targetUri = lmqb.withLabel("pod").build();
		System.out.println(targetUri.toURL().toString());


		String rtVal = template.getForObject(targetUri, String.class);


		DefaultLabelResult result = ConvertUtil.convertLabelResultString(rtVal);


		System.out.println(result);
	}

	public void testSimpleConfig() throws MalformedURLException {
		StatusMetaQueryBuilder smqb = QueryBuilderType.StatusMetadaQuery.newInstance(TARGET_SERVER);
		URI targetUri = smqb.build();
		System.out.println(targetUri.toURL().toString());


		String rtVal = template.getForObject(targetUri, String.class);


		DefaultConfigResult result = ConvertUtil.convertConfigResultString(rtVal);


		System.out.println(result);
	}

	public void testSimpleTargets() throws MalformedURLException {
		TargetMetaQueryBuilder tmqb = QueryBuilderType.TargetMetadaQuery.newInstance(TARGET_SERVER);
		URI targetUri = tmqb.build();
		System.out.println(targetUri.toURL().toString());


		String rtVal = template.getForObject(targetUri, String.class);


		DefaultTargetResult result = ConvertUtil.convertTargetResultString(rtVal);


		System.out.println(result);
	}

	public void testSimpleAlertManager() throws MalformedURLException {
		AlertManagerMetaQueryBuilder ammqb = QueryBuilderType.AlertManagerMetadaQuery.newInstance(TARGET_SERVER);
		URI targetUri = ammqb.build();
		System.out.println(targetUri.toURL().toString());


		String rtVal = template.getForObject(targetUri, String.class);


		DefaultAlertManagerResult result = ConvertUtil.convertAlertManagerResultString(rtVal);


		System.out.println(result);
	}




}
