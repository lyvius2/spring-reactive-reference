package com.walter.reactive.model;

import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.List;

public class SampleData {
	public static final List<Tuple2<Integer, Long>> BTC_TOP_PRICES_PER_YEAR = List.of(
			Tuples.of(2010, 565L),
			Tuples.of(2011, 36_094L),
			Tuples.of(2012, 17_425L),
			Tuples.of(2013, 1_405_209L),
			Tuples.of(2014, 1_237_182L),
			Tuples.of(2015, 557_603L),
			Tuples.of(2016, 1_111_811L),
			Tuples.of(2017, 22_483_583L),
			Tuples.of(2018, 19_521_543L),
			Tuples.of(2019, 15_761_568L),
			Tuples.of(2020, 22_439_002L),
			Tuples.of(2021, 63_364_000L)
	);
}
