package com.rwan.prometheus.client.converter.query;

import com.rwan.prometheus.client.converter.Data;

public class ScalarData extends QueryResultItemValue implements Data {

	public ScalarData(double timestamp, double value) {
		super(timestamp, value);
	}

}
