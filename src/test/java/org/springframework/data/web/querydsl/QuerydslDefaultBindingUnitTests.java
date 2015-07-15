/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.web.querydsl;

import static org.hamcrest.core.Is.*;

import java.util.Arrays;
import java.util.Collections;

import org.hamcrest.Matcher;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.querydsl.QUser;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;

/**
 * @author Christoph Strobl
 */
public class QuerydslDefaultBindingUnitTests {

	QuerydslDefaultBinding builder;

	@Before
	public void setUp() {
		builder = new QuerydslDefaultBinding();
	}

	/**
	 * @see DATACMNS-669
	 */
	@Test
	public void shouldCreatePredicateCorrectlyWhenPropertyIsInRoot() {

		Predicate predicate = builder.bind(QUser.user.firstname, Collections.singleton("tam"));

		assertThat(predicate, is(QUser.user.firstname.eq("tam")));
	}

	/**
	 * @see DATACMNS-669
	 */
	@Test
	public void shouldCreatePredicateCorrectlyWhenPropertyIsInNestedElement() {

		Predicate predicate = builder.bind(QUser.user.address.city, Collections.singleton("two rivers"));

		Assert.assertThat(predicate.toString(), is(QUser.user.address.city.eq("two rivers").toString()));
	}

	/**
	 * @see DATACMNS-669
	 */
	@Test
	public void shouldCreatePredicateCorrectlyWhenValueIsNull() {

		Predicate predicate = builder.bind(QUser.user.firstname, Collections.emptySet());

		assertThat(predicate, is(QUser.user.firstname.isNull()));
	}

	/**
	 * @see DATACMNS-669
	 */
	@Test
	public void shouldCreatePredicateWithContainingWhenPropertyIsCollectionLikeAndValueIsObject() {

		Predicate predicate = builder.bind(QUser.user.nickNames, Collections.singleton("dragon reborn"));

		assertThat(predicate, is(QUser.user.nickNames.contains("dragon reborn")));
	}

	/**
	 * @see DATACMNS-669
	 */
	@Test
	public void shouldCreatePredicateWithInWhenPropertyIsAnObjectAndValueIsACollection() {

		Predicate predicate = builder.bind(QUser.user.firstname, Arrays.asList("dragon reborn", "shadowkiller"));

		assertThat(predicate, is(QUser.user.firstname.in(Arrays.asList("dragon reborn", "shadowkiller"))));
	}

	/*
	 * just to satisfy generic type boundaries o_O
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void assertThat(Predicate predicate, Matcher<? extends Expression> matcher) {
		Assert.assertThat((Expression) predicate, Is.<Expression> is((Matcher<Expression>) matcher));
	}

}